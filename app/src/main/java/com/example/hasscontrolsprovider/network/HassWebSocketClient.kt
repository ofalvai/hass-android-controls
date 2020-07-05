package com.example.hasscontrolsprovider.network

import com.example.hasscontrolsprovider.network.request.websocket.AuthRequest
import com.example.hasscontrolsprovider.network.request.websocket.SubscribeEventRequest
import com.example.hasscontrolsprovider.network.response.*
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import io.reactivex.subjects.PublishSubject
import okhttp3.*
import timber.log.Timber

private const val URL_PART_WEBSOCKET = "/api/websocket"

class HassWebSocketClient(
    private val config: Config,
    private val okHttpClient: OkHttpClient,
    moshi: Moshi
) : WebSocketListener() {

    class Config(val baseUrl: String, val authToken: String)

    enum class State {
        CLOSED,
        OPEN,
        CLOSING,
        FAILED,

        AUTH_REQUIRED,
        AUTH_INVALID,
        AUTH_OK,
        COMMAND_PHASE,
    }

    // TODO: emit errors too
    val stateUpdateSubject = PublishSubject.create<HassState>()

    private var webSocket: WebSocket? = null

    private var state = State.CLOSED
        set(value) {
            Timber.d("WebSocket state: $field -> $value")
            field = value
        }
    private var interactionCounter = 0

    private val responseAdapter = moshi.adapter(WebSocketResponse::class.java)
    private val authRequestAdapter = moshi.adapter(AuthRequest::class.java)
    private val subscribeRequestAdapter = moshi.adapter(SubscribeEventRequest::class.java)
    private val resultResponseAdapter = moshi.adapter(ResultResponse::class.java)
    private val errorResultResponseAdapter = moshi.adapter(ErrorResultResponse::class.java)
    private val eventResponseAdapter = moshi.adapter(EventResponse::class.java)
    private val stateChangeEventResponseAdapter =
        moshi.adapter(StateChangeEventResponse::class.java)

    fun connect() {
        val request = Request.Builder()
            .get()
            .url(config.baseUrl + URL_PART_WEBSOCKET)
            .build()

        webSocket = okHttpClient.newWebSocket(request, this)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        state = State.CLOSED
        interactionCounter = 0
        Timber.e("WebSocket onClosed: code=$code, reason=$reason")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        state = State.CLOSING
        Timber.e("WebSocket onClosing: code=$code, reason=$reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        state = State.FAILED
        interactionCounter = 0
        Timber.e(t, "WebSocket onFailure: response: ${response.toString()}")
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        state = State.OPEN
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Timber.d("onMessage: $text")

        try {
            val message = responseAdapter.nonNull().fromJson(text)!!
            when (message.type) {
                WebSocketResponseType.auth_required -> {
                    state = State.AUTH_REQUIRED
                    authenticate()
                }
                WebSocketResponseType.auth_invalid -> {
                    state = State.AUTH_INVALID
                }
                WebSocketResponseType.auth_ok -> {
                    state = State.AUTH_OK
                    subscribeToStateChanges()
                }
                WebSocketResponseType.result -> {
                    state = State.COMMAND_PHASE
                    val result = resultResponseAdapter.nonNull().fromJson(text)!!
                    if (!result.success) {
                        val errorResult = errorResultResponseAdapter.nonNull().fromJson(text)!!
                        handleErrorResponse(errorResult)
                    }
                }
                WebSocketResponseType.event -> {
                    state = State.COMMAND_PHASE
                    handleEventResponse(text)
                }
            }
        } catch (ex: JsonDataException) {
            Timber.e(ex)
        }
    }

    private fun authenticate() {
        interactionCounter++
        val authRequest = AuthRequest(config.authToken)
        webSocket?.send(authRequestAdapter.toJson(authRequest))
    }

    private fun subscribeToStateChanges() {
        interactionCounter++
        val subscribeRequest = SubscribeEventRequest(
            interactionCounter,
            WebSocketEventType.state_changed
        )
        webSocket?.send(subscribeRequestAdapter.toJson(subscribeRequest))
    }

    private fun handleErrorResponse(errorResult: ErrorResultResponse) {
        val code = errorResult.error.code
        val message = errorResult.error.message
        Timber.w("Error response, code=$code, message=$message")
    }

    private fun handleEventResponse(jsonString: String) {
        val eventResponse = eventResponseAdapter.nonNull().fromJson(jsonString)!!
        when (eventResponse.event.event_type) {
            // We only subscribed for state_changed
            WebSocketEventType.state_changed -> {
                val stateChangeEvent =
                    stateChangeEventResponseAdapter.nonNull().fromJson(jsonString)!!
                val newState = stateChangeEvent.event.data.new_state
                stateUpdateSubject.onNext(newState)
            }
        }
    }
}