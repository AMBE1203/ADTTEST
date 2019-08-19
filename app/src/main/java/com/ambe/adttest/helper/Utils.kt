package com.ambe.adttest.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 *  Created by AMBE on 16/8/2019 at 8:27 AM.
 */
object Utils {
    @JvmStatic
    fun checkInternetConnection(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity == null) {
            return false
        } else {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (anInfo in info) {
                    if (anInfo.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }
}