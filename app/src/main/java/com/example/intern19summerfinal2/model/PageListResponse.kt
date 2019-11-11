package com.example.intern19summerfinal2.model

import com.google.gson.annotations.SerializedName

class PageListResponse (
    @SerializedName("images")
    var pages: ArrayList<ArrayList<String>>?
)