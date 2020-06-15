package com.xapps.karbala.ui.complaintdetails.view;

import com.xapps.karbala.model.data.dto.ComplaintDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.MainView;

public interface ComplaintDetailsView extends MainView {
    void onGetComplaintDetailsResult(ObjectModel<ComplaintDTO> reportDTOObjectModel);
}
