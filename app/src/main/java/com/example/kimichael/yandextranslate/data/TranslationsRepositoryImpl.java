package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

/**
 * Created by mikim on 26.03.17.
 */

public class TranslationsRepositoryImpl implements TranslationsRepository {

    TranslationsServiceApi mTranslationsServiceApi;

    public TranslationsRepositoryImpl(TranslationsServiceApi translationsServiceApi) {
        mTranslationsServiceApi = translationsServiceApi;
    }

    @Override
    public void getTranslation(@NonNull LoadTranslationCallback callback) {

    }

    @Override
    public void getTranslations(@NonNull LoadTranslationsCallback callback) {

    }

    @Override
    public void saveTranslation(@NonNull Translation translation) {

    }
}
