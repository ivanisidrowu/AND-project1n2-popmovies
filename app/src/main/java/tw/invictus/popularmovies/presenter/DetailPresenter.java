package tw.invictus.popularmovies.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tw.invictus.popularmovies.model.api.RestfulApi;
import tw.invictus.popularmovies.model.db.RealmService;
import tw.invictus.popularmovies.model.pojo.Movie;
import tw.invictus.popularmovies.view.DetailView;

/**
 * Created by ivan on 12/27/15.
 */
public class DetailPresenter implements BasePresenter {

    private static final String TAG = DetailPresenter.class.getSimpleName();

    private DetailView detailView;
    private Context context;
    private RestfulApi api;
    private RealmService realmService;
    private List<Subscription> subscriptions = new ArrayList<>();

    @Inject
    public DetailPresenter(Context context) {
        this.context = context;
    }

    public void setDetailView(DetailView detailView) {
        this.detailView = detailView;
    }

    public void setApi(RestfulApi api) {
        this.api = api;
    }

    public void setRealmService(RealmService service){
        this.realmService = service;
    }

    public void loadVideos(int id){
        Subscription subscription = api.getVideos(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoResponse -> detailView.onVideosLoaded(videoResponse.getResults()));
        subscriptions.add(subscription);
    }

    public void loadReviews(int id){
        Subscription subscription = api.getReviews(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviewResponse -> detailView.onReviewsLoaded(reviewResponse.getResults()));
        subscriptions.add(subscription);
    }

    private void cleanSubscriptions(){
        for (int i = 0; i < subscriptions.size(); i++) {
            Subscription subscription = subscriptions.get(i);
            if (subscription != null){
                subscription.unsubscribe();
            }
        }
        subscriptions.clear();
    }

    public void addMovie(Movie movie){
        Subscription subscription = realmService
                .addMovie(movie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(() -> detailView.onMovieAdded())
                .subscribe();
        subscriptions.add(subscription);
    }


    public void deleteMovie(Movie movie){
        Subscription subscription = realmService
                .deleteMovie(movie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(() -> detailView.onMovieDeleted())
                .subscribe();
        subscriptions.add(subscription);
    }

    public boolean isMovieAdded(int id){
        return realmService.isMovieAdded(id);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        cleanSubscriptions();
    }

    @Override
    public void onStop() {

    }
}
