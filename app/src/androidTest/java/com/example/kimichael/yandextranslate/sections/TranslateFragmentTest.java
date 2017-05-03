package com.example.kimichael.yandextranslate.sections;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.activity.MainActivity;
import com.example.kimichael.yandextranslate.pageobjects.TranslatePage;
import com.example.kimichael.yandextranslate.sections.translate.TranslateFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Kim Michael on 24.04.17
 * @see TranslateFragment
 */
@MediumTest
public class TranslateFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
    }

    @Test
    public void onStart_previousLanguagesShown() {
        SharedPreferences sp = mMainActivityTestRule.getActivity().getPreferences(Context.MODE_PRIVATE);
        String srcLang = sp.getString(mMainActivityTestRule.getActivity().getString(R.string.pref_src_language),
                mMainActivityTestRule.getActivity().getString(R.string.default_src_language));
        String destLang = sp.getString(mMainActivityTestRule.getActivity().getString(R.string.pref_dest_language),
                mMainActivityTestRule.getActivity().getString(R.string.default_dest_language));
        TranslatePage.obtain()
                .assertOn()
                .assertSrcLanguageIs(srcLang)
                .assertDestLanguageIs(destLang);
    }

    @Test
    public void onPressingSwapButton_languagesAreSwapped() {
        SharedPreferences sp = mMainActivityTestRule.getActivity().getPreferences(Context.MODE_PRIVATE);
        String srcLang = sp.getString(mMainActivityTestRule.getActivity().getString(R.string.pref_src_language),
                mMainActivityTestRule.getActivity().getString(R.string.default_src_language));
        String destLang = sp.getString(mMainActivityTestRule.getActivity().getString(R.string.pref_dest_language),
                mMainActivityTestRule.getActivity().getString(R.string.default_dest_language));

        TranslatePage.obtain()
                .assertOn()
                .assertSrcLanguageIs(srcLang)
                .assertDestLanguageIs(destLang)
                .clickSwapButton()
                .assertDestLanguageIs(srcLang)
                .assertSrcLanguageIs(destLang)
                .clickSwapButton()
                .assertSrcLanguageIs(srcLang)
                .assertDestLanguageIs(destLang);

    }

    @Test
    public void onClickingClearButton_editTextIsCleared() {
        String sampleText = "Sample text";

        TranslatePage.obtain()
                .typeTextToTranslate(sampleText)
                .clickClearButton()
                .assertTextIs("");
    }

}
