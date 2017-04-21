package com.example.kimichael.yandextranslate.components;

import android.app.Application;

import com.example.kimichael.yandextranslate.modules.DataModule;
import com.example.kimichael.yandextranslate.sections.translate.TranslateFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class})
public interface ActivityComponent {
    void inject(TranslateFragment fragment);
    void inject(Application application);
}
