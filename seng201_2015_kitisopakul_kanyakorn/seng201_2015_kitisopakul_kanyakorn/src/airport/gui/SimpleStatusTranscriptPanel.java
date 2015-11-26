package airport.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.joda.money.Money;
import org.joda.time.DateTime;

import airport.BillableEntity;
import airport.EconoParkingLot;
import airport.ExpressParkingLot;
import airport.Parkable;
import airport.ParkingSystem;
import airport.ShortStayParkingLot;
import airport.Vehicle;
import airport.LongStayParkingLot;

/**
 * A SimpleStatusPanel that can also show details of vehicles here.
 * @author nic11
 *
 */
public class SimpleStatusTranscriptPanel extends JPanel {
	/**
	 * Registration number to charge
	 */
	private JTextField jtr;

	private ParkingSystem company = new ParkingSystem();
	
	/**
	 * Will need to access this elsewhere
	 */
	private JComboBox<Parkable> jcb;

	/**
	 * Big red buttons to process transaction details
	 */
	private JButton chargeButton = new JButton("Charge");
	private JButton admitButton = new JButton("Admit");
	private JButton checkoutButton = new JButton("Checkout");
	private JButton reportButton = new JButton("Report");
	
	// Some labels
	private final JLabel location = new JLabel("Location:");
	private final JLabel capacity = new JLabel("Capacity:");
	private final JLabel occupied = new JLabel("Occupancy:");
	private final JLabel available = new JLabel("Available:");
	private final JLabel jlr = new JLabel("Reg. No:");
	
	//added properties-----------------------------------------------------------------
	private DateTime arriveTime;
	private DateTime departTime;
	//---------------------------------------------------------------------------------
	
	/**
	 * Constructor for class.
	 */
	public SimpleStatusTranscriptPanel() {
		super();
		company.addLot(new ShortStayParkingLot("Short Stay Lot #1", 750));
		company.addLot(new ShortStayParkingLot("Short Stay Lot #2", 500));
		company.addLot(new ExpressParkingLot("Express Lot #1", 1000));
		company.addLot(new LongStayParkingLot("Long Stay Lot #1", 600));
		company.addLot(new EconoParkingLot("Eco Lot #1", 1200));

		jcb = new JComboBox<Parkable>();
		for (Parkable p : company.lots()) {
			jcb.addItem(p);
		}
		
		add(buildOccupancyStatus());
	}

	private JPanel buildOccupancyStatus() {

		JPanel statusPanel = new JPanel();
		JPanel statusInfoPanel = new JPanel(new GridLayout(4, 1));
		statusInfoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		// And whose reg it's for
		JPanel jpr = new JPanel();
		jpr.setBackground(Color.RED);
		jtr = new JTextField(8);
		jpr.add(jlr);
		jpr.add(jtr);
		
		jcb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDisplayInfo();
			}
		});

		JPanel jcbp = new JPanel();
		JLabel jcbl = new JLabel("Parking Type:");
		((FlowLayout) jcbp.getLayout()).setAlignment(FlowLayout.RIGHT);
		jcbp.add(jcbl);
		jcbp.add(jcb);
		jcbp.setBorder(new EtchedBorder());
		
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
		statusPanel.add(jcbp);
		
		statusInfoPanel.add(location);
		statusInfoPanel.add(capacity);
		statusInfoPanel.add(occupied);
		statusInfoPanel.add(available);

		statusPanel.add(statusInfoPanel);
		
		statusPanel.add(jpr);
		
		JPanel buttons = new JPanel();
		buttons.add(admitButton);
		buttons.add(chargeButton);
		buttons.add(checkoutButton);
		buttons.add(reportButton);
		statusPanel.add(buttons);

		final SimpleLogPanel transcript = new SimpleLogPanel();
		transcript.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		add(transcript);
		
		chargeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		//		updateCharge();
			}
		});
		chargeButton.setEnabled(false);
		
		admitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(jtr.getText().equals("")) {
					// reg field is empty
					JOptionPane.showMessageDialog(null, "Please enter the registration number", "Missing Reg",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				//modified part--------------------------------------------------------------------
				if(company.parked(company.vehicleFor(jtr.getText())))
				{
					JOptionPane.showMessageDialog(null, "Already parked", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}
				
				else
				{
					String ownerName = JOptionPane.showInputDialog(null, "Owner", "BillableEntity",
					JOptionPane.QUESTION_MESSAGE);
					
					while (ownerName != null && ownerName.equals(""))
					{
						ownerName = JOptionPane.showInputDialog(null, "Owner", "BillableEntity",
						JOptionPane.QUESTION_MESSAGE);
					}
					
					if (ownerName != null)
					{
						String ownerAddress = JOptionPane.showInputDialog(null, "Address", "BillableEntity",
						JOptionPane.QUESTION_MESSAGE);
					
						while(ownerAddress!= null && ownerAddress.equals(""))
						{
							ownerAddress = JOptionPane.showInputDialog(null, "Address", "BillableEntity",
							JOptionPane.QUESTION_MESSAGE);
						}
					
						if (ownerAddress != null)
						{
							arriveTime = DateTime.now();
							company.checkIn(company.vehicleFor(jtr.getText()));
							//original part
							Parkable pk = (Parkable) jcb.getSelectedItem();
							pk.admit(company.vehicleFor(jtr.getText()));		
							
							company.vehicleFor(jtr.getText()).setOwner(new BillableEntity(ownerName, ownerAddress));
						}
					}		
				}
				//---------------------------------------------------------------------------------
				updateDisplayInfo();
			}
		});
		
		checkoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				//modified part--------------------------------------------------------------------
				Parkable pk = (Parkable) jcb.getSelectedItem();
				if(pk.occupants().contains(company.vehicleFor(jtr.getText())))
				{
					departTime = DateTime.now();
					company.vehicleFor(jtr.getText()).charge(pk.computeCharge(arriveTime, departTime));
					pk.release(company.vehicleFor(jtr.getText()));
					company.checkOut(company.vehicleFor(jtr.getText()));
				}
				//---------------------------------------------------------------------------------
				updateDisplayInfo();
			}
		});
		
		reportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Parkable pk = (Parkable) jcb.getSelectedItem();
				transcript.log(pk.toString() + "contains:");
				for(Vehicle v: pk.occupants()){
					transcript.log(v.toString());
				}
				updateDisplayInfo();
			}
		});
		

		return statusPanel;

	}
	
	/**
	 * Refresh the display fields
	 */
	private void updateDisplayInfo() {
		Parkable pk = (Parkable) jcb.getSelectedItem();
		location.setText("Location: " + pk.toString());
		capacity.setText("Capacity: " + pk.capacity());
		occupied.setText("Occupied: " + pk.occupancy());
		available.setText("Available: "
				+ (pk.capacity() - pk.occupancy()));
	}
}
