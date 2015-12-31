package tw.invictus.popularmovies.view;

import android.view.View;

import java.util.List;

import tw.invictus.popularmovies.model.pojo.Movie;

/**
 * Created by ivan on 12/26/15.
 */
public interface MainView {
    void onMovieClicked(Movie movie);
    void onLoadMoreMovies(List<Movie> movies);
}
