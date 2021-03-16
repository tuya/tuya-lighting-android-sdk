package com.tuya.appsdk.sample.bean;


public class TuyaDeviceManager {

    private static TuyaDeviceManager tuyaDeviceBean;
    private String deviceID;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public static synchronized TuyaDeviceManager getInstance() {

        if (tuyaDeviceBean == null) {
            tuyaDeviceBean = new TuyaDeviceManager();
        }
        return tuyaDeviceBean;
    }

}
