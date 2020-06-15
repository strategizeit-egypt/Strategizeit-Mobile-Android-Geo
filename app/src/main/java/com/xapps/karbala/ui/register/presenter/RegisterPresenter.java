package com.xapps.karbala.ui.register.presenter;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.model.data.dto.MetaDataDTO;
import com.xapps.karbala.ui.register.view.RegisterView;
import com.xapps.karbala.utils.KarbalaUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterPresenter {
    RegisterView registerView;

    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
    }

    public MetaDataDTO getMetaData() {
        return DataManager.getInstance().getSavedMetaData();
    }

    public void citizenRegister(String phoneNumber, String fullName, String email, int citizenTypeId,
                                String city, String neighborhood, String dateOfBirth, int genderID,
                                long municipalityId, long countryId) {
        DataManager.getInstance().citizenRegister(phoneNumber, fullName, email, citizenTypeId, city, neighborhood,
                dateOfBirth, genderID, municipalityId, countryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    registerView.onShowLoader();
                })
                .doOnError(throwable -> {
                    registerView.onDismissLoader();
                    KarbalaUtils.showError(registerView, throwable);
                })
                .doOnComplete(() -> {
                })
                .subscribe(loginDTOObjectModel -> {
                    registerView.onDismissLoader();
                    registerView.onRegisterResult(loginDTOObjectModel);
                }, throwable -> {
                });
    }

    public void visitorRegister(String phoneNumber, String fullName, int citizenTypeId, String dateOfBirth,
                                int genderId, long countryId,long governorateId) {
        DataManager.getInstance().visitorRegister(phoneNumber, fullName, citizenTypeId, dateOfBirth, genderId, countryId,governorateId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    registerView.onShowLoader();
                })
                .doOnError(throwable -> {
                    registerView.onDismissLoader();
                    KarbalaUtils.showError(registerView, throwable);
                })
                .doOnComplete(() -> {
                    registerView.onDismissLoader();
                })
                .subscribe(loginDTOObjectModel -> {
                    registerView.onRegisterResult(loginDTOObjectModel);

                }, throwable -> {
                });
    }
}
