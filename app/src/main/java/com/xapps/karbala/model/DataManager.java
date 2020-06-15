package com.xapps.karbala.model;

import com.xapps.karbala.App;
import com.xapps.karbala.model.data.dto.ComplaintDTO;
import com.xapps.karbala.model.data.dto.ContactUsDTO;
import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.MetaDataDTO;
import com.xapps.karbala.model.data.dto.TownshipDTO;
import com.xapps.karbala.model.data.dto.generic.ListModel;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.model.data.source.database.DatabaseHelper;
import com.xapps.karbala.model.data.source.network.ApiHelper;
import com.xapps.karbala.model.data.source.network.KarbalaApiService;
import com.xapps.karbala.model.data.source.preferences.SharedManager;
import com.xapps.karbala.utils.Constants;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DataManager {
    private static DataManager mInstance = null;
    private KarbalaApiService karbalaApiService;
    private SharedManager sharedManager;
    private DatabaseHelper databaseHelper;


    private DataManager() {
        karbalaApiService = ApiHelper.getRetrofit();
        sharedManager = SharedManager.newInstance();
        databaseHelper = DatabaseHelper.getDatabaseHelper(App.mContext);


    }

    public static DataManager getInstance() {
        if (mInstance == null) {
            synchronized (DataManager.class) {

                mInstance = new DataManager();

            }
        } else if (mInstance.karbalaApiService == null || mInstance.sharedManager == null) {
            mInstance = new DataManager();
        }

        return mInstance;
    }

    public void refreshToken() {
        karbalaApiService = ApiHelper.getRetrofit();
    }

    public Observable<ObjectModel<MetaDataDTO>> getMetaData() {
        return karbalaApiService.getMetaData();
    }

    public void saveMetaData(MetaDataDTO metaDataDTO) {
        sharedManager.saveObject(metaDataDTO, Constants.META_DATA);
    }

    public MetaDataDTO getSavedMetaData() {
        return sharedManager.getObject(Constants.META_DATA, MetaDataDTO.class);
    }

    public Observable<ObjectModel<LoginDTO>> getProfile() {
        return karbalaApiService.getProfile();
    }

    public void saveUserData(LoginDTO loginDTO) {
        sharedManager.saveObject(loginDTO, Constants.USER_DATA);
        if (loginDTO.getToken() != null)
            sharedManager.saveObject(loginDTO.getToken().get(0), Constants.TOKEN);
    }

    public LoginDTO getuserData() {
        return sharedManager.getObject(Constants.USER_DATA, LoginDTO.class);
    }

    public Observable<ObjectModel<LoginDTO>> login(String phoneNumber) {
        return karbalaApiService.login(phoneNumber);
    }

    public Observable<ObjectModel<LoginDTO>> citizenRegister(String phoneNumber, String fullName, String email, int citizenTypeId,
                                                             String city, String neighborhood, String dateOfBirth, int genderID,
                                                             long municipalityId, long countryId) {
        return karbalaApiService.citizenRegister(phoneNumber, fullName, email, citizenTypeId, city, neighborhood, dateOfBirth,
                genderID, municipalityId, countryId);
    }

    public Observable<ObjectModel<LoginDTO>> visitorRegister(String phoneNumber, String fullName, int citizenTypeId,
                                                             String dateOfBirth, int genderID, long countryId, long governorateId) {
        return karbalaApiService.visitorRegister(phoneNumber, fullName, citizenTypeId, dateOfBirth, genderID, countryId, governorateId);
    }

    public Observable<ObjectModel<LoginDTO>> verify(String phoneNumber, String vCode) {
        return karbalaApiService.verify(phoneNumber, vCode);
    }

    public Observable<ListModel<TownshipDTO>> getTownships() {
        return karbalaApiService.getTownships();
    }

    public Observable<ObjectModel<ContactUsDTO>> contactUs(String subject, String description) {
        return karbalaApiService.contactUs(subject, description);
    }

    public Observable<ObjectModel<ComplaintDTO>> addReport(RequestBody title, RequestBody details,
                                                           RequestBody reportTypeId, RequestBody townshipId,
                                                           RequestBody latitude, RequestBody longitude,
                                                           RequestBody duration, RequestBody videoImage,
                                                           List<MultipartBody.Part> files) {
        return karbalaApiService.addReport(title, details, reportTypeId, townshipId, latitude, longitude, duration, videoImage, files);
    }

    /*public Observable<ListModel<ReportDTO>> getAllReports(int page, boolean sorting) {
        return amanaKSAApiService.getAllReports(page, sorting);
    }*/

    public Observable<ListModel<ComplaintDTO>> searchInReports(String searchWord, int page, boolean sorting) {
        return karbalaApiService.searchInReports(searchWord, page, sorting);
    }

    public Observable<ObjectModel<LoginDTO>> editFullName(String fullName) {
        return karbalaApiService.editFullName(fullName);
    }


    public void saveLanguage(String language) {
        sharedManager.CashValue(Constants.LANG, language);
    }

    public Observable<ObjectModel<ComplaintDTO>> getReportDetails(long reportId) {
        return karbalaApiService.getReportDetails(reportId);
    }

    public Observable<ObjectModel<Boolean>> logout() {
        return karbalaApiService.logout();
    }
    public void logoutLocal() {
        sharedManager.logoutLocally();
    }


}
