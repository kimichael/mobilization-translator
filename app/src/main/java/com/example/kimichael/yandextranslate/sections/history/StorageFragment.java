package com.example.kimichael.yandextranslate.sections.history;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kimichael.yandextranslate.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class StorageFragment extends Fragment {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    Unbinder unbinder;


    public StorageFragment() {
    }


    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("StorageFragment created");
        View rootView = inflater.inflate(R.layout.fragment_storage, container, false);
        View toolbarView = inflater.inflate(R.layout.actionbar_tabs, null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(toolbarView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        unbinder = ButterKnife.bind(this, rootView);
        ToolbarViewHolder toolbarViewHolder = new ToolbarViewHolder(toolbarView);
        mViewPager.setAdapter(new HistoryPagerAdapter(getChildFragmentManager(), getContext()));
        return rootView;
    }

    // Section with toolbar ui elements
    protected class ToolbarViewHolder {
        @BindView(R.id.sliding_tabs)
        TabLayout mTabLayout;

        ToolbarViewHolder(View view) {
            Timber.d("ViewHolder created");
            ButterKnife.bind(this, view);
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy");
        super.onDestroy();
    }
}
