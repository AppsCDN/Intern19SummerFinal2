package com.example.intern19summerfinal2.utils

import com.example.intern19summerfinal2.model.MangaListResponse
import retrofit2.http.GET
import retrofit2.Call

interface MangaAPI {
    @GET("api/list/0")
    fun getResponse(): Call<MangaListResponse>
}
