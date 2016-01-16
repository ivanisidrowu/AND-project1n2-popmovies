package tw.invictus.popularmovies.view.event;

/**
 * Created by ivan on 1/10/16.
 */
public class MenuItemClickEvent {

    private String sortType;

    public MenuItemClickEvent(String sortType) {
        this.sortType = sortType;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
