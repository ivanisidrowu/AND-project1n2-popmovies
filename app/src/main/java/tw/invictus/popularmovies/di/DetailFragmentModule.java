package tw.invictus.popularmovies.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import tw.invictus.popularmovies.presenter.DetailPresenter;

/**
 * Created by ivan on 12/27/15.
 */
@Module
public class DetailFragmentModule {

    private Context context;

    public DetailFragmentModule(Context context) {
        this.context = context;
    }

    @Provides
    DetailPresenter provideDetailPresenter(){
        return new DetailPresenter(context);
    }
}
