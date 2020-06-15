package com.xapps.karbala.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.xapps.karbala.App;
import com.xapps.karbala.R;
import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.model.data.dto.SettingsDTO;
import com.xapps.karbala.model.data.source.preferences.SharedManager;
import com.xapps.karbala.ui.base.view.MainView;

import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.HttpException;

public class KarbalaUtils {

    private static KProgressHUD kProgressHUD;

    public static void showError(MainView view, Throwable e) {

        try {
            if (e instanceof HttpException) {
                HttpException error = (HttpException) e;
                JSONObject jsonObject = new JSONObject(((HttpException) e).response().errorBody().string());

                int errorCode;
                try {
                    errorCode = jsonObject.getJSONObject("errors").getInt("Code");
                } catch (Exception e1) {
                    errorCode = Constants.GENERAL_API_EXCEPTION;
                }
                view.onError(errorCode);
                return;
            }
            if (e instanceof SocketTimeoutException) {
                view.onTimeOut();
                return;
            } else if (e instanceof UnknownHostException) {
                view.onTimeOut();
                return;
            }
            view.onError(((HttpException) e).code());
        } catch (Exception err) {
            view.onError(Constants.NONHTTP_EXCEPTION);
        }

    }


    public static void showToast(Context context, String Message, int Status) {
        try {

            FancyToast.makeText(context, Message, FancyToast.LENGTH_SHORT, Status, false).show();

        } catch (Exception e) {
        }
    }

    /////////// init application Languages ////////////////////////////
    public static void InitCurrentLanguage(Context context) {
        String lang = SharedManager.newInstance().getCashValue(Constants.LANG);
        lang = (lang == null) ? "" : lang;
        lang = (lang.contentEquals("")) ? Constants.ENGLISH : lang;
        if (lang.contentEquals(Constants.ARABIC))
            LocalHelper.setLocale(context, "ar");
        else
            LocalHelper.setLocale(context, "en");
    }

    public static String validatePhoneNumber(String countryCode, String phNumber) {
        String internationalFormat = "";
        try {
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
                internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                //Toast.makeText(App.mContext, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
                return internationalFormat;
            } else {
                //Toast.makeText(App.mContext, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
                return internationalFormat;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return internationalFormat;
        }
    }


    public static void InitLoader(Context ctx) {
        try {
            kProgressHUD = new KProgressHUD(ctx);
            kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);


        } catch (Exception e) {
        }
    }

    public static void showLoader(Context ctx, String message) {


        if (kProgressHUD == null) {
            kProgressHUD = new KProgressHUD(ctx);

        } else {

            kProgressHUD.dismiss();
            kProgressHUD = null;
            kProgressHUD = new KProgressHUD(ctx);
            kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setDetailsLabel(message)
                    .setCancellable(false)
                    .setAnimationSpeed(3)
                    .setDimAmount(0.5f).setSize(App.dialogWidth, App.dialogHeight);

            kProgressHUD.show();
        }


    }

    public static void hideLoader() {

        try {
            kProgressHUD.dismiss();
        } catch (Exception e) {
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) App.mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static String formatDate(String oldDate) {

        String finalDateResult = "";
        try {

            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());

            DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            oldFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            DateFormat newFormat = new SimpleDateFormat("dd MMM - h:mm aa", Locale.getDefault());


            Date date = null;


            try {
                date = oldFormat.parse(oldDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String newDateString = "" + newFormat.format(date);


            finalDateResult = newDateString;


            return finalDateResult;
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatNotificationDate(String oldDate) {

        String finalDateResult = "";
        try {


            DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            oldFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            DateFormat newFormat = new SimpleDateFormat("dd MMM yyyy h:mm aa", Locale.getDefault());


            Date date = null;


            try {
                date = oldFormat.parse(oldDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String newDateString = "" + newFormat.format(date);


            finalDateResult = newDateString;


            return finalDateResult;
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatDate(String oldDate, String format) {

        String finalDateResult = "";
        try {

            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());

            DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            oldFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            DateFormat newFormat = new SimpleDateFormat(format, Locale.ENGLISH);


            Date date = null;


            try {
                date = oldFormat.parse(oldDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String newDateString = "" + newFormat.format(date);


            finalDateResult = newDateString;


            return finalDateResult;
        } catch (Exception e) {
            return "";
        }
    }

    public static void uploadPhoto(Activity activity) {
        CropImage.activity()
                .setCropMenuCropButtonTitle(App.mContext.getString(R.string.save))
                .start(activity);
    }

    public static void showErrorResult(Context context) {
        showToast(context, context.getString(R.string.result_error_try_later), Constants.FANCYERROR);
    }

    public static void showTimeOutMessage(Context context) {
        showToast(context, context.getResources().getString(R.string.try_again), Constants.FANCYERROR);
    }

    public static String extractSettingsByKey(String key) {
        HashMap<String, String> settingsHashMap = new HashMap<>();
        try {
            for (SettingsDTO settingsDTO : DataManager.getInstance().getSavedMetaData().getSettingsDTO()) {
                settingsHashMap.put(settingsDTO.getKeyName(), settingsDTO.valueContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return settingsHashMap.get(key);
    }

    public static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    public static RippleDrawable getBackgroundDrawable(int pressedColor, Drawable backgroundDrawable)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null);
        }
        return null;
    }

    public static ColorStateList getPressedState(int pressedColor)
    {
        return new ColorStateList(new int[][]{ new int[]{}},new int[]{pressedColor});
    }
}
