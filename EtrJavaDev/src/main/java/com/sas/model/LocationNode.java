package com.sas.model;


import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class LocationNode implements Comparable<LocationNode> {
    private final Set<Route> routes = new TreeSet<>();
    private String locationName;
    private Long locationId;
    private String latitude;
    private String longitude;

    public LocationNode() {

    }

    public Set<Route> getRoutes() {
        return routes;
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nLocationNode {");
        stringBuilder.append(" locationId=");
        stringBuilder.append(locationId);
        stringBuilder.append(", locationName='");
        stringBuilder.append(locationName);
        stringBuilder.append('\'');
        stringBuilder.append(", latitude=");
        stringBuilder.append(latitude);
        stringBuilder.append(", longitude=");
        stringBuilder.append(longitude);
        stringBuilder.append("}");
        for (Route rt : this.routes) {
            stringBuilder.append(rt.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(LocationNode locationNode) {
        return this.locationId.compareTo(locationNode.locationId);
    }
}