package airport;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class ShortStayParkingLotTest {
	private ShortStayParkingLot shortStayLotA;
	private ShortStayParkingLot shortStayLotB;
	private BillableEntity personA;
	private Vehicle vehicleA;
	private Vehicle vehicleB;
	private Vehicle vehicleC;
	private Vehicle vehicleD;
	
	@Before
	public void setUp() throws Exception {
		shortStayLotA = new ShortStayParkingLot("Short Stay Lot #1", 3);
		personA = new BillableEntity("Ploy", "Christchurch");
		vehicleA = new Vehicle("A1234");
		vehicleB = new Vehicle("B567");
		vehicleC = new Vehicle("C8910", personA);
		vehicleD = new Vehicle("D1111");
		
	}

	@Test
	public void testShortStayParkingLot() {
		//checks that occupancy is initially 0, nothing inside the occupants collection, the capacity is assigned correctly
		assertEquals(0, shortStayLotA.occupancy());
		assertTrue(shortStayLotA.occupants().isEmpty());
		assertEquals(3, shortStayLotA.capacity());
	}
	@Test
	public void testAdmit() {
		//test adding same vehicle
		shortStayLotA.admit(vehicleA);
		shortStayLotA.admit(vehicleB);
		shortStayLotA.admit(vehicleC);
		shortStayLotA.admit(vehicleA);
		assertEquals(3,shortStayLotA.occupancy());
		
		//test admitting more than capacity
		shortStayLotA.admit(vehicleD);
		assertEquals(3,shortStayLotA.occupancy());
		
		//tests admitting vehicle which has been released before.
		shortStayLotA.release(vehicleA);
		shortStayLotA.release(vehicleB);
		shortStayLotA.admit(vehicleB);
		assertEquals(2,shortStayLotA.occupancy());
		
	}

	@Test
	public void testCapacity() {
		shortStayLotB = new ShortStayParkingLot("Short Stay Lot #2", 75);
		//test capacity is always the same
		assertEquals(3, shortStayLotA.capacity());
		shortStayLotA.admit(vehicleB);
		shortStayLotA.admit(vehicleD);
		shortStayLotA.release(vehicleB);
		assertEquals(3, shortStayLotA.capacity());
		assertEquals(75, shortStayLotB.capacity());
	}

	@Test
	public void testComputeCharge() {
		//typical scenario
		
		//1hr and 58mins - within 2 hours, overnight
		DateTime day1From = new DateTime(2015,6,1,23,4);
		DateTime day1To = new DateTime(2015,6,2,1,2);
		//2hrs and 30mins - more than 2 hours, less than daily maximum
		DateTime day2From = new DateTime(2015,6,1,15,0);
		DateTime day2To = new DateTime(2015,6,1,17,30);
		//5 days and 3hrs - less than 1 week, less than daily maximum
		DateTime day3From = new DateTime(2015,6,1,13,0);
		DateTime day3To = new DateTime(2015,6,6,16,0);
		//1 week and 3hrs and 40mins - more than 1 week, more than daily maximum
		DateTime day4From = new DateTime(2015,6,1,15,0);
		DateTime day4To = new DateTime(2015,6,8,18,40);
		
		int ans1 = shortStayLotA.computeCharge(day1From,day1To).getAmountMajorInt();
		int ans2 = shortStayLotA.computeCharge(day2From,day2To).getAmountMajorInt();
		int ans3 = shortStayLotA.computeCharge(day3From,day3To).getAmountMajorInt();
		int ans4 = shortStayLotA.computeCharge(day4From,day4To).getAmountMajorInt();
		
		assertEquals(12 , ans1);
		assertEquals(20 , ans2);
		assertEquals(149 , ans3);
		assertEquals(200 , ans4);
	}

	@Test
	public void testOccupancy() {
		//test occupancy when admitting same vehicle
		assertEquals(0,shortStayLotA.occupancy());
		shortStayLotA.admit(vehicleA);
		shortStayLotA.admit(vehicleB);
		shortStayLotA.admit(vehicleA);		
		assertEquals(2,shortStayLotA.occupancy());
		//test when release and admit again
		shortStayLotA.release(vehicleA);
		shortStayLotA.release(vehicleB);
		shortStayLotA.admit(vehicleA);
		assertEquals(1,shortStayLotA.occupancy());

	}
	@Test
	public void testOccupants() {
		//checks collection gets updated
		assertTrue(shortStayLotA.occupants().isEmpty());
		shortStayLotA.admit(vehicleA);
		shortStayLotA.admit(vehicleB);
		assertTrue(shortStayLotA.occupants().contains(vehicleA));
		assertTrue(shortStayLotA.occupants().contains(vehicleB));
		assertFalse(shortStayLotA.occupants().contains(vehicleC));
	}
	@Test
	public void testRelease() {
		//if release more than have got and release from the beginning
		shortStayLotA.release(vehicleA);
		assertEquals(0, shortStayLotA.occupancy());
		shortStayLotA.admit(vehicleA);
		shortStayLotA.release(vehicleA);
		assertEquals(0, shortStayLotA.occupancy());
		shortStayLotA.admit(vehicleB);
		shortStayLotA.admit(vehicleA);
		shortStayLotA.release(vehicleB);
		assertEquals(1, shortStayLotA.occupancy());
		
	}

	@Test
	public void testToString() {
		//check if contains keyword for that parking lot or not
		EconoParkingLot EconoLotA = new EconoParkingLot("Econo Lot #1", 30);
		assertTrue(shortStayLotA.toString().contains("ShortStayParkingLot"));
		assertFalse(EconoLotA.toString().contains("ShortStayParkingLot"));
	}

}
