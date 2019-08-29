package com.hbmcc.wangsen.netsupport.util.okhttp.Bean;

import java.util.List;
//成员变量名与json的key需要对应
public class Demo {
    private List<Pojo> listpojo;
    private String errorCode;
    private String errorMsg;

    public List<Pojo> getListpojo() {
        return listpojo;
    }

    public void setListpojo(List<Pojo> listpojo) {
        this.listpojo = listpojo;
    }

    public String getErrorCode12() {
        return errorCode;
    }

    public void setErrorCode12(String errorCode12) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
