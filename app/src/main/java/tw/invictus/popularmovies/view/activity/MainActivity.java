package tw.invictus.popularmovies.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import tw.invictus.popularmovies.R;
import tw.invictus.popularmovies.presenter.MainPresenter;
import tw.invictus.popularmovies.view.event.MenuItemClickEvent;
import tw.invictus.popularmovies.view.event.MovieShareEvent;
import tw.invictus.popularmovies.view.fragment.DetailFragment;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.detail_container)
    FrameLayout detailContainer;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (detailContainer != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, new DetailFragment(), DetailFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.sort:
                View sortView = findViewById(R.id.sort);
                onSortButtonClicked(sortView);
                break;
            case R.id.share:
                Log.d(TAG, "onOptionsItemSelected");
                EventBus.getDefault().post(new MovieShareEvent());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSortButtonClicked(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_sort);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                EventBus.getDefault().post(new MenuItemClickEvent(MainPresenter.SORT_BY_POPULARITY));
                break;
            case R.id.highest_rated:
                EventBus.getDefault().post(new MenuItemClickEvent(MainPresenter.SORT_BY_RATES));
                break;
            case R.id.favorites:
                EventBus.getDefault().post(new MenuItemClickEvent(MainPresenter.LIST_MY_FAVORITES));
        }
        return false;
    }

    public boolean ismTwoPane() {
        return mTwoPane;
    }
}
