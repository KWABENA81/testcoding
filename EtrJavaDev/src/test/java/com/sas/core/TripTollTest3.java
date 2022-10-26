package com.sas.core;

import org.testng.annotations.Test;

import com.sas.TollCalculator;
import com.sas.model.LocationNode;

import org.testng.annotations.DataProvider;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.Reporter;

public class TripTollTest3 {

	private TripToll tripToll;
	private List<LocationNode> locationNodes;

	@Test(dataProvider = "dataProvider_TripStartEndPoints")
	public void f(String startNode, String endNode) {
		LocationNode location0 = TollCalculator.getLocation(startNode);
		locationNodes.add(0, location0);

		LocationNode location1 = TollCalculator.getLocation(endNode);
		locationNodes.add(1, location1);

//		// Validate Hwy entrance ID
//		Long nodeID0 = location0.getLocationId();
//		Assert.assertEquals(result, nodeID0, "Test - Validate Ramp Name matches "+id);
	}

	@DataProvider(name = "dataProvider_TripStartEndPoints")
	public Object[][] dataProvider_TripEndPoints() {
		Object[][] objArray = new Object[][] { { "QEW", "Highway 410" }, { "QEW", "Highway 403" } };
		return objArray;
	}

	@BeforeClass
	public void setup() {
		TollCalculator.readLocations(null);
		tripToll = new TripToll();
		locationNodes = new ArrayList<>(2);
	}

	@Test(priority=0)
	public void testTollCharges() {
		tripToll.setTollStartNode(locationNodes.get(0));
		tripToll.setToolEndNode(locationNodes.get(1));

		tripToll.setTripPoints(tripToll);		
		
		Reporter.log( "Test - Distances between QEW -\n " + tripToll.getTollStartNode()+"\n"+tripToll.getTollEndNode());
	

		if (tripToll.getTollEndNode().equals("Highway 403")) {
			Assert.assertEquals(tripToll.getTollDistance(), "25.133", "Test - Distance between QEW - " + "Highway 403");
			Reporter.log( "Test - Distances is: "+tripToll.getTollDistance());
			
			Reporter.log( "Test - Distance between QEW - " + "Highway 403");
			Assert.assertEquals(tripToll.getTollCharge(), "6.28", "Test - Toll between QEW - " + "Highway 403");
			Reporter.log( "Test - Toll is: "+tripToll.getTollCharge());
		}

	}
	
	@Test(priority=1)
	public void testTollCharges1() {
		tripToll.setTollStartNode(locationNodes.get(0));
		tripToll.setToolEndNode(locationNodes.get(1));

		tripToll.setTripPoints(tripToll);		
		
		Reporter.log( "Test - Distances between QEW -\n " + tripToll.getTollStartNode()+"\n"+tripToll.getTollEndNode());
	

		if (tripToll.getTollEndNode().equals("Highway 410")) {			
			
			Assert.assertEquals(tripToll.getTollDistance(), "47.999", "Test - Distance between QEW - " + "Highway 410");
			Reporter.log( "Test - Distances is: "+tripToll.getTollDistance());
			
			Assert.assertEquals(tripToll.getTollCharge(), "12.00", "Test - Toll between QEW - " + "Highway 410");
			Reporter.log( "Test - Toll is: "+tripToll.getTollCharge());
		}

	}


	@AfterClass
	void teardown() {
		tripToll = null;
		locationNodes = null;
	}
}



