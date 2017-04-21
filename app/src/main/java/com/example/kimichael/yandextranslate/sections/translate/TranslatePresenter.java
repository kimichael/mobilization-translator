package com.example.kimichael.yandextranslate.sections.translate;

import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;

public class TranslatePresenter implements TranslateContract.UserActionsListener {

    private TranslateContract.View mTranslateView;
    private TranslationRepository mTranslationRepository;

    private Language mSrcLanguage, mDestLanguage;
    private LanguageDirection mLanguageDirection;

    public TranslatePresenter(TranslationRepository repository) {
        this.mTranslationRepository = repository;
    }

    public void onAttachView(TranslateContract.View view,
                             Language srcLanguage, Language destLanguage) {
        this.mTranslateView = view;
        this.mSrcLanguage = srcLanguage;
        this.mDestLanguage = destLanguage;
        mLanguageDirection = new LanguageDirection(srcLanguage, destLanguage);
        this.mTranslationRepository.retrieveLanguages();
        this.mTranslationRepository.retrieveLanguageDirections();
        view.setLanguages(mSrcLanguage.getName(), mDestLanguage.getName());
    }

    public void onDetachView() {
        this.mTranslateView = null;
    }

    @Override
    public void loadTranslation() {
        mTranslateView.setProgressSpinner(true);
        mTranslationRepository.getTranslation(mTranslateView.getRequestedText(), mLanguageDirection,
                new TranslationRepository.LoadTranslationCallback() {
                    @Override
                    public void onTranslationLoaded(Translation translation) {
                        mTranslateView.setProgressSpinner(false);
                        mTranslateView.showTranslation(translation);
                    }

                    @Override
                    public void onLoadError() {
                        mTranslateView.setProgressSpinner(false);
                        mTranslateView.updateEmptyView();
                    }
                });
    }

    @Override
    public void swapLanguages() {
        Language temp = mSrcLanguage;
        mSrcLanguage = mDestLanguage;
        mDestLanguage = temp;
        mTranslateView.setLanguages(mSrcLanguage.getName(), mDestLanguage.getName());
        mLanguageDirection.swapLanguages();
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

    private void updateViewLanguages() {
        mTranslateView.setLanguages(mSrcLanguage.getName(), mDestLanguage.getName());
    }
}
