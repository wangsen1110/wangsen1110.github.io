package com.hbmcc.wangsen.netsupport.ui.fragment.fifth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.adapter.ComplainAdapter;
import com.hbmcc.wangsen.netsupport.adapter.FailureAdapter;
import com.hbmcc.wangsen.netsupport.adapter.WirelessAdapter;
import com.hbmcc.wangsen.netsupport.base.BaseMainFragment;
import com.hbmcc.wangsen.netsupport.event.TabSelectedEvent;
import com.hbmcc.wangsen.netsupport.ui.fragment.MainFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridComplainData;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridFailureData;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridWirelessData;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class FifthTabFragment extends BaseMainFragment {

    public static FifthTabFragment fragment;
    private RecyclerView recyclerViewFragmentThridTabRecentRecord;
    private RecyclerView recyclerViewFragmentThridTabRecentRecord2;
    private RecyclerView recyclerViewFragmentThridTabRecentRecordcomplain;
    public List<ThridWirelessData> thridWirelessdatalist;
    public List<ThridFailureData> thridFailureDatalist;
    public List<ThridComplainData> thirdComplainDatalist;

    public List<ThridWirelessData> thridWirelessdatalistquery;
    public List<ThridFailureData> thridFailureDatalistquery;
    public List<ThridComplainData> thirdComplainDatalistquery;

    public ThridWirelessData thridWirelessdata;
    public ThridFailureData thridFailureData;
    public ThridComplainData thirdComplainData;

    WirelessAdapter wirelessAdapter;
    FailureAdapter failureAdapter;
    ComplainAdapter complainAdapter;

    private boolean isGetData = false;

    public static FifthTabFragment newInstance() {
        Bundle args = new Bundle();
        if(fragment == null) {
            fragment = new FifthTabFragment();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fifth_tab, container,
                false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        EventBusActivityScope.getDefault(_mActivity).register(this);
//        recyclerViewFragmentThridTabRecentRecord = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_record1);
//        recyclerViewFragmentThridTabRecentRecord2 = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_record2);
//        recyclerViewFragmentThridTabRecentRecordcomplain = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_complain);
    }


    /**
     * Reselected Tab
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainFragment.SECOND) {
            return;
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }


    public void initRecyclerView() {

    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return super.onCreateFragmentAnimator();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作  GetData();
            initRecyclerView();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onResume() {
        super.onResume();
        isGetData = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

}
