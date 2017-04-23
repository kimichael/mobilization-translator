package com.example.kimichael.yandextranslate.data.provider;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.kimichael.yandextranslate.data.objects.Definition;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.parse.Composer;

import java.lang.ref.WeakReference;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class TranslationQueryHandler extends AsyncQueryHandler {
    private WeakReference<AsyncQueryListener> mTranslationListener;
    private WeakReference<AsyncQueryListener> mIsDictSupportedListener;
    private WeakReference<AsyncQueryListener> mDefinitionsListener;
    Composer mComposer;

    private static final int TRANSLATION_TOKEN = 0;
    private static final int IS_DICT_SUPPORTED_TOKEN = 1;
    private static final int DEFINITIONS_TOKEN = 2;
    private static final int CLEAR_HISTORY_TOKEN = 3;

    private static final String[] languageDirectionProjection = new String[] {
            TranslationContract.LanguageDirectionEntry.COLUMN_SRC_LANGUAGE_CODE,
            TranslationContract.LanguageDirectionEntry.COLUMN_DEST_LANGUAGE_CODE,
            TranslationContract.LanguageDirectionEntry.COLUMN_API_DICT_AVAILABLE
    };

    private static final String[] translationProjection = new String[] {
            TranslationContract.WordEntry.COLUMN_SRC_WORD,
            TranslationContract.WordEntry.COLUMN_DEST_WORD,
            TranslationContract.WordEntry.COLUMN_BOOKMARK
    };

    private static final String[] definitionProjection = new String[] {
            TranslationContract.DefinitionEntry.COLUMN_TEXT,
            TranslationContract.DefinitionEntry.COLUMN_SRC_LANG,
            TranslationContract.DefinitionEntry.COLUMN_DEST_LANG,
            TranslationContract.DefinitionEntry.COLUMN_GENUS,
            TranslationContract.DefinitionEntry.COLUMN_ORDER,
            TranslationContract.DefinitionEntry.COLUMN_TRANSCRIPTION,
            TranslationContract.DefinitionEntry.COLUMN_JSON_CHILDREN,
            TranslationContract.DefinitionEntry.COLUMN_PART_OF_SPEECH,
            TranslationContract.DefinitionEntry.COLUMN_WORD_KEY
    };

    public interface AsyncQueryListener {
        void onQueryComplete(int token, Object cookie, Cursor cursor);
    }

    public TranslationQueryHandler(Context context) {
        super(context.getContentResolver());
        mComposer = new Composer();
    }

    public void setTranslationQueryListener(AsyncQueryListener listener) {
        mTranslationListener = new WeakReference<>(listener);
    }

    public void setIsDictSupportedListener(AsyncQueryListener listener) {
        mIsDictSupportedListener = new WeakReference<>(listener);
    }

    public void setDefinitionsListener(AsyncQueryListener listener) {
        mDefinitionsListener = new WeakReference<>(listener);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        final AsyncQueryListener listener;
        switch (token) {
            case TRANSLATION_TOKEN:
                listener = mTranslationListener.get();
                break;
            case IS_DICT_SUPPORTED_TOKEN:
                listener = mIsDictSupportedListener.get();
                break;
            case DEFINITIONS_TOKEN:
                listener = mDefinitionsListener.get();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported token: " + token);
        }
        if (listener != null) {
            listener.onQueryComplete(token, cookie, cursor);
        } else if (cursor != null) {
            cursor.close();
        }
    }

    public void startIsDictSupportedQuery(LanguageDirection direction) {
        startQuery(IS_DICT_SUPPORTED_TOKEN, null,
                TranslationContract.LanguageDirectionEntry.buildLanguageDirectionWithSrcAndDestLanguages(
                        direction.getSrcLangCode(),
                        direction.getDestLangCode()),
                languageDirectionProjection, null, null, null);
    }

    public void startTranslationInsert(Translation translation, LanguageDirection direction) {
        startInsert(0, null,
                TranslationContract.WordEntry.buildWordWithSrcWordAndLangDirectionSetting(
                        translation.getSrcWord(),
                        direction.getSrcLangCode(),
                        direction.getDestLangCode()),
                translation.toContentValues(direction));
    }

    public void startTranslationQuery(String requestedWord, LanguageDirection languageDirection) {
        startQuery(TRANSLATION_TOKEN, null, TranslationContract.WordEntry.
                buildWordWithSrcWordAndLangDirectionSetting(
                        requestedWord,
                        languageDirection.getSrcLangCode(),
                        languageDirection.getDestLangCode()),
                translationProjection, null, null, null);
    }

    public void startDefinitionInsert(Definition definition, String srcWord, LanguageDirection direction) {
        startInsert(0, null,
                TranslationContract.DefinitionEntry.buildDefinitionWithWordAndLangDirection(srcWord,
                        direction.getSrcLangCode(), direction.getDestLangCode()),
                definition.toContentValues(mComposer));
    }

    public void startDefinitionsQuery(String requestedText, LanguageDirection languageDirection) {
        startQuery(DEFINITIONS_TOKEN, null,
                TranslationContract.DefinitionEntry.buildDefinitionWithWordAndLangDirection(
                        requestedText, languageDirection.getSrcLangCode(), languageDirection.getDestLangCode()),
                definitionProjection, null, null, TranslationContract.DefinitionEntry.COLUMN_ORDER + " ASC");
    }

    public void startBookmarkUpdate(HistoryRecord historyRecord) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TranslationContract.WordEntry.COLUMN_BOOKMARK, historyRecord.getTranslation().isMarked() ? 1 : 0);
        startUpdate(TRANSLATION_TOKEN, null, TranslationContract.WordEntry.buildWordWithSrcWordAndLangDirectionSetting(
                historyRecord.getTranslation().getSrcWord(), historyRecord.getLanguageDirection().getSrcLangCode(),
                historyRecord.getLanguageDirection().getDestLangCode()), contentValues, null, null);
    }

    public void clearHistory() {
        startDelete(CLEAR_HISTORY_TOKEN, null, TranslationContract.WordEntry.buildClearAllUri(), null, null);
    }

}
