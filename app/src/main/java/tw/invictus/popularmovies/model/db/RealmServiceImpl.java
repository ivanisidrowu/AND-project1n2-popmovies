package tw.invictus.popularmovies.model.db;

import android.content.Context;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;
import tw.invictus.popularmovies.model.pojo.Movie;
import tw.invictus.popularmovies.model.pojo.MovieBuilder;

/**
 * Created by ivan on 1/9/16.
 */
public class RealmServiceImpl implements RealmService{

    private static final String TAG = RealmServiceImpl.class.getSimpleName();
    
    private final Context context;

    @Inject
    public RealmServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Observable<RealmMovie> addMovie(Movie movie) {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getInstance(context);

            realm.beginTransaction();
            RealmMovie realmMovie = new RealmMovie();
            realmMovie.setId(movie.getId());
            realmMovie.setOverview(movie.getOverview());
            realmMovie.setTitle(movie.getTitle());
            realmMovie.setAdult(movie.getAdult());
            realmMovie.setBackdropPath(movie.getBackdropPath());
            realmMovie.setPopularity(movie.getPopularity());
            realmMovie.setPosterPath(movie.getPosterPath());
            realmMovie.setReleaseDate(movie.getReleaseDate());
            realmMovie.setVideo(movie.getVideo());
            realmMovie.setVoteAverage(movie.getVoteAverage());
            realmMovie.setVoteCount(movie.getVoteCount());
            realm.copyToRealmOrUpdate(realmMovie);
            realm.commitTransaction();

            subscriber.onNext(realmMovie);
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<Movie> deleteMovie(Movie movie) {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getInstance(context);
            RealmMovie realmMovie = realm.where(RealmMovie.class).equalTo("id", movie.getId()).findFirst();
            realm.beginTransaction();
            realmMovie.removeFromRealm();
            realm.commitTransaction();
            subscriber.onNext(movie);
            subscriber.onCompleted();
        });
    }

    @Override
    public boolean isMovieAdded(int id) {
        RealmMovie movie = Realm.getInstance(context).where(RealmMovie.class).equalTo("id", id).findFirst();
        boolean result = (movie != null) ? true : false;
        return result;
    }

    @Override
    public Observable<ArrayList<Movie>> getAllMovies() {
        return Realm.getInstance(context)
                .where(RealmMovie.class)
                .findAllAsync()
                .asObservable()
                .map(realmMovies -> {
                    ArrayList<Movie> list = new ArrayList<>(realmMovies.size());
                    for (RealmMovie realmMovie: realmMovies){
                        list.add(createMovieObject(realmMovie));
                    }
                    return list;
                });
    }

    private Movie createMovieObject(RealmMovie realmMovie){
        MovieBuilder builder = new MovieBuilder(realmMovie.getId(), realmMovie.getTitle(), realmMovie.getOverview());
        Movie newMovie = builder
                .adult(realmMovie.getAdult().booleanValue())
                .backdropPath(realmMovie.getBackdropPath())
                .popularity(realmMovie.getPopularity().doubleValue())
                .posterPath(realmMovie.getPosterPath())
                .releaseDate(realmMovie.getReleaseDate())
                .video(realmMovie.getVideo().booleanValue())
                .voteAverage(realmMovie.getVoteAverage().doubleValue())
                .voteCount(realmMovie.getVoteCount().intValue())
                .create();
        return newMovie;
    }
}
