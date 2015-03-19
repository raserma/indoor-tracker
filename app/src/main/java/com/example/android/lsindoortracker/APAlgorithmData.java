package com.example.android.lsindoortracker;

/**
 * Created by raul on 19.3.2015.
 */
public class APAlgorithmData {
    public String bssid;
    public double distance;
    public int RSS;

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRSS() {
        return RSS;
    }

    public void setRSS(int RSS) {
        this.RSS = RSS;
    }

    public APAlgorithmData(String bssid, double distance, int RSS){
        this.bssid = bssid;
        this.distance = distance;
        this.RSS = RSS;
    }

}
