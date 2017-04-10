package com.example.kimichael.yandextranslate.modules;

import android.content.Context;

import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.TranslationRepositoryImpl;
import com.example.kimichael.yandextranslate.data.TranslationSource;
import com.example.kimichael.yandextranslate.data.local.LocalTranslationSource;
import com.example.kimichael.yandextranslate.data.network.NetworkDataSource;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    Context mContext;

    public ContextModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    TranslationRepository provideTranslationRepository(
            @Named("LocalTranslationSource") TranslationSource localTranslationSource,
            @Named("NetworkTranslationSource") TranslationSource networkTranslationSource) {
        return new TranslationRepositoryImpl(localTranslationSource, networkTranslationSource);
    }

    @Provides
    @Singleton
    @Named("LocalTranslationSource")
    TranslationSource provideLocalTranslationSource() {
        return new LocalTranslationSource();
    }

    @Provides
    @Singleton
    @Named("NetworkTranslationSource")
    TranslationSource provideNetworkTranslationSource() {
        return new NetworkDataSource();
    }
}
