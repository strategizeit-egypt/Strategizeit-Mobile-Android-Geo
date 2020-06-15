package com.xapps.karbala.ui.mycomplaints.view;

import com.xapps.karbala.model.data.dto.ComplaintDTO;
import com.xapps.karbala.model.data.dto.generic.ListModel;
import com.xapps.karbala.ui.base.view.MainView;

public interface MyReportsView extends MainView {
    void onAddReportDetailsResult(ListModel<ComplaintDTO> reportDTOListModel);
}
