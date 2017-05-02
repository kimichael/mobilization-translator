package com.example.kimichael.yandextranslate.adapters;

import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.buttons.BookmarkButton;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.sections.translate.TranslateContract;

import java.util.List;

/**
 * Created by Kim Michael on 23.04.17
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    RecyclerView mRecyclerView;
    List<HistoryRecord> mItems;
    OnHistoryRecordItemClickListener mListener;
    TranslateContract.UserActionsListener mPresenter;
    int mExpandedPosition = -1;

    public void update(int i, HistoryRecord historyRecord) {
        if (mItems.contains(historyRecord)) {
            mItems.get(mItems.indexOf(historyRecord)).getTranslation().setIsMarked(historyRecord.getTranslation().isMarked());
            notifyItemChanged(mItems.indexOf(historyRecord));
        } else {
            insert(i, historyRecord);
        }
    }

    public interface OnHistoryRecordItemClickListener {
        void onBookmarkButtonClick(HistoryRecord historyRecord);
    }

    public HistoryAdapter(List<HistoryRecord> items,
                          OnHistoryRecordItemClickListener listener,
                          RecyclerView recyclerView,
                          TranslateContract.UserActionsListener presenter) {
        this.mItems = items;
        this.mListener = listener;
        this.mRecyclerView = recyclerView;
        this.mPresenter = presenter;
    }

    public void insert(int position, HistoryRecord historyRecord) {
        mItems.add(position, historyRecord);
        notifyItemInserted(position);
    }

    public void remove(HistoryRecord historyRecord) {
        int position = mItems.indexOf(historyRecord);
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_record, parent, false);
        HistoryAdapter.ViewHolder holder = new HistoryAdapter.ViewHolder(v);
        holder.bookmarkButton.setOnClickListener(view -> {
            HistoryRecord historyRecord = mItems.get(holder.getAdapterPosition());
            historyRecord.getTranslation().switchMarked();
            mListener.onBookmarkButtonClick(historyRecord);
            notifyItemChanged(holder.getAdapterPosition());
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final HistoryAdapter.ViewHolder holder, int position) {
        HistoryRecord record = mItems.get(position);
        holder.srcWord.setText(record.getTranslation().getSrcWord());
        holder.destWord.setText(record.getTranslation().getTranslatedWord());
        holder.languageDirection.setText(record.getLanguageDirection().toString());
        holder.bookmarkButton.setMarked(record.getTranslation().isMarked());

//        final boolean isExpanded = position == mExpandedPosition;
//        holder.detailedTranslation.setVisibility(isExpanded?View.VISIBLE:View.GONE);
//        holder.itemView.setActivated(isExpanded);
//        holder.itemView.setOnClickListener(v -> {
//            HistoryAdapter.this.mExpandedPosition = isExpanded ? -1 : position;
//            TransitionManager.beginDelayedTransition(mRecyclerView);
//            notifyDataSetChanged();
//        });

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView srcWord;
        TextView destWord;
        TextView languageDirection;
        BookmarkButton bookmarkButton;
        TextView detailedTranslation;

        ViewHolder(View itemView) {
            super(itemView);
            srcWord = (TextView) itemView.findViewById(R.id.src_word);
            destWord = (TextView) itemView.findViewById(R.id.dest_word);
            languageDirection = (TextView) itemView.findViewById(R.id.language_direction);
            bookmarkButton = (BookmarkButton) itemView.findViewById(R.id.bookmark_button);
            detailedTranslation = (TextView) itemView.findViewById(R.id.detailed_translation);
        }

    }
}
