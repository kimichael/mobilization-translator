package com.example.kimichael.yandextranslate;

import com.example.kimichael.yandextranslate.data.TranslationsRepositories;
import com.example.kimichael.yandextranslate.data.TranslationsRepository;
import com.example.kimichael.yandextranslate.data.TranslationsServiceApiImpl;

/**
 * Created by mikim on 29.03.17.
 */

public class Injector {
    public static TranslationsRepository provideTranslationsRepository() {
        return TranslationsRepositories.getRepositoryInstance(new TranslationsServiceApiImpl());
    }
}
