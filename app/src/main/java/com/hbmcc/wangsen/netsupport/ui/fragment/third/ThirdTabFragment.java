package com.hbmcc.wangsen.netsupport.ui.fragment.third;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.adapter.ComplainAdapter;
import com.hbmcc.wangsen.netsupport.adapter.FailureAdapter;
import com.hbmcc.wangsen.netsupport.adapter.WirelessAdapter;
import com.hbmcc.wangsen.netsupport.base.BaseMainFragment;
import com.hbmcc.wangsen.netsupport.event.TabSelectedEvent;
import com.hbmcc.wangsen.netsupport.telephony.NetworkStatus;
import com.hbmcc.wangsen.netsupport.ui.fragment.MainFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridComplainData;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridFailureData;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridWirelessData;
import com.hbmcc.wangsen.netsupport.util.HttpUtil;
import com.hbmcc.wangsen.netsupport.util.LocatonConverter;

import org.greenrobot.eventbus.Subscribe;
import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

public class ThirdTabFragment extends BaseMainFragment {
    private HttpUtil httpUtil = new HttpUtil();
    static String lalndata = "读取中……";
    String jsonlaln;
    Gson gson = new Gson();
    public static DecimalFormat df = new DecimalFormat("#.00");
    private boolean isGetData = false;
    public static int ID = 0;
    public static String detailname;
    public static LatLng thirdlatLng = new LatLng(0, 0);
    public static long eci = 0;

    private RecyclerView recyclerViewFragmentThridTabRecentRecord;
    private RecyclerView recyclerViewFragmentThridTabRecentRecord2;
    private RecyclerView recyclerViewFragmentThridTabRecentRecordcomplain;
    public static TextView textonclick;
    private ArrayList<String> stringslist = new ArrayList<String>();
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

    public static ThirdTabFragment newInstance() {
        Bundle args = new Bundle();
        ThirdTabFragment fragment = new ThirdTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_tab, container,
                false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        EventBusActivityScope.getDefault(_mActivity).register(this);
        recyclerViewFragmentThridTabRecentRecord = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_record1);
        recyclerViewFragmentThridTabRecentRecord2 = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_record2);
        recyclerViewFragmentThridTabRecentRecordcomplain = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_complain);
        textonclick = view.findViewById(R.id.textView_fragment_thrid_tab_name);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //initRecyclerView(); //初始化 懒加载后调用  此处不用  否则会两次刷新数据
        textonclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThirdTabFragment.detailname = thridWirelessdatalist.get(ID).getCellname();
                fragmentdetail();
//                Toast.makeText(App.getContext(), "ThirdTabFragment.eci的值是 \n" + ThirdTabFragment.eci, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initRecyclerView() {
        NetworkStatus networkStatus = new NetworkStatus();
//        方式一
//        String latLngstring = thirdlatLng.toString();
//        String[] split = latLngstring.split(",");
//        String la = split[0].split(":")[1];
//        String ln = split[1].split(":")[1];
//        jsonlaln = "{\"latitude\":"+la +","+"\"longitude\":"+ln+"\"}";

        HashMap<String, Object> jsonmap = new HashMap<String, Object>();
        jsonmap.put("latitude", thirdlatLng.latitude);
        jsonmap.put("longitude", thirdlatLng.longitude);
        jsonmap.put("userphoneid", NetworkStatus.phonenumber);

        jsonlaln = gson.toJson(jsonmap);

        thridWirelessdatalist = new ArrayList<>();
        thridFailureDatalist = new ArrayList<>();
        thirdComplainDatalist = new ArrayList<>();

        String url = "http://192.168.1.133:8081/query";
        String qpresult = httpUtil.postJsonRequet(jsonlaln, url);

        LocatonConverter.MyLatLng myLatLngWgs84 = LocatonConverter.bd09ToWgs84(new LocatonConverter
                .MyLatLng(thirdlatLng.latitude, thirdlatLng.longitude));//09经纬度转为s84

        thridWirelessdatalistquery = LitePal.where("lon<? and " +
                "lon>? and lat<? and lat >?", myLatLngWgs84.longitude + 0.006
                + "", myLatLngWgs84
                .longitude - 0.006 + "", myLatLngWgs84.latitude + 0.006 + "", myLatLngWgs84
                .latitude - 0.006 + "").find(ThridWirelessData.class);

        if (thridWirelessdatalistquery.size() > 0) {
            for (int j = 0; j < thridWirelessdatalistquery.size(); j++) {
                thridWirelessdata = thridWirelessdatalistquery.get(j);
                thridWirelessdatalist.add(new ThridWirelessData(thridWirelessdata.getEci(), thridWirelessdata.getCellname(),
                        Float.parseFloat(df.format(thridWirelessdata.getConnected() * 100)),
                        Float.parseFloat(df.format(thridWirelessdata.getRelease() * 100)),
                        Float.parseFloat(df.format(thridWirelessdata.getMrcover() * 100)),
                        Float.parseFloat(df.format(thridWirelessdata.getPrbdisturb()))));

                thridFailureDatalistquery = LitePal.where("eci = ?",
                        thridWirelessdata.getEci() + "").find(ThridFailureData.class);
                if (thridFailureDatalistquery.size() > 0) {
                    for (int i = 0; i < thridFailureDatalistquery.size(); i++) {
                        thridFailureData = thridFailureDatalistquery.get(i);
                        thridFailureDatalist.add(new ThridFailureData(thridFailureData.getName(),
                                thridFailureData.getAlarmvalve(), thridFailureData.getTime()));
                    }
                } else {
                    thridFailureDatalist.add(new ThridFailureData(thridWirelessdata.getCellname(), "设备正常", " "));
                }
            }
        }
        thirdComplainDatalistquery = LitePal.where("lon<? and " +
                "lon>? and lat<? and lat >?", myLatLngWgs84.longitude + 0.006
                + "", myLatLngWgs84
                .longitude - 0.006 + "", myLatLngWgs84.latitude + 0.006 + "", myLatLngWgs84
                .latitude - 0.006 + "").find(ThridComplainData.class);

        if (thirdComplainDatalistquery.size() > 0) {
            for (int j = 0; j < thirdComplainDatalistquery.size(); j++) {
                thirdComplainData = thirdComplainDatalistquery.get(j);
                thirdComplainDatalist.add(new ThridComplainData( thirdComplainData.getTime()+"",
                        thirdComplainData.getUserid(), thirdComplainData.getCategory(),
                        thirdComplainData.getAddress()));
            }
        }

        //RecyclerView的四大组成是：
        //Layout Manager：Item的布局。
        //Adapter：为Item提供数据。
        //Item Decoration：Item之间的Divider。
        //Item Animator：添加、删除Item动画。
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        recyclerViewFragmentThridTabRecentRecord.setLayoutManager(layoutManager1);
        wirelessAdapter = new WirelessAdapter(thridWirelessdatalist);
        recyclerViewFragmentThridTabRecentRecord.setAdapter(wirelessAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recyclerViewFragmentThridTabRecentRecord2.setLayoutManager(layoutManager2);
        failureAdapter = new FailureAdapter(thridFailureDatalist);
        recyclerViewFragmentThridTabRecentRecord2.setAdapter(failureAdapter);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        recyclerViewFragmentThridTabRecentRecordcomplain.setLayoutManager(layoutManager3);
        complainAdapter = new ComplainAdapter(thirdComplainDatalist);
        recyclerViewFragmentThridTabRecentRecordcomplain.setAdapter(complainAdapter);
    }

    public void fragmentdetail() {
        ((MainFragment) getParentFragment()).startBrotherFragment(ThirdDetailFragment.newInstance("详细"));
    }

    /**
     * Reselected Tab
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainFragment.FORTH) {
            return;
        }
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
