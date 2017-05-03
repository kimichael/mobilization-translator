package com.example.kimichael.yandextranslate.adapters;

import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.sections.history.HistoryFragment;
import com.example.kimichael.yandextranslate.sections.translate.TranslateContract;

import java.util.List;

/**
 * Created by Kim Michael on 23.04.17
 */
public class BookmarksAdapter extends HistoryAdapter {

    public BookmarksAdapter(List<HistoryRecord> items,
                            OnHistoryRecordItemClickListener listener,
                            RecyclerView recyclerView,
                            TranslateContract.UserActionsListener presenter,
                            HistoryFragment.FragmentSwitcher fragmentSwitcher) {
        super(items, listener, recyclerView, presenter, fragmentSwitcher);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryRecord record = mItems.get(position);
        holder.srcWord.setText(record.getTranslation().getSrcWord());
        holder.destWord.setText(record.getTranslation().getTranslatedWord());
        holder.languageDirection.setText(record.getLanguageDirection().toString());
        holder.bookmarkButton.setMarked(record.getTranslation().isMarked());

        holder.itemView.setOnClickListener(v -> {
            fragmentSwitcher.translate(record);
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_record, parent, false);
        HistoryAdapter.ViewHolder holder = new HistoryAdapter.ViewHolder(v);
        holder.bookmarkButton.setOnClickListener(view -> {
            int position = holder.getAdapterPosition();
            if (position != -1) {
                HistoryRecord historyRecord = mItems.get(position);
                historyRecord.getTranslation().switchMarked();
                mListener.onBookmarkButtonClick(historyRecord);
                if (!historyRecord.getTranslation().isMarked())
                    remove(historyRecord);
                else {
                    notifyItemChanged(position);
                }
            }
        });

        return holder;
    }
}
