package com.example.kimichael.yandextranslate.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.provider.TranslationContract.DefinitionEntry;
import com.example.kimichael.yandextranslate.data.provider.TranslationContract.LanguageDirectionEntry;
import com.example.kimichael.yandextranslate.data.provider.TranslationContract.LanguageEntry;
import com.example.kimichael.yandextranslate.data.provider.TranslationContract.WordEntry;

/**
 * Created by Kim Michael on 31.03.17.
 * DB helper for database that keeps history, bookmarks and languages
 */
public class TranslationDbHelper extends SQLiteOpenHelper {

    // If you change the schema, you should increment the version
    private static final int DATABASE_VERSION = 25;

    private static final String DATABASE_NAME = "translation.db";

    public TranslationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table for words. Each word has a translation.
        // Also each word can have zero, one or more definitions.
        final String SQL_CREATE_WORD_TABLE = "CREATE TABLE " + WordEntry.TABLE_NAME + " (" +
                WordEntry._ID + " INTEGER PRIMARY KEY," +
                WordEntry.COLUMN_SRC_WORD + " TEXT NOT NULL, " +
                WordEntry.COLUMN_DEST_WORD + " TEXT NOT NULL, " +
                WordEntry.COLUMN_SRC_LANG + " TEXT NOT NULL, " +
                WordEntry.COLUMN_DEST_LANG + " TEXT NOT NULL," +
                WordEntry.COLUMN_BOOKMARK + " INTEGER, " +
                "UNIQUE (" + WordEntry.COLUMN_SRC_WORD + ", " +
                            WordEntry.COLUMN_SRC_LANG + ", " +
                            WordEntry.COLUMN_DEST_LANG + ") ON CONFLICT REPLACE"
                + " )";

        // Create a table for definitions. Each definition has one or more translations.
        final String SQL_CREATE_DEFINITION_TABLE = "CREATE TABLE " + DefinitionEntry.TABLE_NAME + " (" +
                DefinitionEntry._ID + " INTEGER PRIMARY KEY," +
                DefinitionEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                DefinitionEntry.COLUMN_PART_OF_SPEECH + " TEXT," +
                DefinitionEntry.COLUMN_TRANSCRIPTION + " TEXT," +
                DefinitionEntry.COLUMN_GENUS + " TEXT," +
                DefinitionEntry.COLUMN_SRC_LANG + " TEXT, " +
                DefinitionEntry.COLUMN_DEST_LANG + " TEXT, " +
                DefinitionEntry.COLUMN_JSON_CHILDREN + " TEXT," +
                DefinitionEntry.COLUMN_WORD_KEY + " INTEGER NOT NULL," +
                DefinitionEntry.COLUMN_ORDER + " INTEGER NOT NULL, " +
                "UNIQUE (" + DefinitionEntry.COLUMN_TEXT + ", " +
                            DefinitionEntry.COLUMN_SRC_LANG + ", " +
                            DefinitionEntry.COLUMN_DEST_LANG + ", " +
                            DefinitionEntry.COLUMN_ORDER + ") ON CONFLICT REPLACE, " +
                "FOREIGN KEY (" + DefinitionEntry.COLUMN_WORD_KEY + ") REFERENCES "
                    + WordEntry.TABLE_NAME +
                    "(" + WordEntry.COLUMN_SRC_WORD + ")" + ")";

        // Create a table for languages to choose from
        final String SQL_CREATE_LANGUAGE_TABLE = "CREATE TABLE " + LanguageEntry.TABLE_NAME + " (" +
                LanguageEntry._ID + " INTEGER PRIMARY KEY," +
                LanguageEntry.COLUMN_LANGUAGE_KEY + " TEXT NOT NULL, " +
                LanguageEntry.COLUMN_LANGUAGE_NAME + " TEXT NOT NULL, " +
                "UNIQUE(" + LanguageEntry.COLUMN_LANGUAGE_KEY + ") ON CONFLICT REPLACE" + " )";

        // Create a table for language directions
        final String SQL_CREATE_LANGUAGE_DIRECTION_TABLE = "CREATE TABLE " + LanguageDirectionEntry.TABLE_NAME + " (" +
                LanguageDirectionEntry._ID + " INTEGER PRIMARY KEY," +
                LanguageDirectionEntry.COLUMN_SRC_LANGUAGE_CODE + " TEXT NOT NULL," +
                LanguageDirectionEntry.COLUMN_DEST_LANGUAGE_CODE + " TEXT NOT NULL," +
                LanguageDirectionEntry.COLUMN_API_DICT_AVAILABLE + " INTEGER NOT NULL DEFAULT 1," +
                "UNIQUE (" + LanguageDirectionEntry.COLUMN_SRC_LANGUAGE_CODE + ", "
                           + LanguageDirectionEntry.COLUMN_DEST_LANGUAGE_CODE + ") ON CONFLICT REPLACE" + ")";


        db.execSQL(SQL_CREATE_WORD_TABLE);
        db.execSQL(SQL_CREATE_DEFINITION_TABLE);
        db.execSQL(SQL_CREATE_LANGUAGE_TABLE);
        db.execSQL(SQL_CREATE_LANGUAGE_DIRECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WordEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DefinitionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LanguageDirectionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LanguageEntry.TABLE_NAME);
        onCreate(db);
    }
}
