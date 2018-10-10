package ru.yandex.dunaev.mick.mecellularnetwork;

public class WifiInfo {
    private String mac;

    public WifiInfo(String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
