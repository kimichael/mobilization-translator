package com.example.kimichael.yandextranslate.translate;

import com.example.kimichael.yandextranslate.data.TranslationRepository;

public class TranslatePresenter implements TranslateContract.UserActionsListener {

    private TranslateContract.View translateView;
    private TranslationRepository translationRepository;

    public TranslatePresenter(TranslateContract.View view, TranslationRepository repository) {
        this.translationRepository = repository;
        onAttachView(view);
    }
    public void onAttachView(TranslateContract.View view) {
        this.translateView = view;
    }

    public void onDetachView() {
        this.translateView = null;
    }

    @Override
    public void loadTranslation() {

    }
}
