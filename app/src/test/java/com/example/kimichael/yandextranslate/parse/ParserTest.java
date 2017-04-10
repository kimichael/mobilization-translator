package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.objects.Definition;
import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Example;
import com.example.kimichael.yandextranslate.data.objects.Interpretation;
import com.example.kimichael.yandextranslate.data.objects.Meaning;
import com.example.kimichael.yandextranslate.data.objects.Synonym;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.google.gson.JsonParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    private Parser parser;

    private String invalidJson = "{time - время}";
    private String timeTranslationJson = "{\"head\":{},\"def\":[{\"text\":\"time\",\"pos\":\"noun\",\"ts\":\"taɪm\",\"tr\":[{\"text\":\"время\",\"pos\":\"noun\",\"gen\":\"ср\",\"syn\":[{\"text\":\"раз\",\"pos\":\"noun\",\"gen\":\"м\"},{\"text\":\"момент\",\"pos\":\"noun\",\"gen\":\"м\"},{\"text\":\"срок\",\"pos\":\"noun\",\"gen\":\"м\"},{\"text\":\"пора\",\"pos\":\"noun\"},{\"text\":\"период\",\"pos\":\"noun\",\"gen\":\"м\"}],\"mean\":[{\"text\":\"period\"},{\"text\":\"once\"},{\"text\":\"moment\"},{\"text\":\"pore\"}],\"ex\":[{\"text\":\"daylight saving time\",\"tr\":[{\"text\":\"летнее время\"}]},{\"text\":\"take some time\",\"tr\":[{\"text\":\"занять некоторое время\"}]},{\"text\":\"real time mode\",\"tr\":[{\"text\":\"режим реального времени\"}]},{\"text\":\"expected arrival time\",\"tr\":[{\"text\":\"ожидаемое время прибытия\"}]},{\"text\":\"external time source\",\"tr\":[{\"text\":\"внешний источник времени\"}]},{\"text\":\"next time\",\"tr\":[{\"text\":\"следующий раз\"}]},{\"text\":\"initial time\",\"tr\":[{\"text\":\"начальный момент\"}]},{\"text\":\"reasonable time frame\",\"tr\":[{\"text\":\"разумный срок\"}]},{\"text\":\"winter time\",\"tr\":[{\"text\":\"зимняя пора\"}]},{\"text\":\"incubation time\",\"tr\":[{\"text\":\"инкубационный период\"}]}]},{\"text\":\"час\",\"pos\":\"noun\",\"gen\":\"м\",\"mean\":[{\"text\":\"hour\"}],\"ex\":[{\"text\":\"checkout time\",\"tr\":[{\"text\":\"расчетный час\"}]}]},{\"text\":\"эпоха\",\"pos\":\"noun\",\"gen\":\"ж\",\"mean\":[{\"text\":\"era\"}]},{\"text\":\"век\",\"pos\":\"noun\",\"gen\":\"м\",\"mean\":[{\"text\":\"age\"}]},{\"text\":\"такт\",\"pos\":\"noun\",\"gen\":\"м\",\"syn\":[{\"text\":\"темп\",\"pos\":\"noun\",\"gen\":\"м\"}],\"mean\":[{\"text\":\"cycle\"},{\"text\":\"rate\"}]},{\"text\":\"жизнь\",\"pos\":\"noun\",\"gen\":\"ж\",\"mean\":[{\"text\":\"life\"}]}]},{\"text\":\"time\",\"pos\":\"verb\",\"ts\":\"taɪm\",\"tr\":[{\"text\":\"приурочивать\",\"pos\":\"verb\",\"asp\":\"несов\"},{\"text\":\"рассчитывать\",\"pos\":\"verb\",\"asp\":\"несов\",\"mean\":[{\"text\":\"count\"}]}]},{\"text\":\"time\",\"pos\":\"adjective\",\"ts\":\"taɪm\",\"tr\":[{\"text\":\"временный\",\"pos\":\"adjective\",\"syn\":[{\"text\":\"временной\",\"pos\":\"adjective\"}],\"mean\":[{\"text\":\"temporary\"}],\"ex\":[{\"text\":\"time series model\",\"tr\":[{\"text\":\"модель временных рядов\"}]},{\"text\":\"time correlation function\",\"tr\":[{\"text\":\"временная корреляционная функция\"}]},{\"text\":\"time code\",\"tr\":[{\"text\":\"временной код\"}]}]},{\"text\":\"повременный\",\"pos\":\"adjective\",\"ex\":[{\"text\":\"time payment\",\"tr\":[{\"text\":\"повременная оплата\"}]}]}]}]}";
    private String simpleJson = "{\"code\":200,\"lang\":\"en-ru\",\"text\":[\"время\"]}";

    @Before
    public void setup() {
        parser = new Parser();
    }

    @Test(expected = JsonParseException.class)
    public void parseInvalidData() {
        parser.parseDictionaryTranslation(invalidJson);
    }

    @Test
    public void parseNormalData() {
        DictionaryTranslation timeTranslation = parser.parseDictionaryTranslation(timeTranslationJson);

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
    public void testSimpleTranslationJson() {
        Translation translation = parser.parseTranslation(simpleJson);
        Assert.assertEquals("время", translation.getTranslatedWord());
    }
}
