package airport;

import org.joda.time.DateTime;
/**
 * Give advice to the client the cheapest option and the difference in value between the most expensive option and the cheapest option in absolute value and percentage
 * @author kki32
 *
 */
public class ChargeAdvicer
{
	//create an instance of each lot so can use computeCharge method
	private ShortStayParkingLot shortStayLot = new ShortStayParkingLot("Short stay lot", 1);
	private LongStayParkingLot longStayLot = new LongStayParkingLot("Long stay lot", 1);
	private ExpressParkingLot expressLot = new ExpressParkingLot("Express lot", 1);
	private EconoParkingLot econoLot = new EconoParkingLot("Econo lot", 1);
	
	/**
	 * Store the cheapest option, and the difference in value between the most expensive option and the cheapest option in absolute value and percentage, into an array
	 * which will be used to display info later.
	 * @author kki32
	 * @param from The time the Vehicle is admitted.
	 * @param to The time the vehicle is released.
	 * @return the cheapest option, the difference in value between the most expensive option and the cheapest option in absolute value and percentage
	 */
	public String[] adviceCharge(DateTime from, DateTime to)
	{
		int shortStayLotCharge = shortStayLot.computeCharge(from, to).getAmountMajorInt();
		int longStayLotCharge = longStayLot.computeCharge(from, to).getAmountMajorInt();
		int expressLotCharge = expressLot.computeCharge(from, to).getAmountMajorInt();
		int econoLotCharge = econoLot.computeCharge(from, to).getAmountMajorInt();
	
		String[] option = {"ShortStay", "LongStay", "Express", "Econo"};
		int[] chargeArray = {shortStayLotCharge, longStayLotCharge, expressLotCharge,econoLotCharge};
		int cheap = chargeArray[0];
		int expensive = chargeArray[0];
		
		int pos = 0;
		int cheapPos = 0;
		int expensivePos = 0;
		
		//find cheap and expensive lot, and keep track of their position
		for(int charge : chargeArray)
		{
			if(charge < cheap)
			{
				cheapPos = pos;
				cheap = chargeArray[cheapPos];
			}
			if(charge > expensive)
			{
				expensivePos = pos;
				expensive = chargeArray[expensivePos];
			}
			pos = pos + 1;
		}
		
		//calculate the percentage change
		String cheapChoice = option[cheapPos] + ", $" + Integer.toString(chargeArray[cheapPos]);
		int numerator = chargeArray[expensivePos] - chargeArray[cheapPos];
		double denominator = ((chargeArray[expensivePos] + chargeArray[cheapPos]) /(double) 2);
		double percentageVal = (numerator/(double) denominator) * 100;
	
		//limit to 1 decimal place
		String percentages =  String.format("%.4g", percentageVal);
	
		//assemble answer
		String[] ans = {cheapChoice, Integer.toString(numerator), percentages};
		return ans;

	}

}
