package airport;

import java.util.Collection;
import java.util.ArrayList;

import org.joda.money.Money;
import org.joda.time.DateTime;
/**
 * Class which controls the vehicle in and out of the Express parking lot. The class checks whether there is enough room
 * for the vehicle before admitting it in. It is then charge the vehicle when the vehicle leaves the parking lot.
 * 
 * @author kki32
 *
 */
public class ExpressParkingLot implements Parkable
{
	
	private String name;
	private int initialCapacity;
	//added properties
	private int available;
	private ArrayList<Vehicle> occupiedExpressParkingLot = new ArrayList<Vehicle>();

	/**
	 * Constructor for class.
	 * @param name Name of the Express parking lot
	 * @param initialCapacity The space the parking lot has
	 */
	public ExpressParkingLot(String name, int initialCapacity)
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
				if (occupiedExpressParkingLot.contains(v) == false)
				{
					occupiedExpressParkingLot.add(v);
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
		Money charge = Money.parse("NZD 0.00");

		int totalDay = to.getDayOfYear() - from.getDayOfYear();
		int totalMinute = to.getMinuteOfDay() - from.getMinuteOfDay();
		final int weekMax = 5;
		final int dayMax = 240;
		
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
			while(totalDay >= 7)
			{
				charge = charge.plus(160);
				totalDay -= 7;
			}
			
			//take care of the remainder
			if (totalDay < weekMax)
			{
				charge = charge.plus(totalDay * 32);
			}
			
			else
			{
				//no need to worry about minute. cannot be any expensive than this. 
				charge = charge.plus(160);
				return charge;
			}
			
			if (totalMinute > 0)
			{	
				if (totalMinute >= 240)
				{
					charge = charge.plus(32);
				}
				
				else
				{
					while (totalMinute >= 60)
					{
						totalMinute -= 60;
						charge = charge.plus(8);
					}
					
					if (totalMinute > 0)
					{
						charge = charge.plus(8);
					}
				}
			}
		}
		
		//special case
		else
		{
			if (totalMinute > 15)
			{
				if(totalMinute <= 40)
				{
					charge = charge.plus(4);
				}
				
				else if (totalMinute <= 60)
				{
					charge = charge.plus(8);
				}
				
				else if (totalMinute <= 120)
				{
					charge = charge.plus(16);
				}
				
				else if (totalMinute < dayMax)
				{
					charge = charge.plus(16+8);
				}
				
				else if (totalMinute >= dayMax)
				{
					charge = charge.plus(32);
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
		return occupiedExpressParkingLot;
	}

	public void release(Vehicle v)
	{
		//don't release more than have got
		if (occupancy() > 0)
		{
			if (occupiedExpressParkingLot.contains(v))
			{
				occupiedExpressParkingLot.remove(v);
				available = available+1;
			}
		}
	}	
	

	/**
	 * Override toString() method. Provide the name of the parking lot.
	 */
	@Override
	public String toString()
	{
		String ans = "ExpressParkingLot called " + this.name;
		return ans;
	}
	
}
