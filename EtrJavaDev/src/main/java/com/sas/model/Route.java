package com.sas.model;

import java.util.Objects;

public class Route implements Comparable<Route> {
    private Long toId;
    private String distance;

    public Route(Long toId, String distance) {
        this.toId = toId;
        this.distance = distance;
    } public Route() {
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Route route) {
        return this.toId.compareTo(route.toId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return getToId().equals(route.getToId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToId());
    }

    @Override
    public String toString() {
        return "\nRoute{" +
                "toId=" + toId +
                ", distance='" + distance +
                '}';
    }
}
