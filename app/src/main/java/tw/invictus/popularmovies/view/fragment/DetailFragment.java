package tw.invictus.popularmovies.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import tw.invictus.popularmovies.BuildConfig;
import tw.invictus.popularmovies.MovieApplication;
import tw.invictus.popularmovies.R;
import tw.invictus.popularmovies.model.api.RestfulApi;
import tw.invictus.popularmovies.model.db.RealmService;
import tw.invictus.popularmovies.model.pojo.Movie;
import tw.invictus.popularmovies.model.pojo.Review;
import tw.invictus.popularmovies.model.pojo.Video;
import tw.invictus.popularmovies.presenter.DetailPresenter;
import tw.invictus.popularmovies.util.IntentUtil;
import tw.invictus.popularmovies.util.NetworkUtil;
import tw.invictus.popularmovies.view.CustomLinearLayoutManager;
import tw.invictus.popularmovies.view.DetailView;
import tw.invictus.popularmovies.view.activity.DetailActivity;
import tw.invictus.popularmovies.view.adapter.DetailReviewRecyclerViewAdapter;
import tw.invictus.popularmovies.view.adapter.DetailVideoRecyclerViewAdapter;
import tw.invictus.popularmovies.view.event.FavoriteMovieChangeEvent;
import tw.invictus.popularmovies.view.event.MovieClickEvent;
import tw.invictus.popularmovies.view.event.MovieShareEvent;

/**
 * Created by ivan on 1/10/16.
 */
public class DetailFragment extends Fragment implements DetailView {

    public static final String TAG = DetailFragment.class.getSimpleName();

    @Inject
    DetailPresenter presenter;
    @Inject
    RestfulApi restfulApi;
    @Inject
    RealmService realmService;

    @Bind(R.id.poster)
    ImageView poster;
    @Bind(R.id.backdrop)
    ImageView backdrop;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.info)
    TextView info;
    @Bind(R.id.review_recycler)
    RecyclerView reviewRecyclerView;
    @Bind(R.id.trailer_recycler)
    RecyclerView trailerRecyclerView;
    @Bind(R.id.fab)
    FloatingActionButton actionButton;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.mask)
    FrameLayout mask;
    @BindString(R.string.image_size_url)
    String imageSize;
    @BindString(R.string.backdrop_size_url)
    String backdropSize;
    @BindString(R.string.share)
    String shareMessage;

    private Movie movie;
    private Video video;
    private boolean isMovieAdd = false;
    private boolean isTwoPane = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        actionButton.setEnabled(false);
        ((MovieApplication) getActivity().getApplication()).getDetailFragmentComponent(this).inject(this);
        initPresenter();
        EventBus.getDefault().registerSticky(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable(TAG);
            initUI();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(!isTwoPane){
            inflater.inflate(R.menu.menu_detail, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id){
            case R.id.share:
                if(video != null)
                    IntentUtil.shareYoutubeVideo(video.getKey(), getContext(), shareMessage);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(movie != null){
            outState.putParcelable(TAG, movie);
        }
    }

    private void initPresenter() {
        presenter.setDetailView(this);
        presenter.setApi(restfulApi);
        presenter.setRealmService(realmService);
    }

    private void initUI(){
        if (movie != null) {
            String posterImgUrl = BuildConfig.IMG_BASE_URL + imageSize + movie.getPosterPath();
            String backdropImgUrl = BuildConfig.IMG_BASE_URL + backdropSize + movie.getBackdropPath();
            String detailsInfo = getResources().getString(R.string.movie_detail_info, movie.getVoteAverage().toString(), movie.getReleaseDate());

            initToolbar();
            initFab();
            loadVideosAndReviews(movie);

            overview.setText(movie.getOverview());
            info.setText(detailsInfo);

            Glide.with(this).load(posterImgUrl).into(poster);
            Glide.with(this).load(backdropImgUrl).centerCrop().into(backdrop);
        } else {
            Log.e(TAG, "initUI: movie is null");
        }
    }

    private void initToolbar(){
        if(!isTwoPane){
            ((DetailActivity) getActivity()).setSupportActionBar(toolbar);
            ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbarLayout.setTitle(movie.getTitle());
        }
        toolbarLayout.setTitle(movie.getTitle());
    }

    private void initFab(){
        isMovieAdd = presenter.isMovieAdded(movie.getId());
        if(isMovieAdd){
            Glide.with(this).load(R.drawable.ic_favorite_pressed).into(actionButton);
        }
    }

    private void loadVideosAndReviews(Movie movie){
        boolean isOnline = NetworkUtil.isOnline(this.getContext());
        if(isOnline){
            presenter.loadVideos(movie.getId());
            presenter.loadReviews(movie.getId());
            trailerRecyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
            reviewRecyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(MovieClickEvent event){
        this.movie = event.getMovie();
        this.isTwoPane = event.isTwoPane();
        initUI();
        mask.setVisibility(View.GONE);
        actionButton.setEnabled(true);
    }

    @SuppressWarnings("unused")
    public void onEvent(MovieShareEvent event){
        if (video != null)
            IntentUtil.shareYoutubeVideo(video.getKey(), getContext(), shareMessage);
    }

    @Override
    public void onVideosLoaded(List<Video> videos) {
        video = (videos.size() > 0) ? videos.get(0) : null;
        trailerRecyclerView.setAdapter(new DetailVideoRecyclerViewAdapter(videos, this));
    }

    @Override
    public void onReviewsLoaded(List<Review> reviews) {
        reviewRecyclerView.setAdapter(new DetailReviewRecyclerViewAdapter(reviews, this));
    }

    @Override
    public void onMovieAdded() {
        isMovieAdd = true;
        Glide.with(this).load(R.drawable.ic_favorite_pressed).into(actionButton);
        EventBus.getDefault().post(new FavoriteMovieChangeEvent());
    }

    @Override
    public void onMovieDeleted() {
        isMovieAdd = false;
        Glide.with(this).load(R.drawable.ic_favorite).into(actionButton);
        EventBus.getDefault().post(new FavoriteMovieChangeEvent());
    }

    @OnClick(R.id.fab)
    public void onFabClicked(View view){
        if(isMovieAdd){
            presenter.deleteMovie(movie);
        }else{
            presenter.addMovie(movie);
        }
    }

    @OnClick(R.id.toolbar_play)
    public void onToolbarPlayClicked(View view){
        if(video != null){
            IntentUtil.viewYoutubeVideo(video.getKey(), this.getContext());
        }
    }
}
