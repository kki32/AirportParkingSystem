package airport;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTime;

public class EconoParkingLotTest {
	
	private EconoParkingLot econoLotA;
	private EconoParkingLot econoLotB;
	private BillableEntity personA;
	private Vehicle vehicleA;
	private Vehicle vehicleB;
	private Vehicle vehicleC;
	private Vehicle vehicleD;

	@Before
	public void setUp() throws Exception {
		econoLotA = new EconoParkingLot("Econo Lot 1", 3);
		econoLotB = new EconoParkingLot("Econo Lot 2", 20);
		personA = new BillableEntity("Ploy", "Christchurch");
		vehicleA = new Vehicle("A1234");
		vehicleB = new Vehicle("B567");
		vehicleC = new Vehicle("C8910", personA);
		vehicleD = new Vehicle("D1111");
	}

	@Test
	public void testEcoParkingLot() {
		//checks that occupancy is initially 0, nothing inside the occupants collection, and the capacity is assigned correctly
		assertEquals(0, econoLotA.occupancy());
		assertTrue(econoLotA.occupants().isEmpty());
		assertEquals(3, econoLotA.capacity());
	}

	@Test
	public void testAdmit() {
		//tests adding same vehicle
		econoLotA.admit(vehicleA);
		econoLotA.admit(vehicleB);
		econoLotA.admit(vehicleC);
		econoLotA.admit(vehicleA);
		assertEquals(3,econoLotA.occupancy());
		
		//tests admitting more than capacity
		econoLotA.admit(vehicleD);
		assertEquals(3,econoLotA.occupancy());
		
		//tests admitting vehicle which has been released before.
		econoLotA.release(vehicleA);
		econoLotA.release(vehicleB);
		econoLotA.admit(vehicleB);
		assertEquals(2,econoLotA.occupancy());
	}

	@Test
	public void testCapacity() {
		econoLotB = new EconoParkingLot("Express Lot 2", 75);
		//test capacity is always the same
		assertEquals(3, econoLotA.capacity());
		econoLotA.admit(vehicleB);
		econoLotA.admit(vehicleD);
		econoLotA.release(vehicleB);
		assertEquals(3, econoLotA.capacity());
		assertEquals(75, econoLotB.capacity());
	}

	@Test
	public void testComputeCharge()
	{
		
		//2hrs and 30min - within 1 day, overnight
		DateTime day1From = new DateTime(2015,6,1,23,0);
		DateTime day1To = new DateTime(2015,6,2,1,30);
		//1 day and 3hrs - more than 1 day but less than daily maximum
		DateTime day2From = new DateTime(2015,6,14,20,0);
		DateTime day2To = new DateTime(2015,6,15,23,0);
		//2 days and 26min - less than daily maximum
		DateTime day3From = new DateTime(2015,6,1,0,4);
		DateTime day3To = new DateTime(2015,6,3,0,30);
		//1 week and 6 days and 15hrs - more than 1 week, less than weekly maximum, more than daily maximum
		DateTime day4From = new DateTime(2015,6,1,3,4);
		DateTime day4To = new DateTime(2015,6,14,18,4);
		//2 week and 4 days and 2hrs and 1min - over a month, less than weekly maximum, less than daily maximum
		DateTime day5From = new DateTime(2015,6,14,12,0);
		DateTime day5To = new DateTime(2015,7,2,14,1);

		int ans1 = econoLotA.computeCharge(day1From,day1To).getAmountMajorInt();
		int ans2 = econoLotA.computeCharge(day2From,day2To).getAmountMajorInt();
		int ans3 = econoLotA.computeCharge(day3From,day3To).getAmountMajorInt();
		int ans4 = econoLotA.computeCharge(day4From,day4To).getAmountMajorInt();
		int ans5 = econoLotA.computeCharge(day5From,day5To).getAmountMajorInt();
		
		assertEquals(15, ans1);
		assertEquals(30 , ans2);
		assertEquals(35 , ans3);
		assertEquals(210 , ans4);
		assertEquals(285 , ans5);
	}

	@Test
	public void testOccupancy() {
		//test occupancy when admitting same vehicle
		assertEquals(0,econoLotA.occupancy());
		econoLotA.admit(vehicleA);
		econoLotA.admit(vehicleB);
		econoLotA.admit(vehicleA);		
		assertEquals(2,econoLotA.occupancy());
		econoLotA.release(vehicleA);
		econoLotA.release(vehicleB);
		econoLotA.admit(vehicleA);
		assertEquals(1,econoLotA.occupancy());
		
	}
	@Test
	public void testOccupants() {
		//checks collection gets updated
		assertTrue(econoLotA.occupants().isEmpty());
		econoLotA.admit(vehicleA);
		econoLotA.admit(vehicleB);
		assertTrue(econoLotA.occupants().contains(vehicleA));
		assertFalse(econoLotA.occupants().contains(vehicleC));
	}

	@Test
	public void testRelease() {
		//if release more than have got and release from the beginning
		econoLotA.admit(vehicleA);
		econoLotA.release(vehicleA);
		assertEquals(0, econoLotA.occupancy());
		econoLotA.release(vehicleA);
		assertEquals(0, econoLotA.occupancy());
		econoLotA.admit(vehicleB);
		econoLotA.admit(vehicleA);
		econoLotA.release(vehicleB);
		assertEquals(1, econoLotA.occupancy());
	}
 
	@Test
	public void testToString() {
		//check if contains keyword for that parking lot or not
		ExpressParkingLot expressLotA = new ExpressParkingLot("Express Lot #1", 30);
		assertTrue(econoLotA.toString().contains("EconoParkingLot"));
		assertFalse(expressLotA.toString().contains("EcoParkingLot"));
	}

}
