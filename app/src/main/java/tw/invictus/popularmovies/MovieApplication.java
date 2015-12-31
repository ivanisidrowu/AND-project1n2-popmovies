package tw.invictus.popularmovies;

import android.app.Application;

import tw.invictus.popularmovies.di.ApiModule;
import tw.invictus.popularmovies.di.DaggerMainActivityComponent;
import tw.invictus.popularmovies.di.MainActivityComponent;
import tw.invictus.popularmovies.di.MainActivityModule;
import tw.invictus.popularmovies.view.MainActivity;

/**
 * Created by ivan on 12/26/15.
 */
public class MovieApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public MainActivityComponent getMainActivityComponent(MainActivity activity){
        return DaggerMainActivityComponent.builder().apiModule(new ApiModule()).mainActivityModule(new MainActivityModule(activity)).build();
    }
}
