package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.Language;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class LanguagesDeserializer implements JsonDeserializer<List<Language>> {

    private static final String LANGUAGES = "langs";

    @Override
    public List<Language> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonObject languagesJson = jsonObject.get(LANGUAGES).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> set = languagesJson.entrySet();
        List<Language> languages = new ArrayList<>();
        for (Map.Entry<String, JsonElement> langEntry : set) {
            Language language = new Language(langEntry.getValue().getAsString(), langEntry.getKey());
            languages.add(language);
        };
        return languages;
    }
}
