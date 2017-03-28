package com.example.kimichael.yandextranslate.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TranslationContract {

    public final static String CONTENT_AUTHORITY = "com.example.kimichael.yandextranslate";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TRANSLATION = "translation";
    public static final String PATH_WORD = "word";
    public static final String PATH_LANGUAGE = "language";

    // This table stores history of translated words
    public static final class WordEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORD).build();

        // Content type is used in ContentProvider#getType
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORD;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORD;

        // Table name
        public static final String TABLE_NAME = "word";

        // Word, which will have translations in this table
        public static final String COLUMN_WORD = "word";
        // "Yandex.Translator" app only caches one-way translation. Foreign keys to Language table
        public static final String COLUMN_SRC_LANG = "src_lang";
        public static final String COLUMN_DEST_LANG = "dest_lang";
        // Flag that marks if translation is favourited or not
        public static final String COLUMN_FAVOURITED = "favourited";
        // Word transcription
        public static final String COLUMN_TRANSCRIPTION = "transcription";
        // Part of speech
        public static final String COLUMN_POS = "pos";
    }

    // This table stores translated words
    public static final class TranslationEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSLATION).build();

        // Content type is used in ContentProvider#getType
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRANSLATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRANSLATION;

        // Table name
        public static final String TABLE_NAME = "translation";

        // Word transcription
        public static final String COLUMN_TRANSCRIPTION = "transcription";
        // TODO: finish this class
    }
}
