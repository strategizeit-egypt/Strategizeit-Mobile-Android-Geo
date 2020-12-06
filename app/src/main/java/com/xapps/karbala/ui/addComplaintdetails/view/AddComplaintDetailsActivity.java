package com.xapps.karbala.ui.addComplaintdetails.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.ComplaintDTO;
import com.xapps.karbala.model.data.dto.SubAreaDTO;
import com.xapps.karbala.model.data.dto.SubCategoryDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.addComplaintdetails.presenter.AddComplaintDetailsPresenter;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.complaintcategory.view.ComplaintCategoryActivity;
import com.xapps.karbala.ui.login.view.LoginActivity;
import com.xapps.karbala.ui.newcomplaint.view.NewComplaintFragment;
import com.xapps.karbala.ui.successfullsendreport.view.SuccessfullSendReportActivity;
import com.xapps.karbala.ui.termsandconditions.view.TermsConditionsActivity;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddComplaintDetailsActivity extends BaseActivity implements AddComplaintDetailsView {
    private AddComplaintDetailsPresenter addComplaintDetailsPresenter = new AddComplaintDetailsPresenter(this);
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_shape)
    ImageView mToolbarShape;
    @BindView(R.id.report_photos_rv)
    RecyclerView mReportPhotosRV;
    @BindView(R.id.term_cond_cb)
    CheckBox mTermsConditionsCB;
    @BindView(R.id.report_type_tv)
    TextView mComplaintSubCategoryTV;
    @BindView(R.id.audio_recorded_time_chronometer)
    Chronometer mAudioRecordedTimeChronometer;
    @BindView(R.id.add_record_container)
    LinearLayout mAddRecordedAudioLayout;
    @BindView(R.id.stop_recording_container)
    RelativeLayout mStopRecordingLayout;
    @BindView(R.id.play_recorded_audio_container)
    LinearLayout mPlayRecordedAudioLayout;
    @BindView(R.id.play_recorded_audio)
    ImageView mPlayRecordedAudioIV;
    @BindView(R.id.pause_recorded_audio)
    ImageView mPauseRecordedAudioIV;
    @BindView(R.id.recorded_audio_progressbar)
    ProgressBar mRecordedAudioProgressbar;
    @BindView(R.id.recorded_audio_duration)
    TextView mRecordedAudioDuration;
    @BindView(R.id.report_description_et)
    EditText mReportDescriptionET;
    @BindView(R.id.img_complaint_details_video_thumb)
    ImageView imgComplaintDetailsVideoThumb;
    @BindView(R.id.img_delete_recorded_video)
    ImageView imgDeleteRecordedVideo;
    @BindView(R.id.layout_add_complaint_video_thumb)
    FrameLayout layoutAddComplaintVideoThumb;

    private MediaRecorder mediaRecorder;
    private String pathSave = "";
    private File audioRecordFile;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    final private Handler handler = new Handler();
    private AudioManager audioManager;
    private boolean isRecording = false;


    private SubCategoryDTO subCategoryDTO = null;
    private SubAreaDTO subAreaDTO;
    private LinearLayoutManager mLinearLayoutManager;
    private ReportPhotosAdapter mReportPhotosAdapter;

    private double reportVoiceDuration = 0.0;

    private RequestBody reportTitle;
    private RequestBody mReportDetails;
    private RequestBody mReportTypeId;
    private RequestBody selectedSubAreaId;
    private RequestBody latitude;
    private RequestBody longitude;
    private RequestBody mReportVoiceDuration;

    private RequestBody mReportFile;
    private List<MultipartBody.Part> mReportImagesList;
    private List<MultipartBody.Part> mReportFilesList;
    private File videoFile;
    private RequestBody videoRequestBody;
    private String videoImage = " ";
    private RequestBody videoImageRequestBody;


    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = focusChange -> {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
            // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
            // our app is allowed to continue playing sound but at a lower volume. We'll treat
            // both cases the same way because our app is playing short sound files.

            // Pause playback and reset player to the start of the file. That way, we can
            // play the word from the beginning when we resume playback.
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
            mediaPlayer.start();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            // The AUDIOFOCUS_LOSS case means we've lost audio focus and
            // Stop playback and clean up resources
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint_details);
        ButterKnife.bind(this);

        initUI();

    }


    private void initUI() {

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mToolbarTitle.setText(getString(R.string.back));
        mToolbarShape.setImageResource(R.drawable.ic_shape_cosmic_latte_bg);

        subAreaDTO = new Gson().fromJson(getIntent().getStringExtra(Constants.SELECTED_SUB_AREA_DTO), SubAreaDTO.class);

        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mReportPhotosAdapter = new ReportPhotosAdapter(new ArrayList<>(), this);
        mReportPhotosRV.setLayoutManager(mLinearLayoutManager);
        mReportPhotosRV.setAdapter(mReportPhotosAdapter);

        mReportImagesList = new ArrayList<>();
        mReportFilesList = new ArrayList<>();

        mReportPhotosAdapter.setOnRemovePhotoClickListener(position -> {
            if (mReportImagesList.size() != 0)
                mReportImagesList.remove(position);
        });


    }

    @OnClick(R.id.ic_back)
    public void onBackClickListener(View view) {
        onBackPressed();
    }

    @OnClick(R.id.complaint_category_container)
    public void onComplaintCategoryLayoutClickListener(View view) {
        Intent intent = new Intent(AddComplaintDetailsActivity.this, ComplaintCategoryActivity.class);
        intent.putExtra(Constants.SELECTED_SUB_CATEGORY_DTO, new Gson().toJson(subCategoryDTO));
        startActivityForResult(intent, Constants.SELECT_CATEGORY_CODE);
    }

    @OnClick(R.id.terms_conditions_container)
    public void TermsAndConditionsClick(View view) {
        if (mTermsConditionsCB.isChecked())
            mTermsConditionsCB.setChecked(false);
        else {
            Intent intent = new Intent(AddComplaintDetailsActivity.this, TermsConditionsActivity.class);
            intent.putExtra(Constants.HIDE_AGREE_TERMS, false);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(intent, Constants.TERMS_CONDITIONS_REQUEST_CODE);
        }
    }

    @OnClick(R.id.add_photo_from_cam)
    public void OnAddPhotoFromCameraClickListener() {
        if (isRecording) {
            KarbalaUtils.showToast(this, getString(R.string.stop_recording_before_select_image_message), Constants.FANCYERROR);
            return;
        }
        selectPhotoFromCamOrGallery();
    }

    @OnClick(R.id.record_video)
    public void onRecordVideoClickListener() {
        if (isRecording) {
            KarbalaUtils.showToast(this, getString(R.string.stop_recording_before_record_video_message), Constants.FANCYERROR);
            return;
        }
        recordVideo();
    }

    private void recordVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, Constants.RECORD_VIDEO_CODE);
        }
    }


    private void selectPhotoFromCamOrGallery() {
        if (isStorageAllowed()) {
            if (mReportPhotosAdapter.getItemCount() < 5) {
                /* KarbalaUtils.uploadPhoto(this);*/
                CropImage.activity()
                        .setCropMenuCropButtonTitle(getString(R.string.save))
                        .start(this);
            } else {
                KarbalaUtils.showToast(this, getString(R.string.error_number_of_photos), Constants.FANCYWARNING);
            }

        } else {
            requestStoragePermission();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    File file = new File(resultUri.getPath());
                    mReportFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    mReportImagesList.add(MultipartBody.Part.createFormData("image", file.getName(), mReportFile));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Bitmap bm = BitmapFactory.decodeFile(resultUri.getPath());

                mReportPhotosAdapter.AddPhotoToAdapter(bm);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 25, baos); //bm is the bitmap object
                byte[] byteArrayImage = baos.toByteArray();
                try {
                    //mImageBaseString = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

                } catch (Exception e) {
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                KarbalaUtils.showToast(this, getString(R.string.error_add_photo), Constants.FANCYERROR);
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.TERMS_CONDITIONS_REQUEST_CODE) {
            mTermsConditionsCB.setChecked(data.getBooleanExtra(Constants.AGREE, false));

        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.SELECT_CATEGORY_CODE) {
            try {
                subCategoryDTO = new Gson().fromJson(data.getStringExtra(Constants.SELECTED_SUB_CATEGORY_DTO), SubCategoryDTO.class);
                if (subCategoryDTO != null) {
                    mComplaintSubCategoryTV.setText(subCategoryDTO.getName());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == Constants.RECORD_VIDEO_CODE) {
            try {
                String s = getRealPathFromURI(data.getData());
                if (s != null && !s.isEmpty()) {
                    videoFile = new File(s);
                    if (videoFile != null) {
                        layoutAddComplaintVideoThumb.setVisibility(View.VISIBLE);
                        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                        imgComplaintDetailsVideoThumb.setImageBitmap(bMap);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bMap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        videoImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    }

                } else {
                    KarbalaUtils.showToast(this, getString(R.string.err_load_video), Constants.FANCYERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @OnClick(R.id.img_delete_recorded_video)
    public void onDeleteRecordedVideoClickListener(View view) {
        layoutAddComplaintVideoThumb.setVisibility(View.GONE);
        videoFile = null;
        videoImage = " ";
    }


    public void initiateRequest() {
        try {

            reportTitle = RequestBody.create(MultipartBody.FORM, "Android");
            mReportDetails = RequestBody.create(MultipartBody.FORM, mReportDescriptionET.getText().toString());
            mReportTypeId = RequestBody.create(MultipartBody.FORM, String.valueOf(subCategoryDTO.getId()));
            selectedSubAreaId = RequestBody.create(MultipartBody.FORM, String.valueOf(subAreaDTO.getId()));
            latitude = RequestBody.create(MultipartBody.FORM, String.valueOf(NewComplaintFragment.mLatLng.latitude));
            longitude = RequestBody.create(MultipartBody.FORM, String.valueOf(NewComplaintFragment.mLatLng.longitude));
            mReportVoiceDuration = RequestBody.create(MultipartBody.FORM, String.valueOf(reportVoiceDuration));
            videoImageRequestBody = RequestBody.create(MultipartBody.FORM, videoImage);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void goToTermsAndConditions() {
        Intent intent = new Intent(AddComplaintDetailsActivity.this, TermsConditionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, Constants.TERMS_CONDITIONS_REQUEST_CODE);
    }

    private void addreCAPTHCA() {
        SafetyNet.getClient(this).verifyWithRecaptcha(Constants.CAPTCHA_KEY)
                .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        // Indicates communication with reCAPTCHA service was
                        // successful.
                        String userResponseToken = recaptchaTokenResponse.getTokenResult();
                        if (!userResponseToken.isEmpty()) {
                            // Validate the user response token using the
                            // reCAPTCHA siteverify API.
                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddComplaintDetailsActivity.this);
                            sweetAlertDialog.setCancelable(false);
                            sweetAlertDialog.setTitleText(getString(R.string.confirm))
                                    .setContentText(getString(R.string.sure_send_complaint))
                                    .setConfirmText(getString(R.string.ok))
                                    .setConfirmClickListener(sweetAlertDialog1 -> {
                                        sweetAlertDialog1.dismiss();
                                        addComplaintDetailsPresenter.addReport(reportTitle, mReportDetails, mReportTypeId, selectedSubAreaId,
                                                latitude, longitude, mReportVoiceDuration, videoImageRequestBody, mReportFilesList);
                                    })
                                    .setCancelText(getString(R.string.cancel))
                                    .setCancelClickListener(SweetAlertDialog::dismiss)
                                    .show();
                            Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
                            btn.setBackground(getResources().getDrawable(R.drawable.round_solid_blue_radius8));
                            Log.e("Register Activity", "Success");

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            // An error occurred when communicating with the
                            // reCAPTCHA service. Refer to the status code to
                            // handle the error appropriately.
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e("RegisterActivity", "Error: " + CommonStatusCodes.getStatusCodeString(statusCode));

                        } else {
                            // A different, unknown type of error occurred.
                            Log.e("RegisterActivity", "Error: " + e.getMessage());
                        }
                    }
                });
    }


    @OnClick(R.id.send_complaint)
    public void onSendReportClickListener(View view) {
        if (subCategoryDTO == null) {
            KarbalaUtils.showToast(this, getString(R.string.choose_complaint_category_error_message), Constants.FANCYERROR);
            return;
        }
        if (TextUtils.isEmpty(mReportDescriptionET.getText().toString()) && pathSave.contentEquals("")) {
            KarbalaUtils.showToast(this, getString(R.string.complaint_details_audio_error_message), Constants.FANCYERROR);
            return;
        }
        /*if (!mTermsConditionsCB.isChecked()) {
            goToTermsAndConditions();
            return;
        }*/
        initiateRequest();
        if (isRecording) {
            KarbalaUtils.showToast(this, getString(R.string.stop_recording_message), Constants.FANCYERROR);
            return;
        }

        mReportFilesList = new ArrayList<>();
        if (mReportImagesList.size() != 0)
            mReportFilesList.addAll(mReportImagesList);
        if (!pathSave.contentEquals("")) {
            mReportFile = RequestBody.create(MediaType.parse("multipart/form-data"), audioRecordFile);
            mReportFilesList.add(MultipartBody.Part.createFormData("audio", audioRecordFile.getName(), mReportFile));
        }
        if (videoFile != null) {
            videoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
            mReportFilesList.add(MultipartBody.Part.createFormData("video", videoFile.getName(), videoRequestBody));

        }
        Log.v("LatLng", NewComplaintFragment.mLatLng.toString());
        sendComplaint();
    }

    public void sendComplaint() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddComplaintDetailsActivity.this);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText(getString(R.string.confirm))
                .setContentText(getString(R.string.sure_send_complaint))
                .setConfirmText(getString(R.string.ok))
                .setConfirmClickListener(sweetAlertDialog1 -> {
                    sweetAlertDialog1.dismiss();
                    addComplaintDetailsPresenter.addReport(reportTitle, mReportDetails, mReportTypeId, selectedSubAreaId,
                            latitude, longitude, mReportVoiceDuration, videoImageRequestBody, mReportFilesList);
                })
                .setCancelText(getString(R.string.cancel))
                .setCancelClickListener(SweetAlertDialog::dismiss)
                .show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.round_solid_blue_radius8));
        Log.e("Register Activity", "Success");
    }

    @Override
    public void onAddReportDetailsResult(ObjectModel<ComplaintDTO> reportDTOObjectModel) {
        Intent intent = new Intent(AddComplaintDetailsActivity.this, SuccessfullSendReportActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.add_record_container)
    public void OnAddRecordClick() {
        if (!isMicrophoneAndStorageAllowed()) {
            // request storage and microphone
            requestMicrophoneAndStoragePermission();
        } else {
            if (!isMicrophoneAllowed()) {
                // request only mic
                requestMicrophonePermission();
            } else if (!isStorageAllowed()) {
                // request only storage
                requestStoragePermissionForRecord();
            } else
                recordAudio();
        }
    }

    private void recordAudio() {
        mAddRecordedAudioLayout.setVisibility(View.GONE);
        mStopRecordingLayout.setVisibility(View.VISIBLE);
        mPlayRecordedAudioLayout.setVisibility(View.GONE);
        mAudioRecordedTimeChronometer.setVisibility(View.VISIBLE);
        mAudioRecordedTimeChronometer.setBase(SystemClock.elapsedRealtime());
        mAudioRecordedTimeChronometer.start();
        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/"
                + "Voice-"
                + getDate() + ".mp3";
        setupMediaRecorder();
        audioRecordFile = new File(pathSave);
        try {
            isRecording = true;
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {

        }

    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(pathSave);

    }

    public String getDate() {
        Date date = new Date();
        String strDateFormat = "dd_MM_yyyy_hh_mm_ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.ENGLISH);
        return dateFormat.format(date);
    }


    @OnClick(R.id.stop_recording_container)
    public void onStopRecordClick() {
        isRecording = false;
        reportVoiceDuration = Double.parseDouble(KarbalaUtils.arabicToDecimal(mAudioRecordedTimeChronometer.getText().toString()).replace(":", "."));
        mRecordedAudioDuration.setText(mAudioRecordedTimeChronometer.getText().toString());
        mAddRecordedAudioLayout.setVisibility(View.VISIBLE);
        mStopRecordingLayout.setVisibility(View.GONE);
        mPlayRecordedAudioLayout.setVisibility(View.VISIBLE);
        mAudioRecordedTimeChronometer.setVisibility(View.GONE);
        mAudioRecordedTimeChronometer.stop();
        mAudioRecordedTimeChronometer.setBase(SystemClock.elapsedRealtime());

        mediaRecorder.stop();
    }

    @OnClick(R.id.play_recorded_audio)
    public void onPlayRecordedAudio() {

        mPlayRecordedAudioIV.setVisibility(View.GONE);
        mPauseRecordedAudioIV.setVisibility(View.VISIBLE);
        if (!pathSave.contentEquals("")) {
//            releaseMediaPlayer();
            int result = audioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(pathSave);
                    mediaPlayer.prepareAsync();
                } catch (Exception e) {

                }

                mediaPlayer.setOnPreparedListener(mp -> {
                    mRecordedAudioProgressbar.setMax(mp.getDuration());
                    if (!mp.isPlaying()) {
                        mPlayRecordedAudioIV.setVisibility(View.GONE);
                        mPauseRecordedAudioIV.setVisibility(View.VISIBLE);
                        mp.start();
                        updateProgressBar();
                    }
                });
                // crash
                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    KarbalaUtils.hideLoader();
                    return true;
                });


                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.seekTo(0);
                    mPlayRecordedAudioIV.setVisibility(View.VISIBLE);
                    mPauseRecordedAudioIV.setVisibility(View.GONE);
                    mAudioRecordedTimeChronometer.stop();
                    mAudioRecordedTimeChronometer.setBase(SystemClock.elapsedRealtime());
                });


            }
        }
    }

    public void updateProgressBar() {
        mRecordedAudioProgressbar.setProgress(mediaPlayer.getCurrentPosition());
        if (!mediaPlayer.isPlaying()) {
            mPauseRecordedAudioIV.setVisibility(View.GONE);
            mPlayRecordedAudioIV.setVisibility(View.VISIBLE);
        }
        if (mediaPlayer.isPlaying()) {
            Runnable updater = this::updateProgressBar;
            handler.postDelayed(updater, 1000);
        } else {
            mPlayRecordedAudioIV.setVisibility(View.VISIBLE);
            mPauseRecordedAudioIV.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.pause_recorded_audio)
    public void onStopPlayIngRecordedAudio() {
        mediaPlayer.pause();
        mPlayRecordedAudioIV.setVisibility(View.VISIBLE);
        mPauseRecordedAudioIV.setVisibility(View.GONE);
        mAudioRecordedTimeChronometer.stop();
        mAudioRecordedTimeChronometer.setBase(SystemClock.elapsedRealtime());

    }

    @OnClick(R.id.delete_recorded_audio)
    public void onDeleteRecordedAudio() {
        pathSave = "";
        reportVoiceDuration = 0.0;
        mPlayRecordedAudioLayout.setVisibility(View.GONE);
        mediaPlayer.seekTo(0);
        mediaPlayer.stop();
        mAudioRecordedTimeChronometer.stop();
        mAudioRecordedTimeChronometer.setBase(SystemClock.elapsedRealtime());
        mPlayRecordedAudioIV.setVisibility(View.VISIBLE);
        mPauseRecordedAudioIV.setVisibility(View.GONE);
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            //mediaPlayer = null;
            audioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    private boolean isMicrophoneAndStorageAllowed() {
        if (

                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestMicrophoneAndStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.permission_denied_message))
                    .setPositiveButton(getResources().getString(R.string.dialog_ok),
                            (dialog, which) -> ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.RECORD_AUDIO,
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    },
                                    Constants.REQUEST_RECORD_AND_STORAGE_PERMISSION_CODE))
                    .setNegativeButton(getResources().getString(R.string.dialog_cancel), null)
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    Constants.REQUEST_RECORD_AND_STORAGE_PERMISSION_CODE);
        }
    }

    private boolean isMicrophoneAllowed() {
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    private void requestMicrophonePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.permission_denied))
                    .setMessage(getResources().getString(R.string.record_permission_denied_message))
                    .setPositiveButton(getResources().getString(R.string.dialog_ok),
                            (dialog, which) -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                                    Constants.REQUEST_MICROPHONE_PERMISSION_CODE))
                    .setNegativeButton(getResources().getString(R.string.dialog_cancel), null)
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    Constants.REQUEST_MICROPHONE_PERMISSION_CODE);
        }
    }

    private void requestStoragePermissionForRecord() {
        // in case record permission granted and want storage then will record
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.permission_denied))
                    .setMessage(getResources().getString(R.string.storage_permission_denied_message))
                    .setPositiveButton(getResources().getString(R.string.dialog_ok),
                            (dialog, which) -> ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constants.REQUEST_STORAGE_PERMISSION_FOR_RECORD_CODE))
                    .setNegativeButton(getResources().getString(R.string.dialog_cancel), null)
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_STORAGE_PERMISSION_FOR_RECORD_CODE);
        }

    }

    private boolean isStorageAllowed() {
        if (
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.permission_denied))
                    .setMessage(getResources().getString(R.string.storage_permission_denied_message))
                    .setPositiveButton(getResources().getString(R.string.dialog_ok),
                            (dialog, which) -> ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constants.REQUEST_STORAGE_PERMISSION_CODE))
                    .setNegativeButton(getResources().getString(R.string.dialog_cancel), null)
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_STORAGE_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                        CropImage.activity()
                                .setCropMenuCropButtonTitle(getString(R.string.save))
                                .start(this);
                        //KarbalaUtils.uploadPhoto(this);
                    } else {
                        KarbalaUtils.showToast(this, getResources().getString(R.string.error), Constants.FANCYERROR);
                    }
                } else {
                    KarbalaUtils.showToast(this, getResources().getString(R.string.error), Constants.FANCYERROR);
                }
                return;
            }
            case Constants.REQUEST_RECORD_AND_STORAGE_PERMISSION_CODE: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                            == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                        recordAudio();
                    } else {
                        KarbalaUtils.showToast(this, getResources().getString(R.string.error), Constants.FANCYERROR);

                    }
                } else {
                    KarbalaUtils.showToast(this, getResources().getString(R.string.error), Constants.FANCYERROR);

                }
                return;
            }
            // in case have storage permission granted and microphone denied
            case Constants.REQUEST_MICROPHONE_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.RECORD_AUDIO)
                            == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                        recordAudio();
                    } else {
                        KarbalaUtils.showToast(this, getResources().getString(R.string.error), Constants.FANCYERROR);

                    }
                } else {
                    KarbalaUtils.showToast(this, getResources().getString(R.string.error), Constants.FANCYERROR);
                }
                return;
            }
            case Constants.REQUEST_STORAGE_PERMISSION_FOR_RECORD_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                                    == PackageManager.PERMISSION_GRANTED) {
                        recordAudio();
                    } else {
                        KarbalaUtils.showToast(this, getResources().getString(R.string.error), Constants.FANCYERROR);

                    }
                } else {
                    KarbalaUtils.showToast(this, getResources().getString(R.string.error), Constants.FANCYERROR);
                }
                return;
            }
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
        KarbalaUtils.showLoader(this, getString(R.string.loading));
    }

    @Override
    public void onTimeOut() {
        KarbalaUtils.showTimeOutMessage(this);
    }

    @Override
    public void onError(int code) {
        if (code == 2) {
            KarbalaUtils.showToast(this, getString(R.string.err_relogin), Constants.FANCYERROR);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(AddComplaintDetailsActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }, 500);
        } else {
            KarbalaUtils.showErrorResult(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }


}
