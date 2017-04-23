package com.example.kimichael.yandextranslate.sections.history;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.kimichael.yandextranslate.ComponentProvider;
import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.components.ActivityComponent;
import com.example.kimichael.yandextranslate.sections.translate.TranslateContract;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by Kim Michael on 31.03.17.
 * Container for history and bookmarks fragments
 */
public class StorageFragment extends Fragment {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    Unbinder unbinder;
    @Inject
    TranslateContract.UserActionsListener mPresenter;
    ActivityComponent mActivityComponent;


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
        mActivityComponent = ((ComponentProvider) getActivity().getApplication()).provideComponent();
        mActivityComponent.inject(this);
        unbinder = ButterKnife.bind(this, rootView);
        ToolbarViewHolder toolbarViewHolder = new ToolbarViewHolder(toolbarView);
        mViewPager.setAdapter(new HistoryPagerAdapter(getChildFragmentManager(), getContext()));
        return rootView;
    }

    // Section with toolbar ui elements
    protected class ToolbarViewHolder {
        @BindView(R.id.sliding_tabs)
        TabLayout mTabLayout;
        @BindView(R.id.clear_history_button)
        ImageButton mClearHistoryButton;

        ToolbarViewHolder(View view) {
            Timber.d("ViewHolder created");
            ButterKnife.bind(this, view);
            mTabLayout.setupWithViewPager(mViewPager);
        }

        @OnClick(R.id.clear_history_button)
        public void clearHistory() {
            mPresenter.clearHistory();
        }
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy");
        super.onDestroy();
    }
}
