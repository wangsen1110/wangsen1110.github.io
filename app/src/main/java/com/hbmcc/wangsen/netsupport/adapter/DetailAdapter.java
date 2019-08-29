package com.hbmcc.wangsen.netsupport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridDetailData;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private List<ThridDetailData> thridDetaildatalist;
    public DetailAdapter(List<ThridDetailData> thridDetailDatalist) {
        this.thridDetaildatalist = thridDetailDatalist;
    }

    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recyclerview_item_thrid_detail, parent, false);
        return new DetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailAdapter.ViewHolder holder, int position) {
        ThridDetailData thriddetailData = thridDetaildatalist.get(position);
        holder.textViewtime.setText(thriddetailData.getTime()+"");
        holder.textViewconnected.setText(thriddetailData.getConnected()+"");
        holder.textViewpaging.setText(thriddetailData.getPaging()+"");
        holder.textViewhandover.setText(thriddetailData.getHandover()+"");
        holder.textViewrelease.setText(thriddetailData.getRelease()+"");
        holder.textViewcover.setText(thriddetailData.getCover()+"");
        holder.textViewusercount.setText(thriddetailData.getUsercount()+"");
        holder.textViewprbuseup.setText(thriddetailData.getPrbuseup()+"");
        holder.textViewprbusedown.setText(thriddetailData.getPrbusedown()+"");
        holder.textViewvoconnected.setText(thriddetailData.getVoconnected()+"");
        holder.textViewvoerabconnet.setText(thriddetailData.getVoerabconnet()+"");
        holder.textViewvodelay.setText(thriddetailData.getVodelay()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return thridDetaildatalist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewtime;
        TextView textViewconnected;
        TextView textViewpaging ;
        TextView textViewhandover ;
        TextView textViewrelease ;
        TextView textViewcover;
        TextView textViewusercount;
        TextView textViewprbuseup ;
        TextView textViewprbusedown ;
        TextView textViewvoconnected;
        TextView textViewvoerabconnet;
        TextView textViewvodelay;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewtime = itemView.findViewById(R.id.textview_recyclerview_item_detail_time);
            textViewconnected = itemView.findViewById(R.id.textview_recyclerview_item_detail_connected);
            textViewpaging = itemView.findViewById(R.id.textview_recyclerview_item_detail_paging);
            textViewhandover = itemView.findViewById(R.id.textview_recyclerview_item_detail_handover);
            textViewrelease = itemView.findViewById(R.id.textview_recyclerview_item_detail_release);
            textViewcover = itemView.findViewById(R.id.textview_recyclerview_item_detail_cover);
            textViewusercount = itemView.findViewById(R.id.textview_recyclerview_item_detail_usercount);
            textViewprbuseup = itemView.findViewById(R.id.textview_recyclerview_item_detail_prbuseup);
            textViewprbusedown = itemView.findViewById(R.id.textview_recyclerview_item_detail_prbusedown);
            textViewvoconnected = itemView.findViewById(R.id.textview_recyclerview_item_detail_voconnected);
            textViewvoerabconnet = itemView.findViewById(R.id.textview_recyclerview_item_detail_voerabconnet);
            textViewvodelay = itemView.findViewById(R.id.textview_recyclerview_item_detail_vodelay);
        }
    }
}
