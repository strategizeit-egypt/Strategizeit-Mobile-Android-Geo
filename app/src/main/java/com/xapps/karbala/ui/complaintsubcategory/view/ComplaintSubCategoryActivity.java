package com.xapps.karbala.ui.complaintsubcategory.view;

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
import com.xapps.karbala.model.data.dto.CategoryDTO;
import com.xapps.karbala.model.data.dto.SubCategoryDTO;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComplaintSubCategoryActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_shape)
    ImageView mToolbarShape;
    @BindView(R.id.rc_sub_categories)
    RecyclerView rcSubCategories;


    private LinearLayoutManager mLinearLayoutManager;
    private SubCategoryAdapter subCategoryAdapter;
    private List<SubCategoryDTO> subCategoryDTOList;
    private CategoryDTO categoryDTO;
    private long selectedSubCategoryId = 0L;
    private SubCategoryDTO selectedSubCategoryDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_sub_category);
        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {
        mToolbarTitle.setText(getString(R.string.complaint_category_title));
        mToolbarShape.setImageResource(R.drawable.ic_shape_cosmic_latte_bg);
        // to get all aub categories in category
        categoryDTO = new Gson().fromJson(getIntent().getStringExtra(Constants.SELECTED_COMPLAINT_CATEGORY_DTO), CategoryDTO.class);
        // to select previous selected of sub category

        subCategoryDTOList = new ArrayList<>();
        try {
            // to get all aub categories in category
            categoryDTO = new Gson().fromJson(getIntent().getStringExtra(Constants.SELECTED_COMPLAINT_CATEGORY_DTO), CategoryDTO.class);
            subCategoryDTOList = categoryDTO.getSubCategoryDTOList();
            // to select previous selected of sub category
            selectedSubCategoryDTO = new Gson().fromJson(getIntent().getStringExtra(Constants.SELECTED_SUB_CATEGORY_DTO), SubCategoryDTO.class);
            if (selectedSubCategoryDTO != null) {
                selectedSubCategoryId = selectedSubCategoryDTO.getId();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        subCategoryAdapter = new SubCategoryAdapter(this, subCategoryDTOList, selectedSubCategoryId);
        mLinearLayoutManager = new LinearLayoutManager(this);
        rcSubCategories.setLayoutManager(mLinearLayoutManager);
        rcSubCategories.setAdapter(subCategoryAdapter);

        try {
            ((SimpleItemAnimator) rcSubCategories.getItemAnimator()).setSupportsChangeAnimations(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.select_sub_category)
    public void onAreaClickListener(View view) {
        SubCategoryDTO subCategoryDTO = subCategoryAdapter.getSelectedSubCategoryDTO();
        if (subCategoryDTO != null) {
            Intent intent = getIntent();
            intent.putExtra(Constants.SELECTED_SUB_CATEGORY_DTO, new Gson().toJson(subCategoryDTO));
            setResult(RESULT_OK, intent);
            finish();
        } else {
            KarbalaUtils.showToast(this, getString(R.string.choose_sub_category_error_message), Constants.FANCYERROR);
        }
    }


    @OnClick(R.id.ic_back)
    public void onBackClickListener(View view) {
        onBackPressed();
    }
}
