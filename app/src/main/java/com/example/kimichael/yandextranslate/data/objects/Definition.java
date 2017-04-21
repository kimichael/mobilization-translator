package com.example.kimichael.yandextranslate.data.objects;

import android.content.ContentValues;

import com.example.kimichael.yandextranslate.data.provider.TranslationContract;
import com.example.kimichael.yandextranslate.parse.Composer;

import java.util.List;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class Definition {

    private String srcWord;
    private String partOfSpeech;
    // This is order in which this definition appears in translation
    private int order;
    private String genus;
    private String transcription;
    List<Interpretation> interpretations;

    private LanguageDirection languageDirection;

    public Definition() {}

    public Definition(String srcWord, String partOfSpeech, int order, String genus, String transcription, List<Interpretation> interpretations) {
        this.srcWord = srcWord;
        this.partOfSpeech = partOfSpeech;
        this.order = order;
        this.genus = genus;
        this.transcription = transcription;
        this.interpretations = interpretations;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
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

    public void setInterpretations(List<Interpretation> interpretations) {
        this.interpretations = interpretations;
    }

    public LanguageDirection getLanguageDirection() {
        return languageDirection;
    }

    public void setLanguageDirection(LanguageDirection languageDirection) {
        this.languageDirection = languageDirection;
    }

    public ContentValues toContentValues(Composer composer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TranslationContract.DefinitionEntry.COLUMN_TEXT, srcWord);
        contentValues.put(TranslationContract.DefinitionEntry.COLUMN_ORDER, order);
        contentValues.put(TranslationContract.DefinitionEntry.COLUMN_PART_OF_SPEECH, partOfSpeech);
        contentValues.put(TranslationContract.DefinitionEntry.COLUMN_TRANSCRIPTION, transcription);
        contentValues.put(TranslationContract.DefinitionEntry.COLUMN_GENUS, genus);
        contentValues.put(TranslationContract.DefinitionEntry.COLUMN_JSON_CHILDREN,
                composer.composeDefinitionChildrenJson(interpretations));
        contentValues.put(TranslationContract.DefinitionEntry.COLUMN_SRC_LANG, languageDirection.getSrcLangCode());
        contentValues.put(TranslationContract.DefinitionEntry.COLUMN_DEST_LANG, languageDirection.getDestLangCode());
        contentValues.put(TranslationContract.DefinitionEntry.COLUMN_WORD_KEY, srcWord);
        return contentValues;
    }
}
