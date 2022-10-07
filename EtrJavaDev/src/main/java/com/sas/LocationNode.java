package com.sas;


import java.util.Objects;

public class LocationNode {

    private String locationName;

    private Integer locationId;

    private String latitude;

    private String longitude;

    private LocationNode prevNode;

    private LocationNode nextNode;

    public LocationNode() {
        this.locationName = null;
        this.locationId = null;
        this.latitude = null;
        this.longitude = null;
        this.prevNode = null;
        this.nextNode = null;
    }

    public LocationNode(String locationName, Integer id, String latitude, String longitude) {
        this.locationName = locationName;
        this.locationId = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.prevNode = null;
        this.nextNode = null;
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

    public LocationNode getPrevNode() {
        return prevNode;
    }

    public void setPrevNode(LocationNode prevNode) {
        this.prevNode = prevNode;
    }

    public LocationNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(LocationNode nextNode) {
        this.nextNode = nextNode;
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
                ", \nlatitude=" + latitude +
                ", longitude=" + longitude +
                ",\nlocationId=" + locationId;
        ;
        str += (this.prevNode != null) ? ", prevNode=" + prevNode.locationName : "";
        str += (this.nextNode != null) ? ", nextNode=" + nextNode.locationName : "";
        str += "}";
        return str;
    }

}