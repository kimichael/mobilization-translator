package com.example.kimichael.yandextranslate.data.network;

import android.support.annotation.IntDef;

import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by Kim Michael on 31.03.17.
 * Powered by Retrofit client to fetch translations
 */
public interface NetworkTranslationSource {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({YANDEX_TRANSLATE_API, YANDEX_DICTIONARY_API})
    @interface TranslationApi {}
    int YANDEX_TRANSLATE_API = 0;
    int YANDEX_DICTIONARY_API = 1;


    /**
     * Get translation from network
     * with given text, language direction and API to be used
     * @return A single observable, that emits requested translation
     */
    Single<Translation> getTranslation(String requestedText,
                          LanguageDirection direction,
                          @TranslationApi int translationApi);

    /**
     * Get languages with their names from network
     * @return A single obserable, that emits list of languages
     */
    Single<List<Language>> retrieveLanguages();

    /**
     * Get language directions, supported by Yandex.Dictionary
     * @return A single observable, that emits list of supported language directions
     */
    Single<List<LanguageDirection>> retrieveLanguageDirections();

}
