package com.sas.model;


import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class LocationNode implements Comparable<LocationNode> {
    private String locationName;
    private Long locationId;
    private String latitude;
    private String longitude;
    private Set<Route> routes;

    public LocationNode() {
        routes = new TreeSet<>();
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Set<Route> routes) {
        this.routes = routes;
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

    public Long getLocationId() {
                return locationId;
    }

    public void setLocationId(Long locationId) {
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
        str += "\nLocationNode {" +
                " locationId=" + locationId +
                ", locationName='" + locationName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude + "}";
        for (Route rt : this.routes) {
            str += rt.toString();
        }
        return str + "\n";
    }

    @Override
    public int compareTo(LocationNode locationNode) {
        return this.locationId.compareTo(locationNode.locationId);
    }
}