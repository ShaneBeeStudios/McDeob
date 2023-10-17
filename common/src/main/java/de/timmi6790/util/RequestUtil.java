package de.timmi6790.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {
    public static OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    final Request originalRequest = chain.request();
                    final Request requestWithUserAgent = originalRequest
                            .newBuilder()
                            .header("User-Agent", "McDeob")
                            .build();

                    return chain.proceed(requestWithUserAgent);
                })
                .build();
    }
}
