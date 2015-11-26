package airport;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.money.Money;
import org.joda.time.DateTime;
/**
 * Class which controls the vehicle in and out of the Short Stay parking lot.
 * @author kki32
 *
 */
public class ShortStayParkingLot implements Parkable
{

	private java.lang.String name;
	private int initialCapacity;
	//added properties
	private int available;
	private ArrayList<Vehicle> occupiedShortStayParkingLot = new ArrayList<Vehicle>();
	
	/**
	 * Constructor for class.
	 * @param name Name of the Econo parking lot
	 * @param initialCapacity The space the parking lot has
	 */
	
	public ShortStayParkingLot(java.lang.String name, int initialCapacity)
	{
		this.name = name;
		this.initialCapacity = initialCapacity;
		//added properties
		this.available = initialCapacity;
	}
	
	
	public void admit(Vehicle v)
	{	
		//don't admit anymore after reach capacity
		if (occupancy() < initialCapacity)
		{
			if (occupiedShortStayParkingLot.contains(v) == false)
			{
				occupiedShortStayParkingLot.add(v);
				available = available-1;
			}
			
		}
	}

	public int capacity()
	{
		return initialCapacity;
	}

	public Money computeCharge(DateTime from, DateTime to)
	{
		//minimum charge
		Money charge = Money.parse("NZD 12.00");
		int totalDay = to.getDayOfYear() - from.getDayOfYear();
		int totalMinute = to.getMinuteOfDay() - from.getMinuteOfDay();
		final int dayMax = 180;

		//if overnight
		if(totalMinute < 0)
		{
			int YesterdayMinute  = 1440 - from.getMinuteOfDay();
			totalMinute = YesterdayMinute + to.getMinuteOfDay();
			totalDay -= 1;
		}
		
		//not special case
		if (totalDay >= 1)
		{
			charge = charge.minus(12);
			if (totalDay >= 7)
			{
				while (totalDay >= 7)
				{
					charge = charge.plus(175);
					totalDay -= 7;
				}
			}
			//take care of the remainder
			charge = charge.plus(totalDay * 25);
			
			if (totalMinute > 0)
			{	
				if (totalMinute > dayMax)
				{
					charge = charge.plus(25);
				}
				
				else
				{
					while(totalMinute > 0)
					{
						totalMinute -= 60;
						charge = charge.plus(8);
					}
				}	
			}
		}
		
		else
		{
			if (totalMinute > 120)
			{	
				if (totalMinute > 180)
				{
					charge = charge.minus(12);
					charge = charge.plus(25);
				}
				
				else
				{
					totalMinute -= 120;
					while(totalMinute > 0)
					{
						totalMinute -= 60;
						charge = charge.plus(8);
					}

				}
			}
		}
	
		return charge;
	}

	public int occupancy()
	{
		return initialCapacity - available;
	}

	public Collection<Vehicle> occupants()
	{
		return occupiedShortStayParkingLot;
	}

	public void release(Vehicle v)
	{
		//don't release more than have got
		if (occupancy() > 0)
		{
			if (occupiedShortStayParkingLot.contains(v))
			{
				occupiedShortStayParkingLot.remove(v);
				available = available+1;
			}
		}
	}
	
	public java.lang.String toString()
	{
		String ans = "ShortStayParkingLot called " + this.name;
		return ans;
	}

}
