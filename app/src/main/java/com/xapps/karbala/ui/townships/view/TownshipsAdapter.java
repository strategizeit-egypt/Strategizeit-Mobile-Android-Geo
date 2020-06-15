package com.xapps.karbala.ui.townships.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xapps.karbala.R;
import com.xapps.karbala.model.data.dto.TownshipDTO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TownshipsAdapter extends RecyclerView.Adapter<TownshipsAdapter.TownshipsViewHolder> {

    private TownshipsActivity townshipsActivity;
    private int checkedPosition = -1;
    private List<TownshipDTO> townshipDTOList;
    private long selectedReportTownshipId;


    public TownshipsAdapter(TownshipsActivity townshipsActivity, List<TownshipDTO> townshipDTOList, long selectedReportTownshipId) {
        this.townshipsActivity = townshipsActivity;
        this.townshipDTOList = townshipDTOList;
        this.selectedReportTownshipId = selectedReportTownshipId;
    }

    @NonNull
    @Override
    public TownshipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_township, parent, false);
        return new TownshipsAdapter.TownshipsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TownshipsViewHolder holder, int position) {
        try {
            TownshipDTO townshipDTO = townshipDTOList.get(position);
            holder.itemTownshipRB.setChecked(false);
            holder.itemTownshipTitle.setText(townshipDTO.getName());
            holder.itemTownshipDescription.setText(townshipDTO.getDescription());

            if (townshipDTO.getId() == selectedReportTownshipId) {
                holder.itemTownshipRB.setChecked(true);
                checkedPosition = position;
            }

            if (checkedPosition != -1) {
                if (checkedPosition == position) {
                    holder.itemTownshipRB.setChecked(true);
                } else {
                    holder.itemTownshipRB.setChecked(false);
                }
            }
            holder.itemView.setOnClickListener(v -> {
                if (checkedPosition != position) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = position;
                    selectedReportTownshipId = townshipDTOList.get(checkedPosition).getId();
                }
                holder.itemTownshipRB.setChecked(true);

            });
            holder.itemTownshipRB.setOnClickListener(v -> {
                if (checkedPosition != position) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = position;
                    selectedReportTownshipId = townshipDTOList.get(checkedPosition).getId();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return townshipDTOList == null ? 0 : townshipDTOList.size();
    }


    public TownshipDTO getSelectedTownshipDTO() {
        return checkedPosition != -1 ? townshipDTOList.get(checkedPosition) : null;
    }

    public void addTownshipsToAdapter(List<TownshipDTO> townshipDTOList, long selectedReportTownshipId) {
       // this.selectedReportTownshipId = selectedReportTownshipId;
        int oldSize = this.townshipDTOList.size();
        this.townshipDTOList.addAll(oldSize, townshipDTOList);
        notifyItemRangeInserted(oldSize, townshipDTOList.size());
    }

    public void clear() {
        townshipDTOList.clear();
        checkedPosition = -1;
        notifyDataSetChanged();
    }

    class TownshipsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_township_title)
        TextView itemTownshipTitle;
        @BindView(R.id.item_township_description)
        TextView itemTownshipDescription;
        @BindView(R.id.item_township_rb)
        RadioButton itemTownshipRB;

        public TownshipsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
