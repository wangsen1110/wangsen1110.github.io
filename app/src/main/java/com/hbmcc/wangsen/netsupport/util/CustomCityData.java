package com.hbmcc.wangsen.netsupport.util;

import java.util.ArrayList;
import java.util.List;

public class CustomCityData  {
    private String id;
    private String name;
    private List<CustomCityData> list = new ArrayList<>();

    public CustomCityData(String id, String name) {
        this.id = id;
        this.name = name;
    }
}