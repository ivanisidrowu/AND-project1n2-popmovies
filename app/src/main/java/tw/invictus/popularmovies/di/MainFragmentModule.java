package tw.invictus.popularmovies.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import tw.invictus.popularmovies.presenter.MainPresenter;

/**
 * Created by ivan on 12/26/15.
 */
@Module
public class MainFragmentModule {

    private final Context context;

    public MainFragmentModule(Context context){
        this.context = context;
    }

    @Provides
    MainPresenter provideMainPresenter(){
        return new MainPresenter(context);
    }
}
