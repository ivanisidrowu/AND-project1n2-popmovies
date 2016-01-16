package tw.invictus.popularmovies.view;

import java.util.ArrayList;

import tw.invictus.popularmovies.model.pojo.Movie;

/**
 * Created by ivan on 12/26/15.
 */
public interface MainView {
    void onMovieClicked(Movie movie);
    void onMoreMoviesLoaded(ArrayList<Movie> movies);
    void onMoviesLoaded(ArrayList<Movie> movies);
}
