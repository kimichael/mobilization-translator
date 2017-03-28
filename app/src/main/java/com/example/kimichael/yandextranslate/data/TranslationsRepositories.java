package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

/**
 * Created by mikim on 28.03.17.
 */

public class TranslationsRepositories {

    private TranslationsRepositories() {}

    private static TranslationsRepository repository = null;

    public synchronized static TranslationsRepository getRepositoryInstance(@NonNull TranslationsServiceApi translationsServiceApi) {
        if (null == repository)
            repository = new TranslationsRepositoryImpl(translationsServiceApi);
        return repository;
    }
}
