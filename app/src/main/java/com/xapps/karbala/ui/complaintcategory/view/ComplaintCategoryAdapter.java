package com.xapps.karbala.ui.complaintcategory.view;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.CategoryDTO;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.KarbalaUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ComplaintCategoryAdapter extends RecyclerView.Adapter<ComplaintCategoryAdapter.ReportTypeViewHolder> {

    private ComplaintCategoryActivity complaintCategoryActivity;
    private List<CategoryDTO> categoryDTOList;
    OnCategoryItemClickListener onCategoryItemClickListener;

    public void setOnCategoryItemClickListener(OnCategoryItemClickListener onCategoryItemClickListener) {
        this.onCategoryItemClickListener = onCategoryItemClickListener;
    }

    public ComplaintCategoryAdapter(ComplaintCategoryActivity complaintCategoryActivity, List<CategoryDTO> categoryDTOList) {
        this.complaintCategoryActivity = complaintCategoryActivity;
        this.categoryDTOList = categoryDTOList;
    }

    @NonNull
    @Override
    public ReportTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ComplaintCategoryAdapter.ReportTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportTypeViewHolder holder, int position) {
        try {
            CategoryDTO categoryDTO = categoryDTOList.get(position);
            holder.itemComplaintCategoryName.setText(categoryDTO.getName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.view.setBackground(KarbalaUtils.getBackgroundDrawable(complaintCategoryActivity.getResources().getColor(R.color.gainsboro), null));
            }
            Picasso.get()
                    .load(Constants.BASE_URL_CATEGORY_IMAGES + categoryDTO.getImage())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(holder.itemCategoryImage);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCategoryItemClickListener != null) {
                        onCategoryItemClickListener.onCategoryItemClick(categoryDTO);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return categoryDTOList == null ? 0 : categoryDTOList.size();
    }

    class ReportTypeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_category_name)
        TextView itemComplaintCategoryName;
        @BindView(R.id.item_category_image)
        CircleImageView itemCategoryImage;
        View view;

        public ReportTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }

    public interface OnCategoryItemClickListener {
        void onCategoryItemClick(CategoryDTO categoryDTO);
    }


}
