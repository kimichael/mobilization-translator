package com.example.kimichael.yandextranslate.sections.history;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.adapters.BookmarksAdapter;
import com.example.kimichael.yandextranslate.adapters.HistoryAdapter;
import com.example.kimichael.yandextranslate.data.provider.TranslationContract;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static android.view.View.GONE;

public class BookmarksFragment extends HistoryFragment {

    private static final int HISTORY_LOADER = 0;
    private static final String[] HISTORY_COLUMNS = new String[] {
            TranslationContract.WordEntry.COLUMN_SRC_WORD,
            TranslationContract.WordEntry.COLUMN_DEST_WORD,
            TranslationContract.WordEntry.COLUMN_BOOKMARK,
            TranslationContract.WordEntry.COLUMN_SRC_LANG,
            TranslationContract.WordEntry.COLUMN_DEST_LANG
    };

    public BookmarksFragment() {
        // Required empty public constructor
    }

    public static BookmarksFragment newInstance() {
        BookmarksFragment fragment = new BookmarksFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        // Set up recycler view
        RecyclerView historyList = (RecyclerView) rootView.findViewById(R.id.history_list);
        mHistoryRecords = new ArrayList<>();
        mHistoryAdapter = new BookmarksAdapter(mHistoryRecords, this, historyList, mPresenter);
        historyList.setAdapter(mHistoryAdapter);
        // Layout items as a list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        historyList.setLayoutManager(linearLayoutManager);
        // Set line as divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(historyList.getContext(),
                linearLayoutManager.getOrientation());
        historyList.addItemDecoration(dividerItemDecoration);

        // Load languages to recyclerView
        getLoaderManager().initLoader(HISTORY_LOADER, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mLoadingSpinner.setVisibility(View.VISIBLE);
        mNoHistoryBlock.setVisibility(GONE);
        String sortOrder = TranslationContract.WordEntry._ID + " ASC";
        Uri languagesUri = TranslationContract.WordEntry.CONTENT_URI;
        String selection = TranslationContract.WordEntry.TABLE_NAME + "." +
                TranslationContract.WordEntry.COLUMN_BOOKMARK + " = ?";
        String[] selectionArgs = new String[] {"1"};
        return new android.support.v4.content.CursorLoader(getContext(),
                languagesUri,
                HISTORY_COLUMNS,
                selection,
                selectionArgs,
                sortOrder
        );
    }
}
