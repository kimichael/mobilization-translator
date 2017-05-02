package com.example.kimichael.yandextranslate.data.objects;

import java.util.List;

/**
 * Created by Kim Michael on 02.05.17
 * Builder for {@link Translation}
 */
public class TranslationBuilder {
    private List<Definition> definitions;
    private String srcWord;
    private String destWord;
    private boolean isMarked;
    private String translatedWord;

    public TranslationBuilder(String srcWord, String destWord) {
        this.srcWord = srcWord;
        this.destWord = destWord;
    }

    public TranslationBuilder setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
        return this;
    }

    public TranslationBuilder setIsMarked(boolean isMarked) {
        this.isMarked = isMarked;
        return this;
    }

    public TranslationBuilder setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
        return this;
    }

    public Translation build() {
        Translation translation = new Translation(srcWord, destWord);
        translation.setDefinitions(definitions);
        translation.setIsMarked(isMarked);
        return translation;
    }

}
