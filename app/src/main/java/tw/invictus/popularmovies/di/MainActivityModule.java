package tw.invictus.popularmovies.di;

import dagger.Module;
import dagger.Provides;
import tw.invictus.popularmovies.presenter.MainPresenter;
import tw.invictus.popularmovies.view.MainActivity;

/**
 * Created by ivan on 12/26/15.
 */
@Module
public class MainActivityModule {

    private final MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Provides
    MainPresenter provideMainPresenter(){
        return new MainPresenter(mainActivity);
    }
}
