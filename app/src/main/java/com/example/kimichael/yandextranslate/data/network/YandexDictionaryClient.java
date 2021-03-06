package com.example.kimichael.yandextranslate.data.network;

import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kim Michael on 31.03.17.
 * Yandex.Dictionary API interface used by Retrofit
 */
public interface YandexDictionaryClient {

    String BASE_URL = "https://dictionary.yandex.net/api/v1/";
    // Language direction
    String LANG = "lang";
    // Translated text
    String TEXT = "text";

    @GET("dicservice.json/lookup")
    Single<Translation> getTranslation(@Query(TEXT) String text, @Query(LANG) String languageDirection);

    @GET("dicservice.json/getLangs")
    Single<List<LanguageDirection>> getLanguageDirections();
}
