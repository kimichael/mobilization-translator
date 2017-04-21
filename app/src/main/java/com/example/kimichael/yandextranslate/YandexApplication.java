package com.example.kimichael.yandextranslate;


import android.app.Application;

import com.example.kimichael.yandextranslate.components.ActivityComponent;
import com.example.kimichael.yandextranslate.components.DaggerActivityComponent;
import com.example.kimichael.yandextranslate.modules.DataModule;

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

    @Override
    public ActivityComponent provideComponent() {
        return mActivityComponent;
    }
}
