package com.sas;

public class Main {
    public static void main(String[] args) {
        readLocations();
    }

    private static void readLocations() {
        String fileString = null;
        String file = "";
        fileString = new String();
        LocationNode locationNode0 =
                createLocationNode(1, "QEW", 43.484361d, -79.766037d);
        LocationNode locationNode2 =
                createLocationNode(2, "Dundas Street", 43.383554d, -79.833478d);
        computeDistanceAndCharge(locationNode0, locationNode2);
    }

    public static void computeDistanceAndCharge(LocationNode locationNode1, LocationNode locationNode2) {
        print(locationNode1, locationNode2);
    }

    private static void print(LocationNode locationNode1, LocationNode locationNode2) {
        System.out.println(locationNode2.toString());
        System.out.println(locationNode1.toString());
    }

    public static LocationNode createLocationNode(int id, String name, double lat, double lng) {
        return new LocationNode(name, id, Double.toString(lat), Double.toString(lng));
    }
}