package com.example.kimichael.yandextranslate.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.example.kimichael.yandextranslate.R;

import timber.log.Timber;

/**
 * Created by Kim Michael on 22.04.17
 * Helper button, that helps us bookmark a translation
 */
public class BookmarkButton extends ImageButton {

    private static final int[] STATE_MARKED = {R.attr.state_marked};

    private boolean mIsMarked = false;

    public BookmarkButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isMarked() {
        return mIsMarked;
    }

    public void setMarked(boolean isMarked) {
        mIsMarked = isMarked;
        refreshDrawableState();
    }

    public void switchMarked() {
        setMarked(!mIsMarked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (mIsMarked) {
            mergeDrawableStates(drawableState, STATE_MARKED);
        }
        return drawableState;
    }
}
