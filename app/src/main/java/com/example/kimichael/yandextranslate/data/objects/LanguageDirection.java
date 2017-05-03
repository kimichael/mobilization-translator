package com.example.kimichael.yandextranslate.data.objects;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.kimichael.yandextranslate.data.provider.TranslationContract;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class LanguageDirection {
    private String srcLangCode, destLangCode;

    public LanguageDirection(String srcLangCode, String destLangCode) {
        this.srcLangCode = srcLangCode;
        this.destLangCode = destLangCode;
    }

    public LanguageDirection(Language srcLang, Language destLanguage) {
        this.srcLangCode = srcLang.getLanguageCode();
        this.destLangCode = destLanguage.getLanguageCode();
    }

    public String getLanguageDirectionForApi() {
        return srcLangCode + "-" + destLangCode;
    }

    public String getSrcLangCode() {
        return srcLangCode;
    }

    public void setSrcLangCode(String srcLangCode) {
        this.srcLangCode = srcLangCode;
    }

    public String getDestLangCode() {
        return destLangCode;
    }

    public void setDestLangCode(String destLangCode) {
        this.destLangCode = destLangCode;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TranslationContract.LanguageDirectionEntry.COLUMN_SRC_LANGUAGE_CODE, srcLangCode);
        contentValues.put(TranslationContract.LanguageDirectionEntry.COLUMN_DEST_LANGUAGE_CODE, destLangCode);
        return contentValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LanguageDirection that = (LanguageDirection) o;

        if (!srcLangCode.equals(that.srcLangCode)) return false;
        return destLangCode.equals(that.destLangCode);

    }

    @Override
    public int hashCode() {
        int result = srcLangCode.hashCode();
        result = 31 * result + destLangCode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return srcLangCode.toUpperCase() + "-" + destLangCode.toUpperCase();
    }

    public void swapLanguages() {
        String temp = srcLangCode;
        srcLangCode = destLangCode;
        destLangCode = temp;
    }

    public static LanguageDirection from(Cursor cursor) {
        return new LanguageDirection(
                cursor.getString(cursor.getColumnIndex(TranslationContract.LanguageDirectionEntry.COLUMN_SRC_LANGUAGE_CODE)),
                cursor.getString(cursor.getColumnIndex(TranslationContract.LanguageDirectionEntry.COLUMN_DEST_LANGUAGE_CODE))
        );
    }
}
