package com.example.kimichael.yandextranslate.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.adapters.LanguageAdapter;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.provider.TranslationContract;
import com.example.kimichael.yandextranslate.util.rules.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;

/**
 * Created by Kim Michael on 31.03.17
 * Activity is very small, so I don't break it into parts
 */
public class SelectLanguageActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        LanguageAdapter.OnLanguageItemClickListener {

    public static final int PICK_SRC_LANGUAGE_REQUEST = 1;
    public static final int PICK_DEST_LANGUAGE_REQUEST = 2;
    public static final String LANGUAGE = "language";

    LanguageAdapter mLanguageAdapter;
    List<Language> mLanguageList;
    @BindView(R.id.spinner) ProgressBar mLoadingSpinner;
    @BindView(R.id.error_message) TextView mErrorMessage;
    Unbinder unbinder;

    private static final String[] LANGUAGE_COLUMNS = {
            TranslationContract.LanguageEntry.TABLE_NAME + "." + TranslationContract.LanguageEntry._ID,
            TranslationContract.LanguageEntry.COLUMN_LANGUAGE_KEY,
            TranslationContract.LanguageEntry.COLUMN_LANGUAGE_NAME
    };

    private static final int LANGUAGE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        mLanguageList = new ArrayList<>();
        mLanguageAdapter = new LanguageAdapter(mLanguageList, this, this);
        unbinder = ButterKnife.bind(this);

        // Recycler view with languages
        RecyclerView languagesList = (RecyclerView) findViewById(R.id.languages_list);
        languagesList.setAdapter(mLanguageAdapter);
        // Layout items as a list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        languagesList.setLayoutManager(linearLayoutManager);
        // Set line as divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(languagesList.getContext(),
                linearLayoutManager.getOrientation());
        languagesList.addItemDecoration(dividerItemDecoration);

        // Load languages to recyclerView
        getLoaderManager().initLoader(LANGUAGE_LOADER, null, this);

        // Set back button and title on toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Make back button work
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        mLoadingSpinner.setVisibility(View.VISIBLE);
        String sortOrder = TranslationContract.LanguageEntry.COLUMN_LANGUAGE_NAME + " DESC";
        Uri languagesUri = TranslationContract.LanguageEntry.CONTENT_URI;
        return new CursorLoader(this,
                                languagesUri,
                                LANGUAGE_COLUMNS,
                                null,
                                null,
                                sortOrder
                );
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String languageName;
        String languageCode;
        if (data != null) {
            while (data.moveToNext()) {
                languageName = data.getString(data
                        .getColumnIndex(TranslationContract.LanguageEntry.COLUMN_LANGUAGE_NAME));
                languageCode = data.getString(data
                        .getColumnIndex(TranslationContract.LanguageEntry.COLUMN_LANGUAGE_KEY));
                mLanguageAdapter.insert(0, new Language(languageName, languageCode));
            }
            mLanguageAdapter.notifyDataSetChanged();
            updateEmptyView();
        }
        mLoadingSpinner.setVisibility(GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    private void updateEmptyView() {
        if (mLanguageAdapter.getItemCount() == 0) {
            if (Utility.isNetworkAvailable(this))
                mErrorMessage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onItemClick(Language language) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(LANGUAGE, language);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
