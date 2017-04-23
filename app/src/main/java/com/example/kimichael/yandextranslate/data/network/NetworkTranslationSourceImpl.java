package com.example.kimichael.yandextranslate.data.network;

import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import java.util.List;
import java.util.Locale;

import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class NetworkTranslationSourceImpl implements NetworkTranslationSource {

    private final YandexTranslateClient yandexTranslateClient;
    private final YandexDictionaryClient yandexDictionaryClient;

    public NetworkTranslationSourceImpl(YandexTranslateClient yandexTranslateClient,
                                        YandexDictionaryClient yandexDictionaryClient) {
        this.yandexTranslateClient = checkNotNull(yandexTranslateClient);
        this.yandexDictionaryClient = checkNotNull(yandexDictionaryClient);
    }

    @Override
    public Single<Translation> getTranslation(final String requestedText,
                                 final LanguageDirection direction,
                                 @TranslationApi int translationApi) {
        if (translationApi == YANDEX_DICTIONARY_API && requestedText.split(" ").length <= 1) {
            return yandexDictionaryClient.getTranslation(requestedText,
                    direction.getLanguageDirectionForApi());
        } else {
            return yandexTranslateClient.getTranslation(requestedText,
                    direction.getLanguageDirectionForApi());
        }
    }

    @Override
    public Single<List<Language>> retrieveLanguages() {
        return yandexTranslateClient.getLanguages(Locale.getDefault().getLanguage());
    }

    @Override
    public Single<List<LanguageDirection>> retrieveLanguageDirections() {
        return yandexDictionaryClient.getLanguageDirections();
    }
}
