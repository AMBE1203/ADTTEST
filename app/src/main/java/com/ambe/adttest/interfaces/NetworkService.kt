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
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import android.net.NetworkInfo
import android.support.v4.content.ContextCompat.getSystemService
import android.net.ConnectivityManager


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


            val OFFLINE_INTERCEPTOR = Interceptor { chain ->
                var request = chain.request()
                if (!Utils.checkInternetConnection(context)) {
                    val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                    request = request.newBuilder()
                            .header("Authorization", "Bearer $token")
                            .header("cache-control", "public, only-if-cached, max-stale=$maxStale")
                            .build()
                }
                chain.proceed(request)
            }

            val REWRITE_RESPONSE_INTERCEPTOR = Interceptor { chain ->
                var originalResponse = chain.proceed(chain.request())
                val cacheControl = originalResponse.header("cache-control")

                return@Interceptor if ((cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0"))) {
                    originalResponse.newBuilder()
                            .header("Authorization", "Bearer $token")

                            .header("cache-control", "public, max-age=" + 10)
                            .build()
                } else {
                    originalResponse
                }
            }

            val okHttpClient = OkHttpClient.Builder()
                    .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                    .addInterceptor(OFFLINE_INTERCEPTOR)
                    .cache(myCache)

//                    .addInterceptor { chain ->
//                        var request = chain.request()
//                        request = if (Utils.checkInternetConnection(context))
//                            request.newBuilder().removeHeader("Pragma")
//                                    .removeHeader("Cache-Control").header("Authorization", "Bearer $token").header(
//                                            "Cache-Control",
//                                            "public, max-age=" + 5
//                                    ).build() //      .header("Authorization", "Bearer " + token)
//
//                        else
//                            request.newBuilder().removeHeader("Pragma")
//                                    .removeHeader("Cache-Control").header("Authorization", "Bearer $token").header(
//                                            "Cache-Control",
//                                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
//                                    ).build()
//                        chain.proceed(request)
//                    }
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