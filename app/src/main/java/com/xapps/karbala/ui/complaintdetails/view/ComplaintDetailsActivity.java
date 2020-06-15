package com.xapps.karbala.ui.complaintdetails.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.ComplaintDTO;
import com.xapps.karbala.model.data.dto.ComplaintFilesDTO;
import com.xapps.karbala.model.data.dto.generic.ObjectModel;
import com.xapps.karbala.ui.base.view.BaseActivity;
import com.xapps.karbala.ui.complaintdetails.imageViewer.ImageViewerFragment;
import com.xapps.karbala.ui.complaintdetails.presenter.ComplaintDetailsPresenter;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComplaintDetailsActivity extends BaseActivity implements ComplaintDetailsView, Player.EventListener {

    private ComplaintDetailsPresenter complaintDetailsPresenter = new ComplaintDetailsPresenter(this);

    @BindView(R.id.img_play_complaint_details_voice)
    ImageView imgPlayComplaintDetailsVoice;
    @BindView(R.id.img_pause_complaint_details_voice)
    ImageView imgPauseComplaintDetailsVoice;
    @BindView(R.id.progress_complaint_details_voice)
    ProgressBar progressComplaintDetailsVoice;
    @BindView(R.id.text_report_complaint_voice_duration)
    TextView textComplaintDetailsVoiceDuration;
    @BindView(R.id.view_complaint_details_status_color)
    View viewComplaintDetailsStatusColor;
    @BindView(R.id.text_complaint_details_status)
    TextView textComplaintDetailsStatus;
    @BindView(R.id.text_complaint_details_type)
    TextView textComplaintDetailsType;
    @BindView(R.id.text_complaint_details_title)
    TextView textComplaintDetailsTitle;
    @BindView(R.id.layout_complaint_details)
    NestedScrollView nestedComplaintDetails;
    @BindView(R.id.text_complaint_details_date)
    TextView textComplaintDetailsDate;
    @BindView(R.id.text_complaint_details_description)
    TextView textComplaintDetailsDescription;
    @BindView(R.id.layout_complaint_details_photos)
    LinearLayout layoutComplaintDetailsPhotos;
    @BindView(R.id.layout_complaint_details_voice)
    LinearLayout layoutComplaintDetailsVoice;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recycler_complaint_details_photos)
    RecyclerView mComplaintDetailsPhotosRV;
    @BindView(R.id.empty_view)
    ConstraintLayout mEmptyView;
    @BindView(R.id.network_error_view)
    ConstraintLayout mNetworkErrorView;
    @BindView(R.id.noDataMessage)
    TextView noDataMessage;
    @BindView(R.id.video_view)
    PlayerView playerView;
    @BindView(R.id.img_play_video)
    ImageView imgPlayVideo;
    @BindView(R.id.layout_complaint_details_video)
    LinearLayout layoutComplaintDetailsVideo;
    @BindView(R.id.img_video_image)
    ImageView imgVideoImage;
    @BindView(R.id.text_complaint_details_comment_layout)
    LinearLayout textComplaintDetailsCommentLayout;
    @BindView(R.id.text_complaint_details_comment_date)
    TextView textComplaintDetailsCommentDate;
    @BindView(R.id.text_complaint_details_comment_description)
    TextView texComplaintDetailsCommentDescription;
    private SimpleExoPlayer player;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private boolean videoIsPlaying = false;

    private ComplaintDTO complaintDTO;
    private long mComplaintId;

    private ComplaintDetailPhotosAdapter mComplaintDetailPhotosAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<ComplaintFilesDTO> mComplaintDetailsImagesList;
    private String mComplaintDetailsVoice;
    private String mComplaintDetailsVideo;

    // media player
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private AudioManager audioManager;
    private final Handler handler = new Handler();


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
            releaseAudioMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details);
        ButterKnife.bind(this);

        initUI();


    }

    private void initUI() {
        toolbarTitle.setText(getString(R.string.complaint_details_hint));
        noDataMessage.setText(getString(R.string.no_complaint_details));

        nestedComplaintDetails.setVisibility(View.GONE);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);


        mComplaintDetailPhotosAdapter = new ComplaintDetailPhotosAdapter(this, new ArrayList<>());
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mComplaintDetailsPhotosRV.setLayoutManager(mLinearLayoutManager);
        mComplaintDetailsPhotosRV.setAdapter(mComplaintDetailPhotosAdapter);

        mComplaintDetailsImagesList = new ArrayList<>();
        mComplaintDetailsVoice = "";
        mComplaintDetailsVideo = "";

        mComplaintId = getIntent().getLongExtra(Constants.COMPLAINT_ID, 0L);
        complaintDetailsPresenter.getReportDetails(mComplaintId);

        mComplaintDetailPhotosAdapter.setOnImageClickListener(imagePath -> {
            ImageViewerFragment imageViewerFragment = ImageViewerFragment.newInstance(imagePath);
            imageViewerFragment.show(getSupportFragmentManager(), "image");
        });
    }


    @Override
    public void onGetComplaintDetailsResult(ObjectModel<ComplaintDTO> complaintDTOObjectModel) {
        complaintDTO = complaintDTOObjectModel.getModel();
        if (complaintDTO != null) {
            try {
                nestedComplaintDetails.setVisibility(View.VISIBLE);
                textComplaintDetailsStatus.setText(complaintDTO.getReportStatusName());
                if (complaintDTO.getComplaintLogDTOList()!=null && complaintDTO.getComplaintLogDTOList().size()!=0){
                    textComplaintDetailsCommentLayout.setVisibility(View.VISIBLE);
                    textComplaintDetailsCommentDate.setText(KarbalaUtils.formatDate(complaintDTO.getComplaintLogDTOList().get(0).getCreationdate(), "dd MMM yyyy hh:mm a"));
                    texComplaintDetailsCommentDescription.setText(complaintDTO.getComplaintLogDTOList().get(0).getCommentforuser());
                }else {
                    textComplaintDetailsCommentLayout.setVisibility(View.GONE);
                }
                textComplaintDetailsType.setText(complaintDTO.getReportTypeName());
                textComplaintDetailsTitle.setText(complaintDTO.getDetails());
                textComplaintDetailsDate.setText(KarbalaUtils.formatDate(complaintDTO.getCreationDate(), "dd MMM yyyy"));
                textComplaintDetailsDescription.setText(complaintDTO.getDetails());
                extractFiles(complaintDTO.getComplaintFileDTOS());
                if (mComplaintDetailsImagesList.size() == 0)
                    layoutComplaintDetailsPhotos.setVisibility(View.GONE);
                else
                    mComplaintDetailPhotosAdapter.addPhotosToAdapter(mComplaintDetailsImagesList);

                if (!mComplaintDetailsVoice.contentEquals("")) {
                    layoutComplaintDetailsVoice.setVisibility(View.VISIBLE);
                    //mediaMetadataRetriever = new MediaMetadataRetriever();
                    //mediaMetadataRetriever.setDataSource(mReportDetailsVoice, new HashMap<>());
                    extractVoiceDuration(complaintDTO.getDuration());
                } else {
                    layoutComplaintDetailsVoice.setVisibility(View.GONE);

                }

                if (!mComplaintDetailsVideo.isEmpty()) {
                    layoutComplaintDetailsVideo.setVisibility(View.VISIBLE);
                } else {
                    layoutComplaintDetailsVideo.setVisibility(View.GONE);
                }

                if (complaintDTO.getVideoImage() != null) {
                    Picasso.get()
                            .load(Constants.BASE_URL_VIDEO_IMAGES + complaintDTO.getVideoImage())
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder)
                            .into(imgVideoImage);
                }
                try {
                    textComplaintDetailsStatus.setTextColor(Color.parseColor(complaintDTO.getColor().trim()));
                    viewComplaintDetailsStatusColor.setBackgroundColor(Color.parseColor(complaintDTO.getColor().trim()));

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            // hide view and show empty or network view error
            handleUiEmptyErrorView(0);
        }
    }

    private void extractFiles(List<ComplaintFilesDTO> complaintFileDTOS) {
        try {

            for (ComplaintFilesDTO complaintFilesDTO1 : complaintFileDTOS) {
                if (complaintFilesDTO1.getFileTypeId() == 1) {
                    mComplaintDetailsImagesList.add(complaintFilesDTO1);
                } else if (complaintFilesDTO1.getFileTypeId() == 2) {
                    mComplaintDetailsVoice = Constants.BASE_URL_COMPLAINT_FILES + complaintFilesDTO1.getReportId() + "/" + complaintFilesDTO1.getFilePath();

                } else if (complaintFilesDTO1.getFileTypeId() == 3) {
                    mComplaintDetailsVideo = Constants.BASE_URL_COMPLAINT_FILES + complaintFilesDTO1.getReportId() + "/" + complaintFilesDTO1.getFilePath();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.img_play_complaint_details_voice)
    public void onPlayRecordedAudio() {
        if (player != null) {
            videoIsPlaying = false;
            imgPlayVideo.setVisibility(View.VISIBLE);
            imgVideoImage.setVisibility(View.VISIBLE);
            playerView.setPlayer(null);
            releaseVideoPlayer();
        }
        imgPlayComplaintDetailsVoice.setVisibility(View.GONE);
        imgPauseComplaintDetailsVoice.setVisibility(View.VISIBLE);
        if (!mComplaintDetailsVoice.contentEquals("")) {
//            releaseMediaPlayer();
            int result = audioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(mComplaintDetailsVoice);
                    onShowLoader();
                    mediaPlayer.prepareAsync();
                } catch (Exception e) {

                }

                mediaPlayer.setOnPreparedListener(mp -> {
                    progressComplaintDetailsVoice.setMax(mp.getDuration());
                    if (!mp.isPlaying()) {
                        imgPlayComplaintDetailsVoice.setVisibility(View.GONE);
                        imgPauseComplaintDetailsVoice.setVisibility(View.VISIBLE);
                        mp.start();
                        onDismissLoader();
                        updateProgressBar();
                    }
                });
                // crash
                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    onDismissLoader();
                    return true;
                });


                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.seekTo(0);
                    imgPlayComplaintDetailsVoice.setVisibility(View.VISIBLE);
                    imgPauseComplaintDetailsVoice.setVisibility(View.GONE);
                });


            }
        }
    }


    public void updateProgressBar() {
        progressComplaintDetailsVoice.setProgress(mediaPlayer.getCurrentPosition());
        if (!mediaPlayer.isPlaying()) {
            imgPauseComplaintDetailsVoice.setVisibility(View.GONE);
            imgPlayComplaintDetailsVoice.setVisibility(View.VISIBLE);
        }
        if (mediaPlayer.isPlaying()) {
            Runnable updater = this::updateProgressBar;
            handler.postDelayed(updater, 500);
        } else {
            imgPlayComplaintDetailsVoice.setVisibility(View.VISIBLE);
            imgPauseComplaintDetailsVoice.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.img_pause_complaint_details_voice)
    public void onStopPlayIngRecordedAudio() {
        mediaPlayer.pause();
        imgPlayComplaintDetailsVoice.setVisibility(View.VISIBLE);
        imgPauseComplaintDetailsVoice.setVisibility(View.GONE);

    }

    private void releaseAudioMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            //mediaPlayer = null;
            audioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    public void extractVoiceDuration(double duration) {
        //mReportDetailsVoiceDuration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        String seconds;
        String minutes;
        String[] d = String.valueOf(duration).replace(".", "-").split("-");
        seconds = d[1];
        minutes = d[0];
        if (seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        textComplaintDetailsVoiceDuration.setText(minutes + ":" + seconds);
    }


    @OnClick(R.id.img_play_video)
    public void onImgPlayVideo(View view) {
        videoIsPlaying = !videoIsPlaying;
        if (videoIsPlaying) {
            imgPlayVideo.setVisibility(View.GONE);
            imgVideoImage.setVisibility(View.GONE);
            initializePlayer();
        } else {
            imgPlayVideo.setVisibility(View.VISIBLE);
            imgVideoImage.setVisibility(View.VISIBLE);
        }
    }


    private void initializePlayer() {
        releaseAudioMediaPlayer();
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(this, new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            //player.seekTo(1000);
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.addListener(this);
        }
        MediaSource mediaSource = buildMediaSource(Uri.parse(mComplaintDetailsVideo));
        player.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {

        return new ProgressiveMediaSource.Factory(new DefaultHttpDataSourceFactory("exoplayer-codelab"))
                .createMediaSource(uri);

    }

    private void releaseVideoPlayer() {
        if (player != null) {
            playbackPosition = 0;
            currentWindow = 0;
            playWhenReady = true;
            player.release();
            player = null;
        }
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoIsPlaying = false;
        imgPlayVideo.setVisibility(View.VISIBLE);
        imgVideoImage.setVisibility(View.VISIBLE);
        playerView.setPlayer(null);
        releaseVideoPlayer();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
            videoIsPlaying = false;
            imgPlayVideo.setVisibility(View.VISIBLE);
            imgVideoImage.setVisibility(View.VISIBLE);
            playerView.setPlayer(null);
            releaseVideoPlayer();
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
        KarbalaUtils.showTimeOutMessage(this);
        handleUiEmptyErrorView(Constants.UI_STAT_NETWORK_ERROR);
    }

    @Override
    public void onError(int code) {
        KarbalaUtils.showErrorResult(this);
        handleUiEmptyErrorView(Constants.UI_STAT_NETWORK_ERROR);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseAudioMediaPlayer();
        videoIsPlaying = false;
        imgPlayVideo.setVisibility(View.VISIBLE);
        imgVideoImage.setVisibility(View.VISIBLE);
        playerView.setPlayer(null);
        releaseVideoPlayer();

    }

    public void handleUiEmptyErrorView(int uiStateDataError) {
        try {

            // adapter is empty
            nestedComplaintDetails.setVisibility(View.GONE);
            if (uiStateDataError == Constants.UI_STAT_NETWORK_ERROR) {
                mNetworkErrorView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
                mNetworkErrorView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
