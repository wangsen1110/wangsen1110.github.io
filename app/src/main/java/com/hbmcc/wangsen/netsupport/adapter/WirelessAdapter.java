package com.hbmcc.wangsen.netsupport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.ThirdTabFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridWirelessData;

import java.util.List;

public class WirelessAdapter extends RecyclerView.Adapter<WirelessAdapter.ViewHolder> {
    private List<ThridWirelessData> thridWirelessdatalist;
    public ThirdTabFragment thirdTabFragment = new ThirdTabFragment();

    public WirelessAdapter(List<ThridWirelessData> thridWirelessDatalist) {
        this.thridWirelessdatalist = thridWirelessDatalist;
    }

    @Override
    public WirelessAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recyclerview_item_thrid_wireless, parent, false);
        return new WirelessAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WirelessAdapter.ViewHolder holder, final int position) {
        final ThridWirelessData thridWirelessData = thridWirelessdatalist.get(position);
        holder.textViewname.setText(thridWirelessData.getCellname() + "");
        holder.textViewconnected.setText(thridWirelessData.getConnected() + "");
        holder.textViewhandover.setText(thridWirelessData.getRelease() + "");
        holder.textViewrelease.setText(thridWirelessData.getMrcover() + "");
        holder.textViewprbuse.setText(thridWirelessData.getPrbdisturb() + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThirdTabFragment.ID = position;
                ThirdTabFragment.detailname = thridWirelessData.getCellname();
                ThirdTabFragment.eci = thridWirelessData.getEci();

                Toast.makeText(App.getContext(), "切换至    \n" + thridWirelessData.getCellname()+"\n"+
                                "Cell-ID是：\t"+(int)(ThirdTabFragment.eci/256)+"—"+(int)(ThirdTabFragment.eci%256)
                        , Toast.LENGTH_SHORT).show();
                ThirdTabFragment.textonclick.setText("点击查详细：\t"+thridWirelessData.getCellname());
                ThirdTabFragment.textonclick.setTextColor(0xff000080);
            }
        });
    }

    @Override
    public int getItemCount() {
        return thridWirelessdatalist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewname;
        TextView textViewconnected;
        TextView textViewhandover;
        TextView textViewrelease;
        TextView textViewprbuse;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewname = itemView.findViewById(R.id.textview_recyclerview_item_wireless_name);
            textViewconnected = itemView.findViewById(R.id.textview_recyclerview_item_wireless_connected);
            textViewhandover = itemView.findViewById(R.id.textview_recyclerview_item_wireless_handover);
            textViewrelease = itemView.findViewById(R.id.textview_recyclerview_item_wireless_release);
            textViewprbuse = itemView.findViewById(R.id.textview_recyclerview_item_wireless_prbuse);
        }
    }
}

