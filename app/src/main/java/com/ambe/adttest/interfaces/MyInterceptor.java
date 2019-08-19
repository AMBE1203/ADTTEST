package com.ambe.adttest.interfaces;

import android.content.Context;
import android.util.Log;

import com.ambe.adttest.helper.Const;
import com.ambe.adttest.helper.Utils;

import okhttp3.Interceptor;
import okhttp3.Request;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by AMBE on 19/8/2019 at 11:02 AM.
 */
public class MyInterceptor {

    private Context context;

    public MyInterceptor(Context context) {
        this.context = context;

    }

    public final Interceptor REWRITE_RESPONSE_INTERCEPTOR = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            String cacheControl = originalResponse.header("Cache-Control");
            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {

                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .addHeader("Authorization", "Bearer " + Const.TOKEN)
                        .header("Cache-Control", "public, max-age=" + 5000)
                        .build();
            } else {

                return originalResponse.newBuilder().header("Authorization", "Bearer " + Const.TOKEN).build();
            }
        }
    };

    public final Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!Utils.checkInternetConnection(context)) {

                request = request.newBuilder()
                        .removeHeader("Pragma")
                        .addHeader("Authorization", "Bearer " + Const.TOKEN)
                        .header("Authorization", "Bearer " + Const.TOKEN)
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                        .build();
            }
            return chain.proceed(request);
        }
    };
}
