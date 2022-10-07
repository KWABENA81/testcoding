package com.sas;

import java.util.Objects;

public class Route {
    private LocationNode nextLocationNode;
    private String distance;
    private Boolean enterStatus;
    private Boolean exitStatus;

    public Route(LocationNode nextLocationNode, String distance) {
        this.nextLocationNode = nextLocationNode;
        this.distance = distance;

    }

    public LocationNode getNextLocationNode() {
        return nextLocationNode;
    }

    public Boolean getEnterStatus() {
        return enterStatus;
    }

    public void setEnterStatus(Boolean enterStatus) {
        this.enterStatus = enterStatus;
    }

    public Boolean getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(Boolean exitStatus) {
        this.exitStatus = exitStatus;
    }

    public void setNextLocationNode(LocationNode nextLocationNode) {
        this.nextLocationNode = nextLocationNode;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return Objects.equals(getNextLocationNode(), route.getNextLocationNode())
                && getDistance().equals(route.getDistance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNextLocationNode(), getDistance());
    }

    @Override
    public String toString() {
        return "Route{" +
                "nextLocationNode=" + nextLocationNode +
                ", distance=" + distance +
                '}';
    }
}
