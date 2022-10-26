package com.sas.core;

import org.testng.annotations.Test;

import com.sas.TollCalculator;
import com.sas.model.LocationNode;

import org.testng.annotations.DataProvider;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.Reporter;

public class TripTollTest {

	private TripToll tripToll;

	@Test(dataProvider = "getTestData")
	public void tollQEWto410(String startNode, String endNode) {
		LocationNode location0 = TollCalculator.getLocation(startNode);

		LocationNode location1 = TollCalculator.getLocation(endNode);
		testTollCharges(location0, location1);
	}

	@Test(dataProvider = "getTestData")
	public void tollQEWto403(String startNode, String endNode) {
		LocationNode location0 = TollCalculator.getLocation(startNode);

		LocationNode location1 = TollCalculator.getLocation(endNode);
		testTollCharges(location0, location1);
	}

	@DataProvider(name = "getTestData")
	public Object[][] getTestData(Method method) {
		
		Object[][] objArrayQEW_HWY410 = new Object[][] { { "QEW", "Highway 410" } };
		Object[][] objArrayQEW_HWY403 = new Object[][] { { "QEW", "Highway 403" } };

		String methodName = method.getName();
		if (methodName.equals("tollQEWto403")) {
			return objArrayQEW_HWY403;
		} //
		else if (methodName.equals("tollQEWto410")) {
			return objArrayQEW_HWY410;
		} //
		else {
			return new Object[][] { { "No Test Data for Start" }, { "No Test Data for End" } };
		}

	}

	@BeforeClass
	public void setup() {
		TollCalculator.readLocations(null);
		tripToll = new TripToll();
	}

	
	private void testTollCharges(LocationNode location0, LocationNode location1) {
		tripToll.setTollStartNode(location0);
		tripToll.setToolEndNode(location1);

		tripToll.setTripPoints(tripToll);

		Reporter.log(
				"Test - Distances between QEW -\n " + tripToll.getTollStartNode() + "\n" + tripToll.getTollEndNode());

		if (tripToll.getTollEndNode().equals("Highway 403")) {
			Assert.assertEquals(tripToll.getTollDistance(), "25.133", "Test - Distance between QEW - " + "Highway 403");
			Reporter.log("Test - Distances is: " + tripToll.getTollDistance());

			Reporter.log("Test - Distance between QEW - " + "Highway 403");
			Assert.assertEquals(tripToll.getTollCharge(), "6.28", "Test - Toll between QEW - " + "Highway 403");
			Reporter.log("Test - Toll is: " + tripToll.getTollCharge());
		}

	}

	

	@AfterClass
	void teardown() {
		tripToll = null;
	}
}
