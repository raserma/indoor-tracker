package com.example.android.lsindoortracker;

import android.content.Context;
import android.graphics.Point;
import android.net.wifi.ScanResult;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raul on 17.3.2015.
 */
public class LSAlgorithm {
    public Context mapViewActivityContext;

    public LSAlgorithm(Context context){
        this.mapViewActivityContext = context;
    }

    /**
     * Main method which finds user position based on WiFi scan results.
     * @param results WiFi scan results list with all the known AP data
     * @return Point object with AP position
     * Point(-10, results.size()) if acquired APs < required AP for LS
     */
    public Point getUserPosition(List<ScanResult> results){
        IndoorTrackerDatabaseHandler apdbhandler =  new IndoorTrackerDatabaseHandler
                (mapViewActivityContext);


        /* Filters known APs from database */
        results = apdbhandler.filterKnownAPDB (results);

        /* TUT WLAN network has 4 SSIDs for each AP. Therefore, only one must be chosen,
        filtering out the rest */
        results = filterSSIDs (results);


        // If less than 4 AP were acquired
        if(results.size() < 4)
            return new Point (-10, results.size());
        /* Gets AP with strongest RSS */
        String bssidStrongest = getStrongestAP (results);
        /* Gets AP position from database */
        Point userPosition = apdbhandler.getAPPositionDB(bssidStrongest);
        return userPosition;
    }

    /**
     * Gets AP with strongest RSS from scan result lists
     * @param results WiFi scan results list with all the known AP data
     * @return strongest AP bssid
     */
    private String getStrongestAP (List<ScanResult> results){
        String bssid = null;
        int rss = 0, max = -120, index = 0;
        // Get strongest RSS AP from WiFi results list
        for(int i = 0; i < results.size(); i++){
            rss = results.get(i).level;
            // Comparison to find the maximum value
            if(rss > max) {
                max = rss;
                index = i;
            }
        }
        return results.get(index).BSSID;
    }

    /**
     * Filters duplicate BSSIDs out caused by different TUT SSIDs.
     * NOTE: TUT WLAN network provides four BSSIDs (MACs) for each AP, one for each SSID: TUT,
     * TUT-WPA,
     * LANGATON-WPA and eduroam. Since these four BSSIDs represent the same position and same
     * power level (this is not actually true), only one must prevail.
     *
     * @param results WiFi scan results list with all the known AP data
     * @return WiFi scan results list with all the known AP data and without duplicate BSSIDs
     */
    private List<ScanResult> filterSSIDs (List<ScanResult> results){
        // It adds the contents of wifi scan results to a Map which will not allow duplicates and
        // then add the Map back to the wifi scan List.
        Map<String, ScanResult> map = new LinkedHashMap<String, ScanResult>();
        for (ScanResult ays : results) {
            map.put(ays.BSSID.toString(), ays);
        }
        results.clear();
        results.addAll(map.values());
        return results;
    }
}
