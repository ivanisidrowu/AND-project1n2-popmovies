package tw.invictus.popularmovies.model.api;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import tw.invictus.popularmovies.model.pojo.MoviesResponse;

/**
 * Created by ivan on 12/26/15.
 */
public interface RestfulApi {

    @GET("discover/movie")
    Observable<MoviesResponse> getPopularMovies(@Query("sort_by") String sortBy, @Query("page") int page, @Query("api_key") String apiKey);

}
