package com.hbmcc.wangsen.netsupport.telephony.cellinfo;

public class CellInfo {
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_GSM = 16;
    public static final int TYPE_CDMA = 4;
    public static final int TYPE_WCDMA = 2;
    public static final int TYPE_LTE = 13;
    public static final int TYPE_TDSCDMA = 17;
    public static final int NETWORK_TYPE_NR = 20;
    public static final String STRING_TYPE_GSM = "GSM";
    public static final String STRING_TYPE_LTE = "LTE";


    public String cellType;
    public int cellId = 0;

    //（必填）：GSM 和 WCDMA 网络的位置区域代码 (LAC)。CDMA 网络的网络 ID (NID)。
    public int locationAreaCode;

    //（必填）：移动电话基站的移动国家代码 (MCC)。
    public String mobileCountryCode;

    //（必填）：移动电话基站的移动网络代码。对于 GSM \ WCDMA\LTE，这就是 MNC；CDMA 使用的是系统 ID(SID)。
    public String mobileNetworkCode;

    //测量到的无线信号强度（以 dBm 为单位）。
    public int signalStrength;

    //自从此小区成为主小区后经过的毫秒数。如果 age 为 0，cellId 就表示当前的测量值。
    public int age;

    //时间提前值。
    public int timingAdvance;

    //是否是服务小区
    public Boolean isRegitered;
}
