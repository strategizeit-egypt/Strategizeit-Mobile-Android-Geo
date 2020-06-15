package com.xapps.karbala.utils;

/**
 * Created by Mansour on 4/16/2019.
 */

public class Constants {
    public static final String ENGLISH = "en";
    public static final String ARABIC = "ar";
    public static final String LANG = "language";

    public static final String TOKEN = "token";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    public static final String NOTIFICATIONChANEL = "";
    public static final String BASE_URL = "https://mobile.smartkarbala.com/ws/api/";
    public static final String BASE_URL_COMPLAINT_FILES = "https://mobile.smartkarbala.com/ws/Uploads/ReportFiles/";
    public static final String BASE_URL_CATEGORY_IMAGES = "https://mobile.smartkarbala.com/ws/Uploads/CategoryImages/";
    public static final String BASE_URL_VIDEO_IMAGES = "http://karbalaapi.xapps.co/Uploads/VideoImages/";


    public static final String NOTIFICATION_ID_COUNTER = "";
    public static final int TERMS_CONDITIONS_REQUEST_CODE = 13;
    public static final String AGREE = "agree";
    public static final String HIDE_AGREE_TERMS = "hide_agree_terms";
    public static final int REQUEST_LOCATION_PERMISSION_CODE = 22;
    public static final int REQUEST_STORAGE_PERMISSION_CODE = 23;
    public static final int SELECT_CATEGORY_CODE = 14;
    public static final int SELECTED_SUB_CATEGORY_CODE = 15;
    public static final String SELECTED_COMPLAINT_CATEGORY_DTO = "select_report_type_dto";
    public static final int REQUEST_RECORD_AND_STORAGE_PERMISSION_CODE = 24;
    public static final int REQUEST_MICROPHONE_PERMISSION_CODE = 25;
    public static final int REQUEST_STORAGE_PERMISSION_FOR_RECORD_CODE = 26;
    public static final String META_DATA = "meta_data";
    public static final String USER_DATA = "user_data";
    public static final String PHONE_NUMBER = "phone_number";
    public static final int UI_STAT_NETWORK_ERROR = 50;
    public static final int UI_STAT_DATA_VIEW = 51;
    public static final String SELECTED_TOWNSHIP_ID = "selected_township_id";
    public static final String SELECTED_LAT_LON = "selected_lat_lon";
    public static final int PAGE_SIZE_30 = 30;
    public static final String COMPLAINT_ID = "report_id";
    public static final int AUTOCOMPLETE_REQUEST_CODE = 27;
    public static final String CAPTCHA_KEY = "6LdpvewUAAAAAE2f2JYiUByCn3O0xf47Hcai3kNw";
    public static final String SELECTED_REPORT_TOWNSHIP_DTO = "selected_township_dto";
    public static final int SELECT_REPORT_TOWNSHIP_CODE = 16;
    public static final String ABOUT_US_KEY = "aboutus";
    public static final String ABOUT_US_KEY_AR = "aboutusar";
    public static final String TERMS_KEY = "termsandconditions";
    public static final String TERMS_KEY_AR = "termsandconditionsar";
    public static final String HOTLINE_NUMBER_KEY = "hotline";
    public static final String EMAIL_KEY = "email";
    public static final String TWITTER_KEY = "twitter";
    public static final String WHATSAPP_KEY = "whatsapp";
    public static final String WHATSAPP_BASE_URL = "https://api.whatsapp.com/send?phone=";
    public static final String TWITTER_BASE_URL = "https://twitter.com/#!/";
    public static final String SELECTED_AREA_DTO = "area_dto";
    public static final String SELECTED_SUB_AREA_DTO = "sub_area_dto";
    public static final String WEBSITE_KEY = "website";
    public static final String SELECTED_SUB_CATEGORY_DTO = "sub_category_dto";
    public static final int RECORD_VIDEO_CODE = 27;


    //API RESPONSE CODES
    public static int NONHTTP_EXCEPTION = 1;
    public static int GENERAL_API_EXCEPTION = 2;


    // response flags
    public static final int NOREADED = 0;
    public static final int READINGFAIL = 1;
    public static final int READINGSUCCESS = 2;


    //fancy toast status codes
    public final static int FANCYSUCCESS = 1;
    public final static int FANCYWARNING = 2;
    public final static int FANCYERROR = 3;
    public final static int FANCYINFO = 4;
    public final static int FANCYDEFAULT = 5;
    public final static int FANCYCONFUSING = 6;
}
