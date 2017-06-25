package pl.hubertkarbowy.ExamsAdmin;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.Delimiter;

import javax.swing.JButton;

public class ChooseExaminer extends JPanel {
	private JComboBox<String> comboBox;
	private JTextPane txtpnNewgrpid;
	private JTextPane txtpnNewGrpName;
	private JLabel lblGroupIdunique;
	private JLabel lblGroupName;
	private JLabel lblExaminer;
	private JButton btnAdd;
	private Map<String, String> examiners;
	
	
	public ChooseExaminer() {
		setLayout(null);
		
		examiners = StringUtilityMethods.getHash("user query *", "user get ", 1, 2, Delimiter.SEMICOLON, Delimiter.PIPE, 5, "examiner");
		System.out.println(examiners.toString());
		comboBox = new JComboBox<String>();
		examiners.values().forEach(x -> comboBox.addItem(x));
		comboBox.setBounds(183, 157, 352, 24);
		add(comboBox);
		
		txtpnNewgrpid = new JTextPane();
		txtpnNewgrpid.setBounds(183, 91, 352, 21);
		add(txtpnNewgrpid);
		
		txtpnNewGrpName = new JTextPane();
		txtpnNewGrpName.setBounds(183, 124, 352, 21);
		add(txtpnNewGrpName);
		
		lblGroupIdunique = new JLabel("Group ID (unique):");
		lblGroupIdunique.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGroupIdunique.setBounds(27, 91, 148, 15);
		add(lblGroupIdunique);
		
		lblGroupName = new JLabel("Group name:");
		lblGroupName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGroupName.setBounds(53, 124, 122, 15);
		add(lblGroupName);
		
		lblExaminer = new JLabel("Examiner:");
		lblExaminer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExaminer.setBounds(85, 157, 90, 15);
		add(lblExaminer);
		
		btnAdd = new JButton("Add");
		btnAdd.setMnemonic('a');
		btnAdd.setBounds(218, 193, 117, 25);
		add(btnAdd);
		this.setVisible(true);
		
	}
}
