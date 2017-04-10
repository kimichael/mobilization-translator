package com.example.kimichael.yandextranslate.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TranslationContract {

    public final static String CONTENT_AUTHORITY = "com.example.kimichael.yandextranslate";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WORD = "word";
    public static final String PATH_DEFINITION = "definition";
    public static final String PATH_TRANSLATION = "translation";
    public static final String PATH_SYNONYM = "synonym";
    public static final String PATH_MEANING = "meaning";
    public static final String PATH_EXAMPLE = "example";
    public static final String PATH_LANGUAGE = "language";
    public static final String PATH_LANGUAGE_DIRECTION = "lang_direction";

    // This table stores history of translated words
    public static final class WordEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORD).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORD;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORD;

        public static final String TABLE_NAME = "word";

        // Word properties
        // Word, which translation is needed. Primary key
        public static final String COLUMN_SRC_WORD = "src_word";
        // Translated word
        public static final String COLUMN_DEST_WORD = "dest_word";
        // Language, from which we translate the word
        public static final String COLUMN_SRC_LANG = "src_lang";
        // Language, to which we translate the word
        public static final String COLUMN_DEST_LANG = "dest_lang";

    }

    // DictionaryTranslation can have several definitions.
    public static final class DefinitionEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DEFINITION).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEFINITION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEFINITION;

        public static final String TABLE_NAME = "definition";

        // Definition itself in source language
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_PART_OF_SPEECH = "pos";
        public static final String COLUMN_TRANSCRIPTION = "transcription";

        // Definition refers to its word
        public static final String COLUMN_WORD_KEY = "word_id";
    }

    // Each definition can have several translations
    public static final class TranslationEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSLATION).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRANSLATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRANSLATION;

        public static final String TABLE_NAME = "translation";

        // Text of translation. It is in destination language
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_PART_OF_SPEECH = "pos";
        public static final String COLUMN_GENUS = "gen";

        // Translation refers to its definition
        public static final String COLUMN_DEFINITION_KEY = "definition_id";
    }

    // Translation can have several synonyms
    public static final class SynonymEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SYNONYM).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRANSLATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRANSLATION;

        public static final String TABLE_NAME = "synonym";

        // Synonym properties
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_PART_OF_SPEECH = "pos";
        public static final String COLUMN_GENUS = "gen";

        // Synonym refers to translation
        public static final String COLUMN_TRANSLATION_KEY = "translation_id";
    }

    // Translation can also have several meanings
    public static final class MeaningEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEANING).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEANING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEANING;

        public static final String TABLE_NAME = "meaning";

        // Meaning of translation
        public static final String COLUMN_TEXT = "text";

        public static final String COLUMN_TRANSLATION_KEY = "translation_id";
    }

    // Translation can also have several examples
    public static final class ExampleEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXAMPLE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXAMPLE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXAMPLE;

        public static final String TABLE_NAME = "example";

        public static final String COLUMN_SRC_TEXT = "src_text";
        public static final String COLUMN_DEST_TEXT = "dest_text";

        public static final String COLUMN_TRANSLATION_KEY = "translation_id";
    }

    public static final class LanguageEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LANGUAGE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LANGUAGE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LANGUAGE;

        public static final String TABLE_NAME = "language";

        public static final String COLUMN_LANGUAGE_KEY = "lang_code";
        public static final String COLUMN_LANGUAGE_NAME = "name";
    }

    // Table which determines, whether you can use dictionary API for these two languages
    public static final class LanguageDirectionEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LANGUAGE_DIRECTION).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LANGUAGE_DIRECTION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LANGUAGE_DIRECTION;

        public static final String TABLE_NAME = "lang_direction";

        public static final String COLUMN_SRC_LANGUAGE = "src_lang";
        public static final String COLUMN_DEST_LANGUAGE = "dest_lang";
        public static final String COLUMN_API_DICT_AVAILABLE = "dict_api_available";
    }
}
