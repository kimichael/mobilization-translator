package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.Definition;
import com.example.kimichael.yandextranslate.data.objects.Example;
import com.example.kimichael.yandextranslate.data.objects.Interpretation;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.Meaning;
import com.example.kimichael.yandextranslate.data.objects.Synonym;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeserializersTest {

    private Gson gson;

    @Before
    public void setup() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Translation.class, new TranslationDeserializer())
                .registerTypeAdapter(Translation.class, new TranslationDeserializer())
                .registerTypeAdapter(new TypeToken<List<Language>>(){}.getType(), new LanguagesDeserializer())
                .create();
    }

    @Test
    public void testDictionaryTranslationDeserializer() {
        String translationJson = "{\"head\":{},\"def\":[{\"text\":\"time\",\"pos\":\"noun\",\"ts\":\"taɪm\",\"tr\":[{\"text\":\"время\",\"pos\":\"noun\",\"gen\":\"ср\",\"syn\":[{\"text\":\"раз\",\"pos\":\"noun\",\"gen\":\"м\"},{\"text\":\"момент\",\"pos\":\"noun\",\"gen\":\"м\"},{\"text\":\"срок\",\"pos\":\"noun\",\"gen\":\"м\"},{\"text\":\"пора\",\"pos\":\"noun\"},{\"text\":\"период\",\"pos\":\"noun\",\"gen\":\"м\"}],\"mean\":[{\"text\":\"period\"},{\"text\":\"once\"},{\"text\":\"moment\"},{\"text\":\"pore\"}],\"ex\":[{\"text\":\"daylight saving time\",\"tr\":[{\"text\":\"летнее время\"}]},{\"text\":\"take some time\",\"tr\":[{\"text\":\"занять некоторое время\"}]},{\"text\":\"real time mode\",\"tr\":[{\"text\":\"режим реального времени\"}]},{\"text\":\"expected arrival time\",\"tr\":[{\"text\":\"ожидаемое время прибытия\"}]},{\"text\":\"external time source\",\"tr\":[{\"text\":\"внешний источник времени\"}]},{\"text\":\"next time\",\"tr\":[{\"text\":\"следующий раз\"}]},{\"text\":\"initial time\",\"tr\":[{\"text\":\"начальный момент\"}]},{\"text\":\"reasonable time frame\",\"tr\":[{\"text\":\"разумный срок\"}]},{\"text\":\"winter time\",\"tr\":[{\"text\":\"зимняя пора\"}]},{\"text\":\"incubation time\",\"tr\":[{\"text\":\"инкубационный период\"}]}]},{\"text\":\"час\",\"pos\":\"noun\",\"gen\":\"м\",\"mean\":[{\"text\":\"hour\"}],\"ex\":[{\"text\":\"checkout time\",\"tr\":[{\"text\":\"расчетный час\"}]}]},{\"text\":\"эпоха\",\"pos\":\"noun\",\"gen\":\"ж\",\"mean\":[{\"text\":\"era\"}]},{\"text\":\"век\",\"pos\":\"noun\",\"gen\":\"м\",\"mean\":[{\"text\":\"age\"}]},{\"text\":\"такт\",\"pos\":\"noun\",\"gen\":\"м\",\"syn\":[{\"text\":\"темп\",\"pos\":\"noun\",\"gen\":\"м\"}],\"mean\":[{\"text\":\"cycle\"},{\"text\":\"rate\"}]},{\"text\":\"жизнь\",\"pos\":\"noun\",\"gen\":\"ж\",\"mean\":[{\"text\":\"life\"}]}]},{\"text\":\"time\",\"pos\":\"verb\",\"ts\":\"taɪm\",\"tr\":[{\"text\":\"приурочивать\",\"pos\":\"verb\",\"asp\":\"несов\"},{\"text\":\"рассчитывать\",\"pos\":\"verb\",\"asp\":\"несов\",\"mean\":[{\"text\":\"count\"}]}]},{\"text\":\"time\",\"pos\":\"adjective\",\"ts\":\"taɪm\",\"tr\":[{\"text\":\"временный\",\"pos\":\"adjective\",\"syn\":[{\"text\":\"временной\",\"pos\":\"adjective\"}],\"mean\":[{\"text\":\"temporary\"}],\"ex\":[{\"text\":\"time series model\",\"tr\":[{\"text\":\"модель временных рядов\"}]},{\"text\":\"time correlation function\",\"tr\":[{\"text\":\"временная корреляционная функция\"}]},{\"text\":\"time code\",\"tr\":[{\"text\":\"временной код\"}]}]},{\"text\":\"повременный\",\"pos\":\"adjective\",\"ex\":[{\"text\":\"time payment\",\"tr\":[{\"text\":\"повременная оплата\"}]}]}]}]}";
        Translation timeTranslation = gson.fromJson(translationJson, Translation.class);
        Assert.assertEquals("время", timeTranslation.getTranslatedWord());

        Assert.assertNull(timeTranslation.getSrcWord());
        List<Definition> definitions = timeTranslation.getDefinitions();
        Assert.assertEquals(3, definitions.size());

        Definition firstDefinition = definitions.get(0);
        Assert.assertEquals("time", firstDefinition.getSrcWord());
        Assert.assertEquals("noun", firstDefinition.getPartOfSpeech());
        Assert.assertEquals("taɪm", firstDefinition.getTranscription());

        List<Interpretation> interpretations = firstDefinition.getInterpretations();
        Assert.assertEquals(6, interpretations.size());

        Interpretation firstInterpretation = interpretations.get(0);
        Assert.assertEquals("время", firstInterpretation.getDestWord());
        Assert.assertEquals("noun", firstInterpretation.getPartOfSpeech());
        Assert.assertEquals("ср", firstInterpretation.getGenus());

        List<Synonym> synonyms = firstInterpretation.getSynonyms();
        List<Meaning> meanings = firstInterpretation.getMeanings();
        List<Example> examples = firstInterpretation.getExamples();
        Assert.assertEquals(4, meanings.size());
        Assert.assertEquals(5, synonyms.size());
        Assert.assertEquals(10, examples.size());

        Synonym firstSynonym = synonyms.get(0);
        Assert.assertEquals("раз", firstSynonym.getText());
        Assert.assertEquals("м", firstSynonym.getGenus());
        Assert.assertEquals("noun", firstSynonym.getPartOfSpeech());

        Meaning firstMeaning = meanings.get(0);
        Assert.assertEquals("period", firstMeaning.getText());

        Example example = examples.get(0);
        Assert.assertEquals("daylight saving time", example.getText());
        Assert.assertEquals("летнее время", example.getTranslations().get(0));
    }

    @Test
    public void testTranslationDeserializer() {
        String translationJson = "{\"code\":200,\"lang\":\"ru-en\",\"text\":[\"crown\"]}";
        Translation translation = gson.fromJson(translationJson, Translation.class);
        Assert.assertEquals("crown", translation.getTranslatedWord());
    }

    @Test
    public void testLanguagesDeserializer() {
        String languagesJson = "{\"dirs\":[\"az-ru\",\"be-bg\",\"be-cs\",\"be-de\",\"be-en\",\"be-es\",\"be-fr\",\"be-it\",\"be-pl\",\"be-ro\",\"be-ru\",\"be-sr\",\"be-tr\",\"bg-be\",\"bg-ru\",\"bg-uk\",\"ca-en\",\"ca-ru\",\"cs-be\",\"cs-en\",\"cs-ru\",\"cs-uk\",\"da-en\",\"da-ru\",\"de-be\",\"de-en\",\"de-es\",\"de-fr\",\"de-it\",\"de-ru\",\"de-tr\",\"de-uk\",\"el-en\",\"el-ru\",\"en-be\",\"en-ca\",\"en-cs\",\"en-da\",\"en-de\",\"en-el\",\"en-es\",\"en-et\",\"en-fi\",\"en-fr\",\"en-hu\",\"en-it\",\"en-lt\",\"en-lv\",\"en-mk\",\"en-nl\",\"en-no\",\"en-pt\",\"en-ru\",\"en-sk\",\"en-sl\",\"en-sq\",\"en-sv\",\"en-tr\",\"en-uk\",\"es-be\",\"es-de\",\"es-en\",\"es-ru\",\"es-uk\",\"et-en\",\"et-ru\",\"fi-en\",\"fi-ru\",\"fr-be\",\"fr-de\",\"fr-en\",\"fr-ru\",\"fr-uk\",\"hr-ru\",\"hu-en\",\"hu-ru\",\"hy-ru\",\"it-be\",\"it-de\",\"it-en\",\"it-ru\",\"it-uk\",\"lt-en\",\"lt-ru\",\"lv-en\",\"lv-ru\",\"mk-en\",\"mk-ru\",\"nl-en\",\"nl-ru\",\"no-en\",\"no-ru\",\"pl-be\",\"pl-ru\",\"pl-uk\",\"pt-en\",\"pt-ru\",\"ro-be\",\"ro-ru\",\"ro-uk\",\"ru-az\",\"ru-be\",\"ru-bg\",\"ru-ca\",\"ru-cs\",\"ru-da\",\"ru-de\",\"ru-el\",\"ru-en\",\"ru-es\",\"ru-et\",\"ru-fi\",\"ru-fr\",\"ru-hr\",\"ru-hu\",\"ru-hy\",\"ru-it\",\"ru-lt\",\"ru-lv\",\"ru-mk\",\"ru-nl\",\"ru-no\",\"ru-pl\",\"ru-pt\",\"ru-ro\",\"ru-sk\",\"ru-sl\",\"ru-sq\",\"ru-sr\",\"ru-sv\",\"ru-tr\",\"ru-uk\",\"sk-en\",\"sk-ru\",\"sl-en\",\"sl-ru\",\"sq-en\",\"sq-ru\",\"sr-be\",\"sr-ru\",\"sr-uk\",\"sv-en\",\"sv-ru\",\"tr-be\",\"tr-de\",\"tr-en\",\"tr-ru\",\"tr-uk\",\"uk-bg\",\"uk-cs\",\"uk-de\",\"uk-en\",\"uk-es\",\"uk-fr\",\"uk-it\",\"uk-pl\",\"uk-ro\",\"uk-ru\",\"uk-sr\",\"uk-tr\"],\"langs\":{\"af\":\"Африкаанс\",\"am\":\"Амхарский\",\"ar\":\"Арабский\",\"az\":\"Азербайджанский\",\"ba\":\"Башкирский\",\"be\":\"Белорусский\",\"bg\":\"Болгарский\",\"bn\":\"Бенгальский\",\"bs\":\"Боснийский\",\"ca\":\"Каталанский\",\"ceb\":\"Себуанский\",\"cs\":\"Чешский\",\"cy\":\"Валлийский\",\"da\":\"Датский\",\"de\":\"Немецкий\",\"el\":\"Греческий\",\"en\":\"Английский\",\"eo\":\"Эсперанто\",\"es\":\"Испанский\",\"et\":\"Эстонский\",\"eu\":\"Баскский\",\"fa\":\"Персидский\",\"fi\":\"Финский\",\"fr\":\"Французский\",\"ga\":\"Ирландский\",\"gd\":\"Шотландский (гэльский)\",\"gl\":\"Галисийский\",\"gu\":\"Гуджарати\",\"he\":\"Иврит\",\"hi\":\"Хинди\",\"hr\":\"Хорватский\",\"ht\":\"Гаитянский\",\"hu\":\"Венгерский\",\"hy\":\"Армянский\",\"id\":\"Индонезийский\",\"is\":\"Исландский\",\"it\":\"Итальянский\",\"ja\":\"Японский\",\"jv\":\"Яванский\",\"ka\":\"Грузинский\",\"kk\":\"Казахский\",\"km\":\"Кхмерский\",\"kn\":\"Каннада\",\"ko\":\"Корейский\",\"ky\":\"Киргизский\",\"la\":\"Латынь\",\"lb\":\"Люксембургский\",\"lo\":\"Лаосский\",\"lt\":\"Литовский\",\"lv\":\"Латышский\",\"mg\":\"Малагасийский\",\"mhr\":\"Марийский\",\"mi\":\"Маори\",\"mk\":\"Македонский\",\"ml\":\"Малаялам\",\"mn\":\"Монгольский\",\"mr\":\"Маратхи\",\"mrj\":\"Горномарийский\",\"ms\":\"Малайский\",\"mt\":\"Мальтийский\",\"my\":\"Бирманский\",\"ne\":\"Непальский\",\"nl\":\"Голландский\",\"no\":\"Норвежский\",\"pa\":\"Панджаби\",\"pap\":\"Папьяменто\",\"pl\":\"Польский\",\"pt\":\"Португальский\",\"ro\":\"Румынский\",\"ru\":\"Русский\",\"si\":\"Сингальский\",\"sk\":\"Словацкий\",\"sl\":\"Словенский\",\"sq\":\"Албанский\",\"sr\":\"Сербский\",\"su\":\"Сунданский\",\"sv\":\"Шведский\",\"sw\":\"Суахили\",\"ta\":\"Тамильский\",\"te\":\"Телугу\",\"tg\":\"Таджикский\",\"th\":\"Тайский\",\"tl\":\"Тагальский\",\"tr\":\"Турецкий\",\"tt\":\"Татарский\",\"udm\":\"Удмуртский\",\"uk\":\"Украинский\",\"ur\":\"Урду\",\"uz\":\"Узбекский\",\"vi\":\"Вьетнамский\",\"xh\":\"Коса\",\"yi\":\"Идиш\",\"zh\":\"Китайский\"}}";
        Type listType = new TypeToken<List<Language>>(){}.getType();
        List<Language> languages = gson.fromJson(languagesJson, listType);
        Assert.assertEquals("af", languages.get(0).getLanguageCode());
        Assert.assertEquals("Африкаанс", languages.get(0).getName());
        Assert.assertEquals("bn", languages.get(7).getLanguageCode());
        Assert.assertEquals("Бенгальский", languages.get(7).getName());
    }
}
