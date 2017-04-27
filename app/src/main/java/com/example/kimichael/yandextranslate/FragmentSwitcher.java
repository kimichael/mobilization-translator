package com.example.kimichael.yandextranslate;

import com.example.kimichael.yandextranslate.activity.MainActivity;

/**
 * Created by Kim Michael on 24.04.17
 * Helper fragment that allows switching between fragments
 */
public interface FragmentSwitcher {
    void switchFragment(@MainActivity.ChosenFragmentStatus int fragment);
}
