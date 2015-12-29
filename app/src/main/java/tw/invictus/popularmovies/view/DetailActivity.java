package tw.invictus.popularmovies.view;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import tw.invictus.popularmovies.BuildConfig;
import tw.invictus.popularmovies.R;
import tw.invictus.popularmovies.model.pojo.Movie;
import tw.invictus.popularmovies.presenter.DetailPresenter;

/**
 * Created by ivan on 12/27/15.
 */
public class DetailActivity extends AppCompatActivity {

    @Inject
    DetailPresenter presenter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.poster)
    ImageView poster;
    @Bind(R.id.backdrop)
    ImageView backdrop;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.info)
    TextView info;
    @BindString(R.string.image_size_url)
    String imageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUI(){
        Bundle bundle = getIntent().getExtras();
        Movie movie = bundle.getParcelable(MainActivity.class.getSimpleName());
        String posterImgUrl = BuildConfig.IMG_BASE_URL + imageSize + movie.getPosterPath();
        String backdropImgUrl = BuildConfig.IMG_BASE_URL + imageSize + movie.getBackdropPath();
        String detailsInfo = getResources().getString(R.string.movie_detail_info, movie.getVoteAverage().toString(), movie.getReleaseDate());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle(movie.getTitle());
        overview.setText(movie.getOverview());
        info.setText(detailsInfo);

        Glide.with(this).load(posterImgUrl).into(poster);
        Glide.with(this).load(backdropImgUrl).centerCrop().into(backdrop);
    }
}
