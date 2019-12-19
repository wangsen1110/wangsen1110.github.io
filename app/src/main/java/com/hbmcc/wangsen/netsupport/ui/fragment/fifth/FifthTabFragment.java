package com.hbmcc.wangsen.netsupport.ui.fragment.fifth;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener;
import com.lljjcoder.bean.CustomCityData;
import com.lljjcoder.citywheel.CustomConfig;
import com.lljjcoder.style.citycustome.CustomCityPicker;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class FifthTabFragment extends BaseMainFragment implements View.OnClickListener{

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

    private TextView customeBtn;
    private TextView resultTv;

    EditText mProVisibleCountEt;

    CheckBox mProCyclicCk;

    CheckBox mCityCyclicCk;

    CheckBox mAreaCyclicCk;

    CheckBox mHalfBgCk;
    TextView mResetSettingTv;

    TextView mOneTv;

    TextView mTwoTv;

    TextView mThreeTv;

    LinearLayout pro_cyclic_ll;
    LinearLayout city_cyclic_ll;
    LinearLayout area_cyclic_ll;

    private int visibleItems = 5;

    private boolean isProvinceCyclic = true;

    private boolean isCityCyclic = true;

    private boolean isDistrictCyclic = true;

    private boolean isShowBg = true;
    private CustomCityPicker customCityPicker = null;
    /**
     * 自定义数据源-省份数据
     */
    public List<CustomCityData> mProvinceListData = new ArrayList<>();
    public CustomConfig.WheelType mWheelType = CustomConfig.WheelType.PRO_CITY_DIS;


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

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.btn_Time && pvTime != null) {
//            // pvTime.setDate(Calendar.getInstance());
//            /* pvTime.show(); //show timePicker*/
//            pvTime.show(v);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
//        } else if (v.getId() == R.id.btn_Options && pvOptions != null) {
//            pvOptions.show(); //弹出条件选择器
//        } else if (v.getId() == R.id.btn_CustomOptions && pvCustomOptions != null) {
//            pvCustomOptions.show(); //弹出自定义条件选择器
//        } else if (v.getId() == R.id.btn_CustomTime && pvCustomTime != null) {
//            pvCustomTime.show(); //弹出自定义时间选择器
//        } else if (v.getId() == R.id.btn_no_linkage && pvNoLinkOptions != null) {//不联动数据选择器
//            pvNoLinkOptions.show();
//        } else if (v.getId() == R.id.btn_GotoJsonData) {//跳转到 省市区解析示例页面
//            startActivity(new Intent(MainActivity.this, JsonDataActivity.class));
//        } else if (v.getId() == R.id.btn_fragment) {//跳转到 fragment
//            startActivity(new Intent(MainActivity.this, FragmentTestActivity.class));
//        } else if (v.getId() == R.id.btn_lunar) {
//            pvCustomLunar.show();
//        } else if (v.getId() == R.id.btn_circle) {
//            startActivity(new Intent(MainActivity.this, TestCircleWheelViewActivity.class));
//        }
    }
}
