package com.xapps.karbala.ui.mycomplaints.presenter;

import android.annotation.SuppressLint;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.ui.mycomplaints.view.MyComplaintsView;
import com.xapps.karbala.utils.KarbalaUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MyReportsPresenter {
    MyComplaintsView myComplaintsView;

    public MyReportsPresenter(MyComplaintsView myComplaintsView) {
        this.myComplaintsView = myComplaintsView;
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
                    myComplaintsView.onShowLoader();
                })
                .doOnError(throwable -> {
                            KarbalaUtils.showError(myComplaintsView, throwable);
                            myComplaintsView.onDismissLoader();
                        }
                )
                .doOnComplete(() -> {
                    myComplaintsView.onDismissLoader();
                })
                .subscribe(reportDTOListModel -> {
                    myComplaintsView.onDismissLoader();
                    myComplaintsView.onAddReportDetailsResult(reportDTOListModel);
                }, throwable -> {
                });
    }
}
