package com.xapps.karbala.ui.register.view;

import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.MainView;

public interface RegisterView extends MainView {
    void onRegisterResult(ObjectModel<LoginDTO> loginDTOObjectModel);

}
