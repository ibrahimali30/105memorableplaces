package com.ibrahim.a105memorableplaces;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class SavedPlaces implements Serializable {

    LatLng latLng;
    String name = "";

    public SavedPlaces(LatLng latLng, String name) {
        this.latLng = latLng;
        this.name = name;
        MainActivity.locationsname.add(name);
//        MainActivity.locationsname.add(name);
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public SavedPlaces() {
    }

    public String getName() {
        return name;
    }
}
