package de.timmi6790.launchermeta;

import dagger.Module;
import dagger.Provides;
import de.timmi6790.RequestModule;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;

@Module(includes = RequestModule.class)
public class LauncherMetaModule {
    @Provides
    @Singleton
    public LauncherMeta getLauncherMeta(final OkHttpClient httpClient) {
        return new LauncherMeta(httpClient);
    }
}
