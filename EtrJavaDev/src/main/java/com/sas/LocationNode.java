package com.sas;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationNode {
    private String locationName;
    private Integer locationId;
    private String latitude;
    private String longitude;
    private List<Route> routeList;

    public LocationNode(String locationName, Integer id, String latitude, String longitude) {
        this.locationName = locationName;
        this.locationId = id;
        this.latitude = latitude;
        this.longitude = longitude;

        routeList = new ArrayList<>();
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getLocationId() {
        return locationId;
    }


    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationNode)) return false;
        LocationNode that = (LocationNode) o;
        return getLocationName().equals(that.getLocationName()) &&
                locationId.equals(that.locationId) &&
                getLatitude().equals(that.getLatitude())
                && getLongitude().equals(that.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, latitude, longitude);
    }

    @Override
    public String toString() {
        String str = "";
        str += "LocationNode{" +
                "locationName='" + locationName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", locationId=" + locationId + "}";
        return str;
    }

}