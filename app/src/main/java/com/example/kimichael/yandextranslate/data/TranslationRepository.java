package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Translation;

public interface TranslationRepository {

    interface LoadTranslationCallback {
        void onTranslationLoaded(DictionaryTranslation dictionaryTranslation);
    }

    void getTranslationFromNetwork(String requestedWord, @NonNull LoadTranslationCallback callback);
    void getTranslationFromDb(String requestedWord, @NonNull LoadTranslationCallback callback);

    void saveTranslation(@NonNull DictionaryTranslation dictionaryTranslation);
    void saveTranslation(@NonNull Translation translation);
}
