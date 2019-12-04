package com.hbmcc.wangsen.netsupport;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.DeviceUtils;
import com.hbmcc.wangsen.netsupport.event.UpdateUeStatusEvent;
import com.hbmcc.wangsen.netsupport.telephony.DownloadSpeedStatus;
import com.hbmcc.wangsen.netsupport.telephony.LocationStatus;
import com.hbmcc.wangsen.netsupport.telephony.NetworkStatus;
import com.hbmcc.wangsen.netsupport.telephony.UeStatus;
import com.hbmcc.wangsen.netsupport.telephony.UploadSpeedStatus;
import com.hbmcc.wangsen.netsupport.ui.fragment.MainFragment;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


public class MainActivity extends SupportActivity {
    public FragmentActivity _mActivity;
    private static final String TAG = "MainActivity";

    //百度地图LocationClient和回调Listener
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

    //获取UEStatus并通过UpdateUEStatusEvent进行广播
    private UeStatus ueStatus;
    private NetworkStatus networkStatus;
    private UploadSpeedStatus uploadSpeedStatus;
    private DownloadSpeedStatus downloadSpeedStatus;
    private LocationStatus locationStatus;

    MyPhoneStateListener myPhoneStateListener;
    TelephonyManager mTelephonyManager;

    //只有Event中前后两条记录的time不一致，才进行广播
    private String mTime;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        newCachedThreadPool.shutdown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTime = "";
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.framelayout_mainactivity_container, MainFragment.newInstance());
        }
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                .ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            startWork();
        }
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置纵向(和安卓4.x动画相同)
        return new DefaultVerticalAnimator();
//        设置横向
//        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, "必须同意所有权限", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                } else {
                    _mActivity.runOnUiThread(new Runnable() {//开启子线程进行提示
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "请同意所有权限", Toast.LENGTH_LONG).show();
                        }
                    });
                    finish();
                }
                startWork();
                break;
            default:
        }
    }

    private void startWork() {
        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.restart();
        mLocationClient.registerLocationListener(myListener);
        requestLocation();

        mTelephonyManager = (TelephonyManager) App.getContext().getSystemService(Context
                .TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {

        LocationClientOption option = new LocationClientOption();
        //设置发起定位请求的间隔，int类型，单位ms。如果设置为0，则代表单次定位，即仅定位一次，默认为0。如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(1000);
        //设置返回经纬度坐标类型，默认gcj02。gcj02：国测局坐标；/bd09ll：百度经纬度坐标；bd09：百度墨卡托坐标；海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        option.setCoorType("bd09ll");
        //设置是否打开gps进行定位
        option.setOpenGps(true);
        //7.2版本新增能力，设置wifi缓存超时时间阈值，超过该阈值，首次定位将会主动扫描wifi以使得定位精准度提高，定位速度会有所下降，具体延时取决于wifi扫描时间，大约是1-3秒
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //获取高度信息，目前只有是GPS定位结果时或者设置LocationClientOption.setIsNeedAltitude(true)时才有效，单位米
        option.setIsNeedAltitude(true);
        //设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        //设置是否需要返回位置语义化信息，可以在BDLocation
        option.setIsNeedLocationDescribe(true);
        //在网络定位时，是否需要设备方向 true:需要 ; false:不需要。
        option.setNeedDeviceDirect(true);
        // getLocationDescribe()中得到数据，ex:"在天安门附近"， 可以用作地址信息的补充
//        option.setIsNeedLocationDescribe(true);
        //设置是否允许仿真GPS信号
        option.setEnableSimulateGps(true);
        //默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        mLocationClient.setLocOption(option);
    }

    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location) {
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                location.getGpsAccuracyStatus();// *****gps质量判断*****
//                Toast.makeText(MainActivity.this, "gps定位成功", Toast.LENGTH_SHORT).show();
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                Toast.makeText(MainActivity.this, "网络定位成功", Toast.LENGTH_SHORT).show();
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                Toast.makeText(MainActivity.this, "离线定位成功", Toast.LENGTH_SHORT).show();
//                double lat = location.getLatitude();
//                double lon = location.getLongitude();
//            }  else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                Toast.makeText(MainActivity.this, "网络不通导致定位失败，请检查网络是否通畅", Toast.LENGTH_SHORT).show();
//
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//
//                Toast.makeText(MainActivity.this, "法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结 果，可以试着重启手机", Toast.LENGTH_SHORT).show();
//            }
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果

            if (myPhoneStateListener != null) {
                newCachedThreadPool.execute(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void run() {
                        //收到百度定位的回调后，开始获取NetworkStatus并在EventBus中广播
                        networkStatus = new NetworkStatus();
                        //只有Event中前后两条记录的time不一致，才进行广播
                        if (!mTime.equals(networkStatus.time)) {
                            uploadSpeedStatus = new UploadSpeedStatus();
                            downloadSpeedStatus = new DownloadSpeedStatus();
                            locationStatus = new LocationStatus(location);
                            ueStatus = new UeStatus(networkStatus, locationStatus, downloadSpeedStatus, uploadSpeedStatus);
                            UpdateUeStatusEvent updateUEStatusEvent = new UpdateUeStatusEvent(ueStatus);
                            EventBusActivityScope.getDefault(MainActivity.this).post(updateUEStatusEvent);
                            mTime = networkStatus.time;
                        }
                    }
                });
            }
        }
    }

    class MyPhoneStateListener extends PhoneStateListener {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            try {
                NetworkStatus.RSRP = (Integer) signalStrength.getClass().getMethod("getDbm").invoke(signalStrength);
//                    Toast.makeText(App.getContext(), NetworkStatus.RSRP +"未检测到SIM数据包，请关闭WIFI，打开数据连接", Toast.LENGTH_LONG).show();
//                    Log.e("7", NetworkStatus.RSRP+"NetworkStatus.RSRP****** \t" + signalStrength.getClass().getMethod("getDbm").invoke(signalStrength));
                NetworkStatus.RSRQ = (Integer) signalStrength.getClass().getMethod("getLteRsrq").invoke(signalStrength);
//                    NetworkStatus.RSSI = -113 + 2 * (Integer) signalStrength.getClass().getMethod("getLteSignalStrength").invoke(signalStrength);
                if (DeviceUtils.getModel().contains("-AL00") || DeviceUtils.getModel().contains("-AN00") || DeviceUtils.getModel().contains("-TL00"))
                    NetworkStatus.SINR = (Integer) signalStrength.getClass().getMethod("getLteRssnr").invoke(signalStrength);
                else
                    NetworkStatus.SINR = (Integer) signalStrength.getClass().getMethod("getLteRssnr").invoke(signalStrength) / 10;
                Log.e("4", signalStrength.getClass().toString()+"" );

//
//                            List<CellInfo> cellInfoList;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                    // TODO: Consider calling
//                                    //    ActivityCompat#requestPermissions
//                                    // here to request the missing permissions, and then overriding
//                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                    //                                          int[] grantResults)
//                                    // to handle the case where the user grants the permission. See the documentation
//                                    // for ActivityCompat#requestPermissions for more details.
//                                    return;
//                                }
//                                cellInfoList = mTelephonyManager.getAllCellInfo();
//                                if (null != cellInfoList) {
//                                    for (CellInfo cellInfo : cellInfoList) {
//                                        if (cellInfo instanceof CellInfoLte) {
//                                            CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellInfo).getCellSignalStrength();
////                                            Log.e("4", "cellSignalStrengthLte.getAsuLevel()\t" + cellSignalStrengthLte.getAsuLevel());
//                                            Log.e("4", "cellSignalStrengthLte.getCqi()\t" + cellSignalStrengthLte.getCqi());
////                                            Log.e("4", "cellSignalStrengthLte.getDbm()\t " + cellSignalStrengthLte.getDbm());
//                                            Log.e("4", "cellSignalStrengthLte.getLevel()\t " + cellSignalStrengthLte.getLevel());
//                                            Log.e("4", "cellSignalStrengthLte.getRsrp()\t "+NetworkStatus.RSRP +"____"+ cellSignalStrengthLte.getRsrp());
//                                            Log.e("4", "cellSignalStrengthLte.getRsrq()\t " + cellSignalStrengthLte.getRsrq());
//                                            Log.e("4", "cellSignalStrengthLte.getRssnr()\t "+NetworkStatus.SINR +"____" + cellSignalStrengthLte.getRssnr());
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                                Log.e("4", "cellSignalStrengthLte.getTimingAdvance()\t " + NetworkStatus.RSSI +"____" +cellSignalStrengthLte.getRssi());
//                                            }
//                                        }
//                                    }
//                        }
//                    }
//
//                List<CellInfo> cellInfoList;
//                if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                cellInfoList = mTelephonyManager.getAllCellInfo();
//                if (null != cellInfoList) {
//                    for (CellInfo cellInfo : cellInfoList) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            if (cellInfo instanceof CellInfoNr) {
//                                CellSignalStrengthNr cellSignalStrengthLNr = (CellSignalStrengthNr) ((CellInfoNr) cellInfo).getCellSignalStrength();
//                                Log.e("4", "cellSignalStrengthLte.getAsuLevel()\t" + cellSignalStrengthLNr.getAsuLevel());
//                                Log.e("4", "cellSignalStrengthLte.getDbm()\t " + cellSignalStrengthLNr.getDbm());
//                                Log.e("4", "cellSignalStrengthLte.getLevel()\t " + cellSignalStrengthLNr.getLevel());
//                                Log.e("4", "cellSignalStrengthLte.getRsrp()\t "+NetworkStatus.RSRP +"____"+ cellSignalStrengthLNr.getSsRsrp());
//                                Log.e("4", "cellSignalStrengthLte.getRsrq()\t " +NetworkStatus.RSRQ +"____"+ cellSignalStrengthLNr.getSsRsrq());
//                                Log.e("4", "cellSignalStrengthLte.getRssnr()\t "+NetworkStatus.SINR +"____" + cellSignalStrengthLNr.getSsSinr());
//                            }
//                        }
//                    }
//                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //    private int getDefalutDataID(){
//        SubscriptionManager subscriptionManager = (SubscriptionManager)App.getContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
//        int subscriberId = 0;
//        if (Build.VERSION.SDK_INT > 24) {
//            subscriberId = SubscriptionManager.getDefaultDataSubscriptionId();
//        }else{
//            try {
//                Class cls  =  SubscriptionManager.class.getClass();
//                Method method = cls.getDeclaredMethod("getDefaultDataSubId");
//                subscriberId = (Integer) method.invoke(subscriptionManager);
//            }catch (Exception e){e.printStackTrace();}
//        }
//        return subscriberId;
//    }

}