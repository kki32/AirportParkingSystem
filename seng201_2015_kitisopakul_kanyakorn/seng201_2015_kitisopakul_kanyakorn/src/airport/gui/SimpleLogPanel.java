package airport.gui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A simple transcript window log for use with student example programs. Typical
 * use involves the log method which will append its argument to the text area.
 * This is suitable for simple text output or debugging statement output.
 *
 * Other methods allow the log contents to be saved to a text file and the
 * content of a text file to be added to the log. These methods would normally
 * be called in response to a button press or other stimulus but can safely be
 * used elsewhere.
 *
 * Use it like this:
 * 
 * <PRE>
 * SimpleLogPanel transcript = new SimpleLogPanel();
 * transcript.log(&quot;x = &quot; + x);
 * </PRE>
 * 
 * The log is not editable and can only be written to using the log method. Its
 * foreground and background colours can be changed but please use this
 * sparingly! Scroll bars will appear if necessary.
 */

@SuppressWarnings("serial")
public class SimpleLogPanel extends JPanel {
	protected JTextArea txt;
	protected JScrollPane jsp;

	private JFileChooser jfc = new JFileChooser();

	/**
	 * Constructor with default size.
	 */
	public SimpleLogPanel() {
		this(8, 25);
	}

	/**
	 * Constructor for class allowing size of text area to be specified.
	 * 
	 * @param rows
	 *            how many rows visible
	 * @param cols
	 *            how many columns visible
	 */
	public SimpleLogPanel(int rows, int cols) {
		txt = new JTextArea(rows, cols);
		txt.setEditable(false);
		jsp = new JScrollPane(txt);
		add(jsp);
	}

	/**
	 * Set the background colour of the log panel.
	 */
	public void setBgColour(Color c) {
		txt.setBackground(c);
	}

	/**
	 * Set the foreground colour of the log panel.
	 */
	public void setFgColour(Color c) {
		txt.setForeground(c);
	}

	/**
	 * Append message and move to new line.
	 */
	public void log(String message) {
		txt.append(message + "\n");
	}

	/**
	 * Append a new line to the log.
	 */
	public void newLine() {
		txt.append("\n");
	}

	/**
	 * Save the content of the log window to a text file whose name is provided.
	 * If any error occurs (eg. if the file name is not legal or the disc is
	 * full) then nothing will be written.
	 */
	public void saveToFile(String fName) {
		try {
			PrintWriter p = new PrintWriter(new FileWriter(fName));
			p.print(txt.getText());
			p.close();
		} catch (IOException e) {
			System.err.println("saveToFile: error saving to <" + fName + ">");
			System.err.println(e);
		}
	}

	/**
	 * Save the content of the log window to a text file whose name is to be
	 * selected via a file chooser dialog. If any error occurs (eg. if the file
	 * name is not legal or the disc is full) then nothing will be written.
	 */
	public void saveToFile() {
		String cwd = System.getProperty("user.dir");
		jfc.setCurrentDirectory(new File(cwd));

		int userChoice = jfc.showSaveDialog(null);
		File chosenFile = jfc.getSelectedFile();

		if ((chosenFile != null) && (userChoice == jfc.APPROVE_OPTION)) {
			// valid choice
			if (chosenFile.exists()) {
				// check if already exists first?
				System.err.println("File exists: " + chosenFile);
			} else {
				saveToFile(chosenFile.getName());
				jfc.rescanCurrentDirectory();
			}
		}
	}

	/**
	 * Insert the contents of a named text file into the log text area. If any
	 * error occurs (eg. if the file name is not legal) then nothing will be
	 * inserted.
	 */
	public void insertFile(String fName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fName));

			for (String line = ""; (line = br.readLine()) != null; txt
					.append(line + '\n')) {
				// nothing ...
			}
		} catch (IOException e) {
			System.err.println("insertFile: error loading <" + fName + ">");
			System.err.println(e);
		}
	}

	/**
	 * Insert the contents of a text file into the log text area. The file will
	 * be selected using a file chooser dialog. If any error occurs (eg. if the
	 * file name is not legal) then nothing will be inserted.
	 */
	public void insertFile() {
		String cwd = System.getProperty("user.dir");
		jfc.setCurrentDirectory(new File(cwd));

		int userChoice = jfc.showOpenDialog(null);
		File chosenFile = jfc.getSelectedFile();

		if ((chosenFile != null) && (userChoice == jfc.APPROVE_OPTION)) {
			// valid choice
			insertFile(chosenFile.getName());
		}
	}

}
