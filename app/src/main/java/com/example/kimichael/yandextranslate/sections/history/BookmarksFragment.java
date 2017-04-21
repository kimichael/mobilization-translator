package com.example.kimichael.yandextranslate.sections.history;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kimichael.yandextranslate.R;

public class BookmarksFragment extends Fragment {

    public BookmarksFragment() {
        // Required empty public constructor
    }

    public static BookmarksFragment newInstance() {
        BookmarksFragment fragment = new BookmarksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }
}
