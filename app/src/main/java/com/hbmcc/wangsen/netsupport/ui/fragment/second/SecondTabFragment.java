package com.hbmcc.wangsen.netsupport.ui.fragment.second;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
//import com.baidu.mapapi.search.poi.PoiCitySearchOption;
//import com.baidu.mapapi.search.poi.PoiDetailResult;
//import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
//import com.baidu.mapapi.search.poi.PoiIndoorResult;
//import com.baidu.mapapi.search.poi.PoiResult;
//import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.base.BaseBackFragment;
import com.hbmcc.wangsen.netsupport.database.LteBasesCustom;
import com.hbmcc.wangsen.netsupport.database.LteBasesGrid;
import com.hbmcc.wangsen.netsupport.database.LteBasesTrack;
import com.hbmcc.wangsen.netsupport.database.LteBasestationCell;
import com.hbmcc.wangsen.netsupport.event.UpdateUeStatusEvent;
import com.hbmcc.wangsen.netsupport.telephony.LocationStatus;
import com.hbmcc.wangsen.netsupport.telephony.NetworkStatus;
import com.hbmcc.wangsen.netsupport.telephony.UeStatus;
import com.hbmcc.wangsen.netsupport.ui.fragment.MainFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.first.FirstTabFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.other.LteBasestationcellDetailInfoFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.ThirdTabFragment;
import com.hbmcc.wangsen.netsupport.util.LocatonConverter;
import com.hbmcc.wangsen.netsupport.util.NumberFormat;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;

import static android.content.Context.SENSOR_SERVICE;

public class SecondTabFragment extends BaseBackFragment implements SensorEventListener {
    ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
    public static final double DISTANCE_OFFSET = 0.0105;//半径1公里
    public static final float MARKER_ALPHA = 0.7f;
    public static final double MAPDISTANCE = 1300;
    private static final String TAG = "SecondTabFragment";
    private static final String ARG_TITLE = "arg_title";
    private LatLng ll;
    LteBasestationCell lteBasestationCell;
    LteBasesCustom lteBasesCustom;
    LteBasesTrack lteBasesTrack;
    LteBasesGrid lteBasesGrid;
    // 是否首次定位
    private boolean isFirstLoc = true;
    private MapView mMapView;
    private BaiduMap mBaiduMap;//百度map对象；
    // UI相关
    private Button btnChangeMapType;
    private Button btnLocation;
    private Button btnDot;
    private Button btnDot1;
    private Button btnSearchStart;
    private Button btn_fragment_second_tab_back;
    private boolean dotType = false;
    private boolean booleanSearchStart = true;

    private CheckBox checkBoxBaiduHeatMap;
    private CheckBox checkboxFragmentSecondTabDisplayLTECell;//4G小区复选框
    private CheckBox checkboxFragmentSecondCustom; //规划图复选框
    private CheckBox checkboxFragmentSecondTrack;
    private CheckBox checkboxFragmentSecondGrid;
    private TextView textViewCurrentPositionLonLat;
    private TextView textViewCurrentPositionDefinition;
    private TextView textViewFragmentSecondTabClickedCell;
    private TextView textViewFragmentSecondTabCGI;
    private EditText editTextLteLatlonSearch;

    //    public PoiSearch mPoiSearch;
    public LocationStatus locationStatus;
    private Toolbar mToolbar;

    private MyLocationConfiguration.LocationMode mCurrentLocationMode;
    private int mMapType;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private MyLocationData locData;
    private static List<LteBasestationCell> lteBasestationCellList = new ArrayList<>();
    private static List<LteBasestationCell> currentLteBasestationCellList = new ArrayList<>();
    private static List<LteBasesCustom> lteBasesCustomList = new ArrayList<>();//规划自定义数据集合
    private static List<LteBasesTrack> lteBasesTrackList = new ArrayList<>();//规划自定义数据集合
    private static List<LteBasesGrid> lteBasesGridList1 = new ArrayList<>();
    private MarkerOptions markerOptions = new MarkerOptions();//构建MarkerOption，用于在地图上添加Marker
    private MarkerOptions markerOptionscustom = new MarkerOptions();
    private MarkerOptions markerOptionsttrack = new MarkerOptions();
    private MarkerOptions markerOptionstdot = new MarkerOptions();

    private Marker marker;
    private Marker markercustom;
    private Marker markertrack;
    private PolygonOptions mPolygonOptions1;

    private Marker lastSelectedMarker;
    private List<Marker> markerList = new ArrayList<Marker>();
    private List<MarkerOptions> markerListcustom = new ArrayList<>();
    private List<MarkerOptions> markerListtrack = new ArrayList<>();
    private List<MarkerOptions> markerlisttdot = new ArrayList<>();
    private List<PolygonOptions> markerListgrid = new ArrayList<>();
    // 主服务小区的连接线
    private Polyline mPolyline;
    private LatLng startlatlng;
    private ThirdTabFragment thirdTabFragment;
    private String mTitle;

    private List<LatLng> gesturepoint = new ArrayList<LatLng>();
    private List<LatLng> mapstatusdistance = new ArrayList<LatLng>();
    //初始化marker信息
    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor markerLteTDDOutside = BitmapDescriptorFactory
            .fromResource(R.drawable.roomout_4g_red);
    private BitmapDescriptor markerLteTDDOutsideSelected = BitmapDescriptorFactory
            .fromResource(R.drawable.roomout_4g_red_over);
    private BitmapDescriptor markerLteTDDIndoor = BitmapDescriptorFactory
            .fromResource(R.drawable.roomin_4g);
    private BitmapDescriptor markerLteTDDIndoorSelected = BitmapDescriptorFactory
            .fromResource(R.drawable.roomin_4g_over);
    private BitmapDescriptor markerLteFDD900Outside = BitmapDescriptorFactory
            .fromResource(R.drawable.roomout_4g_green);
    private BitmapDescriptor markerLteFDD900OutsideSelected = BitmapDescriptorFactory
            .fromResource(R.drawable.roomout_4g_green_over);
    private BitmapDescriptor markerLteFDD1800Outside = BitmapDescriptorFactory
            .fromResource(R.drawable.roomout_4g_yellow);
    private BitmapDescriptor markerLteFDD1800OutsideSelected = BitmapDescriptorFactory
            .fromResource(R.drawable.roomout_4g_yellow_over);

    private BitmapDescriptor markerLetcustom = BitmapDescriptorFactory
            .fromResource(R.drawable.roomout_baidu_project);

    private BitmapDescriptor markerLettrack75 = BitmapDescriptorFactory
            .fromResource(R.drawable.marker_rsrp_75);
    private BitmapDescriptor markerLettrack85 = BitmapDescriptorFactory
            .fromResource(R.drawable.marker_rsrp_85);
    private BitmapDescriptor markerLettrack95 = BitmapDescriptorFactory
            .fromResource(R.drawable.marker_rsrp_95);
    private BitmapDescriptor markerLettrack105 = BitmapDescriptorFactory
            .fromResource(R.drawable.marker_rsrp_105);
    private BitmapDescriptor markerLettrack115 = BitmapDescriptorFactory
            .fromResource(R.drawable.marker_rsrp_115);
    private BitmapDescriptor markerLettrack125 = BitmapDescriptorFactory
            .fromResource(R.drawable.marker_rsrp_125);
    private BitmapDescriptor markerLettrack126 = BitmapDescriptorFactory
            .fromResource(R.drawable.marker_rsrp_126);
    private static SecondTabFragment fragment;

    public static SecondTabFragment newInstance(String title) {

//        Bundle args = new Bundle();
        if (fragment == null) {
            fragment = new SecondTabFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fragment.setArguments(bundle);

//        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_tab, container,
                false);
        mapstatusdistance.add(new LatLng(112.45703, 30.788457));
        initView(view);
        return view;
    }

//    /**
//     * Reselected Tab
//     */
//    @Subscribe
//    public void onTabSelectedEvent(TabSelectedEvent event) {
//        if (event.position != MainFragment.SECOND) {
//            return;
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
//        mBaiduMap.setMyLocationEnabled(false);
//        markerLteTDDOutside.recycle();
//        markerLteFDD900Outside.recycle();
//        markerLteFDD1800Outside.recycle();
//        markerLteTDDIndoor.recycle();
    }

    @Override
    public void onPause() {
//        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
//        mMapView.onResume();
        super.onResume();
        mapstatusdistance.clear();
        mapstatusdistance.add(new LatLng(112.45703, 30.788457));
        goToCurrentLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mMapView.onDestroy();
//        mMapView = null;
    }

    @Override
    public void onStop() {
        //取消注册传感器监听
//        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    private void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        //初始视图状态
        EventBusActivityScope.getDefault(_mActivity).register(this);

        textViewCurrentPositionLonLat = view.findViewById(R.id.textView_fragment_second_tab_current_position_lon_lat);
        textViewCurrentPositionDefinition = view.findViewById(R.id
                .textView_fragment_second_tab_current_position_definition);
        textViewFragmentSecondTabClickedCell = view.findViewById(R.id.textView_fragment_second_tab_clicked_cell);
        textViewFragmentSecondTabCGI = view.findViewById(R.id.textView_fragment_second_tab_current_id);
        btnChangeMapType = view.findViewById(R.id.btn_fragment_second_tab_change_map_type);
        btnLocation = view.findViewById(R.id.btn_fragment_second_tab_location);
//        checkBoxTraffic = view.findViewById(R.id.checkBox_fragment_second_tab_traffic);
        //热力图绑定
        checkBoxBaiduHeatMap = view.findViewById(R.id.checkbox_fragment_second_tab_baidu_heat_map);
        //4G小区绑定
        checkboxFragmentSecondTabDisplayLTECell = view.findViewById(R.id
                .checkbox_fragment_second_tab_LTE_cell);
        //绑定规划
        checkboxFragmentSecondCustom = view.findViewById(R.id.checkbox_fragment_second_tab_project);
        //绑定轨迹
        checkboxFragmentSecondTrack = view.findViewById(R.id.checkbox_fragment_second_tab_track);
        //绑定珊格
        checkboxFragmentSecondGrid = view.findViewById(R.id.checkbox_fragment_second_tab_grid);

        btnDot = view.findViewById(R.id.btn_fragment_second_tab_dot);
        btnDot1 = view.findViewById(R.id.btn_fragment_second_tab_dot1);
        btnSearchStart = view.findViewById(R.id.btn_fragment_second_tab_search_start);
        btn_fragment_second_tab_back = view.findViewById(R.id.btn_fragment_second_tab_back);

        editTextLteLatlonSearch = view.findViewById(R.id.editText_fragment_lte_latlon_search);

        //获取传感器管理服务
        mSensorManager = (SensorManager) _mActivity.getSystemService(SENSOR_SERVICE);

        // 地图初始化
        mMapView = view.findViewById(R.id.bmapView_fragment_second_tab_bmapview);
        mBaiduMap = mMapView.getMap();
        ThirdTabFragment.thirdlatLng = mMapView.getMap().getMapStatus().target;
//        mPoiSearch = PoiSearch.newInstance();
//        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
    }

    //懒加载方式
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initMap();
        btn_fragment_second_tab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });

        btnChangeMapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mMapType) {
                    case BaiduMap.MAP_TYPE_NORMAL:
                        btnChangeMapType.setText("二维");
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        mMapType = BaiduMap.MAP_TYPE_SATELLITE;
                        break;
                    case BaiduMap.MAP_TYPE_SATELLITE:
                        btnChangeMapType.setText("卫星");
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        mMapType = BaiduMap.MAP_TYPE_NORMAL;
                        break;
                    default:
                }
            }
        });

        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dotType) {
                    btnDot.setText("停止打点");
                    btnDot.setTextColor(0xFFFF0000);
                    btnDot1.setVisibility(View.VISIBLE);
                    FirstTabFragment.fragment.startDot();
                    dotType = true;
                } else if (dotType) {
                    btnDot.setText("开始打点");
                    btnDot.setTextColor(0xE13F51B5);
                    btnDot1.setVisibility(View.GONE);
                    dotType = false;
                    markerlisttdot.clear();
                }
            }
        });

        btnDot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirstTabFragment.fragment.exportDot(FirstTabFragment.fragment.indexStartDot);
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCurrentLocation();
            }
        });

        btnSearchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (booleanSearchStart) {
                    btnSearchStart.setBackgroundResource(R.drawable.unfold_less_72px);
                    booleanSearchStart = false;


                } else {
                    btnSearchStart.setBackgroundResource(R.drawable.unfold_more_72px);
                    booleanSearchStart = true;


                }
            }
        });

        checkBoxBaiduHeatMap.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBaiduMap.setBaiduHeatMapEnabled(isChecked);
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {//设置监听事件
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {//地图状态改变结束后的地图状态
                ll = mapStatus.target;//地图操作中心位置 status.target获取当前地图中心点的坐标
                ThirdTabFragment.thirdlatLng = mapStatus.target;
                mapstatusdistance.add(ll);

                if (mapstatusdistance.size() >= 2) {
                    final double mapdistance = DistanceUtil.getDistance(mapstatusdistance.get(0),
                            mapstatusdistance.get(mapstatusdistance.size() - 1));
                    if (mapdistance > MAPDISTANCE) {
                        bmapMarkerLoad();
                        mapstatusdistance.clear();
                        mapstatusdistance.add(ll);
                    }
                }

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }
        });

        //marker点击监听事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                try {
                    textViewFragmentSecondTabClickedCell.setVisibility(View.VISIBLE);
                    textViewFragmentSecondTabClickedCell.setText(lteBasestationCellList.get(marker
                            .getZIndex())
                            .getName());
                    textViewFragmentSecondTabCGI.setText("CGI:\t" + lteBasestationCellList.get(marker
                            .getZIndex())
                            .getEnbId() + "-" + lteBasestationCellList.get(marker.getZIndex())
                            .getEnbCellId() + "\t\t\tPCI:\t" + lteBasestationCellList.get(marker
                            .getZIndex())
                            .getPci());
                    if (lastSelectedMarker != null) {
                        if (lastSelectedMarker.getIcon() == markerLteFDD900OutsideSelected) {
                            lastSelectedMarker.setIcon(markerLteFDD900Outside);
                        } else if (lastSelectedMarker.getIcon() == markerLteFDD1800OutsideSelected) {
                            lastSelectedMarker.setIcon(markerLteFDD1800Outside);
                        } else if (lastSelectedMarker.getIcon() == markerLteTDDOutsideSelected) {
                            lastSelectedMarker.setIcon(markerLteTDDOutside);
                        } else if (lastSelectedMarker.getIcon() == markerLteTDDIndoorSelected) {
                            lastSelectedMarker.setIcon(markerLteTDDIndoor);
                        }
                    }
                    marker.setToTop();
                    if (marker.getIcon() == markerLteFDD900Outside) {
                        marker.setIcon(markerLteFDD900OutsideSelected);
                    } else if (marker.getIcon() == markerLteFDD1800Outside) {
                        marker.setIcon(markerLteFDD1800OutsideSelected);
                    } else if (marker.getIcon() == markerLteTDDOutside) {
                        marker.setIcon(markerLteTDDOutsideSelected);
                    } else if (marker.getIcon() == markerLteTDDIndoor) {
                        marker.setIcon(markerLteTDDIndoorSelected);
                    }
                } catch (Exception e) {
                    Toast.makeText(App.getContext(), " ", Toast.LENGTH_LONG).show();
                }
                lastSelectedMarker = marker;
                return true;
            }
        });

        textViewFragmentSecondTabClickedCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lteBasestationCellList.get(marker.getZIndex()).getName();
                if (lastSelectedMarker != null) {

                    ((MainFragment) getPreFragment()).startBrotherFragment(LteBasestationcellDetailInfoFragment
                            .newInstance(lteBasestationCellList.get(lastSelectedMarker.getZIndex())));
                }
            }
        });


//        /**
//         * start other BrotherFragment
//         */
//        public void startBrotherFragment(SupportFragment targetFragment) {
//            start(targetFragment);
//        }


        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                startlatlng = point;
                gesturepoint.add(startlatlng);
                if (gesturepoint.size() == 1) {
                    OverlayOptions ooCircle = new CircleOptions()
                            .fillColor(0x40DC143C)
                            .center(startlatlng)
                            //通过stroke属性即可设置线的颜色及粗细，new Stroke(5, 0xAA000000) 5为线宽，0xAA000000 为颜色
                            .stroke(new Stroke(2, 0xFFDC143C))
                            .radius(13);
                    mBaiduMap.addOverlay(ooCircle);
                }
                if (gesturepoint.size() == 2) {
                    OverlayOptions mOverlayOptions = new PolylineOptions()
                            .width(8)
                            .color(0x99DC143C)
                            .points(gesturepoint);
                    Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);

                    final double distance_util = DistanceUtil.getDistance(gesturepoint.get(0), gesturepoint.get(1));
                    OverlayOptions ooCircle1 = new CircleOptions()
                            .fillColor(0x80DC143C)
                            .center(startlatlng)
                            .stroke(new Stroke(1, 0xFFDC143C))
                            .radius(13);
                    mBaiduMap.addOverlay(ooCircle1);

                    //构建文字Option对象，用于在地图上添加文字
                    OverlayOptions textOption1 = new TextOptions()
                            .fontSize(55) //字号
                            .fontColor(0xAA000000) //文字颜色
                            .rotate(0) //旋转角度
                            .text("\t \t        距离 " + (int) distance_util + "米")
                            .position(startlatlng);
                    mBaiduMap.addOverlay(textOption1);
                    gesturepoint.clear();
                }
            }
        });

//        mPoiSearch.searchInCity((new PoiCitySearchOption()).city("武汉").keyword("武汉").pageNum(10));
    }

    private void btnDotLoad(final LatLng lll) {
        newCachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (NetworkStatus.RSRP > -75) {
                    //通过stroke属性即可设置线的颜色及粗细，new Stroke(5, 0xAA000000) 5为线宽，0xAA000000 为颜色
                    //.stroke(new Stroke(1, 0xFF0000FF))
                    markerOptionstdot = new MarkerOptions()
                            .position(lll)
                            .icon(markerLettrack75)
                            .draggable(false).alpha(MARKER_ALPHA).perspective(true);
                } else if (NetworkStatus.RSRP > -85) {
                    markerOptionstdot = new MarkerOptions()
                            .position(lll)
                            .icon(markerLettrack85)
                            .draggable(false).alpha(MARKER_ALPHA).perspective(true);
                } else if (NetworkStatus.RSRP > -95) {
                    markerOptionstdot = new MarkerOptions()
                            .position(lll)
                            .icon(markerLettrack95)
                            .draggable(false).alpha(MARKER_ALPHA).perspective(true);
                } else if (NetworkStatus.RSRP > -105) {
                    markerOptionstdot = new MarkerOptions()
                            .position(lll)
                            .icon(markerLettrack105)
                            .draggable(false).alpha(MARKER_ALPHA).perspective(true);
                } else if (NetworkStatus.RSRP > -115) {
                    markerOptionstdot = new MarkerOptions()
                            .position(lll)
                            .icon(markerLettrack115)
                            .draggable(false).alpha(MARKER_ALPHA).perspective(true);
                } else if (NetworkStatus.RSRP > -125) {
                    markerOptionstdot = new MarkerOptions()
                            .position(lll)
                            .icon(markerLettrack125)
                            .draggable(false).alpha(MARKER_ALPHA).perspective(true);
                } else if (NetworkStatus.RSRP > -150) {
                    markerOptionstdot = new MarkerOptions()
                            .position(lll)
                            .icon(markerLettrack126)
                            .draggable(false).alpha(MARKER_ALPHA).perspective(true);
                }
                mBaiduMap.addOverlay(markerOptionstdot);
                markerlisttdot.add(markerOptionstdot);
            }
        });
    }

    private void markerlisttdotfor() {
        for (MarkerOptions markerOptions : markerlisttdot) {
            mBaiduMap.addOverlay(markerOptions);
        }
    }

    private void initMap() {

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //显示方向，初始化为普通地图模式（普通、跟随、导航）
        mCurrentLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentLocationMode, true, null));
        final MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        //显示指南针
        UiSettings mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setAllGesturesEnabled(false);
        //实例化UiSettings类对象
        mUiSettings.setCompassEnabled(true);

        //通过设置enable为true或false 选择是否启用地图缩放手势
        mUiSettings.setZoomGesturesEnabled(true);

        mUiSettings.setOverlookingGesturesEnabled(true);

        //通过设置enable为true或false 选择是否启用地图平移
        mUiSettings.setScrollGesturesEnabled(true);
        //设置初步地图类型为二维地图（二维、卫星）
        mMapType = BaiduMap.MAP_TYPE_NORMAL;
        btnChangeMapType.setText("卫星");

        //设置地图各组件的位置
        mBaiduMap.setViewPadding(0, 0, 0, 0);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.AXIS_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //更新当前位置
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLocation(UpdateUeStatusEvent updateUEStatusEvent) {
        if (SecondTabFragment.this.isVisible()) {
            getCurrentLocation(updateUEStatusEvent.ueStatus.locationStatus);
            //在界面上更新当前地点的经纬度、名称等信息
            //更新地图
            if (updateUEStatusEvent.ueStatus.locationStatus.bdLocation == null || mMapView == null) {
                return;
            }
            mCurrentLat = updateUEStatusEvent.ueStatus.locationStatus.latitudeBaidu;
            mCurrentLon = updateUEStatusEvent.ueStatus.locationStatus.longitudeBaidu;
            mCurrentAccracy = updateUEStatusEvent.ueStatus.locationStatus.radius;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                goToCurrentLocation();
            }
            if (dotType) {
                btnDotLoad(new LatLng(mCurrentLat, mCurrentLon));
            }
            displayCellLine(updateUEStatusEvent.ueStatus);
        }
    }

    private void getCurrentLocation(LocationStatus locationStatus) {
        StringBuilder currentPostion = new StringBuilder();
        currentPostion.append("经纬度:").append(NumberFormat.doubleFormat(locationStatus
                .longitudeWgs84, 6))
                .append(",")
                .append
                        (NumberFormat.doubleFormat(locationStatus.latitudeWgs84, 6)).append("," +
                "高度:").append((int) (locationStatus.altitude)).append("," +
                "\t\tRSRP:\t").append(NetworkStatus.RSRP);
        textViewCurrentPositionLonLat.setText(currentPostion);
        textViewCurrentPositionDefinition.setText("地址" + locationStatus.addrStr);
    }

    private void goToCurrentLocation() {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(mCurrentLat, mCurrentLon)).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private void bmapMarkerLoad() {
        if ((int) mBaiduMap.getMapStatus().zoom >= 13) {
            if (checkboxFragmentSecondTabDisplayLTECell.isChecked()) {
                displayMyOverlay(ll);
            }
            if (checkboxFragmentSecondCustom.isChecked()) {
                displayMyOverlaycustom(ll);
            }
            if (checkboxFragmentSecondTrack.isChecked()) {
                displayMyOverlaytrack(ll);
            }
            if (checkboxFragmentSecondGrid.isChecked()) {
                displayMyOverlaygrid(ll);
            }
            if (markerlisttdot.size() > 1) {
                markerlisttdotfor();
            }
            checkboxFragmentSecondTabDisplayLTECell.setOnCheckedChangeListener(new CheckBox
                    .OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        displayMyOverlay(ll);
                    } else {
                        markerList.clear();
                        mBaiduMap.clear();
                        if (checkboxFragmentSecondCustom.isChecked()) {
                            displayMyOverlaycustom(ll);
                        }
                        if (checkboxFragmentSecondTrack.isChecked()) {
                            displayMyOverlaytrack(ll);
                        }
                        if (checkboxFragmentSecondGrid.isChecked()) {
                            displayMyOverlaygrid(ll);
                        }
                        if (markerlisttdot.size() > 1) {
                            markerlisttdotfor();
                        }
                    }
                }
            });
            checkboxFragmentSecondCustom.setOnCheckedChangeListener(new CheckBox
                    .OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        displayMyOverlaycustom(ll);
                    } else {
                        mBaiduMap.clear();
                        markerListcustom.clear();
                        if (checkboxFragmentSecondTabDisplayLTECell.isChecked()) {
                            displayMyOverlay(ll);
                        }
                        if (checkboxFragmentSecondTrack.isChecked()) {
                            displayMyOverlaytrack(ll);
                        }
                        if (checkboxFragmentSecondGrid.isChecked()) {
                            displayMyOverlaygrid(ll);
                        }
                        if (markerlisttdot.size() > 1) {
                            markerlisttdotfor();
                        }
                    }
                }
            });
            checkboxFragmentSecondTrack.setOnCheckedChangeListener(new CheckBox
                    .OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        displayMyOverlaytrack(ll);
                    } else {
                        mBaiduMap.clear();
                        markerListtrack.clear();
                        if (checkboxFragmentSecondTabDisplayLTECell.isChecked()) {
                            displayMyOverlay(ll);
                        }
                        if (checkboxFragmentSecondCustom.isChecked()) {
                            displayMyOverlaycustom(ll);
                        }
                        if (checkboxFragmentSecondGrid.isChecked()) {
                            displayMyOverlaygrid(ll);
                        }
                        if (markerlisttdot.size() > 1) {
                            markerlisttdotfor();
                        }
                    }
                }
            });
            checkboxFragmentSecondGrid.setOnCheckedChangeListener(new CheckBox
                    .OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        displayMyOverlaygrid(ll);
                    } else {
                        mBaiduMap.clear();
                        markerListgrid.clear();
                        if (checkboxFragmentSecondTrack.isChecked()) {
                            displayMyOverlaytrack(ll);
                        }
                        if (checkboxFragmentSecondTabDisplayLTECell.isChecked()) {
                            displayMyOverlay(ll);
                        }
                        if (checkboxFragmentSecondCustom.isChecked()) {
                            displayMyOverlaycustom(ll);
                        }
                        if (markerlisttdot.size() > 1) {
                            markerlisttdotfor();
                        }
                    }
                }
            });
        } else {
            mBaiduMap.clear();
        }

    }


    //4G工参添加marker
    public void displayMyOverlay(final LatLng latLngBd09) {
        newCachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LocatonConverter.MyLatLng myLatLngWgs84 = LocatonConverter.bd09ToWgs84(new LocatonConverter
                        .MyLatLng(latLngBd09.latitude, latLngBd09.longitude));//09经纬度转为s84
                lteBasestationCellList = LitePal.where("lng<? and " +
                        "lng>? and lat<? and lat >?", myLatLngWgs84.longitude + DISTANCE_OFFSET
                        + "", myLatLngWgs84
                        .longitude - DISTANCE_OFFSET + "", myLatLngWgs84.latitude + DISTANCE_OFFSET + "", myLatLngWgs84
                        .latitude - DISTANCE_OFFSET + "").find(LteBasestationCell.class);

                // add marker overlay
                if (lteBasestationCellList.size() > 0) {
                    for (int i = 0; i < lteBasestationCellList.size(); i++) {
                        lteBasestationCell = lteBasestationCellList.get(i);
                        //这里需要将数据库中查出来的每个站点均进行wgs84 to bd09的变换
                        LocatonConverter.MyLatLng myLatLngBd09 = LocatonConverter.wgs84ToBd09(new
                                LocatonConverter.MyLatLng(lteBasestationCell
                                .getLat(), lteBasestationCell
                                .getLng()));
                        LatLng ll = new LatLng(myLatLngBd09.getLatitude(), myLatLngBd09.getLongitude());
                        if (lteBasestationCell.getIndoorOrOutdoor() == LteBasestationCell.COVERAGE_OUTSIDE) {
                            if (lteBasestationCell.getLteEarFcn() > 10000) {
                                markerOptions = new MarkerOptions().position(ll).icon(markerLteTDDOutside)
                                        .zIndex(i)
                                        .draggable(false).alpha(MARKER_ALPHA).rotate(360 -
                                                lteBasestationCell.getEnbCellAzimuth()).perspective(true);
                            } else if (lteBasestationCell.getLteEarFcn() > 3000 && lteBasestationCell
                                    .getLteEarFcn() <= 10000) {
                                markerOptions = new MarkerOptions().position(ll).icon
                                        (markerLteFDD900Outside)
                                        .zIndex(i)
                                        .draggable(false).alpha(MARKER_ALPHA).rotate(360 -
                                                lteBasestationCell.getEnbCellAzimuth()).perspective(true);
                            } else if (lteBasestationCell.getLteEarFcn() < 3000) {
                                markerOptions = new MarkerOptions().position(ll).icon
                                        (markerLteFDD1800Outside)
                                        .zIndex(i)
                                        .draggable(false).alpha(MARKER_ALPHA).rotate(360 -
                                                lteBasestationCell.getEnbCellAzimuth()).perspective(true);
                            }
                        } else if (lteBasestationCell.getIndoorOrOutdoor() == LteBasestationCell.COVERAGE_INDOOR) {
                            markerOptions = new MarkerOptions().position(ll).icon
                                    (markerLteTDDIndoor)
                                    .zIndex(i)
                                    .draggable(false).alpha(MARKER_ALPHA).perspective(true);
                        }
                        marker = (Marker) (mBaiduMap.addOverlay(markerOptions));
                        markerList.add(marker);
                    }
                }
            }
        });
    }

    //规划自定义数据加marker
    public void displayMyOverlaycustom(final LatLng latLngBd09) {
        newCachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LocatonConverter.MyLatLng myLatLngWgs84 = LocatonConverter.bd09ToWgs84(new LocatonConverter
                        .MyLatLng(latLngBd09.latitude, latLngBd09.longitude));//09经纬度转为s84
                lteBasesCustomList = LitePal.where("lng<? and " +
                        "lng>? and lat<? and lat >?", myLatLngWgs84.longitude + DISTANCE_OFFSET
                        + "", myLatLngWgs84
                        .longitude - DISTANCE_OFFSET + "", myLatLngWgs84.latitude + DISTANCE_OFFSET + "", myLatLngWgs84
                        .latitude - DISTANCE_OFFSET + "").find(LteBasesCustom.class);
                // add marker overlay
                if (lteBasesCustomList.size() > 0) {
                    for (int j = 0; j < lteBasesCustomList.size(); j++) {
                        lteBasesCustom = lteBasesCustomList.get(j);
                        //这里需要将数据库中查出来的每个站点均进行wgs84 to bd09的变换
                        LocatonConverter.MyLatLng myLatLngBd09 = LocatonConverter.wgs84ToBd09(new
                                LocatonConverter.MyLatLng(lteBasesCustom
                                .getLat(), lteBasesCustom
                                .getLng()));
                        LatLng ll = new LatLng(myLatLngBd09.getLatitude(), myLatLngBd09.getLongitude());

                        markerOptionscustom = new MarkerOptions()
                                .position(ll)
                                .icon(markerLetcustom)
                                .zIndex(j)
                                .draggable(false).alpha(MARKER_ALPHA).perspective(true);

                        //构建文字Option对象，用于在地图上添加文字
                        OverlayOptions textOption = new TextOptions()
                                .fontSize(30) //字号
                                .fontColor(0xFFFF00FF) //文字颜色
                                .rotate(0) //旋转角度
                                .text(lteBasesCustom.getName())
                                .position(ll);

                        mBaiduMap.addOverlay(textOption);
                        mBaiduMap.addOverlay(markerOptionscustom);
                    }
                }
            }
        });
    }

    //轨迹log数据加marker
    public void displayMyOverlaytrack(final LatLng latLngBd09) {
        newCachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LocatonConverter.MyLatLng myLatLngWgs84 = LocatonConverter.bd09ToWgs84(new LocatonConverter
                        .MyLatLng(latLngBd09.latitude, latLngBd09.longitude));//09经纬度转为s84
                lteBasesTrackList = LitePal.where("lng<? and " +
                        "lng>? and lat<? and lat >?", myLatLngWgs84.longitude + DISTANCE_OFFSET
                        + "", myLatLngWgs84
                        .longitude - DISTANCE_OFFSET + "", myLatLngWgs84.latitude + DISTANCE_OFFSET + "", myLatLngWgs84
                        .latitude - DISTANCE_OFFSET + "").find(LteBasesTrack.class);
                // add marker overlay
                if (lteBasesTrackList.size() > 0) {
                    MarkerOptions markerOptionsttrackresult;
                    for (LteBasesTrack lteBasesTrack : lteBasesTrackList) {
                        LocatonConverter.MyLatLng myLatLngBd09 = LocatonConverter.wgs84ToBd09(new
                                LocatonConverter.MyLatLng(lteBasesTrack.getLat(), lteBasesTrack.getLng()));
                        LatLng ll = new LatLng(myLatLngBd09.getLatitude(), myLatLngBd09.getLongitude());
                        markerOptionsttrackresult = lteTrackLoad(lteBasesTrack, ll);
                        mBaiduMap.addOverlay(markerOptionsttrackresult);
                    }
                }
            }
        });
    }

    protected MarkerOptions lteTrackLoad(LteBasesTrack lteBasesTrack, LatLng ll) {
        if (lteBasesTrack.getRsrp() > -75) {
            //通过stroke属性即可设置线的颜色及粗细，new Stroke(5, 0xAA000000) 5为线宽，0xAA000000 为颜色
            //.stroke(new Stroke(1, 0xFF0000FF))
            markerOptionsttrack = new MarkerOptions()
                    .position(ll)
                    .icon(markerLettrack75)
                    .draggable(false).alpha(MARKER_ALPHA).perspective(true);
        } else if (lteBasesTrack.getRsrp() > -85) {
            markerOptionsttrack = new MarkerOptions()
                    .position(ll)
                    .icon(markerLettrack85)
                    .draggable(false).alpha(MARKER_ALPHA).perspective(true);
        } else if (lteBasesTrack.getRsrp() > -95) {
            markerOptionsttrack = new MarkerOptions()
                    .position(ll)
                    .icon(markerLettrack95)
                    .draggable(false).alpha(MARKER_ALPHA).perspective(true);
        } else if (lteBasesTrack.getRsrp() > -105) {
            markerOptionsttrack = new MarkerOptions()
                    .position(ll)
                    .icon(markerLettrack105)
                    .draggable(false).alpha(MARKER_ALPHA).perspective(true);
        } else if (lteBasesTrack.getRsrp() > -115) {
            markerOptionsttrack = new MarkerOptions()
                    .position(ll)
                    .icon(markerLettrack115)
                    .draggable(false).alpha(MARKER_ALPHA).perspective(true);
        } else if (lteBasesTrack.getRsrp() > -125) {
            markerOptionsttrack = new MarkerOptions()
                    .position(ll)
                    .icon(markerLettrack125)
                    .draggable(false).alpha(MARKER_ALPHA).perspective(true);
        } else if (lteBasesTrack.getRsrp() > -150) {
            markerOptionsttrack = new MarkerOptions()
                    .position(ll)
                    .icon(markerLettrack126)
                    .draggable(false).alpha(MARKER_ALPHA).perspective(true);
        }
        return markerOptionsttrack;
    }

    public void displayMyOverlaygrid(final LatLng latLngBd09) {
        newCachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LocatonConverter.MyLatLng myLatLngWgs84 = LocatonConverter.bd09ToWgs84(new LocatonConverter
                        .MyLatLng(latLngBd09.latitude, latLngBd09.longitude));//09经纬度转为s84
                lteBasesGridList1 = LitePal.where("lng <? and " +
                        "lng >? and lat <? and lat >?", myLatLngWgs84.longitude + DISTANCE_OFFSET
                        + "", myLatLngWgs84
                        .longitude - DISTANCE_OFFSET + "", myLatLngWgs84.latitude + DISTANCE_OFFSET + "", myLatLngWgs84
                        .latitude - DISTANCE_OFFSET + "").find(LteBasesGrid.class);

                //添加栅格图层
                if (lteBasesGridList1.size() > 0) {
                    for (int l = 0; l < lteBasesGridList1.size(); l++) {
                        lteBasesGrid = lteBasesGridList1.get(l);
                        //这里需要将数据库中查出来的每个站点均进行wgs84 to bd09的变换
                        LocatonConverter.MyLatLng myLatLngBd09 = LocatonConverter.wgs84ToBd09(new
                                LocatonConverter.MyLatLng(lteBasesGrid.getLat(), lteBasesGrid.getLng()));

                        //LatLng ll = new LatLng(myLatLngBd09.getLatitude(), myLatLngBd09.getLongitude());

                        LocatonConverter.MyLatLng myLatLngBd091 = LocatonConverter.wgs84ToBd09(new
                                LocatonConverter.MyLatLng(lteBasesGrid.getLat1(), lteBasesGrid.getLng1()));
                        LatLng ll1 = new LatLng(myLatLngBd091.getLatitude(), myLatLngBd091.getLongitude());

                        LocatonConverter.MyLatLng myLatLngBd092 = LocatonConverter.wgs84ToBd09(new
                                LocatonConverter.MyLatLng(lteBasesGrid.getLat2(), lteBasesGrid.getLng2()));
                        LatLng ll2 = new LatLng(myLatLngBd092.getLatitude(), myLatLngBd092.getLongitude());

                        LocatonConverter.MyLatLng myLatLngBd093 = LocatonConverter.wgs84ToBd09(new
                                LocatonConverter.MyLatLng(lteBasesGrid.getLat3(), lteBasesGrid.getLng3()));
                        LatLng ll3 = new LatLng(myLatLngBd093.getLatitude(), myLatLngBd093.getLongitude());

                        LocatonConverter.MyLatLng myLatLngBd094 = LocatonConverter.wgs84ToBd09(new
                                LocatonConverter.MyLatLng(lteBasesGrid.getLat4(), lteBasesGrid.getLng4()));
                        LatLng ll4 = new LatLng(myLatLngBd094.getLatitude(), myLatLngBd094.getLongitude());
                        List<LatLng> points1 = new ArrayList<>();//多边形顶点位置
                        points1.add(ll1);
                        points1.add(ll2);
                        points1.add(ll3);
                        points1.add(ll4);
                        if (lteBasesGrid.getRsrp() > -75) {
                            mPolygonOptions1 = new PolygonOptions()
                                    .points(points1)
                                    .fillColor(0x330000FF)//填充颜色
                                    .stroke(new Stroke(0, 0x000000FF));//边框宽度和颜色
                        } else if (lteBasesGrid.getRsrp() > -85) {
                            mPolygonOptions1 = new PolygonOptions()
                                    .points(points1)
                                    .fillColor(0x3300FFFF)
                                    .stroke(new Stroke(0, 0x0000FFFF));
                        } else if (lteBasesGrid.getRsrp() > -95) {
                            mPolygonOptions1 = new PolygonOptions()
                                    .points(points1)
                                    .fillColor(0x3300FF00)
                                    .stroke(new Stroke(0, 0x0000FF00));
                        } else if (lteBasesGrid.getRsrp() > -105) {
                            mPolygonOptions1 = new PolygonOptions()
                                    .points(points1)
                                    .fillColor(0x33FFFF00)
                                    .stroke(new Stroke(0, 0x00FFFF00));
                        } else if (lteBasesGrid.getRsrp() > -115) {
                            mPolygonOptions1 = new PolygonOptions()
                                    .points(points1)
                                    .fillColor(0x40ff0000)
                                    .stroke(new Stroke(0, 0x00ff0000));
                        } else if (lteBasesGrid.getRsrp() > -125) {
                            mPolygonOptions1 = new PolygonOptions()
                                    .points(points1)
                                    .fillColor(0x336D6969)
                                    .stroke(new Stroke(0, 0x006D6969));
                        } else if (lteBasesGrid.getRsrp() > -200) {
                            mPolygonOptions1 = new PolygonOptions()
                                    .points(points1)
                                    .fillColor(0x33363636)
                                    .stroke(new Stroke(0, 0x00363636));
                        }
                        mBaiduMap.addOverlay(mPolygonOptions1);
                    }
                }
            }
        });
    }

    private void displayCellLine(UeStatus ueStatus) {
        try {
            if (checkboxFragmentSecondTabDisplayLTECell.isChecked() && ueStatus != null) {
                currentLteBasestationCellList = LitePal.where("eci=?", ueStatus.networkStatus
                        .lteServingCellTower
                        .cellId + "").find(LteBasestationCell.class);
                if (lteBasestationCellList != null) {
                    LatLng p1 = new LatLng(ueStatus.locationStatus.latitudeBaidu, ueStatus.locationStatus.longitudeBaidu);
                    LocatonConverter.MyLatLng myLatLngBd09 = LocatonConverter.wgs84ToBd09(new
                            LocatonConverter
                                    .MyLatLng
                            (currentLteBasestationCellList.get(0).getLat(), currentLteBasestationCellList
                                    .get(0).getLng()));

                    LatLng p2 = new LatLng(myLatLngBd09.latitude, myLatLngBd09.longitude);
                    List<LatLng> points = new ArrayList<>();
                    points.add(p1);
                    points.add(p2);
                    OverlayOptions ooPolyline = new PolylineOptions().width(1).color(0xAAFF0000).points
                            (points);
                    mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

//    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
//        public void onGetPoiResult(PoiResult result) {
//            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                Toast.makeText(App.getContext(), "未搜索到POI数据", Toast.LENGTH_SHORT).show();
//            }
//            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//                //获取POI检索结果
//                Toast.makeText(App.getContext(), "已搜索到POI数据", Toast.LENGTH_SHORT).show();
//            }
//            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
//                // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
//                String strInfo = "在";
//                for (CityInfo cityInfo : result.getSuggestCityList()) {
//                    strInfo += cityInfo.city;
//                    strInfo += ",";
//                }
//                strInfo += "找到结果";
//                Toast.makeText(App.getContext(), strInfo, Toast.LENGTH_LONG).show();
//            }
//        }
//
//        @Override
//        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//
//        }
//
//        @Override
//        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
//            //获取Place详情页检索结果
//            poiDetailSearchResult.getPoiDetailInfoList();
//
//        }
//
//        @Override
//        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
//
//        }

//    };


}
