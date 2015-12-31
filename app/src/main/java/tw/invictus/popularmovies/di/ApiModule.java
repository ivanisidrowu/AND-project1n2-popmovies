package tw.invictus.popularmovies.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import tw.invictus.popularmovies.BuildConfig;
import tw.invictus.popularmovies.model.api.RestfulApi;

/**
 * Created by ivan on 12/26/15.
 */
@Module
public class ApiModule {

    public ApiModule(){

    }

    @Singleton
    @Provides
    RestfulApi provideRestfulApi(){
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestfulApi.class);
    }
}
