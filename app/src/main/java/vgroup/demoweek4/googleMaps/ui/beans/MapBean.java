package vgroup.demoweek4.googleMaps.ui.beans;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by DELL on 10/3/2017.
 */

public class MapBean {
    private LatLng latLng;
    private String name, distance;

    public MapBean() {
    }

    public MapBean(LatLng latLng, String name, String distance) {
        this.latLng = latLng;
        this.name = name;

        this.distance = distance;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
