package com.xapps.karbala.ui.login.view;

import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.MainView;

public interface LoginView extends MainView {
    void onLoginResult(ObjectModel<LoginDTO> loginDTOObjectModel);
}
