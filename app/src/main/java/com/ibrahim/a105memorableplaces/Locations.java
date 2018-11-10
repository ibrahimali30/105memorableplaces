package com.ibrahim.a105memorableplaces;

import java.util.ArrayList;

public class Locations {

    ArrayList<SavedPlaces> locations;

    public Locations(ArrayList<SavedPlaces> locations) {
        this.locations = locations;
    }

    public ArrayList<SavedPlaces> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<SavedPlaces> locations) {
        this.locations = locations;
    }
}
