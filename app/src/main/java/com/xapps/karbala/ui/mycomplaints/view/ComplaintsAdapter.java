package com.xapps.karbala.ui.mycomplaints.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.ComplaintDTO;
import com.xapps.karbala.utils.KarbalaUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ReportsViewHolder> {
    private MyComplaintsFragment myComplaintsFragment;
    OnReportItemClickListener onReportItemClickListener;
    List<ComplaintDTO> complaintDTOList;

    public void setOnReportItemClickListener(OnReportItemClickListener onReportItemClickListener) {
        this.onReportItemClickListener = onReportItemClickListener;
    }

    public ComplaintsAdapter(MyComplaintsFragment myComplaintsFragment, List<ComplaintDTO> complaintDTOList) {
        this.myComplaintsFragment = myComplaintsFragment;
        this.complaintDTOList = complaintDTOList;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ComplaintsAdapter.ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        try {
            ComplaintDTO complaintDTO = complaintDTOList.get(position);
            holder.itemReportType.setText(complaintDTO.getReportTypeName());
            holder.itemReportTitle.setText(complaintDTO.getDetails());
            holder.itemReportDate.setText(KarbalaUtils.formatDate(complaintDTO.getCreationDate(), "dd MMMM yyyy"));
            holder.itemReportStatus.setText(complaintDTO.getReportStatusName());
            try {
                holder.itemReportStatusColor.setBackgroundColor(Color.parseColor(complaintDTO.getColor().trim()));
            }catch (Exception e){
                e.printStackTrace();
            }

            if (position == complaintDTOList.size() - 1) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.cardView.getLayoutParams();
                params.setMargins(0, 0, 0, 50);
                holder.cardView.setLayoutParams(params);
            }


            holder.itemView.setOnClickListener(v -> {
                onReportItemClickListener.onItemReportClick(complaintDTO.getId());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return complaintDTOList == null ? 0 : complaintDTOList.size();
    }

    public void clear() {
        complaintDTOList.clear();
        notifyDataSetChanged();
    }

    public void addReportsToAdapter(List<ComplaintDTO> complaintDTOList) {
        int oldSize = this.complaintDTOList.size();
        this.complaintDTOList.addAll(oldSize, complaintDTOList);
        notifyItemRangeInserted(oldSize, complaintDTOList.size());
    }

    class ReportsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_report_type)
        TextView itemReportType;
        @BindView(R.id.item_report_title)
        TextView itemReportTitle;
        @BindView(R.id.item_report_date)
        TextView itemReportDate;
        @BindView(R.id.item_report_status)
        TextView itemReportStatus;
        @BindView(R.id.item_report_status_color)
        View itemReportStatusColor;
        @BindView(R.id.item_card)
        CardView cardView;

        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnReportItemClickListener {
        void onItemReportClick(long reportId);
    }
}
