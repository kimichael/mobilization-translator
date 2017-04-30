package com.example.kimichael.yandextranslate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;

import java.util.List;

/**
 * Created by Kim Michael on 23.04.17
 */
public class BookmarksAdapter extends HistoryAdapter {

    public BookmarksAdapter(List<HistoryRecord> items, OnHistoryRecordItemClickListener listener) {
        super(items, listener);
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
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
        return holder;
    }


}
