package com.example.kimichael.yandextranslate.sections.history;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kimichael.yandextranslate.R;

import timber.log.Timber;

/**
 * Created by Kim Michael on 21.04.17
 */
public class HistoryPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private Context context;

    public HistoryPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Timber.d("History Fragment created");
                return HistoryFragment.newInstance();
            case 1:
                Timber.d("Bookmarks fragment created");
                return BookmarksFragment.newInstance();
            default:
                throw new UnsupportedOperationException("Maybe page count is invalid");
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.history_tab_name);
            case 1:
                return context.getString(R.string.bookmarks_tab_name);
            default:
                throw new UnsupportedOperationException("Such page doesn't exist");
        }
    }
}
