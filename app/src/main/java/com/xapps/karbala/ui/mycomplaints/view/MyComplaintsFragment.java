package com.xapps.karbala.ui.mycomplaints.view;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.ComplaintDTO;
import com.xapps.karbala.model.data.dto.generic.ListModel;
import com.xapps.karbala.ui.mycomplaints.presenter.MyReportsPresenter;
import com.xapps.karbala.ui.complaintdetails.view.ComplaintDetailsActivity;
import com.xapps.karbala.utils.KarbalaUtils;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.LinearPaginationScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyComplaintsFragment extends Fragment implements MyComplaintsView {
    private MyReportsPresenter myReportsPresenter = new MyReportsPresenter(this);

    @BindView(R.id.reports_rv)
    ShimmerRecyclerView mReportsRV;
    @BindView(R.id.swipe_refresh_reports)
    SwipeRefreshLayout mReportsSwipe;
    @BindView(R.id.empty_view)
    ConstraintLayout mEmptyView;
    @BindView(R.id.network_error_view)
    ConstraintLayout mNetworkErrorView;
    @BindView(R.id.noDataMessage)
    TextView noDataMessage;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.edit_search_report)
    EditText mEditSearchReport;

    private int offset = 0;
    private boolean afterLoad = true;
    private boolean isLoadMore = true;

    // private LinearLayoutManager mLinearLayoutManager;
    private ComplaintsAdapter mComplaintsAdapter;

    private boolean isSort = false;
    private boolean loadedFirstTime = false;
    private double _backPressTime;
    private boolean searchFlag;


    public MyComplaintsFragment() {
        // Required empty public constructor
    }

    public static MyComplaintsFragment newInstance() {
        MyComplaintsFragment fragment = new MyComplaintsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_reports, container, false);
        ButterKnife.bind(this, view);

        initUI();

        return view;
    }

    private void initUI() {

        //mReportsRV.setNestedScrollingEnabled(false);

        mReportsRV.hideShimmerAdapter();
        mReportsRV.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mReportsRV.setLayoutManager(mLayoutManager);
        mComplaintsAdapter = new ComplaintsAdapter(this, new ArrayList<>());
        mReportsRV.setAdapter(mComplaintsAdapter);

        mComplaintsAdapter.setOnReportItemClickListener(reportId -> {
            Intent intent = new Intent(getContext(), ComplaintDetailsActivity.class);
            intent.putExtra(Constants.COMPLAINT_ID, reportId);
            startActivity(intent);
        });


        mReportsRV.addOnScrollListener(new LinearPaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if (afterLoad && isLoadMore) {
                    offset++;
                    searchInReports();
                }
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });

        mReportsSwipe.setOnRefreshListener(() -> {
            mComplaintsAdapter.clear();
            afterLoad = false;
            isLoadMore = true;
            offset = 0;
            searchInReports();
        });


        mEditSearchReport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    stop();
                    double t = System.currentTimeMillis();
                    if (t - _backPressTime < 2000 || searchFlag) {
                        _backPressTime = t;
                        start();
                    } else {
                        prepareToLoadReports();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        noDataMessage.setText(getString(R.string.no_complaint));
        searchInReports();

    }


    @Override
    public void onAddReportDetailsResult(ListModel<ComplaintDTO> reportDTOListModel) {
        mReportsSwipe.setRefreshing(false);
        try {
            loadedFirstTime = true;
            if (reportDTOListModel.getModel() != null) {
                if (reportDTOListModel.getModel().size() == 0 && mComplaintsAdapter.getItemCount() == 0) {
                    offset--;
                    isLoadMore = false;
                } else {
                    afterLoad = false;
                    if (reportDTOListModel.getModel().size() < Constants.PAGE_SIZE_30) {

                        isLoadMore = false;
                    }
                    new Handler().postDelayed(() -> afterLoad = true, 2000);
                    mComplaintsAdapter.addReportsToAdapter(reportDTOListModel.getModel());

                }
            } else {
                offset--;
                isLoadMore = false;
            }

        } catch (Exception e) {
            KarbalaUtils.showToast(getContext(), getString(R.string.try_again), Constants.FANCYERROR);
        }
        handleUiEmptyViews(Constants.UI_STAT_DATA_VIEW);
    }

    @OnClick(R.id.img_sort_reports)
    public void onSortReportClickListener(View view) {
        isSort = !isSort;
        mComplaintsAdapter.clear();
        afterLoad = false;
        isLoadMore = true;
        offset = 0;
        searchInReports();
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

    }

    @Override
    public void onTimeOut() {
        KarbalaUtils.showTimeOutMessage(getContext());
        offset = offset <= 0 ? 0 : --offset;
        handleUiEmptyViews(Constants.UI_STAT_NETWORK_ERROR);
    }

    @Override
    public void onError(int code) {
        KarbalaUtils.showErrorResult(getContext());
        offset--;
        if (offset < 0)
            offset = 0;

        handleUiEmptyViews(Constants.UI_STAT_DATA_VIEW);
    }

    private void setRefreshOf() {
        try {
            if (mReportsSwipe.isRefreshing()) {
                mReportsSwipe.setRefreshing(false);
            }
        } catch (Exception e) {
        }
    }

    private void handleUiEmptyViews(int uiStatDataError) {
        try {
            setRefreshOf();
            mReportsRV.hideShimmerAdapter();
            progressBar.setVisibility(View.GONE);
            if (mComplaintsAdapter.getItemCount() > 0) {
                mReportsRV.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mNetworkErrorView.setVisibility(View.GONE);
            } else {
                if (uiStatDataError == Constants.UI_STAT_NETWORK_ERROR) {
                    mReportsRV.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.GONE);
                    mNetworkErrorView.setVisibility(View.VISIBLE);
                } else {
                    mReportsRV.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                    mNetworkErrorView.setVisibility(View.GONE);

                }
            }
        } catch (Exception e) {
            mReportsRV.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mNetworkErrorView.setVisibility(View.GONE);
        }
    }

    public static Handler myHandler = new Handler();
    Runnable myRunnable = () -> prepareToLoadReports();

    public void start() {
        myHandler.postDelayed(myRunnable, 1000);
    }

    public void stop() {
        try {
            myHandler.removeCallbacks(myRunnable);
        } catch (Exception e) {
        }
    }


    private void prepareToLoadReports() {
        afterLoad = false;
        //isLoadMore = true;
        searchFlag = true;
        mComplaintsAdapter.clear();
        isSort = false;
        new Handler().postDelayed(this::searchInReports, 1000);

    }

    private void searchInReports() {
        if (mComplaintsAdapter.getItemCount() != 0)
            progressBar.setVisibility(View.VISIBLE);

        if (offset == 0)
            mReportsRV.showShimmerAdapter();
        myReportsPresenter.searchInReports(mEditSearchReport.getText().toString(), offset, isSort);
    }

}


