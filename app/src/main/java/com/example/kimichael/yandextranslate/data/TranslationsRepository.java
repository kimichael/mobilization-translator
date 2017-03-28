package com.example.kimichael.yandextranslate.data;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by mikim on 24.03.17.
 */

public interface TranslationsRepository {

    interface LoadTranslationsCallback {
        void onTranslationLoaded(List<Translation> translations);
    }

    interface LoadTranslationCallback {
        void onTranslationLoaded(Translation translation);
    }

    void getTranslation(@NonNull LoadTranslationCallback callback);

    void getTranslations(@NonNull LoadTranslationsCallback callback);

    void saveTranslation(@NonNull Translation translation);
}
