package com.example.kimichael.yandextranslate.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.kimichael.yandextranslate.data.objects.Language;

public class TranslationProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TranslationDbHelper mOpenHelper;

    static final int WORD = 100;
    static final int DEFINITION = 101;
    static final int LANGUAGE = 200;
    static final int LANGUAGE_DIRECTION = 201;

    // definition.word_id = ?
    private static final String sDefinitionByWordIdSelection =
            TranslationContract.DefinitionEntry.TABLE_NAME + "." +
            TranslationContract.DefinitionEntry.COLUMN_WORD_KEY + " = ?";

    private static final String sTranslationByWordAndLangSelection =
            TranslationContract.WordEntry.TABLE_NAME + "." +
                    TranslationContract.WordEntry.COLUMN_SRC_WORD + " = ? AND " +
                    TranslationContract.WordEntry.COLUMN_SRC_LANG + " = ? AND " +
                    TranslationContract.WordEntry.COLUMN_DEST_LANG + " = ? ";

    private static final String sTranslationDirectionBySrcLangAndDestLangSelection =
            TranslationContract.LanguageDirectionEntry.TABLE_NAME + "." +
                    TranslationContract.LanguageDirectionEntry.COLUMN_SRC_LANGUAGE + " = ? AND " +
                    TranslationContract.LanguageDirectionEntry.COLUMN_DEST_LANGUAGE + " = ?";

    private Cursor getTranslationByWordAndLangSelection(Uri uri, String[] projection, String sortOrder) {
        String wordSetting = TranslationContract.WordEntry.getWordSettingFromUri(uri);
        String srcLangSetting = TranslationContract.WordEntry.getSrcLangSettingFromUri(uri);
        String destLangSetting = TranslationContract.WordEntry.getDestLangSettingFromUri(uri);
        String[] selectionArgs = new String[]{wordSetting, srcLangSetting, destLangSetting};
        String selection = sTranslationByWordAndLangSelection;

        return mOpenHelper.getReadableDatabase().query(
                TranslationContract.WordEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getLangDirectionBySrcAndDestLang(Uri uri, String[] projection, String sortOrder) {
        String srcLangSetting = TranslationContract.LanguageDirectionEntry.getSrcLanguageFromUri(uri);
        String destLangSetting = TranslationContract.LanguageDirectionEntry.getDestLanguageFromUri(uri);
        String[] selectionArgs = new String[] {srcLangSetting, destLangSetting};
        String selection = sTranslationDirectionBySrcLangAndDestLangSelection;

        return mOpenHelper.getReadableDatabase().query(
                TranslationContract.LanguageDirectionEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder,
                "1"
        );
    }

    private Cursor getDefinitionsByWordId(Uri uri, String[] projection, String sortOrder) {
        Integer wordId = TranslationContract.DefinitionEntry.getWordIdFromUri(uri);
        String[] selectionArgs = new String[] {wordId.toString()};
        String selection = sDefinitionByWordIdSelection;

        return mOpenHelper.getReadableDatabase().query(
                TranslationContract.DefinitionEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TranslationDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case WORD:
                // We need to get one row with translation of the word
                return TranslationContract.WordEntry.CONTENT_ITEM_TYPE;
            case LANGUAGE:
                // We need to get all supported languages
                return TranslationContract.LanguageEntry.CONTENT_TYPE;
            case LANGUAGE_DIRECTION:
                // We need to know if we can translate one language to another with dictionary
                return TranslationContract.LanguageDirectionEntry.CONTENT_ITEM_TYPE;
            case DEFINITION:
                // We need to get all definitions by word
                return TranslationContract.DefinitionEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TranslationContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, TranslationContract.PATH_WORD + "/*", WORD);
        uriMatcher.addURI(authority, TranslationContract.PATH_LANGUAGE, LANGUAGE);
        uriMatcher.addURI(authority, TranslationContract.PATH_LANGUAGE_DIRECTION + "/*", LANGUAGE_DIRECTION);
        uriMatcher.addURI(authority, TranslationContract.PATH_DEFINITION + "/*", DEFINITION);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            // "word/*/*/*"
            case WORD: {
                retCursor = getTranslationByWordAndLangSelection(uri, projection, sortOrder);
                break;
            }
            case DEFINITION: {
                retCursor = getDefinitionsByWordId(uri, projection, sortOrder);
                break;
            }
            case LANGUAGE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TranslationContract.LanguageEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case LANGUAGE_DIRECTION: {
                retCursor = getLangDirectionBySrcAndDestLang(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case WORD: {
                long _id = db.insert(TranslationContract.WordEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TranslationContract.WordEntry.buildWordUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case DEFINITION: {
                long _id = db.insert(TranslationContract.DefinitionEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TranslationContract.DefinitionEntry.buildDefinitionUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case LANGUAGE: {
                long _id = db.insert(TranslationContract.LanguageEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TranslationContract.LanguageEntry.buildLanguageUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case LANGUAGE_DIRECTION: {
                long _id = db.insert(TranslationContract.LanguageDirectionEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TranslationContract.LanguageDirectionEntry.buildLanguageDirectionUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) selection = "1";
        switch (match) {
            case WORD:
                rowsDeleted = db.delete(
                        TranslationContract.WordEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DEFINITION:
                rowsDeleted = db.delete(
                        TranslationContract.DefinitionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LANGUAGE:
                rowsDeleted = db.delete(
                        TranslationContract.LanguageEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LANGUAGE_DIRECTION:
                rowsDeleted = db.delete(
                        TranslationContract.LanguageDirectionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case WORD:
                rowsUpdated = db.update(TranslationContract.WordEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case DEFINITION:
                rowsUpdated = db.update(TranslationContract.DefinitionEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case LANGUAGE:
                rowsUpdated = db.update(TranslationContract.LanguageEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case LANGUAGE_DIRECTION:
                rowsUpdated = db.update(TranslationContract.LanguageDirectionEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case LANGUAGE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TranslationContract.LanguageEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
