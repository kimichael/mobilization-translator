package com.example.kimichael.yandextranslate.data.objects;

import java.util.List;

/**
 * Created by mikim on 31.03.17.
 */
public class Example {
    private String text;

    public List<String> getTranslations() {
        return translations;
    }

    public void setTranslations(List<String> translations) {
        this.translations = translations;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private List<String> translations;
}
