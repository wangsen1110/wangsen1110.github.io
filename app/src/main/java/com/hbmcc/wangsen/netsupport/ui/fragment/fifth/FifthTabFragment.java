package com.hbmcc.wangsen.netsupport.ui.fragment.fifth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.hbmcc.wangsen.netsupport.App;
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
import com.hbmcc.wangsen.netsupport.util.CustomCityData;
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener;
import com.lljjcoder.citywheel.CustomConfig;
import com.lljjcoder.style.citycustome.CustomCityPicker;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
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

    ScrollView scrollview;

    WirelessAdapter wirelessAdapter;
    FailureAdapter failureAdapter;
    ComplainAdapter complainAdapter;

    private boolean isGetData = false;
    private List<CustomCityData> mProvinceListData = new ArrayList<>();
    private CustomCityPicker customCityPicker = null;


    public static FifthTabFragment newInstance() {
        Bundle args = new Bundle();
        if (fragment == null) {
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
        scrollview = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_record1);
//        recyclerViewFragmentThridTabRecentRecord = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_record1);
//        recyclerViewFragmentThridTabRecentRecord2 = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_record2);
//        recyclerViewFragmentThridTabRecentRecordcomplain = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_complain);


        CustomCityData jsPro = new CustomCityData("10000", "江苏省");

        CustomCityData ycCity = new CustomCityData("11000", "盐城市");
        List<CustomCityData> ycDistList = new ArrayList<>();
        ycDistList.add(new CustomCityData("11100", "滨海县"));
        ycDistList.add(new CustomCityData("11200", "阜宁县"));
        ycDistList.add(new CustomCityData("11300", "大丰市"));
        ycDistList.add(new CustomCityData("11400", "盐都区"));
        ycCity.setList(ycDistList);

        CustomCityData czCity = new CustomCityData("12000", "常州市");
        List<CustomCityData> czDistList = new ArrayList<>();
        czDistList.add(new CustomCityData("12100", "新北区"));
        czDistList.add(new CustomCityData("12200", "天宁区"));
        czDistList.add(new CustomCityData("12300", "钟楼区"));
        czDistList.add(new CustomCityData("12400", "武进区"));
        czCity.setList(czDistList);

        List<CustomCityData> jsCityList = new ArrayList<>();
        jsCityList.add(ycCity);
        jsCityList.add(czCity);

        jsPro.setList(jsCityList);


        CustomCityData zjPro = new CustomCityData("20000", "浙江省");

        CustomCityData nbCity = new CustomCityData("21000", "宁波市");
        List<CustomCityData> nbDistList = new ArrayList<>();
        nbDistList.add(new CustomCityData("21100", "海曙区"));
        nbDistList.add(new CustomCityData("21200", "鄞州区"));
        nbCity.setList(nbDistList);

        CustomCityData hzCity = new CustomCityData("22000", "杭州市");
        List<CustomCityData> hzDistList = new ArrayList<>();
        hzDistList.add(new CustomCityData("22100", "上城区"));
        hzDistList.add(new CustomCityData("22200", "西湖区"));
        hzDistList.add(new CustomCityData("22300", "下沙区"));
        hzCity.setList(hzDistList);

        List<CustomCityData> zjCityList = new ArrayList<>();
        zjCityList.add(hzCity);
        zjCityList.add(nbCity);

        zjPro.setList(zjCityList);


        CustomCityData gdPro = new CustomCityData("30000", "广东省");

        CustomCityData fjCity = new CustomCityData("21000", "潮州市");
        List<CustomCityData> fjDistList = new ArrayList<>();
        fjDistList.add(new CustomCityData("21100", "湘桥区"));
        fjDistList.add(new CustomCityData("21200", "潮安区"));
        fjCity.setList(fjDistList);



        CustomCityData gzCity = new CustomCityData("22000", "广州市");
        List<CustomCityData> szDistList = new ArrayList<>();
        szDistList.add(new CustomCityData("22100", "荔湾区"));
        szDistList.add(new CustomCityData("22200", "增城区"));
        szDistList.add(new CustomCityData("22300", "从化区"));
        szDistList.add(new CustomCityData("22400", "南沙区"));
        szDistList.add(new CustomCityData("22500", "花都区"));
        szDistList.add(new CustomCityData("22600", "番禺区"));
        szDistList.add(new CustomCityData("22700", "黄埔区"));
        szDistList.add(new CustomCityData("22800", "白云区"));
        szDistList.add(new CustomCityData("22900", "天河区"));
        szDistList.add(new CustomCityData("22110", "海珠区"));
        szDistList.add(new CustomCityData("22120", "越秀区"));
        gzCity.setList(szDistList);

        List<CustomCityData> gdCityList = new ArrayList<>();
        gdCityList.add(gzCity);
        gdCityList.add(fjCity);

        gdPro.setList(gdCityList);

        mProvinceListData.add(jsPro);
        mProvinceListData.add(zjPro);
        mProvinceListData.add(gdPro);


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


        CustomConfig cityConfig = new CustomConfig.Builder()
                .title("选择城市")
                .visibleItemsCount(5)
                .setCityData(mProvinceListData)//设置数据源
                .provinceCyclic(false)
                .cityCyclic(false)
                .districtCyclic(false)
                .build();

        customCityPicker = new CustomCityPicker(App.getContext());
        customCityPicker.setCustomConfig(cityConfig);
        customCityPicker.setOnCustomCityPickerItemClickListener(new OnCustomCityPickerItemClickListener() {
            @Override
            public void onSelected(CustomCityData province, CustomCityData city, CustomCityData district) {
                if (province != null && city != null && district != null) {
                    resultTv.setText("province：" + province.getName() + "    " + province.getId() + "\n" +
                            "city：" + city.getName() + "    " + city.getId() + "\n" +
                            "area：" + district.getName() + "    " + district.getId() + "\n");
                }else{
                    resultTv.setText("结果出错！");
                }
            }
        });
        customCityPicker.showCityPicker();


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
