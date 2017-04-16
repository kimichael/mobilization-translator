package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LanguageDirectionsDeserializer implements JsonDeserializer<List<LanguageDirection>> {
    @Override
    public List<LanguageDirection> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray directionsJson = json.getAsJsonArray();
        List<LanguageDirection> languageDirections = new ArrayList<>();
        for (JsonElement direction : directionsJson) {
            String directionString = direction.getAsString();
            String[] directions = directionString.split("-");
            LanguageDirection languageDirection = new LanguageDirection(directions[0],directions[1]);
            languageDirections.add(languageDirection);
        }
        return languageDirections;
    }
}
