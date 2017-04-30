package com.example.kimichael.yandextranslate.sections.translate;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class TranslatePresenter implements TranslateContract.UserActionsListener {

    private TranslateContract.View mTranslateView;
    private TranslationRepository mTranslationRepository;

    private Language mSrcLanguage, mDestLanguage;
    private LanguageDirection mLanguageDirection;
    private Translation mCachedTranslation;

    public TranslatePresenter(TranslationRepository repository) {
        this.mTranslationRepository = checkNotNull(repository);
        this.mTranslationRepository.retrieveLanguages();
        this.mTranslationRepository.retrieveLanguageDirections();
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
            mTranslationRepository.getTranslation(mTranslateView.getRequestedText(), mLanguageDirection,
                    new TranslationRepository.LoadTranslationCallback() {
                        @Override
                        public void onTranslationLoaded(Translation translation) {
                            mCachedTranslation = translation;
                            if (isAttached()) {
                                mTranslateView.setProgressSpinner(false);
                                mTranslateView.showTranslation(translation);
                            }
                        }

                        @Override
                        public void onLoadError() {
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
