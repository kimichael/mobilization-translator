package com.example.kimichael.yandextranslate.pageobjects;

import com.example.kimichael.yandextranslate.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Kim Michael on 26.04.17
 */
public class SettingsPage extends PageObject {

    private static SettingsPage instance = null;

    public static SettingsPage obtain() {
        if (instance == null)
            instance = new SettingsPage();
        return instance;
    }

    @Override
    public SettingsPage assertOn() {
        onView(withId(R.id.settings_placeholder)).check(matches(isDisplayed()));
        return this;
    }
}
