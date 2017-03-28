package com.example.kimichael.yandextranslate.translate;

import com.example.kimichael.yandextranslate.data.TranslationsRepository;

public class TranslatePresenter implements TranslateContract.UserActionsListener {

    private TranslateContract.View translateView;
    private TranslationsRepository translationsRepository;


    public TranslatePresenter(TranslateContract.View view, TranslationsRepository repository) {
        this.translateView = view;
        this.translationsRepository = repository;
        view.setProgressSpinner(true);
    }

    public void loadTranslations(boolean update) {
        translateView.setProgressSpinner(true);

    }
}
