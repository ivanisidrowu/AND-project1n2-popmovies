package tw.invictus.popularmovies.di;

import dagger.Module;
import tw.invictus.popularmovies.presenter.DetailPresenter;

/**
 * Created by ivan on 12/27/15.
 */
@Module
public class DetailActivityModule {
    public DetailActivityModule() {
    }

    DetailPresenter provideDetailPresenter(){
        return new DetailPresenter();
    }
}
