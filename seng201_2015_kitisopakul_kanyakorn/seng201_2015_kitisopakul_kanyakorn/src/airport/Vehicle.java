package airport;
/**
 * Represent vehicle
 * @author kki32
 *
 */
public class Vehicle
{
	private java.lang.String regNo;
	private BillableEntity owner;
	/**
	 * Constructor for class.
	 * @param regNo registration number (any combination of alphabetical letter(s) and/or number(s))
	 */
	public Vehicle(java.lang.String regNo)
	{
		if(regNo.matches("[a-zA-Z0-9]*"))
		{
			this.regNo = regNo;
			this.owner = new BillableEntity("Unknown", "Unknown");
		}
		else
		{
			throw new IllegalArgumentException("Registration number can only be a combination of letter(s) and digit(s)");
		}
	}
	
	/**
	 * 
	 * @param regNo registration number (any combination of alphabetical letter(s) and/or number(s))
	 * @param owner owner who owns the vehicle.
	 */
	public Vehicle(java.lang.String regNo, BillableEntity owner)
	{
		if(regNo.matches("[a-zA-Z0-9]*"))
		{
			this.regNo = regNo;
			this.owner = owner;
		}
		
		else
		{
			throw new IllegalArgumentException("Registration number can only be a combination of letter(s) and digit(s)");
		}
	}
	/**
	 * Provides info about how much it costs to park the vehicle and who will get charge for it.
	 * @param cost charge for parking
	 */
	public void charge(org.joda.money.Money cost)
	{
		if(owner.getWho().equals("Unknown")==false)
		{
			System.out.println("Dear " + owner.getWho() + ", the owner of the vehicle with registration number " + regNo
					+ ", the charge of " + cost + " has been sent to the given address, " + owner.getWhere() + ".");
		}
	}
	/**
	 * Report the registration number as it appears on the licence plate.
	 * @return registration number
	 */
	public java.lang.String regNo()
	{
		return regNo;
	}
	
	/**
	 * Provides info about who to send charges to etc.
	 * @param owner owner who has to pay charge
	 */
	public void setOwner(BillableEntity owner)
	{
		this.owner = owner;
	}
	
	@Override
	/**
	 * Print the registration number of the vehicle.
	 */
	public java.lang.String toString()
	{
			return "Reg No: " + regNo;
	}

}
