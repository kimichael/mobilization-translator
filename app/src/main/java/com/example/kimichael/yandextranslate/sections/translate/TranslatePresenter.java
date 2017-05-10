package com.example.kimichael.yandextranslate.sections.translate;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Kim Michael on 31.03.17.
 * Presenter in the MVP
 */
public class TranslatePresenter implements TranslateContract.UserActionsListener {

    private TranslateContract.View mTranslateView;
    private TranslationRepository mTranslationRepository;

    private Language mSrcLanguage, mDestLanguage;
    private LanguageDirection mLanguageDirection;
    private Translation mCachedTranslation;
    private Map<String, Language> mLanguagesMap;
    private List<LanguageDirection> mLanguageDirections;

    public TranslatePresenter(TranslationRepository repository) {
        this.mTranslationRepository = checkNotNull(repository);
        this.mTranslationRepository.retrieveLanguages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Language>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("Subscribed on getting languages map");
                    }

                    @Override
                    public void onSuccess(List<Language> languages) {
                        Timber.d("Languages map is retrieved");
                        mLanguagesMap = new HashMap<>();
                        for (Language language : languages)
                            mLanguagesMap.put(language.getLanguageCode(), language);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        this.mTranslationRepository.retrieveLanguageDirections()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(languageDirections -> {
                    mLanguageDirections = languageDirections;
                });
    }

    public void onAttachView(TranslateContract.View view,
                             Language srcLanguage, Language destLanguage) {
        this.mTranslateView = checkNotNull(view);
        if (mSrcLanguage == null && mDestLanguage == null) {
            this.mSrcLanguage = checkNotNull(srcLanguage);
            this.mDestLanguage = checkNotNull(destLanguage);
        }
        mLanguageDirection = new LanguageDirection(mSrcLanguage, mDestLanguage);
        view.setLanguages(mSrcLanguage.getName(), mDestLanguage.getName());
    }

    @Override
    public void onDetachView() {
        this.mTranslateView = null;
    }

    @Override
    public void clearCache() {
        mCachedTranslation = null;
    }

    @Override
    public void saveTranslationToHistory(Translation translation) {
        Timber.d("Translation saved");
        if (translation != null)
            mTranslationRepository.saveTranslationToHistory(translation, mLanguageDirection);
    }

    @Override
    public void startLoadingTranslation() {
        if (isAttached()) {
                mTranslateView.setProgressSpinner(true);
            if (mCachedTranslation != null && mCachedTranslation.getSrcWord().equals(mTranslateView.getRequestedText())) {
                Timber.d("Found cached translation");
                mTranslateView.setProgressSpinner(false);
                mTranslateView.showTranslation(mCachedTranslation);
                return;
            }
            Timber.d("Start loading");
            mTranslationRepository.getTranslation(mTranslateView.getRequestedText(), mLanguageDirection)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Translation>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Timber.d("Subscribed on getting translation");
                        }

                        @Override
                        public void onSuccess(Translation translation) {
                            mCachedTranslation = translation;
                            if (isAttached()) {
                                mTranslateView.setProgressSpinner(false);
                                mTranslateView.showTranslation(translation);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.d("Error loading");
                            if (isAttached()) {
                                mTranslateView.setProgressSpinner(false);
                                mTranslateView.updateEmptyView();
                            }
                        }
                    });
        }
    }

    @Override
    public void showHistoryRecord(HistoryRecord historyRecord) {
        setLanguages(
                mLanguagesMap.get(historyRecord.getLanguageDirection().getSrcLangCode()),
                mLanguagesMap.get(historyRecord.getLanguageDirection().getDestLangCode()));
        mTranslateView.setInput(historyRecord.getTranslation().getSrcWord());
        startLoadingTranslation();
    }

    @Override
    public void swapLanguages() {
        Language temp = mSrcLanguage;
        mSrcLanguage = mDestLanguage;
        mDestLanguage = temp;
        if (isAttached()) {
            mTranslateView.setLanguages(mSrcLanguage.getName(), mDestLanguage.getName());
            mLanguageDirection.swapLanguages();
            mTranslateView.clearTranslation();
        }
        clearCache();
        startLoadingTranslation();
    }

    @Override
    public void setSrcLanguage(Language language) {
        if (mDestLanguage.equals(language)) {
            swapLanguages();
            mLanguageDirection.swapLanguages();
            return;
        }
        mSrcLanguage = language;
        mLanguageDirection.setSrcLangCode(language.getLanguageCode());
        updateViewLanguages();
    }

    @Override
    public void setDestLanguage(Language language) {
        if (mSrcLanguage.equals(language)) {
            swapLanguages();
            mLanguageDirection.swapLanguages();
            return;
        }
        mDestLanguage = language;
        mLanguageDirection.setDestLangCode(language.getLanguageCode());
        updateViewLanguages();
    }

    @Override
    public void setLanguages(Language srcLanguage, Language destLanguage) {
        mSrcLanguage = srcLanguage;
        mDestLanguage = destLanguage;
        updateViewLanguages();
    }

    @Override
    public void saveState(SharedPreferences prefs, Context context) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_src_language_code),
                mSrcLanguage.getLanguageCode());
        editor.putString(context.getString(R.string.pref_src_language),
                mSrcLanguage.getName());
        editor.putString(context.getString(R.string.pref_dest_language_code),
                mDestLanguage.getLanguageCode());
        editor.putString(context.getString(R.string.pref_dest_language),
                mDestLanguage.getName());
        editor.putString(context.getString(R.string.key_input_text),
                mTranslateView.getRequestedText());
        editor.apply();
    }

    private void updateViewLanguages() {
        if (isAttached())
            mTranslateView.setLanguages(mSrcLanguage.getName(), mDestLanguage.getName());
    }

    @Override
    public void clearHistory() {
        Timber.d("Clearing history");
        mTranslationRepository.clearHistory();
    }

    @Override
    public Translation getCachedTranslation() {
        return mCachedTranslation;
    }

    @Override
    public void bookmarkTranslation(HistoryRecord historyRecord) {
        if (historyRecord.getLanguageDirection() == null)
            historyRecord.setLanguageDirection(mLanguageDirection);
        mTranslationRepository.bookmarkTranslation(historyRecord);
    }

    private boolean isAttached() {
        return mTranslateView != null;
    }
}
