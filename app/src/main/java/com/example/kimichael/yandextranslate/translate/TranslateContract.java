package com.example.kimichael.yandextranslate.translate;

import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;

/**
 * Created by mikim on 22.03.17.
 */

public interface TranslateContract {

    interface View {

        void clearInput(boolean showKeyboard);

        void setProgressSpinner(boolean active);

        void showTranslation(DictionaryTranslation dictionaryTranslation);

        String getRequestedWord();

    }

    interface UserActionsListener {

        void loadTranslation();

    }
}
