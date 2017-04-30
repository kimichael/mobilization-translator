package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.Interpretation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Kim Michael on 31.03.17.
 * Helper class to help us parse JSON
 */
public class Parser {

    private Gson gson;

    public Parser() {
        gson = new GsonBuilder()
                .create();
    }

    public List<Interpretation> parseDefinitionChildren(String json) {
        Timber.d("Parsing json: " + json);
        return gson.fromJson(json, new TypeToken<List<Interpretation>>(){}.getType());
    }
}
