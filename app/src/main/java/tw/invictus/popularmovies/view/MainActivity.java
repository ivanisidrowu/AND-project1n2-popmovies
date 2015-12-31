package tw.invictus.popularmovies.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import tw.invictus.popularmovies.MovieApplication;
import tw.invictus.popularmovies.R;
import tw.invictus.popularmovies.model.api.RestfulApi;
import tw.invictus.popularmovies.model.pojo.Movie;
import tw.invictus.popularmovies.presenter.MainPresenter;
import tw.invictus.popularmovies.util.DialogUtil;
import tw.invictus.popularmovies.util.NetworkUtil;
import tw.invictus.popularmovies.view.adapter.MainRecyclerViewAdapter;
import tw.invictus.popularmovies.view.event.NetworkChangeEvent;
import tw.invictus.popularmovies.view.listener.InfiniteRecyclerViewScrollListener;

public class MainActivity extends AppCompatActivity implements MainView, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    MainPresenter presenter;

    @Inject
    RestfulApi restfulApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MovieApplication) getApplication()).getMainActivityComponent(this).inject(this);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initRecyclerView();
        initPresenter();
        setSupportActionBar(toolbar);
        presenter.loadMovies(MainPresenter.SORT_BY_POPULARITY, MainPresenter.START_PAGE_INDEX);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.sort:
                View sortView = findViewById(R.id.sort);
                onSortButtonClicked(sortView);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    private void initPresenter(){
        presenter.setMainView(this);
        presenter.setApi(restfulApi);
        presenter.setRecyclerView(recyclerView);
    }

    private void initRecyclerView(){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new InfiniteRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                presenter.loadMovies(page);
            }
        });
    }

    public void onSortButtonClicked(View view){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_sort);
        popup.show();
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(TAG, movie);
        startActivity(intent);
    }

    @Override
    public void onLoadMoreMovies(List<Movie> movies) {
        Log.d(TAG, "onLoadMoreMovies");
        int currentSize = recyclerView.getAdapter().getItemCount();
        List<Movie> currentMovies = ((MainRecyclerViewAdapter) recyclerView.getAdapter()).getMovies();
        currentMovies.addAll(movies);
        recyclerView.getAdapter().notifyItemRangeInserted(currentSize, currentMovies.size() - 1);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.popular:
                presenter.loadMovies(MainPresenter.SORT_BY_POPULARITY, MainPresenter.START_PAGE_INDEX);
                break;
            case R.id.highest_rated:
                presenter.loadMovies(MainPresenter.SORT_BY_RATES, MainPresenter.START_PAGE_INDEX);
                break;
        }
        return false;
    }

    @SuppressWarnings("unused")
    public void onEvent(NetworkChangeEvent event){
        int status = event.getNetworkStatus();
        if(status == NetworkUtil.CONNECTED){
            presenter.loadMovies(MainPresenter.SORT_BY_POPULARITY, MainPresenter.START_PAGE_INDEX);
        }else{
            String message = getResources().getString(R.string.no_network_connection);
            DialogUtil.getAlertDialog(this, "", message).show();
        }
    }

}
