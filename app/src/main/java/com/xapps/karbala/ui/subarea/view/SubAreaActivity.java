package com.xapps.karbala.ui.subarea.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.gson.Gson;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.AreaDTO;
import com.xapps.karbala.model.data.dto.SubAreaDTO;
import com.xapps.karbala.ui.addComplaintdetails.view.AddComplaintDetailsActivity;
import com.xapps.karbala.ui.area.presenter.AreaPresenter;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubAreaActivity extends BaseActivity {
    private AreaPresenter areaPresenter = new AreaPresenter();
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_shape)
    ImageView mToolbarShape;
    @BindView(R.id.sub_areas_rv)
    RecyclerView mSubAreasRV;

    private LinearLayoutManager mLinearLayoutManager;
    private SubAreasAdapter mSubAreaAdapter;
    private List<SubAreaDTO> subAreaDTOList;
    private long selectedSubAreaId = 0L;
    private AreaDTO areaDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_area);
        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {
        mToolbarTitle.setText(getString(R.string.title_townships));
        mToolbarShape.setImageResource(R.drawable.ic_shape_cosmic_latte_bg);

        areaDTO = new Gson().fromJson(getIntent().getStringExtra(Constants.SELECTED_AREA_DTO), AreaDTO.class);
        subAreaDTOList = new ArrayList<>();
        try {
            subAreaDTOList = areaDTO.getSubAreaDTOList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        mSubAreaAdapter = new SubAreasAdapter(this, subAreaDTOList);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mSubAreasRV.setLayoutManager(mLinearLayoutManager);
        mSubAreasRV.setAdapter(mSubAreaAdapter);

        try {
            ((SimpleItemAnimator) mSubAreasRV.getItemAnimator()).setSupportsChangeAnimations(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.select_sub_area)
    public void onAreaClickListener(View view) {
        if (mSubAreaAdapter.getSelectedSubAreaDTO() != null) {
            Intent intent = new Intent(SubAreaActivity.this, AddComplaintDetailsActivity.class);
            intent.putExtra(Constants.SELECTED_SUB_AREA_DTO, new Gson().toJson(mSubAreaAdapter.getSelectedSubAreaDTO()));
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

