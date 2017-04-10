package com.example.kimichael.yandextranslate.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kimichael.yandextranslate.data.TranslationContract.*;

public class TranslationDbHelper extends SQLiteOpenHelper {

    // If you change the schema, you should increment the version
    private static final int DATABASE_VERSION = 1;

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
                DefinitionEntry.COLUMN_WORD_KEY + " INTEGER NOT NULL," +
                " FOREIGN KEY (" + DefinitionEntry.COLUMN_WORD_KEY + ") REFERENCES " +
                        WordEntry.TABLE_NAME + " (" + WordEntry._ID + ")" + " )";

        // Create a table for translations. Each translation has zero, one or more synonyms, meaning and examples.
        final String SQL_CREATE_TRANSLATION_TABLE = "CREATE TABLE " + TranslationEntry.TABLE_NAME + " (" +
                TranslationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TranslationEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                TranslationEntry.COLUMN_PART_OF_SPEECH + " TEXT, " +
                TranslationEntry.COLUMN_GENUS + " TEXT, " +
                " FOREIGN KEY (" + TranslationEntry.COLUMN_DEFINITION_KEY + ") REFERENCES " +
                DefinitionEntry.TABLE_NAME + " (" + DefinitionEntry._ID + ")" + " )";

        // Create a table for synonyms. One meaning is tied to one translation
        final String SQL_CREATE_SYNONYM_TABLE = "CREATE TABLE " + SynonymEntry.TABLE_NAME + " (" +
                SynonymEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SynonymEntry.COLUMN_TEXT + " TEXT NOT NULL," +
                SynonymEntry.COLUMN_PART_OF_SPEECH + " TEXT NOT NULL, " +
                SynonymEntry.COLUMN_GENUS + " TEXT NOT NULL, " +
                SynonymEntry.COLUMN_TRANSLATION_KEY + " INTEGER NOT NULL," +
                "FOREIGN KEY (" + SynonymEntry.COLUMN_TRANSLATION_KEY + ") REFERENCES " +
                TranslationEntry.TABLE_NAME + " (" + TranslationEntry._ID + ")" + ")";

        // Create a table for meanings. One meaning is tied to one translation
        final String SQL_CREATE_MEANING_TABLE = "CREATE TABLE " + MeaningEntry.TABLE_NAME + " (" +
                MeaningEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MeaningEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                MeaningEntry.COLUMN_TRANSLATION_KEY + " INTEGER NOT NULL," +
                "FOREIGN KEY (" + MeaningEntry.COLUMN_TRANSLATION_KEY + ") REFERENCES " +
                TranslationEntry.TABLE_NAME + " (" + TranslationEntry._ID + ")" + ")";

        // Create a table for examples. One example is tied to one translation
        final String SQL_CREATE_EXAMPLE_TABLE = "CREATE TABLE " + ExampleEntry.TABLE_NAME + " (" +
                ExampleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ExampleEntry.COLUMN_SRC_TEXT + " TEXT NOT NULL," +
                ExampleEntry.COLUMN_DEST_TEXT + " TEXT NOT NULL," +
                ExampleEntry.COLUMN_TRANSLATION_KEY + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + ExampleEntry.COLUMN_TRANSLATION_KEY + ") REFERENCES " +
                TranslationEntry.TABLE_NAME + " (" + TranslationEntry._ID + ") " + ")";

        // Create a table for language. Each language has its code and formatted language
        final String SQL_CREATE_LANGUAGE_TABLE = "CREATE TABLE " + LanguageEntry.TABLE_NAME + " (" +
                LanguageEntry.COLUMN_LANGUAGE_KEY + " TEXT PRIMARY KEY, " +
                LanguageEntry.COLUMN_LANGUAGE_NAME + " TEXT NOT NULL" + " )";

        final String SQL_CREATE_LANGUAGE_DIRECTION_TABLE = "CREATE TABLE " + LanguageDirectionEntry.TABLE_NAME + " (" +
                LanguageDirectionEntry.COLUMN_SRC_LANGUAGE + " TEXT NOT NULL," +
                LanguageDirectionEntry.COLUMN_DEST_LANGUAGE + " TEXT NOT NULL, " +
                // It is a boolean data
                LanguageDirectionEntry.COLUMN_API_DICT_AVAILABLE + " INTEGER NOT NULL," +
                "FOREIGN KEY (" + LanguageDirectionEntry.COLUMN_SRC_LANGUAGE + ") REFERENCES " +
                LanguageEntry.TABLE_NAME + " (" + LanguageEntry.COLUMN_LANGUAGE_KEY + ") " + ")";


        db.execSQL(SQL_CREATE_WORD_TABLE);
        db.execSQL(SQL_CREATE_DEFINITION_TABLE);
        db.execSQL(SQL_CREATE_TRANSLATION_TABLE);
        db.execSQL(SQL_CREATE_MEANING_TABLE);
        db.execSQL(SQL_CREATE_SYNONYM_TABLE);
        db.execSQL(SQL_CREATE_EXAMPLE_TABLE);
        db.execSQL(SQL_CREATE_LANGUAGE_TABLE);
        db.execSQL(SQL_CREATE_LANGUAGE_DIRECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
