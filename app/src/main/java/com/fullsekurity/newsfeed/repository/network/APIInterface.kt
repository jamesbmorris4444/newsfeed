package com.fullsekurity.newsfeed.repository.network

import com.fullsekurity.newsfeed.repository.storage.Meaning
import com.fullsekurity.newsfeed.utils.Constants
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("top-headlines")
    fun getMeanings(
        @Query(Constants.NEWSFEED_COUNTRY) country: String,
        @Query(Constants.NEWSFEED_KEY) key: String
    ): Flowable<DonorResponse>
}

data class DonorResponse (
    val status: String,
    val totalResults: Int,
    val articles: List<Meaning>
)