package com.hbmcc.wangsen.netsupport.telephony;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.telephony.cellinfo.CellInfo;
import com.hbmcc.wangsen.netsupport.telephony.cellinfo.GsmCellInfo;
import com.hbmcc.wangsen.netsupport.telephony.cellinfo.LteCellInfo;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.NetworkUtils.isWifiConnected;

public class NetworkStatus {
    protected FragmentActivity _mActivity;
    private static final String TAG = "NetworkStatus";
    public static final int NETWORK_STATUS_ERROR = 9999;
    TelephonyManager mTelephonyManager;
    static public String phonenumber = "1234567890";
    static public String netOperators = "中国移动";
    static public int RSRP = 0;
    static public int RSRQ = 0;
    static public int RSSI = 0;
    static public int SINR = 0;
    static public int ASULEVEL = 0;

    public String time;
    public LteCellInfo lteServingCellTower;
    public GsmCellInfo gsmServingCellTower;
    public ArrayList<LteCellInfo> lteNeighbourCellTowers;
    public ArrayList<GsmCellInfo> gsmNeighbourCellTowers;

    public String imei;
    public String imsi;
    public String androidVersion;
    public String hardwareModel;

    //获取电话号码
    @RequiresApi(api = Build.VERSION_CODES.P)
    public NetworkStatus() {
        mTelephonyManager = (TelephonyManager) App.getContext().getSystemService(Context
                .TELEPHONY_SERVICE);
        if (mTelephonyManager == null) {
            Toast.makeText(App.getContext(), "获取手机网络存在问题", Toast.LENGTH_SHORT).show();
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss ");
        time = sDateFormat.format(new java.util.Date());
        imei = PhoneUtils.getIMEI();
        imsi = PhoneUtils.getIMSI();
        androidVersion = Build.VERSION.RELEASE;
        hardwareModel = Build.MODEL;

        // 获取基站信息
        //SDK18及之后android系统使用getAllCellInfo方法，并且对基站的类型加以区分
        if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List<android.telephony.CellInfo> infos = mTelephonyManager.getAllCellInfo();
        if (infos != null && infos.size() != 0) {
            lteNeighbourCellTowers = new ArrayList<>();
            gsmNeighbourCellTowers = new ArrayList<>();
            for (android.telephony.CellInfo i : infos) { // 根据邻区总数进行循环
                if (i instanceof CellInfoLte) {
                    LteCellInfo tower = new LteCellInfo();
                    CellIdentityLte cellIdentityLte = ((CellInfoLte) i).getCellIdentity();
                    tower.cellType = CellInfo.STRING_TYPE_LTE;
                    tower.isRegitered = i.isRegistered();
                    tower.tac = cellIdentityLte.getTac();
                    tower.mobileCountryCode = cellIdentityLte.getMcc()+"";
                    tower.mobileNetworkCode = cellIdentityLte.getMnc()+"";
                    tower.cellId = cellIdentityLte.getCi();
                    tower.timingAdvance = ((CellInfoLte) i).getCellSignalStrength().getTimingAdvance();

                    if (Build.VERSION.SDK_INT >= 24) {
                        tower.lteEarFcn = cellIdentityLte.getEarfcn();
                    } else {
                        try {
                            tower.lteEarFcn = (int) cellIdentityLte.getClass().getDeclaredMethod("getEarfcn").invoke(cellIdentityLte);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    tower.signalStrength = ((CellInfoLte) i).getCellSignalStrength().getDbm();
                    if (tower.signalStrength > 0) {
                        tower.signalStrength = tower.signalStrength / 4 * -1;
                    }

                    if (Build.VERSION.SDK_INT >= 26) {
                        tower.rsrq = ((CellInfoLte) i).getCellSignalStrength().getRsrq();
                    } else {
                        try {
                            Class<?> cellSignalStrengthLteClass = ((CellInfoLte) i)
                                    .getCellSignalStrength().getClass();
                            Method methodGetRsrq = cellSignalStrengthLteClass.getDeclaredMethod
                                    ("getRsrq");
                            tower.rsrq = (int) methodGetRsrq.invoke(((CellInfoLte) i)
                                    .getCellSignalStrength());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (tower.sinr == Float.MAX_VALUE) {
                        tower.sinr = NetworkStatus.NETWORK_STATUS_ERROR;
                    }
                    if (tower.rsrq == Integer.MAX_VALUE) {
                        tower.rsrq = NetworkStatus.NETWORK_STATUS_ERROR;
                    }
                    if (tower.tac == Integer.MAX_VALUE) {
                        tower.tac = NetworkStatus.NETWORK_STATUS_ERROR;
                    }

                    tower.pci = cellIdentityLte.getPci();
                    tower.enbId = (int) Math.floor(tower.cellId / 256);
                    tower.enbCellId = tower.cellId % 256;
                    if (i.isRegistered()) {
                        tower.signalStrength = RSRP;
                        tower.sinr = SINR;
                        tower.rsrq = RSRQ;
                        lteServingCellTower = tower;
                    } else {
                        lteNeighbourCellTowers.add(tower);
                    }
                } else
//                    if (i instanceof CellInfoGsm) {
//                    GsmCellInfo tower = new GsmCellInfo();
//                    CellIdentityGsm cellIdentityGsm = ((CellInfoGsm) i).getCellIdentity();
//                    if (cellIdentityGsm == null) {
//                        continue;
//                    }
//                    tower.cellType = CellInfo.STRING_TYPE_GSM;
//                    tower.isRegitered = i.isRegistered();
//                    tower.locationAreaCode = cellIdentityGsm.getLac();
//
//                    tower.signalStrength = ((CellInfoGsm) i).getCellSignalStrength().getDbm();
//                    tower.timingAdvance = 0;
//                    tower.gsmCellId = cellIdentityGsm.getCid();
//                    if (Build.VERSION.SDK_INT >= 24) {
//                        tower.bsic = cellIdentityGsm.getBsic();
//                        tower.gsmArFcn = cellIdentityGsm.getArfcn();
//                    }
//                    if (i.isRegistered()) {
//                        gsmServingCellTower = tower;
//                    } else {
//                        gsmNeighbourCellTowers.add(tower);
//                    }
//                }else
                {
                    Log.i(TAG, "基站库中无此小区");
                }
            }
        } else {
            getServerCellInfoOnOlderDevices();
        }

        if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        //获取电话号码
        phonenumber = mTelephonyManager.getLine1Number();
        netOperators = mTelephonyManager.getNetworkOperatorName();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private static int determineNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return CellInfo.TYPE_UNKNOWN;
        }
        android.net.NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if (network == null) {
            return CellInfo.TYPE_UNKNOWN;
        }
        switch (network.getSubtype()) {
            case TelephonyManager.NETWORK_TYPE_LTE:
                return CellInfo.TYPE_LTE;
            case TelephonyManager.NETWORK_TYPE_GSM:
                return CellInfo.TYPE_GSM;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return CellInfo.TYPE_GSM;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return CellInfo.TYPE_CDMA;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return CellInfo.TYPE_GSM;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return CellInfo.TYPE_TDSCDMA;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return CellInfo.TYPE_TDSCDMA;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return CellInfo.TYPE_TDSCDMA;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return CellInfo.TYPE_TDSCDMA;
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return CellInfo.TYPE_TDSCDMA;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return CellInfo.TYPE_CDMA;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return CellInfo.TYPE_CDMA;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return CellInfo.TYPE_CDMA;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return TelephonyManager.NETWORK_TYPE_IDEN;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return CellInfo.TYPE_TDSCDMA;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return TelephonyManager.NETWORK_TYPE_EHRPD;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return TelephonyManager.NETWORK_TYPE_1xRTT;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return TelephonyManager.NETWORK_TYPE_UNKNOWN;
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                return TelephonyManager.NETWORK_TYPE_IWLAN;
//            case TelephonyManager.NETWORK_TYPE_NR:
//                return CellInfo.NETWORK_TYPE_NR;
            default:
                return CellInfo.TYPE_UNKNOWN;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getServerCellInfoOnOlderDevices() {
        if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        try {
            GsmCellLocation location = (GsmCellLocation) mTelephonyManager.getAllCellInfo();
            if (determineNetworkType(App.getContext()) == CellInfo.TYPE_LTE) {
                lteServingCellTower.cellId = location.getCid();
                lteServingCellTower.rsrq = RSRQ;
                lteServingCellTower.sinr = SINR;
                lteServingCellTower.signalStrength = RSRP;
                lteServingCellTower.cellType = CellInfo.STRING_TYPE_LTE;
                lteServingCellTower.enbCellId = lteServingCellTower.cellId % 256;
                lteServingCellTower.enbId = lteServingCellTower.cellId / 256;
                lteServingCellTower.isRegitered = true;
                lteServingCellTower.tac = location.getLac();
            }
            else if (determineNetworkType(App.getContext())  == CellInfo.TYPE_GSM) {
                gsmServingCellTower.gsmCellId = location.getCid();
                gsmServingCellTower.asu = ASULEVEL;
                gsmServingCellTower.signalStrength = RSRP;
                gsmServingCellTower.cellType = CellInfo.STRING_TYPE_GSM;
                gsmServingCellTower.isRegitered = true;
                gsmServingCellTower.locationAreaCode = location.getLac();
            }
        } catch (Exception e) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public String getCellType() {
        if (isWifiConnected()) {
            return "WIFI";
        }
        switch (determineNetworkType(App.getContext())) {
            case 0:
                return "NO";
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "EVDO_0";
            case 6:
                return "EVDO_A";
            case 7:
                return "1XRTT";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 11:
                return "IDEN";
            case 12:
                return "EVDO_B";
            case 13:
                return "LTE";
            case 14:
                return "EHRPD";
            case 15:
                return "HSPAP";
            case 16:
                return "GSM";
            case 17:
                return "TDSCDMA";
            case 18:
                return "IWLAN";
            case 19:
                return "LTE_CA";
            case 20:
                return "NR";
        }
        return "NO";
    }
}
