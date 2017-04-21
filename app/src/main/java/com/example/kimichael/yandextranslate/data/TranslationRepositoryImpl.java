package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.network.NetworkTranslationSource;

import java.util.List;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TranslationRepositoryImpl implements TranslationRepository {

    //TODO Finish this class
    private LocalTranslationSource mLocalTranslationSource;
    private NetworkTranslationSource mNetworkTranslationSource;

    public TranslationRepositoryImpl(LocalTranslationSource localLocalTranslationSource,
                                     NetworkTranslationSource networkTranslationSource) {
        mLocalTranslationSource = localLocalTranslationSource;
        mNetworkTranslationSource = networkTranslationSource;
    }


    @Override
    public void getTranslation(final String requestedText,
                                          final LanguageDirection languageDirection,
                                          @NonNull final LoadTranslationCallback callback) {
        Single<Translation> translationSingle = mLocalTranslationSource.isDictSupported(languageDirection).flatMap(new Function<Boolean, SingleSource<Translation>>() {
            @Override
            public SingleSource<Translation> apply(Boolean aBoolean) throws Exception {
                @NetworkTranslationSource.TranslationApi int api = aBoolean ?
                        NetworkTranslationSource.YANDEX_DICTIONARY_API :
                        NetworkTranslationSource.YANDEX_TRANSLATE_API;
                return mLocalTranslationSource.getTranslation(requestedText, languageDirection, api)
                        .onErrorResumeNext(throwable -> {
                            Timber.d("Call to network");
                            return mNetworkTranslationSource.getTranslation(requestedText, languageDirection, api);
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
                        translation.setSrcWord(requestedText.toLowerCase());
                        mLocalTranslationSource.saveTranslation(translation, languageDirection);
                        callback.onTranslationLoaded(translation);
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
}
