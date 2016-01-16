package tw.invictus.popularmovies.view;

import java.util.List;

import tw.invictus.popularmovies.model.pojo.Review;
import tw.invictus.popularmovies.model.pojo.Video;

/**
 * Created by ivan on 12/27/15.
 */
public interface DetailView {
    void onVideosLoaded(List<Video> videos);
    void onReviewsLoaded(List<Review> reviews);
    void onMovieAdded();
    void onMovieDeleted();
}
