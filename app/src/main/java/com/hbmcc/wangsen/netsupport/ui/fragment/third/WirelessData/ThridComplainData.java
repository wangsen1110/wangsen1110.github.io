package com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class ThridComplainData extends LitePalSupport implements Parcelable {

    @Column(defaultValue = "0")
    private String address;
    @Column(defaultValue = "0")
    private String userid;
    @Column(defaultValue = "0")
    private String category;
    @Column(defaultValue = "0")
    private double lon;
    @Column(defaultValue = "0")
    private double lat;
    @Column(defaultValue = "0")
    private String time;

    public ThridComplainData(Parcel in){

    }

    public ThridComplainData(String address, String userid, String category, String time) {
        this.address = address;
        this.userid = userid;
        this.category = category;
        this.time = time;
    }

    public ThridComplainData() {

    }



    public static final Creator<ThridComplainData> CREATOR = new Creator<ThridComplainData>() {
        @Override
        public ThridComplainData createFromParcel(Parcel in) {
            return new ThridComplainData(in);
        }

        @Override
        public ThridComplainData[] newArray(int size) {
            return new ThridComplainData[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }


}
