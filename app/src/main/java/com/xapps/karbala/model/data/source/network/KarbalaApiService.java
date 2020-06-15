package com.xapps.karbala.model.data.source.network;

import com.xapps.karbala.model.data.dto.ComplaintDTO;
import com.xapps.karbala.model.data.dto.ContactUsDTO;
import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.MetaDataDTO;
import com.xapps.karbala.model.data.dto.TownshipDTO;
import com.xapps.karbala.model.data.dto.generic.ListModel;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface KarbalaApiService {
    @GET("GetMeta")
    Observable<ObjectModel<MetaDataDTO>> getMetaData();

    @GET("GetCitizenProfile")
    Observable<ObjectModel<LoginDTO>> getProfile();

    //login
    @FormUrlEncoded
    @POST("Register")
    Observable<ObjectModel<LoginDTO>> login(@Field("PhoneNumber") String phoneNumber);

    // citizen register
    @FormUrlEncoded
    @POST("Register")
    Observable<ObjectModel<LoginDTO>> citizenRegister(@Field("PhoneNumber") String phoneNumber, @Field("FullName") String fullName,
                                                      @Field("Email") String email, @Field("CitizenTypeId") int citizenTypeId,
                                                      @Field("City") String city, @Field("Neighborhood") String neighborhood,
                                                      @Field("DateOfBirth") String dateOfBirth, @Field("GenderId") int genderID,
                                                      @Field("MunicipalityId") long municipalityId,@Field("CountryId") long countryId);

    // visitor register
    @FormUrlEncoded
    @POST("Register")
    Observable<ObjectModel<LoginDTO>> visitorRegister(@Field("PhoneNumber") String phoneNumber, @Field("FullName") String fullName,
                                                      @Field("CitizenTypeId") int citizenTypeId, @Field("DateOfBirth") String dateOfBirth,
                                                      @Field("GenderId") int genderID,@Field("CountryId") long countryId,
                                                      @Field("GovernorateId") long governorateId);

    @GET("Verify")
    Observable<ObjectModel<LoginDTO>> verify(@Query("PhoneNumber") String phoneNumber, @Query("vcode") String vCode);

    @GET("GetTownships")
    Observable<ListModel<TownshipDTO>> getTownships();

    @FormUrlEncoded
    @POST("AddContactUs")
    Observable<ObjectModel<ContactUsDTO>> contactUs(@Field("Titlle") String subject, @Field("Message") String description);

   /* @GET("GetAllReports")
    Observable<ListModel<ReportDTO>> getAllReports(@Query("Page") int page, @Query("Sorting") boolean sorting);*/

    @GET("SearchInReports")
    Observable<ListModel<ComplaintDTO>> searchInReports(@Query("Word") String searchWord, @Query("Page") int page,
                                                        @Query("Sorting") boolean sorting);

    @FormUrlEncoded
    @POST("EditFullName")
    Observable<ObjectModel<LoginDTO>> editFullName(@Field("FullName") String fullName);

    @GET("GetReportDetails")
    Observable<ObjectModel<ComplaintDTO>> getReportDetails(@Query("ReportId") long reportId);

    @Multipart
    @POST("AddReport")
    Observable<ObjectModel<ComplaintDTO>> addReport(
            // we can use RequestBody instead of String, Retrofit2 convert data types to RequestBody then into MultipartBody.part object
            // but better to use RequestBody
            // by using RequestBody we can pass null value in case in some case not send
            // if user doesn't have title we can pass null and this too for files
            @Part("Title") RequestBody title,
            @Part("Details") RequestBody details,
            @Part("ReportTypeId") RequestBody reportTypeId,
            @Part("TownshipId") RequestBody townshipId,
            @Part("Latitude") RequestBody latitude,
            @Part("Longitude") RequestBody longitude,
            @Part("Duration") RequestBody duration,
            @Part("VideoImage") RequestBody videoImage,
            @Part List<MultipartBody.Part> files);

    @GET("Logout")
    Observable<ObjectModel<Boolean>>logout();
}