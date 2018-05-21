package com.dyhdyh.plugin.wifipwd;

/**
 * @author dengyuhan
 *         created 2018/5/21 17:43
 */
public class WiFiInfo {
    private String ssid;
    private String password;
    private int priority;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
