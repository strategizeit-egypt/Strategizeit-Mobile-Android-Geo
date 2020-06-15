package com.xapps.karbala.ui.termsandconditions.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xapps.karbala.R;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.utils.KarbalaUtils;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.LocalHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsConditionsActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.agree_terms_conditions)
    ImageView agreeTermsConditions;
    @BindView(R.id.text_terms_conditions)
    TextView mTextTermsConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        toolbarTitle.setText(getString(R.string.terms_policy_title));
        if (getIntent().getBooleanExtra(Constants.HIDE_AGREE_TERMS, false))
            agreeTermsConditions.setVisibility(View.INVISIBLE);
        else
            agreeTermsConditions.setVisibility(View.VISIBLE);

        try {
            mTextTermsConditions.setText(KarbalaUtils.extractSettingsByKey(LocalHelper.isArabic() ? Constants.TERMS_KEY_AR : Constants.TERMS_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.agree_terms_conditions)
    public void onAgreeTermsAndConditionsClickListener(View view) {
        Intent intent = getIntent();
        intent.putExtra(Constants.AGREE, true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.ic_back)
    public void onBackClickListener(View view) {
        onBackPressed();
    }

}
