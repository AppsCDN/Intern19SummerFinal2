package com.example.intern19summerfinal2.model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class MangaListResponse(
    var end: Int,
    @SerializedName("manga")
    var manga: ArrayList<Manga>?,
    var page: Int,
    var start: Int,
    var total: Int
)
