package com.example.kimichael.yandextranslate.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.kimichael.yandextranslate.data.objects.Translation;

public class TranslationProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TranslationDbHelper mOpenHelper;

    static final int WORD = 100;
    static final int DEFINITION = 101;
    static final int TRANSLATION = 102;
    static final int SYNONYM = 103;
    static final int MEANING = 104;
    static final int EXAMPLE = 105;
    static final int LANGUAGE = 200;
    static final int LANGUAGE_DIRECTION = 201;

    // definition.word_id = ?
    private static final String sDefinitionByWordIdSelection =
            TranslationContract.DefinitionEntry.TABLE_NAME + "." +
            TranslationContract.DefinitionEntry.COLUMN_WORD_KEY + " = ?";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TranslationContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, TranslationContract.PATH_WORD + "/*", WORD);
        uriMatcher.addURI(authority, TranslationContract.PATH_DEFINITION + "/#", DEFINITION);
        uriMatcher.addURI(authority, TranslationContract.PATH_TRANSLATION + "/#", TRANSLATION);
        uriMatcher.addURI(authority, TranslationContract.PATH_SYNONYM + "/#", SYNONYM);
        uriMatcher.addURI(authority, TranslationContract.PATH_MEANING + "/#", MEANING);
        uriMatcher.addURI(authority, TranslationContract.PATH_EXAMPLE + "/#", EXAMPLE);
        uriMatcher.addURI(authority, TranslationContract.PATH_LANGUAGE, LANGUAGE);
        uriMatcher.addURI(authority, TranslationContract.PATH_LANGUAGE_DIRECTION + "/*", LANGUAGE_DIRECTION);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
