package com.example.kimichael.yandextranslate.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kimichael.yandextranslate.data.TranslationContract.*;

public class TranslationDbHelper extends SQLiteOpenHelper {

    // If you change the schema, you should increment the version
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "translation.db";

    public TranslationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table for words. Each word has a translation.
        // Also each word can have zero, one or more definitions.
        final String SQL_CREATE_WORD_TABLE = "CREATE TABLE " + WordEntry.TABLE_NAME + " (" +
                WordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WordEntry.COLUMN_SRC_WORD + " TEXT NOT NULL, " +
                WordEntry.COLUMN_DEST_WORD + " TEXT NOT NULL, " +
                WordEntry.COLUMN_SRC_LANG + " TEXT NOT NULL, " +
                WordEntry.COLUMN_DEST_LANG + " TEXT NOT NULL" + " )";

        // Create a table for definitions. Each definition has one or more translations.
        final String SQL_CREATE_DEFINITION_TABLE = "CREATE TABLE " + DefinitionEntry.TABLE_NAME + " (" +
                DefinitionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DefinitionEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                DefinitionEntry.COLUMN_PART_OF_SPEECH + " TEXT NOT NULL," +
                DefinitionEntry.COLUMN_TRANSCRIPTION + " TEXT NOT NULL," +
                DefinitionEntry.COLUMN_JSON_CHILDREN + " TEXT," +
                DefinitionEntry.COLUMN_WORD_KEY + " INTEGER NOT NULL," +
                DefinitionEntry.COLUMN_ORDER + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + DefinitionEntry.COLUMN_WORD_KEY + ") REFERENCES "
                    + WordEntry.TABLE_NAME +
                    "(" + WordEntry._ID + ")" + ")";

        // Create a table for languages to choose from
        final String SQL_CREATE_LANGUAGE_TABLE = "CREATE TABLE " + LanguageEntry.TABLE_NAME + " (" +
                LanguageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LanguageEntry.COLUMN_LANGUAGE_KEY + " TEXT NOT NULL, " +
                LanguageEntry.COLUMN_LANGUAGE_NAME + " TEXT NOT NULL" + " )";

        // Create a table for language directions
        final String SQL_CREATE_LANGUAGE_DIRECTION_TABLE = "CREATE TABLE " + LanguageDirectionEntry.TABLE_NAME + " (" +
                LanguageDirectionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LanguageDirectionEntry.COLUMN_SRC_LANGUAGE + " TEXT NOT NULL," +
                LanguageDirectionEntry.COLUMN_DEST_LANGUAGE + " TEXT NOT NULL," +
                LanguageDirectionEntry.COLUMN_API_DICT_AVAILABLE + " INTEGER NOT NULL" + " )";


        db.execSQL(SQL_CREATE_WORD_TABLE);
        db.execSQL(SQL_CREATE_DEFINITION_TABLE);
        db.execSQL(SQL_CREATE_LANGUAGE_TABLE);
        db.execSQL(SQL_CREATE_LANGUAGE_DIRECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
