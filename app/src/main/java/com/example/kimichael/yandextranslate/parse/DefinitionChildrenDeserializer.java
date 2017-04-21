package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.Example;
import com.example.kimichael.yandextranslate.data.objects.Interpretation;
import com.example.kimichael.yandextranslate.data.objects.Meaning;
import com.example.kimichael.yandextranslate.data.objects.Synonym;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class DefinitionChildrenDeserializer implements JsonDeserializer<List<Interpretation>> {

    private static final String INTERPRETATION_ARRAY = "tr";
    private static final String SYNONYM_ARRAY = "syn";
    private static final String MEANING_ARRAY = "mean";
    private static final String EXAMPLE_ARRAY = "ex";
    private static final String EXAMPLE_TRANSLATION_ARRAY = "tr";

    private static final String TEXT = "text";
    private static final String PART_OF_SPEECH = "pos";
    private static final String GENUS = "gen";

    @Override
    public List<Interpretation> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Timber.d("Parsing json : " + json.toString());
        JsonObject interpretationsJson = json.getAsJsonObject();
        JsonArray interpretationJsonArray = interpretationsJson.getAsJsonArray(INTERPRETATION_ARRAY);
        List<Interpretation> interpretations = new ArrayList<>();
        for (JsonElement interpretationElement : interpretationJsonArray) {
            final JsonObject interpretationJson = interpretationElement.getAsJsonObject();

            Interpretation interpretation = new Interpretation();
            interpretation.setDestWord(getAsString(interpretationJson.get(TEXT)));
            interpretation.setPartOfSpeech(getAsString(interpretationJson.get(PART_OF_SPEECH)));
            interpretation.setGenus(getAsString(interpretationJson.get(GENUS)));

            if (interpretationJson.has(SYNONYM_ARRAY)) {
                final JsonArray synonymsJson = interpretationJson.getAsJsonArray(SYNONYM_ARRAY);
                List<Synonym> synonyms = new ArrayList<>();
                for (JsonElement synonymElement : synonymsJson) {
                    final JsonObject synonymJson = synonymElement.getAsJsonObject();

                    Synonym synonym = new Synonym();
                    synonym.setText(getAsString(synonymJson.get(TEXT)));
                    synonym.setPartOfSpeech(getAsString(synonymJson.get(PART_OF_SPEECH)));
                    synonym.setGenus(getAsString(synonymJson.get(GENUS)));

                    synonyms.add(synonym);
                }
                interpretation.setSynonyms(synonyms);
            }

            if (interpretationJson.has(MEANING_ARRAY)) {
                final JsonArray meaningsJson = interpretationJson.getAsJsonArray(MEANING_ARRAY);
                List<Meaning> meanings = new ArrayList<>();
                for (JsonElement meaningElement : meaningsJson) {
                    final JsonObject meaningJson = meaningElement.getAsJsonObject();

                    Meaning meaning = new Meaning();
                    meaning.setText(getAsString(meaningJson.get(TEXT)));
                    meanings.add(meaning);
                }
                interpretation.setMeanings(meanings);
            }

            if (interpretationJson.has(EXAMPLE_ARRAY)) {
                final JsonArray examplesJson = interpretationJson.getAsJsonArray(EXAMPLE_ARRAY);
                List<Example> examples = new ArrayList<>();
                for (JsonElement exampleElement : examplesJson) {
                    final JsonObject exampleJson = exampleElement.getAsJsonObject();

                    Example example = new Example();
                    example.setText(getAsString(exampleJson.get(TEXT)));
                    List<String> exampleTranslations = new ArrayList<>();
                    for (JsonElement exampleTranslationElement :
                            exampleJson.getAsJsonArray(EXAMPLE_TRANSLATION_ARRAY)) {
                        exampleTranslations.add(
                                getAsString(exampleTranslationElement.getAsJsonObject()
                                        .get(TEXT)));
                    }
                    example.setTranslations(exampleTranslations);
                    examples.add(example);
                }
                interpretation.setExamples(examples);
            }

            interpretations.add(interpretation);
        }
        return interpretations;
    }

    private static String getAsString(JsonElement element) {
        return (element == null) ? "" : element.getAsString();
    }
}
