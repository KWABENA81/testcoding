package com.sas;

import com.sas.core.JSONHandler;
import com.sas.core.TripToll;
import com.sas.model.LocationNode;
import com.sas.model.Route;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TollCalculator {
    private static final Logger logger = Logger.getLogger(TollCalculator.class.getName());
    private static Set<LocationNode> locations;
    private static Scanner inputScanner;
    private static TripToll tripToll;

    public static void main(String... args) {
        readLocations(args);
        inputScanner = new Scanner(System.in);
        menuPrompt();
    }

    private static void readLocations(String... args) {
        try {
            boolean ideArgBool = false;
            if (args != null && !args[0].isEmpty()) {
                //  retrieve file from project root folder
                ideArgBool = readJsonFile(new File(args[0]));
            }
            if (!ideArgBool) {
                //  retrieve file from resource folder   
                URL resource = TollCalculator.class.getClassLoader().getResource("interchanges.json");

                if (resource != null) {
                    Path paths = Paths.get(resource.toURI()).toAbsolutePath();
                    readJsonFile(paths.toFile());
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Program cannot be run with no args");
        }
    }

    private static void menuPrompt() {
        String str = "\n______________________\n";
        str += "Toll Calculator prompt.\n";
        str += "Select:\tS - (S)tart, \tX - E(x)it Program\n";

        String inputString = null;
        boolean canProceed = true;

        while (canProceed) {
            nicePrintLocations();
            System.out.println(str);
            inputString = inputScanner.nextLine();

            switch (inputString) {
                case "S":
                case "s":
                    onRampPrompt();
                    break;
                case "X":
                case "x":
                    canProceed = false;
            }
        }
    }

    private static void onRampPrompt() {
        tripToll = new TripToll();
        System.out.println("\nHwy Entrance Location: ");
        try {
            String startString = inputScanner.nextLine();
            LocationNode startLocation = getLocation(startString);

            tripToll.setTollStartNode(startLocation);
            nicePrintLocations();
            exitRampPrompt();
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception " + e.getMessage());
        }
    }

    private static void exitRampPrompt() {
        try {
            System.out.println("\nHwy Exit Location: ");
            String exitString = inputScanner.nextLine();
            LocationNode exitLocation = getLocation(exitString);

            tripToll.setToolEndNode(exitLocation);
            tollCompute();
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception " + e.getMessage());
        }
    }

    private static LocationNode getLocation(String string) {
        Optional<LocationNode> optionalLocation = locations.stream()//.map(l -> l.getLocationName())
                .filter(l -> l.getLocationName().equalsIgnoreCase(string)).findFirst();
        LocationNode location = null;
        if (optionalLocation.isPresent()) {
            location = optionalLocation.get();
        }
        return location;
    }

    private static void nicePrintLocations() {
        if (locations == null) return;
        List<String> namesList = locations.stream().map(l -> l.getLocationName())
                .sorted(Comparator.naturalOrder()).collect(Collectors.toList());

        int counter = 0;
        String strNames = "\nETR 407 ramp names\n---------------------\n";
        for (String name : namesList) {
            counter += 1;
            strNames += patchString(name);
            if (counter % 9 == 0) {
                strNames += "\n";
            }
        }
        System.out.println(strNames);
    }

    private static String patchString(String name) {
        int strLength = 21;
        StringBuilder stringBuilder = new StringBuilder(name);
        while ((stringBuilder.length() < strLength)) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private static void tollCompute() {
        tollCompute(tripToll.getTollStartNode(), tripToll.getTollEndNode());

        String string = "\ncostOfTrip (\'";
        string += tripToll.getTollStartNode().getLocationName();
        string += "\', \'";
        string += tripToll.getTollEndNode().getLocationName();

        string += "\')\nDistance:\t";
        string += tripToll.getTollDistance();
        string += "km\nCost:\t\t$";
        string += tripToll.getTollCharge();
        System.out.println(string);
    }

    private static void tollCompute(LocationNode startNode, LocationNode endNode) {
        double charge = 0;
        double distance = 0d;
        Set<Route> startRoutes = startNode.getRoutes();
        Set<Route> endRoutes = endNode.getRoutes();
        LocationNode currentNode = startNode;
        if (startNode.getLocationId() > endNode.getLocationId()) {
            printout(startRoutes.isEmpty() + " 170---------");
            printout(endRoutes.toString() + " t171");
            while (!currentNode.equals(endNode)) {
                long currentId_Final = currentNode.getLocationId();

                //  get next id node
                Route nextRoute = currentNode.getRoutes().stream()
                        .filter(next -> next.getToId() > currentId_Final).findFirst().get();
                distance += Double.parseDouble(nextRoute.getDistance());

                //  hop into the next node retrieved above
                currentNode = locations.stream()
                        .filter(n -> n.getLocationId().equals(nextRoute.getToId())).findFirst().get();
            }
            tripToll.setTollDistance(distance);
        }
        if (startNode.getLocationId() < endNode.getLocationId()) {
            while (!currentNode.equals(endNode)) {
                long currentId_Final = currentNode.getLocationId();

                //  get next id node
                Route nextRoute = null;
                Optional<Route> nextRouteOptional = currentNode.getRoutes().stream()
                        .filter(next -> next.getToId() > currentId_Final).findFirst();

                if (nextRouteOptional.isPresent()) {
                    nextRoute = nextRouteOptional.get();
                    distance += Double.parseDouble(nextRoute.getDistance());

                    //  hop into the next node retrieved above
                    long nextRouteId = nextRoute.getToId();
                    currentNode = locations.stream()
                            .filter(n -> n.getLocationId().equals(nextRouteId)).findFirst().get();
                }
            }
            tripToll.setTollDistance(distance);
        }
    }

    private static void printout(String string) {
        System.out.println(string);
    }

    private static boolean readJsonFile(File file) {
        boolean boolReadFile = false;
        try (FileReader reader = new FileReader(file)) {
            org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            jsonObject.keySet().iterator().forEachRemaining(element -> {
                handle(jsonObject);
            });
            boolReadFile = true;
        } catch (FileNotFoundException e) {
            logger.log(Level.INFO, "File Not Found Exception " + e.getMessage());
        } catch (IOException e) {
            logger.log(Level.INFO, "IOException " + e.getMessage());
        } catch (ParseException e) {
            logger.log(Level.INFO, "ParseException " + e.getMessage());
        }
        return boolReadFile;
    }

    private static void handle(JSONObject jsonObj) {
        JSONHandler jsonHandler = new JSONHandler();
        jsonHandler.parseJSONObject(jsonObj);
        //  handlerLocationsMap = jsonHandler.getLocationsMap();
        locations = jsonHandler.getLocationNodes();
    }

}