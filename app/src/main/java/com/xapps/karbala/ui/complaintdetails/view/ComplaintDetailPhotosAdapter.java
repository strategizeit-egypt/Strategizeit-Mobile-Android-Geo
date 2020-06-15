package com.xapps.karbala.ui.complaintdetails.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.ComplaintFilesDTO;
import com.xapps.karbala.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComplaintDetailPhotosAdapter extends RecyclerView.Adapter<ComplaintDetailPhotosAdapter.ReportDetailPhotosViewHolder> {

    private ComplaintDetailsActivity complaintDetailsActivity;
    private List<ComplaintFilesDTO> mReportDetailsImagesList;
    OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public ComplaintDetailPhotosAdapter(ComplaintDetailsActivity complaintDetailsActivity, List<ComplaintFilesDTO> mReportDetailsImagesList) {
        this.complaintDetailsActivity = complaintDetailsActivity;
        this.mReportDetailsImagesList = mReportDetailsImagesList;
    }

    @NonNull
    @Override
    public ReportDetailPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_details_photo, parent, false);
        return new ComplaintDetailPhotosAdapter.ReportDetailPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportDetailPhotosViewHolder holder, int position) {
        try {
            ComplaintFilesDTO complaintFilesDTO = mReportDetailsImagesList.get(position);
            String imagePath = Constants.BASE_URL_COMPLAINT_FILES + complaintFilesDTO.getReportId() + "/" + complaintFilesDTO.getFilePath();
            Picasso.get()
                    .load(imagePath)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(holder.itemReportDetailsPhoto);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClickListener.onImageClickListener(imagePath);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mReportDetailsImagesList == null ? 0 : mReportDetailsImagesList.size();
    }

    public void addPhotosToAdapter(List<ComplaintFilesDTO> mReportDetailsImagesList) {
        this.mReportDetailsImagesList = mReportDetailsImagesList;
        notifyDataSetChanged();
    }

    class ReportDetailPhotosViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_report_details_photo)
        ImageView itemReportDetailsPhoto;

        public ReportDetailPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnImageClickListener {
        void onImageClickListener(String imagePath);
    }
}
