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

object APIClient {
    val client: APIInterface
        get() {
            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    LogUtils.D(APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(API), String.format("okHttp logging interceptor=%s", message))
                }
            })
            interceptor.level = HttpLoggingInterceptor.Level.BODY  // BASIC or BODY
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
        for (index in 0 until jsonArray.length()) {
            val stringJson = jsonArray[index].toString()
            val jsonObject = JSONObject(stringJson)
            jsonObject.get("author")?.let {
                if (it.toString().isEmpty() || it.toString() == "null") {
                    (jsonArray[index] as JSONObject).put("author", "AUTHOR MISSING")
                }
            } ?: run {
                (jsonArray[index] as JSONObject).put("author", "AUTHOR MISSING")
            }
            jsonArray
        }
        return jsonArray
    }
}
