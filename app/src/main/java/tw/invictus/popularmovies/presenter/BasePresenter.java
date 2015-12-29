package tw.invictus.popularmovies.presenter;

/**
 * Created by ivan on 12/27/15.
 */
public interface BasePresenter {
    void onStart();
    void onPause();
    void onDestroy();
    void onStop();
}
