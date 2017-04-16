package com.example.kimichael.yandextranslate.data;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface TranslationSource {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({YANDEX_TRANSLATE_API, YANDEX_DICTIONARY_API})
    @interface TranslationApi {}
    int YANDEX_TRANSLATE_API = 0;
    int YANDEX_DICTIONARY_API = 1;

    interface LoadDictionaryTranslationCallback {
        void onDictionaryTranslationLoaded(DictionaryTranslation dictionaryTranslation);
    }

    interface LoadTranslationCallback {
        void onTranslationLoaded(Translation translation);
    }


    void getTranslation(String requestedWord, @TranslationApi int translationApi, @NonNull LoadDictionaryTranslationCallback callback);

    void saveTranslation(@NonNull DictionaryTranslation dictionaryTranslation);


}
