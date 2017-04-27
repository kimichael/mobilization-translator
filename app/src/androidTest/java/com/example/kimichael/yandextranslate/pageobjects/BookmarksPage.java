package com.example.kimichael.yandextranslate.pageobjects;

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
public class BookmarksPage extends PageObject {

    private static BookmarksPage instance = null;

    public static BookmarksPage obtain() {
        if (instance == null)
            instance = new BookmarksPage();
        return instance;
    }

    @Override
    public PageObject assertOn() {
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));
        return this;
    }
}
