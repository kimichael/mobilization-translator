package com.example.kimichael.yandextranslate.network;

import com.example.kimichael.yandextranslate.data.objects.Definition;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface YandexTranslateClient {

    String BASE_URL = "https://translate.yandex.net/api/v1.5/";
    // Text to translate
    String TEXT = "text";
    // Translation direction (ex. "ru-en")
    String LANG = "lang";
    // UI in which we will show the languages
    String UI = "ui";

    @POST("tr.json/translate")
    Call<Translation> getTranslation(@Query(TEXT) String text, @Query(LANG) String langDirection);

    @GET("tr.json/getLangs")
    Call<List<Language>> getLanguages(@Query(UI) String ui);
}