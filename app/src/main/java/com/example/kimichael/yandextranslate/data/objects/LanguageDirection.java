package com.example.kimichael.yandextranslate.data.objects;

import android.content.ContentValues;

import com.example.kimichael.yandextranslate.data.TranslationContract;

public class LanguageDirection {
    private String srcLangCode, destLangCode;

    public LanguageDirection(String srcLangCode, String destLangCode) {
        this.srcLangCode = srcLangCode;
        this.destLangCode = destLangCode;
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
}
