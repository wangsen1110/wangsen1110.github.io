package com.hbmcc.wangsen.netsupport.ui.fragment.fifth.FifthData;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

public class FifthProblemData extends LitePalSupport implements Parcelable {



    protected FifthProblemData(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FifthProblemData> CREATOR = new Creator<FifthProblemData>() {
        @Override
        public FifthProblemData createFromParcel(Parcel in) {
            return new FifthProblemData(in);
        }

        @Override
        public FifthProblemData[] newArray(int size) {
            return new FifthProblemData[size];
        }
    };
}
