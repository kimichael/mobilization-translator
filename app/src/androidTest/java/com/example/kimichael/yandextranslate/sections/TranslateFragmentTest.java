package com.example.kimichael.yandextranslate.sections;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.activity.MainActivity;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.pageobjects.TranslatePage;
import com.example.kimichael.yandextranslate.sections.translate.TranslateContract;
import com.example.kimichael.yandextranslate.sections.translate.TranslateFragment;
import com.example.kimichael.yandextranslate.util.EspressoIdlingResource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;

/**
 * Created by Kim Michael on 24.04.17
 */
@RunWith(MockitoJUnitRunner.class)
@MediumTest
public class TranslateFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
    }

    @Test
    public void onStart_defaultLanguagesShown() {
        TranslatePage.obtain()
                .assertOn()
                .assertSrcLanguageIs(mMainActivityTestRule.getActivity().getString(R.string.default_src_language))
                .assertDestLanguageIs(mMainActivityTestRule.getActivity().getString(R.string.default_dest_language));
    }

    @Test
    public void onPressingSwapButton_languagesAreSwapped() {
        String srcLang = mMainActivityTestRule.getActivity().getString(R.string.default_src_language);
        String destLang = mMainActivityTestRule.getActivity().getString(R.string.default_dest_language);

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
