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


    Single<Translation> getTranslation(String requestedText,
                          LanguageDirection direction,
                          @TranslationApi int translationApi);

    Single<List<Language>> retrieveLanguages();

    Single<List<LanguageDirection>> retrieveLanguageDirections();

}
