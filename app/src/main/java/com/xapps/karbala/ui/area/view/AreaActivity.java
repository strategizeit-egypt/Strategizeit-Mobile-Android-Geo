package com.xapps.karbala.ui.area.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.gson.Gson;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.AreaDTO;
import com.xapps.karbala.ui.addComplaintdetails.view.AddComplaintDetailsActivity;
import com.xapps.karbala.ui.area.presenter.AreaPresenter;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.subarea.view.SubAreaActivity;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AreaActivity extends BaseActivity {
    private AreaPresenter areaPresenter = new AreaPresenter();
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_shape)
    ImageView mToolbarShape;
    @BindView(R.id.areas_rv)
    RecyclerView mAreasRV;
    @BindView(R.id.empty_layout)
    ConstraintLayout emptyLayout;
    @BindView(R.id.noDataMessage)
    TextView noDataMessage;
    @BindView(R.id.select_area)
    TextView selectArea;

    private LinearLayoutManager mLinearLayoutManager;
    private AreasAdapter mAreaAdapter;
    private List<AreaDTO> areaDTOList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);
        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {
        noDataMessage.setText(getString(R.string.no_districts));
        mToolbarTitle.setText(getString(R.string.title_districts));
        mToolbarShape.setImageResource(R.drawable.ic_shape_cosmic_latte_bg);


        areaDTOList = new ArrayList<>();
        try {
            areaDTOList = areaPresenter.getMetaDataDTO().getAreaDTOList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAreaAdapter = new AreasAdapter(this, areaDTOList);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAreasRV.setLayoutManager(mLinearLayoutManager);
        mAreasRV.setAdapter(mAreaAdapter);

        if (mAreaAdapter.getItemCount() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            mAreasRV.setVisibility(View.GONE);
            selectArea.setVisibility(View.GONE);

        }else{
            emptyLayout.setVisibility(View.GONE);
            mAreasRV.setVisibility(View.VISIBLE);
            selectArea.setVisibility(View.VISIBLE);

        }

        try {
            ((SimpleItemAnimator) mAreasRV.getItemAnimator()).setSupportsChangeAnimations(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.select_area)
    public void onAreaClickListener(View view) {
        AreaDTO areaDTO = mAreaAdapter.getSelectedAreaDTO();
        if (areaDTO != null) {
            Intent intent;
            if (areaDTO.getSubAreaDTOList().size() == 1) {
                intent = new Intent(AreaActivity.this, AddComplaintDetailsActivity.class);
                intent.putExtra(Constants.SELECTED_SUB_AREA_DTO, new Gson().toJson(areaDTO.getSubAreaDTOList().get(0)));
            } else {
                intent = new Intent(AreaActivity.this, SubAreaActivity.class);
                intent.putExtra(Constants.SELECTED_AREA_DTO, new Gson().toJson(areaDTO));
            }
            startActivity(intent);
        } else {
            KarbalaUtils.showToast(this, getString(R.string.choose_area_error_message), Constants.FANCYERROR);
        }
    }

    @OnClick(R.id.ic_back)
    public void onBackClickListener(View view) {
        onBackPressed();
    }
}

