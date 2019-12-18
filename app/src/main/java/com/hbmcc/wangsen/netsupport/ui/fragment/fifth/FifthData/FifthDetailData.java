package com.hbmcc.wangsen.netsupport.ui.fragment.fifth.FifthData;

import android.os.Parcel;
import android.os.Parcelable;
import org.litepal.crud.LitePalSupport;

public class FifthDetailData extends LitePalSupport implements Parcelable {

    protected FifthDetailData(Parcel in) {
    }

    public static final Creator<FifthDetailData> CREATOR = new Creator<FifthDetailData>() {
        @Override
        public FifthDetailData createFromParcel(Parcel in) {
            return new FifthDetailData(in);
        }

        @Override
        public FifthDetailData[] newArray(int size) {
            return new FifthDetailData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
