package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;

/**
 * Created by Kim Michael on 31.03.17.
 */
public interface TranslationRepository {

    interface LoadTranslationCallback {
        void onTranslationLoaded(Translation translation);
        void onLoadError();
    }

    void getTranslation(String requestedText,
                                   LanguageDirection languageDirection,
                                   @NonNull LoadTranslationCallback callback);

    void retrieveLanguages();
    void retrieveLanguageDirections();
}
