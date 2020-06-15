package com.xapps.karbala.model.data.source.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.xapps.karbala.App;
import com.xapps.karbala.utils.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;


public class SharedManager {


    private static final String APP_PREFERENCE = "tollab_partner";
    private static SharedManager mSharedManager;
    private SharedPreferences mPreference;
    private Gson mGson;
    private SharedPreferences.Editor mEditor;

    private SharedManager(Context mContext) {
        mPreference = mContext.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        mEditor = mPreference.edit();
        mGson = new Gson();
    }

    public static SharedManager newInstance() {
        if (mSharedManager == null) {
            return mSharedManager = new SharedManager(App.mContext);
        } else {
            return mSharedManager;
        }
    }

    public SharedPreferences getPreference() {
        return mPreference;
    }

    public <T> void saveObject(T object, String key) {
        String fromObject = mGson.toJson(object);
        Boolean status = mEditor.putString(key, fromObject).commit();
    }



    public <T> void saveTypedObject(T object, Type type, String key) {
        String fromObject = mGson.toJson(object, type);
        mEditor.putString(key, fromObject).commit();
    }

    public <T> T getObject(String key, Class<T> Clazz) {
        String fromString = mPreference.getString(key, null);
        T t;
        if (fromString != null) {
            t = mGson.fromJson(fromString, Clazz);
            return t;
        }
        return null;
    }

    public <T> ArrayList<T> getTypedObject(String key, Type type) {
        String fromString = mPreference.getString(key, null);
        ArrayList<T> list;
        if (fromString != null) {
            list = mGson.fromJson(fromString, type);
            return list;
        }
        return null;
    }

    public String getCashValue(String key) {
        return mPreference.getString(key, null);

    }

    public void CashValue(String key, String value) {
        mEditor.putString(key, value).commit();

    }

    // Long -> key -> order_id
    // Integer -> value -> notification id
    public void saveNotificationsMap(Map<Long, Integer> inputMap) {

        String str = new Gson().toJson(inputMap);
        //mEditor.remove(Constants.NOTIFICATIONChANEL);
        mEditor.putString(Constants.NOTIFICATIONChANEL, str).commit();
    }

    public String getNotificationsMap(String key) {
        return mPreference.getString(key, "");
    }

    public void saveNotificationIdCounter(int counter) {
        mEditor.putInt(Constants.NOTIFICATION_ID_COUNTER,counter).commit();
    }
    public int getNotificationIdCounter(String key) {
        return mPreference.getInt(key,0);
    }


    public void CashBoolean(String key, Boolean value) {
        mEditor.putBoolean(key, value).commit();

    }

    public Boolean getCashBoolean(String key) {
        return mPreference.getBoolean(key, false);

    }

    public void removeImagePath(String key) {
        mEditor.remove(key).commit();
    }

    public void removeCashed(String key) {
        mEditor.remove(key).commit();
    }

    public void saveDownloadedContentVersion(String contentId, float version) {
        mEditor.putFloat(contentId, version).commit();
    }

    public float getDownloadedContentVersion(String key) {
        return mPreference.getFloat(key, -1);

    }
    public void logoutLocally() {
        try {
            mEditor.clear();
            mEditor.apply();
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
