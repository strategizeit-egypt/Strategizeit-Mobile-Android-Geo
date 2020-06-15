package com.xapps.karbala;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.xapps.karbala.model.data.source.preferences.SharedManager;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.LocalHelper;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    public static Context mContext;
    public static int dialogWidth = 200;
    public static int dialogHeight = 120;



    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mContext = this;

       /* CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/cairo.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/
    }


    @Override
    protected void attachBaseContext(Context base) {

        try {
            String lang = SharedManager.newInstance().getCashValue(Constants.LANG);
            if (lang == null)
                lang = Constants.ENGLISH;

            if (lang.equals(Constants.ARABIC)) {

                super.attachBaseContext(LocalHelper.onAttach(base, "ar"));

            } else {

                super.attachBaseContext(LocalHelper.onAttach(base, "en"));
            }

        } catch (Exception e) {
            super.attachBaseContext(LocalHelper.onAttach(base, "en"));
        }
        MultiDex.install(this);
    }

}

