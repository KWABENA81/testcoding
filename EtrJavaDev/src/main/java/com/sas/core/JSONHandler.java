package com.sas.core;

import com.sas.model.LocationNode;
import com.sas.model.Route;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONHandler {

    private final Logger logger = Logger.getLogger(JSONHandler.class.getName());
    private final Map<Long, Object> locationsMap = new TreeMap<>();
    private final Set<LocationNode> locationNodes = new TreeSet<>();


    public void parseJSONObject(JSONObject jsonObj) {
        Iterator<String> iterator = jsonObj.keySet().iterator();
        iterator.forEachRemaining(key -> {
            parseValues(key, jsonObj.get(key));
        });
    }

    private void parseValues(String key, Object value) {
        if (value instanceof JSONArray) {
            parseJSONArray(key, (JSONArray) value);
        } else if (value instanceof JSONObject) {
            parseJSONObject((JSONObject) value);
        }
        try {
            Long locationId = Long.parseLong(key);
            locationsMap.put(locationId, value);

            createObject(locationId, value);
        } catch (NumberFormatException ex) {
            logger.log(Level.INFO, "To handle Long primitives only");
        }
    }

    private void createObject(Long id, Object value0) {
        if (value0 instanceof JSONObject) {
            JSONObject value = (JSONObject) value0;
            LocationNode locationNode = new LocationNode();
            locationNode.setLocationId(id);
            locationNode.setLocationName((String) value.get("name"));
            locationNode.setLatitude(Double.toString((Double) value.get("lat")));
            locationNode.setLongitude(Double.toString((Double) value.get("lng")));

            locationNodes.add(locationNode);
            Object routeMap = value.get("routes");
            if (routeMap instanceof JSONArray) {
                parseRouteJSONArray(id, (JSONArray) routeMap);
            }
        }
    }

    private void parseRouteJSONArray(Long id, JSONArray routeArray) {
        Iterator iterator = routeArray.iterator();
        while (iterator.hasNext()) {

            Object jsonObj = iterator.next();
            if (jsonObj instanceof JSONObject) {
                Route route = new Route();

                Object toId = ((JSONObject) jsonObj).get("toId");
                route.setToId((Long) toId);

                Object distance = ((JSONObject) jsonObj).get("distance");
                double dist = (distance instanceof Double) ? (Double) distance : 0.d;
                route.setDistance(Double.toString(dist));

                Optional<LocationNode> locNodeOptional = locationNodes.stream().
                        filter(x -> x.getLocationId().equals(id)).findFirst();
                if (locNodeOptional.isPresent()) {
                    LocationNode locNode = locNodeOptional.get();
                    locNode.getRoutes().add(route);
                }
            }
        }
    }

    private void parseJSONArray(String key, JSONArray jsonArray) {
        Iterator<Object> arrayIterator = jsonArray.iterator();
        arrayIterator.forEachRemaining(elem -> {

            parseValues(key, elem);
        });
    }

    public Set<LocationNode> getLocationNodes() {
        return locationNodes;
    }
}
