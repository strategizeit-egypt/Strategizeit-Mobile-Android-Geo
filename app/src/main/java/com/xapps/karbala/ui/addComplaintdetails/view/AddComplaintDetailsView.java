package com.xapps.karbala.ui.addComplaintdetails.view;

import com.xapps.karbala.model.data.dto.ComplaintDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.MainView;

public interface AddComplaintDetailsView extends MainView {
    void onAddReportDetailsResult(ObjectModel<ComplaintDTO> reportDTOObjectModel);
}
