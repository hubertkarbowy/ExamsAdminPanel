package pl.hubertkarbowy.ExamsAdmin;

import pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.Delimiter;
import javax.swing.JPanel;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Dialog to choose the examiner
 * Queries the server for a list of examiners.
 *
 */
public class ChooseExaminer extends JPanel {
	private JComboBox<String> comboBox;
	private JTextPane txtpnNewgrpid;
	private JTextPane txtpnNewGrpName;
	private JLabel lblGroupIdunique;
	private JLabel lblGroupName;
	private JLabel lblExaminer;
	/**
	 * Contains information about examiners.
	 * Keys = examiner IDs
	 * Values = their names
	 */
	private Map<String, String> examiners;
	/**
	 * Instance of the class that creates new ChooseExaminer objects
	 */
	private UsersPanel superPanel;
	
	/**
	 * Creates the GUI
	 * @param panel instance of the class that creates new ChooseExaminer objects
	 * Used to automatically update / refresh tables in the calling class.
	 */
	public ChooseExaminer(UsersPanel panel) {
		setMinimumSize(new Dimension(500, 250));
		setPreferredSize(new Dimension(500, 127));
		superPanel=panel;
		
		examiners = StringUtilityMethods.getHash("user query *", "user get ", 1, 2, Delimiter.SEMICOLON, Delimiter.PIPE, 5, "examiner");
		superPanel.newGroupExaminer=examiners.entrySet().iterator().next().getKey(); // ugly, ugly, ugly...
		System.out.println(examiners.toString());
		comboBox = new JComboBox<String>();
		comboBox.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				String retid;
				for (Entry<String,String> tuple : examiners.entrySet()) {
					if (tuple.getValue().equals(comboBox.getSelectedItem())) {
						superPanel.newGroupExaminer = tuple.getKey();
						break;
					}
				}
			}
		});
		examiners.values().forEach(x -> comboBox.addItem(x));
		setLayout(null);
		comboBox.setBounds(146, 76, 324, 24);
		add(comboBox);
		
		txtpnNewgrpid = new JTextPane();
		txtpnNewgrpid.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				superPanel.newGroupId = txtpnNewgrpid.getText();
			}
		});
		txtpnNewgrpid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});
		txtpnNewgrpid.setBounds(146, 10, 324, 21);
		add(txtpnNewgrpid);
		
		txtpnNewGrpName = new JTextPane();
		txtpnNewGrpName.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				superPanel.newGroupName = txtpnNewGrpName.getText();
			}
		});
		txtpnNewGrpName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});
		txtpnNewGrpName.setBounds(146, 43, 324, 21);
		add(txtpnNewGrpName);
		
		lblGroupIdunique = new JLabel("Group ID (unique):");
		lblGroupIdunique.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGroupIdunique.setBounds(12, 16, 129, 15);
		add(lblGroupIdunique);
		
		lblGroupName = new JLabel("Group name:");
		lblGroupName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGroupName.setBounds(50, 43, 91, 15);
		add(lblGroupName);
		
		lblExaminer = new JLabel("Examiner:");
		lblExaminer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExaminer.setBounds(72, 81, 69, 15);
		add(lblExaminer);
		this.setVisible(true);
		
	}
}
