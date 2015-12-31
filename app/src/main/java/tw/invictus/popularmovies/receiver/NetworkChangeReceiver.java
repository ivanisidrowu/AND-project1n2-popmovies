package tw.invictus.popularmovies.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.greenrobot.event.EventBus;
import tw.invictus.popularmovies.util.NetworkUtil;
import tw.invictus.popularmovies.view.event.NetworkChangeEvent;

/**
 * Created by ivan.wu on 12/31/2015.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int status = NetworkUtil.getConnectivityStatus(context);
        EventBus.getDefault().post(new NetworkChangeEvent(status));
    }
}