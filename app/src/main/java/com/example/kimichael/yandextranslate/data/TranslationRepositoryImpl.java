package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.network.NetworkTranslationSource;

import java.util.List;

public class TranslationRepositoryImpl implements TranslationRepository {

    //TODO Finish this class

    TranslationSource mLocalTranslationSource;
    NetworkTranslationSource mNetworkTranslationSourceImpl;

    public TranslationRepositoryImpl(TranslationSource localTranslationSource,
                                     NetworkTranslationSource networkTranslationSource) {
        mLocalTranslationSource = localTranslationSource;
        mNetworkTranslationSourceImpl = networkTranslationSource;
    }


    @Override
    public void getTranslationFromNetwork(String requestedWord, @NonNull LoadTranslationCallback callback) {

    }

    @Override
    public void getTranslationFromDb(String requestedWord, @NonNull LoadTranslationCallback callback) {

    }

    @Override
    public void saveTranslation(@NonNull DictionaryTranslation dictionaryTranslation) {

    }

    @Override
    public void saveTranslation(@NonNull Translation translation) {

    }

    @Override
    public void retrieveLanguages(final TranslationQueryHandler queryHandler) {
        mNetworkTranslationSourceImpl.retrieveLanguages(new NetworkTranslationSource.LoadLanguagesCallback() {
            @Override
            public void onLanguagesLoaded(List<Language> languages) {
                int i = 0;
                for (Language language : languages) {
                    queryHandler.startInsert(i++, null, TranslationContract.LanguageEntry.CONTENT_URI,
                            language.toContentValues());
                }
            }

            @Override
            public void onLanguagesLoadError() {
            }
        });
    }

    @Override
    public void retrieveLanguageDirections(final TranslationQueryHandler queryHandler) {
        mNetworkTranslationSourceImpl.retrieveLanguageDirections(new NetworkTranslationSource.LoadLanguageDirectionsCallback() {
            @Override
            public void onDirectionsLoaded(List<LanguageDirection> directions) {
                int i = 0;
                for (LanguageDirection direction : directions) {
                    queryHandler.startInsert(i++, null, TranslationContract.LanguageDirectionEntry.CONTENT_URI,
                            direction.toContentValues());
                }
            }

            @Override
            public void onDirectionsLoadError() {
            }
        });
    }
}
