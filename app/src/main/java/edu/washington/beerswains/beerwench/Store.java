package edu.washington.beerswains.beerwench;

import com.parse.ParseGeoPoint;

/**
 * Created by Smyth on 6/8/2015.
 */
public class Store {
    private ParseGeoPoint location;
    private String address;
    private String name;

    public Store(String name, String address, ParseGeoPoint location) {
        this.name = name;
        this.address = address;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public ParseGeoPoint getLocation() {
        return this.location;
    }
}
