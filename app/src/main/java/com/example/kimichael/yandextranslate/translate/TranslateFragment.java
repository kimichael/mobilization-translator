package com.example.kimichael.yandextranslate.translate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.kimichael.yandextranslate.ComponentProvider;
import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.components.ActivityComponent;
import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class TranslateFragment extends Fragment implements TranslateContract.View {

    @BindView(R.id.text_input_box) RelativeLayout mTextBox;
    @BindView(R.id.translated_word_edit_text) EditText mInputEditText;
    @BindView(R.id.clear_text_button) ImageButton mClearButton;
    @BindView(R.id.spinner) ProgressBar mLoadingSpinner;
    // A special field of ButterKnife to nullify all binded views
    private Unbinder unbinder;

    private ActivityComponent mActivityComponent;
    TranslateContract.UserActionsListener mPresenter;
    @Inject
    TranslationRepository mTranslationSource;

    public TranslateFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent.inject(this);
        mPresenter = new TranslatePresenter(this, mTranslationSource);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_translate, container, false);
        unbinder = ButterKnife.bind(this, rootview);
        clearInput(false);
        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityComponent = ((ComponentProvider) context).provideComponent();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // Show clear button when user starts typing
    @OnTextChanged(value = R.id.translated_word_edit_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable s) {
        if (s.length() < 1)
            mClearButton.setVisibility(GONE);
        else
            mClearButton.setVisibility(VISIBLE);

    }

    @OnClick(R.id.clear_text_button)
    public void onClearButtonClick() {
        clearInput(true);
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

    // Change border line when edit text changes focus
    @OnFocusChange(R.id.translated_word_edit_text)
    public void changeFocus(View v, boolean hasFocus) {
        int drawable = hasFocus ? R.drawable.text_edit_border_focused
                : R.drawable.text_edit_border_unfocused;
        mTextBox.setBackground(ContextCompat.getDrawable(getContext(), drawable));
        if (!hasFocus)
            mPresenter.loadTranslation();
    }

    @Override
    public void setProgressSpinner(boolean active) {
        mLoadingSpinner.setVisibility(active ? VISIBLE : GONE);
    }

    @Override
    public void showTranslation(DictionaryTranslation dictionaryTranslation) {

    }

    @Override
    public String getRequestedWord() {
        return mInputEditText.getText().toString();
    }
}
