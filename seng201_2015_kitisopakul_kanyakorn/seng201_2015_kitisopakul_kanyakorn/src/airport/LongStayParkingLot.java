package airport;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.money.Money;
import org.joda.time.DateTime;
/**
 * Class which controls the vehicle in and out of the Long Stay parking lot.
 * @author kki32
 *
 */
public class LongStayParkingLot implements Parkable
{
	private String name;
	private int initialCapacity;
	//added properties
	private int available;
	private ArrayList<Vehicle> occupiedLongStayParkingLot = new ArrayList<Vehicle>();

	/**
	 * Constructor for class.
	 * @param name Name of the Long Stay parking lot
	 * @param initialCapacity The space the parking lot has
	 */
	public LongStayParkingLot(String name, int initialCapacity)
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
			if (occupiedLongStayParkingLot.contains(v) == false)
			{
				occupiedLongStayParkingLot.add(v);
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
		Money charge = Money.parse("NZD 25.00");
		
		int totalDay = to.getDayOfYear() - from.getDayOfYear();
		int totalMinute = to.getMinuteOfDay() - from.getMinuteOfDay();
		final int weekMax = 5;
		final int dayMax = 180;
		
		//if overnight
		if (totalMinute < 0)
		{
			int YesterdayMinute  = 1440 - from.getMinuteOfDay();
			totalMinute = YesterdayMinute + to.getMinuteOfDay();
			totalDay -= 1;
		}		
		
		//not special case
		if (totalDay >= 1)
		{
			charge = charge.minus(25);
			if (totalDay >= 7)
			{
				int total_wk = totalDay / 7;
				totalDay = totalDay - (total_wk * 7);
				charge = charge.plus(total_wk * 125);
			}
			if (totalDay >= weekMax)
			{
				charge = charge.plus(125);
				return charge;
			}
			
			else
			{
				charge = charge.plus(25 * totalDay);
			}
			
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

		return charge;
	}

	public int occupancy()
	{
		return initialCapacity - available;
	}

	public Collection<Vehicle> occupants()
	{
		return occupiedLongStayParkingLot;
	}

	public void release(Vehicle v)
	{
		{
			//don't release more than have got
			if (occupancy() > 0)
			{
				if(occupiedLongStayParkingLot.contains(v))
				{
					occupiedLongStayParkingLot.remove(v);
					available = available+1;
				}
			}
		}
	}
	
	
	/**
	 * Override toString() method. Provide the name of the parking lot.
	 */
	@Override
	public String toString()
	{
		String ans = "LongStayParkingLot called " + this.name;
		return ans;
	}
	
}
