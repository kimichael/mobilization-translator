package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.Definition;
import com.example.kimichael.yandextranslate.data.objects.Interpretation;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Kim Michael on 31.03.17.
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
