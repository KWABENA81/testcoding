package com.sas.core;

public class TripToll {
    static Double TOLL_RATE = 0.25d;
    private String tollStart;
    private String tollEnd;
    private double tollDistance;
    private double tollCharge;

    public void setStrStartNode(String s) {
    }

    public void setEndNode(String s) {
    }

    public String getTollStart() {
        return tollStart;
    }

    public void setTollStart(String tollStart) {
        this.tollStart = tollStart;
    }

    public void setToolEnd(String tollEnd) {
        this.tollEnd = tollEnd;
    }

    public String getTollEnd() {
        return tollEnd;
    }

    public String getTollDistance() {
        return Double.toString(tollDistance);
    }

    public void setTollDistance(Double distance) {
        this.tollDistance = distance;
        setTollCharge(Double.toString(TOLL_RATE * this.tollDistance));
    }

    public String getTollCharge() {
        return Double.toString(tollCharge);
    }

    public void setTollCharge(String tollCharge) {
        this.tollCharge = Double.parseDouble(tollCharge);
    }
}
