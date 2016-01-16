package tw.invictus.popularmovies.view.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tw.invictus.popularmovies.BuildConfig;
import tw.invictus.popularmovies.R;
import tw.invictus.popularmovies.model.pojo.Movie;
import tw.invictus.popularmovies.view.MainView;

/**
 * Created by ivan on 12/27/15.
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = MainRecyclerViewAdapter.class.getSimpleName();
    private ArrayList<Movie> movies;
    private MainView mainView;

    public MainRecyclerViewAdapter(ArrayList<Movie> movies, MainView mainView) {
        this.movies = movies;
        this.mainView = mainView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(rootView, mainView, movies);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        String imageUrl = BuildConfig.IMG_BASE_URL + holder.imageSizeUrl + movie.getPosterPath();
        String title = movie.getTitle();
        if(holder.textView != null){
            holder.textView.setText(title);
        }
        Glide.with(holder.poster.getContext()).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.poster);
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.poster)
        ImageView poster;
        @Nullable
        @Bind(R.id.card_text)
        TextView textView;
        @Bind(R.id.card_view)
        CardView cardView;
        @BindString(R.string.image_size_url)
        String imageSizeUrl;

        View itemView;
        MainView mainView;
        List<Movie> movies;

        public ViewHolder(View itemView, MainView mainView, List<Movie> movies) {
            super(itemView);
            this.itemView = itemView;
            this.mainView = mainView;
            this.movies = movies;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        public void onClick(View view){
            mainView.onMovieClicked(movies.get(getAdapterPosition()));
        }
    }
}
