package com.sas.core;

import com.sas.model.LocationNode;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

public class TripToll {
    private static final DecimalFormat chargeDecimalFormat = new DecimalFormat("0.00");
    private static final DecimalFormat distanceDecimalFormat = new DecimalFormat("0.000");
    private static final Double TOLL_RATE = 0.25d;
    private LocationNode tollStartNode;
    private LocationNode tollEndNode;
    private double tollDistance;
    private double tollCharge;

    public LocationNode getTollStartNode() {
        return tollStartNode;
    }

    public void setTollStartNode(LocationNode tollStart) {
        this.tollStartNode = tollStart;
    }

    public void setToolEndNode(LocationNode tollEnd) {
        this.tollEndNode = tollEnd;
    }

    public LocationNode getTollEndNode() {
        return tollEndNode;
    }

    public String getTollDistance() {
        return distanceDecimalFormat.format(tollDistance);
    }

    public void setTollDistance(Double distance) {
        this.tollDistance = distance;
        setTollCharge(Double.toString(TOLL_RATE * this.tollDistance));
    }

    public String getTollCharge() {
        chargeDecimalFormat.setCurrency(Currency.getInstance(Locale.CANADA));
        return chargeDecimalFormat.format(tollCharge);
    }

    public void setTollCharge(String tollCharge) {
        this.tollCharge = Double.parseDouble(tollCharge);
    }
}
