package com.xapps.karbala.ui.changelanguage.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xapps.karbala.R;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.changelanguage.presenter.ChangeLanguagePresenter;
import com.xapps.karbala.ui.home.view.HomeActivity;
import com.xapps.karbala.utils.LocalHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeLanguageActivity extends BaseActivity  {
    private ChangeLanguagePresenter changeLanguagePresenter = new ChangeLanguagePresenter();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_shape)
    ImageView mToolbarShape;
    @BindView(R.id.english_language_rb)
    RadioButton mEnglishLanguageRB;
    @BindView(R.id.arabic_language_rb)
    RadioButton mArabicLanguageRB;

    private String mSelectedLanguage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {
        toolbarTitle.setText(getString(R.string.change_language));
        mToolbarShape.setImageResource(R.drawable.ic_shape_cosmic_latte_bg);


        mSelectedLanguage = LocalHelper.getLocal().getLanguage();

        if (!mSelectedLanguage.contentEquals("")) {
            if (mSelectedLanguage.contentEquals("en")) {
                mEnglishLanguageRB.setChecked(true);
            } else {
                mArabicLanguageRB.setChecked(true);
            }
        }


        mEnglishLanguageRB.setOnClickListener(v -> checkEnglishLanguage());

        mArabicLanguageRB.setOnClickListener(v -> checkArabicLanguage());


    }

    @OnClick(R.id.ic_back)
    public void onBackClickListener(View view) {
        finish();
    }

    @OnClick(R.id.english_language_container)
    public void onEnglishLanguageLayoutClickListener(View view) {
        checkEnglishLanguage();
    }


    @OnClick(R.id.arabic_language_container)
    public void onArabicLanguageLayoutClickListener(View view) {
        checkArabicLanguage();
    }


    public void checkEnglishLanguage() {
        mArabicLanguageRB.setChecked(false);
        mEnglishLanguageRB.setChecked(true);
        mSelectedLanguage = "en";

    }

    public void checkArabicLanguage() {
        mArabicLanguageRB.setChecked(true);
        mEnglishLanguageRB.setChecked(false);
        mSelectedLanguage = "ar";

    }

    @OnClick(R.id.change_language_tv)
    public void onChangeLanguageClickListener(View view) {
        if (!LocalHelper.getLocal().getLanguage().contentEquals(mSelectedLanguage)) {
           changeLanguagePresenter .setLanguage(mSelectedLanguage);
            LocalHelper.setLocale(ChangeLanguageActivity.this, mSelectedLanguage);
            HomeActivity.isRequiredRefresh = true;
            finish();
            startActivity(getIntent());
        }else {
            finish();
        }

    }

}
