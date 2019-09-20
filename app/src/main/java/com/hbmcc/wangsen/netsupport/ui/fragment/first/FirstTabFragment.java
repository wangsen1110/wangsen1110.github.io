package com.hbmcc.wangsen.netsupport.ui.fragment.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileIOUtils;
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
import com.hbmcc.wangsen.netsupport.ui.fragment.third.ThirdTabFragment;
import com.hbmcc.wangsen.netsupport.util.FileUtils;
import com.hbmcc.wangsen.netsupport.util.NumberFormat;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

public class FirstTabFragment extends BaseMainFragment {
    private static final String TAG = "FirstTabFragment";
    ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
    RecentRecordAdapter recentRecordAdapter;
    NeighbourCellAdapter neighbourCellAdapter;
    long recentNetworkStatusCnt = 1;
    long recentSumSignalStrength = 1;
    double recentAvgSignalStrength;
    private Toolbar toolbarMain;
    private TextView textViewFragmentFirstTabOperator;
    private TextView textViewFragmentFirstTabIMSI;
    private TextView textViewFragmentFirstTabIMEI;
    private TextView textViewFragmentFirstTabUEModel;
    private TextView textViewFragmentFirstTabAndroidVersion;
    private TextView textViewFragmentFirstTabphonenumber;
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
    private List<UeStatus> recentueStatusRecordList;
    private List<NetworkStatus> recentNetworkStatusRecordList;
    private List<LteCellInfo> neighbourCellList;
    private TextView btnFragmentFirstTabConvert;
    private LinearLayout linearlayoutFragmentFirstTabRecentRecord;
    private LinearLayout linearlayoutFragmentFirstTabNeighbourCell;
    private List<LteBasestationCell> litepalLteBasestationCellList;
    private long startTime; //起始时间
    private long endTime;//结束时间

    public static FirstTabFragment newInstance() {
        Bundle args = new Bundle();
        FirstTabFragment fragment = new FirstTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_tab, container,
                false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        EventBusActivityScope.getDefault(_mActivity).register(this);
        toolbarMain = view.findViewById(R.id.toolbar);
        toolbarMain.setTitle(getString(R.string.app_name));
        textViewFragmentFirstTabOperator = view.findViewById(R.id.textView_fragment_first_tab_operator);
        textViewFragmentFirstTabIMSI = view.findViewById(R.id.textView_fragment_first_tab_IMSI);
        textViewFragmentFirstTabIMEI = view.findViewById(R.id.textView_fragment_first_tab_IMEI);
        textViewFragmentFirstTabUEModel = view.findViewById(R.id.textView_fragment_first_tab_uemodel);
        textViewFragmentFirstTabAndroidVersion = view.findViewById(R.id.textView_fragment_first_tab_androidversion);
        textViewFragmentFirstTabphonenumber = view.findViewById(R.id.textView_fragment_first_tab_phonenumber);
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
                if (recentueStatusRecordList.size() > 0) {
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
                                FileIOUtils.writeFileFromString(pathExport, "时间," + "经度," + "纬度," +
                                        "RSRP," + "ENBID," + "CellId," + "PCI," + "SINR," + "RSRQ," + "TAC," + "频点," +
                                        "邻区频点," + "邻区PCI," + "邻区RSRP," + "位置" + "\n", true);
                                for (UeStatus ueStatus : recentueStatusRecordList) {
                                    String st = ueStatus.networkStatus.time + "," +
                                            ueStatus.locationStatus.longitudeWgs84 + "," +
                                            ueStatus.locationStatus.latitudeWgs84 + "," +
                                            ueStatus.networkStatus.lteServingCellTower.signalStrength + "," +
                                            ueStatus.networkStatus.lteServingCellTower.enbId + "," +
                                            ueStatus.networkStatus.lteServingCellTower.enbCellId + "," +
                                            ueStatus.networkStatus.lteServingCellTower.pci + "," +
                                            ueStatus.networkStatus.lteServingCellTower.sinr + "," +
                                            ueStatus.networkStatus.lteServingCellTower.rsrq + "," +
                                            ueStatus.networkStatus.lteServingCellTower.tac + "," +
                                            ueStatus.networkStatus.lteServingCellTower.lteEarFcn + "," +
                                            ((ueStatus.networkStatus.lteNeighbourCellTowers.size() > 0) ?
                                                    ueStatus.networkStatus.lteNeighbourCellTowers.get(0).lteEarFcn + "," +
                                                            ueStatus.networkStatus.lteNeighbourCellTowers.get(0).pci + "," +
                                                            ueStatus.networkStatus.lteNeighbourCellTowers.get(0).signalStrength + ","
                                                    : "无,无,无,") +
                                            ueStatus.locationStatus.city + ueStatus.locationStatus.district +
                                            ueStatus.locationStatus.street + ueStatus.locationStatus.streetNumber
                                            + "\n";
                                    FileIOUtils.writeFileFromString(pathExport, st, true);
                                }
                            }
                        });
                        endTime = System.currentTimeMillis();
                        final long usedTime = (int) ((endTime - startTime) / 1000);
                        Toast.makeText(App.getContext(), "/优易/" + nowString +
                                "测试log.csv" + "\n导出成功 \n用时 " +
                                usedTime + " 秒", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Reselected Tab
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainFragment.FIRST) {
            return;
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
                if (updateUEStatusEvent.ueStatus.networkStatus != null && updateUEStatusEvent.ueStatus != null
                        && updateUEStatusEvent.ueStatus.networkStatus.lteServingCellTower != null) {
                    recentNetworkStatusRecordList.add(0, updateUEStatusEvent.ueStatus.networkStatus);
                    recentueStatusRecordList.add(0, updateUEStatusEvent.ueStatus);
                    neighbourCellList.clear();
                    if (updateUEStatusEvent.ueStatus.networkStatus.lteNeighbourCellTowers
                            != null) {
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
                                    textViewFragmentFirstTabOperator.setText(updateUEStatusEvent.ueStatus.locationStatus
                                            .operators + "");
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
                                        textViewFragmentFirstTabCellChsName.setText(litepalLteBasestationCellList.get(0)
                                                .getName() + "");
                                    }
                                    recentAvgSignalStrength = NumberFormat.doubleFormat((double) recentSumSignalStrength /
                                            (double)
                                                    recentNetworkStatusCnt, 1);
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
                        .networkStatus
                        .lteServingCellTower.signalStrength;
            }
        } catch (Exception e) {
            Toast.makeText(App.getContext(), "网络出现问题，请检查", Toast.LENGTH_LONG).show();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        recyclerViewFragmentFirstTabRecentRecord.setLayoutManager(layoutManager1);
        recentRecordAdapter = new RecentRecordAdapter(recentNetworkStatusRecordList);
        recyclerViewFragmentFirstTabRecentRecord.setAdapter(recentRecordAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recyclerViewFragmentFirstTabNeighbourCellInfo.setLayoutManager(layoutManager2);
        neighbourCellAdapter = new NeighbourCellAdapter(neighbourCellList);
        recyclerViewFragmentFirstTabNeighbourCellInfo.setAdapter(neighbourCellAdapter);

    }
}
