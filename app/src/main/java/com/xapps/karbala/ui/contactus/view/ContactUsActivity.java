package com.xapps.karbala.ui.contactus.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.ContactUsDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.contactus.presenter.ContactUsPresenter;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends BaseActivity implements ContactUSView {
    private ContactUsPresenter contactUsPresenter = new ContactUsPresenter(this);

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_shape)
    ImageView mToolbarShape;
    @BindView(R.id.text_hotline_number)
    TextView mHotlineTV;
    @BindView(R.id.contact_us_message_title_et)
    EditText mContactUsMessageTitleET;
    @BindView(R.id.contact_us_message_description_et)
    EditText mContactUsMessageDescriptionET;

    private String hotlineNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {
        toolbarTitle.setText(getString(R.string.contact_us));
        hotlineNumber = KarbalaUtils.extractSettingsByKey(Constants.HOTLINE_NUMBER_KEY);
        mHotlineTV.setText(hotlineNumber);
        mToolbarShape.setImageResource(R.drawable.ic_shape_cosmic_latte_bg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHotlineTV.setLetterSpacing(0.3f);
        }

    }

    @OnClick(R.id.ic_back)
    public void onBackClickListener(View view) {
        onBackPressed();
    }

    @OnClick(R.id.send_contact_us_message_tv)
    public void onSendContactUsMessageClickListener(View view) {
        if (!TextUtils.isEmpty(mContactUsMessageTitleET.getText().toString())) {
            if (!TextUtils.isEmpty(mContactUsMessageDescriptionET.getText().toString())) {
                contactUsPresenter.contactUs(mContactUsMessageTitleET.getText().toString(), mContactUsMessageDescriptionET.getText().toString());
            } else {
                KarbalaUtils.showToast(this, getString(R.string.contact_us_message_description_error), Constants.FANCYERROR);
            }
        } else {
            KarbalaUtils.showToast(this, getString(R.string.contact_us_message_subject_error), Constants.FANCYERROR);
        }
    }

    @OnClick(R.id.whatsapp_container)
    public void onWhatsAppClickListener(View view) {
        try {
            String url = Constants.WHATSAPP_BASE_URL + KarbalaUtils.extractSettingsByKey(Constants.WHATSAPP_KEY);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.twitter_container)
    public void onMessengerClickListener(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + KarbalaUtils.extractSettingsByKey(Constants.TWITTER_KEY))));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TWITTER_BASE_URL + KarbalaUtils.extractSettingsByKey(Constants.TWITTER_KEY))));
        }
    }

    @OnClick(R.id.mail_container)
    public void onMailClickListener(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + KarbalaUtils.extractSettingsByKey(Constants.EMAIL_KEY)));
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.website_container)
    public void onWebsiteClickListener(View view) {
        try {
            String url = KarbalaUtils.extractSettingsByKey(Constants.WEBSITE_KEY);
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.layout_hotline_container)
    public void onHotlineNumberClickListener(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + hotlineNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @Override
    public void onContactUsResult(ObjectModel<ContactUsDTO> contactUsDTOObjectModel) {
        KarbalaUtils.showToast(this, getString(R.string.contact_us_message_sent_successfuly), Constants.FANCYSUCCESS);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);
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
        KarbalaUtils.showTimeOutMessage(this);
    }

    @Override
    public void onError(int code) {
        KarbalaUtils.showErrorResult(this);
    }
}
