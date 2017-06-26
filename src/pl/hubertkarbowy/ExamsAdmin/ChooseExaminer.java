package pl.hubertkarbowy.ExamsAdmin;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.Delimiter;

import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class ChooseExaminer extends JPanel {
	private JComboBox<String> comboBox;
	private JTextPane txtpnNewgrpid;
	private JTextPane txtpnNewGrpName;
	private JLabel lblGroupIdunique;
	private JLabel lblGroupName;
	private JLabel lblExaminer;
	private Map<String, String> examiners;
	private UsersPanel superPanel;
	
	public ChooseExaminer(UsersPanel panel) {
		//setModal(true);
		//setModalityType(ModalityType.APPLICATION_MODAL);
		//setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setMinimumSize(new Dimension(500, 250));
		setPreferredSize(new Dimension(500, 127));
		//getContentPane().setLayout(null);
		superPanel=panel;
		
		examiners = StringUtilityMethods.getHash("user query *", "user get ", 1, 2, Delimiter.SEMICOLON, Delimiter.PIPE, 5, "examiner");
		superPanel.newGroupExaminer=examiners.entrySet().iterator().next().getKey(); // ugly, ugly, ugly...
		System.out.println(examiners.toString());
		comboBox = new JComboBox<String>();
		comboBox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				String retid;
				for (Entry<String,String> tuple : examiners.entrySet()) {
					if (tuple.getValue().equals((String)comboBox.getSelectedItem())) {
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
