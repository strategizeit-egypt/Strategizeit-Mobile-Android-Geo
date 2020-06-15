package com.xapps.karbala.ui.complaintdetails.imageViewer;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.xapps.karbala.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageViewerFragment extends DialogFragment {


    private String imageURL;

    @BindView(R.id.ortho_image_IV)
    PhotoView ortho_image_IV;
    @BindView(R.id.ortho_image_close)
    ImageView ortho_image_close;


    public ImageViewerFragment() {
        // Required empty public constructor
    }


    public static ImageViewerFragment newInstance(String imageURL) {
        ImageViewerFragment fragment = new ImageViewerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.imageURL=imageURL;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
      //  setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_image_viewer, container, false);
        ButterKnife.bind(this, view);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initUI();
        return view;
    }

    private void initUI() {

        ortho_image_IV.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ortho_image_close.setOnClickListener(v -> dismiss());
        Picasso.get().
                load(imageURL).
                error(R.drawable.image_placeholder).
                into(ortho_image_IV);
    }
    @Override
    public void onStart() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = width;
        params.height = height;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        /********* fully transparent************
         Window window = getDialog().getWindow();
         WindowManager.LayoutParams windowParams = window.getAttributes();
         windowParams.dimAmount = 0.1f;
         windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
         window.setAttributes(windowParams);
         /**********************************/
        super.onStart();
    }


}