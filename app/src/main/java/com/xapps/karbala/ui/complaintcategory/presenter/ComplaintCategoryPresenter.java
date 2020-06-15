package com.xapps.karbala.ui.complaintcategory.presenter;

import com.xapps.karbala.model.data.dto.MetaDataDTO;
import com.xapps.karbala.model.data.source.preferences.SharedManager;
import com.xapps.karbala.ui.complaintcategory.view.ComplaintCategoryView;
import com.xapps.karbala.utils.Constants;

public class ComplaintCategoryPresenter {
    ComplaintCategoryView complaintCategoryView;

    public ComplaintCategoryPresenter(ComplaintCategoryView complaintCategoryView) {
        this.complaintCategoryView = complaintCategoryView;
    }

    public MetaDataDTO getMetaDataDTO() {
        return SharedManager.newInstance().getObject(Constants.META_DATA, MetaDataDTO.class);
    }
}
