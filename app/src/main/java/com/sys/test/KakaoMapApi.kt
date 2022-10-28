package com.sys.test

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoMapApi {
    @GET("searchList?apiKey=rrq71a2rotyj9tqm&locale=kr&page=1")
    fun getData() : Call<Item>
}