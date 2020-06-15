package com.xapps.karbala.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.xapps.karbala.App;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;


public final class CommonUtils {

    private static final String TAG = "CommonUtils";

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

  /*  public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }*/

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String loadJSONFromAsset(Context context, String jsonFileName)
            throws IOException {

        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }

    /////////////////////////////////// phone //////////////////////////////////////
    public static boolean PhoneNumberValidation(String countryCode, EditText phone, Context context) {

        if (phone.getText().toString().contentEquals(""))
            // makeToast(context.getString(R.string.enter_mobile), Constants.ERROR);
            return false;

        if (countryCode.length() > 0 && phone.getText().length() > 0) {

            if (isValidPhoneNumber(phone.getText().toString())) {

                boolean status = validateUsing_libphonenumber(formatCountryCode(countryCode), phone.getText().toString());
                // makeToast(context.getString(R.string.invalid_mobile), Constants.ERROR);
                return status;
            } else {

                // makeToast(context.getString(R.string.invalid_mobile), Constants.ERROR);
                return false;
            }
        } else {

            //makeToast(context.getString(R.string.invalid_mobile), Constants.ERROR);
            return false;
        }
    }

    private static String formatCountryCode(String countryCode) {
        return countryCode.charAt(0) == '+' ? countryCode.substring(1) : countryCode;
    }

    private static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    private static boolean validateUsing_libphonenumber(String countryCode, String phNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(App.mContext);
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
        } catch (NumberParseException e) {
            System.err.println(e);
        }

        boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
        if (isValid) {
            String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
//            Toast.makeText(this, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
            return true;
        } else {
//            Toast.makeText(this, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /////////////////////////////////////// email//////////////////////////////////
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static Pattern pattern;
    private Matcher matcher;


    public boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    ///////////////////////////////////// date //////////////////////////////////////
    public static String formatDate(String date) {
        try {
            date = date.replaceAll("T", " ");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateStart = format.parse(date);

            DateFormat dateFormatResult = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm");

            return dateFormatResult.format(dateStart);


        } catch (Exception e) {
            return date.replaceAll("T", " ");
        }
    }

/*    public static boolean isNetworkAvailable() {
        return  isConnected();
    }

    public  static boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) App.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()  ) {
            return true;
        }
        return false;
    }
    public static boolean isOnline() {
        try {
            if (isConnected()) {
                try {
                    Log.d("BING", "start bing");
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("google.com").openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    Log.d("BING", "done bing");
                    return (urlc.getResponseCode() == 200);
                } catch (IOException e) {
                    Log.e("BING", "ErrorDTO checking internet connection", e);
                }
            } else {
                Log.d("BING", "No network available!");
            }

            return false;
        }catch (Exception e)
        {
            return  false;
        }

    }*/

}
