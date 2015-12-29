package tw.invictus.popularmovies.di;

import javax.inject.Singleton;

import dagger.Component;
import tw.invictus.popularmovies.view.DetailActivity;

/**
 * Created by ivan on 12/27/15.
 */
@Singleton
@Component(modules = {DetailActivityModule.class})
public interface DetailActivityComponent {
    void inject(DetailActivity activity);
}
