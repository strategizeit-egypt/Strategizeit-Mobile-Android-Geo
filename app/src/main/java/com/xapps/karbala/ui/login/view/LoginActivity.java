package com.xapps.karbala.ui.login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.hbb20.CountryCodePicker;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.login.presenter.LoginPresenter;
import com.xapps.karbala.ui.register.view.RegisterActivity;
import com.xapps.karbala.ui.termsandconditions.view.TermsConditionsActivity;
import com.xapps.karbala.ui.verify.view.VerificationActivity;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {
    private LoginPresenter loginPresenter = new LoginPresenter(this);

    @BindView(R.id.term_cond_cb)
    CheckBox mTermsConditionsCB;
    @BindView(R.id.phone_et)
    EditText mPhoneET;
    @BindView(R.id.spinner_country_code)
    CountryCodePicker spinnerCountryCode;
    private String internationalPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.terms_conditions_container)
    public void TermsAndConditionsClick(View view) {
        if (mTermsConditionsCB.isChecked())
            mTermsConditionsCB.setChecked(false);
        else {
            Intent intent = new Intent(LoginActivity.this, TermsConditionsActivity.class);
            intent.putExtra(Constants.HIDE_AGREE_TERMS, false);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(intent, Constants.TERMS_CONDITIONS_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.TERMS_CONDITIONS_REQUEST_CODE) {
            mTermsConditionsCB.setChecked(data.getBooleanExtra(Constants.AGREE, false));
        }
    }

    @OnClick(R.id.btn_new_account)
    public void onNewAccountClickListener(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }


    @OnClick(R.id.login_btn)
    public void onLoginClickListener(View view) {
        login();
    }

    private void login() {
        if (TextUtils.isEmpty(mPhoneET.getText().toString())) {
            KarbalaUtils.showToast(this, getString(R.string.phone_empty_error_message), Constants.FANCYERROR);
            return;
        }
        // testing account (+96601064860055) wrong but this is tested account
        String countryCode = spinnerCountryCode.getSelectedCountryCode();
        internationalPhoneNumber = KarbalaUtils.validatePhoneNumber(countryCode, mPhoneET.getText().toString());
        if (internationalPhoneNumber.contentEquals("")) {
            KarbalaUtils.showToast(this, getString(R.string.phone_wrong_error_message), Constants.FANCYERROR);
            return;
        }
        if (!mTermsConditionsCB.isChecked()) {
            goToTermsAndConditions();
            return;
        }
        loginPresenter.login(internationalPhoneNumber.replace(" ", "").trim());

        //addreCAPTHCA();
    }

    private void addreCAPTHCA() {

        SafetyNet.getClient(this).verifyWithRecaptcha(Constants.CAPTCHA_KEY)

                .addOnSuccessListener(recaptchaTokenResponse -> {
                    // Indicates communication with reCAPTCHA service was
                    // successful.
                    String userResponseToken = recaptchaTokenResponse.getTokenResult();
                    if (!userResponseToken.isEmpty()) {
                        // Validate the user response token using the
                        // reCAPTCHA siteverify API.
                        loginPresenter.login(internationalPhoneNumber.replace(" ", "").trim());
                        Log.e("Register Activity", "Success");
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof ApiException) {
                        // An error occurred when communicating with the
                        // reCAPTCHA service. Refer to the status code to
                        // handle the error appropriately.
                        ApiException apiException = (ApiException) e;
                        int statusCode = apiException.getStatusCode();
                        Log.e("RegisterActivity", "Error: " + CommonStatusCodes.getStatusCodeString(statusCode));

                    } else {
                        // A different, unknown type of error occurred.
                        Log.e("RegisterActivity", "Error: " + e.getMessage());
                    }
                });
    }

    public void goToTermsAndConditions() {
        Intent intent = new Intent(LoginActivity.this, TermsConditionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, Constants.TERMS_CONDITIONS_REQUEST_CODE);
    }

    @Override
    public void onLoginResult(ObjectModel<LoginDTO> loginDTOObjectModel) {
        if (loginDTOObjectModel.getModel() != null) {
            Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
            intent.putExtra(Constants.PHONE_NUMBER, loginDTOObjectModel.getModel().getPhoneNumber());
            startActivity(intent);
        }
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
        if (code == 31) {
            KarbalaUtils.showToast(this, getString(R.string.user_not_registered), Constants.FANCYERROR);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    intent.putExtra(Constants.PHONE_NUMBER,mPhoneET.getText().toString());
                    startActivity(intent);
                }
            },500);
            return;
        }
        KarbalaUtils.showErrorResult(this);
    }


}
