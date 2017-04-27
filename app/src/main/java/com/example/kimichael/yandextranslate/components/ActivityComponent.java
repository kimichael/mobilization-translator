package com.example.kimichael.yandextranslate.components;

import com.example.kimichael.yandextranslate.module.DataModule;
import com.example.kimichael.yandextranslate.sections.history.HistoryFragment;
import com.example.kimichael.yandextranslate.sections.history.StorageFragment;
import com.example.kimichael.yandextranslate.sections.translate.TranslateFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Kim Michael on 31.03.17.
 */
@Singleton
@Component(modules = {DataModule.class})
public interface ActivityComponent {
    void inject(TranslateFragment fragment);
    void inject(HistoryFragment fragment);
    void inject(StorageFragment fragment);
}
