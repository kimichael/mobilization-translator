package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.Example;
import com.example.kimichael.yandextranslate.data.objects.Interpretation;
import com.example.kimichael.yandextranslate.data.objects.Meaning;
import com.example.kimichael.yandextranslate.data.objects.Synonym;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;


public class DefinitionChildrenSerializer implements JsonSerializer<List<Interpretation>> {

    private static final String INTERPRETATION_ARRAY = "tr";
    private static final String MEANING_ARRAY = "mean";
    private static final String TRANSLATION_ARRAY = "translations";
    private static final String EXAMPLE_ARRAY = "examples";
    private static final String SYNONYM_ARRAY = "syn";
    private static final String TEXT = "text";
    private static final String PART_OF_SPEECH = "pos";
    private static final String GENUS = "gen";

    @Override
    public JsonElement serialize(List<Interpretation> interpretationList, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        final JsonArray jsonArrayInterpretations = new JsonArray();
        for (Interpretation interpretation : interpretationList) {
            JsonObject interpretationJson = new JsonObject();
            if (interpretation.getDestWord() != null)
                interpretationJson.addProperty(TEXT, interpretation.getDestWord());
            if (interpretation.getPartOfSpeech() != null)
                interpretationJson.addProperty(PART_OF_SPEECH, interpretation.getPartOfSpeech());
            if (interpretation.getGenus() != null)
                interpretationJson.addProperty(GENUS, interpretation.getGenus());
            if (interpretation.getMeanings() != null) {
                JsonArray jsonMeaningArray = new JsonArray();
                for (Meaning meaning : interpretation.getMeanings()) {
                    JsonObject meaningJson = new JsonObject();
                    meaningJson.addProperty(TEXT, meaning.getText());
                    jsonMeaningArray.add(meaningJson);
                }
                interpretationJson.add(MEANING_ARRAY, jsonMeaningArray);
            }
            if (interpretation.getExamples() != null) {
                JsonArray jsonArrayExamples = new JsonArray();
                for (Example example : interpretation.getExamples()) {
                    JsonObject exampleJson = new JsonObject();
                    exampleJson.addProperty(TEXT, example.getText());
                    JsonArray jsonArrayTranslations = new JsonArray();
                    for (String translation : example.getTranslations()) {
                        JsonObject translationJson = new JsonObject();
                        translationJson.addProperty(TEXT, translation);
                        jsonArrayTranslations.add(translationJson);
                    }
                    exampleJson.add(TRANSLATION_ARRAY, jsonArrayTranslations);
                    jsonArrayExamples.add(exampleJson);
                }
                interpretationJson.add(EXAMPLE_ARRAY, jsonArrayExamples);
            }
            if (interpretation.getSynonyms() != null) {
                JsonArray jsonArraySynonyms = new JsonArray();
                for (Synonym synonym : interpretation.getSynonyms()) {
                    JsonObject synonymJson = new JsonObject();
                    synonymJson.addProperty(TEXT, synonym.getText());
                    synonymJson.addProperty(PART_OF_SPEECH, synonym.getPartOfSpeech());
                    synonymJson.addProperty(GENUS, synonym.getGenus());
                    jsonArraySynonyms.add(synonymJson);
                }
                interpretationJson.add(SYNONYM_ARRAY, jsonArraySynonyms);
            }
            jsonArrayInterpretations.add(interpretationJson);
        }
        jsonObject.add(INTERPRETATION_ARRAY, jsonArrayInterpretations);
        return jsonObject;
    }
}
