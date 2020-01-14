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
        holder.textViewpDistrict.setText(fifthProblemData.getpDistrict() + "");
        holder.textViewpTime.setText(fifthProblemData.getpTime() + "");
        holder.textViewpCoverage.setText(fifthProblemData.getpCoverage() + "");
        holder.textViewpWeakcoverage.setText(fifthProblemData.getpCoverage() + "");
        holder.textViewpWeakCoverageOutdoor.setText(fifthProblemData.getpWeakCoverageOutdoor() + "");
        holder.textViewpWeakCoverageIndoor.setText(fifthProblemData.getpWeakCoverageIndoor() + "");
        holder.textViewpHighLoad.setText(fifthProblemData.getpHighLoad() + "");
        holder.textViewpHighInterference.setText(fifthProblemData.getpHighInterference() + "");
        holder.textViewpHighInterferenceFDD.setText(fifthProblemData.getpHighInterferenceFDD() + "");
        holder.textViewpHighInterferenceTDD.setText(fifthProblemData.getpHighInterferenceTDD() + "");
        holder.textViewpLowOm.setText(fifthProblemData.getpLowOm() + "");
        holder.textViewpHighDrop.setText(fifthProblemData.getpHighDrop() + "");
        holder.textViewpUpwardHighDrop.setText(fifthProblemData.getpUpwardHighDrop() + "");
        holder.textViewpDownHighDrop.setText(fifthProblemData.getpDownHighDrop() + "");
        holder.textViewpLowESRVCCSwitch.setText(fifthProblemData.getpLowESRVCCSwitch() + "");
        holder.textViewpLowCQI.setText(fifthProblemData.getpLowCQI() + "");
        holder.textViewpLongTimeWithdraw.setText(fifthProblemData.getpLongTimeWithdraw() + "");
        holder.textViewpMaintainSolve.setText(fifthProblemData.getpMaintainSolve() + "");
        holder.textViewp4GAvailable.setText(fifthProblemData.getP4GAvailable() + "");
        holder.textViewpIndoorZeroFlow.setText(fifthProblemData.getpIndoorZeroFlow() + "");
        holder.textViewpLongTimeWithdrawQuantity.setText(fifthProblemData.getpLongTimeWithdrawQuantity() + "");
        holder.textViewpCoverageProblemQuantity.setText(fifthProblemData.getpCoverageProblemQuantity() + "");
        holder.textViewpPerceptionProblemQuantity.setText(fifthProblemData.getpPerceptionProblemQuantity() + "");
        holder.textViewpInterferenceProblemQuantity.setText(fifthProblemData.getpInterferenceProblemQuantity() + "");
        holder.textViewpMaintainProblemQuantity.setText(fifthProblemData.getpMaintainProblemQuantity() + "");
        holder.textViewpOtherProblemQuantity.setText(fifthProblemData.getpOtherProblemQuantity() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return fifthProblemDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewpDistrict;
        TextView textViewpTime;
        TextView textViewpCoverage;
        TextView textViewpWeakcoverage;
        TextView textViewpWeakCoverageOutdoor;
        TextView textViewpWeakCoverageIndoor;
        TextView textViewpHighLoad;
        TextView textViewpHighInterference;
        TextView textViewpHighInterferenceFDD;
        TextView textViewpHighInterferenceTDD;
        TextView textViewpLowOm;
        TextView textViewpHighDrop;
        TextView textViewpUpwardHighDrop;
        TextView textViewpDownHighDrop;
        TextView textViewpLowESRVCCSwitch;
        TextView textViewpLowCQI;
        TextView textViewpLongTimeWithdraw;
        TextView textViewpMaintainSolve;
        TextView textViewp4GAvailable;
        TextView textViewpIndoorZeroFlow;
        TextView textViewpLongTimeWithdrawQuantity;
        TextView textViewpCoverageProblemQuantity;
        TextView textViewpPerceptionProblemQuantity;
        TextView textViewpInterferenceProblemQuantity;
        TextView textViewpMaintainProblemQuantity;
        TextView textViewpOtherProblemQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewpDistrict = itemView.findViewById(R.id.textview_recyclerview_item_problem_pDistrict);
            textViewpTime = itemView.findViewById(R.id.textview_recyclerview_item_problem_pTime);
            textViewpCoverage = itemView.findViewById(R.id.textview_recyclerview_item_problem_pCoverage);
            textViewpWeakcoverage = itemView.findViewById(R.id.textview_recyclerview_item_problem_pWeakcoverage);
            textViewpWeakCoverageOutdoor = itemView.findViewById(R.id.textview_recyclerview_item_problem_pWeakCoverageOutdoor);
            textViewpWeakCoverageIndoor = itemView.findViewById(R.id.textview_recyclerview_item_problem_pWeakCoverageIndoor);
            textViewpHighLoad = itemView.findViewById(R.id.textview_recyclerview_item_problem_pHighLoad);
            textViewpHighInterference = itemView.findViewById(R.id.textview_recyclerview_item_problem_pHighInterference);
            textViewpHighInterferenceFDD = itemView.findViewById(R.id.textview_recyclerview_item_problem_pHighInterferenceFDD);
            textViewpHighInterferenceTDD = itemView.findViewById(R.id.textview_recyclerview_item_problem_pHighInterferenceTDD);
            textViewpLowOm = itemView.findViewById(R.id.textview_recyclerview_item_problem_pLowOm);
            textViewpHighDrop = itemView.findViewById(R.id.textview_recyclerview_item_problem_pHighDrop);
            textViewpUpwardHighDrop = itemView.findViewById(R.id.textview_recyclerview_item_problem_pUpwardHighDrop);
            textViewpDownHighDrop = itemView.findViewById(R.id.textview_recyclerview_item_problem_pDownHighDrop);
            textViewpLowESRVCCSwitch = itemView.findViewById(R.id.textview_recyclerview_item_problem_pLowESRVCCSwitch);
            textViewpLowCQI = itemView.findViewById(R.id.textview_recyclerview_item_problem_pLowCQI);
            textViewpLongTimeWithdraw = itemView.findViewById(R.id.textview_recyclerview_item_problem_pLongTimeWithdraw);
            textViewpMaintainSolve = itemView.findViewById(R.id.textview_recyclerview_item_problem_pMaintainSolve);
            textViewp4GAvailable = itemView.findViewById(R.id.textview_recyclerview_item_problem_p4GAvailable);
            textViewpIndoorZeroFlow = itemView.findViewById(R.id.textview_recyclerview_item_problem_pIndoorZeroFlow);
            textViewpLongTimeWithdrawQuantity = itemView.findViewById(R.id.textview_recyclerview_item_problem_pLongTimeWithdrawQuantity);
            textViewpCoverageProblemQuantity = itemView.findViewById(R.id.textview_recyclerview_item_problem_pCoverageProblemQuantity);
            textViewpPerceptionProblemQuantity = itemView.findViewById(R.id.textview_recyclerview_item_problem_pPerceptionProblemQuantity);
            textViewpInterferenceProblemQuantity = itemView.findViewById(R.id.textview_recyclerview_item_problem_pInterferenceProblemQuantity);
            textViewpMaintainProblemQuantity = itemView.findViewById(R.id.textview_recyclerview_item_problem_pMaintainProblemQuantity);
            textViewpOtherProblemQuantity = itemView.findViewById(R.id.textview_recyclerview_item_problem_pOtherProblemQuantity);
        }
    }
}
