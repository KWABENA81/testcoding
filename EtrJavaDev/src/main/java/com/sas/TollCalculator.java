package com.sas;

import com.sas.core.JSONHandler;
import com.sas.core.TripToll;
import com.sas.model.LocationNode;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TollCalculator {
    private static final Logger logger = Logger.getLogger(TollCalculator.class.getName());
    //  private static Map<Long, Object> handlerLocationsMap;
    private static Set<LocationNode> locations;
    private static Scanner inputScanner;
    //  private static String exitLocationName;
    // private static String startLocationName;
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
        String str = "\n______________________\nToll Calculator prompt.\n" +
                "Select:\tO - (O)N Ramp\tX - E(x)it Ramp\tE - (E)nd\n";

        String inputString = null;
        boolean canProceed = true;

        while (canProceed) {
            nicePrintLocations();
            printout(str);
            inputString = inputScanner.nextLine();

            switch (inputString) {
                case "X":
                case "x":
                    exitRampPrompt();
                    break;
                case "O":
                case "o":
                    onRampPrompt();
                    break;
                case "E":
                case "e":
                    canProceed = false;
            }
        }
    }

    private static void onRampPrompt() {
        tripToll = new TripToll();
        printout("\nProvide Highway Start: ");
        String startString = inputScanner.nextLine();
        String startLocation = getLocation(startString);

        tripToll.setTollStart(startLocation);
        nicePrintLocations();
        exitRampPrompt();
    }

    private static void exitRampPrompt() {
        printout("\nProvide Highway Exit: ");
        String exitString = inputScanner.nextLine();
        String exitLocation = getLocation(exitString);

        tripToll.setToolEnd(exitLocation);
        optionTollCompute();
    }

    private static String getLocation(String string) {
        String location = locations.stream().map(l -> l.getLocationName())
                .filter(s -> s.equalsIgnoreCase(string)).findFirst().get();
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
        printout(strNames);
    }

    private static String patchString(String name) {
        int strLength = 21;
        StringBuilder stringBuilder = new StringBuilder(name);
        while ((stringBuilder.length() < strLength)) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private static void optionTollCompute() {
        printout("\ncostOfTrip(\'" + tripToll.getTollStart()
                + "\', \'" + tripToll.getTollEnd() + "\')");
        printout("Distance:\t" + tripToll.getTollDistance());
        printout("Cost:\t" + tripToll.getTollCharge());
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