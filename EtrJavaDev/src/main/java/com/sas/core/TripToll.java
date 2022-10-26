package com.sas.core;

import com.sas.model.LocationNode;
import com.sas.model.Route;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class TripToll {
	private static final DecimalFormat chargeDecimalFormat = new DecimalFormat("0.00");
	private static final DecimalFormat distanceDecimalFormat = new DecimalFormat("0.000");
	private static final Double TOLL_RATE = 0.25d;
	private LocationNode tollStartNode;
	private LocationNode tollEndNode;
	private double tollDistance;
	private double tollCharge;
	private Set<LocationNode>locations ;

	
	public TripToll() {
		super();locations=JSONHandler.getLocationNodes();
	}

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

	public void setTripPoints(TripToll tripToll) {
		LocationNode startNode=tripToll.getTollStartNode();
		LocationNode endNode=tripToll.getTollEndNode();
		
		if (startNode.getLocationId() > endNode.getLocationId()) {
			tollComputeDown(startNode, endNode);
		}
		if (startNode.getLocationId() < endNode.getLocationId()) {
			tollComputeUp(startNode, endNode);
		}
	}

	private void tollComputeDown(LocationNode startNode, LocationNode endNode) {
		LocationNode currentNode = startNode;
		double distance = 0.;
		while (currentNode.getLocationId() > endNode.getLocationId()) {
			long currentId_Final = currentNode.getLocationId();

			// get next id node
			Route nextRoute;
			Optional<Route> nextRouteOptional = currentNode.getRoutes().stream()
					.filter(next -> next.getToId() < currentId_Final).findFirst();

			if (nextRouteOptional.isPresent()) {
				nextRoute = nextRouteOptional.get();
				distance += Double.parseDouble(nextRoute.getDistance());

				// hop into the next node retrieved above
				long nextRouteId = nextRoute.getToId();
				Optional<LocationNode> optionalNode = locations.stream()
						.filter(n -> n.getLocationId().equals(nextRouteId)).findFirst();
				if (optionalNode.isPresent()) {
					currentNode = optionalNode.get();
				}
			}
		}
		this.setTollDistance(distance);
	}

	private void tollComputeUp(LocationNode startNode, LocationNode endNode) {
		LocationNode currentNode = startNode;
		double distance = 0.;
		while (!currentNode.equals(endNode)) {
			long currentId_Final = currentNode.getLocationId();

			// get next id node
			Route nextRoute;
			Optional<Route> nextRouteOptional = currentNode.getRoutes().stream()
					.filter(next -> next.getToId() > currentId_Final).findFirst();

			if (nextRouteOptional.isPresent()) {
				nextRoute = nextRouteOptional.get();
				distance += Double.parseDouble(nextRoute.getDistance());

				// hop into the next node retrieved above
				long nextRouteId = nextRoute.getToId();
				Optional<LocationNode> optionalNode = locations.stream()
						.filter(n -> n.getLocationId().equals(nextRouteId)).findFirst();
				if (optionalNode.isPresent()) {
					currentNode = optionalNode.get();
				}
			}
		}
		this.setTollDistance(distance);
	}

}
