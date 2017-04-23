package com.example.kimichael.yandextranslate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.data.objects.Language;

import java.util.Collections;
import java.util.List;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private List<Language> mItems = Collections.EMPTY_LIST;
    private OnLanguageItemClickListener mListener;

    public interface OnLanguageItemClickListener {
        void onItemClick(Language language);
    }

    public LanguageAdapter(List<Language> items, OnLanguageItemClickListener listener, Context context) {
        this.mItems = items;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.languageName.setText(mItems.get(position).getName());
        holder.itemView.setOnClickListener(v ->
                mListener.onItemClick(mItems.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void insert(int position, Language language) {
        mItems.add(position, language);
        notifyItemInserted(position);
    }

    public void remove(Language data) {
        int position = mItems.indexOf(data);
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView languageName;
        ViewHolder(View itemView) {
            super(itemView);
            languageName = (TextView) itemView.findViewById(R.id.language_name);
        }
    }
}
