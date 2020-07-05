package com.example.hasscontrolsprovider

import android.os.Build
import android.service.controls.Control
import android.service.controls.ControlsProviderService
import android.service.controls.actions.BooleanAction
import android.service.controls.actions.ControlAction
import android.service.controls.actions.FloatAction
import androidx.annotation.RequiresApi
import com.example.hasscontrolsprovider.mapper.toHassControl
import com.example.hasscontrolsprovider.mapper.toStatefulControl
import com.example.hasscontrolsprovider.mapper.toStatelessControl
import com.example.hasscontrolsprovider.network.request.BrightnessRequest
import com.example.hasscontrolsprovider.network.request.TurnOffRequest
import com.example.hasscontrolsprovider.network.request.TurnOnRequest
import com.example.hasscontrolsprovider.network.response.EntityType
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.FlowAdapters
import timber.log.Timber
import java.util.concurrent.Flow
import java.util.function.Consumer
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.R)
class HassControlService : ControlsProviderService() {

    private val hassRestService = Dependencies.hassRestService
    private val hassWebSocketClient = Dependencies.hassWebSocketClient
    private val compositeDisposable = CompositeDisposable()

    private companion object {
        val FILTERED_TYPES =
            setOf(EntityType.LIGHT, EntityType.VACUUM, EntityType.SWITCH, EntityType.CAMERA)
    }

    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> {
        val controlStream: Flowable<Control> = hassRestService.getStates()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Timber.e(it.toString()) }
            .toObservable()
            .flatMapIterable { it } // List of controls -> stream of controls
            .filter { FILTERED_TYPES.contains(it.entityType) }
            .map { it.toHassControl().toStatelessControl(this) }
            .toFlowable(BackpressureStrategy.BUFFER)

        return FlowAdapters.toFlowPublisher(controlStream)
    }

    override fun createPublisherFor(controlIds: MutableList<String>): Flow.Publisher<Control> {
        // TODO: in case of error, return a Control with Status.ERROR or something similar
        // TODO: when a requested control is not in the response, return a Control with Status.UNAVAILABLE

        val controlStream: Flowable<Control> = hassRestService.getStates()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Timber.e(it.toString()) }
            .toObservable()
            .flatMapIterable { it } // List of controls -> stream of controls
            .mergeWith(hassWebSocketClient.stateUpdateSubject)
            .filter { controlIds.contains(it.entity_id) }
            .map { it.toHassControl().toStatefulControl(this) }
            .toFlowable(BackpressureStrategy.BUFFER)

        hassWebSocketClient.connect()

        return FlowAdapters.toFlowPublisher(controlStream)
    }

    override fun performControlAction(
        controlId: String,
        action: ControlAction,
        consumer: Consumer<Int>
    ) {
        if (controlId.startsWith("switch.") && action is BooleanAction) {
            if (action.newState) {
                turnSwitchOn(controlId, consumer)
            } else {
                turnSwitchOff(controlId, consumer)
            }
        } else if (controlId.startsWith("light.")) {
            if (action is BooleanAction) {
                if (action.newState) {
                    turnLightOn(controlId, consumer)
                } else {
                    turnLightOff(controlId, consumer)
                }
            } else if (action is FloatAction) {
                setLightBrightness(controlId, action.newValue.roundToInt(), consumer)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun callService(serviceCall: Completable, resultConsumer: Consumer<Int>) {
        serviceCall
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resultConsumer.accept(ControlAction.RESPONSE_OK)
            }, {
                resultConsumer.accept(ControlAction.RESPONSE_FAIL)
                Timber.e(it.toString())
            })
            .addTo(compositeDisposable)
    }

    private fun turnSwitchOff(controlId: String, resultConsumer: Consumer<Int>) {
        val serviceCall = hassRestService.switchTurnOff(TurnOffRequest(controlId))
        callService(serviceCall, resultConsumer)
    }

    private fun turnSwitchOn(controlId: String, resultConsumer: Consumer<Int>) {
        val serviceCall = hassRestService.switchTurnOn(TurnOnRequest(controlId))
        callService(serviceCall, resultConsumer)
    }

    private fun turnLightOff(controlId: String, resultConsumer: Consumer<Int>) {
        val serviceCall = hassRestService.lightTurnOff(TurnOffRequest(controlId))
        callService(serviceCall, resultConsumer)
    }

    private fun turnLightOn(controlId: String, resultConsumer: Consumer<Int>) {
        val serviceCall = hassRestService.lightTurnOn(TurnOnRequest(controlId))
        callService(serviceCall, resultConsumer)
    }

    private fun setLightBrightness(
        controlId: String,
        brightnessPercent: Int,
        resultConsumer: Consumer<Int>
    ) {
        val brightnessRequest = BrightnessRequest(controlId, brightnessPercent)
        val serviceCall = hassRestService.setLightBrightness(brightnessRequest)
        callService(serviceCall, resultConsumer)
    }
}