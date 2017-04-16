package com.example.kimichael.yandextranslate.data.objects;

import java.util.List;

/**
 * Created by mikim on 31.03.17.
 */
public class Definition {

    private String srcWord;
    private String partOfSpeech;
    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getSrcWord() {
        return srcWord;
    }

    public void setSrcWord(String srcWord) {
        this.srcWord = srcWord;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public List<Interpretation> getInterpretations() {
        return interpretations;
    }

    public void setInterpretations(List<Interpretation> interpretations) { this.interpretations = interpretations; }

    private String transcription;
    List<Interpretation> interpretations;

}
