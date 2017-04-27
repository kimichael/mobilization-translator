package com.example.kimichael.yandextranslate.sections.history;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.kimichael.yandextranslate.ComponentProvider;
import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.adapters.HistoryAdapter;
import com.example.kimichael.yandextranslate.components.ActivityComponent;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.LanguageDirection;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.data.provider.TranslationContract;
import com.example.kimichael.yandextranslate.sections.translate.TranslateContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

import static android.view.View.GONE;

/**
 * Created by Kim Michael on 21.04.17
 */
public class HistoryFragment extends Fragment implements HistoryAdapter.OnHistoryRecordItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int HISTORY_LOADER = 0;
    private static final String[] HISTORY_COLUMNS = new String[] {
            TranslationContract.WordEntry.COLUMN_SRC_WORD,
            TranslationContract.WordEntry.COLUMN_DEST_WORD,
            TranslationContract.WordEntry.COLUMN_BOOKMARK,
            TranslationContract.WordEntry.COLUMN_SRC_LANG,
            TranslationContract.WordEntry.COLUMN_DEST_LANG
    };

    @BindView(R.id.spinner) ProgressBar mLoadingSpinner;
    @BindView(R.id.no_history_block) LinearLayout mNoHistoryBlock;
    Unbinder unbinder;
    List<HistoryRecord> mHistoryRecords;
    HistoryAdapter mHistoryAdapter;

    ActivityComponent mActivityComponent;
    @Inject TranslateContract.UserActionsListener mPresenter;

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = ((ComponentProvider) getActivity().getApplication()).provideComponent();
        mActivityComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        mHistoryRecords = new ArrayList<>();
        mHistoryAdapter = new HistoryAdapter(mHistoryRecords, this, getContext());

        RecyclerView historyList = (RecyclerView) rootView.findViewById(R.id.history_list);
        historyList.setAdapter(mHistoryAdapter);
        // Layout items as a list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        historyList.setLayoutManager(linearLayoutManager);
        // Set line as divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(historyList.getContext(),
                linearLayoutManager.getOrientation());
        historyList.addItemDecoration(dividerItemDecoration);
        unbinder = ButterKnife.bind(this, rootView);
        // Load languages to recyclerView
        getLoaderManager().initLoader(HISTORY_LOADER, null, this);

        updateEmptyView();
        return rootView;
    }

    @Override
    public void onBookmarkButtonClick(HistoryRecord historyRecord) {
        mPresenter.bookmarkTranslation(historyRecord);
        if (mPresenter.getCachedTranslation().equals(historyRecord.getTranslation())){
            mPresenter.getCachedTranslation().setIsMarked(historyRecord.getTranslation().isMarked());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.d("Loader created");
        mLoadingSpinner.setVisibility(View.VISIBLE);
        mNoHistoryBlock.setVisibility(GONE);
        String sortOrder = TranslationContract.WordEntry._ID + " ASC";
        Uri languagesUri = TranslationContract.WordEntry.CONTENT_URI;
        return new android.support.v4.content.CursorLoader(getContext(),
                languagesUri,
                HISTORY_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        HistoryRecord historyRecord;
        Translation translation;
        LanguageDirection languageDirection;
        if (data != null) {
            while (data.moveToNext()) {
                translation = new Translation(
                        data.getString(data.getColumnIndex(TranslationContract.WordEntry.COLUMN_SRC_WORD)),
                        data.getString(data.getColumnIndex(TranslationContract.WordEntry.COLUMN_DEST_WORD)));
                translation.setIsMarked(data.getInt(data.getColumnIndex(TranslationContract.WordEntry.COLUMN_BOOKMARK)) > 0);
                languageDirection = new LanguageDirection(
                        data.getString(data.getColumnIndex(TranslationContract.WordEntry.COLUMN_SRC_LANG)),
                        data.getString(data.getColumnIndex(TranslationContract.WordEntry.COLUMN_DEST_LANG))
                );
                historyRecord = new HistoryRecord(translation, languageDirection);
                mHistoryAdapter.update(0, historyRecord);
            }
        }
        if (data.getCount() == 0)
            mHistoryAdapter.removeAll();
        updateEmptyView();
        mLoadingSpinner.setVisibility(GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    private void updateEmptyView() {
        if (mHistoryAdapter.getItemCount() == 0) {
            mNoHistoryBlock.setVisibility(View.VISIBLE);
        } else {
            mNoHistoryBlock.setVisibility(GONE);
        }
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroyView();
    }
}
