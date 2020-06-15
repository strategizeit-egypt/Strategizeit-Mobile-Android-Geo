package com.xapps.karbala.ui.townships.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.TownshipDTO;
import com.xapps.karbala.model.data.dto.generic.ListModel;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.townships.presenter.TownshipsPresenter;
import com.xapps.karbala.utils.KarbalaUtils;
import com.xapps.karbala.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TownshipsActivity extends BaseActivity implements TownshipsView {
    private TownshipsPresenter townshipsPresenter = new TownshipsPresenter(this);

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_shape)
    ImageView mToolbarShape;
    @BindView(R.id.townships_rv)
    RecyclerView mTownshipsRV;
    @BindView(R.id.empty_view)
    ConstraintLayout mEmptyView;
    @BindView(R.id.network_error_view)
    ConstraintLayout mNetworkErrorView;
    @BindView(R.id.select_township)
    TextView mSelectTownshipTV;
    @BindView(R.id.townships_swipe_refresh)
    SwipeRefreshLayout townshipsSwipeRefresh;
    @BindView(R.id.noDataMessage)
    TextView noDataMessage;

    private LinearLayoutManager mLinearLayoutManager;
    private TownshipsAdapter mTownshipsAdapter;
    private long selectedReportTownshipId = 0L;
    private TownshipDTO selectedReportTownshipDTO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_townships);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
        mToolbarTitle.setText(getString(R.string.townships_title));
        mToolbarShape.setImageResource(R.drawable.ic_shape_cosmic_latte_bg);

        selectedReportTownshipDTO = new Gson().fromJson(getIntent().getStringExtra(Constants.SELECTED_REPORT_TOWNSHIP_DTO), TownshipDTO.class);
        if (selectedReportTownshipDTO != null) {
            selectedReportTownshipId = selectedReportTownshipDTO.getId();
        }

        mTownshipsAdapter = new TownshipsAdapter(this, new ArrayList<>(), selectedReportTownshipId);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mTownshipsRV.setLayoutManager(mLinearLayoutManager);
        mTownshipsRV.setAdapter(mTownshipsAdapter);

        try {
            ((SimpleItemAnimator) mTownshipsRV.getItemAnimator()).setSupportsChangeAnimations(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        townshipsSwipeRefresh.setOnRefreshListener(() -> {
            mTownshipsAdapter.clear();
            townshipsPresenter.getTownships();
        });

        townshipsPresenter.getTownships();
    }

    @Override
    public void onGetTownshipsResult(ListModel<TownshipDTO> townshipDTOListModel) {
        mTownshipsAdapter.addTownshipsToAdapter(townshipDTOListModel.getModel(), selectedReportTownshipId);
        handleUiEmptyErrorView(Constants.UI_STAT_DATA_VIEW);
    }

    @OnClick(R.id.select_township)
    public void onSelectTownshipClickListener(View view) {
        if (mTownshipsAdapter.getSelectedTownshipDTO() != null) {
            Intent intent = getIntent();
            intent.putExtra(Constants.SELECTED_REPORT_TOWNSHIP_DTO, new Gson().toJson(mTownshipsAdapter.getSelectedTownshipDTO()));
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            KarbalaUtils.showToast(this, getString(R.string.choose_township_error_message), Constants.FANCYERROR);
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
        KarbalaUtils.hideLoader();
    }

    @Override
    public void onShowLoader() {
        KarbalaUtils.showLoader(this, getString(R.string.loading));
    }

    @Override
    public void onTimeOut() {
        KarbalaUtils.hideLoader();
        KarbalaUtils.showTimeOutMessage(this);
        handleUiEmptyErrorView(Constants.UI_STAT_NETWORK_ERROR);
    }

    @Override
    public void onError(int code) {
        KarbalaUtils.showErrorResult(this);
        handleUiEmptyErrorView(Constants.UI_STAT_NETWORK_ERROR);
    }

    public void handleUiEmptyErrorView(int uiStateDataError) {
        try {
            setRefreshOf();
            if (mTownshipsAdapter.getItemCount() > 0) {
                mSelectTownshipTV.setVisibility(View.VISIBLE);
                mTownshipsRV.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mNetworkErrorView.setVisibility(View.GONE);
            } else {
                // adapter is empty
                mSelectTownshipTV.setVisibility(View.GONE);
                mTownshipsRV.setVisibility(View.GONE);
                if (uiStateDataError == Constants.UI_STAT_NETWORK_ERROR) {
                    mNetworkErrorView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                    noDataMessage.setText(getString(R.string.no_townships));
                    mNetworkErrorView.setVisibility(View.GONE);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setRefreshOf() {
        try {
            if (townshipsSwipeRefresh.isRefreshing()) {
                townshipsSwipeRefresh.setRefreshing(false);
            }
        } catch (Exception e) {
        }
    }


}
