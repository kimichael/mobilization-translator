package com.example.kimichael.yandextranslate.data.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.data.provider.TranslationContract;

import java.util.List;

public class Translation {

    private String srcWord;
    private String translatedWord;
    @Nullable
    private List<Definition> definitions;

    public String getSrcWord() {
        return srcWord;
    }

    public void setSrcWord(String srcWord) {
        this.srcWord = srcWord;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    @Nullable
    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(@Nullable List<Definition> definitions) {
        this.definitions = definitions;
    }

    public boolean isFull() {
        return (null != definitions);
    }

    public ContentValues toContentValues(LanguageDirection direction) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TranslationContract.WordEntry.COLUMN_SRC_WORD, srcWord);
        contentValues.put(TranslationContract.WordEntry.COLUMN_DEST_WORD, translatedWord);
        contentValues.put(TranslationContract.WordEntry.COLUMN_SRC_LANG, direction.getSrcLangCode());
        contentValues.put(TranslationContract.WordEntry.COLUMN_DEST_LANG, direction.getDestLangCode());
        return contentValues;
    }

    public static Translation from(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        Translation translation = new Translation();
        translation.setSrcWord(cursor.getString(cursor.getColumnIndex(TranslationContract.WordEntry.COLUMN_SRC_WORD)));
        translation.setTranslatedWord(cursor.getString(cursor.getColumnIndex(TranslationContract.WordEntry.COLUMN_DEST_WORD)));
        return translation;
    }

    // Format translation to show to user
    // We need context to get formatting strings
    public Spanned toHtml(Context context) {
        StringBuilder sb = new StringBuilder();
        Definition firstDefinition = getDefinitions().get(0);
        if (firstDefinition != null) {
            if (!firstDefinition.getTranscription().equals("")) {
                sb.append(context.getString(R.string.format_definition_with_genus,
                        firstDefinition.getSrcWord(), firstDefinition.getGenus()));
            } else if (!firstDefinition.getGenus().equals("")) {
                sb.append(context.getString(R.string.format_definition_with_transcription,
                        firstDefinition.getSrcWord(), firstDefinition.getTranscription()));
            } else {
                sb.append(context.getString(R.string.format_main_translation,
                        firstDefinition.getSrcWord()));
            }
        }
        for (int i = 0; i < definitions.size(); i++) {
            Definition definition = definitions.get(i);
            List<Interpretation> interpretations = definition.getInterpretations();
            for (int j = 0; j < interpretations.size(); j++) {
                Interpretation interpretation = interpretations.get(j);
                
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_COMPACT);
        }
        return Html.fromHtml(sb.toString());

    }
}
