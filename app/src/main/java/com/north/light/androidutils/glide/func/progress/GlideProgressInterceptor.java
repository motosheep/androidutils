package com.north.light.androidutils.glide.func.progress;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * FileName: GlideProgressInterceptor
 * Author: lzt
 * Date: 2022/6/8 16:32
 */

public class GlideProgressInterceptor implements Interceptor {
//
//    private static final Map<String, GlideProgressListener> LISTENER_MAP = new HashMap<>();
//
//    public static Map<String, GlideProgressListener> getListenerMap() {
//        return LISTENER_MAP;
//    }
//
//    public static void addListener(String url, GlideProgressListener glideProgressListener) {
//        LISTENER_MAP.put(url, glideProgressListener);
//    }
//
//    public static void removeListener(String url) {
//        LISTENER_MAP.remove(url);
//    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        String url = request.url().toString();
        ResponseBody responseBody = response.body();

        Response newResponse = response.newBuilder().body(
                new GlideProgressResponseBody(url, responseBody)).build();

        return newResponse;
    }
}
