package com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class ThridDetailData extends LitePalSupport implements Parcelable {

    @Column(defaultValue = "未采集")
    private String time;

    @Column(defaultValue = "0")
    private long eci;

    @Column(defaultValue = "未采集")
    private String name;
    @Column(defaultValue = "0")
    private Float connected;
    @Column(defaultValue = "0")
    private Float paging;
    @Column(defaultValue = "0")
    private Float handover;
    @Column(defaultValue = "0")
    private Float release;
    @Column(defaultValue = "0")
    private Float cover;

    @Column(defaultValue = "0")
    private int usercount;
    @Column(defaultValue = "0")
    private Float prbuseup;
    @Column(defaultValue = "0")
    private Float prbusedown;

    @Column(defaultValue = "0")
    private Float voconnected;
    @Column(defaultValue = "0")
    private Float voerabconnet;

    @Column(defaultValue = "0")
    private Float vodelay;

    public ThridDetailData() {
    }

    public ThridDetailData(long eci, String time, Float connected, Float paging, Float handover, Float release, Float cover, int usercount,
                           Float prbuseup, Float prbusedown, Float voconnected, Float voerabconnet, Float vodelay) {
        this.eci = eci;
        this.time = time;
        this.connected = connected;
        this.paging = paging;
        this.handover = handover;
        this.release = release;
        this.cover = cover;
        this.usercount = usercount;
        this.prbuseup = prbuseup;
        this.prbusedown = prbusedown;
        this.voconnected = voconnected;
        this.voerabconnet = voerabconnet;
        this.vodelay = vodelay;
    }

    public ThridDetailData(String time, long eci, String name, Float connected, Float paging, Float handover, Float release,
                           Float cover, int usercount, Float prbuseup, Float prbusedown,
                           Float voconnected, Float voerabconnet, Float vodelay) {
        this.time = time;
        this.eci = eci;
        this.name = name;
        this.connected = connected;
        this.paging = paging;
        this.handover = handover;
        this.release = release;
        this.cover = cover;
        this.usercount = usercount;
        this.prbuseup = prbuseup;
        this.prbusedown = prbusedown;
        this.voconnected = voconnected;
        this.voerabconnet = voerabconnet;
        this.vodelay = vodelay;
    }


    protected ThridDetailData(Parcel in) {
        time = in.readString();
        eci = in.readLong();
        name = in.readString();
        if (in.readByte() == 0) {
            connected = null;
        } else {
            connected = in.readFloat();
        }
        if (in.readByte() == 0) {
            paging = null;
        } else {
            paging = in.readFloat();
        }
        if (in.readByte() == 0) {
            handover = null;
        } else {
            handover = in.readFloat();
        }
        if (in.readByte() == 0) {
            release = null;
        } else {
            release = in.readFloat();
        }
        if (in.readByte() == 0) {
            cover = null;
        } else {
            cover = in.readFloat();
        }
        usercount = in.readInt();
        if (in.readByte() == 0) {
            prbuseup = null;
        } else {
            prbuseup = in.readFloat();
        }
        if (in.readByte() == 0) {
            prbusedown = null;
        } else {
            prbusedown = in.readFloat();
        }
        if (in.readByte() == 0) {
            voconnected = null;
        } else {
            voconnected = in.readFloat();
        }
        if (in.readByte() == 0) {
            voerabconnet = null;
        } else {
            voerabconnet = in.readFloat();
        }
        if (in.readByte() == 0) {
            vodelay = null;
        } else {
            vodelay = in.readFloat();
        }
    }

    public static final Creator<ThridDetailData> CREATOR = new Creator<ThridDetailData>() {
        @Override
        public ThridDetailData createFromParcel(Parcel in) {
            return new ThridDetailData(in);
        }

        @Override
        public ThridDetailData[] newArray(int size) {
            return new ThridDetailData[size];
        }
    };

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getEci() {
        return eci;
    }

    public void setEci(long eci) {
        this.eci = eci;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getConnected() {
        return connected;
    }

    public void setConnected(Float connected) {
        this.connected = connected;
    }

    public Float getPaging() {
        return paging;
    }

    public void setPaging(Float paging) {
        this.paging = paging;
    }

    public Float getHandover() {
        return handover;
    }

    public void setHandover(Float handover) {
        this.handover = handover;
    }

    public Float getRelease() {
        return release;
    }

    public void setRelease(Float release) {
        this.release = release;
    }

    public Float getCover() {
        return cover;
    }

    public void setCover(Float cover) {
        this.cover = cover;
    }

    public int getUsercount() {
        return usercount;
    }

    public void setUsercount(int usercount) {
        this.usercount = usercount;
    }

    public Float getPrbuseup() {
        return prbuseup;
    }

    public void setPrbuseup(Float prbuseup) {
        this.prbuseup = prbuseup;
    }

    public Float getPrbusedown() {
        return prbusedown;
    }

    public void setPrbusedown(Float prbusedown) {
        this.prbusedown = prbusedown;
    }

    public Float getVoconnected() {
        return voconnected;
    }

    public void setVoconnected(Float voconnected) {
        this.voconnected = voconnected;
    }

    public Float getVoerabconnet() {
        return voerabconnet;
    }

    public void setVoerabconnet(Float voerabconnet) {
        this.voerabconnet = voerabconnet;
    }

    public Float getVodelay() {
        return vodelay;
    }

    public void setVodelay(Float vodelay) {
        this.vodelay = vodelay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(time);
        parcel.writeLong(eci);
        parcel.writeString(name);
        if (connected == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(connected);
        }
        if (paging == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(paging);
        }
        if (handover == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(handover);
        }
        if (release == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(release);
        }
        if (cover == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(cover);
        }
        parcel.writeInt(usercount);
        if (prbuseup == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(prbuseup);
        }
        if (prbusedown == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(prbusedown);
        }
        if (voconnected == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(voconnected);
        }
        if (voerabconnet == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(voerabconnet);
        }
        if (vodelay == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(vodelay);
        }
    }
}
