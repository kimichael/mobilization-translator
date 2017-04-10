package com.example.kimichael.yandextranslate.data.objects;

import android.support.annotation.Nullable;

import java.util.List;

public class DictionaryTranslation extends Translation {

    @Nullable
    private List<Definition> definitions;

    @Nullable
    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(@Nullable List<Definition> definitions) {
        this.definitions = definitions;
    }

    public boolean isFullTranslation() {
        return (null != definitions);
    }
}