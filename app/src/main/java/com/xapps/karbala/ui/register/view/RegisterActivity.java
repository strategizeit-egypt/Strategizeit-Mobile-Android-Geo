package com.xapps.karbala.ui.register.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.AreaDTO;
import com.xapps.karbala.model.data.dto.CountryDTO;
import com.xapps.karbala.model.data.dto.GovernorateDTO;
import com.xapps.karbala.model.data.dto.LoginDTO;
import com.xapps.karbala.model.data.dto.MetaDataDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.register.presenter.RegisterPresenter;
import com.xapps.karbala.ui.verify.view.VerificationActivity;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;
import com.xapps.karbala.utils.LocalHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends BaseActivity implements RegisterView {
    @BindView(R.id.txt_citizen_full_name)
    EditText txtCitizenFullName;
    @BindView(R.id.txt_citizen_email)
    EditText txtCitizenEmail;
    @BindView(R.id.txt_dob)
    TextView txtDob;
    @BindView(R.id.et_citizen_city)
    EditText etCitizenCity;
    @BindView(R.id.et_citizen_neighborhood)
    EditText etCitizenNeighborhood;
    @BindView(R.id.layout_dob)
    LinearLayout layoutDob;
    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
    private RegisterPresenter registerPresenter = new RegisterPresenter(this);
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.layout_phone)
    LinearLayout layoutPhone;
    String lang;
    @BindView(R.id.toolbar_shape)
    ImageView toolbarShape;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rg_user_type)
    RadioGroup RGuserType;
    @BindView(R.id.layout_citizen_address)
    LinearLayout layoutCitizenAddress;
    @BindView(R.id.spinner_citizen_area)
    Spinner spinnerCitizenArea;
    @BindView(R.id.layout_visitor_address)
    LinearLayout layoutVisitorAddress;
    @BindView(R.id.spinner_visitor_country)
    Spinner spinnerVisitorCountry;
    @BindView(R.id.spinner_visitor_govern)
    Spinner spinner_visitor_govern;
    @BindView(R.id.layout_visitor_govern)
    LinearLayout layoutVisitorGovern;
    @BindView(R.id.layout_visitor_country)
    LinearLayout layoutVisitorCountry;


    @BindView(R.id.txt_sign_up)
    TextView txtSignUp;
    @BindView(R.id.layout_citizen_area)
    LinearLayout layoutCitizenArea;
    @BindView(R.id.spinner_country_code)
    CountryCodePicker countryCodePicker;
    private MetaDataDTO metaDataDTO;
    List<AreaDTO> areaDTOList;
    List<CountryDTO> countryDTOList;
    List<GovernorateDTO> iraqGovernsList;
    private long selectedCitizenMunicipalId = 0;
    private long selectedVisitorMunicipalId = 0;
    private int selectedGenderId = 0;
    // select iraq by default
    private long selectedCountryId = 0;
    private String dob;


    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setBirthdate();


    };

    private void setBirthdate() {
        String myFormat = "MM / dd / yyyy";
        String myformat2 = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat(myformat2, Locale.ENGLISH);
        txtDob.setText(sdf.format(myCalendar.getTime()));
        dob = sdf2.format(myCalendar.getTime());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        lang = LocalHelper.getLanguage(this);

        initUI();

    }

    private void initUI() {
        if (lang.contentEquals("ar")) {
            layoutCitizenArea.setBackground(getResources().getDrawable(R.drawable.right_round_border_grey_radius20));
            etCitizenNeighborhood.setBackground(getResources().getDrawable(R.drawable.left_round_border_grey_radius20));
        } else {
            layoutCitizenArea.setBackground(getResources().getDrawable(R.drawable.left_round_border_grey_radius20));
            etCitizenNeighborhood.setBackground(getResources().getDrawable(R.drawable.right_round_border_grey_radius20));
        }

        toolbarTitle.setText(getString(R.string.back));
        toolbarShape.setImageResource(R.drawable.ic_shape_cosmic_latte_bg);

        RGuserType.check(R.id.rb_citizen);
        RGuserType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_citizen) {
                layoutCitizenAddress.setVisibility(View.VISIBLE);
                layoutVisitorAddress.setVisibility(View.GONE);
                txtSignUp.setText(getString(R.string.title_sign_up_as_citizen));

            } else {
                layoutCitizenAddress.setVisibility(View.GONE);
                layoutVisitorAddress.setVisibility(View.VISIBLE);
                txtSignUp.setText(getString(R.string.title_sign_up_as_visitor));
            }
        });
        rgGender.check(R.id.rb_male);
        selectedGenderId = 1;
        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_male) {
                selectedGenderId = 1;

            } else {
                selectedGenderId = 2;
            }
        });

        metaDataDTO = registerPresenter.getMetaData();
        if (metaDataDTO != null) {
            areaDTOList = new ArrayList<>();
            areaDTOList.add(new AreaDTO(0L, getString(R.string.title_area), getString(R.string.title_area)));
            areaDTOList.addAll(1, metaDataDTO.getAreaDTOList());
            setAreaSpinner();
            countryDTOList = new ArrayList<>();
            countryDTOList.add(new CountryDTO(getString(R.string.title_country), getString(R.string.title_country), 0L));
            countryDTOList.addAll(metaDataDTO.getCountryDTOList());
            setCountrySpinner();
        }


        txtCitizenFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                highlightInput();
            }
        });
        txtCitizenEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                highlightInput();
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                highlightInput();
            }
        });
        txtDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                highlightInput();
            }
        });
        etCitizenCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                highlightInput();
            }
        });
        etCitizenNeighborhood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                highlightInput();
            }
        });
    }

    @OnClick(R.id.layout_dob)
    public void onLayoutDobClicked() {
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setAreaSpinner() {
        //Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<AreaDTO> areaSpinnerAdapter = new ArrayAdapter<AreaDTO>(this, R.layout.spinner_item_view
                , areaDTOList) {
            @Override
            public boolean isEnabled(int position) {
                if (position > 0)
                    return true;
                return false;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        areaSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item_view);
        spinnerCitizenArea.setAdapter(areaSpinnerAdapter);
        spinnerCitizenArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    AreaDTO areaDTO = (AreaDTO) spinnerCitizenArea.getSelectedItem();
                    selectedCitizenMunicipalId = areaDTO.getId();
                    highlightInput();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setCountrySpinner() {
        //Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CountryDTO> countrySpinnerAdapter = new ArrayAdapter<CountryDTO>(this, R.layout.spinner_item_view
                , countryDTOList) {
            @Override
            public boolean isEnabled(int position) {
                if (position > 0)
                    return true;
                return false;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        countrySpinnerAdapter.setDropDownViewResource(R.layout.spinner_item_view);
        spinnerVisitorCountry.setAdapter(countrySpinnerAdapter);
        spinnerVisitorCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVisitorMunicipalId = 0;

                if (position > 0) {
                    layoutVisitorCountry.setBackground(getResources().getDrawable(R.drawable.round_border_grey_radius20));
                    CountryDTO countryDTO = (CountryDTO) spinnerVisitorCountry.getSelectedItem();
                    selectedCountryId = countryDTO.getId();
                    if (selectedCountryId == 1L) {
                        iraqGovernsList = new ArrayList<>();
                        iraqGovernsList.addAll(metaDataDTO.getGovernorateDTOList());
                        iraqGovernsList.add(0, new GovernorateDTO(getString(R.string.title_govern), getString(R.string.title_govern), 0L));
                        layoutVisitorGovern.setBackground(getResources().getDrawable(R.drawable.round_border_red_radius20));

                        setGovernsSpinner();
                        layoutVisitorGovern.setVisibility(View.VISIBLE);
                    } else {
                        layoutVisitorGovern.setVisibility(View.GONE);
                        iraqGovernsList = new ArrayList<>();
                    }
                } else {
                    selectedCountryId = 0;
                    layoutVisitorGovern.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountryId = 0;
                layoutVisitorGovern.setVisibility(View.GONE);


            }
        });
    }

    public void setGovernsSpinner() {
        //Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<GovernorateDTO> iraqGovernsSpinnerAdapter = new ArrayAdapter<GovernorateDTO>(this, R.layout.spinner_item_view
                , iraqGovernsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position > 0)
                    return true;
                return false;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        iraqGovernsSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item_view);
        spinner_visitor_govern.setAdapter(iraqGovernsSpinnerAdapter);
        spinner_visitor_govern.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    GovernorateDTO governorateDTO = (GovernorateDTO) spinner_visitor_govern.getSelectedItem();
                    selectedVisitorMunicipalId = governorateDTO.getId();
                    layoutVisitorGovern.setBackground(getResources().getDrawable(R.drawable.round_border_grey_radius20));
                } else {
                    selectedVisitorMunicipalId = 0;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedVisitorMunicipalId = 0;
            }
        });
    }


    @OnClick(R.id.txt_register)
    public void onRegisterClicked() {
        highlightInput();
        if (validateInput()) {
            String phoneNumber = KarbalaUtils.validatePhoneNumber(countryCodePicker.getSelectedCountryCode(), etPhone.getText().toString());
            if (RGuserType.getCheckedRadioButtonId() == R.id.rb_citizen) {
                registerPresenter.citizenRegister(phoneNumber.replace(" ", "").trim(),
                        txtCitizenFullName.getText().toString(), txtCitizenEmail.getText().toString(), 2,
                        etCitizenCity.getText().toString(), etCitizenNeighborhood.getText().toString(),
                        dob, selectedGenderId, selectedCitizenMunicipalId, 1);
            } else {
                registerPresenter.visitorRegister(phoneNumber.replace(" ", "").trim(),
                        txtCitizenFullName.getText().toString(), 1, dob, selectedGenderId,
                        selectedCountryId, selectedVisitorMunicipalId);
            }
        }
    }

    public void highlightInput() {
        if (txtCitizenFullName.getText().toString().contentEquals("")) {
            txtCitizenFullName.setBackground(getResources().getDrawable(R.drawable.round_border_red_radius20));
        } else {
            txtCitizenFullName.setBackground(getResources().getDrawable(R.drawable.round_border_grey_radius20));
        }
        if (txtCitizenEmail.getText().toString().contentEquals("")) {
            txtCitizenEmail.setBackground(getResources().getDrawable(R.drawable.round_border_red_radius20));
        } else {
            txtCitizenEmail.setBackground(getResources().getDrawable(R.drawable.round_border_grey_radius20));
        }
        if (etPhone.getText().toString().contentEquals("")) {
            layoutPhone.setBackground(getResources().getDrawable(R.drawable.round_border_red_radius20));
        } else {
            layoutPhone.setBackground(getResources().getDrawable(R.drawable.round_border_grey_radius20));
        }
        if (txtDob.getText().toString().contentEquals("")) {
            layoutDob.setBackground(getResources().getDrawable(R.drawable.round_border_red_radius20));
        } else {
            layoutDob.setBackground(getResources().getDrawable(R.drawable.round_border_grey_radius20));
        }
        if (selectedCitizenMunicipalId == 0) {
            if (lang.contentEquals("ar")) {
                layoutCitizenArea.setBackground(getResources().getDrawable(R.drawable.right_round_border_red_radius20));
            } else {
                layoutCitizenArea.setBackground(getResources().getDrawable(R.drawable.left_round_border_red_radius20));
            }
        } else {
            if (lang.contentEquals("ar")) {
                layoutCitizenArea.setBackground(getResources().getDrawable(R.drawable.right_round_border_grey_radius20));
            } else {
                layoutCitizenArea.setBackground(getResources().getDrawable(R.drawable.left_round_border_grey_radius20));
            }
        }
        if (etCitizenCity.getText().toString().contentEquals("")) {
            etCitizenCity.setBackground(getResources().getDrawable(R.drawable.border_red));
        } else {
            etCitizenCity.setBackground(getResources().getDrawable(R.drawable.border_grey));

        }
        if (etCitizenNeighborhood.getText().toString().contentEquals("")) {
            if (lang.contentEquals("ar")) {
                etCitizenNeighborhood.setBackground(getResources().getDrawable(R.drawable.left_round_border_red_radius20));
            } else {
                etCitizenNeighborhood.setBackground(getResources().getDrawable(R.drawable.right_round_border_red_radius20));
            }
        } else {
            if (lang.contentEquals("ar")) {
                etCitizenNeighborhood.setBackground(getResources().getDrawable(R.drawable.left_round_border_grey_radius20));
            } else {
                etCitizenNeighborhood.setBackground(getResources().getDrawable(R.drawable.right_round_border_grey_radius20));
            }
        }

        if (selectedCountryId == 0) {
            layoutVisitorCountry.setBackground(getResources().getDrawable(R.drawable.round_border_red_radius20));
        } else {
            layoutVisitorCountry.setBackground(getResources().getDrawable(R.drawable.round_border_grey_radius20));
        }

    }

    public boolean validateInput() {
        if (txtCitizenFullName.getText().toString().contentEquals("")) {
            KarbalaUtils.showToast(this, getString(R.string.err_citizen_name), Constants.FANCYERROR);
            return false;
        }
        if (txtCitizenEmail.getText().toString().contentEquals("")) {
            KarbalaUtils.showToast(this, getString(R.string.err_email_empty), Constants.FANCYERROR);
            return false;
        }
        if (etPhone.getText().toString().contentEquals("")) {
            KarbalaUtils.showToast(this, getString(R.string.err_phone_empty), Constants.FANCYERROR);
            return false;
        } else {
            if (KarbalaUtils.validatePhoneNumber(countryCodePicker.getSelectedCountryCode(), etPhone.getText().toString()).isEmpty()) {
                KarbalaUtils.showToast(this, getString(R.string.err_phone_wrong), Constants.FANCYERROR);
                return false;
            }
        }
        if (txtDob.getText().toString().contentEquals("")) {
            KarbalaUtils.showToast(this, getString(R.string.err_birthdate_empty), Constants.FANCYERROR);
            return false;
        }
        if (RGuserType.getCheckedRadioButtonId() == R.id.rb_citizen) {
            if (selectedCitizenMunicipalId == 0) {
                KarbalaUtils.showToast(this, getString(R.string.err_district_empty), Constants.FANCYERROR);
                return false;
            }
            if (etCitizenCity.getText().toString().contentEquals("")) {
                KarbalaUtils.showToast(this, getString(R.string.err_sub_district_empty), Constants.FANCYERROR);
                return false;
            }
            if (etCitizenNeighborhood.getText().toString().contentEquals("")) {
                KarbalaUtils.showToast(this, getString(R.string.err_hay_empty), Constants.FANCYERROR);
                return false;
            }
        } else {
            if (selectedCountryId == 0) {
                return false;
            } else if (selectedCountryId == 1 && selectedVisitorMunicipalId == 0) {
                KarbalaUtils.showToast(this, getString(R.string.err_govern_empty), Constants.FANCYERROR);
                return false;
            }

        }

        return true;
    }

    @Override
    public void onRegisterResult(ObjectModel<LoginDTO> loginDTOObjectModel) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText(getString(R.string.msg_successfull_rigster))
                .setContentText(getString(R.string.msg_user_number) + " " + loginDTOObjectModel.getModel().getId())
                .setConfirmText(getString(R.string.ok))
                .setConfirmClickListener(sweetAlertDialog1 -> {
                    sweetAlertDialog1.dismiss();
                    Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                    intent.putExtra(Constants.PHONE_NUMBER, loginDTOObjectModel.getModel().getPhoneNumber());
                    startActivity(intent);
                }).show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.round_solid_blue_radius8));

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
        KarbalaUtils.showTimeOutMessage(this);
    }

    @Override
    public void onError(int code) {
        if (code == 1) {
            KarbalaUtils.showToast(this, getString(R.string.phone_registered_before), Constants.FANCYERROR);
            return;
        }
        if (code == 2) {
            KarbalaUtils.showToast(this, getString(R.string.no_data_found), Constants.FANCYERROR);
            return;
        }
        if (code == 5) {
            KarbalaUtils.showToast(this, getString(R.string.invalid_email), Constants.FANCYERROR);
            return;
        }
        if (code == 10 || code == 13 || code == 14) {
            KarbalaUtils.showToast(this, getString(R.string.invalid_phone), Constants.FANCYERROR);
            return;
        }
        if (code == 17) {
            KarbalaUtils.showToast(this, getString(R.string.wrong_data), Constants.FANCYERROR);
            return;
        }
        if (code == 20) {
            KarbalaUtils.showToast(this, getString(R.string.invalid_phone_email), Constants.FANCYERROR);
            return;
        }
        if (code == 31) {
            KarbalaUtils.showToast(this, getString(R.string.user_not_registered), Constants.FANCYERROR);
            return;
        }

        KarbalaUtils.showErrorResult(this);

    }


}
