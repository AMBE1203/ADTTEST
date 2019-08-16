package com.ambe.adttest.interfaces

import android.content.Context
import com.ambe.adttest.helper.Const
import com.ambe.adttest.helper.Utils
import com.ambe.adttest.model.Homes
import com.ambe.adttest.model.Response
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  Created by AMBE on 15/8/2019 at 13:57 PM.
 */
interface NetworkService {

    //
    @GET("api/v1/home?deviceId=33&orientation=0")
    fun  getHomes(): Observable<Response>



    companion object {
        fun getService(context: Context, token: String): NetworkService {
            val cacheSize = (5 * 1024 * 1024).toLong()
            val myCache = Cache(context.cacheDir, cacheSize)

            val okHttpClient = OkHttpClient.Builder()
                .cache(myCache)
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (Utils.checkInternetConnection(context))
                        request.newBuilder().header("Authorization", "Bearer $token").header(
                            "Cache-Control",
                            "public, max-age=" + 5
                        ).build() //      .header("Authorization", "Bearer " + token)

                    else
                        request.newBuilder().removeHeader("Authorization").header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                        ).build()
                    chain.proceed(request)
                }
                .build()


            val retrofit = Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(NetworkService::class.java)
        }
    }
}