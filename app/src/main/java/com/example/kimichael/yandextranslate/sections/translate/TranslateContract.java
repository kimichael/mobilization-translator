package com.example.kimichael.yandextranslate.sections.translate;

import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Language;

/**
 * Created by mikim on 22.03.17.
 */

public interface TranslateContract {

    interface View {

        void setLanguages(String srcLanguageName, String destLanguageName);

        void clearInput(boolean showKeyboard);

        void setProgressSpinner(boolean active);

        void showTranslation(DictionaryTranslation dictionaryTranslation);

        String getRequestedWord();

    }

    interface UserActionsListener {

        void loadTranslation();

        void swapLanguages();

        void setSrcLanguage(Language language);
        void setDestLanguage(Language language);
    }
}
