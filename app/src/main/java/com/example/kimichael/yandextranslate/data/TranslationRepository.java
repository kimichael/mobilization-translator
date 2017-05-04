package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Kim Michael on 31.03.17.
 * Repository, which works with network and internal memory to deliver translations to us.
 * Represents model
 */
public interface TranslationRepository {
    /**
     * Get translation
     * @param requestedText word, which we need translation to
     * @param languageDirection direction, in which we need our translation
     */
    Single<Translation> getTranslation(String requestedText,
                                   LanguageDirection languageDirection);

    /**
     * Get all supported languages
     */
    Single<List<Language>> retrieveLanguages();
    /**
     * Get language directions, supported by Yandex.Dictionary
     */
    Single<List<LanguageDirection>> retrieveLanguageDirections();

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
