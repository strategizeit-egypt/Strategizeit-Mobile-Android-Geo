package com.xapps.karbala.ui.subarea.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.SubAreaDTO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubAreasAdapter extends RecyclerView.Adapter<SubAreasAdapter.SubAreaViewHolder> {

    private SubAreaActivity subAreaActivity;
    private int checkedPosition = -1;
    private List<SubAreaDTO> subAreaDTOList;


    public SubAreasAdapter(SubAreaActivity subAreaActivity, List<SubAreaDTO> subAreaDTOList) {
        this.subAreaActivity = subAreaActivity;
        this.subAreaDTOList = subAreaDTOList;
    }

    @NonNull
    @Override
    public SubAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basic, parent, false);
        return new SubAreasAdapter.SubAreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubAreaViewHolder holder, int position) {
        try {
            SubAreaDTO subAreaDTO = subAreaDTOList.get(position);
            holder.itemSubAreaRB.setChecked(false);
            holder.itemSubAreaName.setText(subAreaDTO.getName());
            holder.itemSubAreaDescription.setText(subAreaDTO.getDescription());


            if (checkedPosition != -1) {
                if (checkedPosition == position) {
                    holder.itemSubAreaRB.setChecked(true);
                } else {
                    holder.itemSubAreaRB.setChecked(false);
                }
            }
            holder.itemView.setOnClickListener(v -> {
                if (checkedPosition != position) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = position;
                }
                holder.itemSubAreaRB.setChecked(true);

            });
            holder.itemSubAreaRB.setOnClickListener(v -> {
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
        return subAreaDTOList == null ? 0 : subAreaDTOList.size();
    }

    public SubAreaDTO getSelectedSubAreaDTO() {
        return checkedPosition != -1 ? subAreaDTOList.get(checkedPosition) : null;
    }

    class SubAreaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView itemSubAreaName;
        @BindView(R.id.item_description)
        TextView itemSubAreaDescription;
        @BindView(R.id.item_rb)
        RadioButton itemSubAreaRB;

        public SubAreaViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
