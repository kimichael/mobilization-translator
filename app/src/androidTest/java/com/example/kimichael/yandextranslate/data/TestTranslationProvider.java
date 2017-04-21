package com.example.kimichael.yandextranslate.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.test.ProviderTestCase2;

import com.example.kimichael.yandextranslate.data.provider.TranslationContract;
import com.example.kimichael.yandextranslate.data.provider.TranslationProvider;

public class TestTranslationProvider extends ProviderTestCase2<TranslationProvider> {

    public TestTranslationProvider() {
        super(TranslationProvider.class, TranslationContract.CONTENT_AUTHORITY);
    }

    private ContentResolver contentResolver;

    @Override
    protected void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
    }

    public void testClearRecordsFromProvider() {
        mContext.getContentResolver().delete(
                TranslationContract.WordEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                TranslationContract.DefinitionEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                TranslationContract.WordEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Word records are not deleted", 0, cursor.getCount());
        cursor = mContext.getContentResolver().query(
                TranslationContract.DefinitionEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Example records are not deleted", 0, cursor.getCount());
        cursor.close();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                TranslationProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + TranslationContract.CONTENT_AUTHORITY,
                    providerInfo.authority, TranslationContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }



}
