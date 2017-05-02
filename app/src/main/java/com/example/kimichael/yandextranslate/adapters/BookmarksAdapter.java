package com.example.kimichael.yandextranslate.adapters;

import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.sections.translate.TranslateContract;

import java.util.List;

/**
 * Created by Kim Michael on 23.04.17
 */
public class BookmarksAdapter extends HistoryAdapter {

    public BookmarksAdapter(List<HistoryRecord> items,
                            OnHistoryRecordItemClickListener listener,
                            RecyclerView recyclerView,
                            TranslateContract.UserActionsListener presenter) {
        super(items, listener, recyclerView, presenter);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryRecord record = mItems.get(position);
        holder.srcWord.setText(record.getTranslation().getSrcWord());
        holder.destWord.setText(record.getTranslation().getTranslatedWord());
        holder.languageDirection.setText(record.getLanguageDirection().toString());
        holder.bookmarkButton.setMarked(record.getTranslation().isMarked());

//        final boolean isExpanded = position == mExpandedPosition;
//        holder.detailedTranslation.setVisibility(isExpanded? View.VISIBLE:View.GONE);
//        holder.itemView.setActivated(isExpanded);
//        holder.itemView.setOnClickListener(v -> {
//            mExpandedPosition = isExpanded ? -1:position;
//            TransitionManager.beginDelayedTransition(mRecyclerView);
//            notifyDataSetChanged();
//        });
    }
}
