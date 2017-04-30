package com.example.kimichael.yandextranslate.data;

import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.data.network.NetworkTranslationSource;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Kim Michael on 31.03.17.
 * Local translation source keeps all history of translations and bookmarks
 */
public interface LocalTranslationSource {
    /**
     * Try getting translation from internal memory
     * @param languageDirection direction, in which we need our translation
     * @param requestedText text, which we need to translate
     * @param translationApi API or service, which we use to translate
     * @return a single observable that emits requested translation
     */
    Single<Translation> getTranslation(String requestedText,
                        LanguageDirection languageDirection,
                        @NetworkTranslationSource.TranslationApi int translationApi);
    /**
     * Save translation to internal memory
     * @param translation translation to be saved
     * @param languageDirection source and destination languages to be saved
     */
    void saveTranslation(Translation translation, LanguageDirection languageDirection);
    /**
     * Save languages directions
     * We use them then for determining whether
     * language direction is supported by Yandex.Dictionary or not
     * @param supportedLanguageDirections directions to be saved
     */
    void saveLanguageDirections(List<LanguageDirection> supportedLanguageDirections);
    /**
     * Save languages with their names to internal memory
     * @param languages languages to be saved
     */
    void saveLanguages(List<Language> languages);
    /**
     * Mark particular translation in internal memory as bookmarked or not
     * @param historyRecord history record to be saved
     */
    void bookmarkTranslation(HistoryRecord historyRecord);
    /**
     * Show whether given language direction is supported by Yandex.Dictionary or not
     * @return a single observable, that emits the answer
     */
    Single<Boolean> isDictSupported(LanguageDirection direction);
    /**
     * Clear the whole history from internal memory
     */
    void clearHistory();
}
