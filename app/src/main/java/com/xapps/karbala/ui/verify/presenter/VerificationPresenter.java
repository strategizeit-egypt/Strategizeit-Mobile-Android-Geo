package com.xapps.karbala.ui.verify.presenter;

import android.annotation.SuppressLint;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.ui.verify.view.VerificationView;
import com.xapps.karbala.utils.KarbalaUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VerificationPresenter {
    VerificationView verificationView;

    public VerificationPresenter(VerificationView verificationView) {
        this.verificationView = verificationView;
    }

    @SuppressLint("CheckResult")
    public void verify(String phoneNumber, String vCode) {
        DataManager.getInstance().verify(phoneNumber, vCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    verificationView.onShowLoader();
                })
                .doOnError(throwable -> {
                            KarbalaUtils.showError(verificationView, throwable);
                            verificationView.onDismissLoader();
                        }
                )
                .doOnComplete(() -> {
                    verificationView.onDismissLoader();
                })
                .subscribe(loginDTOObjectModel -> {
                    verificationView.onDismissLoader();
                    verificationView.onVerifyResult(loginDTOObjectModel);
                }, throwable -> {
                });
    }
    public void saveUserData(LoginDTO loginDTO) {
        DataManager.getInstance().saveUserData(loginDTO);
        DataManager.getInstance().refreshToken();
    }
}
