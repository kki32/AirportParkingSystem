package airport;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VehicleTest {
	private Vehicle vehicleA;
	private Vehicle vehicleB;

	@Before
	public void setUp() throws Exception {
		vehicleA = new Vehicle("4825");
		vehicleB = new Vehicle("215", new BillableEntity("Kelly", "Timaru"));
	}

	@Test
	public void testVehicleString() {
		assertEquals("4825", vehicleA.regNo());
		assertFalse("215" == vehicleA.regNo());
	}

	@Test
	public void testVehicleStringBillableEntity() {
		assertFalse("215" == vehicleA.regNo());
	}
	

	@Test
	public void testRegNo() {
		//no space
		assertFalse("215".equals(vehicleA.regNo()));
		assertFalse("215  " == vehicleB.regNo());
	}

	@Test
	public void testSetOwner() {
//		assertTrue(b.toString().contains("Kelly"));
//		b.setOwner(new BillableEntity("Lydia", "Christchurch"));
//		assertFalse(b.toString().contains("Kelly"));
//		assertTrue(b.toString().contains("Lydia"));
//		assertTrue(b.toString().contains("Christchurch"));
	}

	@Test
	public void testToString() {
		assertTrue(vehicleA.toString().contains("4825"));
	}

}
