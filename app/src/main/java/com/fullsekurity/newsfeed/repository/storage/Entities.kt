package com.fullsekurity.newsfeed.repository.storage

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meaning(

    @SerializedName(value = "source") var source: Source,
    @SerializedName(value = "author") var author: String,
    @SerializedName(value = "title") var title: String,
    @SerializedName(value = "description") var description: String,
    @SerializedName(value = "url") var url: String,
    @SerializedName(value = "urlToImage") var urlToImage: String,
    @SerializedName(value = "publishedAt") var publishedAt: String,
    @SerializedName(value = "content") var content: String

) : Parcelable

@Parcelize
data class Source(

    @SerializedName(value = "id") var id: String = "",
    @SerializedName(value = "name") var name: String = ""

) : Parcelable