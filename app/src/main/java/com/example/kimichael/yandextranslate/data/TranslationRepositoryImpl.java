package com.example.kimichael.yandextranslate.data;

import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.data.network.NetworkTranslationSource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Kim Michael on 31.03.17
 */
public class TranslationRepositoryImpl implements TranslationRepository {

    private LocalTranslationSource mLocalTranslationSource;
    private NetworkTranslationSource mNetworkTranslationSource;
    // It's a word that is waited by presenter
    private String pendingWord;

    public TranslationRepositoryImpl(LocalTranslationSource localLocalTranslationSource,
                                     NetworkTranslationSource networkTranslationSource) {
        mLocalTranslationSource = checkNotNull(localLocalTranslationSource);
        mNetworkTranslationSource = checkNotNull(networkTranslationSource);
    }


    @Override
    public Single<Translation> getTranslation(final String requestedText,
                                          final LanguageDirection languageDirection) {
        pendingWord = requestedText;
        return mLocalTranslationSource
                .isDictSupported(languageDirection)
                .flatMap(new Function<Boolean, SingleSource<Translation>>() {
            @Override
            public SingleSource<Translation> apply(Boolean isDictAvailable) throws Exception {
                @NetworkTranslationSource.TranslationApi int api = isDictAvailable ?
                        NetworkTranslationSource.YANDEX_DICTIONARY_API :
                        NetworkTranslationSource.YANDEX_TRANSLATE_API;
                // First try looking in database
                return mLocalTranslationSource.getTranslation(requestedText, languageDirection, api)
                        .timeout(5L, TimeUnit.SECONDS)
                        // If we cannot find the translation on our device, we go to network
                        .onErrorResumeNext((Throwable throwable) -> {
                            Timber.d("Call to network");
                            return mNetworkTranslationSource
                                    .getTranslation(requestedText, languageDirection, api);
                            // If we cannot find the translation on dictionary, we look in Yandex.Translate
                        }).onErrorResumeNext(throwable -> {
                            return mNetworkTranslationSource
                                    .getTranslation(requestedText, languageDirection,
                                            NetworkTranslationSource.YANDEX_TRANSLATE_API);
                        }).doOnSuccess(translation -> {
                            // Assert that downloaded word is what we want
                            if (requestedText.equals(pendingWord)) {
                                translation.setSrcWord(requestedText.toLowerCase());
                            }
                        });
            }
        });
    }

    @Override
    public Single<List<Language>> retrieveLanguages() {
        Single<List<Language>> languagesObs = mNetworkTranslationSource.retrieveLanguages()
                .onErrorResumeNext(mLocalTranslationSource.retrieveLanguages());

        languagesObs
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<List<Language>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Timber.d("Subscribed on getting language list");
                }
                @Override
                public void onSuccess(List<Language> languages) {
                    mLocalTranslationSource.saveLanguages(languages);
                }
                @Override
                public void onError(Throwable e) {
                    Timber.d("Cannot retrieve languages");
                }
            });

        return languagesObs;
    }

    @Override
    public Single<List<LanguageDirection>> retrieveLanguageDirections() {
        Single<List<LanguageDirection>> languageDirectionsObs =
                mLocalTranslationSource.retrieveLanguageDirections()
                .onErrorResumeNext(mNetworkTranslationSource.retrieveLanguageDirections());

        languageDirectionsObs
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<LanguageDirection>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("Subscribed on getting language directions");
                    }

                    @Override
                    public void onSuccess(List<LanguageDirection> languageDirections) {
                        mLocalTranslationSource.saveLanguageDirections(languageDirections);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("Cannot retrieve language directions");
                    }
                });

        return languageDirectionsObs;
    }

    @Override
    public void bookmarkTranslation(HistoryRecord historyRecord) {
        mLocalTranslationSource.bookmarkTranslation(historyRecord);
    }

    @Override
    public void saveTranslationToHistory(Translation translation, LanguageDirection languageDirection) {
        mLocalTranslationSource.saveTranslation(translation, languageDirection);
    }

    @Override
    public void clearHistory() {
        mLocalTranslationSource.clearHistory();
    }
}
