package pl.hubertkarbowy.ExamsAdmin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import javax.swing.table.DefaultTableModel;

import com.sun.org.apache.xpath.internal.functions.Function;

import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.*;
import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;

public class UsersPanel extends JDialog {

	private final JPanel usersTablePanel = new JPanel();
	private JTable usersTable;
	private BetterTableModel model_ulist;
	private JTextField txtUid;
	private JTextField txtFirstName;
	private JTextField txtFamilyName;
	private JTextField txtDOB;
	private JLabel lblHiddenpw;
	private JComboBox comboRole;
	private JButton btnReset;
	private JLabel lblAddingANew;
	private JButton applyButton;
	private JButton btnNewUser;
	private JButton btnRemoveuser;
	private JButton cancelButton;
	
	
	//List<JComponent> clist = new ArrayList<>();

	private boolean isInNewUserMode = false;
	/**
	 * Launch the application.
	 */
	
	/**
	 * Create the dialog.
	 */
	public UsersPanel() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				prevWindowQueue.poll().setVisible(true);
			}
		});
		
		String serverResponse;
		List<String> tokens;
		List<String> values;
		List<List<String>> listModel = new ArrayList<>();
		serverResponse = sendAndReceive("user query *");
		if (!serverResponse.startsWith("OK")) throw new ExamsException("Cannot retrieve users list!");
		tokens = tokenize(serverResponse, Delimiter.SEMICOLON);
		for (String userid : tokens) {
			serverResponse=sendAndReceive("user get " + userid);
			if (!serverResponse.startsWith("OK")) throw new ExamsException("Cannot retrieve user data!");
			values = tokenize(serverResponse, Delimiter.PIPE);
			listModel.add(values);
		}
	//	clist.add(txtUid); clist.add(txtFirstName); clist.add(txtFamilyName); clist.add(txtDOB); clist.add(lblHiddenpw);
		
		
		
		setBounds(100, 100, 722, 605);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		usersTablePanel.setPreferredSize(new Dimension(10, 300));
		usersTablePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(usersTablePanel);
		usersTablePanel.setLayout(new BoxLayout(usersTablePanel, BoxLayout.Y_AXIS));
		
		model_ulist = (BetterTableModel) createTableModelv3(new String[] {"User ID", "First name", "Family name", "DOB", "Pwd", "Role"}, listModel);
		usersTable = new JTable(model_ulist);
		usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		usersTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refreshInputFields();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(usersTable);
		usersTable.removeColumn(usersTable.getColumn("Pwd"));
		usersTablePanel.add(scrollPane);
		for (int row=0; row<usersTable.getRowCount(); row++) {
			usersTable.setValueAt(toDOB((String)model_ulist.getValueAt(row, 3)), row, 3);
		}
		
			JPanel editPanel = new JPanel();
			editPanel.setMaximumSize(new Dimension(32767, 350));
			getContentPane().add(editPanel);
			GridBagLayout gbl_editPanel = new GridBagLayout();
			gbl_editPanel.columnWidths = new int[]{10,100};
			gbl_editPanel.rowHeights = new int[]{30,30,30,30,30,30,30};
			gbl_editPanel.columnWeights = new double[]{0.1,1.0};
			gbl_editPanel.rowWeights = new double[]{0.16,0.16,0.16,0.16,0.16,0.16,0.03};
			editPanel.setLayout(gbl_editPanel);
			
			JLabel lblUserId = new JLabel("User id:");
			GridBagConstraints gbc_lblUserId = new GridBagConstraints();
			gbc_lblUserId.anchor = GridBagConstraints.EAST;
			gbc_lblUserId.insets = new Insets(0, 0, 5, 5);
			gbc_lblUserId.gridx = 0;
			gbc_lblUserId.gridy = 0;
			editPanel.add(lblUserId, gbc_lblUserId);
			
			txtUid = new JTextField();
			txtUid.setEditable(false);
			GridBagConstraints gbc_txtUid = new GridBagConstraints();
			gbc_txtUid.insets = new Insets(0, 0, 5, 0);
			gbc_txtUid.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtUid.gridx = 1;
			gbc_txtUid.gridy = 0;
			editPanel.add(txtUid, gbc_txtUid);
			txtUid.setColumns(10);
			
			JLabel lblFirstName = new JLabel("First name:");
			lblFirstName.setHorizontalAlignment(SwingConstants.RIGHT);
			GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
			gbc_lblFirstName.anchor = GridBagConstraints.EAST;
			gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
			gbc_lblFirstName.gridx = 0;
			gbc_lblFirstName.gridy = 1;
			editPanel.add(lblFirstName, gbc_lblFirstName);
			
			txtFirstName = new JTextField();
			GridBagConstraints gbc_txtFirstName = new GridBagConstraints();
			gbc_txtFirstName.insets = new Insets(0, 0, 5, 0);
			gbc_txtFirstName.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtFirstName.gridx = 1;
			gbc_txtFirstName.gridy = 1;
			editPanel.add(txtFirstName, gbc_txtFirstName);
			txtFirstName.setColumns(10);
			
			JLabel lblFamilyName = new JLabel("Family name:");
			GridBagConstraints gbc_lblFamilyName = new GridBagConstraints();
			gbc_lblFamilyName.anchor = GridBagConstraints.EAST;
			gbc_lblFamilyName.insets = new Insets(0, 0, 5, 5);
			gbc_lblFamilyName.gridx = 0;
			gbc_lblFamilyName.gridy = 2;
			editPanel.add(lblFamilyName, gbc_lblFamilyName);
			
			txtFamilyName = new JTextField();
			GridBagConstraints gbc_txtFamilyName = new GridBagConstraints();
			gbc_txtFamilyName.insets = new Insets(0, 0, 5, 0);
			gbc_txtFamilyName.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtFamilyName.gridx = 1;
			gbc_txtFamilyName.gridy = 2;
			editPanel.add(txtFamilyName, gbc_txtFamilyName);
			txtFamilyName.setColumns(10);
			
			JLabel lblDob = new JLabel("DOB:");
			GridBagConstraints gbc_lblDob = new GridBagConstraints();
			gbc_lblDob.anchor = GridBagConstraints.EAST;
			gbc_lblDob.insets = new Insets(0, 0, 5, 5);
			gbc_lblDob.gridx = 0;
			gbc_lblDob.gridy = 3;
			editPanel.add(lblDob, gbc_lblDob);
			
			txtDOB = new JTextField();
			GridBagConstraints gbc_txtDOB = new GridBagConstraints();
			gbc_txtDOB.insets = new Insets(0, 0, 5, 0);
			gbc_txtDOB.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtDOB.gridx = 1;
			gbc_txtDOB.gridy = 3;
			editPanel.add(txtDOB, gbc_txtDOB);
			txtDOB.setColumns(10);
			
			JLabel lblPassword = new JLabel("Password:");
			GridBagConstraints gbc_lblPassword = new GridBagConstraints();
			gbc_lblPassword.anchor = GridBagConstraints.EAST;
			gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
			gbc_lblPassword.gridx = 0;
			gbc_lblPassword.gridy = 4;
			editPanel.add(lblPassword, gbc_lblPassword);
			
			btnReset = new JButton("Reset");
			GridBagConstraints gbc_btnReset = new GridBagConstraints();
			gbc_btnReset.anchor = GridBagConstraints.WEST;
			gbc_btnReset.insets = new Insets(0, 0, 5, 0);
			gbc_btnReset.gridx = 1;
			gbc_btnReset.gridy = 4;
			editPanel.add(btnReset, gbc_btnReset);
			
			JLabel lblRole = new JLabel("Role:");
			GridBagConstraints gbc_lblRole = new GridBagConstraints();
			gbc_lblRole.anchor = GridBagConstraints.EAST;
			gbc_lblRole.insets = new Insets(0, 0, 5, 5);
			gbc_lblRole.gridx = 0;
			gbc_lblRole.gridy = 5;
			editPanel.add(lblRole, gbc_lblRole);
			
			comboRole = new JComboBox();
			comboRole.setModel(new DefaultComboBoxModel(new String[] {"student", "examiner", "admin"}));
			GridBagConstraints gbc_comboRole = new GridBagConstraints();
			gbc_comboRole.insets = new Insets(0, 0, 5, 0);
			gbc_comboRole.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboRole.gridx = 1;
			gbc_comboRole.gridy = 5;
			editPanel.add(comboRole, gbc_comboRole);
			
			lblAddingANew = new JLabel("Adding a new user. Click 'Apply' to add an account or 'Abort' to discard changes.");
			lblAddingANew.setVisible(false);
			lblAddingANew.setForeground(Color.RED);
			GridBagConstraints gbc_lblAddingANew = new GridBagConstraints();
			gbc_lblAddingANew.gridx = 1;
			gbc_lblAddingANew.gridy = 6;
			editPanel.add(lblAddingANew, gbc_lblAddingANew);
		
			JPanel buttonPane = new JPanel();
			buttonPane.setMaximumSize(new Dimension(32767, 100));
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane);
				applyButton = new JButton("Apply changes");
				applyButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						updateUser();
					}
				});
				applyButton.setMnemonic('a');
				applyButton.setActionCommand("OK");
				buttonPane.add(applyButton);
				getRootPane().setDefaultButton(applyButton);
				cancelButton = new JButton("Exit");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				
				btnNewUser = new JButton("New user");
				btnNewUser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						newUser();
					}
				});
				btnNewUser.setMnemonic('n');
				buttonPane.add(btnNewUser);
				
				btnRemoveuser = new JButton("Remove User");
				btnRemoveuser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					int opt=0;
					if (usersTable.getSelectedRowCount() != 1) throw new ExamsException("Please select a user to remove");
					opt = JOptionPane.showConfirmDialog(null, "Do you want to remove this user?", "User operation", JOptionPane.YES_NO_OPTION);
					if (opt==0) removeUser(usersTable.getSelectedRow());
					}
				});
				btnRemoveuser.setMnemonic('r');
				buttonPane.add(btnRemoveuser);
				cancelButton.setMnemonic('x');
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				
				lblHiddenpw = new JLabel("hiddenPW");
				lblHiddenpw.setVisible(false);
				buttonPane.add(lblHiddenpw);
			
		
	}
	
	void refreshInputFields() {
	//	long unixtimestamp = Long.parseLong((String)((BetterTableModel)model_ulist).getValueAt(usersTable.getSelectedRow(), 3));
	//	LocalDateTime ldt = LocalDateTime.ofEpochSecond(unixtimestamp, 0, ZoneOffset.UTC);
		
		txtUid.setText((String)model_ulist.getValueAt(usersTable.getSelectedRow(), 0));
		txtFirstName.setText((String)model_ulist.getValueAt(usersTable.getSelectedRow(), 1));
		txtFamilyName.setText((String)model_ulist.getValueAt(usersTable.getSelectedRow(), 2));
		txtDOB.setText((String)usersTable.getValueAt(usersTable.getSelectedRow(), 3));
		lblHiddenpw.setText((String)model_ulist.getValueAt(usersTable.getSelectedRow(), 4));
		comboRole.setSelectedItem((String)model_ulist.getValueAt(usersTable.getSelectedRow(), 5));
	}
	
	void newUser() {
		
		if (!isInNewUserMode) {
			JPasswordField pf = new JPasswordField();
			pf.setText("zaq12WSX");
			int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter password for the new user (default is zaq12WSX):", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	
			if (okCxl == JOptionPane.OK_OPTION) {
			  String password = new String(pf.getPassword());
			  if (password.isEmpty()) throw new ExamsException("Password cannot be empty.");
			  txtUid.setEditable(true);
			  lblHiddenpw.setText(password);
			  // System.err.println("You entered: " + password);
			  usersTable.setEnabled(false);
			  lblAddingANew.setVisible(true);
			  btnReset.setEnabled(false);
			  btnNewUser.setText("Abort adding a new user");
			  Arrays.asList(txtUid, txtFirstName, txtFamilyName, txtDOB).stream().forEach(x -> x.setText(""));
			  isInNewUserMode=true;
			}
		}
		else
		{
			txtUid.setEditable(false);
			usersTable.setEnabled(true);
			lblAddingANew.setVisible(false);
			btnNewUser.setText("New user");
			btnReset.setEnabled(true);
			Arrays.asList(txtUid, txtFirstName, txtFamilyName, txtDOB).stream().forEach(x -> x.setText(""));
			isInNewUserMode=false;
		}
	}
	
	void updateUser() {
		
		String serverResponse = "";
		LocalDate user_dob = null;
		try {
			Integer.parseInt(txtUid.getText());
			user_dob = LocalDate.parse(txtDOB.getText(), DateTimeFormatter.ofPattern("d-M-y"));
		}
		catch (DateTimeParseException dtpe) {
			throw new ExamsException("Enter a valid date of birth (dd-MM-YYYY)");
		}
		catch (NumberFormatException nfe) {
			throw new ExamsException("User ID must be an integer and cannot be empty. Suggested ranges: \n1-100: admins\n101-1000: examiners\n1000+: students");
		}
		
		if (txtUid.getText().isEmpty()) throw new ExamsException("User ID must be an integer and cannot be empty. Suggested ranges: \n1-100: admins\n101-1000: examiners\n1000+: students");
		if (txtFirstName.getText().isEmpty()) throw new ExamsException("First name cannot be empty.");
		if (txtFamilyName.getText().isEmpty()) throw new ExamsException("Family name cannot be empty.");
		
		StringBuilder sb = new StringBuilder();
		List<Object> clist = Arrays.asList(txtUid, txtFirstName, txtFamilyName, user_dob, lblHiddenpw, comboRole);
		List<String> lstr = clist.stream().map(x ->  {
			if (x instanceof JTextField) return ((JTextField)x).getText();
			else if (x instanceof JLabel) return ((JLabel)x).getText();
			else if (x instanceof JComboBox) return (String)((JComboBox)x).getSelectedItem();
			else if (x instanceof LocalDate) return ("" + ((LocalDate)x).atStartOfDay().toEpochSecond(ZoneOffset.UTC));
			else throw new ExamsException("Unknown component!");
		}).collect(Collectors.toList());
		
		if (isInNewUserMode==false) {
			
			serverResponse = sendAndReceive("user modify " + txtUid.getText() + " {" + String.join("|", lstr) + "}");
			if (!serverResponse.startsWith("OK")) throw new ExamsException("Cannot modify user in database.");
			System.out.println("user modify " + txtUid.getText() + " {" + String.join("|", lstr) + "}");
			showMsg("Successfully modified user entry. @TODO: Autorefresh.");
			
			
		}
		else {
			String newid="";
			sb.append("user new {" + String.join("|", lstr) + "}");
			System.out.println(sb.toString());
			serverResponse = sendAndReceive(sb.toString());
			if (!serverResponse.startsWith("OK")) throw new ExamsException("Cannot add user to database.");
			showMsg("Added new user");
			newid=serverResponse.substring(3);
			// lstr.remove(4); // usuwam haslo
			model_ulist.addRow2(lstr);
			
			txtUid.setEditable(false);
			usersTable.setEnabled(true);
			lblAddingANew.setVisible(false);
			btnNewUser.setText("New user");
			btnReset.setEnabled(true);
			Arrays.asList(txtUid, txtFirstName, txtFamilyName, txtDOB).stream().forEach(x -> x.setText(""));
			isInNewUserMode=false;
		}
	}
	
	void removeUser(int rowid) {
		
		
		String serverResponse = sendAndReceive("user remove " + txtUid.getText());
		if (!serverResponse.startsWith("OK")) throw new ExamsException("Cannot remove user.");
		else showMsg("User removed");
		model_ulist.removeRow(rowid);
		
		
		 
		
	
	}
	
	String toDOB(String that) {
			long unixtimestamp = Long.parseLong(that);
			LocalDateTime ldt = LocalDateTime.ofEpochSecond(unixtimestamp, 0, ZoneOffset.UTC);						
			return ldt.format(DateTimeFormatter.ofPattern("d-M-y"));
	}
	

}
