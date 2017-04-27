package com.example.kimichael.yandextranslate.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class Utility {

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static Spanned fromHtml(String s) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(s, Html.FROM_HTML_MODE_COMPACT);
        }
        return Html.fromHtml(s);
    }
}
