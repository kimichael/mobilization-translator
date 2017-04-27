package com.example.kimichael.yandextranslate.pageobjects;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;

import com.example.kimichael.yandextranslate.R;

import org.hamcrest.Matchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Kim Michael on 26.04.17
 */
public class MainPage extends PageObject {

    private static MainPage instance = null;

    public static MainPage obtain() {
        if (instance == null)
            instance = new MainPage();
        return instance;
    }

    @Override
    public MainPage assertOn() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        return this;
    }

    public MainPage moveToTranslate() {
        onView(Matchers.allOf(ViewMatchers.withId(R.id.action_translate)))
                .perform(ViewActions.click());
        return this;
    }

    public MainPage moveToBookmarks() {
        onView(Matchers.allOf(ViewMatchers.withId(R.id.action_bookmarks)))
                .perform(ViewActions.click());
        return this;
    }

    public MainPage moveToSettings() {
        onView(Matchers.allOf(ViewMatchers.withId(R.id.action_settings)))
                .perform(ViewActions.click());
        return this;
    }

}
