package com.example.kimichael.yandextranslate.data.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kim Michael on 31.03.17.
 */
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

        public static Uri buildWordUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWordWithSrcWordAndLangDirectionSetting(String word, String srcLang, String destLang) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_SRC_WORD, word)
                    .appendPath(srcLang)
                    .appendPath(destLang)
                    .build();
        }

        public static String getWordSettingFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_SRC_WORD);
        }

        public static String getSrcLangSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDestLangSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

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
        public static final String COLUMN_GENUS = "genus";
        // Each definition must stay in order we get it from service
        public static final String COLUMN_ORDER = "def_order";
        // Definition has a translations, which has meanings, synonyms and examples
        // We will show it as a json text
        public static final String COLUMN_JSON_CHILDREN = "json_children";

        public static final String COLUMN_WORD_KEY = "word";
        public static final String COLUMN_SRC_LANG = "src_lang";
        public static final String COLUMN_DEST_LANG = "dest_lang";

        public static Uri buildDefinitionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildDefinitionWithWordAndLangDirection(String word,
                                                                  String srcLang,
                                                                  String destLang) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_WORD_KEY, word)
                    .appendQueryParameter(COLUMN_SRC_LANG, srcLang)
                    .appendQueryParameter(COLUMN_DEST_LANG, destLang)
                    .build();
        }

        public static String getWordFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_WORD_KEY);
        }

        public static String getSrcLangFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_SRC_LANG);
        }

        public static String getDestLangFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DEST_LANG);
        }
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

        public static Uri buildLanguageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
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

        public static final String COLUMN_SRC_LANGUAGE_CODE = "src_lang";
        public static final String COLUMN_DEST_LANGUAGE_CODE = "dest_lang";
        public static final String COLUMN_API_DICT_AVAILABLE = "dict_api_available";

        public static Uri buildLanguageDirectionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildLanguageDirectionWithSrcAndDestLanguages(String srcLanguage, String destLanguage) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_SRC_LANGUAGE_CODE, srcLanguage)
                    .appendQueryParameter(COLUMN_DEST_LANGUAGE_CODE, destLanguage)
                    .build();
        }

        public static String getSrcLanguageFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_SRC_LANGUAGE_CODE);
        }

        public static String getDestLanguageFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DEST_LANGUAGE_CODE);
        }
    }
}
