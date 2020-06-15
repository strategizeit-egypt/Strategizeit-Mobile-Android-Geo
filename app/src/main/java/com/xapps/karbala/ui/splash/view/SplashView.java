package com.xapps.karbala.ui.splash.view;

import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.MetaDataDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.MainView;

public interface SplashView extends MainView {
    void onMetaDataResult(ObjectModel<MetaDataDTO> metaDataDTOObjectModel);
    void onProfileResult(ObjectModel<LoginDTO> profileDTOObjectModel);
}
