package airport;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.money.Money;
import org.joda.time.DateTime;

/**
 * Class which controls the vehicle in and out of the Econo parking lot. The class checks whether there is enough room
 * for the vehicle before admitting it in. It is then charge the vehicle when the vehicle leaves the parking lot.
 * 
 * @author kki32
 */

public class EconoParkingLot implements Parkable
{
	private String name;
	private int initialCapacity;
	//added properties
	private int available;
	private ArrayList<Vehicle> occupiedEconoParkingLot = new ArrayList<Vehicle>();

	/**
	 * Constructor for class.
	 * @param name Name of the Econo parking lot
	 * @param initialCapacity The space the parking lot has
	 */
	
	public EconoParkingLot(String name, int initialCapacity)
	{
		this.name = name;
		this.initialCapacity = initialCapacity;
		//added property
		this.available = initialCapacity;
	}
	
	
	public void admit(Vehicle v) 
	{
		//don't admit anymore after reach capacity
		if (occupancy() < initialCapacity)
		{
			if (occupiedEconoParkingLot.contains(v) == false)
			{
				occupiedEconoParkingLot.add(v);
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
		Money charge = Money.parse("NZD 15.00");
		
		int totalDay = to.getDayOfYear() - from.getDayOfYear();
		int totalMinute = to.getMinuteOfDay() - from.getMinuteOfDay();
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
			charge = charge.minus(15);
			if (totalDay >= 7)
			{
				while (totalDay >= 7)
				{
					totalDay -= 7;
					charge = charge.plus(105);
				}
			}
			//take care of the remainder
			charge = charge.plus(totalDay * 15);
			
			if (totalMinute > 0)
			{	
				if (totalMinute >= dayMax)
					{
						charge = charge.plus(15);
					}
				
				else
					{
						while(totalMinute > 0)
						{
							totalMinute -= 60;
							charge = charge.plus(5);
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
		return occupiedEconoParkingLot;
	}


	public void release(Vehicle v)
	{
		//don't release more than have got
		if (occupancy() > 0)
		{
			if (occupiedEconoParkingLot.contains(v))
			{
				occupiedEconoParkingLot.remove(v);
				available = available+1;
			}
		}
	}
	
	/**
	 * Override toString() method. Provide the name of the parking lot.
	 */
	public String toString()
	{
		String ans = "EconoParkingLot called " + this.name;
		return ans;
	}
	
}
