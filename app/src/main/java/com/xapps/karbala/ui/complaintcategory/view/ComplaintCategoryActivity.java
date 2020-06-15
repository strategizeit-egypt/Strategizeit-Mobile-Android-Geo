package com.xapps.karbala.ui.complaintcategory.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.gson.Gson;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.CategoryDTO;
import com.xapps.karbala.model.data.dto.SubCategoryDTO;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.complaintcategory.presenter.ComplaintCategoryPresenter;
import com.xapps.karbala.ui.complaintsubcategory.view.ComplaintSubCategoryActivity;
import com.xapps.karbala.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComplaintCategoryActivity extends BaseActivity implements ComplaintCategoryView {
    private ComplaintCategoryPresenter complaintCategoryPresenter = new ComplaintCategoryPresenter(this);
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_shape)
    ImageView mToolbarShape;
    @BindView(R.id.complaint_category_rv)
    RecyclerView mComplaintCategoryRV;
    private SubCategoryDTO subCategoryDTO;

    private LinearLayoutManager mLinearLayoutManager;
    private ComplaintCategoryAdapter mComplaintCategoryAdapter;
    private List<CategoryDTO> categoryDTOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_category);
        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {
        mToolbarTitle.setText(getString(R.string.complaint_category_title));
        mToolbarShape.setImageResource(R.drawable.ic_shape_cosmic_latte_bg);

        subCategoryDTO = new Gson().fromJson(getIntent().getStringExtra(Constants.SELECTED_SUB_CATEGORY_DTO),SubCategoryDTO.class);

        categoryDTOList = new ArrayList<>();
        try {
            categoryDTOList = complaintCategoryPresenter.getMetaDataDTO().getCategoryDTOList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mComplaintCategoryAdapter = new ComplaintCategoryAdapter(this, categoryDTOList);
        mLinearLayoutManager = new GridLayoutManager(this, 2);
        mComplaintCategoryRV.setLayoutManager(mLinearLayoutManager);
        mComplaintCategoryRV.setAdapter(mComplaintCategoryAdapter);

        try {
            ((SimpleItemAnimator) mComplaintCategoryRV.getItemAnimator()).setSupportsChangeAnimations(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mComplaintCategoryAdapter.setOnCategoryItemClickListener(categoryDTO -> {
            Intent intent = new Intent(ComplaintCategoryActivity.this, ComplaintSubCategoryActivity.class);
            intent.putExtra(Constants.SELECTED_COMPLAINT_CATEGORY_DTO, new Gson().toJson(categoryDTO));
            intent.putExtra(Constants.SELECTED_SUB_CATEGORY_DTO, new Gson().toJson(subCategoryDTO));
            startActivityForResult(intent, Constants.SELECTED_SUB_CATEGORY_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.SELECTED_SUB_CATEGORY_CODE){
            subCategoryDTO = new Gson().fromJson(data.getStringExtra(Constants.SELECTED_SUB_CATEGORY_DTO),SubCategoryDTO.class);
            Intent intent = getIntent();
            intent.putExtra(Constants.SELECTED_SUB_CATEGORY_DTO,new Gson().toJson(subCategoryDTO));
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @OnClick(R.id.ic_back)
    public void onBackClickListener(View view) {
        onBackPressed();
    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onDismissLoader() {

    }

    @Override
    public void onShowLoader() {

    }

    @Override
    public void onTimeOut() {

    }

    @Override
    public void onError(int code) {

    }
}
