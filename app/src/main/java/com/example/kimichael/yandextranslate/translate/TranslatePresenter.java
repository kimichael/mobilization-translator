package com.example.kimichael.yandextranslate.translate;

import com.example.kimichael.yandextranslate.data.TranslationRepository;

public class TranslatePresenter implements TranslateContract.UserActionsListener {

    private TranslateContract.View translateView;
    private TranslationRepository translationsRepository;


    public TranslatePresenter(TranslateContract.View view, TranslationRepository repository) {
        this.translateView = view;
        this.translationsRepository = repository;
        view.setProgressSpinner(true);
    }

    public void loadTranslations(boolean update) {
        translateView.setProgressSpinner(true);

    }
}
