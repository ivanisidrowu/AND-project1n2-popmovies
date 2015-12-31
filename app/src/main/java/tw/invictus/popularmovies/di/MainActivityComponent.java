package tw.invictus.popularmovies.di;

import javax.inject.Singleton;

import dagger.Component;
import tw.invictus.popularmovies.view.MainActivity;

/**
 * Created by ivan on 12/26/15.
 */
@Singleton
@Component(modules = {MainActivityModule.class, ApiModule.class})
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
