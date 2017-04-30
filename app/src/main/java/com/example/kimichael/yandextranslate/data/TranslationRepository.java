package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;

/**
 * Created by Kim Michael on 31.03.17.
 * Repository, which works with network and internal memory to deliver translations to us.
 */
public interface TranslationRepository {

    interface LoadTranslationCallback {
        void onTranslationLoaded(Translation translation);
        void onLoadError();
    }

    /**
     * Get translation
     * @param requestedText word, which we need translation to
     * @param languageDirection direction, in which we need our translation
     * @param callback callback which will then get the translation
     */
    void getTranslation(String requestedText,
                                   LanguageDirection languageDirection,
                                   @NonNull LoadTranslationCallback callback);

    /**
     * Get all supported languages
     */
    void retrieveLanguages();
    /**
     * Get language directions, supported by Yandex.Dictionary
     */
    void retrieveLanguageDirections();

    /**
     * Save translation to history
     * @param translation translation to be saved
     * @param languageDirection direction, in which translation must be saved
     */
    void saveTranslationToHistory(Translation translation, LanguageDirection languageDirection);
    /**
     * Update bookmark of history record as bookmarked(or not)
     * @param historyRecord record to be updated
     */
    void bookmarkTranslation(HistoryRecord historyRecord);

    /**
     * Clear the whole history from internal memory
     */
    void clearHistory();
}
