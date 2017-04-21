package com.example.kimichael.yandextranslate.data.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.data.provider.TranslationContract;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

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
        final String breakTag = "<br/>";
        final String blueComma = "<font color=\\'#3082db\\'>, </font>";
        final String tab = "&ensp;";
        Definition firstDefinition = getDefinitions().get(0);
        if (firstDefinition != null) {
            // Some definitions don't need transcriptions, like in Russian
            if (!firstDefinition.getTranscription().equals("")) {
                sb.append(context.getString(R.string.format_definition_with_transcription,
                        firstDefinition.getSrcWord(), firstDefinition.getTranscription()));
            } else if (!firstDefinition.getGenus().equals("")) {
                sb.append(context.getString(R.string.format_definition_with_genus,
                        firstDefinition.getSrcWord(), firstDefinition.getGenus()));
            } else {
                sb.append(context.getString(R.string.format_main_translation,
                        firstDefinition.getSrcWord()));
            }
        }
        sb.append(breakTag);
        for (int i = 0; i < definitions.size(); i++) {
            Definition definition = definitions.get(i);
            sb.append(context.getString(R.string.format_part_of_speech, definition.getPartOfSpeech()));
            sb.append(breakTag);
            List<Interpretation> interpretations = definition.getInterpretations();
            for (int j = 0; j < interpretations.size(); j++) {
                Interpretation interpretation = interpretations.get(j);
                // Interpretation header - ex. "1 circle, round, lap, ring"
                // or just "eye"
                if (interpretations.size() != 1)
                    sb.append(context.getString(R.string.format_order, j+1));
                else
                    sb.append(tab);
                if (interpretation.getGenus().equals(""))
                    sb.append(context.getString(R.string.format_interpretation,
                            interpretation.getDestWord()));
                else
                    sb.append(context.getString(R.string.format_interpretation_with_genus,
                            interpretation.getDestWord(), interpretation.getGenus()));
                if (interpretation.getSynonyms() != null) {
                    sb.append(blueComma);
                    sb.append(Joiner.on(blueComma).join(Iterables.transform(interpretation.getSynonyms(),
                            synonym -> {
                                if (synonym.getGenus().equals(""))
                                    return context.getString(R.string.format_synonym, synonym.getText());
                                return context.getString(R.string.format_synonym_with_genus, synonym.getText(), synonym.getGenus());
                            })));
                }
                // Meanings - "(meaning1, meaning2)"
                if (interpretation.getMeanings() != null) {
                    sb.append(breakTag + tab);
                    sb.append(context.getString(R.string.format_meanings,
                            Joiner.on(", ").join(Iterables.transform(
                                    interpretation.getMeanings(), meaning -> meaning.getText()))));
                }
                sb.append(breakTag);
                // Example - ex. "око за око - eye for eye"
                if (interpretation.getExamples() != null) {
                    sb.append(Joiner.on(breakTag).join(Iterables.transform(interpretation.getExamples(),
                            example -> context.getString(R.string.format_example, example.getText(), example.getTranslations().get(0)))));
                    sb.append(breakTag);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_COMPACT);
        }
        return Html.fromHtml(sb.toString());

    }
}
