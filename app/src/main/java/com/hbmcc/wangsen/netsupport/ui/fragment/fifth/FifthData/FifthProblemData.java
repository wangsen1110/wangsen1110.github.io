package com.hbmcc.wangsen.netsupport.ui.fragment.fifth.FifthData;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class FifthProblemData extends LitePalSupport implements Parcelable {

    @Column(defaultValue = "")
    private String pTime;
    @Column(defaultValue = "")
    private String pCity;
    @Column(defaultValue = "")
    private String pDistrict;
    @Column(defaultValue = "")
    private Float pCoverage;
    @Column(defaultValue = "")
    private Float pWeakcoverage;
    @Column(defaultValue = "")
    private Float pWeakCoverageOutdoor;
    @Column(defaultValue = "")
    private Float pWeakCoverageIndoor;
    @Column(defaultValue = "")
    private Float pHighLoad;
//    @Column(defaultValue = "")
//    private String pNotExpanded;
    @Column(defaultValue = "")
    private Float pHighInterference;
    @Column(defaultValue = "")
    private Float pHighInterferenceFDD;
    @Column(defaultValue = "")
    private Float pHighInterferenceTDD;
    @Column(defaultValue = "")
    private Integer pLowOm;
    @Column(defaultValue = "")
    private Integer pHighDrop;
    @Column(defaultValue = "")
    private Integer pUpwardHighDrop;
    @Column(defaultValue = "")
    private Integer pDownHighDrop;
    @Column(defaultValue = "")
    private Integer pLowESRVCCSwitch;
    @Column(defaultValue = "")
    private Float pLowCQI;
    @Column(defaultValue = "")
    private Float pLongTimeWithdraw;
    @Column(defaultValue = "")
    private Float pMaintainSolve;
    @Column(defaultValue = "")
    private Float p4GAvailable;
    @Column(defaultValue = "")
    private Float pIndoorZeroFlow;
    @Column(defaultValue = "")
    private Integer pLongTimeWithdrawQuantity;
    @Column(defaultValue = "")
    private Integer pCoverageProblemQuantity;
    @Column(defaultValue = "")
    private Integer pPerceptionProblemQuantity;
    @Column(defaultValue = "")
    private Integer pInterferenceProblemQuantity;
    @Column(defaultValue = "")
    private Integer pMaintainProblemQuantity;
    @Column(defaultValue = "")
    private Integer pOtherProblemQuantity;

    public FifthProblemData(String pTime,String pCity,String pDistrict, Float pCoverage, Float pWeakcoverage, Float pWeakCoverageOutdoor, Float pWeakCoverageIndoor, Float pHighLoad, Float pHighInterference, Float pHighInterferenceFDD, Float pHighInterferenceTDD, Integer pLowOm, Integer pHighDrop, Integer pUpwardHighDrop, Integer pDownHighDrop, Integer pLowESRVCCSwitch, Float pLowCQI, Float pLongTimeWithdraw, Float pMaintainSolve, Float p4GAvailable, Float pIndoorZeroFlow, Integer pLongTimeWithdrawQuantity, Integer pCoverageProblemQuantity, Integer pPerceptionProblemQuantity, Integer pInterferenceProblemQuantity, Integer pMaintainProblemQuantity, Integer pOtherProblemQuantity) {
        this.pTime = pTime;
        this.pCity = pCity;
        this.pDistrict = pDistrict;
        this.pCoverage = pCoverage;
        this.pWeakcoverage = pWeakcoverage;
        this.pWeakCoverageOutdoor = pWeakCoverageOutdoor;
        this.pWeakCoverageIndoor = pWeakCoverageIndoor;
        this.pHighLoad = pHighLoad;
        this.pHighInterference = pHighInterference;
        this.pHighInterferenceFDD = pHighInterferenceFDD;
        this.pHighInterferenceTDD = pHighInterferenceTDD;
        this.pLowOm = pLowOm;
        this.pHighDrop = pHighDrop;
        this.pUpwardHighDrop = pUpwardHighDrop;
        this.pDownHighDrop = pDownHighDrop;
        this.pLowESRVCCSwitch = pLowESRVCCSwitch;
        this.pLowCQI = pLowCQI;
        this.pLongTimeWithdraw = pLongTimeWithdraw;
        this.pMaintainSolve = pMaintainSolve;
        this.p4GAvailable = p4GAvailable;
        this.pIndoorZeroFlow = pIndoorZeroFlow;
        this.pLongTimeWithdrawQuantity = pLongTimeWithdrawQuantity;
        this.pCoverageProblemQuantity = pCoverageProblemQuantity;
        this.pPerceptionProblemQuantity = pPerceptionProblemQuantity;
        this.pInterferenceProblemQuantity = pInterferenceProblemQuantity;
        this.pMaintainProblemQuantity = pMaintainProblemQuantity;
        this.pOtherProblemQuantity = pOtherProblemQuantity;
    }

    public String getpCity() {
        return pCity;
    }

    public void setpCity(String pCity) {
        this.pCity = pCity;
    }

    public String getpDistrict() {
        return pDistrict;
    }

    public void setpDistrict(String pDistrict) {
        this.pDistrict = pDistrict;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public Float getpCoverage() {
        return pCoverage;
    }

    public void setpCoverage(Float pCoverage) {
        this.pCoverage = pCoverage;
    }

    public Float getpWeakcoverage() {
        return pWeakcoverage;
    }

    public void setpWeakcoverage(Float pWeakcoverage) {
        this.pWeakcoverage = pWeakcoverage;
    }

    public Float getpWeakCoverageOutdoor() {
        return pWeakCoverageOutdoor;
    }

    public void setpWeakCoverageOutdoor(Float pWeakCoverageOutdoor) {
        this.pWeakCoverageOutdoor = pWeakCoverageOutdoor;
    }

    public Float getpWeakCoverageIndoor() {
        return pWeakCoverageIndoor;
    }

    public void setpWeakCoverageIndoor(Float pWeakCoverageIndoor) {
        this.pWeakCoverageIndoor = pWeakCoverageIndoor;
    }

    public Float getpHighLoad() {
        return pHighLoad;
    }

    public void setpHighLoad(Float pHighLoad) {
        this.pHighLoad = pHighLoad;
    }

    public Float getpHighInterference() {
        return pHighInterference;
    }

    public void setpHighInterference(Float pHighInterference) {
        this.pHighInterference = pHighInterference;
    }

    public Float getpHighInterferenceFDD() {
        return pHighInterferenceFDD;
    }

    public void setpHighInterferenceFDD(Float pHighInterferenceFDD) {
        this.pHighInterferenceFDD = pHighInterferenceFDD;
    }

    public Float getpHighInterferenceTDD() {
        return pHighInterferenceTDD;
    }

    public void setpHighInterferenceTDD(Float pHighInterferenceTDD) {
        this.pHighInterferenceTDD = pHighInterferenceTDD;
    }

    public Integer getpLowOm() {
        return pLowOm;
    }

    public void setpLowOm(Integer pLowOm) {
        this.pLowOm = pLowOm;
    }

    public Integer getpHighDrop() {
        return pHighDrop;
    }

    public void setpHighDrop(Integer pHighDrop) {
        this.pHighDrop = pHighDrop;
    }

    public Integer getpUpwardHighDrop() {
        return pUpwardHighDrop;
    }

    public void setpUpwardHighDrop(Integer pUpwardHighDrop) {
        this.pUpwardHighDrop = pUpwardHighDrop;
    }

    public Integer getpDownHighDrop() {
        return pDownHighDrop;
    }

    public void setpDownHighDrop(Integer pDownHighDrop) {
        this.pDownHighDrop = pDownHighDrop;
    }

    public Integer getpLowESRVCCSwitch() {
        return pLowESRVCCSwitch;
    }

    public void setpLowESRVCCSwitch(Integer pLowESRVCCSwitch) {
        this.pLowESRVCCSwitch = pLowESRVCCSwitch;
    }

    public Float getpLowCQI() {
        return pLowCQI;
    }

    public void setpLowCQI(Float pLowCQI) {
        this.pLowCQI = pLowCQI;
    }

    public Float getpLongTimeWithdraw() {
        return pLongTimeWithdraw;
    }

    public void setpLongTimeWithdraw(Float pLongTimeWithdraw) {
        this.pLongTimeWithdraw = pLongTimeWithdraw;
    }

    public Float getpMaintainSolve() {
        return pMaintainSolve;
    }

    public void setpMaintainSolve(Float pMaintainSolve) {
        this.pMaintainSolve = pMaintainSolve;
    }

    public Float getP4GAvailable() {
        return p4GAvailable;
    }

    public void setP4GAvailable(Float p4GAvailable) {
        this.p4GAvailable = p4GAvailable;
    }

    public Float getpIndoorZeroFlow() {
        return pIndoorZeroFlow;
    }

    public void setpIndoorZeroFlow(Float pIndoorZeroFlow) {
        this.pIndoorZeroFlow = pIndoorZeroFlow;
    }

    public Integer getpLongTimeWithdrawQuantity() {
        return pLongTimeWithdrawQuantity;
    }

    public void setpLongTimeWithdrawQuantity(Integer pLongTimeWithdrawQuantity) {
        this.pLongTimeWithdrawQuantity = pLongTimeWithdrawQuantity;
    }

    public Integer getpCoverageProblemQuantity() {
        return pCoverageProblemQuantity;
    }

    public void setpCoverageProblemQuantity(Integer pCoverageProblemQuantity) {
        this.pCoverageProblemQuantity = pCoverageProblemQuantity;
    }

    public Integer getpPerceptionProblemQuantity() {
        return pPerceptionProblemQuantity;
    }

    public void setpPerceptionProblemQuantity(Integer pPerceptionProblemQuantity) {
        this.pPerceptionProblemQuantity = pPerceptionProblemQuantity;
    }

    public Integer getpInterferenceProblemQuantity() {
        return pInterferenceProblemQuantity;
    }

    public void setpInterferenceProblemQuantity(Integer pInterferenceProblemQuantity) {
        this.pInterferenceProblemQuantity = pInterferenceProblemQuantity;
    }

    public Integer getpMaintainProblemQuantity() {
        return pMaintainProblemQuantity;
    }

    public void setpMaintainProblemQuantity(Integer pMaintainProblemQuantity) {
        this.pMaintainProblemQuantity = pMaintainProblemQuantity;
    }

    public Integer getpOtherProblemQuantity() {
        return pOtherProblemQuantity;
    }

    public void setpOtherProblemQuantity(Integer pOtherProblemQuantity) {
        this.pOtherProblemQuantity = pOtherProblemQuantity;
    }

    public static Creator<FifthProblemData> getCREATOR() {
        return CREATOR;
    }

    public FifthProblemData(Parcel in) {

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
