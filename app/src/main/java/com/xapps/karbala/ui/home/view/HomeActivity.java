package com.xapps.karbala.ui.home.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.xapps.karbala.R;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.more.view.MoreFragment;
import com.xapps.karbala.ui.mycomplaints.view.MyComplaintsFragment;
import com.xapps.karbala.ui.newcomplaint.view.NewComplaintFragment;
import com.xapps.karbala.utils.KarbalaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.nav_view)
    BottomNavigationView navView;

    private FragmentTransaction ft;
    private boolean fragmentsInitiated = false;
    NewComplaintFragment newComplaintFragment;
    MyComplaintsFragment myComplaintsFragment;
    MoreFragment moreFragment;
    private boolean newReportFragmentAdded = false;
    private boolean myReportsFragmentAdded = false;
    private boolean moreFragmentAdded = false;
    public static boolean isRequiredRefresh = false;
    public static int selectedFragmentId = 0;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        display(item.getItemId());
        selectedFragmentId = item.getItemId();
        return true;
    };
    private double _backPressTime;

    private void display(int itemId) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.fade_out);
        if (!fragmentsInitiated) {
            newComplaintFragment = NewComplaintFragment.newInstance();
            myComplaintsFragment = MyComplaintsFragment.newInstance();
            moreFragment = MoreFragment.newInstance();
            fragmentsInitiated = true;
        }
        switch (itemId) {
            case R.id.navigation_new_complaint:
                if (!newReportFragmentAdded) {
                    ft.add(R.id.frame_container, newComplaintFragment);
                    newReportFragmentAdded = true;
                }
                ft.show(newComplaintFragment);
                ft.hide(myComplaintsFragment);
                ft.hide(moreFragment);
                break;
            case R.id.navigation_my_complaints:
                if (!myReportsFragmentAdded) {
                    ft.add(R.id.frame_container, myComplaintsFragment);
                    myReportsFragmentAdded = true;
                }
                ft.hide(newComplaintFragment);
                ft.show(myComplaintsFragment);
                ft.hide(moreFragment);
                break;
            case R.id.navigation_more:
                if (!moreFragmentAdded) {
                    ft.add(R.id.frame_container, moreFragment);
                    moreFragmentAdded = true;
                }
                ft.hide(newComplaintFragment);
                ft.hide(myComplaintsFragment);
                ft.show(moreFragment);
                break;
        }
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initUI();
    }

    private void initUI() {
        // Firebase force crash
        /*Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));*/
        if (selectedFragmentId == 0) {
            navView.setSelectedItemId(R.id.navigation_new_complaint);
        } else if (selectedFragmentId == 1) {
            navView.setSelectedItemId(R.id.navigation_my_complaints);
        }/*else if (selectedFragmentId == 2) {
            navView.setSelectedItemId(R.id.navigation_more);
        }*/ else {
            navView.setSelectedItemId(selectedFragmentId);
        }
        isRequiredRefresh = false;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newComplaintFragment.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        try {


            double t = System.currentTimeMillis();

            if (t - _backPressTime > 2000) {
                _backPressTime = t;

                KarbalaUtils.showToast(this, getString(R.string.press_back_again), FancyToast.INFO);
            } else {


                finish();
            }


        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequiredRefresh) {
            finish();
            startActivity(getIntent());
        }
    }
}
