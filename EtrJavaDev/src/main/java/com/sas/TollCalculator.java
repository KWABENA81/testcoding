package com.sas;

import com.sas.core.JSONHandler;
import com.sas.core.TripToll;
import com.sas.model.LocationNode;
import com.sas.model.Route;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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

    private static void readLocations(String[] args) {
        boolean ideArgBool = false;

        try {
            String filename = null;
            if (args[0] == null && args == null) {
                filename = "..\\..\\interchanges.json";
            } else {
                filename = "interchanges.json";
            }

            ideArgBool = readJsonFile(new File(filename));

        } catch (Exception e) {
            logger.log(Level.INFO, "Program cannot be run with no args");
        }
        try {
            if (!ideArgBool) {
                //  retrieve file from resource folder
                URL resource = TollCalculator.class.getClassLoader().getResource("interchanges.json");

                if (resource != null) {
                    Path paths = Paths.get(resource.toURI()).toAbsolutePath();
                    readJsonFile(paths.toFile());
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Program arg from resource failed");
        }
    }

    private static void menuPrompt() {
        String str = "\n_____________________________\n";
        str += "ETR Toll prompt.  Select one:\n";
        str += "S - Start trip, \tX - Exit Program";

        String inputString;
        boolean canProceed = true;

        while (canProceed) {
            locationDisplay();
            System.out.println(str);
            inputString = inputScanner.nextLine();

            switch (inputString) {
                case "S":
                case "s":
                    onRampPrompt();
                    break;
                case "X":
                case "x":
                case "E":
                case "e":
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
            locationDisplay();
            exitRampPrompt();
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception " + e.getMessage() + "\nPress ENTER to proceed");
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
            logger.log(Level.INFO, "Exception " + e.getMessage() + "\nPress ENTER to proceed");
        }
    }

    private static LocationNode getLocation(String string) {
        Optional<LocationNode> optionalLocation = locations.stream()
                .filter(l -> l.getLocationName().equalsIgnoreCase(string)).findFirst();
        LocationNode location = null;
        if (optionalLocation.isPresent()) {
            location = optionalLocation.get();
        }
        return location;
    }

    private static void locationDisplay() {
        if (locations == null) return;
        List<String> namesList = locations.stream().map(l -> l.getLocationName())
                .sorted(Comparator.naturalOrder()).collect(Collectors.toList());

        int counter = 0;
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("\nETR 407 ramp names");
        sbuilder.append("\n------------------\n");
        for (String name : namesList) {
            counter += 1;
            sbuilder.append(patchString(name));
            if (counter % 9 == 0) {
                sbuilder.append("\n");
            }
        }
        System.out.println(sbuilder);
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

        String string = "\ncostOfTrip ('";
        string += tripToll.getTollStartNode().getLocationName();
        string += "', '";
        string += tripToll.getTollEndNode().getLocationName();

        string += "')\nDistance:\t";
        string += tripToll.getTollDistance();
        string += "km\nCost:\t\t$";
        string += tripToll.getTollCharge();
        System.out.println(string);
    }

    private static void tollCompute(LocationNode startNode, LocationNode endNode) {
        if (startNode.getLocationId() > endNode.getLocationId()) {
            tollComputeDown(startNode, endNode);
        }
        if (startNode.getLocationId() < endNode.getLocationId()) {
            tollComputeUp(startNode, endNode);
        }
    }

    private static void tollComputeDown(LocationNode startNode, LocationNode endNode) {
        LocationNode currentNode = startNode;
        double distance = 0.;
        while (currentNode.getLocationId() > endNode.getLocationId()) {
            long currentId_Final = currentNode.getLocationId();

            //  get next id node
            Route nextRoute;
            Optional<Route> nextRouteOptional = currentNode.getRoutes().stream()
                    .filter(next -> next.getToId() < currentId_Final).findFirst();

            if (nextRouteOptional.isPresent()) {
                nextRoute = nextRouteOptional.get();
                distance += Double.parseDouble(nextRoute.getDistance());

                //  hop into the next node retrieved above
                long nextRouteId = nextRoute.getToId();
                Optional<LocationNode> optionalNode = locations.stream()
                        .filter(n -> n.getLocationId().equals(nextRouteId)).findFirst();
                if (optionalNode.isPresent()) {
                    currentNode = optionalNode.get();
                }
            }
        }
        tripToll.setTollDistance(distance);
    }

    private static void tollComputeUp(LocationNode startNode, LocationNode endNode) {
        LocationNode currentNode = startNode;
        double distance = 0.;
        while (!currentNode.equals(endNode)) {
            long currentId_Final = currentNode.getLocationId();

            //  get next id node
            Route nextRoute;
            Optional<Route> nextRouteOptional = currentNode.getRoutes().stream()
                    .filter(next -> next.getToId() > currentId_Final).findFirst();

            if (nextRouteOptional.isPresent()) {
                nextRoute = nextRouteOptional.get();
                distance += Double.parseDouble(nextRoute.getDistance());

                //  hop into the next node retrieved above
                long nextRouteId = nextRoute.getToId();
                Optional<LocationNode> optionalNode = locations.stream()
                        .filter(n -> n.getLocationId().equals(nextRouteId)).findFirst();
                if (optionalNode.isPresent()) {
                    currentNode = optionalNode.get();
                }
            }
        }
        tripToll.setTollDistance(distance);
    }

    private static boolean readJsonFile(File file) {
        boolean boolReadFile = false;
        try (FileReader reader = new FileReader(file)) {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            jsonObject.keySet().iterator().forEachRemaining(element -> {
                handle(jsonObject);
            });
            boolReadFile = true;
        } catch (FileNotFoundException e) {
            logger.log(Level.INFO, "File Not Found Exception " + e.getMessage());
        } catch (IOException e) {
            logger.log(Level.INFO, "IOException " + e.getMessage());
        } catch (org.json.simple.parser.ParseException e) {
            logger.log(Level.INFO, "org.json.simple.parser.ParseException " + e.getMessage());
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