package vgroup.demoweek4.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by DELL on 10/3/2017.
 */

public class Utils {
    public static String distance = "";


    public static boolean checkInternet(Activity context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
