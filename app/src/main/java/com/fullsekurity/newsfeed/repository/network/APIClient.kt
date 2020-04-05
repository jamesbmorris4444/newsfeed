package com.fullsekurity.newsfeed.repository.network

import com.fullsekurity.newsfeed.logger.LogUtils
import com.fullsekurity.newsfeed.logger.LogUtils.TagFilter.API
import com.fullsekurity.newsfeed.utils.Constants.URBANDICT_BASE_URL
import com.fullsekurity.newsfeed.utils.Constants.URBANDICT_LIST_CLASS_TYPE
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*

object APIClient {
    val client: APIInterface
        get() {
            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    LogUtils.D(APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(API), String.format("okHttp logging interceptor=%s", message))
                }
            })
            interceptor.level = HttpLoggingInterceptor.Level.BASIC  // BASIC or BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(TransformInterceptor())
                .build()
            val gson = GsonBuilder()
                .registerTypeAdapter(URBANDICT_LIST_CLASS_TYPE,
                    MeaningsJsonDeserializer()
                )
                .create()
            val builder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(URBANDICT_BASE_URL)
            return builder.build().create(APIInterface::class.java)
        }

}

class TransformInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val stringJson = response.body?.string()
        return if (stringJson == null) {
            response
        } else {
            val jsonObject = JSONObject(stringJson)
            val jsonArray = jsonObject.get("articles") as JSONArray
            jsonObject.remove("articles")
            jsonObject.put("articles", replaceAuthor(jsonArray))
            val contentType = response.body?.contentType()
            val body: ResponseBody = ResponseBody.create(contentType, jsonObject.toString())
            response.newBuilder().body(body).build()
        }
    }

    private fun replaceAuthor(jsonArray: JSONArray) : JSONArray {
        val listNumber: MutableList<Int> = mutableListOf()
        val random = Random()
        var company: String
        for (index in 0 until jsonArray.length()) {
            val stringJson = jsonArray[index].toString()
            val jsonObject = JSONObject(stringJson)
            var rand: Int
            while (true) {
                rand = random.nextInt(jsonArray.length())
                if (!listNumber.contains(rand)) {
                    listNumber.add(rand)
                    break
                }
            }
            company = when (rand) {
                0 -> { "Amazon" }
                1 -> { "Best Buy" }
                2 -> { "Hobby Lobby" }
                3 -> { "Michaels" }
                4 -> { "Dillards" }
                5 -> { "Nordstrom" }
                6 -> { "Macys" }
                7 -> { "Sylvesters" }
                8 -> { "Tommy Bahama" }
                9 -> { "Sears" }
                10 -> { "EBay" }
                11 -> { "Lowes" }
                12 -> { "Home Depot" }
                13 -> { "Office Depot" }
                14 -> { "Trulucks" }
                15 -> { "Cheesecake Factory" }
                16 -> { "P F Changs" }
                17 -> { "Red Lobster" }
                18 -> { "Mings" }
                19 -> { "BMW" }
                else -> { "Zulu" }
            }
            jsonObject.get("author")?.let {
                (jsonArray[index] as JSONObject).put("author", company)
            } ?: run {
                (jsonArray[index] as JSONObject).put("author", "NO COMPANY")
            }
            val percentNumeratorOuter = random.nextInt(9)
            jsonObject.get("title")?.let {
                (jsonArray[index] as JSONObject).put("title", percentNumeratorOuter.toString())
            } ?: run {
                (jsonArray[index] as JSONObject).put("title", "-1")
            }
            val percentNumeratorInner = random.nextInt(9)
            jsonObject.get("description")?.let {
                (jsonArray[index] as JSONObject).put("description", percentNumeratorInner.toString())
            } ?: run {
                (jsonArray[index] as JSONObject).put("description", "-1")
            }
            jsonArray
        }
        return jsonArray
    }
}
