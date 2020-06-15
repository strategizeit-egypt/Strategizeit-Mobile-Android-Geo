package com.xapps.karbala.ui.more.presenter;

import android.annotation.SuppressLint;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.source.preferences.SharedManager;
import com.xapps.karbala.ui.more.view.MoreView;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MorePresenter {
    MoreView moreView;

    public MorePresenter(MoreView moreView) {
        this.moreView = moreView;
    }

    public LoginDTO getUserData(){
        return DataManager.getInstance().getuserData();
    }

    public void saveUserData(LoginDTO loginDTO) {
        DataManager.getInstance().saveUserData(loginDTO);
    }

    @SuppressLint("CheckResult")
    public void editFullName(String fullName){
        DataManager.getInstance().editFullName(fullName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    moreView.onShowLoader();
                })
                .doOnError(throwable -> {
                            KarbalaUtils.showError(moreView, throwable);
                            moreView.onDismissLoader();
                        }
                )
                .doOnComplete(() -> {
                    moreView.onDismissLoader();
                })
                .subscribe(loginDTOObjectModel -> {
                    moreView.onDismissLoader();
                    moreView.onEditFullNameResult(loginDTOObjectModel);
                }, throwable -> {
                });
    }

    @SuppressLint("CheckResult")
    public void logout() {
        DataManager.getInstance().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> moreView.onShowLoader())
                .doOnError(throwable -> KarbalaUtils.showError(moreView,throwable))
                .doOnComplete(() -> moreView.onDismissLoader())
                .subscribe(result ->
                        {
                            moreView.onDismissLoader();
                            moreView.onLogout(result);
                        }
                        , throwable -> {});
    }
    public void logoutLocally(){
        SharedManager.newInstance().saveObject(null,Constants.TOKEN);
    }
}
