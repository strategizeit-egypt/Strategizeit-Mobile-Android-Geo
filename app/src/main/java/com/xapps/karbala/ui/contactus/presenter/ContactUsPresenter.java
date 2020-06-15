package com.xapps.karbala.ui.contactus.presenter;

import android.annotation.SuppressLint;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.ui.contactus.view.ContactUSView;
import com.xapps.karbala.utils.KarbalaUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactUsPresenter {
    ContactUSView contactUSView;

    public ContactUsPresenter(ContactUSView contactUSView) {
        this.contactUSView = contactUSView;
    }

    @SuppressLint("CheckResult")
    public void contactUs(String subject , String description) {
        DataManager.getInstance().contactUs(subject,description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    contactUSView.onShowLoader();
                })
                .doOnError(throwable -> {
                            KarbalaUtils.showError(contactUSView, throwable);
                            contactUSView.onDismissLoader();
                        }
                )
                .doOnComplete(() -> {
                    contactUSView.onDismissLoader();
                })
                .subscribe(contactUsDTOObjectModel -> {
                    contactUSView.onDismissLoader();
                    contactUSView.onContactUsResult(contactUsDTOObjectModel);
                }, throwable -> {
                });
    }
}
