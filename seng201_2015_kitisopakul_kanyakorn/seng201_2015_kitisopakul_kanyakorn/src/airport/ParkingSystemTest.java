package airport;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class ParkingSystemTest {
	private ParkingSystem airportpark;
	private LongStayParkingLot longStayLotA;
	private ShortStayParkingLot shortStayLotA;
	private ExpressParkingLot expressLotA;
	private EconoParkingLot econoLotA;
	private Vehicle vehicleA;
	private Vehicle vehicleB;
	private Vehicle vehicleC;
	private Vehicle vehicleD;
	
	@Before
	public void setUp() throws Exception {
		airportpark = new ParkingSystem();
		longStayLotA = new LongStayParkingLot("Long Stay Lot #1", 40);
		shortStayLotA = new ShortStayParkingLot("Short Stay Lot #1", 10);
		expressLotA = new ExpressParkingLot("Express Lot #1", 100);
		econoLotA = new EconoParkingLot("Econo Lot #1", 50);
		vehicleA = new Vehicle("12A");
		vehicleB = new Vehicle("365G");
		vehicleC = new Vehicle("A123", new BillableEntity("Abbey", "Auckland"));
		vehicleD = new Vehicle("B456" , new BillableEntity("Barry" , "Oamaru"));
		
	}

	@Test
	public void testParkingSystem()
	{//test that the parking system is initially empty
		assertEquals(0, airportpark.lots().size());
	}

	@Test
	public void testAddLot()
	{
		ShortStayParkingLot shortStayLotB = new ShortStayParkingLot("Short Stay Lot#2", 44);
		//check that the size of the lot is increasing
		assertTrue(airportpark.lots().isEmpty());
		airportpark.addLot(shortStayLotA);
		assertEquals(1, airportpark.lots().size());
		airportpark.addLot(longStayLotA);
		airportpark.addLot(expressLotA);
		assertEquals(3, airportpark.lots().size());
		airportpark.addLot(econoLotA);
		airportpark.addLot(shortStayLotB);
		assertEquals(5, airportpark.lots().size());
		
	}

	@Test
	public void testLots() {
		//check that a lot is added properly
		airportpark.addLot(longStayLotA);
		airportpark.addLot(shortStayLotA);
		Collection<Parkable> resultArray = airportpark.lots();
		assertEquals(2, airportpark.lots().size());
		assertTrue(resultArray.contains(longStayLotA));
		assertTrue(resultArray.contains(shortStayLotA));
		assertFalse(resultArray.contains(econoLotA));
		assertFalse(resultArray.contains(expressLotA));		
	}

	@Test
	public void testParkOptions() {
		//make sure there is only four options here
		String[] expectedArray = {"Express Parking", "Long Stay Parking", "Econo Parking", "Short Stay Parking"};
		String[] resultArray = ParkingSystem.parkOptions();
		assertArrayEquals(expectedArray, resultArray);
	}

	@Test
	public void testVehicleFor()
	{
		longStayLotA.admit(vehicleA);
		shortStayLotA.admit(vehicleB);
		econoLotA.admit(vehicleC);
		longStayLotA.admit(vehicleD);
		
		//ensure that grabbing the vehicle with the same registration number
		assertTrue(vehicleB.regNo() == airportpark.vehicleFor("365G").regNo());
		assertFalse(vehicleB.regNo() == airportpark.vehicleFor("A123").regNo());
		assertTrue(vehicleC.regNo() == airportpark.vehicleFor("A123").regNo());
		assertFalse(vehicleA.regNo() == airportpark.vehicleFor("000").regNo());
		
	}

}
