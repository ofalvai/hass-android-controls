package com.example.hasscontrolsprovider.network

import com.example.hasscontrolsprovider.network.request.BrightnessRequest
import com.example.hasscontrolsprovider.network.request.TurnOffRequest
import com.example.hasscontrolsprovider.network.request.TurnOnRequest
import com.example.hasscontrolsprovider.network.response.HassState
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HassRestService {

    @GET("/api/states")
    fun getStates(): Single<List<HassState>>

    @POST("/api/services/switch/turn_on")
    fun switchTurnOn(@Body requestData: TurnOnRequest): Completable

    @POST("/api/services/switch/turn_off")
    fun switchTurnOff(@Body requestData: TurnOffRequest): Completable

    @POST("/api/services/light/turn_on")
    fun lightTurnOn(@Body requestData: TurnOnRequest): Completable

    @POST("/api/services/light/turn_off")
    fun lightTurnOff(@Body requestData: TurnOffRequest): Completable

    @POST("/api/services/light/turn_on")
    fun setLightBrightness(@Body requestData: BrightnessRequest): Completable

}