package com.example.kimichael.yandextranslate.modules;

import android.content.Context;

import com.example.kimichael.yandextranslate.BuildConfig;
import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.TranslationRepositoryImpl;
import com.example.kimichael.yandextranslate.data.TranslationSource;
import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.data.LocalTranslationSource;
import com.example.kimichael.yandextranslate.network.NetworkTranslationSourceImpl;
import com.example.kimichael.yandextranslate.network.NetworkTranslationSource;
import com.example.kimichael.yandextranslate.network.YandexDictionaryClient;
import com.example.kimichael.yandextranslate.network.YandexTranslateClient;
import com.example.kimichael.yandextranslate.parse.DictionaryTranslationDeserializer;
import com.example.kimichael.yandextranslate.parse.LanguageDirectionsDeserializer;
import com.example.kimichael.yandextranslate.parse.LanguagesDeserializer;
import com.example.kimichael.yandextranslate.parse.TranslationDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ContextModule {

    Context mContext;

    public ContextModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    TranslationRepository provideTranslationRepository(
            @Named("LocalTranslationSource") TranslationSource localTranslationSource,
            @Named("NetworkTranslationSource") NetworkTranslationSource networkTranslationSource) {
        return new TranslationRepositoryImpl(localTranslationSource, networkTranslationSource);
    }

    @Provides
    @Singleton
    @Named("LocalTranslationSource")
    TranslationSource provideLocalTranslationSource() {
        return new LocalTranslationSource();
    }

    @Provides
    @Singleton
    @Named("NetworkTranslationSource")
    NetworkTranslationSource provideNetworkTranslationSource(
            @Named("YandexTranslateClient") YandexTranslateClient yandexTranslateClient,
            @Named("YandexDictionaryClient") YandexDictionaryClient yandexDictionaryClient) {
        return new NetworkTranslationSourceImpl(yandexTranslateClient, yandexDictionaryClient);
    }

    @Provides
    @Singleton
    @Named("YandexTranslateClient")
    YandexTranslateClient provideYandexTranslateClient(@Named("Gson") Gson gson, @Named("YandexTranslateOkHttpClient") OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(YandexTranslateClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build().create(YandexTranslateClient.class);
    }

    @Provides
    @Singleton
    @Named("YandexDictionaryClient")
    YandexDictionaryClient provideYandexDictionaryClient(@Named("Gson") Gson gson, @Named("YandexDictionaryOkHttpClient") OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(YandexDictionaryClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build().create(YandexDictionaryClient.class);
    }

    @Provides
    @Singleton
    @Named("Gson")
    Gson provideGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DictionaryTranslation.class, new DictionaryTranslationDeserializer())
                .registerTypeAdapter(Translation.class, new TranslationDeserializer())
                // Here is an awful workaround. The classes are of types List<Language> and List<LanguageDirection>
                .registerTypeAdapter(new TypeToken<List<Language>>(){}.getType(), new LanguagesDeserializer())
                .registerTypeAdapter(new TypeToken<List<LanguageDirection>>(){}.getType(), new LanguageDirectionsDeserializer())
                .create();
        return gson;
    }

    @Provides
    @Singleton
    @Named("YandexTranslateOkHttpClient")
    OkHttpClient provideTranslateOkHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("key", BuildConfig.YANDEX_TRANSLATE_API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);}})
                .addInterceptor(interceptor)
                .build();
        return client;
    }

    @Provides
    @Singleton
    @Named("YandexDictionaryOkHttpClient")
    OkHttpClient provideDictionaryOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url().newBuilder().addQueryParameter("key", BuildConfig.YANDEX_DICTIONARY_API_KEY).build();
                        request = request.newBuilder().url(url).build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        return client;
    }
}
