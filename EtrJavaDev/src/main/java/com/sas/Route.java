package com.sas;

import java.util.Objects;

public class Route {
    private LocationNode sourceLocation;

    private LocationNode destLocation;

    private Float distance;

    public Route(LocationNode sourceLocation, LocationNode destLocation, Float distance) {
        this.sourceLocation = sourceLocation;
        this.destLocation = destLocation;
        this.distance = distance;
    }

    public LocationNode getSourceLocation() {
        return sourceLocation;
    }

    public LocationNode getDestLocation() {
        return destLocation;
    }

    public Float getDistance() {
        return distance;
    }

    public void setSourceLocation(LocationNode sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public void setDestLocation(LocationNode destLocation) {
        this.destLocation = destLocation;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return Objects.equals(getSourceLocation(), route.getSourceLocation()) && Objects.equals(getDestLocation(),
                route.getDestLocation()) && getDistance().equals(route.getDistance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSourceLocation(), getDestLocation(), getDistance());
    }

    @Override
    public String toString() {
        return "Route{" +
                "locationNode1=" + sourceLocation +
                ", locationNode2=" + destLocation +
                ", distance=" + distance +
                '}';
    }
}
