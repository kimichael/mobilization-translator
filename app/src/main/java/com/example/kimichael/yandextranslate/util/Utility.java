package com.example.kimichael.yandextranslate.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class Utility {

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
