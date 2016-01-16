package tw.invictus.popularmovies.model.pojo;

/**
 * Created by ivan on 1/14/16.
 */
public final class MovieBuilder {

    private int id;
    private boolean adult;
    private String backdropPath;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private double popularity;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    public MovieBuilder(int id, String title, String overview){
        this.id = id;
        this.title = title;
        this.overview = overview;
    }

    public MovieBuilder adult(boolean adult){
        this.adult = adult;
        return this;
    }

    public MovieBuilder backdropPath(String backdropPath){
        this.backdropPath = backdropPath;
        return this;
    }

    public MovieBuilder releaseDate(String releaseDate){
        this.releaseDate = releaseDate;
        return this;
    }

    public MovieBuilder posterPath(String posterPath){
        this.posterPath = posterPath;
        return this;
    }

    public MovieBuilder popularity(double popularity){
        this.popularity = popularity;
        return this;
    }

    public MovieBuilder video(boolean video){
        this.video = video;
        return this;
    }

    public MovieBuilder voteAverage(double voteAverage){
        this.voteAverage = voteAverage;
        return this;
    }

    public MovieBuilder voteCount(int voteCount){
        this.voteCount = voteCount;
        return this;
    }

    public Movie create(){
        return new Movie(adult, backdropPath, id, overview, releaseDate, posterPath, popularity, title, video, voteAverage, voteCount);
    }
}
