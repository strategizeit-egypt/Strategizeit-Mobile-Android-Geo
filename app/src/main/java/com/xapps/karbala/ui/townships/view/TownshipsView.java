package com.xapps.karbala.ui.townships.view;

import com.xapps.karbala.model.data.dto.TownshipDTO;
import com.xapps.karbala.model.data.dto.generic.ListModel;
import com.xapps.karbala.ui.base.view.MainView;

public interface TownshipsView extends MainView {
    void onGetTownshipsResult(ListModel<TownshipDTO> townshipDTOListModel);
}
