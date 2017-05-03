package com.example.kimichael.yandextranslate.pageobjects;

import com.example.kimichael.yandextranslate.R;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * Created by Kim Michael on 26.04.17
 */
public class TranslatePage extends PageObject {

    private static TranslatePage instance = null;

    public static TranslatePage obtain() {
        if (instance == null)
            instance = new TranslatePage();
        return instance;
    }

    @Override
    public TranslatePage assertOn() {
        onView(withId(R.id.swap_lang_button)).check(matches(isDisplayed()));
        return this;
    }

    public TranslatePage assertSrcLanguageIs(String srcLanguage) {
        onView(withId(R.id.src_lang_button)).check(matches(withText(srcLanguage)));
        return this;
    }

    public TranslatePage assertDestLanguageIs(String srcLanguage) {
        onView(withId(R.id.dest_lang_button)).check(matches(withText(srcLanguage)));
        return this;
    }

    public TranslatePage clickSwapButton() {
        onView(withId(R.id.swap_lang_button)).perform(click());
        return this;
    }

    public TranslatePage typeTextToTranslate(String text) {
        onView(withId(R.id.translated_word_edit_text)).perform(clearText(), typeText(text));
        return this;
    }

    public TranslatePage clickClearButton() {
        onView(withId(R.id.clear_text_button)).perform(click());
        return this;
    }

    public TranslatePage assertTextIs(String text) {
        onView(withId(R.id.translated_word_edit_text)).check(matches(withText(text)));
        return this;
    }

    public TranslatePage assertMainTranslationIs(String text) {
        onView(withId(R.id.main_translation)).check(matches(withText(text)));
        return this;
    }
}
