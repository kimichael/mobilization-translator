package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by mikim on 03.04.17.
 */

public class TranslationDeserializer implements JsonDeserializer<Translation> {

    private static final String TRANSLATED_WORD = "text";

    @Override
    public Translation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject simpleTranslationJson = json.getAsJsonObject();
        Translation translation = new Translation();
        translation.setTranslatedWord(simpleTranslationJson
                .get(TRANSLATED_WORD)
                .getAsJsonArray()
                .get(0)
                .getAsString());
        return translation;
    }
}
