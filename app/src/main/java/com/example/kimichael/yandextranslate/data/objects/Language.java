package com.example.kimichael.yandextranslate.data.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.kimichael.yandextranslate.data.provider.TranslationContract;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class Language implements Parcelable {

    private String name;
    private String languageCode;

    public Language(String name, String languageCode) {
        this.name = name;
        this.languageCode = languageCode;
    }

    public String getName() {
        return name;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TranslationContract.LanguageEntry.COLUMN_LANGUAGE_KEY, languageCode);
        contentValues.put(TranslationContract.LanguageEntry.COLUMN_LANGUAGE_NAME, name);
        return contentValues;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.languageCode);
    }

    protected Language(Parcel in) {
        this.name = in.readString();
        this.languageCode = in.readString();
    }

    public static final Parcelable.Creator<Language> CREATOR = new Parcelable.Creator<Language>() {
        @Override
        public Language createFromParcel(Parcel source) {
            return new Language(source);
        }

        @Override
        public Language[] newArray(int size) {
            return new Language[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        return name.equals(language.name) && languageCode.equals(language.languageCode);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + languageCode.hashCode();
        return result;
    }

    public static Language from(Cursor cursor) {
        return new Language(
                cursor.getString(cursor.getColumnIndex(TranslationContract.LanguageEntry.COLUMN_LANGUAGE_KEY)),
                cursor.getString(cursor.getColumnIndex(TranslationContract.LanguageEntry.COLUMN_LANGUAGE_NAME))
        );
    }
}
