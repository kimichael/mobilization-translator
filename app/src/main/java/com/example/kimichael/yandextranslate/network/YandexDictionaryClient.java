package com.example.kimichael.yandextranslate.network;

import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YandexDictionaryClient {

    String BASE_URL = "https://dictionary.yandex.net/api/v1/";
    // Language direction
    String LANG = "lang";
    // Translated text
    String TEXT = "text";

    @GET("dicservice.json/lookup")
    Call<DictionaryTranslation> getDictionaryTranslation(@Query(TEXT) String text, @Query(LANG) String languageDirection);

    @GET("dicservice.json/getLangs")
    Call<List<LanguageDirection>> getLanguageDirections();
}
