package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

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
import io.reactivex.functions.Predicate;
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
    public void getTranslation(final String requestedText,
                                          final LanguageDirection languageDirection,
                                          @NonNull final LoadTranslationCallback callback) {
        pendingWord = requestedText;
        Single<Translation> translationSingle = mLocalTranslationSource.isDictSupported(languageDirection).flatMap(new Function<Boolean, SingleSource<Translation>>() {
            @Override
            public SingleSource<Translation> apply(Boolean aBoolean) throws Exception {
                @NetworkTranslationSource.TranslationApi int api = aBoolean ?
                        NetworkTranslationSource.YANDEX_DICTIONARY_API :
                        NetworkTranslationSource.YANDEX_TRANSLATE_API;
                return mLocalTranslationSource.getTranslation(requestedText, languageDirection, api)
                        .timeout(5L, TimeUnit.SECONDS)
                        // If we cannot find the translation on our device, we go to network
                        .onErrorResumeNext((Throwable throwable) -> {
                            Timber.d("Call to network");
                            return mNetworkTranslationSource.getTranslation(requestedText, languageDirection, api);
                            // If we cannot find the translation on dictionary, we look in Yandex.Translate
                        }).onErrorResumeNext(throwable -> {
                            return mNetworkTranslationSource.getTranslation(requestedText, languageDirection, NetworkTranslationSource.YANDEX_TRANSLATE_API);
                        });
            }
        });
        translationSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Translation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("Subscribed on getting translation");
                    }

                    @Override
                    public void onSuccess(Translation translation) {
                        // Assert that downloaded word is what we want
                        if (requestedText.equals(pendingWord)) {
                            translation.setSrcWord(requestedText.toLowerCase());
                            callback.onTranslationLoaded(translation);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onLoadError();
                    }
                });
    }

    @Override
    public void retrieveLanguages() {
        mNetworkTranslationSource.retrieveLanguages()
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
    }

    @Override
    public void retrieveLanguageDirections() {
        mNetworkTranslationSource.retrieveLanguageDirections()
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
