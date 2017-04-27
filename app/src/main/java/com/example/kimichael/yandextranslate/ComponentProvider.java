package com.example.kimichael.yandextranslate;

import android.support.annotation.VisibleForTesting;

import com.example.kimichael.yandextranslate.components.ActivityComponent;

/**
 * Created by Kim Michael on 31.03.17.
 */
public interface ComponentProvider {
    ActivityComponent provideComponent();
    @VisibleForTesting
    void setComponent(ActivityComponent component);
}
