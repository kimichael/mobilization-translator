package com.example.kimichael.yandextranslate;


import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.example.kimichael.yandextranslate.components.ActivityComponent;
import com.example.kimichael.yandextranslate.components.DaggerActivityComponent;
import com.example.kimichael.yandextranslate.module.DataModule;

import timber.log.Timber;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class YandexApplication extends Application implements ComponentProvider {

    private ActivityComponent mActivityComponent;

    @Override
    public void onCreate() {
        mActivityComponent = DaggerActivityComponent.builder()
                .dataModule(new DataModule(this))
                .build();
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
        super.onCreate();
    }

    @VisibleForTesting
    public void setComponent(ActivityComponent component) {
        mActivityComponent = component;
    }

    @Override
    public ActivityComponent provideComponent() {
        return mActivityComponent;
    }
}
