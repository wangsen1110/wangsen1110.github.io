package com.hbmcc.wangsen.netsupport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridComplainData;

import java.util.List;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ViewHolder> {

    private List<ThridComplainData> complainAdapterslist;
    public ComplainAdapter(List<ThridComplainData> complainAdapterslistData) {
        this.complainAdapterslist = complainAdapterslistData;
    }

    @Override
    public ComplainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recyclerview_item_thrid_complain, parent, false);
        return new ComplainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ComplainAdapter.ViewHolder holder, int position) {
        ThridComplainData complainAdapteData = complainAdapterslist.get(position);
        holder.textViewname.setText(complainAdapteData.getAddress()+"");
        holder.textViewuser.setText(complainAdapteData.getUserid()+"");
        holder.textViewid.setText(complainAdapteData.getCategory()+"");
        holder.textViewtime.setText(complainAdapteData.getTime()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return complainAdapterslist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewname;
        TextView textViewuser;
        TextView textViewid;
        TextView textViewtime;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewname = itemView.findViewById(R.id.textview_recyclerview_item_complain_name);
            textViewuser = itemView.findViewById(R.id.textview_recyclerview_item_complain_user);
            textViewid = itemView.findViewById(R.id.textview_recyclerview_item_complain_id);
            textViewtime = itemView.findViewById(R.id.textview_recyclerview_item_complain_time);
        }
    }
}
