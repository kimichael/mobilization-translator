package com.example.kimichael.yandextranslate;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.kimichael.yandextranslate.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TranslateScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickClearButton_clearsInput() {
        String typedText = "Sample text";

        onView(withId(R.id.translated_word_edit_text)).
                perform(typeText(typedText));

        onView(withId(R.id.clear_text_button)).perform(click());

        onView(withId(R.id.translated_word_edit_text))
                .check(matches(withText("")));

        onView(withId(R.id.translated_word_edit_text))
                .check(matches(hasFocus()));
    }
}
