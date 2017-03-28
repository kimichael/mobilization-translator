package com.example.kimichael.yandextranslate.translate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.kimichael.yandextranslate.Injector;
import com.example.kimichael.yandextranslate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class TranslateFragment extends Fragment implements TranslateContract.View {

    @BindView(R.id.text_input_box) RelativeLayout mTextBox;
    @BindView(R.id.translated_word_edit_text) EditText mInputEditText;
    @BindView(R.id.clear_text_button) ImageButton mClearButton;
    @BindView(R.id.spinner) ProgressBar mLoadingSpinner;

    TranslateContract.UserActionsListener mPresenter;

    public TranslateFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TranslatePresenter(this, Injector.provideTranslationsRepository());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_translate, container, false);
        ButterKnife.bind(this, rootview);

        // Change border line when edit text changes focus
        mInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int drawable = hasFocus ? R.drawable.text_edit_border_focused
                        : R.drawable.text_edit_border_unfocused;
                mTextBox.setBackground(ContextCompat.getDrawable(getContext(), drawable));
            }
        });

        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1)
                    mClearButton.setVisibility(GONE);
                else
                    mClearButton.setVisibility(VISIBLE);
            }
        });

        // Clear edit text on clear button click
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput(true);
            }
        });

        clearInput(false);
        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void clearInput(boolean showKeyboard) {
        mInputEditText.setText("");
        mClearButton.setVisibility(View.INVISIBLE);
        if (!mInputEditText.hasFocus() && showKeyboard)
            // Set focus
            if (mInputEditText.requestFocus())
                // Set soft keyboard
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void setProgressSpinner(boolean active) {
        mLoadingSpinner.setVisibility(active ? VISIBLE : GONE);
    }
}
