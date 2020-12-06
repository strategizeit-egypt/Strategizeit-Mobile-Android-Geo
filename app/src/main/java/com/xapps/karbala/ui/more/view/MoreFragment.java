package com.xapps.karbala.ui.more.view;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.aboutus.view.AboutUsActivity;
import com.xapps.karbala.ui.changelanguage.view.ChangeLanguageActivity;
import com.xapps.karbala.ui.contactus.view.ContactUsActivity;
import com.xapps.karbala.ui.home.view.HomeActivity;
import com.xapps.karbala.ui.login.view.LoginActivity;
import com.xapps.karbala.ui.more.presenter.MorePresenter;
import com.xapps.karbala.ui.termsandconditions.view.TermsConditionsActivity;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class MoreFragment extends Fragment implements MoreView {

    MorePresenter morePresenter = new MorePresenter(this);

    @BindView(R.id.user_name_et)
    EditText mUserNameET;
    @BindView(R.id.save_user_name)
    AppCompatImageView saveUserName;
    @BindView(R.id.edit_user_name)
    ImageView edit_user_name;
    @BindView(R.id.cancel_user_name)
    AppCompatImageView cancelUserName;
    @BindView(R.id.profileSaveNameParent)
    LinearLayout profileSaveNameParent;
    @BindView(R.id.SpaceView)
    Space SpaceView;
    @BindView(R.id.header_container)
    LinearLayout headerContainer;
    @BindView(R.id.about_us_card)
    CardView aboutUsCard;
    @BindView(R.id.contact_us_card)
    CardView contactUsCard;
    @BindView(R.id.about_contact_container)
    LinearLayout aboutContactContainer;
    @BindView(R.id.terms_policy_card)
    CardView termsPolicyCard;
    @BindView(R.id.change_language_card)
    CardView changeLanguageCard;
    @BindView(R.id.more_layout)
    ConstraintLayout moreLayout;
    @BindView(R.id.logout_container)
    LinearLayout logoutContainer;

    private Drawable mUserNameDefaultDrawable;

    private LoginDTO loginDTO;
    private Unbinder unbinder;


    public MoreFragment() {
        // Required empty public constructor
    }


    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
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
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        unbinder = ButterKnife.bind(this, view);

        initUI();

        return view;
    }

    private void initUI() {


        mUserNameDefaultDrawable = mUserNameET.getBackground();
        mUserNameET.setBackground(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            logoutContainer.setBackground(KarbalaUtils.getBackgroundDrawable(getResources().getColor(R.color.blue2), null));
        }


        try {
            loginDTO = morePresenter.getUserData();
            mUserNameET.setText(loginDTO.getFullName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.edit_user_name)
    public void onEditUserNameClickListener(View view) {
        mUserNameET.setEnabled(true);
        mUserNameET.requestFocus(View.FOCUS_BACKWARD);
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(mUserNameET, InputMethodManager.SHOW_IMPLICIT);
        profileSaveNameParent.setVisibility(View.VISIBLE);
        mUserNameET.setBackground(mUserNameDefaultDrawable);
        edit_user_name.setVisibility(View.GONE);
        SpaceView.setVisibility(View.GONE);
    }

    @OnClick(R.id.save_user_name)
    public void onSaveUserNameClicked() {
        try {
            if (loginDTO.getFullName().contentEquals(mUserNameET.getText().toString())) {
                KarbalaUtils.showToast(getContext(), getString(R.string.error_user_name_not_chnaged), Constants.FANCYERROR);
                return;
            }
            morePresenter.editFullName(mUserNameET.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onEditFullNameResult(ObjectModel<LoginDTO> loginDTOObjectModel) {
        KarbalaUtils.showToast(getContext(), getString(R.string.msg_user_name_changed), Constants.FANCYSUCCESS);
        loginDTO = loginDTOObjectModel.getModel();
        morePresenter.saveUserData(loginDTO);
        hideEditUserName();
    }


    @OnClick(R.id.cancel_user_name)
    public void onCancelUserNameClicked() {
        hideEditUserName();
        mUserNameET.setText(loginDTO.getFullName());
    }

    public void hideEditUserName() {
        mUserNameET.setBackground(null);
        profileSaveNameParent.setVisibility(View.GONE);
        mUserNameET.setEnabled(false);
        edit_user_name.setVisibility(View.VISIBLE);
        SpaceView.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.about_us_card)
    public void onAboutUsClickListener(View view) {
        startActivity(new Intent(getContext(), AboutUsActivity.class));
    }

    @OnClick(R.id.contact_us_card)
    public void onContactUsClickListener(View view) {
        startActivity(new Intent(getContext(), ContactUsActivity.class));
    }

    @OnClick(R.id.terms_policy_card)
    public void onTermsPolicyClickListener(View view) {
        Intent intent = new Intent(getContext(), TermsConditionsActivity.class);
        intent.putExtra(Constants.HIDE_AGREE_TERMS, true);
        startActivity(intent);
    }

    @OnClick(R.id.change_language_card)
    public void onChangeLanguageClickListener(View view) {
        Intent intent = new Intent(getContext(), ChangeLanguageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @OnClick(R.id.logout_container)
    public void onLogoutClick(View view) {
        showAlertDialogToLogout();
    }

    public void showAlertDialogToLogout() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText(getString(R.string.confirm))
                .setContentText(getString(R.string.sure_logout))
                .setConfirmText(getString(R.string.ok))
                .setConfirmClickListener(sweetAlertDialog1 -> {
                    sweetAlertDialog1.dismiss();
                    morePresenter.logout();

                })
                .setCancelText(getString(R.string.cancel))
                .setCancelClickListener(SweetAlertDialog::dismiss)
                .show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.round_solid_blue_radius8));
    }

    @Override
    public void onLogout(ObjectModel<Boolean> result) {
        if (result.getModel()) {
            morePresenter.logoutLocally();
            HomeActivity.selectedFragmentId = 0;
            Toast.makeText(getContext(), getString(R.string.msg_logout_success), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }, 200);
        }

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
        KarbalaUtils.showLoader(getContext(), getString(R.string.loading));
    }

    @Override
    public void onTimeOut() {
        KarbalaUtils.showTimeOutMessage(getContext());
    }

    @Override
    public void onError(int code) {
        KarbalaUtils.showErrorResult(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
