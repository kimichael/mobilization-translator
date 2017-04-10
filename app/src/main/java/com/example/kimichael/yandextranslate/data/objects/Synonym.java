package com.example.kimichael.yandextranslate.data.objects;

/**
 * Created by mikim on 31.03.17.
 */
public class Synonym {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    private String partOfSpeech;
    private String genus;
}
