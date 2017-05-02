package com.example.kimichael.yandextranslate.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.kimichael.yandextranslate.data.LocalTranslationSource;
import com.example.kimichael.yandextranslate.data.LocalTranslationSourceImpl;
import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.TranslationRepositoryImpl;
import com.example.kimichael.yandextranslate.data.network.NetworkTranslationSource;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.data.provider.TranslationQueryHandler;
import com.example.kimichael.yandextranslate.parse.Parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * Created by Kim Michael on 24.04.17
 * @see LocalTranslationSourceImpl
 */
@RunWith(AndroidJUnit4.class)
public class LocalTranslationSourceTest {

    Context context;
    LocalTranslationSource localTranslationSource;
    LanguageDirection languageDirection;

    @BeforeClass
    public void setup() {
        context = InstrumentationRegistry.getContext();
        localTranslationSource = new LocalTranslationSourceImpl(
                new TranslationQueryHandler(context),
                new Parser()
        );
        languageDirection = new LanguageDirection(new Language("Russian", "ru"), new Language("English", "en"));
    }


    @Test
    public void addedTranslationsCanBeRetrieved() {
        Translation translation1 = new Translation("Яндекс", "Yandex");
        localTranslationSource.saveTranslation(translation1, languageDirection);
        // This translation is simple, so we use Yandex.Translate API
        Translation fetchedTranslation =
                localTranslationSource.getTranslation("Яндекс", languageDirection, NetworkTranslationSource.YANDEX_TRANSLATE_API)
                        .blockingGet();
        Assert.assertEquals(translation1, fetchedTranslation);
    }

    @Test
    public void twoTranslationsCanBeAdded_OneRetrieved() {
        Translation translation1 = new Translation("труп", "corpse");
        Translation translation2 = new Translation("друг", "friend");
        localTranslationSource.saveTranslation(translation1, languageDirection);
        localTranslationSource.saveTranslation(translation2, languageDirection);
        localTranslationSource.saveTranslation(translation1, languageDirection);
        localTranslationSource.saveTranslation(translation1, languageDirection);
        localTranslationSource.saveTranslation(translation2, languageDirection);
        Translation fetchedTranslation1 =
                localTranslationSource.getTranslation("труп", languageDirection,
                        NetworkTranslationSource.YANDEX_TRANSLATE_API)
                .blockingGet();
        Assert.assertEquals(translation1, fetchedTranslation1);
    }
}
