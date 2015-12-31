package tw.invictus.popularmovies.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by ivan.wu on 12/31/2015.
 */
public class DialogUtil {

    public static AlertDialog getAlertDialog(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        return builder.create();
    }
}
