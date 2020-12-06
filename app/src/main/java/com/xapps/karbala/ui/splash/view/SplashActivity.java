package com.xapps.karbala.ui.splash.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.android.gms.maps.model.LatLng;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.MetaDataDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.home.view.HomeActivity;
import com.xapps.karbala.ui.login.view.LoginActivity;
import com.xapps.karbala.ui.splash.presenter.SplashPresenter;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;
import com.xapps.karbala.utils.LocalHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class SplashActivity extends BaseActivity implements SplashView {

    private SplashPresenter splashPresenter = new SplashPresenter(this);

    @BindView(R.id.english_language_rb)
    RadioButton englishLanguageRB;
    @BindView(R.id.language_rg)
    RadioGroup languageRG;
    private String mSavedLanguage;

    private double TIME_TO_WAIT = 0;
    private double t;
    private int readMetaData = Constants.NOREADED;
    private int userSignedUp = Constants.NOREADED;
    private List<LatLng> latLngs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {


        /*createKarbalaPolygon();
        if (PolyUtil.containsLocation(new LatLng(32.73647762,44.24121314),latLngs,true)) {
            KarbalaUtils.showToast(this,"Done",Constants.FANCYERROR);

        } else {
            KarbalaUtils.showToast(this,getString(R.string.err_out_of_karbala_area),Constants.FANCYERROR);
        }
*/

        TIME_TO_WAIT = System.currentTimeMillis();
        initLoader();

        mSavedLanguage = splashPresenter.getCashedLanguage();
        if (mSavedLanguage == null) {
            LocalHelper.setLocale(SplashActivity.this, "ar");
            languageRG.setVisibility(View.VISIBLE);
        } else {
            languageRG.setVisibility(View.GONE);
            splashPresenter.getMetaData();
        }


        languageRG.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.english_language_rb) {
                splashPresenter.setLanguage("en");
                LocalHelper.setLocale(this, "en");
            } else {
                splashPresenter.setLanguage("ar");
                LocalHelper.setLocale(this, "ar");
            }
            splashPresenter.getMetaData();


        });
    }

    public void createKarbalaPolygon() {
        latLngs = new ArrayList<>();
        try {
            InputStream file = getAssets().open("KarbalaPoints.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] strings = line.split(",");
                LatLng latLng = new LatLng(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                latLngs.add(latLng);
            }

            br.close();
            file.close();    //closes the stream and release the resources


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initLoader() {
        try {
            KarbalaUtils.InitLoader(this);

        } catch (Exception e) {
        }

    }

    @Override
    public void onMetaDataResult(ObjectModel<MetaDataDTO> metaDataDTOObjectModel) {
        if (metaDataDTOObjectModel.getModel() != null) {
            readMetaData = Constants.READINGSUCCESS;
            splashPresenter.saveMetaData(metaDataDTOObjectModel.getModel());
            initiateUserData();
        } else {
            readMetaData = Constants.READINGFAIL;
        }
    }

    private void initiateUserData() {
        KarbalaUtils.InitCurrentLanguage(this);
        t = System.currentTimeMillis();

        try {
            splashPresenter.getProfile();

        } catch (Exception e) {
            calculateTimeToOpenLogin();

        }
    }

    public void calculateTimeToOpenLogin() {
        if (t - TIME_TO_WAIT > 2000) {
            goToLogin();
        } else {

            new Handler().postDelayed(() ->
            {
                goToLogin();
            }, 2000 - (long) (t - TIME_TO_WAIT));
        }
    }

    public void calculateTimeToOpenHome() {
        if (t - TIME_TO_WAIT > 2000) {
            goToHome();
        } else {

            new Handler().postDelayed(() ->
            {
                goToHome();
            }, 2000 - (long) (t - TIME_TO_WAIT));
        }
    }

    @Override
    public void onProfileResult(ObjectModel<LoginDTO> profileDTOObjectModel) {
        if (profileDTOObjectModel.getModel() != null) {
            userSignedUp = Constants.READINGSUCCESS;
            splashPresenter.saveUserData(profileDTOObjectModel.getModel());
            calculateTimeToOpenHome();
        } else {
            userSignedUp = Constants.READINGFAIL;
            calculateTimeToOpenLogin();
            return;
        }
    }

    public void goToLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    private void goToHome() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onDismissLoader() {

    }

    @Override
    public void onShowLoader() {

    }

    @Override
    public void onTimeOut() {
        showErrorMessage();
    }

    @Override
    public void onError(int code) {
        try {
            if (readMetaData == Constants.READINGFAIL && userSignedUp == Constants.NOREADED)
                userSignedUp = Constants.READINGFAIL;

            if (readMetaData == Constants.NOREADED)
                readMetaData = Constants.READINGFAIL;

            if (readMetaData == Constants.READINGSUCCESS)
                userSignedUp = Constants.READINGFAIL;

            if (userSignedUp == Constants.READINGFAIL) {
                if (!this.isFinishing() && !NetworkUtils.isConnected()) {
                    showErrorMessage();
                } else {
                    calculateTimeToOpenLogin();
                }
            } else {
                showErrorMessage();
            }

        } catch (Exception e) {
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        userSignedUp = Constants.NOREADED;
        readMetaData = Constants.NOREADED;
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText(getString(R.string.error))
                .setContentText(getString(R.string.try_again))
                .setCancelText(getString(R.string.exit))
                .setConfirmText(getString(R.string.reconnect))
                .setCancelClickListener(sweetAlertDialog12 -> {
                    SplashActivity.this.finish();
                    sweetAlertDialog12.cancel();
                }).setConfirmClickListener(sweetAlertDialog1 -> {
            splashPresenter.getMetaData();
            sweetAlertDialog1.dismissWithAnimation();
        }).show();

        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.round_solid_blue_radius8));
    }

}
