package airport;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

public class ChargeAdvicerTest {

	@Test
	public void testAdviceCharge() {
		ChargeAdvicer advicerA = new ChargeAdvicer();
		
		//15min - Express best
		DateTime day1From = new DateTime(2015,6,14,20,0);
		DateTime day1To = new DateTime(2015,6,14,20,15);
		//2hr - ShortStay best
		DateTime day2From = new DateTime(2015,6,14,20,0);
		DateTime day2To = new DateTime(2015,6,14,22,0);
		//3hr - Econo best
		DateTime day3From = new DateTime(2015,6,14,20,0);
		DateTime day3To = new DateTime(2015,6,14,23,0);
		//3day and 3hrs - Econo best
		DateTime day4From = new DateTime(2015,6,14,20,0);
		DateTime day4To = new DateTime(2015,6,17,23,0);
		
		assertTrue(advicerA.adviceCharge(day1From, day1To)[0].contains("Express"));
		assertEquals("25", advicerA.adviceCharge(day1From, day1To)[1]);
		
		assertTrue(advicerA.adviceCharge(day2From, day2To)[0].contains("ShortStay"));
		assertEquals("13", advicerA.adviceCharge(day2From, day2To)[1]);
		
		assertTrue(advicerA.adviceCharge(day3From, day3To)[0].contains("Econo"));
		assertEquals("10",advicerA.adviceCharge(day3From, day3To)[1]);
		
		assertFalse(advicerA.adviceCharge(day4From, day4To)[0].contains("Express"));
		assertTrue(advicerA.adviceCharge(day4From, day4To)[0].contains("Econo"));
		assertEquals("60",advicerA.adviceCharge(day4From, day4To)[1]);
		
	}

}
