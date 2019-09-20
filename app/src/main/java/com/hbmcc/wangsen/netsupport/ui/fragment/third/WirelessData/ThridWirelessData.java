package com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class ThridWirelessData extends LitePalSupport implements Parcelable {

    @Column(defaultValue = "未定义")
    private long eci;
    @Column(defaultValue = "未定义")
    private String cellname;
    @Column(defaultValue = "0")
    private Double lon;
    @Column(defaultValue = "0")
    private Double lat;
    @Column(defaultValue = "1")
    private Float connected;
    @Column(defaultValue = "0.1")
    private Float release;
    @Column(defaultValue = "1")
    private Float mrcover;
    @Column(defaultValue = "-120")
    private Float prbdisturb;


    public ThridWirelessData() {
    }

    public ThridWirelessData(long eci,String cellname, Float connected,Float release, Float mrcover,Float prbdisturb) {
        this.eci = eci;
        this.cellname = cellname;
        this.connected = connected;
        this.mrcover = mrcover;
        this.release = release;
        this.prbdisturb = prbdisturb;
    }

    public ThridWirelessData(long eci, String cellname, Double lon, Double lat, Float connected,
                             Float release, Float mrcover, Float prbdisturb) {
        this.eci = eci;
        this.cellname = cellname;
        this.lon = lon;
        this.lat = lat;
        this.connected = connected;
        this.mrcover = mrcover;
        this.release = release;
        this.prbdisturb = prbdisturb;
    }

    protected ThridWirelessData(Parcel in) {
        eci = in.readLong();
        cellname = in.readString();
        if (in.readByte() == 0) {
            lon = null;
        } else {
            lon = in.readDouble();
        }
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            connected = null;
        } else {
            connected = in.readFloat();
        }
        if (in.readByte() == 0) {
            mrcover = null;
        } else {
            mrcover = in.readFloat();
        }
        if (in.readByte() == 0) {
            release = null;
        } else {
            release = in.readFloat();
        }
        if (in.readByte() == 0) {
            prbdisturb = null;
        } else {
            prbdisturb = in.readFloat();
        }
    }


    public long getEci() {
        return eci;
    }

    public void setEci(long eci) {
        this.eci = eci;
    }

    public String getCellname() {
        return cellname;
    }

    public void setCellname(String cellname) {
        this.cellname = cellname;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Float getConnected() {
        return connected;
    }

    public void setConnected(Float connected) {
        this.connected = connected;
    }

    public Float getMrcover() {
        return mrcover;
    }

    public void setMrcover(Float mrcover) {
        this.mrcover = mrcover;
    }

    public Float getRelease() {
        return release;
    }

    public void setRelease(Float release) {
        this.release = release;
    }

    public Float getPrbdisturb() {
        return prbdisturb;
    }

    public void setPrbdisturb(Float prbdisturb) {
        this.prbdisturb = prbdisturb;
    }

    @Override
    public String toString() {
        return "ThridWirelessData{" +
                "eci='" + eci + '\'' +
                ", cellname='" + cellname + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", connected=" + connected +
                ", mrcover=" + mrcover +
                ", release=" + release +
                ", prbdisturb=" + prbdisturb +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(eci);
        parcel.writeString(cellname);
        parcel.writeDouble(lon);
        parcel.writeDouble(lat);
        parcel.writeFloat(connected);
        parcel.writeFloat(mrcover);
        parcel.writeFloat(release);
        parcel.writeFloat(prbdisturb);
    }

    public static final Creator<ThridWirelessData> CREATOR = new Creator<ThridWirelessData>() {
        @Override
        public ThridWirelessData createFromParcel(Parcel source) {
            ThridWirelessData thridWirelessData = new ThridWirelessData();
            thridWirelessData.eci = source.readLong();
            thridWirelessData.cellname = source.readString();
            thridWirelessData.lon = source.readDouble();
            thridWirelessData.lat = source.readDouble();
            thridWirelessData.connected = source.readFloat();
            thridWirelessData.mrcover = source.readFloat();
            thridWirelessData.release = source.readFloat();
            thridWirelessData.prbdisturb = source.readFloat();
            return thridWirelessData;
        }

        @Override
        public ThridWirelessData[] newArray(int size) {
            return new ThridWirelessData[size];
        }
    };

}
