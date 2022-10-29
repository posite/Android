package com.sys.test.network

import retrofit2.Call
import retrofit2.http.GET

interface KakaoMapApi {
    @GET("searchList?apiKey=rrq71a2rotyj9tqm&locale=kr&page=1")
    fun getData() : Call<Monttak>
}