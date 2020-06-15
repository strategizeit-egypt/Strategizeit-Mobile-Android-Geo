package com.xapps.karbala.ui.subarea.presenter;

import com.xapps.karbala.model.DataManager;
import com.xapps.karbala.model.data.dto.MetaDataDTO;

public class SubAreasPresenter {
    public MetaDataDTO getMetaDataDTO(){
        return DataManager.getInstance().getSavedMetaData();
    }
}
