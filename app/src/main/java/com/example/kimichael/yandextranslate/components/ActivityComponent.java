package com.example.kimichael.yandextranslate.components;

import com.example.kimichael.yandextranslate.modules.ContextModule;
import com.example.kimichael.yandextranslate.sections.translate.TranslateFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class})
public interface ActivityComponent {
    void inject(TranslateFragment fragment);
}
