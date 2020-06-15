package com.xapps.karbala.ui.verify.view;

import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.MainView;

public interface VerificationView extends MainView {
    void onVerifyResult(ObjectModel<LoginDTO> loginDTOObjectModel);

}
