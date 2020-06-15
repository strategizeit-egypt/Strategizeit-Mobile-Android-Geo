package com.xapps.karbala.ui.area.presenter;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.model.data.dto.MetaDataDTO;

public class AreaPresenter {

    public MetaDataDTO getMetaDataDTO(){
        return DataManager.getInstance().getSavedMetaData();
    }
}
