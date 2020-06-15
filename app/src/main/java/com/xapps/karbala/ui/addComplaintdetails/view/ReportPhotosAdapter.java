package com.xapps.karbala.ui.addComplaintdetails.view;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xapps.karbala.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportPhotosAdapter extends RecyclerView.Adapter<ReportPhotosAdapter.ReportPhotosViewHolder> {

    private List<Bitmap> bitmapList;
    private AddComplaintDetailsActivity addComplaintDetailsActivity;
    private int count;
    private OnRemovePhotoClickListener onRemovePhotoClickListener;

    public void setOnRemovePhotoClickListener(OnRemovePhotoClickListener onRemovePhotoClickListener) {
        this.onRemovePhotoClickListener = onRemovePhotoClickListener;
    }

    public ReportPhotosAdapter(List<Bitmap> bitmapList, AddComplaintDetailsActivity addComplaintDetailsActivity) {
        this.bitmapList = bitmapList;
        this.addComplaintDetailsActivity = addComplaintDetailsActivity;
        count = bitmapList.size();
    }

    @NonNull
    @Override
    public ReportPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_photo, parent, false);
        return new ReportPhotosAdapter.ReportPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportPhotosViewHolder holder, int position) {
        Bitmap bitmap = bitmapList.get(position);
        holder.itemReportPhoto.setImageBitmap(bitmap);
        holder.itemDeleteReportPhoto.setOnClickListener(v -> {
            removePhotoFromAdapter(position);
        });
    }

    public void AddPhotoToAdapter(Bitmap bitmap) {
        if (count >= 0) {
            count += 1;
        } else {
            count = 0;
        }
        bitmapList.add(count - 1, bitmap);
        notifyItemInserted(count - 1);
    }

    public void removePhotoFromAdapter(int position) {
        count--;
        bitmapList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bitmapList.size());
        if (onRemovePhotoClickListener!=null)
            onRemovePhotoClickListener.onRemovePhotoClick(position);
    }

    @Override
    public int getItemCount() {
        if (bitmapList == null) return 0;
        return count;
    }

    class ReportPhotosViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_report_photo)
        ImageView itemReportPhoto;
        @BindView(R.id.item_delete_report_photo)
        ImageView itemDeleteReportPhoto;

        public ReportPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnRemovePhotoClickListener{
        void onRemovePhotoClick(int position);
    }
}
