package tw.invictus.popularmovies.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tw.invictus.popularmovies.model.db.RealmService;
import tw.invictus.popularmovies.model.db.RealmServiceImpl;

/**
 * Created by ivan on 1/9/16.
 */
@Module
public class DbModule {
    private Context context;

    public DbModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    RealmService provideRealmService(){
        return new RealmServiceImpl(context);
    }
}
