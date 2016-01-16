package tw.invictus.popularmovies.di;

import javax.inject.Singleton;

import dagger.Component;
import tw.invictus.popularmovies.view.fragment.MainFragment;

/**
 * Created by ivan on 12/26/15.
 */
@Singleton
@Component(modules = {MainFragmentModule.class, ApiModule.class, DbModule.class})
public interface MainFragmentComponent {
    void inject(MainFragment mainFragment);
}
