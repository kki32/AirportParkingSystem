package airport;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class LongStayParkingLotTest {
	private LongStayParkingLot longStayLotA;
	private LongStayParkingLot longStayLotB;
	private BillableEntity PersonA;
	private Vehicle vehicleA;
	private Vehicle vehicleB;
	private Vehicle vehicleC;
	private Vehicle vehicleD;

	@Before
	public void setUp() throws Exception {
		longStayLotA = new LongStayParkingLot("LongStay# 1", 3);
		PersonA = new BillableEntity("Ploy", "Christchurch");
		vehicleA = new Vehicle("A1234");
		vehicleB = new Vehicle("B567");
		vehicleC = new Vehicle("C8910", PersonA);
		vehicleD = new Vehicle("D1111");
		longStayLotB = new LongStayParkingLot("LongStay# 2", 20);
	}

	@Test
	public void testLongStayParkingLot() {
		//checks that occupancy is initially 0, nothing inside the occupants collection, the capacity is assigned correctly
		assertEquals(0, longStayLotA.occupancy());
		assertTrue(longStayLotA.occupants().isEmpty());
		assertEquals(3, longStayLotA.capacity());
	}

	@Test
	public void testAdmit() {
		//test adding same vehicle
		longStayLotA.admit(vehicleA);
		longStayLotA.admit(vehicleB);
		longStayLotA.admit(vehicleC);
		longStayLotA.admit(vehicleA);
		assertEquals(3,longStayLotA.occupancy());
		
		//test admitting more than capacity
		longStayLotA.admit(vehicleD);
		assertEquals(3,longStayLotA.occupancy());
		
		//tests admitting vehicle which has been released before.
		longStayLotA.release(vehicleA);
		longStayLotA.release(vehicleB);
		longStayLotA.admit(vehicleB);
		assertEquals(2,longStayLotA.occupancy());
	}

	@Test
	public void testCapacity() {
		
		longStayLotB = new LongStayParkingLot("Long Stay Lot #2", 75);
		//test capacity is always the same
		assertEquals(3, longStayLotA.capacity());
		longStayLotA.admit(vehicleB);
		longStayLotA.admit(vehicleD);
		longStayLotA.release(vehicleB);
		assertEquals(3, longStayLotA.capacity());
		assertEquals(75, longStayLotB.capacity());

	}

	@Test
	public void testComputeCharge() {
	//different scenarios
		
		//2hrs and 30min - within 1 day, overnight
		DateTime day1From = new DateTime(2015,6,1,23,0);
		DateTime day1To = new DateTime(2015,6,2,1,30);
		//1 day and 3hrs - more than 1 day, less than daily maximum
		DateTime day2From = new DateTime(2015,6,14,20,0);
		DateTime day2To = new DateTime(2015,6,15,23,0);
		//2 days and 3hrs and 26min - more than 1 day, more than daily maximum 
		DateTime day3From = new DateTime(2015,6,1,0,4);
		DateTime day3To = new DateTime(2015,6,3,3,30);
		//1 week and 6 days and 2hrs and 1min - more than 2 weeks, less than weekly maximum
		DateTime day4From = new DateTime(2015,6,1,12,0);
		DateTime day4To = new DateTime(2015,6,14,14,1);
		//2 weeks and 4 days and 15hrs - more than 1 week, more than weekly maximum
		DateTime day5From = new DateTime(2015,6,14,3,4);
		DateTime day5To = new DateTime(2015,7,2,18,4);
		
		int ans1 = longStayLotA.computeCharge(day1From,day1To).getAmountMajorInt();
		int ans2 = longStayLotA.computeCharge(day2From,day2To).getAmountMajorInt();
		int ans3 = longStayLotA.computeCharge(day3From,day3To).getAmountMajorInt();
		int ans4 = longStayLotA.computeCharge(day4From,day4To).getAmountMajorInt();
		int ans5 = longStayLotA.computeCharge(day5From,day5To).getAmountMajorInt();

		
		assertEquals(25, ans1);
		assertEquals(49 , ans2);
		assertEquals(75 , ans3);
		assertEquals(250 , ans4);
		assertEquals(375 , ans5);

	}

	@Test
	public void testOccupancy() {
		//test occupancy when admitting same vehicle
		assertEquals(0,longStayLotA.occupancy());
		longStayLotA.admit(vehicleA);
		longStayLotA.admit(vehicleB);
		longStayLotA.admit(vehicleA);		
		assertEquals(2,longStayLotA.occupancy());
		//test when release and admit again
		longStayLotA.release(vehicleA);
		longStayLotA.release(vehicleB);
		longStayLotA.admit(vehicleA);
		assertEquals(1,longStayLotA.occupancy());

	}

	@Test
	public void testOccupants() {
		
		//checks collection gets updated
		assertTrue(longStayLotA.occupants().isEmpty());
		longStayLotA.admit(vehicleA);
		longStayLotA.admit(vehicleB);
		assertTrue(longStayLotA.occupants().contains(vehicleA));
		assertTrue(longStayLotA.occupants().contains(vehicleB));
		assertFalse(longStayLotA.occupants().contains(vehicleC));
	}

	@Test
	public void testRelease()
	{
		//if release more than have got and release from the beginning
		longStayLotA.release(vehicleA);
		assertEquals(0, longStayLotA.occupancy());
		longStayLotA.admit(vehicleA);
		longStayLotA.release(vehicleA);
		assertEquals(0, longStayLotA.occupancy());
		longStayLotA.admit(vehicleB);
		longStayLotA.admit(vehicleA);
		longStayLotA.release(vehicleB);
		assertEquals(1, longStayLotA.occupancy());
				
	}

	@Test
	public void testToString() {
		//check if contains keyword for that parking lot or not
		EconoParkingLot EconoLotA = new EconoParkingLot("Econo Lot #1", 30);
		assertTrue(longStayLotA.toString().contains("LongStayParkingLot"));
		assertFalse(EconoLotA.toString().contains("LongStayParkingLot"));
	}
}
