package com.example.kimichael.yandextranslate.translate;

/**
 * Created by mikim on 22.03.17.
 */

public interface TranslateContract {

    interface View {

        void clearInput(boolean showKeyboard);

        void setProgressSpinner(boolean active);

    }

    interface UserActionsListener {

        void loadTranslations(boolean update);


    }
}
