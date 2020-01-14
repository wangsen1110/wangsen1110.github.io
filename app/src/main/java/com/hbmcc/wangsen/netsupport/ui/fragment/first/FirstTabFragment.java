package com.hbmcc.wangsen.netsupport.ui.fragment.first;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileIOUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.adapter.NeighbourCellAdapter;
import com.hbmcc.wangsen.netsupport.adapter.RecentRecordAdapter;
import com.hbmcc.wangsen.netsupport.base.BaseMainFragment;
import com.hbmcc.wangsen.netsupport.database.LteBasestationCell;
import com.hbmcc.wangsen.netsupport.event.TabSelectedEvent;
import com.hbmcc.wangsen.netsupport.event.UpdateUeStatusEvent;
import com.hbmcc.wangsen.netsupport.telephony.LteBand;
import com.hbmcc.wangsen.netsupport.telephony.NetworkStatus;
import com.hbmcc.wangsen.netsupport.telephony.UeStatus;
import com.hbmcc.wangsen.netsupport.telephony.cellinfo.LteCellInfo;
import com.hbmcc.wangsen.netsupport.ui.fragment.MainFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.fifth.FifthData.FifthProblemData;
import com.hbmcc.wangsen.netsupport.ui.fragment.second.SecondTabFragment;
import com.hbmcc.wangsen.netsupport.util.FileUtils;
import com.hbmcc.wangsen.netsupport.util.HttpUtil;
import com.hbmcc.wangsen.netsupport.util.NumberFormat;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

import static com.hbmcc.wangsen.netsupport.base.EnumHttpQ.FIRST_LOGIN;
import static com.hbmcc.wangsen.netsupport.telephony.NetworkStatus.netOperators;

public class FirstTabFragment extends BaseMainFragment {
    public static FirstTabFragment fragment;
    private static final String TAG = "FirstTabFragment";
    ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
    RecentRecordAdapter recentRecordAdapter;
    NeighbourCellAdapter neighbourCellAdapter;
    long recentNetworkStatusCnt;
    long recentSumSignalStrength;
    double recentAvgSignalStrength;
    private Toolbar toolbarMain;
    private TextView textViewFragmentFirstTabOperator;
    private TextView textViewFragmentFirstTabIMSI;
    private TextView textViewFragmentFirstTabIMEI;
    private TextView textViewFragmentFirstTabUEModel;
    private TextView textViewFragmentFirstTabAndroidVersion;
    private TextView textViewFragmentFirstTabphonenumber;
    private TextView textViewFragmentFirstTabnetWorkType;
    private TextView textViewFragmentFirstTabCurrentLocName;
    private TextView textViewFragmentFirstTabLongitude;
    private TextView textViewFragmentFirstTabLatitude;
    private TextView textViewFragmentFirstTabTAC;
    private TextView textViewFragmentFirstTabPCI;
    private TextView textViewFragmentFirstTabCGI;
    private TextView textViewFragmentFirstTabEarFcn;
    private TextView textViewFragmentFirstTabBand;
    private TextView textViewFragmentFirstTabFrequency;
    private TextView textViewFragmentFirstTabRSRP;
    private TextView textViewFragmentFirstTabRSRQ;
    private TextView textViewFragmentFirstTabSINR;
    private TextView textViewFragmentFirstTabAltitude;
    private TextView textViewFragmentFirstTabCellChsName;
    private TextView textViewFragmentFirstTabRecentAvgSignalStrength;
    private TextView textViewFragmentFirstTabNeighbourCell;
    private TextView textViewFragmentFirstTabExport;
    private RecyclerView recyclerViewFragmentFirstTabRecentRecord;
    private RecyclerView recyclerViewFragmentFirstTabNeighbourCellInfo;
    public List<UeStatus> recentueStatusRecordList;
    private List<NetworkStatus> recentNetworkStatusRecordList;
    private List<LteCellInfo> neighbourCellList;
    private TextView btnFragmentFirstTabConvert;
    private LinearLayout linearlayoutFragmentFirstTabRecentRecord;
    private LinearLayout linearlayoutFragmentFirstTabNeighbourCell;
    private List<LteBasestationCell> litepalLteBasestationCellList;
    private long startTime; //起始时间
    private long endTime;//结束时间
    private String st, str = "";
    private int indexStar = 0;
    public static int indexStartDot = 0;

    private Button btn_fragment_bar_tab_map;

    private GestureDetector mGestureDetector;
    private int verticalMinistance = 100;            //水平最小识别距离
    private int minVelocity = 10;            //最小识别速度

    private static final String TAG1 = "HomeFragment";


    public static FirstTabFragment newInstance() {
        Bundle args = new Bundle();
        if (fragment == null) {
            fragment = new FirstTabFragment();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_tab, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mGestureDetector = new GestureDetector(App.getContext(), new LearnGestureListener());
//        为fragment添加OnTouchListener监听器
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        EventBusActivityScope.getDefault(_mActivity).register(this);
//        toolbarMain = view.findViewById(R.id.toolbar);
//        toolbarMain.setTitle(getString(R.string.app_name));
        textViewFragmentFirstTabOperator = view.findViewById(R.id.textView_fragment_first_tab_operator);
        textViewFragmentFirstTabIMSI = view.findViewById(R.id.textView_fragment_first_tab_IMSI);
        textViewFragmentFirstTabIMEI = view.findViewById(R.id.textView_fragment_first_tab_IMEI);
        textViewFragmentFirstTabUEModel = view.findViewById(R.id.textView_fragment_first_tab_uemodel);
        textViewFragmentFirstTabAndroidVersion = view.findViewById(R.id.textView_fragment_first_tab_androidversion);
        textViewFragmentFirstTabphonenumber = view.findViewById(R.id.textView_fragment_first_tab_phonenumber);
        textViewFragmentFirstTabnetWorkType = view.findViewById(R.id.textView_fragment_first_tab_netWorkType);
        textViewFragmentFirstTabCurrentLocName = view.findViewById(R.id.textView_fragment_first_tab_currentlocname);
        textViewFragmentFirstTabLongitude = view.findViewById(R.id.textView_fragment_first_tab_longitude);
        textViewFragmentFirstTabLatitude = view.findViewById(R.id.textView_fragment_first_tab_latitude);
        textViewFragmentFirstTabTAC = view.findViewById(R.id.textView_fragment_first_tab_tac);
        textViewFragmentFirstTabPCI = view.findViewById(R.id.textView_fragment_first_tab_pci);
        textViewFragmentFirstTabCGI = view.findViewById(R.id.textView_fragment_first_tab_cgi);
        textViewFragmentFirstTabEarFcn = view.findViewById(R.id.textView_fragment_first_tab_earfcn);
        textViewFragmentFirstTabBand = view.findViewById(R.id.textView_fragment_first_tab_band);
        textViewFragmentFirstTabFrequency = view.findViewById(R.id.textView_fragment_first_tab_frequency);
        textViewFragmentFirstTabRSRP = view.findViewById(R.id.textView_fragment_first_tab_RSRP);
        textViewFragmentFirstTabRSRQ = view.findViewById(R.id.textView_fragment_first_tab_RSRQ);
        textViewFragmentFirstTabSINR = view.findViewById(R.id.textView_fragment_first_tab_SINR);
        textViewFragmentFirstTabAltitude = view.findViewById(R.id.textView_fragment_first_tab_altitude);
        textViewFragmentFirstTabCellChsName = view.findViewById(R.id.textView_fragment_first_tab_cellchsname);
        btnFragmentFirstTabConvert = view.findViewById(R.id.textView_btn_fragment_first_tab_convert);
        linearlayoutFragmentFirstTabRecentRecord = view.findViewById(R.id
                .linearlayout_fragment_first_tab_recent_record);
        linearlayoutFragmentFirstTabNeighbourCell = view.findViewById(R.id
                .linearlayout_fragment_first_tab_neighbour_cell);
        textViewFragmentFirstTabRecentAvgSignalStrength = view.findViewById(R.id.textView_fragment_first_tab_recent_avg_signal_strength);
        textViewFragmentFirstTabNeighbourCell = view.findViewById(R.id
                .textView_fragment_first_tab_neighbour_cell);
        recyclerViewFragmentFirstTabRecentRecord = view.findViewById(R.id.recyclerView_fragment_first_tab_recent_record);
        recyclerViewFragmentFirstTabNeighbourCellInfo = view.findViewById(R.id.recyclerView_fragment_first_tab_neighbour_cell_info);
        textViewFragmentFirstTabExport = view.findViewById(R.id.textView_fragment_first_tab_recent_export);
        btn_fragment_bar_tab_map = view.findViewById(R.id.btn_fragment_bar_tab_map);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        recentNetworkStatusRecordList = new ArrayList<>();
        recentueStatusRecordList = new ArrayList<>();
        neighbourCellList = new ArrayList<>();
        initRecyclerView();
        recentNetworkStatusCnt = 0;
        recentAvgSignalStrength = 0;
        FileUtils.initialStorage();


        btn_fragment_bar_tab_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) getParentFragment()).startBrotherFragment
                        (SecondTabFragment.newInstance("地图"));
            }
        });

        btnFragmentFirstTabConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearlayoutFragmentFirstTabRecentRecord.getVisibility() == View.VISIBLE) {
                    linearlayoutFragmentFirstTabRecentRecord.setVisibility(View.GONE);
                    linearlayoutFragmentFirstTabNeighbourCell.setVisibility(View.VISIBLE);
                    textViewFragmentFirstTabRecentAvgSignalStrength.setVisibility(View.GONE);
                    textViewFragmentFirstTabNeighbourCell.setVisibility(View.VISIBLE);
                    btnFragmentFirstTabConvert.setText("切换主小区 ");
                } else {
                    linearlayoutFragmentFirstTabRecentRecord.setVisibility(View.VISIBLE);
                    linearlayoutFragmentFirstTabNeighbourCell.setVisibility(View.GONE);
                    textViewFragmentFirstTabRecentAvgSignalStrength.setVisibility(View.VISIBLE);
                    textViewFragmentFirstTabNeighbourCell.setVisibility(View.GONE);
                    btnFragmentFirstTabConvert.setText("切换邻区 ");
                }
            }
        });

        textViewFragmentFirstTabExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewFragmentFirstTabExport.getText().equals("开始保存")) {
                    textViewFragmentFirstTabExport.setText("导出LOG");
                    indexStar = recentueStatusRecordList.size();
                    textViewFragmentFirstTabExport.setTextColor(0xffff0000);
                } else {
                    textViewFragmentFirstTabExport.setText("开始保存");
                    textViewFragmentFirstTabExport.setTextColor(0xff0000FF);
                    exportDot(indexStar);
                }
            }
        });

//        login();
    }


    public void startDot() {
        indexStartDot = recentueStatusRecordList.size();
    }

    public void exportDot(final int indexi) {
        if (indexi > 0) {
            startTime = System.currentTimeMillis();
            if (!FileUtils.isFileExist(FileUtils.getAppPath())) {
                FileUtils.createSDDirs(FileUtils.getAppPath());
            }
            if (FileUtils.isFileExist(FileUtils.getAppPath())) {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowString = dateFormat.format(date);
                final File pathExport = new File(FileUtils.getAppPath() + nowString + "测试log.csv");
                newCachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        FileIOUtils.writeFileFromString(pathExport, "time," + "longitude," + "latitude," +
                                "RSRP," + "ENBID," + "CellId," + "PCI," + "SINR," + "RSRQ," + "TAC," + "EarFcn," + "location," +
                                "NeighbourEarFcn," + "NeighbourPCI," + "NeighbourRSRP," + "NeighbourEarFcn2," + "NeighbourPCI2,"
                                + "NeighbourRSRP2," + "\n", true);
                        for (int i = indexi; i < recentueStatusRecordList.size(); i++) {
                            st = recentueStatusRecordList.get(i).networkStatus.time + "," +
                                    recentueStatusRecordList.get(i).locationStatus.longitudeWgs84 + "," +
                                    recentueStatusRecordList.get(i).locationStatus.latitudeWgs84 + "," +
                                    recentueStatusRecordList.get(i).networkStatus.lteServingCellTower.signalStrength + "," +
                                    recentueStatusRecordList.get(i).networkStatus.lteServingCellTower.enbId + "," +
                                    recentueStatusRecordList.get(i).networkStatus.lteServingCellTower.enbCellId + "," +
                                    recentueStatusRecordList.get(i).networkStatus.lteServingCellTower.pci + "," +
                                    recentueStatusRecordList.get(i).networkStatus.lteServingCellTower.sinr + "," +
                                    recentueStatusRecordList.get(i).networkStatus.lteServingCellTower.rsrq + "," +
                                    recentueStatusRecordList.get(i).networkStatus.lteServingCellTower.tac + "," +
                                    recentueStatusRecordList.get(i).networkStatus.lteServingCellTower.lteEarFcn + "," +
                                    recentueStatusRecordList.get(i).locationStatus.city + recentueStatusRecordList.get(i).locationStatus.district +
                                    recentueStatusRecordList.get(i).locationStatus.street +
                                    recentueStatusRecordList.get(i).locationStatus.streetNumber + " ";
                            if (recentueStatusRecordList.get(i).networkStatus.lteNeighbourCellTowers.size() > 0) {
                                for (LteCellInfo lteCellInfo : recentueStatusRecordList.get(i).networkStatus.lteNeighbourCellTowers) {
                                    str = str + lteCellInfo.lteEarFcn + "," + lteCellInfo.pci + "," + lteCellInfo.signalStrength + ",";
                                }
                                st = st + "," + str;
                            }
                            FileIOUtils.writeFileFromString(pathExport, st + "\n", true);
                            st = str = "";
                        }
                    }
                });
                endTime = System.currentTimeMillis();
                final long usedTime = (int) ((endTime - startTime) / 1000);
                Toast.makeText(App.getContext(), "/优易/" + nowString +
                        "测试log.csv" + "\n导出成功\t\t共计 " + (recentueStatusRecordList.size() - indexi) + " 条数据\n用时 " +
                        usedTime + " 秒", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(final UpdateUeStatusEvent updateUEStatusEvent) {
        newCachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (updateUEStatusEvent.ueStatus.networkStatus != null && updateUEStatusEvent.ueStatus.networkStatus.lteServingCellTower != null) {
                    recentNetworkStatusRecordList.add(0, updateUEStatusEvent.ueStatus.networkStatus);
                    recentueStatusRecordList.add(updateUEStatusEvent.ueStatus);
                    neighbourCellList.clear();
                    if (updateUEStatusEvent.ueStatus.networkStatus.lteNeighbourCellTowers != null) {
                        neighbourCellList.addAll(updateUEStatusEvent.ueStatus.networkStatus
                                .lteNeighbourCellTowers);
                        if (updateUEStatusEvent.ueStatus.networkStatus.lteServingCellTower.cellId != 0) {
                            litepalLteBasestationCellList = LitePal.where("eci = ?", updateUEStatusEvent.ueStatus
                                    .networkStatus.lteServingCellTower.cellId + "").find(LteBasestationCell.class);
                        }
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (FirstTabFragment.this.isVisible()) {
                                    textViewFragmentFirstTabnetWorkType.setText(updateUEStatusEvent.ueStatus.networkStatus.getCellType());
                                    textViewFragmentFirstTabOperator.setText(netOperators);
                                    textViewFragmentFirstTabIMSI.setText(updateUEStatusEvent.ueStatus.networkStatus.imsi + "");
                                    textViewFragmentFirstTabIMEI.setText(updateUEStatusEvent.ueStatus.networkStatus.imei + "");
                                    textViewFragmentFirstTabUEModel.setText(updateUEStatusEvent.ueStatus.networkStatus.hardwareModel + "");
                                    textViewFragmentFirstTabAndroidVersion.setText(updateUEStatusEvent.ueStatus.networkStatus.androidVersion + "");
                                    textViewFragmentFirstTabphonenumber.setText(NetworkStatus.phonenumber + "");
                                    textViewFragmentFirstTabLongitude.setText(NumberFormat.doubleFormat(updateUEStatusEvent.ueStatus.locationStatus
                                            .longitudeWgs84, 5) + "");
                                    textViewFragmentFirstTabLatitude.setText(NumberFormat.doubleFormat
                                            (updateUEStatusEvent.ueStatus.locationStatus
                                                    .latitudeWgs84, 5) + "");
                                    textViewFragmentFirstTabAltitude.setText((int) (updateUEStatusEvent.ueStatus.locationStatus.altitude) + "米");
                                    textViewFragmentFirstTabCurrentLocName.setText(updateUEStatusEvent.ueStatus.locationStatus
                                            .city + updateUEStatusEvent.ueStatus.locationStatus.district + updateUEStatusEvent
                                            .ueStatus.locationStatus.street + updateUEStatusEvent.ueStatus.locationStatus
                                            .streetNumber);
                                    textViewFragmentFirstTabTAC.setText(updateUEStatusEvent.ueStatus.networkStatus
                                            .lteServingCellTower.tac + "");
                                    textViewFragmentFirstTabPCI.setText(updateUEStatusEvent.ueStatus.networkStatus
                                            .lteServingCellTower.pci + "");
                                    textViewFragmentFirstTabCGI.setText(updateUEStatusEvent.ueStatus.networkStatus
                                            .lteServingCellTower.enbId + "-" + updateUEStatusEvent.ueStatus.networkStatus
                                            .lteServingCellTower.enbCellId);
                                    textViewFragmentFirstTabEarFcn.setText(updateUEStatusEvent.ueStatus.networkStatus
                                            .lteServingCellTower.lteEarFcn + "");
                                    textViewFragmentFirstTabRSRP.setText(updateUEStatusEvent.ueStatus.networkStatus
                                            .lteServingCellTower.signalStrength + "");
                                    textViewFragmentFirstTabRSRQ.setText(updateUEStatusEvent.ueStatus.networkStatus
                                            .lteServingCellTower.rsrq + "");
                                    textViewFragmentFirstTabSINR.setText(updateUEStatusEvent.ueStatus.networkStatus
                                            .lteServingCellTower.sinr + "");
                                    textViewFragmentFirstTabBand.setText(LteBand.getBand
                                            (updateUEStatusEvent.ueStatus.networkStatus
                                                    .lteServingCellTower.lteEarFcn) + "");
                                    if (LteBand.getDuplexMode(updateUEStatusEvent.ueStatus.networkStatus
                                            .lteServingCellTower.lteEarFcn) == LteBand.TDD) {
                                        textViewFragmentFirstTabFrequency.setText(LteBand.getDlCenterFreq(
                                                updateUEStatusEvent.ueStatus.networkStatus
                                                        .lteServingCellTower.lteEarFcn) + "");
                                    } else if (LteBand.getDuplexMode(updateUEStatusEvent.ueStatus.networkStatus
                                            .lteServingCellTower.lteEarFcn) == LteBand.FDD) {
                                        textViewFragmentFirstTabFrequency.setText(LteBand.getDlCenterFreq(
                                                updateUEStatusEvent.ueStatus.networkStatus
                                                        .lteServingCellTower.lteEarFcn) + "/" + LteBand
                                                .getUlCenterFreq(
                                                        updateUEStatusEvent.ueStatus.networkStatus
                                                                .lteServingCellTower.lteEarFcn));
                                    }
                                    if (litepalLteBasestationCellList.isEmpty()) {
                                        textViewFragmentFirstTabCellChsName.setText("基站数据库无此小区");
                                    } else {
                                        textViewFragmentFirstTabCellChsName.setText(litepalLteBasestationCellList.get(0).getName() + "");
                                    }
                                    if (recentNetworkStatusCnt != 0) {
                                        recentAvgSignalStrength = NumberFormat.doubleFormat((double) recentSumSignalStrength /
                                                (double)
                                                        recentNetworkStatusCnt, 1);
                                    }
                                    textViewFragmentFirstTabRecentAvgSignalStrength.setText
                                            (recentNetworkStatusCnt + "条 平均电平" + (int) (recentAvgSignalStrength) + "dbm");

                                    if (recentRecordAdapter != null && neighbourCellAdapter != null) {
                                        recentRecordAdapter.notifyDataSetChanged();
                                        neighbourCellAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        });
                    }
                }
            }

        });
        try {
            if (updateUEStatusEvent.ueStatus.networkStatus.lteServingCellTower != null) {
                recentNetworkStatusCnt = recentNetworkStatusCnt + 1;
                recentSumSignalStrength = recentSumSignalStrength + updateUEStatusEvent.ueStatus
                        .networkStatus.lteServingCellTower.signalStrength;
            }
        } catch (Exception e) {
            Toast.makeText(App.getContext(), "网络出现问题，请检查", Toast.LENGTH_LONG).show();
        }
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

    private void initRecyclerView() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        recyclerViewFragmentFirstTabRecentRecord.setLayoutManager(layoutManager1);
        recyclerViewFragmentFirstTabRecentRecord.setFocusable(false);
        recentRecordAdapter = new RecentRecordAdapter(recentNetworkStatusRecordList);
        recyclerViewFragmentFirstTabRecentRecord.setAdapter(recentRecordAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recyclerViewFragmentFirstTabNeighbourCellInfo.setLayoutManager(layoutManager2);
        recyclerViewFragmentFirstTabNeighbourCellInfo.setFocusable(false);
        neighbourCellAdapter = new NeighbourCellAdapter(neighbourCellList);
        recyclerViewFragmentFirstTabNeighbourCellInfo.setAdapter(neighbourCellAdapter);
    }


    //设置手势识别监听器
    class LearnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > verticalMinistance && Math.abs(velocityX) > minVelocity) {
                if (MainFragment.newSelectedPosition >= 3) {
                    MainFragment.newSelectedPosition = 0;
                } else {
                    MainFragment.newSelectedPosition = MainFragment.newSelectedPosition + 1;
                }
                showHideFragment(MainFragment.mFragments[MainFragment.newSelectedPosition],
                        MainFragment.mFragments[MainFragment.lastSelectedPosition]);
                MainFragment.bottomNavigationBar.setFirstSelectedPosition(MainFragment.newSelectedPosition).initialise();
                MainFragment.lastSelectedPosition = MainFragment.newSelectedPosition;

            } else if (e2.getX() - e1.getX() > verticalMinistance && Math.abs(velocityX) > minVelocity) {
                if (MainFragment.newSelectedPosition <= 0) {
                    MainFragment.newSelectedPosition = 3;
                } else {
                    MainFragment.newSelectedPosition = MainFragment.newSelectedPosition - 1;
                }
                showHideFragment(MainFragment.mFragments[MainFragment.newSelectedPosition], MainFragment.mFragments[MainFragment.lastSelectedPosition]);
                MainFragment.bottomNavigationBar.setFirstSelectedPosition(MainFragment.newSelectedPosition)
                        .initialise();
                MainFragment.lastSelectedPosition = MainFragment.newSelectedPosition;
            }
            return true;
        }
    }

    public void showToast(String text) {
        Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT).show();
    }


//    public void handle() {
//        Message msg = new Message();
//        mhandler.sendMessage(msg);
//    }
//
//    Handler mhandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (HttpUtil.qresult == null) {
//                showToast("登录失败");
//            } else {
//                showToast("登录成功");
//            }
//
//        }
//    };

}
