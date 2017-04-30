package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.Interpretation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by Kim Michael on 31.03.17.
 * Helper class to compose JSON of our objects
 */
public class Composer {

    private Gson gson;

    public Composer() {
        gson = new GsonBuilder()
                .create();
    }

    public String composeDefinitionChildrenJson(List<Interpretation> interpretations) {
        return gson.toJson(interpretations);
    }
}
