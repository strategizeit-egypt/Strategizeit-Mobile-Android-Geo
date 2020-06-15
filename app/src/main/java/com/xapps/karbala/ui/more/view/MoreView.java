package com.xapps.karbala.ui.more.view;

import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.MainView;

public interface MoreView extends MainView {
    void onEditFullNameResult(ObjectModel<LoginDTO> loginDTOObjectModel);

    void onLogout(ObjectModel<Boolean> result);
}
