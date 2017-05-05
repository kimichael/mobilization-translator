package com.example.kimichael.yandextranslate.sections.translate;

import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.util.rules.RxImmediateSchedulerRule;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Kim Michael on 04.05.17
 */
public class TranslatePresenterTest {

    @ClassRule
    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    private TranslatePresenter presenter;
    @Mock
    private TranslationRepository repository;
    @Mock
    private TranslateContract.View view;

    private static final String srcWord = "Source typed word";
    private static final String translatedWord = "Translated Word";
    private Language firstLanguage = new Language("Russian", "ru");
    private Language secondLanguage = new Language("English", "en");
    private LanguageDirection languageDirection = new LanguageDirection(firstLanguage, secondLanguage);
    private List<Language> languages;
    private List<LanguageDirection> languageDirections;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(view.getRequestedText()).thenReturn(srcWord);
        when(repository.getTranslation(anyString(), any())).thenAnswer(new Answer<Single<Translation>>() {
            @Override
            public Single<Translation> answer(InvocationOnMock invocation) throws Throwable {
                String srcWord = (String) invocation.getArguments()[0];
                return Single.just(new Translation(srcWord, translatedWord));
            }
        });
        languages = new ArrayList<>();
        languages.add(firstLanguage);
        languages.add(secondLanguage);
        languageDirections = new ArrayList<>();
        languageDirections.add(new LanguageDirection(firstLanguage, secondLanguage));
        languageDirections.add(new LanguageDirection(secondLanguage, firstLanguage));
        when(repository.retrieveLanguages()).thenReturn(Single.just(languages));
        when(repository.retrieveLanguageDirections()).thenReturn(Single.just(languageDirections));

        presenter = new TranslatePresenter(repository);
        presenter.onAttachView(view, firstLanguage, secondLanguage);
    }

    @Test
    public void whenStartedLoading_ShowsRequestedTranslation() {
        ArgumentCaptor<Translation> translationArgumentCaptor = ArgumentCaptor.forClass(Translation.class);

        presenter.startLoadingTranslation();
        verify(view).showTranslation(translationArgumentCaptor.capture());
        Translation capturedTranslation = translationArgumentCaptor.getValue();
        assertEquals(capturedTranslation.getSrcWord(), srcWord);
        assertEquals(capturedTranslation.getTranslatedWord(), translatedWord);
    }

    @Test
    public void canShowHistoryRecord() {
        ArgumentCaptor<Translation> translationCaptor =
                ArgumentCaptor.forClass(Translation.class);
        ArgumentCaptor<String> srcLanguageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> destLanguageCaptor = ArgumentCaptor.forClass(String.class);

        presenter.showHistoryRecord(
                new HistoryRecord(new Translation(srcWord, translatedWord), languageDirection));
        verify(view).showTranslation(translationCaptor.capture());
        verify(view, times(2)).setLanguages(srcLanguageCaptor.capture(), destLanguageCaptor.capture());

        assertEquals(firstLanguage.getName(), srcLanguageCaptor.getValue());
        assertEquals(secondLanguage.getName(), destLanguageCaptor.getValue());
        assertEquals("Russian", srcLanguageCaptor.getValue());
        assertEquals("English", destLanguageCaptor.getValue());
    }
}
