package com.hbmcc.wangsen.netsupport.adapter.fifth;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.ui.fragment.fifth.FifthData.FifthProblemData;

import java.util.List;

public class ProblemAdapter extends RecyclerView.Adapter<ProblemAdapter.ViewHolder> {
    private List<FifthProblemData> fifthProblemDataList;

    public ProblemAdapter(List<FifthProblemData> fifthProblemDataList) {
        this.fifthProblemDataList = fifthProblemDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recyclerview_item_fifth_problem, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        FifthProblemData fifthProblemData = fifthProblemDataList.get(i);
//        holder.textViewtime.setText(fifthProblemData.getTime() + "");
//        holder.textViewconnected.setText(fifthProblemData.getConnected() + "");
//        holder.textViewpaging.setText(fifthProblemData.getPaging() + "");
//        holder.textViewhandover.setText(fifthProblemData.getHandover() + "");
//        holder.textViewrelease.setText(fifthProblemData.getRelease() + "");
//        holder.textViewcover.setText(fifthProblemData.getCover() + "");
//        holder.textViewusercount.setText(fifthProblemData.getUsercount() + "");
//        holder.textViewprbuseup.setText(fifthProblemData.getPrbuseup() + "");
//        holder.textViewprbusedown.setText(fifthProblemData.getPrbusedown() + "");
//        holder.textViewvoconnected.setText(fifthProblemData.getVoconnected() + "");
//        holder.textViewvoerabconnet.setText(fifthProblemData.getVoerabconnet() + "");
//        holder.textViewvodelay.setText(fifthProblemData.getVodelay() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewtime;
        TextView textViewconnected;
        TextView textViewpaging;
        TextView textViewhandover;
        TextView textViewrelease;
        TextView textViewcover;
        TextView textViewusercount;
        TextView textViewprbuseup;
        TextView textViewprbusedown;
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
