package tw.invictus.popularmovies.presenter;

import javax.inject.Inject;

import tw.invictus.popularmovies.view.DetailView;

/**
 * Created by ivan on 12/27/15.
 */
public class DetailPresenter implements BasePresenter {

    private DetailView detailView;

    @Inject
    public DetailPresenter() {
    }

    public DetailView getDetailView() {
        return detailView;
    }

    public void setDetailView(DetailView detailView) {
        this.detailView = detailView;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStop() {

    }
}
