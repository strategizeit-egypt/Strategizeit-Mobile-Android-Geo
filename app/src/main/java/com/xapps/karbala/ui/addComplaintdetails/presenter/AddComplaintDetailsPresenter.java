package com.xapps.karbala.ui.addComplaintdetails.presenter;

import android.annotation.SuppressLint;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.ui.addComplaintdetails.view.AddComplaintDetailsView;
import com.xapps.karbala.utils.KarbalaUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddComplaintDetailsPresenter {
    AddComplaintDetailsView addComplaintDetailsView;

    public AddComplaintDetailsPresenter(AddComplaintDetailsView addComplaintDetailsView) {
        this.addComplaintDetailsView = addComplaintDetailsView;
    }

    @SuppressLint("CheckResult")
    public void addReport(RequestBody title, RequestBody details,
                          RequestBody reportTypeId, RequestBody townshipId,
                          RequestBody latitude, RequestBody longitude,
                          RequestBody duration, RequestBody videoImage, List<MultipartBody.Part> files) {
        DataManager.getInstance().addReport(title, details, reportTypeId, townshipId, latitude, longitude, duration, videoImage, files)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    addComplaintDetailsView.onShowLoader();
                })
                .doOnError(throwable -> {
                            KarbalaUtils.showError(addComplaintDetailsView, throwable);
                            addComplaintDetailsView.onDismissLoader();
                        }
                )
                .doOnComplete(() -> {
                    addComplaintDetailsView.onDismissLoader();
                })
                .subscribe(reportDTOObjectModel -> {
                    addComplaintDetailsView.onDismissLoader();
                    addComplaintDetailsView.onAddReportDetailsResult(reportDTOObjectModel);
                }, throwable -> {
                });
    }
}
