package com.sas;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        readLocations();
    }

    private static void readLocations() throws URISyntaxException, IOException {
        String fileString = null;
        String jsonFile = "interchanges.json";
        URL resource = Main.class.getClassLoader().getResource(jsonFile);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        fileString = new String(bytes);

        if (fileString != null)
            print(fileString);

        LocationNode locationNode0 =
                createLocationNode(1, "QEW", 43.484361d, -79.766037d);
        LocationNode locationNode2 =
                createLocationNode(2, "Dundas Street", 43.383554d, -79.833478d);
        computeDistanceAndCharge(locationNode0, locationNode2);
    }

    private static void print(String fileString) {
        System.out.println("jason file: " + fileString);
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