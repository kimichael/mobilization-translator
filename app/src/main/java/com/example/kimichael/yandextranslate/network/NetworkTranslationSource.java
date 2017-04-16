package com.example.kimichael.yandextranslate.network;

import android.support.v4.app.LoaderManager;

import com.example.kimichael.yandextranslate.data.TranslationSource;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;

import java.util.List;


public interface NetworkTranslationSource extends TranslationSource {

    void retrieveLanguages(LoadLanguagesCallback callback);

    void retrieveLanguageDirections(LoadLanguageDirectionsCallback callback);

    interface LoadLanguagesCallback {
        void onLanguagesLoaded(List<Language> languages);
        void onLanguagesLoadError();
    }

    interface LoadLanguageDirectionsCallback {
        void onDirectionsLoaded(List<LanguageDirection> directions);
        void onDirectionsLoadError();
    }
}
