package com.example.kimichael.yandextranslate.sections.translate;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.Translation;

/**
 * Created by Kim Michael on 31.03.17.
 * I used MVP in this app. It is easy to maintain for translator app
 */
public interface TranslateContract {

    interface View {

        /**
        * Set languages to display to user
        */
        void setLanguages(String srcLanguageName, String destLanguageName);

        /**
         * Clear edit box
         */
        void clearInput(boolean showKeyboard);

        /**
         * Fill edit box with text
         * @param input text which must be set
         */
        void setInput(String input);

        /**
         * Clear the zone where translation is shown
         */
        void clearTranslation();

        /**
        * Set spinner, which indicates loading
         * @param active true to show, false to hide
        */
        void setProgressSpinner(boolean active);

        /**
         * Show translation to user
         * @param translation translation to be shown
         */
        void showTranslation(Translation translation);

        /**
         * Update the view, which shows some errors
         */
        void updateEmptyView();

        /**
         * Get the text, typed into edit box
         * @return the typed text
         */
        String getRequestedText();

    }

    interface UserActionsListener {

        /**
         * Start loading translation for requested word
        */
        void startLoadingTranslation();
        /**
         * Swap source and destination languages
         */
        void swapLanguages();
        /**
         * Set source language
         * @param language language to be set
         * */
        void setSrcLanguage(Language language);
        /**
         * Set destination language
         * @param language language to be set
         * */
        void setDestLanguage(Language language);
        /**
         * Tie view to the presenter
         * @param view view to be tied
         * @param srcLanguage initial source language to be set to the presenter
         * @param destLanguage initial destination language to be set to the presenter
         */
        void onAttachView(TranslateContract.View view,
                          Language srcLanguage, Language destLanguage);
        /**
         * Detach the presenter from the view
         */
        void onDetachView();
        /**
         * Save state of the presenter
         * @param prefs preferences for saving
         */
        void saveState(SharedPreferences prefs, Context context);
        /**
         * Remove cached translation from the presenter
         */
        void clearCache();

        /**
         * Save translation to history
         * @param translation translation to be saved
         */
        void saveTranslationToHistory(Translation translation);

        /**
         * Bookmark given translation with languages
         * @param historyRecord record to be saved
         */
        void bookmarkTranslation(HistoryRecord historyRecord);

        /**
         * Clear the whole history from internal memory
         */
        void clearHistory();
        /**
         * Get cached translation from presenter
         * @return cached translation
         */
        Translation getCachedTranslation();
    }
}
