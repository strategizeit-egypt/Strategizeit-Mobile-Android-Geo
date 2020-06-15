package com.xapps.karbala.ui.complaintsubcategory.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.SubCategoryDTO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {

    private ComplaintSubCategoryActivity complaintSubCategoryActivity;
    private int checkedPosition = -1;
    private List<SubCategoryDTO> subCategoryDTOList;
    private long selectedSubCategoryId;


    public SubCategoryAdapter(ComplaintSubCategoryActivity complaintSubCategoryActivity,
                              List<SubCategoryDTO> subCategoryDTOList, long selectedSubCategoryId) {
        this.complaintSubCategoryActivity = complaintSubCategoryActivity;
        this.subCategoryDTOList = subCategoryDTOList;
        this.selectedSubCategoryId = selectedSubCategoryId;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basic, parent, false);
        return new SubCategoryAdapter.SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        try {
            SubCategoryDTO subCategoryDTO = subCategoryDTOList.get(position);
            holder.itemSubCategoryRB.setChecked(false);
            holder.itemSubCategoryName.setText(subCategoryDTO.getName());
            holder.itemSubCategoryDescription.setText(subCategoryDTO.getDescription() == null ? " " : subCategoryDTO.getDescription());

            if (subCategoryDTO.getId() == selectedSubCategoryId) {
                holder.itemSubCategoryRB.setChecked(true);
                checkedPosition = position;
            }

            if (checkedPosition != -1) {
                if (checkedPosition == position) {
                    holder.itemSubCategoryRB.setChecked(true);
                } else {
                    holder.itemSubCategoryRB.setChecked(false);
                }
            }
            holder.itemView.setOnClickListener(v -> {
                if (checkedPosition != position) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = position;
                    selectedSubCategoryId = subCategoryDTOList.get(checkedPosition).getId();

                }
                holder.itemSubCategoryRB.setChecked(true);

            });
            holder.itemSubCategoryRB.setOnClickListener(v -> {
                if (checkedPosition != position) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = position;
                    selectedSubCategoryId = subCategoryDTOList.get(checkedPosition).getId();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return subCategoryDTOList == null ? 0 : subCategoryDTOList.size();
    }

    public SubCategoryDTO getSelectedSubCategoryDTO() {
        return checkedPosition != -1 ? subCategoryDTOList.get(checkedPosition) : null;
    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView itemSubCategoryName;
        @BindView(R.id.item_description)
        TextView itemSubCategoryDescription;
        @BindView(R.id.item_rb)
        RadioButton itemSubCategoryRB;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
