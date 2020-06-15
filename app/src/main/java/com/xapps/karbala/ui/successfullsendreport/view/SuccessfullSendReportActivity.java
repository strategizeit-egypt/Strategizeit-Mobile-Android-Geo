package com.xapps.karbala.ui.successfullsendreport.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xapps.karbala.R;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.home.view.HomeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuccessfullSendReportActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull_send_report);
        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {

    }

    @OnClick(R.id.text_home_screen)
    public void onBackToHomeScreenClickListener() {
        HomeActivity.selectedFragmentId = 0;
        goToHome();
    }

    @OnClick(R.id.img_open_reports)
    public void onOpenReportsClickListener(View view) {
        HomeActivity.selectedFragmentId = 1;
        goToHome();
    }

    private void goToHome() {
        Intent intent = new Intent(SuccessfullSendReportActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        onBackToHomeScreenClickListener();
    }
}
