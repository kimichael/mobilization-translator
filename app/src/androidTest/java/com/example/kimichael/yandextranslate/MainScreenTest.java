package com.example.kimichael.yandextranslate;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.kimichael.yandextranslate.activity.MainActivity;
import com.example.kimichael.yandextranslate.pageobjects.BookmarksPage;
import com.example.kimichael.yandextranslate.pageobjects.MainPage;
import com.example.kimichael.yandextranslate.pageobjects.SettingsPage;
import com.example.kimichael.yandextranslate.pageobjects.TranslatePage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void canSwitchBetweenSections() {
        TranslatePage.obtain()
                .assertOn();
        MainPage.obtain()
                .assertOn()
                .moveToBookmarks();
        BookmarksPage.obtain()
                .assertOn();
        MainPage.obtain()
                .moveToSettings();
        SettingsPage.obtain()
                .assertOn();
        // We can press back and return to starting fragment
        Espresso.pressBack();
        TranslatePage.obtain()
                .assertOn();


    }
}
