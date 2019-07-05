package com.example.newsfortelegram.models

import com.google.gson.annotations.SerializedName


data class NewsModel(



    @SerializedName("name") val name:String,
    @SerializedName("image") val image:String,
    @SerializedName("link") val link:String


)












