package tw.invictus.popularmovies.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import tw.invictus.popularmovies.MovieApplication;
import tw.invictus.popularmovies.R;
import tw.invictus.popularmovies.model.api.RestfulApi;
import tw.invictus.popularmovies.model.db.RealmService;
import tw.invictus.popularmovies.model.pojo.Movie;
import tw.invictus.popularmovies.presenter.MainPresenter;
import tw.invictus.popularmovies.util.DialogUtil;
import tw.invictus.popularmovies.util.NetworkUtil;
import tw.invictus.popularmovies.view.MainView;
import tw.invictus.popularmovies.view.activity.DetailActivity;
import tw.invictus.popularmovies.view.activity.MainActivity;
import tw.invictus.popularmovies.view.adapter.MainRecyclerViewAdapter;
import tw.invictus.popularmovies.view.event.FavoriteMovieChangeEvent;
import tw.invictus.popularmovies.view.event.MenuItemClickEvent;
import tw.invictus.popularmovies.view.event.MovieClickEvent;
import tw.invictus.popularmovies.view.event.NetworkChangeEvent;
import tw.invictus.popularmovies.view.listener.InfiniteRecyclerViewScrollListener;

/**
 * Created by ivan on 1/10/16.
 */
public class MainFragment extends Fragment implements MainView{

    public static final String TAG = MainFragment.class.getSimpleName();
    public static final String SAVED_POSITION = "saved.position";
    public static final String SAVED_SORT_PARAM = "saved.sort.param";
    public static final String SAVED_MOVIES = "saved.movies";

    @Inject
    MainPresenter presenter;
    @Inject
    RestfulApi restfulApi;
    @Inject
    RealmService realmService;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private int lastScrollPosition = 0;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ((MovieApplication) getActivity().getApplication()).getMainFragmentComponent(this).inject(this);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        initRecyclerView();
        initPresenter();

        if (savedInstanceState == null){
            presenter.loadMovies(presenter.getCurrentSortParam(), MainPresenter.START_PAGE_INDEX);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int currentScrollPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        ArrayList<Movie> movies = ((MainRecyclerViewAdapter) recyclerView.getAdapter()).getMovies();
        outState.putInt(SAVED_POSITION, currentScrollPosition);
        outState.putParcelableArrayList(SAVED_MOVIES, movies);
        outState.putString(SAVED_SORT_PARAM, presenter.getCurrentSortParam());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            Log.d(TAG, "onViewCreated");
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(SAVED_MOVIES);
            lastScrollPosition = savedInstanceState.getInt(SAVED_POSITION);
            recyclerView.setAdapter(new MainRecyclerViewAdapter(movies, this));
            recyclerView.scrollToPosition(lastScrollPosition);
            presenter.setCurrentSortParam(savedInstanceState.getString(SAVED_SORT_PARAM));
        }
    }

    private void initPresenter(){
        presenter.setMainView(this);
        presenter.setApi(restfulApi);
        presenter.setRealmService(realmService);
    }

    private void initRecyclerView(){
        int movieColumns = getResources().getInteger(R.integer.activity_movie_column);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), movieColumns);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new InfiniteRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreMovies(page);
            }
        });
    }

    private void loadMoreMovies(int page){
        String currentSortParam = presenter.getCurrentSortParam();
        if(currentSortParam != MainPresenter.LIST_MY_FAVORITES){
            presenter.loadMovies(page);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @SuppressWarnings("unused")
    public void onEvent(NetworkChangeEvent event){
        int status = event.getNetworkStatus();
        if(status == NetworkUtil.CONNECTED){
            presenter.loadMovies(MainPresenter.SORT_BY_POPULARITY, MainPresenter.START_PAGE_INDEX);
        }else{
            String message = getResources().getString(R.string.no_network_connection);
            DialogUtil.getAlertDialog(getActivity(), "", message).show();
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(MenuItemClickEvent event){
        presenter.loadMovies(event.getSortType(), MainPresenter.START_PAGE_INDEX);
    }

    @SuppressWarnings("unused")
    public void onEvent(FavoriteMovieChangeEvent event){
        String type = presenter.getCurrentSortParam();
        if(type == MainPresenter.LIST_MY_FAVORITES){
            presenter.loadMoviesFromDb();
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        boolean isTwoPane = ((MainActivity) getActivity()).ismTwoPane();
        EventBus.getDefault().postSticky(new MovieClickEvent(movie, isTwoPane));
        if(isTwoPane){
            DetailFragment fragment = new DetailFragment();
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_container, fragment, DetailFragment.TAG)
                    .commit();
        }else{
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onMoreMoviesLoaded(ArrayList<Movie> movies) {
        Log.d(TAG, "onMoreMoviesLoaded");
        scrollToLastScrollPosition();
        lastScrollPosition = 0;
        int currentSize = recyclerView.getAdapter().getItemCount();
        List<Movie> currentMovies = ((MainRecyclerViewAdapter) recyclerView.getAdapter()).getMovies();
        currentMovies.addAll(movies);
        recyclerView.getAdapter().notifyItemRangeInserted(currentSize, currentMovies.size() - 1);
    }

    @Override
    public void onMoviesLoaded(ArrayList<Movie> movies) {
        Log.d(TAG, "onMoviesLoaded" + Integer.toString(lastScrollPosition));
        recyclerView.setAdapter(new MainRecyclerViewAdapter(movies, this));
        scrollToLastScrollPosition();
    }

    private void scrollToLastScrollPosition(){
        if(lastScrollPosition != 0){
            recyclerView.scrollToPosition(lastScrollPosition);
        }
    }
}
