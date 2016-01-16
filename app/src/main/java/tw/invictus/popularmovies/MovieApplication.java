package tw.invictus.popularmovies;

import android.app.Application;

import tw.invictus.popularmovies.di.ApiModule;
import tw.invictus.popularmovies.di.DaggerDetailFragmentComponent;
import tw.invictus.popularmovies.di.DaggerMainFragmentComponent;
import tw.invictus.popularmovies.di.DbModule;
import tw.invictus.popularmovies.di.DetailFragmentComponent;
import tw.invictus.popularmovies.di.DetailFragmentModule;
import tw.invictus.popularmovies.di.MainFragmentComponent;
import tw.invictus.popularmovies.di.MainFragmentModule;
import tw.invictus.popularmovies.view.fragment.DetailFragment;
import tw.invictus.popularmovies.view.fragment.MainFragment;

/**
 * Created by ivan on 12/26/15.
 */
public class MovieApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public MainFragmentComponent getMainFragmentComponent(MainFragment mainFragment) {
        return DaggerMainFragmentComponent.builder()
                .apiModule(new ApiModule())
                .dbModule(new DbModule(mainFragment.getContext()))
                .mainFragmentModule(new MainFragmentModule(mainFragment.getContext())).build();
    }

    public DetailFragmentComponent getDetailFragmentComponent(DetailFragment detailFragment){
        return DaggerDetailFragmentComponent.builder()
                .apiModule(new ApiModule())
                .dbModule(new DbModule(detailFragment.getContext()))
                .detailFragmentModule(new DetailFragmentModule(detailFragment.getContext())).build();
    }
}
