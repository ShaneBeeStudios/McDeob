package com.shanebeestudios.mcdeop.util;

import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestUtil {
    public static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Request requestWithUserAgent = originalRequest
                        .newBuilder()
                        .header("User-Agent", "McDeob")
                        .build();

                return chain.proceed(requestWithUserAgent);
            })
            .build();
}
