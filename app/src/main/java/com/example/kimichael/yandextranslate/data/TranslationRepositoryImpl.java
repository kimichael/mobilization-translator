package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Translation;

public class TranslationRepositoryImpl implements TranslationRepository {

    //TODO Finish this class

    TranslationSource mLocalTranslationSource;
    TranslationSource mNetworkTranslationSource;

    public TranslationRepositoryImpl(TranslationSource localTranslationSource,
                                     TranslationSource networkTranslationSource) {
    }


    @Override
    public void getTranslationFromNetwork(String requestedWord, @NonNull LoadTranslationCallback callback) {

    }

    @Override
    public void getTranslationFromDb(String requestedWord, @NonNull LoadTranslationCallback callback) {

    }

    @Override
    public void saveTranslation(@NonNull DictionaryTranslation dictionaryTranslation) {

    }

    @Override
    public void saveTranslation(@NonNull Translation translation) {

    }
}
