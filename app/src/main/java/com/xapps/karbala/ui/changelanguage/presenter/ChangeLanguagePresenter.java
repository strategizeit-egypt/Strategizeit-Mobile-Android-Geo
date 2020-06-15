package com.xapps.karbala.ui.changelanguage.presenter;

import com.xapps.karbala.model.DataManager;

public class ChangeLanguagePresenter {

    public void setLanguage(String language){
        DataManager.getInstance().saveLanguage(language);
    }
}
