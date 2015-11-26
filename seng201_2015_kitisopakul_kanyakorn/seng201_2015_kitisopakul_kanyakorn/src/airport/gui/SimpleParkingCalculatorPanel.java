package airport.gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.freixas.jcalendar.*;

import airport.EconoParkingLot;
import airport.ExpressParkingLot;
import airport.LongStayParkingLot;
import airport.Parkable;
import airport.ParkingSystem;
import airport.ShortStayParkingLot;

@SuppressWarnings("serial")
public class SimpleParkingCalculatorPanel extends JPanel {

	/**
	 * Date listeners for calendars
	 */

	MyDateListener arriveListener = new MyDateListener();
	MyDateListener departListener = new MyDateListener();

	/**
	 * The choosers for arrival and departure info
	 */
	private JCalendarCombo arriveTime;
	private JCalendarCombo departTime;

	/**
	 * The duration we'll be charging for. Computed from arrive/depart times or
	 * just entered
	 */
	private JTextField jtf;

	/**
	 * Registration number to charge
	 */
	private JTextField jtr;
	/**
	 * The charge we computed goes here
	 */
	private JTextField jtc;

	// Endless messing around with dates
	private PeriodFormatter pfmt = PeriodFormat.getDefault();
	
	private static DateFormat dfmt = new SimpleDateFormat(
			"HH:mm EEE dd MMM, yyyy");

	
	private ParkingSystem company = new ParkingSystem();
	
	//
//	private ParkingSystem company = ParkingSystem.getParkingSystem();
	
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
	
	// Handy labels
	final JLabel location = new JLabel("Location:");
	final JLabel capacity = new JLabel("Capacity:");
	final JLabel occupied = new JLabel("Occupancy:");
	final JLabel available = new JLabel("Available:");
	
	/**
	 * Constructor for class.
	 */
	public SimpleParkingCalculatorPanel() {
		super();
		
		// Put in some sample parking lots --- replace these with your own as needed.
		
//		company.addLot(new ShortStayParkingLot("Short Stay Lot #1", 50));	
//		company.addLot(new ShortStayParkingLot("Short Stay Lot #2", 25));	
//		company.addLot(new ExpressParkingLot("Express Lot #1", 100));
//		company.addLot(new LongStayParkingLot("Long Stay Lot #1", 10));
		company.addLot(new EconoParkingLot("Eco Lot #1", 10));
		
		jcb = new JComboBox<Parkable>();
		for (Parkable p: company.lots()) {
			jcb.addItem(p);
		}
		
		add(buildCalculator());
	}

	
	/**
	 * Arrange the various bits and pieces.
	 */
	private JPanel buildCalculator() {
		JPanel calculatorPanel = new JPanel();

		JPanel jcbp = new JPanel();
		JLabel jcbl = new JLabel("Parking Type:");

		// Assemble the arrival time elements
		arriveTime = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE
				| JCalendarCombo.DISPLAY_TIME, true);

		arriveTime.setEditable(false);
		arriveTime.setDateFormat(dfmt);
		arriveTime.addDateListener(arriveListener);

		JPanel arr = new JPanel();
		JLabel jla = new JLabel("Arrival Time:");

		// Assemble the departure time elements
		departTime = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE
				| JCalendarCombo.DISPLAY_TIME, true);

		departTime.setEditable(false);
		departTime.setDateFormat(dfmt);
		departTime.addDateListener(departListener);

		JPanel dep = new JPanel();
		JLabel jld = new JLabel("Departure Time:");

		// Place for the interval
		JPanel jpi = new JPanel();
		JLabel jli = new JLabel("Duration:");
		jtf = new JTextField(64);
		jtf.setEditable(false);

		// Place for the charge
		JPanel jpc = new JPanel();
		JLabel jlc = new JLabel("Charge:");
		jtc = new JTextField(15);
		jtc.setEditable(false);

		// And whose reg it's for
		JPanel jpr = new JPanel();
		JLabel jlr = new JLabel("Reg. No:");
		jtr = new JTextField(8);

		// Assemble the whole mess
		calculatorPanel.setLayout(new BoxLayout(calculatorPanel,
				BoxLayout.Y_AXIS));

		((FlowLayout) jcbp.getLayout()).setAlignment(FlowLayout.RIGHT);
		jcbp.add(jcbl);
		jcbp.add(jcb);
		jcbp.setBorder(new EtchedBorder());
		calculatorPanel.add(jcbp);

		((FlowLayout) arr.getLayout()).setAlignment(FlowLayout.RIGHT);
		arr.add(jla);
		arr.add(arriveTime);
		arr.setBorder(new EtchedBorder());
		calculatorPanel.add(arr);

		((FlowLayout) dep.getLayout()).setAlignment(FlowLayout.RIGHT);
		dep.add(jld);
		dep.add(departTime);
		dep.setBorder(new EtchedBorder());
		calculatorPanel.add(dep);

		((FlowLayout) jpi.getLayout()).setAlignment(FlowLayout.RIGHT);
		jpi.add(jli);
		jpi.add(jtf);
		jpi.setBorder(new EtchedBorder());
		calculatorPanel.add(jpi);

		// Reg and charge on same row
		JPanel jpcr = new JPanel();
		((FlowLayout) jpcr.getLayout()).setAlignment(FlowLayout.RIGHT);

		((FlowLayout) jpi.getLayout()).setAlignment(FlowLayout.RIGHT);
		jpc.add(jlc);
		jpc.add(jtc);

		jpc.setBorder(new EtchedBorder());

		jpr.add(jlr);
		jpr.add(jtr);
		jpr.setBorder(new EtchedBorder());

		jpcr.add(jpr);
		jpcr.add(jpc);
		calculatorPanel.add(jpcr);

		JPanel buttons = new JPanel();
		buttons.add(admitButton);
		buttons.add(chargeButton);
		buttons.add(checkoutButton);
		calculatorPanel.add(buttons);

		chargeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCharge();
			}
		});
		
		admitButton.setEnabled(false);
		admitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// something appropriate
			}
		});
		
		checkoutButton.setEnabled(false);
		checkoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// something appropriate
			}
		});
		
		return calculatorPanel;
	}

	/**
	 * Make sure the departure time is after the arrival time etc
	 */
	private void updateDuration() {
		/* Date from = arriveTime.getDate();
		Date to = departTime.getDate();
		DateTime fdt = new DateTime(from);
		DateTime tdt = new DateTime(to);
		Period p = new Period(fdt, tdt);
		*/
		DateTime from = new DateTime(arriveTime.getDate());
		DateTime to = new DateTime(departTime.getDate());
		Period p = new Period(from, to);

		if (DateTimeComparator.getInstance().compare(from, to) >= 0) {
			JOptionPane.showMessageDialog(null,
					"Can't leave before you arrive!", "Negative time interval",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("Difference violates causality, ignoring : ");
		} else {
			// All's well
			jtf.setText(p.toString(pfmt));
		}
	}

	/**
	 * Make the charge field reflect the currently selected dates
	 */
	private void updateCharge() {
		jtc.setText(jcb.getSelectedItem() + " for " + jtf.getText());

		DateTime from = new DateTime(arriveTime.getDate());
		DateTime to = new DateTime(departTime.getDate());
		Period p = new Period(from, to);

		Parkable pk = (Parkable) jcb.getSelectedItem();
		pk.computeCharge(from, to);
		jtc.setText(pk.computeCharge(from, to).toString());
		System.out.println(jcb.getSelectedItem() + " for " + jtf.getText()
				+ " costs " + pk.computeCharge(from, to));
	}

	/**
	 * Internal class to respond to change events on the date chooser
	 * @author nic11
	 *
	 */
	class MyDateListener implements DateListener {
		String origin = "Unknown: ";

		public void dateChanged(DateEvent de) {
			Calendar c = de.getSelectedDate();

			if (c != null) {
				//System.out.println(c.getTime());
			} else {
				System.out.println("No time selected.");
			}

			updateDuration();
		}
	}
}