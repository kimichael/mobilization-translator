package com.example.kimichael.yandextranslate.data.objects;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by mikim on 31.03.17.
 */
public class Interpretation {

    private String destWord;
    private String partOfSpeech;

    public String getDestWord() {
        return destWord;
    }

    public void setDestWord(String destWord) {
        this.destWord = destWord;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    @Nullable
    public List<Synonym> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(@Nullable List<Synonym> synonyms) {
        this.synonyms = synonyms;
    }

    @Nullable
    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(@Nullable List<Meaning> meanings) {
        this.meanings = meanings;
    }

    @Nullable
    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(@Nullable List<Example> examples) {
        this.examples = examples;
    }

    private String genus;
    @Nullable
    List<Synonym> synonyms;
    @Nullable
    List<Meaning> meanings;
    @Nullable
    List<Example> examples;


}
