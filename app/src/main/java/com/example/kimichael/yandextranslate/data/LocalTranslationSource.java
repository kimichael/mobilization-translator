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
 */
public interface LocalTranslationSource {

    Single<Translation> getTranslation(String requestedText,
                        LanguageDirection languageDirection,
                        @NetworkTranslationSource.TranslationApi int translationApi);

    void saveTranslation(Translation translation, LanguageDirection languageDirection);
    void saveLanguageDirections(List<LanguageDirection> languageDirections);
    void saveLanguages(List<Language> languages);
    void bookmarkTranslation(HistoryRecord historyRecord);

    Single<Boolean> isDictSupported(LanguageDirection direction);

    void clearHistory();
}
