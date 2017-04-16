package com.example.kimichael.yandextranslate.sections.translate;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.kimichael.yandextranslate.data.TranslationContract;
import com.example.kimichael.yandextranslate.data.TranslationQueryHandler;
import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.network.NetworkTranslationSource;

import java.util.List;

public class TranslatePresenter implements TranslateContract.UserActionsListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LANGUAGES_LOADER = 1;

    private TranslateContract.View translateView;
    private TranslationRepository translationRepository;
    private LoaderManager mLoaderManager;
    private TranslationQueryHandler mQueryHandler;

    private Language srcLanguage, destLanguage;

    public TranslatePresenter(TranslateContract.View view, TranslationRepository repository,
                              Language srcLanguage, Language destLanguage, LoaderManager loaderManager,
                              TranslationQueryHandler queryHandler) {
        this.translationRepository = repository;
        this.srcLanguage = srcLanguage;
        this.destLanguage = destLanguage;
        mLoaderManager = loaderManager;
        mQueryHandler = queryHandler;
        mQueryHandler.setQueryListener(new TranslationQueryHandler.AsyncQueryListener() {
            @Override
            public void onQueryComplete(int token, Object cookie, Cursor cursor) {}});
        this.translationRepository.retrieveLanguages(queryHandler);
        this.translationRepository.retrieveLanguageDirections(queryHandler);
        onAttachView(view);
    }

    public void onAttachView(TranslateContract.View view) {
        this.translateView = view;
        view.setLanguages(srcLanguage.getName(), destLanguage.getName());
    }

    public void onDetachView() {
        this.translateView = null;
    }

    @Override
    public void loadTranslation() {

    }

    @Override
    public void swapLanguages() {
        Language temp = srcLanguage;
        srcLanguage = destLanguage;
        destLanguage = temp;
        translateView.setLanguages(srcLanguage.getName(), destLanguage.getName());
    }

    @Override
    public void setSrcLanguage(Language language) {
        if (destLanguage.equals(language)) {
            swapLanguages();
            return;
        }
        srcLanguage = language;
        updateViewLanguages();
    }

    @Override
    public void setDestLanguage(Language language) {
        if (srcLanguage.equals(language)) {
            swapLanguages();
            return;
        }
        destLanguage = language;
        updateViewLanguages();
    }

    private void updateViewLanguages() {
        translateView.setLanguages(srcLanguage.getName(), destLanguage.getName());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
