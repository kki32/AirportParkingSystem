package airport;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Class representing the overall parking business. Keeps info about the various parking lots we have.
 * When vehicles enter the system, their number plates are recorded by cameras and a Vehicle record is created if we haven't seen it before.
 * This enables us to prevent vehicles being in two places at the same time, and similar problems.
 * @author kki32
 *
 */
public class ParkingSystem
{
	private ArrayList<Vehicle> keepTrackVehicles = new ArrayList<Vehicle>();
	private Collection<Parkable> allParkingLot = new ArrayList<Parkable>();
	private static String[] ParkingType = {"Express Parking","Long Stay Parking", "Econo Parking", "Short Stay Parking"};

	/**
	 * Constructor for class.
	 */
	public ParkingSystem()
	{
	}

	/**
	 * @param p parking lot
	 * Add a new parking lot.
	 */
	public void addLot(Parkable p)
	{
		allParkingLot.add(p);
	}

	/**
	 * @return all parking lots we have
	 * Returns (in no particular order) the parking lots we have.
	 * Ideally, it shouldn't be possible to change these without using addLot().
	 */
	public java.util.Collection<Parkable> lots()
	{
		return allParkingLot;
	}
	
	/**
	 * Provides a list of the parking lot types we offer.
	 * @return list of the parking lot types
	 */
	public static String[] parkOptions()
	{		
		return ParkingType;
	}
	/**
	 * 
	 * @param reg registration number (any combination of alphabetical letter(s) and/or number(s))
	 * @return vehicle with that registration number
	 * Find the vehicle corresponding to the registration number. Add it if it isn't already known.
	 */
	public Vehicle vehicleFor(java.lang.String reg)
	{		
		for (Vehicle vehicle : keepTrackVehicles)
		{
			if (vehicle.regNo().equals(reg))
			{
				Vehicle ans = vehicle;
				return ans;
			}
		}
		Vehicle unknown = new Vehicle(reg);
		return unknown;
	}
	/**
	 * Check whether the vehicle has already been parked somewhere inside the airport.
	 * @param v vehicle
	 * @return false if parked and true if hasn't parked inside yet
	 */
	public boolean parked(Vehicle v)
	{
		if (keepTrackVehicles.contains(v))
		{
			return true;
		}
		return false;
	}
	/**
	 * Used to keep track of which vehicles are inside the airport.
	 * @param v entering vehicle
	 */
	
	public void checkIn(Vehicle v)
	{
		keepTrackVehicles.add(v);
	}
	
	/**
	 * Used to keep track of which vehicles are inside the airport.
	 * @param v leaving vehicle
	 */
	public void checkOut(Vehicle v)
	{
		if(keepTrackVehicles.contains(v))
		{
			keepTrackVehicles.remove(v);
		}
	}
	
}
