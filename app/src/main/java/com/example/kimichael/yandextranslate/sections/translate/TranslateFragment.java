package com.example.kimichael.yandextranslate.sections.translate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kimichael.yandextranslate.ComponentProvider;
import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.activity.SelectLanguageActivity;
import com.example.kimichael.yandextranslate.components.ActivityComponent;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.Translation;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class TranslateFragment extends Fragment implements TranslateContract.View {

    private final String LOG_TAG = TranslateFragment.class.getSimpleName();

    @BindView(R.id.text_input_box) RelativeLayout mTextBox;
    @BindView(R.id.translated_word_edit_text) EditText mInputEditText;
    @BindView(R.id.clear_text_button) ImageButton mClearButton;
    @BindView(R.id.spinner) ProgressBar mLoadingSpinner;
    @BindView(R.id.main_translation) TextView mMainTranslation;
    @BindView(R.id.expanded_translation) TextView mExpandedTranslation;
    ToolbarViewHolder mToolbarViewHolder;
    // A special field of ButterKnife to nullify all binded views
    private Unbinder unbinder;
    private ActivityComponent mActivityComponent;
    private SharedPreferences mSharedPreferences;

    @Inject TranslateContract.UserActionsListener mPresenter;

    public TranslateFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivityComponent = ((ComponentProvider) getActivity().getApplication()).provideComponent();
        mActivityComponent.inject(this);
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mToolbarViewHolder = new ToolbarViewHolder(toolbarView);

        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        unbinder = ButterKnife.bind(this, rootview);

        // Make editText multiline and with done action button
        mInputEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mInputEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        clearInput(false);
        mPresenter.onAttachView(this,
                new Language(
                        mSharedPreferences.getString(getString(R.string.pref_src_language),
                                getString(R.string.default_src_language)),
                        mSharedPreferences.getString(getString(R.string.pref_src_language_code),
                                getString(R.string.default_src_language_code))),
                new Language(
                        mSharedPreferences.getString(getString(R.string.pref_dest_language),
                                getString(R.string.default_dest_language)),
                        mSharedPreferences.getString(getString(R.string.pref_dest_language_code),
                                getString(R.string.default_dest_language_code))));
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void clearTranslation() {

    }

    // Show clear button when user starts typing
    @OnTextChanged(value = R.id.translated_word_edit_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable s) {
        if (s.length() < 1)
            mClearButton.setVisibility(GONE);
        else
            mClearButton.setVisibility(VISIBLE);

    }

    // Clear input text on clicking clear button and let edit text get focus
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

    // Start translating when user presses done button
    @OnEditorAction(R.id.translated_word_edit_text)
    public boolean onDoneActionButtonPressed(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // Hide keyboard
            InputMethodManager inputManager = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(0, 0);
            // And make editText lose focus
            mInputEditText.clearFocus();

            commitTranslateAction();
            return true;
        }
        return false;
    }

    // Start translating when EditText loses focus
    @OnFocusChange(R.id.translated_word_edit_text)
    public void onEditTextFocusChange(boolean hasFocus) {
        if (!hasFocus) {
            commitTranslateAction();
        }
    }

    // Start translating the text in EditText
    private void commitTranslateAction() {
        Log.v(LOG_TAG, "Started loading");
        // If anything is written in the editText box
        if (mInputEditText.getText().length() > 0)
            // Start translating
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
    public void showTranslation(Translation translation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mMainTranslation.setText(Html.fromHtml(getString(R.string.format_main_translation, translation.getTranslatedWord()), Html.FROM_HTML_MODE_COMPACT));
        } else {
            mMainTranslation.setText(Html.fromHtml(getString(R.string.format_main_translation, translation.getTranslatedWord())));
        }
        if (translation.isFull()) {
            mExpandedTranslation.setText(translation.toHtml(getContext()));
        }
    }

    @Override
    public void updateEmptyView() {

    }

    @Override
    public String getRequestedText() {
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
