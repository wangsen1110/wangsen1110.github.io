package com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class ThridFailureData extends LitePalSupport implements Parcelable {
    @Column(defaultValue = "未采集")
    private String name;
    @Column(defaultValue = "0")
    private long eci;
    @Column(defaultValue = "未采集")
    private String alarmvalve;
    @Column(defaultValue = "未采集")
    private String time;

    public ThridFailureData(String name, String alarmvalve, String time) {
        this.name = name;
        this.alarmvalve = alarmvalve;
        this.time = time;
    }

    public ThridFailureData() {
        this.name = name;
        this.eci = eci;
        this.alarmvalve = alarmvalve;
        this.time = time;
    }

    protected ThridFailureData(Parcel in) {
        name = in.readString();
        eci = in.readLong();
        alarmvalve = in.readString();
        time = in.readString();
    }

    public static final Creator<ThridFailureData> CREATOR = new Creator<ThridFailureData>() {
        @Override
        public ThridFailureData createFromParcel(Parcel in) {
            return new ThridFailureData(in);
        }

        @Override
        public ThridFailureData[] newArray(int size) {
            return new ThridFailureData[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEci() {
        return eci;
    }

    public void setEci(long eci) {
        this.eci = eci;
    }

    public String getAlarmvalve() {
        return alarmvalve;
    }

    public void setAlarmvalve(String alarmvalve) {
        this.alarmvalve = alarmvalve;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeLong(eci);
        parcel.writeString(alarmvalve);
        parcel.writeString(time);
    }
}
