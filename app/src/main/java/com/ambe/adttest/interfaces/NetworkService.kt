package com.ambe.adttest.interfaces

import android.content.Context
import com.ambe.adttest.helper.Const
import com.ambe.adttest.helper.Utils
import com.ambe.adttest.model.Response
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


/**
 *  Created by AMBE on 15/8/2019 at 13:57 PM.
 */
interface NetworkService {

    //
    @GET("api/v1/home?deviceId=33&orientation=0")
    fun getHomes(): Observable<Response>


    companion object {


        fun getService(context: Context, token: String): NetworkService {
            val cacheSize = (5 * 1024 * 1024).toLong()

            val myCache = Cache(context.cacheDir, cacheSize)


            val okHttpClient = OkHttpClient.Builder()
                .cache(myCache)

                .addNetworkInterceptor(MyInterceptor(context).REWRITE_RESPONSE_INTERCEPTOR)
                .addInterceptor(MyInterceptor(context).REWRITE_RESPONSE_INTERCEPTOR_OFFLINE)

                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (Utils.checkInternetConnection(context))
                        request.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                            .header("Authorization", "Bearer $token").header(
                                "Cache-Control",
                                "public, max-age=" + 5
                            ).build()
                    else
                        request.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                            .header("Authorization", "Bearer $token").header(
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