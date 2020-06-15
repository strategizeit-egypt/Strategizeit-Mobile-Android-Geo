package com.xapps.karbala.ui.townships.presenter;

import android.annotation.SuppressLint;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.ui.townships.view.TownshipsView;
import com.xapps.karbala.utils.KarbalaUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TownshipsPresenter {
    TownshipsView townshipsView;

    public TownshipsPresenter(TownshipsView townshipsView) {
        this.townshipsView = townshipsView;
    }

    @SuppressLint("CheckResult")
    public void getTownships() {
        DataManager.getInstance().getTownships()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    townshipsView.onShowLoader();
                })
                .doOnError(throwable -> {
                            KarbalaUtils.showError(townshipsView, throwable);
                            townshipsView.onDismissLoader();
                        }
                )
                .doOnComplete(() -> {
                    townshipsView.onDismissLoader();
                })
                .subscribe(townshipDTOListModel -> {
                    townshipsView.onDismissLoader();
                    townshipsView.onGetTownshipsResult(townshipDTOListModel);
                }, throwable -> {
                });
    }
}
