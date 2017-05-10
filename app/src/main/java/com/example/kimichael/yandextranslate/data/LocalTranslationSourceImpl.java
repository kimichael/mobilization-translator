package com.example.kimichael.yandextranslate.data;

import com.example.kimichael.yandextranslate.data.objects.Definition;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.data.provider.TranslationContract;
import com.example.kimichael.yandextranslate.data.provider.TranslationQueryHandler;
import com.example.kimichael.yandextranslate.data.network.NetworkTranslationSource;
import com.example.kimichael.yandextranslate.parse.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Kim Michael on 31.03.17.
 * Internal storage of the phone itself
 */
public class LocalTranslationSourceImpl implements LocalTranslationSource {

    TranslationQueryHandler mQueryHandler;
    Parser mParser;

    public LocalTranslationSourceImpl(TranslationQueryHandler queryHandler, Parser parser) {
        mQueryHandler = checkNotNull(queryHandler);
        mParser = checkNotNull(parser);
    }

    @Override
    public Single<Translation> getTranslation(String requestedText,
                                 LanguageDirection languageDirection,
                                 @NetworkTranslationSource.TranslationApi int translationApi) {
        mQueryHandler.startTranslationQuery(requestedText, languageDirection);
        mQueryHandler.startDefinitionsQuery(requestedText, languageDirection);
        return Single.zip(Single.create(new SingleOnSubscribe<Translation>() {
            @Override
            public void subscribe(SingleEmitter<Translation> e) throws Exception {
                TranslationQueryHandler.AsyncQueryListener listener = (token, cookie, cursor) -> {
                    Translation translation = Translation.from(cursor);
                    e.onSuccess(translation);
                };
                mQueryHandler.setTranslationQueryListener(listener);
            }
        }).doOnError(Throwable::printStackTrace), Single.create(new SingleOnSubscribe<List<Definition>>() {
            @Override
            public void subscribe(SingleEmitter<List<Definition>> e) throws Exception {
                Timber.d("Started definitions query");
                TranslationQueryHandler.AsyncQueryListener listener = (token, cookie, cursor) -> {
                    Timber.d("Listener got a new definitions query");
                    if (cursor == null || cursor.getCount() == 0) {
                        e.onSuccess(new ArrayList<>());
                        return;
                    }
                    List<Definition> definitions = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        Timber.d("New definition gotten from db");
                        Definition definition = new Definition(
                                cursor.getString(cursor.getColumnIndex(
                                        TranslationContract.DefinitionEntry.COLUMN_WORD_KEY)),
                                cursor.getString(cursor.getColumnIndex(
                                        TranslationContract.DefinitionEntry.COLUMN_PART_OF_SPEECH)),
                                cursor.getInt(cursor.getColumnIndex(
                                        TranslationContract.DefinitionEntry.COLUMN_ORDER)),
                                cursor.getString(cursor.getColumnIndex(
                                        TranslationContract.DefinitionEntry.COLUMN_GENUS)),
                                cursor.getString(cursor.getColumnIndex(
                                        TranslationContract.DefinitionEntry.COLUMN_TRANSCRIPTION)),
                                mParser.parseDefinitionChildren(
                                        cursor.getString(cursor.getColumnIndex(
                                                TranslationContract.DefinitionEntry.COLUMN_JSON_CHILDREN)))
                        );
                        definitions.add(definition);
                    }
                    e.onSuccess(definitions);
                };
                mQueryHandler.setDefinitionsListener(listener);
            }
        }).timeout(3L, TimeUnit.SECONDS)
                .doOnError(Throwable::printStackTrace), (translation, definitions) -> {
            Timber.d("Translation got from local data source");
            if (definitions.size() > 0)
                translation.setDefinitions(definitions);
            return translation;
        });
    }

    @Override
    public void bookmarkTranslation(HistoryRecord historyRecord) {
        mQueryHandler.startBookmarkUpdate(historyRecord);
    }

    @Override
    public void saveTranslation(Translation translation, LanguageDirection languageDirection) {
        mQueryHandler.startTranslationInsert(translation, languageDirection);
        if (translation.isFull())
            for (Definition definition : translation.getDefinitions()) {
                definition.setLanguageDirection(languageDirection);
                mQueryHandler.startDefinitionInsert(definition, translation.getSrcWord(), languageDirection);
            }
    }

    @Override
    public void saveLanguageDirections(List<LanguageDirection> supportedLanguageDirections) {
        int i = 0;
        for (LanguageDirection languageDirection : supportedLanguageDirections) {
            mQueryHandler.startInsert(i++, null,
                    TranslationContract.LanguageDirectionEntry.CONTENT_URI,
                    languageDirection.toContentValues());
        }
    }

    @Override
    public void saveLanguages(List<Language> languages) {
        int i = 0;
        for (Language language : languages) {
            mQueryHandler.startInsert(i++, null,
                    TranslationContract.LanguageEntry.CONTENT_URI,
                    language.toContentValues());
        }
    }

    @Override
    public Single<Boolean> isDictSupported(LanguageDirection direction) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> e) throws Exception {
                TranslationQueryHandler.AsyncQueryListener listener = (token, cookie, cursor) -> {
                    if (cursor == null || cursor.getCount() == 0) {
                        // This can also mean that the direction is not in the table
                        // So we will use Yandex.Translate
                        e.onSuccess(false);
                    } else {
                        cursor.moveToFirst();
                        if (cursor.getInt(cursor.getColumnIndex(
                                TranslationContract.LanguageDirectionEntry.COLUMN_API_DICT_AVAILABLE)) > 0)
                            e.onSuccess(true);
                        else
                            e.onSuccess(false);

                    }
                };
                mQueryHandler.setIsDictSupportedListener(listener);
                mQueryHandler.startIsDictSupportedQuery(direction);
            }
        });
    }

    @Override
    public void clearHistory() {
        mQueryHandler.clearHistory();
    }

    @Override
    public Single<List<Language>> retrieveLanguages() {
        return Single.create(new SingleOnSubscribe<List<Language>>() {
            @Override
            public void subscribe(SingleEmitter<List<Language>> e) throws Exception {
                TranslationQueryHandler.AsyncQueryListener listener = (token, cookie, cursor) -> {
                    List<Language> languages = new ArrayList<>();
                    if (cursor == null || cursor.getCount() == 0) {
                        e.onError(new Throwable("Cannot retrieve languages from db"));
                    } else {
                        while (cursor.moveToNext()) {
                            languages.add(Language.from(cursor));
                        }
                        e.onSuccess(languages);
                    }
                };
                mQueryHandler.setLanguagesListener(listener);
                mQueryHandler.startLanguagesQuery();
                Timber.d("Query for retrieving languages started");
            }
        });
    }

    @Override
    public Single<List<LanguageDirection>> retrieveLanguageDirections() {
        return Single.create(new SingleOnSubscribe<List<LanguageDirection>>() {
            @Override
            public void subscribe(SingleEmitter<List<LanguageDirection>> e) throws Exception {
                TranslationQueryHandler.AsyncQueryListener listener = (token, cookie, cursor) -> {
                    List<LanguageDirection> languageDirections = new ArrayList<>();
                    if (cursor == null || cursor.getCount() == 0) {
                        e.onSuccess(languageDirections);
                    } else {
                        while (cursor.moveToNext()) {
                            languageDirections.add(LanguageDirection.from(cursor));
                        }
                        e.onSuccess(languageDirections);
                    }
                };
                mQueryHandler.setLanguageDirectionsListener(listener);
                mQueryHandler.startLanguageDirectionsQuery();
            }
        });
    }
}
