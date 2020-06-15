package com.xapps.karbala.ui.verify.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.home.view.HomeActivity;
import com.xapps.karbala.ui.verify.presenter.VerificationPresenter;
import com.xapps.karbala.utils.KarbalaUtils;
import com.xapps.karbala.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationActivity extends BaseActivity implements VerificationView {
    private VerificationPresenter verificationPresenter = new VerificationPresenter(this);

    @BindView(R.id.first_num)
    EditText mVerifyFirstNumberET;
    @BindView(R.id.second_num)
    EditText mVerifySecondNumberET;
    @BindView(R.id.third_num)
    EditText mVerifyThirdNumberET;
    @BindView(R.id.forth_num)
    EditText mVerifyForthNumberET;
    @BindView(R.id.verify_iv)
    ImageView mVerifyIV;
    @BindView(R.id.resend_tv)
    TextView mResendTV;
    @BindView(R.id.code_timer)
    TextView mCodeTimerTV;

    private List<EditText> mVerifyCodeList;
    private String mVerificationCode = "";
    private boolean isBack;
    private String validChars = "0123456789";
    private String phoneNumber;

    private InputFilter filter = (source, start, end, dest, dstart, dend) -> {

        if (source != null && validChars.contains(("" + source))) {
            return null;
        }
        return "";

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {

        phoneNumber = getIntent().getStringExtra(Constants.PHONE_NUMBER);

        mVerifyCodeList = new ArrayList<>();
        mVerifyCodeList.add(mVerifyFirstNumberET);
        mVerifyCodeList.add(mVerifySecondNumberET);
        mVerifyCodeList.add(mVerifyThirdNumberET);
        mVerifyCodeList.add(mVerifyForthNumberET);
        mVerifyFirstNumberET.setFilters(new InputFilter[]{filter});
        mVerifySecondNumberET.setFilters(new InputFilter[]{filter});
        mVerifyThirdNumberET.setFilters(new InputFilter[]{filter});
        mVerifyForthNumberET.setFilters(new InputFilter[]{filter});

        downCountTimer();
        addTextWatchers();
    }

    private void addTextWatchers() {
        for (int index = 0; index < mVerifyCodeList.size(); index++) {
            final int index2 = index;
            mVerifyCodeList.get(index2).setOnKeyListener((v, keyCode, event) -> {

                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return false;
                if (event.getAction() == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    try {
                        switch (v.getId()) {
                            case R.id.forth_num:
                                mVerifyThirdNumberET.requestFocus();
                                try {
                                    mVerifyThirdNumberET.setSelection(mVerifyThirdNumberET.getText().length());
                                } catch (Exception e) {
                                }
                                break;
                            case R.id.third_num:
                                mVerifySecondNumberET.requestFocus();
                                try {
                                    mVerifySecondNumberET.setSelection(mVerifySecondNumberET.getText().length());
                                } catch (Exception e) {
                                }
                                break;
                            case R.id.second_num:
                                mVerifyFirstNumberET.requestFocus();
                                try {
                                    mVerifyFirstNumberET.setSelection(mVerifyFirstNumberET.getText().length());
                                } catch (Exception e) {
                                }
                                break;

                        }
                    } catch (Exception e) {
                    }
                    return false;
                } else {
                    switch (v.getId()) {
                        case R.id.first_num:
                            if (mVerifyFirstNumberET.getText().toString().length() == 1) {
                                //mVerifyFirstNumberET.setText(event.getCharacters());
                            }
                            break;
                        case R.id.second_num:
                            if (mVerifySecondNumberET.getText().toString().length() == 1) {
                                //mVerifySecondNumberET.setText(event.getCharacters());
                            }
                            break;
                        case R.id.third_num:
                            if (mVerifyThirdNumberET.getText().toString().length() == 1) {
                                //mVerifyThirdNumberET.setText(event.getCharacters());
                            }
                            break;
                        case R.id.forth_num:
                            if (mVerifyForthNumberET.getText().toString().length() == 1) {
                                //mVerifyForthNumberET.setText(event.getCharacters());
                            }
                            break;

                    }
                }
                return false;

            });
            mVerifyCodeList.get(index2).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() == 1 && index2 < 3) {
                        mVerifyCodeList.get(index2 + 1).requestFocus();
                        try {
                            mVerifyCodeList.get(index2 + 1).setSelection(mVerifyCodeList.get(index2 + 1).getText().length());

                        } catch (Exception e) {
                        }

                        // to hide keyboard after finished
                    } else if (index2 == 3 && s.toString().length() == 1) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    }


                }
            });
        }


    }

    public void downCountTimer() {
        isBack = false;
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                try {
                    mCodeTimerTV.setText((millisUntilFinished / 1000) + " " + getResources().getString(R.string.second));
                    mResendTV.setClickable(false);
                } catch (Exception e) {

                }

            }

            public void onFinish() {
                isBack = true;
                mResendTV.setClickable(true);
            }

        }.start();
    }

    @OnClick(R.id.resend_tv)
    public void resendClick(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isBack)
            super.onBackPressed();
    }

    @OnClick(R.id.verify_iv)
    public void verifyBtnClick(View view) {
        mVerificationCode = mVerifyFirstNumberET.getText().toString() + mVerifySecondNumberET.getText().toString()
                + mVerifyThirdNumberET.getText().toString() + mVerifyForthNumberET.getText().toString();
        if (mVerificationCode.length() != 4){
            KarbalaUtils.showToast(VerificationActivity.this, getResources().getString(R.string.no_code), Constants.FANCYERROR);
            return;
        }
        verificationPresenter.verify(phoneNumber, mVerificationCode);

    }

    @Override
    public void onVerifyResult(ObjectModel<LoginDTO> loginDTOObjectModel) {
        verificationPresenter.saveUserData(loginDTOObjectModel.getModel());
        goToHome();
    }

    private void goToHome() {
        Intent intent = new Intent(VerificationActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onFinished() {

    }

    @Override
    public void onDismissLoader() {
        KarbalaUtils.hideLoader();
    }

    @Override
    public void onShowLoader() {
        KarbalaUtils.showLoader(this, getString(R.string.loading));
    }

    @Override
    public void onTimeOut() {
        KarbalaUtils.showTimeOutMessage(this);
    }

    @Override
    public void onError(int code) {
        if (code == 6){
            KarbalaUtils.showToast(this,getString(R.string.invalid_verification_code), Constants.FANCYERROR);
        }
    }
}
