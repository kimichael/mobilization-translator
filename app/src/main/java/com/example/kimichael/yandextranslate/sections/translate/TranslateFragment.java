package com.example.kimichael.yandextranslate.sections.translate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.kimichael.yandextranslate.ComponentProvider;
import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.activity.SelectLanguageActivity;
import com.example.kimichael.yandextranslate.components.ActivityComponent;
import com.example.kimichael.yandextranslate.data.TranslationQueryHandler;
import com.example.kimichael.yandextranslate.data.TranslationRepository;
import com.example.kimichael.yandextranslate.data.objects.DictionaryTranslation;
import com.example.kimichael.yandextranslate.data.objects.Language;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class TranslateFragment extends Fragment implements TranslateContract.View {

    @BindView(R.id.text_input_box) RelativeLayout mTextBox;
    @BindView(R.id.translated_word_edit_text) EditText mInputEditText;
    @BindView(R.id.clear_text_button) ImageButton mClearButton;
    @BindView(R.id.spinner) ProgressBar mLoadingSpinner;
    ToolbarViewHolder mToolbarViewHolder;
    // A special field of ButterKnife to nullify all binded views
    private Unbinder unbinder;
    private ActivityComponent mActivityComponent;
    private SharedPreferences mSharedPreferences;

    TranslateContract.UserActionsListener mPresenter;
    @Inject TranslationRepository mTranslationRepository;

    public TranslateFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivityComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootview = inflater.inflate(R.layout.fragment_translate, container, false);
        // Set chooser for translation direction on toolbar
        View toolbarView = inflater.inflate(R.layout.actionbar_translation_chooser, null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(toolbarView);
        mToolbarViewHolder = new ToolbarViewHolder(toolbarView);

        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        mPresenter = new TranslatePresenter(this, mTranslationRepository,
                new Language(
                mSharedPreferences.getString(getString(R.string.pref_src_language),
                        getString(R.string.default_src_language)),
                mSharedPreferences.getString(getString(R.string.pref_src_language_code),
                        getString(R.string.default_src_language_code))),
                new Language(
                mSharedPreferences.getString(getString(R.string.pref_dest_language),
                        getString(R.string.default_dest_language)),
                mSharedPreferences.getString(getString(R.string.pref_dest_language_code),
                        getString(R.string.default_dest_language_code))), getLoaderManager(), new TranslationQueryHandler(getContext(), null));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // Show clear button when user starts typing
    @OnTextChanged(value = R.id.translated_word_edit_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable s) {
        if (s.length() < 1)
            mClearButton.setVisibility(GONE);
        else
            mClearButton.setVisibility(VISIBLE);

    }

    // Clear input text on clicking clear button
    @OnClick(R.id.clear_text_button)
    public void onClearButtonClick() {
        clearInput(true);
    }
    @Override
    public void clearInput(boolean showKeyboard) {
        mInputEditText.setText(null);
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

    // Set languages at the toolbar (chosen translation direction)
    @Override
    public void setLanguages(String srcLanguageName, String destLanguageName) {
        mToolbarViewHolder.srcLanguageButton.setText(srcLanguageName);
        mToolbarViewHolder.destLanguageButton.setText(destLanguageName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Language selectedLanguage = data.getParcelableExtra(SelectLanguageActivity.LANGUAGE);
            switch (requestCode) {
                case SelectLanguageActivity.PICK_SRC_LANGUAGE_REQUEST:
                    mPresenter.setSrcLanguage(selectedLanguage);
                    break;
                case SelectLanguageActivity.PICK_DEST_LANGUAGE_REQUEST:
                    mPresenter.setDestLanguage(selectedLanguage);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setProgressSpinner(boolean active) {
        mLoadingSpinner.setVisibility(active ? VISIBLE : GONE);
    }

    @Override
    public void showTranslation(DictionaryTranslation dictionaryTranslation){
    }

    @Override
    public String getRequestedWord() {
        return mInputEditText.getText().toString();
    }

    // Section with toolbar ui elements
    protected class ToolbarViewHolder {
        @BindView(R.id.src_lang_button) Button srcLanguageButton;
        @BindView(R.id.dest_lang_button) Button destLanguageButton;
        @BindView(R.id.swap_lang_button) ImageButton swapLanguageButton;

        ToolbarViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.src_lang_button)
        public void changeSrcLanguage() {
            Intent intent = new Intent(getContext(), SelectLanguageActivity.class);
            startActivityForResult(intent, SelectLanguageActivity.PICK_SRC_LANGUAGE_REQUEST);
        }
        @OnClick(R.id.dest_lang_button)
        public void changeDestLanguage() {
            Intent intent = new Intent(getContext(), SelectLanguageActivity.class);
            startActivityForResult(intent, SelectLanguageActivity.PICK_DEST_LANGUAGE_REQUEST);
        }
        @OnClick(R.id.swap_lang_button)
        public void swapLanguages() {
            mPresenter.swapLanguages();
        }

    }
}
