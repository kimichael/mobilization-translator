package com.example.kimichael.yandextranslate.network;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.LocalTranslationSource;
import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import org.reactivestreams.Subscription;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by Kim Michael on 31.03.17.
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
