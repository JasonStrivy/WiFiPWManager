package com.jiushu.wifipwmanager.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 13/11/15.
 */
public class WpaSupplication {
    public String headerStr;
    public List<WiFiNetWork> wiFiNetWorks;

    public String getHeaderStr() {
        return headerStr;
    }

    public void setHeaderStr(String headerStr) {
        this.headerStr = headerStr;
    }

    public List<WiFiNetWork> getWiFiNetWorks() {
        if (this.wiFiNetWorks == null)
            this.wiFiNetWorks = new ArrayList<>();

        return wiFiNetWorks;
    }

    public void setWiFiNetWorks(List<WiFiNetWork> wiFiNetWorks) {
        this.wiFiNetWorks = wiFiNetWorks;
    }
}
