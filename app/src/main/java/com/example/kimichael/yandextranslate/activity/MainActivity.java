package com.example.kimichael.yandextranslate.activity;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.kimichael.yandextranslate.ComponentProvider;
import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.components.ActivityComponent;
import com.example.kimichael.yandextranslate.components.DaggerActivityComponent;
import com.example.kimichael.yandextranslate.history.HistoryFragment;
import com.example.kimichael.yandextranslate.modules.ContextModule;
import com.example.kimichael.yandextranslate.settings.SettingsFragment;
import com.example.kimichael.yandextranslate.translate.TranslateFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import dagger.Component;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, ComponentProvider {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FRAGMENT_STATUS_TRANSLATE, FRAGMENT_STATUS_BOOKMARKS, FRAGMENT_STATUS_SETTINGS})
    public @interface ChosenFragmentStatus {}
    public static final int FRAGMENT_STATUS_TRANSLATE = 0;
    public static final int FRAGMENT_STATUS_BOOKMARKS = 1;
    public static final int FRAGMENT_STATUS_SETTINGS = 2;

    private @ChosenFragmentStatus int mSelectedFragment;
    private ActivityComponent mActivityComponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityComponent = DaggerActivityComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Default fragment is translate fragment
        resetFragmentState();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        // Set toolbar as support action bar for fragment to acces it
        Toolbar toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //Navigate to translate fragment
    private void resetFragmentState() {
        // Initialize activity with translate fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        TranslateFragment translateFragment = new TranslateFragment();
        transaction.replace(R.id.container, translateFragment);
        transaction.commit();
        mSelectedFragment = FRAGMENT_STATUS_TRANSLATE;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                HistoryFragment historyFragment = new HistoryFragment();
                transaction.replace(R.id.container, historyFragment);
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

    @Override
    public ActivityComponent provideComponent() {
        return mActivityComponent;
    }

    // Workaround to make edit text lose focus
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
