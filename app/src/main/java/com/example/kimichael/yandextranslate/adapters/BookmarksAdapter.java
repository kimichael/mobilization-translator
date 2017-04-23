package com.example.kimichael.yandextranslate.adapters;

import android.content.Context;

import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;

import java.util.List;

/**
 * Created by Kim Michael on 23.04.17
 */
public class BookmarksAdapter extends HistoryAdapter {

    public BookmarksAdapter(List<HistoryRecord> items, OnHistoryRecordItemClickListener listener, Context context) {
        super(items, listener, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryRecord record = mItems.get(position);
        holder.srcWord.setText(record.getTranslation().getSrcWord());
        holder.destWord.setText(record.getTranslation().getTranslatedWord());
        holder.languageDirection.setText(record.getLanguageDirection().toString());
        holder.bookmarkButton.setMarked(record.getTranslation().isMarked());
        holder.bookmarkButton.setOnClickListener(v -> {
            HistoryRecord historyRecord = mItems.get(holder.getAdapterPosition());
            historyRecord.getTranslation().switchMarked();
            mListener.onBookmarkButtonClick(historyRecord);
            if (!historyRecord.getTranslation().isMarked())
                remove(historyRecord);
            else {
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }
}
