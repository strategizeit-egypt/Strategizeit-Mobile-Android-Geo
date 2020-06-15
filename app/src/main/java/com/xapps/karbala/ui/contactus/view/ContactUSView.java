package com.xapps.karbala.ui.contactus.view;

import com.xapps.karbala.model.data.dto.ContactUsDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.MainView;

public interface ContactUSView extends MainView {
    void onContactUsResult(ObjectModel<ContactUsDTO> contactUsDTOObjectModel);
}
