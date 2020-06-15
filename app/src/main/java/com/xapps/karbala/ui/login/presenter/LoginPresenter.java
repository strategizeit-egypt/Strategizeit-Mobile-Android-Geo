package com.xapps.karbala.ui.login.presenter;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.ui.login.view.LoginView;
import com.xapps.karbala.utils.KarbalaUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter {
    LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    public void login(String phoneNumber) {
        DataManager.getInstance().login(phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    loginView.onShowLoader();
                })
                .doOnError(throwable -> {
                    loginView.onDismissLoader();
                    KarbalaUtils.showError(loginView, throwable);
                })
                .doOnComplete(() -> {
                    loginView.onDismissLoader();

                })
                .subscribe(loginDTOObjectModel -> {
                    loginView.onDismissLoader();

                    loginView.onLoginResult(loginDTOObjectModel);

                }, throwable -> {
                });
    }

}
