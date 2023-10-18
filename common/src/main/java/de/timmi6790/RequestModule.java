package de.timmi6790;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import javax.inject.Singleton;

@Module
public class RequestModule {
    @Provides
    @Singleton
    public OkHttpClient getHttpClient() {
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
