package tw.invictus.popularmovies.model.api;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
import tw.invictus.popularmovies.model.pojo.MoviesResponse;
import tw.invictus.popularmovies.model.pojo.ReviewResponse;
import tw.invictus.popularmovies.model.pojo.VideoResponse;

/**
 * Created by ivan on 12/26/15.
 */
public interface RestfulApi {

    @GET("discover/movie")
    Observable<MoviesResponse> getPopularMovies(@Query("sort_by") String sortBy, @Query("page") int page);

    @GET("movie/{id}/videos")
    Observable<VideoResponse> getVideos(@Path("id") int id);

    @GET("movie/{id}/reviews")
    Observable<ReviewResponse> getReviews(@Path("id") int id);
}
