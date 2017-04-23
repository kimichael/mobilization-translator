package com.example.kimichael.yandextranslate.data.objects;

/**
 * Created by Kim Michael on 23.04.17
 */
public class HistoryRecord {
    private Translation translation;

    public HistoryRecord(Translation translation, LanguageDirection languageDirection) {
        this.translation = translation;
        this.languageDirection = languageDirection;
    }

    public LanguageDirection getLanguageDirection() {
        return languageDirection;
    }

    public void setLanguageDirection(LanguageDirection languageDirection) {
        this.languageDirection = languageDirection;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    private LanguageDirection languageDirection;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryRecord that = (HistoryRecord) o;

        if (!translation.equals(that.translation)) return false;
        return languageDirection.equals(that.languageDirection);

    }

    @Override
    public int hashCode() {
        int result = translation.hashCode();
        result = 31 * result + languageDirection.hashCode();
        return result;
    }
}
