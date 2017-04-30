package com.example.kimichael.yandextranslate.sections.translate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kimichael.yandextranslate.ComponentProvider;
import com.example.kimichael.yandextranslate.R;
import com.example.kimichael.yandextranslate.activity.SelectLanguageActivity;
import com.example.kimichael.yandextranslate.buttons.BookmarkButton;
import com.example.kimichael.yandextranslate.components.ActivityComponent;
import com.example.kimichael.yandextranslate.data.objects.HistoryRecord;
import com.example.kimichael.yandextranslate.data.objects.Language;
import com.example.kimichael.yandextranslate.data.objects.Translation;
import com.example.kimichael.yandextranslate.util.EspressoIdlingResource;
import com.example.kimichael.yandextranslate.util.Utility;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Kim Michael on 31.03.17.
 */
public class TranslateFragment extends Fragment implements TranslateContract.View {

    private final String LOG_TAG = TranslateFragment.class.getSimpleName();

    @BindView(R.id.text_input_box) RelativeLayout mTextBox;
    @BindView(R.id.translated_word_edit_text) EditText mInputEditText;
    @BindView(R.id.clear_text_button) ImageButton mClearButton;
    @BindView(R.id.spinner) ProgressBar mLoadingSpinner;
    @BindView(R.id.main_translation) TextView mMainTranslation;
    @BindView(R.id.expanded_translation) TextView mExpandedTranslation;
    @BindView(R.id.yandex_message) TextView mYandexMessage;
    @BindView(R.id.no_internet_message_block) LinearLayout mNoInternetBlock;
    @BindView(R.id.retry_button) Button mRetryButton;
    @BindView(R.id.translation_zone) LinearLayout mTranslationZone;
    @BindView(R.id.bookmark_button) BookmarkButton mBookmarkButton;
    ToolbarViewHolder mToolbarViewHolder;
    // A special field of ButterKnife to nullify all binded views
    private Unbinder unbinder;
    private ActivityComponent mActivityComponent;
    private SharedPreferences mSharedPreferences;
    private Translation shownTranslation;
    // Timer for delaying translating typed text
    private Timer mTimer = new Timer();

    private boolean mAttached;

    @Override
    public void setInput(String input) {
        mInputEditText.setText(input);
    }

    @Inject TranslateContract.UserActionsListener mPresenter;

    // Used for testing, will be null in prod
    @Nullable EspressoIdlingResource mIdlingResource;

    public TranslateFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivityComponent = ((ComponentProvider) getActivity().getApplication()).provideComponent();
        mActivityComponent.inject(this);
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        mAttached = false;
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

        unbinder = ButterKnife.bind(this, rootview);
        // Make editText multiline and with done action button
        mInputEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mInputEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mYandexMessage.setMovementMethod(LinkMovementMethod.getInstance());
        clearInput(false);
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        mAttached = true;
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
        mInputEditText.setText(mSharedPreferences.getString(getString(R.string.key_input_text), ""));
        commitTranslateAction();
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

    @OnClick(R.id.bookmark_button)
    public void addToBookmarks() {
        boolean isMarked = !mBookmarkButton.isMarked();
        mBookmarkButton.setMarked(isMarked);
        shownTranslation.setIsMarked(isMarked);
        mPresenter.bookmarkTranslation(new HistoryRecord(shownTranslation, null));
    }

    // Show clear button when user starts typing
    @OnTextChanged(value = R.id.translated_word_edit_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable s) {

        if (s.length() < 1) {
            mClearButton.setVisibility(GONE);
            clearTranslation();
        } else {
            mClearButton.setVisibility(VISIBLE);
            if (mAttached) {
                // Set up timer, so we don't translate immediately after typing
                mTimer.cancel();
                mTimer = new Timer();
                long DELAY = 400;
                mTimer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (getActivity() != null)
                                    getActivity().runOnUiThread(() -> mPresenter.startLoadingTranslation());
                            }
                        }, DELAY);
            }
        }
    }

//    @OnTextChanged(R.id.translated_word_edit_text)
//    public void onTextChanged(Editable s) {
//        clearTranslation();
//        if (!s.toString().equals(""))
//            mPresenter.startLoadingTranslation();
//    }

    @OnClick(R.id.retry_button)
    public void retryConnection() {
        mNoInternetBlock.setVisibility(GONE);
        commitTranslateAction();
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
        clearTranslation();
        if (!mInputEditText.hasFocus() && showKeyboard)
            // Set focus
            if (mInputEditText.requestFocus())
                // Set soft keyboard
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void clearTranslation() {
        Timber.d("Translation cleared");
        mMainTranslation.setText("");
        mExpandedTranslation.setText("");
        mYandexMessage.setText("");
        mTranslationZone.setVisibility(View.GONE);
        mBookmarkButton.setVisibility(View.GONE);
        mBookmarkButton.setMarked(false);
        mLoadingSpinner.setVisibility(GONE);
    }

    @Override
    public void showTranslation(Translation translation) {
        if (mInputEditText.getText().length() < 1)
            return;
        shownTranslation = translation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mMainTranslation.setText(Html.fromHtml(getString(R.string.format_main_translation, translation.getTranslatedWord()), Html.FROM_HTML_MODE_COMPACT));
        } else {
            mMainTranslation.setText(Html.fromHtml(getString(R.string.format_main_translation, translation.getTranslatedWord())));
        }
        if (translation.isFull()) {
            mExpandedTranslation.setText(translation.toHtml(getContext()));
            mYandexMessage.setText(getString(R.string.yandex_dictionary_message));
            Linkify.addLinks(mYandexMessage, Linkify.WEB_URLS);
        } else {
            mExpandedTranslation.setText("");
            mYandexMessage.setText(getString(R.string.yandex_translate_message));
        }
        mTranslationZone.setVisibility(View.VISIBLE);
        mBookmarkButton.setVisibility(View.VISIBLE);
        mBookmarkButton.setMarked(translation.isMarked());
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
            return true;
        }
        return false;
    }

    // Start translating when EditText loses focus
    @OnFocusChange(R.id.translated_word_edit_text)
    public void onEditTextFocusChange(boolean hasFocus) {
        if (!hasFocus) {
            mPresenter.saveTranslationToHistory(shownTranslation);
        }
    }

    // Start translating the text in EditText
    private void commitTranslateAction() {
        Timber.d("Commit translate action");
        // If anything is written in the editText box
        if (mInputEditText.getText().length() > 0) {
            // Start translating
            mPresenter.startLoadingTranslation();
        }
    }

    @Override
    public void onPause() {
        mPresenter.saveState(mSharedPreferences, getContext());
        mPresenter.onDetachView();
        mAttached = false;
        super.onPause();
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
            mPresenter.clearCache();
            clearTranslation();
            commitTranslateAction();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setProgressSpinner(boolean active) {
        if (mLoadingSpinner != null)
            mLoadingSpinner.setVisibility(active ? VISIBLE : GONE);
    }

    @Override
    public void updateEmptyView() {
        if (!Utility.isNetworkAvailable(getContext())){
            mNoInternetBlock.setVisibility(VISIBLE);
        } else {
            if (mMainTranslation != null)
                clearTranslation();
        }
    }

    @Override
    public String getRequestedText() {
        return mInputEditText.getText().toString().toLowerCase();
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

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
