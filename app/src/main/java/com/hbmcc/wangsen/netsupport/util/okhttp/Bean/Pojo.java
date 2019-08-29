package com.hbmcc.wangsen.netsupport.util.okhttp.Bean;

public class Pojo {
    public String desc;
    public int id;
    public String imagePath;
    public int isVisible;
    public int order;
    public String title;
    public int type;
    public String url;

    public Pojo(String desc, int id2, String imagePath, int isVisible, int order, String title, int type, String url) {
        this.desc = desc;
        this.id = id2;
        this.imagePath = imagePath;
        this.isVisible = isVisible;
        this.order = order;
        this.title = title;
        this.type = type;
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId2() {
        return id;
    }

    public void setId2(int id2) {
        this.id = id2;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
