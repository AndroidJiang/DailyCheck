package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.CheckInRecord;

import java.util.List;

/**
 * 打卡记录详情适配器
 */
public class CheckInRecordAdapter extends RecyclerView.Adapter<CheckInRecordAdapter.ViewHolder> {
    private List<CheckInRecord> records;

    public CheckInRecordAdapter(List<CheckInRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_check_in_record_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckInRecord record = records.get(position);
        
        // 只显示时间段，不显示时长
        holder.tvTime.setText(record.getTime());
        
        if (record.hasNote()) {
            holder.layoutNote.setVisibility(View.VISIBLE);
            holder.tvNote.setText(record.getNote());
            holder.tvNoNote.setVisibility(View.GONE);
        } else {
            holder.layoutNote.setVisibility(View.GONE);
            holder.tvNoNote.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        LinearLayout layoutNote;
        TextView tvNote;
        TextView tvNoNote;

        ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvNoNote = itemView.findViewById(R.id.tvNoNote);
        }
    }
}

