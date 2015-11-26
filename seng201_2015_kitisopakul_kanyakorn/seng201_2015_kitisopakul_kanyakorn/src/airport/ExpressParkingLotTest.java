package airport;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class ExpressParkingLotTest {
	
	private ExpressParkingLot expressLotA;
	private ExpressParkingLot expressLotB;
	private BillableEntity personA;
	private Vehicle vehicleA;
	private Vehicle vehicleB;
	private Vehicle vehicleC;
	private Vehicle vehicleD;

	@Before
	public void setUp() throws Exception
	{
		expressLotA = new ExpressParkingLot("Express Lot #1", 3);
		personA = new BillableEntity("Ploy", "Christchurch");
		vehicleA = new Vehicle("A1234");
		vehicleB = new Vehicle("B567");
		vehicleC = new Vehicle("C8910", personA);
		vehicleD = new Vehicle("D1111");
	}

	@Test
	public void testExpressParkingLot()
	{
		//checks that occupancy is initially 0, nothing inside the occupants collection, the capacity is assigned correctly
		assertEquals(0, expressLotA.occupancy());
		assertTrue(expressLotA.occupants().isEmpty());
		assertEquals(3, expressLotA.capacity());
	}

	@Test
	public void testAdmit()
	{
		//tests adding same vehicle
		expressLotA.admit(vehicleA);
		expressLotA.admit(vehicleB);
		expressLotA.admit(vehicleC);
		expressLotA.admit(vehicleA);
		assertEquals(3,expressLotA.occupancy());
		
		//tests admitting more than capacity
		expressLotA.admit(vehicleD);
		assertEquals(3,expressLotA.occupancy());
		
		//tests admitting vehicle which has been released before.
		expressLotA.release(vehicleA);
		expressLotA.release(vehicleB);
		expressLotA.admit(vehicleB);
		assertEquals(2,expressLotA.occupancy());
	}
	
	@Test
	public void testCapacity()
	{
		expressLotB = new ExpressParkingLot("Express Lot #2", 75);
		//test capacity is always the same
		assertEquals(3, expressLotA.capacity());
		expressLotA.admit(vehicleB);
		expressLotA.admit(vehicleD);
		expressLotA.release(vehicleB);
		assertEquals(3, expressLotA.capacity());
		assertEquals(75, expressLotB.capacity());
	}

	@Test
	public void testComputeCharge()
	{	
		//15mins - within 15 mins
		DateTime day1From = new DateTime(2015,6,1,17,5);
		DateTime day1To = new DateTime(2015,6,1,17,20);
		//20mins - more than 15 mins
		DateTime day2From = new DateTime(2015,6,1,17,5);
		DateTime day2To = new DateTime(2015,6,1,17,25);
		//1hr - within 41 to 60 mins
		DateTime day3From = new DateTime(2015,6,1,0,0);
		DateTime day3To = new DateTime(2015,6,1,1,0);
		//1hr and 30min - within 1 to 2 hrs
		DateTime day4From = new DateTime(2015,6,1,0,0);
		DateTime day4To = new DateTime(2015,6,1,1,30);
		//2hrs and 1min - more than 2 hrs, less than daily maximum 
		DateTime day5From = new DateTime(2015,6,1,12,4);
		DateTime day5To = new DateTime(2015,6,1,14,5);
		//6days and 4hrs and 1min - less than 1 week, more than weekly maximum
		DateTime day6From = new DateTime(2015,6,1,12,4);
		DateTime day6To = new DateTime(2015,6,7,16,5);
		//1week and 4days and 1hr and 1min - more than 1 week, less than weekly maximum, and less than daily maximum. overnight.
		DateTime day7From = new DateTime(2015,6,14,23,0);
		DateTime day7To = new DateTime(2015,6,25,0,1);
		
		int ans1 = expressLotA.computeCharge(day1From,day1To).getAmountMajorInt();
		int ans2 = expressLotA.computeCharge(day2From,day2To).getAmountMajorInt();
		int ans3 = expressLotA.computeCharge(day3From,day3To).getAmountMajorInt();
		int ans4 = expressLotA.computeCharge(day4From,day4To).getAmountMajorInt();
		int ans5 = expressLotA.computeCharge(day5From,day5To).getAmountMajorInt();
		int ans6 = expressLotA.computeCharge(day6From,day6To).getAmountMajorInt();
		int ans7 = expressLotA.computeCharge(day7From,day7To).getAmountMajorInt();
		
		assertEquals(0 , ans1);
		assertEquals(4 , ans2);
		assertEquals(8 , ans3);
		assertEquals(16 , ans4);
		assertEquals(24 , ans5);
		assertEquals(160 , ans6);
		assertEquals(272 , ans7);
	}

	@Test
	public void testOccupancy()
	{
		//test occupancy when admitting same vehicle
		assertEquals(0,expressLotA.occupancy());
		expressLotA.admit(vehicleA);
		expressLotA.admit(vehicleB);
		expressLotA.admit(vehicleA);		
		assertEquals(2,expressLotA.occupancy());
		//test when release and admit again
		expressLotA.release(vehicleA);
		expressLotA.release(vehicleB);
		expressLotA.admit(vehicleA);
		assertEquals(1,expressLotA.occupancy());
	}

	@Test
	public void testOccupants()
	{	
		//checks collection gets updated
		assertTrue(expressLotA.occupants().isEmpty());
		expressLotA.admit(vehicleA);
		expressLotA.admit(vehicleB);
		assertTrue(expressLotA.occupants().contains(vehicleA));
		assertTrue(expressLotA.occupants().contains(vehicleB));
		assertFalse(expressLotA.occupants().contains(vehicleC));
	}

	@Test
	public void testRelease()
	{
		//if release more than have got and release from the beginning
		expressLotA.release(vehicleA);
		assertEquals(0, expressLotA.occupancy());
		expressLotA.admit(vehicleA);
		expressLotA.release(vehicleA);
		assertEquals(0, expressLotA.occupancy());
		expressLotA.admit(vehicleB);
		expressLotA.admit(vehicleA);
		expressLotA.release(vehicleB);
		assertEquals(1, expressLotA.occupancy());
	}

	@Test
	public void testToString() {
		//check if contains keyword for that parking lot or not
		EconoParkingLot EconoLotA = new EconoParkingLot("Econo Lot #1", 30);
		assertTrue(expressLotA.toString().contains("ExpressParkingLot"));
		assertFalse(EconoLotA.toString().contains("ExpressParkingLot"));
	}

}
