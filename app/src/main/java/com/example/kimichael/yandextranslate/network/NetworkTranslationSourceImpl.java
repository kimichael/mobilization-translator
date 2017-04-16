package com.example.kimichael.yandextranslate.network;

import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.network.NetworkTranslationSource;
import com.example.kimichael.yandextranslate.network.YandexDictionaryClient;
import com.example.kimichael.yandextranslate.network.YandexTranslateClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NetworkTranslationSourceImpl implements NetworkTranslationSource {

    private YandexTranslateClient yandexTranslateClient;
    private YandexDictionaryClient yandexDictionaryClient;

    public NetworkTranslationSourceImpl(YandexTranslateClient yandexTranslateClient,
                                        YandexDictionaryClient yandexDictionaryClient) {
        this.yandexTranslateClient = yandexTranslateClient;
        this.yandexDictionaryClient = yandexDictionaryClient;
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

    @Override
    public void retrieveLanguages(final LoadLanguagesCallback callback) {
        yandexTranslateClient.getLanguages("ru").enqueue(new Callback<List<Language>>() {
                @Override
                public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {
                    callback.onLanguagesLoaded(response.body());
                }

                @Override
                public void onFailure(Call<List<Language>> call, Throwable t) {
                    callback.onLanguagesLoadError();
                }
        });
    }

    @Override
    public void retrieveLanguageDirections(final LoadLanguageDirectionsCallback callback) {
        yandexDictionaryClient.getLanguageDirections().enqueue(new Callback<List<LanguageDirection>>() {
            @Override
            public void onResponse(Call<List<LanguageDirection>> call, Response<List<LanguageDirection>> response) {
                callback.onDirectionsLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<LanguageDirection>> call, Throwable t) {
                callback.onDirectionsLoadError();
            }
        });
    }
}
