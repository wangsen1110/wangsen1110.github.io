package com.hbmcc.wangsen.netsupport.event;

import com.hbmcc.wangsen.netsupport.telephony.UeStatus;

public class UpdateUeStatusEvent {
    public static UeStatus ueStatus;

    public UpdateUeStatusEvent(UeStatus ueStatus){
        this.ueStatus = ueStatus;
    }
}
