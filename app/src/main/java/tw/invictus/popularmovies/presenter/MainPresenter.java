package tw.invictus.popularmovies.presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tw.invictus.popularmovies.R;
import tw.invictus.popularmovies.model.api.RestfulApi;
import tw.invictus.popularmovies.model.db.RealmService;
import tw.invictus.popularmovies.model.pojo.Movie;
import tw.invictus.popularmovies.model.pojo.MoviesResponse;
import tw.invictus.popularmovies.util.DialogUtil;
import tw.invictus.popularmovies.util.NetworkUtil;
import tw.invictus.popularmovies.view.MainView;

/**
 * Created by ivan on 12/26/15.
 */
public class MainPresenter implements BasePresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();
    public static final int START_PAGE_INDEX = 0;
    public static final String SORT_BY_POPULARITY = "popularity.desc";
    public static final String SORT_BY_RATES = "vote_average.desc";
    public static final String LIST_MY_FAVORITES = "favorites";

    private Context context;
    private MainView mainView;
    private Subscription subscription;
    private RestfulApi api;
    private RealmService realmService;
    private String currentSortParam = SORT_BY_POPULARITY;

    @Inject
    public MainPresenter(Context context) {
        this.context = context;
    }

    public void setApi(RestfulApi api) {
        this.api = api;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void setRealmService(RealmService realmService) {
        this.realmService = realmService;
    }

    public void loadMovies(String sortParam, int page) {
        this.currentSortParam = sortParam;

        if(sortParam == LIST_MY_FAVORITES){
            loadMoviesFromDb();
        }else{
            loadMovies(page);
        }
    }

    public void loadMovies(int page) {
        if (NetworkUtil.isOnline(context)) {
            subscription = api.getPopularMovies(currentSortParam, page + 1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(e -> Log.e(TAG, "loadMovies: ", e))
                    .subscribe(response -> processMovieResponse(response, page));
        }else{
            String message = context.getResources().getString(R.string.no_network_connection);
            DialogUtil.getAlertDialog(context, "", message).show();
        }
    }

    public void loadMoviesFromDb(){
        Log.d(TAG, "loadMoviesFromDb");
        currentSortParam = LIST_MY_FAVORITES;
        subscription = realmService
                .getAllMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> mainView.onMoviesLoaded(movies));
    }

    private void processMovieResponse(MoviesResponse response, int page) {
        ArrayList<Movie> movies = response.getResults();
        Log.d(TAG, "processMovieResponse: page" + Integer.toString(page));
        if(page == START_PAGE_INDEX){
            mainView.onMoviesLoaded(movies);
        }else{
            mainView.onMoreMoviesLoaded(movies);
        }
    }

    public String getCurrentSortParam() {
        return currentSortParam;
    }

    public void setCurrentSortParam(String currentSortParam) {
        this.currentSortParam = currentSortParam;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        if(subscription != null){
            subscription.unsubscribe();
        }
    }

    @Override
    public void onStop() {

    }

}
