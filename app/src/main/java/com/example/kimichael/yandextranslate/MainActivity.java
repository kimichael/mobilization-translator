package com.example.kimichael.yandextranslate;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v4.app.FragmentTransaction;
import android.view.animation.TranslateAnimation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MainActivity extends AppCompatActivity {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FRAGMENT_STATUS_TRANSLATE, FRAGMENT_STATUS_BOOKMARKS, FRAGMENT_STATUS_SETTINGS})
    public @interface ChosenFragmentStatus {}
    public static final int FRAGMENT_STATUS_TRANSLATE = 0;
    public static final int FRAGMENT_STATUS_BOOKMARKS = 1;
    public static final int FRAGMENT_STATUS_SETTINGS = 2;

    private @ChosenFragmentStatus int mSelectedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize activity with translate fragment
        mSelectedFragment = FRAGMENT_STATUS_TRANSLATE;

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // TODO: Shorten this code
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                switch (item.getItemId()) {
                    case (R.id.action_translate):
                        if (mSelectedFragment == FRAGMENT_STATUS_TRANSLATE)
                            return false;
                        TranslateFragment translateFragment = new TranslateFragment();
                        transaction.replace(R.id.container, translateFragment);
                        mSelectedFragment = FRAGMENT_STATUS_TRANSLATE;
                        break;
                    case (R.id.action_bookmarks):
                        if (mSelectedFragment == FRAGMENT_STATUS_BOOKMARKS)
                            return false;
                        BookmarksFragment bookmarksFragment = new BookmarksFragment();
                        transaction.replace(R.id.container, bookmarksFragment);
                        mSelectedFragment = FRAGMENT_STATUS_BOOKMARKS;
                        break;
                    case (R.id.action_settings):
                        if (mSelectedFragment == FRAGMENT_STATUS_SETTINGS)
                            return false;
                        SettingsFragment settingsFragment = new SettingsFragment();
                        transaction.replace(R.id.container, settingsFragment);
                        mSelectedFragment = FRAGMENT_STATUS_SETTINGS;
                        break;
                    default:
                        return false;
                }
                transaction.commit();
                return true;
            }
        });

    }
}
