package com.hbmcc.wangsen.netsupport.ui.fragment.fifth;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.adapter.fifth.ProblemAdapter;
import com.hbmcc.wangsen.netsupport.base.BaseMainFragment;
import com.hbmcc.wangsen.netsupport.event.TabSelectedEvent;
import com.hbmcc.wangsen.netsupport.event.UpdateUeStatusEvent;
import com.hbmcc.wangsen.netsupport.telephony.NetworkStatus;
import com.hbmcc.wangsen.netsupport.ui.fragment.MainFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.fifth.FifthData.FifthProblemData;
import com.hbmcc.wangsen.netsupport.ui.fragment.fifth.bean.CardBean;
import com.hbmcc.wangsen.netsupport.ui.fragment.fifth.bean.GetJsonDataUtil;
import com.hbmcc.wangsen.netsupport.ui.fragment.fifth.bean.JsonBean;
import com.hbmcc.wangsen.netsupport.ui.fragment.forth.ForthTabFragment;
import com.hbmcc.wangsen.netsupport.util.HttpUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

import static com.hbmcc.wangsen.netsupport.base.EnumHttpQ.FIFTH_PROBLEM;


public class FifthTabFragment extends BaseMainFragment {

    public static FifthTabFragment fragment;
    public FifthProblemData fifthProblemData;
    private ProblemAdapter problemAdapter;
    private RecyclerView recyclerViewFragmentFifthTabProblemRecentRecord;
    private List<FifthProblemData> fifthProblemDataList = new ArrayList<>();
    private List<FifthProblemData> fifthProblemDataListQuery = new ArrayList<FifthProblemData>();
    private Gson gson = new Gson();
    private HttpUtil httpUtil = new HttpUtil();
    private ScrollView scrollview;
    private Button btnFifthCityChoose;
    private Button btnFifthDayChoose;
    private Button btnFifthTabAdd;
    private Button btnFifthTabDelete;
    private boolean isGetData = false;
    //    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private List<JsonBean> options1ItemsJosn = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();


    private TimePickerView pvTime, pvCustomTime, pvCustomLunar;
    private OptionsPickerView pvOptions, pvCustomOptions, pvNoLinkOptions;
    private ArrayList<CardBean> cardItem = new ArrayList<>();
    private FrameLayout mFrameLayout;

    private ArrayList<String> food = new ArrayList<>();
    private ArrayList<String> clothes = new ArrayList<>();
    private ArrayList<String> computer = new ArrayList<>();
    private ArrayList<String> timeSelectList = new ArrayList<>();

    private String opt1tx, opt2tx, opt3tx = "湖北汇总";
    private String dayChooseText = "2020/1/2";
    private String cityChooseText = "cityChoose";
    private boolean fristQuery = true;


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
        mFrameLayout = (FrameLayout) view.findViewById(R.id.fragmen_fragment);
        //等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃。
        initView(view);
        return view;
    }

    private void initView(View view) {
        EventBusActivityScope.getDefault(_mActivity).register(this);
        scrollview = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_record1);

        btnFifthCityChoose = view.findViewById(R.id.btn_fragment_fifth_city_choose);
        btnFifthDayChoose = view.findViewById(R.id.btn_fragment_fifth_day_choose);
        btnFifthTabAdd = view.findViewById(R.id.btn_fragment_fifth_tab_add);
        btnFifthTabDelete = view.findViewById(R.id.btn_fragment_fifth_tab_delete);
        recyclerViewFragmentFifthTabProblemRecentRecord = view.findViewById(R.id.recyclerView_fragment_fifth_tab_recent_record);

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
        initJsonData();

        try {
            ProblemDataQuery(dayChooseText);
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnFifthCityChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerView();
                cityChooseText = (String) btnFifthCityChoose.getText();
            }
        });


        btnFifthDayChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimePicker();
                pvTime.show(v, false);
                dayChooseText = (String) btnFifthDayChoose.getText();
            }
        });

        btnFifthTabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayChooseText.equals("dayChooseText")) {
                    Toast.makeText(_mActivity, "未选择日期，不知道查询哪一天的指标", Toast.LENGTH_LONG).show();
                }
                if (cityChooseText.equals("cityChoose")) {
                    Toast.makeText(_mActivity, "未选择地区，不知道查询哪地方的指标", Toast.LENGTH_LONG).show();
                }
                if (!timeSelectList.contains(dayChooseText) && !cityChooseText.equals("cityChoose") && !dayChooseText.equals("dayChooseText")) {
                    timeSelectList.add(dayChooseText);
//                    Toast.makeText(_mActivity,
//                            !timeSelectList.contains(dayChooseText)+""+
//                    !cityChooseText.equals("cityChoose")+""+
//                    !dayChooseText.equals("dayChooseText")+""
//                            , Toast.LENGTH_LONG).show();
                    try {
                        ProblemDataQuery(dayChooseText);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (fifthProblemDataListQuery != null && !cityChooseText.equals("cityChoose") && !dayChooseText.equals("dayChooseText")) {
//                    Toast.makeText(_mActivity, "fifthProblemDataListQuery" +fifthProblemDataListQuery.size() , Toast.LENGTH_SHORT).show();

                    for (int j = 0; j < fifthProblemDataListQuery.size() - 1; j++) {
                        if (fifthProblemDataListQuery.get(j).getpDistrict().equals(opt3tx) &&
                                fifthProblemDataListQuery.get(j).getpTime().equals(dayChooseText)) {
                            fifthProblemDataList.add(fifthProblemDataListQuery.get(j));
                            break;
                        }

                    }
                }
                initRecyclerView();
            }
        });

        btnFifthTabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fifthProblemDataList.clear();
                initRecyclerView();
            }
        });
    }


    public void handle() {
        Message msg = new Message();
        mhandler.sendMessage(msg);
    }

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!HttpUtil.qresult.equals(null)) {
                Gson gsonservice = new GsonBuilder().serializeNulls().create();
                Type type = new TypeToken<List<FifthProblemData>>() {
                }.getType();
                fifthProblemDataListQuery = gsonservice.fromJson(HttpUtil.qresult, type);

                for (int j = 0; j < fifthProblemDataListQuery.size() - 1; j++) {
                    if (fifthProblemDataListQuery.get(j).getpDistrict().equals(opt3tx) &&
                            fifthProblemDataListQuery.get(j).getpTime().equals(dayChooseText)) {
                        fifthProblemDataList.add(fifthProblemDataListQuery.get(j));
                        break;
                    }
                }
                initRecyclerView();
            } else {
                Toast.makeText(_mActivity, "未查询到对应数据，请更换日期再查询", Toast.LENGTH_LONG).show();
            }
            if (fristQuery) {
                dayChooseText = "dayChooseText";
                fristQuery = false;
            }

        }
    };

    private void ProblemDataQuery(String ptime) throws Exception {

        HashMap<String, Object> jsonmap = new HashMap<String, Object>();
        jsonmap.put("latitude", 123.21321);
        jsonmap.put("longitude", 123.21321);
        jsonmap.put("userphone", NetworkStatus.phonenumber);

        jsonmap.put("ptime", ptime);

        String jsonlaln = gson.toJson(jsonmap);
        String url = "http://192.168.1.133:8082/fifth/problemlist";
        httpUtil.postJsonRequet(jsonlaln, url, FIFTH_PROBLEM);
    }


    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFragmentFifthTabProblemRecentRecord.setLayoutManager(layoutManager);
        problemAdapter = new ProblemAdapter(fifthProblemDataList);
        recyclerViewFragmentFifthTabProblemRecentRecord.setAdapter(problemAdapter);
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

    private void showPickerView() {// 弹出选择器
        if (options1ItemsJosn.isEmpty()) {
            initJsonData();
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(_mActivity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                opt1tx = options1ItemsJosn.size() > 0 ?
                        options1ItemsJosn.get(options1).getPickerViewText() : "";

                opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;
                btnFifthCityChoose.setText(opt3tx);
                if (opt3tx.equals("汇总")) {
                    btnFifthCityChoose.setText(opt2tx + opt3tx);
                }
                Toast.makeText(_mActivity, "已选择\t\t" + tx, Toast.LENGTH_SHORT).show();
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1ItemsJosn, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(App.getContext(), "province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1ItemsJosn = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(cityList);
            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }


    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2020, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);
        //时间选择器
        pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                btnFifthDayChoose.setText(getTime(date));
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                                pvTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setContentTextSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, selectedDate)
                .setDecorView(mFrameLayout)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                .setOutSideColor(0x00000000)
                .setOutSideCancelable(false)
                .build();

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d");// HH:mm:ss
        return format.format(date);
    }

    private String getTimeDefault() {//获取当前时间的前一天时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d");// HH:mm:ss
        long ms = new Date().getTime() - 1 * 24 * 3600 * 1000L;
        Date prevDay = new Date(ms);
        return format.format(prevDay);
    }

}
