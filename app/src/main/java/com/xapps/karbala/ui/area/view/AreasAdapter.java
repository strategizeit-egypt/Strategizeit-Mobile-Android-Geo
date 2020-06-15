package com.xapps.karbala.ui.area.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.AreaDTO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreasAdapter extends RecyclerView.Adapter<AreasAdapter.ReportTypeViewHolder> {

    private AreaActivity areaActivity;
    private int checkedPosition = -1;
    private List<AreaDTO> areaDTOList;


    public AreasAdapter(AreaActivity areaActivity, List<AreaDTO> areaDTOList) {
        this.areaActivity = areaActivity;
        this.areaDTOList = areaDTOList;
    }

    @NonNull
    @Override
    public ReportTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basic, parent, false);
        return new AreasAdapter.ReportTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportTypeViewHolder holder, int position) {
        try {
            AreaDTO areaDTO = areaDTOList.get(position);
            holder.itemAreaRB.setChecked(false);
            holder.itemAreaName.setText(areaDTO.getName());
            holder.itemAreaDescription.setText(areaDTO.getName());

            if (checkedPosition != -1 ) {
                if (checkedPosition == position) {
                    holder.itemAreaRB.setChecked(true);
                } else {
                    holder.itemAreaRB.setChecked(false);
                }
            }
            holder.itemView.setOnClickListener(v -> {
                if (checkedPosition != position) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = position;

                }
                holder.itemAreaRB.setChecked(true);

            });
            holder.itemAreaRB.setOnClickListener(v -> {
                if (checkedPosition != position) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = position;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return areaDTOList == null ? 0 : areaDTOList.size();
    }

    public AreaDTO getSelectedAreaDTO() {
        return checkedPosition != -1 ? areaDTOList.get(checkedPosition) : null;
    }

    class ReportTypeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView itemAreaName;
        @BindView(R.id.item_description)
        TextView itemAreaDescription;
        @BindView(R.id.item_rb)
        RadioButton itemAreaRB;

        public ReportTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
