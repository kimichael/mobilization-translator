package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.TranslationSource;
import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;


public class LocalTranslationSource implements TranslationSource {
    //TODO Finish this class
    @Override
    public void getTranslation(String requestedWord,
                               @TranslationApi int translationApi,
                               @NonNull LoadDictionaryTranslationCallback callback) {

    }

    @Override
    public void saveTranslation(@NonNull DictionaryTranslation dictionaryTranslation) {

    }
}
