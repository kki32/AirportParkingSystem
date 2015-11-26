package airport;

/**
 * Class providing contact info for billing.
 * @author kki32
 */

public class BillableEntity 

{
	private java.lang.String who;
	private java.lang.String where;
	
	/**
	 * Constructor for class.
	 * @param who Name of person or business
	 * @param where Postal address
	 */
	public BillableEntity(java.lang.String who, java.lang.String where)
	{
		this.who = who;
		this.where = where;
	}
	
	/**
	 * Getter for who to send the bill to.
	 * @return Name of person or business
	 */
	public String getWho()
	{
		//make getter so that can print more details in charge() method in class Vehicle.
		return who;
	}
	
	/**
	 * Getter for where to send the bill to.
	 * @return Postal address
	 */
	
	public String getWhere()
	{
		//make getter so that can print more details in charge() method in class Vehicle.
		return where;
	}
	
}
