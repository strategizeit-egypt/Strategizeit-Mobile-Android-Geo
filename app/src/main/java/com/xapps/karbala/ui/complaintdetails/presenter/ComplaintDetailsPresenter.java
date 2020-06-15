package com.xapps.karbala.ui.complaintdetails.presenter;

import android.annotation.SuppressLint;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.ui.complaintdetails.view.ComplaintDetailsView;
import com.xapps.karbala.utils.KarbalaUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ComplaintDetailsPresenter {

    ComplaintDetailsView complaintDetailsView;

    public ComplaintDetailsPresenter(ComplaintDetailsView complaintDetailsView) {
        this.complaintDetailsView = complaintDetailsView;
    }

    @SuppressLint("CheckResult")
    public void getReportDetails(long reportId){
        DataManager.getInstance().getReportDetails(reportId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    complaintDetailsView.onShowLoader();
                })
                .doOnError(throwable -> {
                            KarbalaUtils.showError(complaintDetailsView, throwable);
                            complaintDetailsView.onDismissLoader();
                        }
                )
                .doOnComplete(() -> {
                    complaintDetailsView.onDismissLoader();
                })
                .subscribe( reportDTOObjectModel-> {
                    complaintDetailsView.onDismissLoader();
                    complaintDetailsView.onGetComplaintDetailsResult(reportDTOObjectModel);
                }, throwable -> {
                });
    }
}
