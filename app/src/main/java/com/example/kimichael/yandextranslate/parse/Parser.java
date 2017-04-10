package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Parser {

    Gson gson;

    public Parser() {
        gson = new GsonBuilder()
                .registerTypeAdapter(DictionaryTranslation.class, new DictionaryTranslationDeserializer())
                .registerTypeAdapter(Translation.class, new TranslationDeserializer())
                .create();
    }

    public DictionaryTranslation parseDictionaryTranslation(String translationJson) {
        return gson.fromJson(translationJson, DictionaryTranslation.class);
    }

    public Translation parseTranslation(String translationJson) {
        return gson.fromJson(translationJson, Translation.class);
    }
}
