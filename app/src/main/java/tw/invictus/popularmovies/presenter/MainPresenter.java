package tw.invictus.popularmovies.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tw.invictus.popularmovies.BuildConfig;
import tw.invictus.popularmovies.model.api.RestfulApi;
import tw.invictus.popularmovies.model.pojo.Movie;
import tw.invictus.popularmovies.model.pojo.MoviesResponse;
import tw.invictus.popularmovies.view.MainView;
import tw.invictus.popularmovies.view.adapter.MainRecyclerViewAdapter;

/**
 * Created by ivan on 12/26/15.
 */
public class MainPresenter implements BasePresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();
    public static final String SORT_BY_POPULARITY = "popularity.desc";
    public static final String SORT_BY_RATES = "vote_average.desc";

    private Context context;
    private MainView mainView;
    private Subscription subscription;
    private RestfulApi api;
    private RecyclerView recyclerView;
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

    public void loadMovies(String sortParam) {
        this.currentSortParam = sortParam;
        subscription = api.getPopularMovies(currentSortParam, BuildConfig.API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e -> Log.e(TAG, "loadMovies: ", e))
                .doOnCompleted(() -> Log.d(TAG, "loadMovies: completed"))
                .subscribe(response -> processMovieResponse(response));
    }

    private void processMovieResponse(MoviesResponse response) {
        List<Movie> movies = response.getResults();
        recyclerView.setAdapter(new MainRecyclerViewAdapter(movies, mainView));
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {
        subscription.unsubscribe();
    }

    @Override
    public void onDestroy() {
        subscription.unsubscribe();
    }

    @Override
    public void onStop() {

    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}
