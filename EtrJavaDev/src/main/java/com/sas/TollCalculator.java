package com.sas;

import com.sas.core.JSONHandler;
import com.sas.model.LocationNode;
import com.sas.model.Route;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TollCalculator {
    private static Map<Long, Object> handlerLocationsMap;
    private static Set<LocationNode> locations;
    private static Scanner inputScanner;
    private static String exitLocationName;
    private static String startLocationName;
    private static TripToll tripToll;
    private static final Logger logger = Logger.getLogger(TollCalculator.class.getName());

    public static void main(String... args) throws URISyntaxException, IOException {
        readLocations(args);
        inputScanner = new Scanner(System.in);
        menuPrompt();
    }

    private static void readLocations(String... args) throws URISyntaxException {
        try {
            boolean ideArgBool = false;
            if (args != null && !args[0].isEmpty()) {
                //  retrieve file from project root folder
                ideArgBool = readJsonFile(new File(args[0]));
            }
            if (!ideArgBool) {
                //  retrieve file from resource folder   
                URL resource = TollCalculator.class.getClassLoader().getResource("interchanges.json");
                Path paths = Paths.get(resource.toURI()).toAbsolutePath();

                readJsonFile(paths.toFile());
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Program cannot be run with no args");
        }
    }

    private static void menuPrompt() throws IOException {
        String str = "\n______________________\nToll calculator prompt.\n" +
                "Select ..:\tO - (O)N Ramp:\tX - E(x)it Ramp:\tF - (F)inish: \n";

        long pid = 0L;
        String options = "Exit";
        String inputString = null;

        boolean canExit = !options.equalsIgnoreCase("EXIT");

        while (!canExit) {
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
                case "F":
                case "f":
            }

            canExit = false;
        }
    }

    private static void onRampPrompt() {
        printout("\nType HWY Start: ");
        String startString = inputScanner.nextLine();
        Optional<String> locationOptional = getLocation(startString);

        if (locationOptional.isPresent()) {
            setTollStart(locationOptional.get());
            nicePrintLocations();
            exitRampPrompt();
        }
    }

    private static void setTollStart(String s) {
      //  if (tripToll == null)
       ///     tripToll = new TripToll();
        TripToll.strStartNode=s;//setStrStartNode(s);
    }

    private static void setTollEnd(String s) {
//        if (tripToll != null) {
//            tripToll.setStrEndNode(s);
//        }
        TripToll.strEndNode=s;
    }
    private static void exitRampPrompt() {
        printout("\nType HWY Exit: ");
        String exitString = inputScanner.nextLine();
        Optional<String> locationOptional = getLocation(exitString);

        if (locationOptional.isPresent()) {
            setTollEnd(locationOptional.get());
            optionTollCompute();
        }
    }


    private static Optional<String> getLocation(String string) {
        Optional<String> optional = locations.stream().map(l -> l.getLocationName())
                .filter(s -> s.equalsIgnoreCase(string)).findFirst();
        return optional;
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
            if (counter % 7 == 0) {
                strNames += "\n";
            }
        }
        printout(strNames);
    }

    private static String patchString(String name) {
        int strLength = 25;
        StringBuilder sbuilder = new StringBuilder(name);
        while ((sbuilder.length() < strLength)) {
            sbuilder.append(" ");
        }
        return sbuilder.toString();
    }


    private static void optionTollCompute() {
        System.out.println("ldkl: "  );

        printout(Double.toString(TripToll.toll));
        printout("\nToll Start:\t" + /*startLocationName*/TripToll.strStartNode/*getStartNodeId()*//*strStartNode*/);
        printout("Toll Exit:\t" + TripToll.strEndNode+/*exitLocationName*/"tripToll.strEndNode");
        printout("Distance:\t" + TripToll.distance/*distance*/);
        printout("Cost:\t\t" + TripToll.toll/*toll*/);
    }

    private static long checkpid() throws IOException {
        long pid = 0L;//process.pid();
        Runtime.getRuntime().exec("kill " + pid);
        return pid;
    }

    private static void printout(String string) {
        System.out.println(string);
    }

    private static boolean readJsonFile(File file) {
        boolean boolHandled = false;
        try (FileReader reader = new FileReader(file)) {
            org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            jsonObject.keySet().iterator().forEachRemaining(element -> {
                handle(jsonObject);
            });
            boolHandled = true;
        } catch (FileNotFoundException e) {
            logger.log(Level.INFO, "FileNotFoundException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.log(Level.INFO, "IOException " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            logger.log(Level.INFO, "ParseException " + e.getMessage());
        }
        return boolHandled;
    }

    private static void handle(JSONObject jsonObj) {
        //
        JSONHandler jsonHandler = new JSONHandler();
        jsonHandler.parseJSONObject(jsonObj);
        handlerLocationsMap = jsonHandler.getLocationsMap();
        locations = jsonHandler.getLocationNodes();
    }

     private static class TripToll {
        private static final Double TOLL_RATE = 0.25d;
        private static Long startNodeId;
        private static  Long endNodeId;
        private static String strStartNode;
        private static String strEndNode;
        private  static Double toll;
        private  static Double distance;
        private  static LocationNode startNode;
        private  static LocationNode endNode;

         public Double getToll() {
             return toll;
         }

         public void setToll(Double toll) {
             this.toll = toll;
         }

         public Double getDistance() {
             return distance;
         }

         public void setDistance(Double distance) {
             this.distance = distance;
         }

         public Long getStartNodeId() {
             return startNodeId;
         }

         public void setStartNodeId(Long startNodeId) {
             this.startNodeId = startNodeId;
         }

         public void setStrStartNode(String s) {
            this.startNode = locations.stream()
                    .filter(l -> l.getLocationName().equalsIgnoreCase(s)).findFirst().get();

            computeToll();
        }

        public void setStrEndNode(String s) {
            this.endNode = locations.stream()
                    .filter(l -> l.getLocationName().equalsIgnoreCase(s)).findFirst().get();

            computeToll();
        }

        private void computeToll() {
            double dist = 0;
            if (this.endNode != null && this.startNode != null) {
                Long endId = this.endNode.getLocationId();
                Long startId = this.startNode.getLocationId();

                while (endId > startId) {

                    Route vb = this.startNode.getRoutes().stream()
                            .filter(n -> n.getToId() > this.startNode.getLocationId())
                            .findFirst().get();

                    dist += Double.parseDouble(vb.getDistance());
                    startId = vb.getToId();
                }
                TripToll.this.setDistance(dist);//distance = dist;
                TripToll.this.setToll(TOLL_RATE+6d);//this.toll = this.distance * TOLL_RATE;
                if (endId == startId) {
                    this.toll = 0.0d;
                    this.distance = 0.d;
                }
            }
        }


    }
}