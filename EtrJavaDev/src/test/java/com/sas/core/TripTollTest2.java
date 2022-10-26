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

public class TripTollTest2 {

	private TripToll tripToll;
	private List<LocationNode> locationNodes;

	@Test(dataProvider = "dataProvider_TripStartEndPoints")
	public void f(String node, Long result) {
		LocationNode location = TollCalculator.getLocation(node);
		locationNodes.add(location);

		// Validate Hwy entrance ID
		Long nodeID = location.getLocationId();
		Assert.assertEquals(result, nodeID, "Test - Validate Ramp Name matches node ID");
	}

	@DataProvider(name = "dataProvider_TripStartEndPoints")
	public Object[][] dataProvider_TripEndPoints() {
		Object[][] objArray = new Object[][] { { "QEW", 1L }, { "Highway 410", 14L } };
		return objArray;
	}

	@BeforeClass
	public void setup() {
		TollCalculator.readLocations(null);
		tripToll = new TripToll();
		locationNodes = new ArrayList<>(2);
	}

	@Test
	public void testTollCharges() {
		tripToll.setTollStartNode(locationNodes.get(0));
		tripToll.setToolEndNode(locationNodes.get(1));

		tripToll.setTripPoints(tripToll);
		assertTest("Distance", "47.999", tripToll.getTollDistance());

		assertTest("Toll", "12.00", tripToll.getTollCharge());
	}

	private void assertTest(String criteria, String expectedString, String computedString) {
		if (criteria.equals("Distance")) {

			Assert.assertTrue(expectedString.equals(computedString), "Test - Distance is Validated");
		}
		if (criteria.equals("Toll")) {

			Assert.assertTrue(expectedString.equals(computedString), "Test - Toll is Validated");
		}
	}
@AfterClass
void teardown() {
	tripToll = null;
	locationNodes = null;
}
}
