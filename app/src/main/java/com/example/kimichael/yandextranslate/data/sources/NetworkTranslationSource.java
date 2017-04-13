package com.example.kimichael.yandextranslate.data.sources;

import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.TranslationSource;
import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;

import retrofit2.Retrofit;


public class NetworkTranslationSource implements TranslationSource {

    Retrofit mRetrofit;


    public NetworkTranslationSource() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl()
    }

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
