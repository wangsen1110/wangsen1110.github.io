package com.hbmcc.wangsen.netsupport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridFailureData;

import java.util.List;

public class FailureAdapter extends RecyclerView.Adapter<FailureAdapter.ViewHolder> {

    private List<ThridFailureData> thridFailuredatalist;
    public FailureAdapter(List<ThridFailureData> thridThridFailureData) {
        this.thridFailuredatalist = thridThridFailureData;
    }

    @Override
    public FailureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recyclerview_item_thrid_failure, parent, false);
        return new FailureAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FailureAdapter.ViewHolder holder, int position) {
        ThridFailureData thridFailureData = thridFailuredatalist.get(position);
        holder.textViewname.setText(thridFailureData.getName()+"");
        holder.textViewalarmvalve.setText(thridFailureData.getAlarmvalve()+"");
        holder.textViewtime.setText(thridFailureData.getTime()+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return thridFailuredatalist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewname;
        TextView textViewisalarm;
        TextView textViewalarmvalve;
        TextView textViewtime;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewname = itemView.findViewById(R.id.textview_recyclerview_item_Failure_name);
            textViewalarmvalve = itemView.findViewById(R.id.textview_recyclerview_item_Failure_alarmvalve);
            textViewtime = itemView.findViewById(R.id.textview_recyclerview_item_Failure_time);
        }
    }
}
