package com.xapps.karbala.ui.aboutus.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xapps.karbala.R;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.utils.KarbalaUtils;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.LocalHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.text_about_us)
    TextView mTextAboutUs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {
        toolbarTitle.setText(getString(R.string.about_us));
        try {
            mTextAboutUs.setText(KarbalaUtils.extractSettingsByKey(LocalHelper.isArabic() ? Constants.ABOUT_US_KEY_AR : Constants.ABOUT_US_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ic_back)
    public void onBackClickListener(View view) {
        onBackPressed();
    }
}
