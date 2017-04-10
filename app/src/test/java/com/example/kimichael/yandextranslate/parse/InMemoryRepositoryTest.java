package com.example.kimichael.yandextranslate.parse;

import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.TranslationRepositoryImpl;
import com.example.kimichael.yandextranslate.data.TranslationSource;
import com.example.kimichael.yandextranslate.data.local.LocalTranslationSource;
import com.example.kimichael.yandextranslate.data.network.NetworkDataSource;
import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InMemoryRepositoryTest {

    private TranslationRepository repository;

    DictionaryTranslation dictionaryTranslation;
    Translation translation;

    @Before
    public void setup() {
        repository = new TranslationRepositoryImpl(new LocalTranslationSource(), new NetworkDataSource());
        Parser parser = new Parser();
        String timeTranslationJson = "{\"head\":{},\"def\":[{\"text\":\"time\",\"pos\":\"noun\",\"ts\":\"taɪm\",\"tr\":[{\"text\":\"время\",\"pos\":\"noun\",\"gen\":\"ср\",\"syn\":[{\"text\":\"раз\",\"pos\":\"noun\",\"gen\":\"м\"},{\"text\":\"момент\",\"pos\":\"noun\",\"gen\":\"м\"},{\"text\":\"срок\",\"pos\":\"noun\",\"gen\":\"м\"},{\"text\":\"пора\",\"pos\":\"noun\"},{\"text\":\"период\",\"pos\":\"noun\",\"gen\":\"м\"}],\"mean\":[{\"text\":\"period\"},{\"text\":\"once\"},{\"text\":\"moment\"},{\"text\":\"pore\"}],\"ex\":[{\"text\":\"daylight saving time\",\"tr\":[{\"text\":\"летнее время\"}]},{\"text\":\"take some time\",\"tr\":[{\"text\":\"занять некоторое время\"}]},{\"text\":\"real time mode\",\"tr\":[{\"text\":\"режим реального времени\"}]},{\"text\":\"expected arrival time\",\"tr\":[{\"text\":\"ожидаемое время прибытия\"}]},{\"text\":\"external time source\",\"tr\":[{\"text\":\"внешний источник времени\"}]},{\"text\":\"next time\",\"tr\":[{\"text\":\"следующий раз\"}]},{\"text\":\"initial time\",\"tr\":[{\"text\":\"начальный момент\"}]},{\"text\":\"reasonable time frame\",\"tr\":[{\"text\":\"разумный срок\"}]},{\"text\":\"winter time\",\"tr\":[{\"text\":\"зимняя пора\"}]},{\"text\":\"incubation time\",\"tr\":[{\"text\":\"инкубационный период\"}]}]},{\"text\":\"час\",\"pos\":\"noun\",\"gen\":\"м\",\"mean\":[{\"text\":\"hour\"}],\"ex\":[{\"text\":\"checkout time\",\"tr\":[{\"text\":\"расчетный час\"}]}]},{\"text\":\"эпоха\",\"pos\":\"noun\",\"gen\":\"ж\",\"mean\":[{\"text\":\"era\"}]},{\"text\":\"век\",\"pos\":\"noun\",\"gen\":\"м\",\"mean\":[{\"text\":\"age\"}]},{\"text\":\"такт\",\"pos\":\"noun\",\"gen\":\"м\",\"syn\":[{\"text\":\"темп\",\"pos\":\"noun\",\"gen\":\"м\"}],\"mean\":[{\"text\":\"cycle\"},{\"text\":\"rate\"}]},{\"text\":\"жизнь\",\"pos\":\"noun\",\"gen\":\"ж\",\"mean\":[{\"text\":\"life\"}]}]},{\"text\":\"time\",\"pos\":\"verb\",\"ts\":\"taɪm\",\"tr\":[{\"text\":\"приурочивать\",\"pos\":\"verb\",\"asp\":\"несов\"},{\"text\":\"рассчитывать\",\"pos\":\"verb\",\"asp\":\"несов\",\"mean\":[{\"text\":\"count\"}]}]},{\"text\":\"time\",\"pos\":\"adjective\",\"ts\":\"taɪm\",\"tr\":[{\"text\":\"временный\",\"pos\":\"adjective\",\"syn\":[{\"text\":\"временной\",\"pos\":\"adjective\"}],\"mean\":[{\"text\":\"temporary\"}],\"ex\":[{\"text\":\"time series model\",\"tr\":[{\"text\":\"модель временных рядов\"}]},{\"text\":\"time correlation function\",\"tr\":[{\"text\":\"временная корреляционная функция\"}]},{\"text\":\"time code\",\"tr\":[{\"text\":\"временной код\"}]}]},{\"text\":\"повременный\",\"pos\":\"adjective\",\"ex\":[{\"text\":\"time payment\",\"tr\":[{\"text\":\"повременная оплата\"}]}]}]}]}";
        String simpleJson = "{\"code\":200,\"lang\":\"en-ru\",\"text\":[\"время\"]}";
        dictionaryTranslation = parser.parseDictionaryTranslation(timeTranslationJson);
        translation = parser.parseTranslation(simpleJson);
    }

    @Test
    public void youCanRetrieveAddedTranslations() {
        repository.saveTranslation(dictionaryTranslation);
    }
}
