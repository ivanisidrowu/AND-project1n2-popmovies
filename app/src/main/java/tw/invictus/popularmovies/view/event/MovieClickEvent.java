package tw.invictus.popularmovies.view.event;

import tw.invictus.popularmovies.model.pojo.Movie;

/**
 * Created by ivan on 1/9/16.
 */
public class MovieClickEvent {
    private Movie movie;
    private boolean isTwoPane = false;

    public MovieClickEvent(Movie movie, boolean isTwoPane) {
        this.movie = movie;
        this.isTwoPane = isTwoPane;
    }

    public boolean isTwoPane() {
        return isTwoPane;
    }

    public void setIsTwoPane(boolean isTwoPane) {
        this.isTwoPane = isTwoPane;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
