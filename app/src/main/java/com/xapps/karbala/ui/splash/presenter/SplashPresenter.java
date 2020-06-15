package com.xapps.karbala.ui.splash.presenter;

import android.annotation.SuppressLint;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.MetaDataDTO;
import com.xapps.karbala.model.data.source.preferences.SharedManager;
import com.xapps.karbala.ui.splash.view.SplashView;
import com.xapps.karbala.utils.KarbalaUtils;
import com.xapps.karbala.utils.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashPresenter {
    SplashView splashView;

    public SplashPresenter(SplashView splashView) {
        this.splashView = splashView;
    }

    public String getCashedLanguage() {
        return SharedManager.newInstance().getCashValue(Constants.LANG);
    }

    public void setLanguage(String language) {
        SharedManager.newInstance().CashValue(Constants.LANG, language);
    }

    public void getMetaData() {
        DataManager.getInstance().getMetaData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                })
                .doOnError(throwable ->
                        KarbalaUtils.showError(splashView, throwable))
                .doOnComplete(() -> {
                })
                .subscribe(metaDataDTOObjectModel -> {
                    splashView.onMetaDataResult(metaDataDTOObjectModel);
                }, throwable -> {
                });
    }

    public void saveMetaData(MetaDataDTO metaDataDTO) {
        DataManager.getInstance().saveMetaData(metaDataDTO);
    }

    @SuppressLint("CheckResult")
    public void getProfile() {
        DataManager.getInstance().getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                })
                .doOnError(throwable ->
                        KarbalaUtils.showError(splashView, throwable))
                .doOnComplete(() -> {
                })
                .subscribe(profileDTOObjectModel -> {
                    splashView.onProfileResult(profileDTOObjectModel);
                }, throwable -> {
                });
    }

    public void saveUserData(LoginDTO loginDTO) {
        DataManager.getInstance().saveUserData(loginDTO);
    }
}
