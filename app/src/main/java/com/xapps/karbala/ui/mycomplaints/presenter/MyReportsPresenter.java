package com.xapps.karbala.ui.mycomplaints.presenter;

import android.annotation.SuppressLint;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.ui.mycomplaints.view.MyReportsView;
import com.xapps.karbala.utils.KarbalaUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MyReportsPresenter {
    MyReportsView myReportsView;

    public MyReportsPresenter(MyReportsView myReportsView) {
        this.myReportsView = myReportsView;
    }
   /* @SuppressLint("CheckResult")
    public void getAllReports(int page, boolean sorting){
        DataManager.getInstance().getAllReports(page, sorting)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    myReportsView.onShowLoader();
                })
                .doOnError(throwable -> {
                            AmanaKSAUtils.showError(myReportsView, throwable);
                            myReportsView.onDismissLoader();
                        }
                )
                .doOnComplete(() -> {
                    myReportsView.onDismissLoader();
                })
                .subscribe(reportDTOListModel -> {
                    myReportsView.onDismissLoader();
                    myReportsView.onAddReportDetailsResult(reportDTOListModel);
                }, throwable -> {
                });
    }*/

    @SuppressLint("CheckResult")
    public void searchInReports(String searchWord, int page, boolean sorting) {
        DataManager.getInstance().searchInReports(searchWord, page, sorting)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    myReportsView.onShowLoader();
                })
                .doOnError(throwable -> {
                            KarbalaUtils.showError(myReportsView, throwable);
                            myReportsView.onDismissLoader();
                        }
                )
                .doOnComplete(() -> {
                    myReportsView.onDismissLoader();
                })
                .subscribe(reportDTOListModel -> {
                    myReportsView.onDismissLoader();
                    myReportsView.onAddReportDetailsResult(reportDTOListModel);
                }, throwable -> {
                });
    }
}
