package com.jiushu.wifipwmanager.data;

/**
 * Created by simon on 15/11/15.
 */
public class WiFiNetWork {
    public Integer id;
    public String ssid;
    public String psk;
    public String comment;
    public String key_mgmt;
    public boolean isChineseSsid;
    public Integer priority;
    public Integer sup_dis_reason;
    public boolean isDel;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isChineseSsid() {
        return isChineseSsid;
    }

    public void setIsChineseSsid(boolean isChineseSsid) {
        this.isChineseSsid = isChineseSsid;
    }

    public String getKey_mgmt() {
        return key_mgmt;
    }

    public void setKey_mgmt(String key_mgmt) {
        this.key_mgmt = key_mgmt;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPsk() {
        return psk;
    }

    public void setPsk(String psk) {
        this.psk = psk;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Integer getSup_dis_reason() {
        return sup_dis_reason;
    }

    public void setSup_dis_reason(Integer sup_dis_reason) {
        this.sup_dis_reason = sup_dis_reason;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setIsDel(boolean isDel) {
        this.isDel = isDel;
    }
}
