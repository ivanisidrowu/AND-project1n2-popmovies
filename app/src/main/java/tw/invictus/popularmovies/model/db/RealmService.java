package tw.invictus.popularmovies.model.db;

import java.util.ArrayList;

import rx.Observable;
import tw.invictus.popularmovies.model.pojo.Movie;

/**
 * Created by ivan on 1/9/16.
 */
public interface RealmService {
    Observable<RealmMovie> addMovie(Movie movie);
    Observable<Movie> deleteMovie(Movie movie);
    boolean isMovieAdded(int id);
    Observable<ArrayList<Movie>> getAllMovies();
}
