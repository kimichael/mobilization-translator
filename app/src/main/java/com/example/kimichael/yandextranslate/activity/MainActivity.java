package com.example.kimichael.yandextranslate.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.kimichael.yandextranslate.FragmentSwitcher;
import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.sections.history.StorageFragment;
import com.example.kimichael.yandextranslate.sections.settings.SettingsFragment;
import com.example.kimichael.yandextranslate.sections.translate.TranslateFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Kim Michael on 31.03.17
 */
public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        FragmentSwitcher {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FRAGMENT_STATUS_TRANSLATE, FRAGMENT_STATUS_BOOKMARKS, FRAGMENT_STATUS_SETTINGS})
    public @interface ChosenFragmentStatus {}
    public static final int FRAGMENT_STATUS_TRANSLATE = 0;
    public static final int FRAGMENT_STATUS_BOOKMARKS = 1;
    public static final int FRAGMENT_STATUS_SETTINGS = 2;

    private @ChosenFragmentStatus int mSelectedFragment;
    BottomNavigationView mNavigationView;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.key_chosen_fragment), mSelectedFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    @SuppressWarnings("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigationView.setOnNavigationItemSelectedListener(this);
        // Set toolbar as support action bar for fragment to access it
        Toolbar toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState != null)
            resetFragmentState(savedInstanceState.getInt(getString(R.string.key_chosen_fragment), FRAGMENT_STATUS_TRANSLATE));
        else
            resetFragmentState(FRAGMENT_STATUS_TRANSLATE);
    }

    // Navigate to translate fragment
    private void resetFragmentState(@ChosenFragmentStatus int chosenFragment) {
        // Initialize activity with translate fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        Fragment fragment;
        switch (chosenFragment) {
            case FRAGMENT_STATUS_BOOKMARKS:
                fragment = new StorageFragment();
                break;
            case FRAGMENT_STATUS_SETTINGS:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new TranslateFragment();
        }
        transaction.replace(R.id.container, fragment);
        transaction.commit();
        mSelectedFragment = chosenFragment;
        mNavigationView.getMenu().getItem(0).setChecked(true);
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
                checkNotNull(getSupportActionBar()).setDisplayShowCustomEnabled(false);
                transaction.replace(R.id.container, translateFragment);
                mSelectedFragment = FRAGMENT_STATUS_TRANSLATE;
                break;
            case (R.id.action_bookmarks):
                if (mSelectedFragment == FRAGMENT_STATUS_BOOKMARKS)
                    return false;
                StorageFragment storageFragment = new StorageFragment();
                checkNotNull(getSupportActionBar()).setDisplayShowCustomEnabled(false);
                transaction.replace(R.id.container, storageFragment);
                mSelectedFragment = FRAGMENT_STATUS_BOOKMARKS;
                break;
            case (R.id.action_settings):
                if (mSelectedFragment == FRAGMENT_STATUS_SETTINGS)
                    return false;
                SettingsFragment settingsFragment = new SettingsFragment();
                checkNotNull(getSupportActionBar()).setDisplayShowCustomEnabled(false);
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
    public void switchFragment(@ChosenFragmentStatus int fragment) {
        resetFragmentState(fragment);
    }

    @Override
    public void onBackPressed() {
        if (mSelectedFragment == FRAGMENT_STATUS_SETTINGS ||
                mSelectedFragment == FRAGMENT_STATUS_BOOKMARKS) {
            resetFragmentState(FRAGMENT_STATUS_TRANSLATE);
        } else {
            super.onBackPressed();
        }
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
