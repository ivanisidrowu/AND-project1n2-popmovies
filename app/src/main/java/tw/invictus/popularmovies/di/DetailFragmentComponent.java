package tw.invictus.popularmovies.di;

import javax.inject.Singleton;

import dagger.Component;
import tw.invictus.popularmovies.view.fragment.DetailFragment;

/**
 * Created by ivan on 12/27/15.
 */
@Singleton
@Component(modules = {DetailFragmentModule.class, ApiModule.class, DbModule.class})
public interface DetailFragmentComponent {
    void inject(DetailFragment fragment);
}
