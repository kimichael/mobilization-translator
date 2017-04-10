package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Translation;

public interface TranslationSource {

    interface LoadDictionaryTranslationCallback {
        void onDictionaryTranslationLoaded(DictionaryTranslation dictionaryTranslation);
    }

    interface LoadTranslationCallback {
        void onTranslationLoaded(Translation translation);
    }

    void getTranslation(String requestedWord, @NonNull LoadDictionaryTranslationCallback callback);

    void saveTranslation(@NonNull DictionaryTranslation dictionaryTranslation);
}
