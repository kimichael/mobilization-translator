package com.example.kimichael.yandextranslate.sections.translate;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.Translation;

/**
 * Created by Kim Michael on 31.03.17.
 */
public interface TranslateContract {

    interface View {

        void setLanguages(String srcLanguageName, String destLanguageName);

        void clearInput(boolean showKeyboard);

        void setInput(String input);

        void clearTranslation();

        void setProgressSpinner(boolean active);

        void showTranslation(Translation translation);

        void updateEmptyView();

        String getRequestedText();

    }

    interface UserActionsListener {

        void loadTranslation();

        void swapLanguages();

        void setSrcLanguage(Language language);
        void setDestLanguage(Language language);

        void onAttachView(TranslateContract.View view,
                          Language srcLanguage, Language destLanguage);
        void onDetachView();

        void saveState(SharedPreferences prefs, Context context);

        void clearCache();

        void saveTranslationToHistory(Translation translation);

        void bookmarkTranslation(HistoryRecord historyRecord);

        void clearHistory();
        Translation getCachedTranslation();
    }
}
