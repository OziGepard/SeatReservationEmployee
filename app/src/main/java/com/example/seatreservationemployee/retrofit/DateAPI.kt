package com.example.seatreservationemployee.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface DateAPI {

    @GET("api/timezone/Europe/Warsaw")
    fun getActualDate(): Call<DateResponse>
}