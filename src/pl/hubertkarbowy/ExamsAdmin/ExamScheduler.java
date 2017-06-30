package pl.hubertkarbowy.ExamsAdmin;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.*;
import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.*;

import java.awt.FlowLayout;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.Stream;

import javax.swing.*;

/**
 * Handles exam schedules
 *
 */
public class ExamScheduler extends JDialog {
	private JLabel lblGroup;
	private JComboBox<String> grpBox;
	private JLabel lblExamCode;
	private JComboBox<String> excodeBox;
	private JTextField txtStartdate;
	private JTextField txtStarttime;
	private JTextField txtEndtime;
	private JTextField txtEnddate;
	private JLabel lblStartDt;
	private JLabel lblTime;
	private JLabel label;
	private JLabel lblEndDate;
	private JLabel lblType;
	private JTextField txtType;
	private JLabel lblTimeLimit;
	private JTextField txtTimeLimit;
	private JLabel lblExamName;
	private JLabel lblExName;
	
	private List<List<String>> scheduledList = new ArrayList<>();
	private JTable scheduledTable;
	private BetterTableModel model_schedulelist;
	List<String> groupsList = new ArrayList<>();
	List<String> examsList = new ArrayList<>();
	private LocalDateTime ldt;
	private JButton btnNewexam;
	/**
	 * Controls scheduling type
	 * If set to true, the command <i>exam schedule</i> is submitted to the server.
	 * Otherwise the command <i>exam reschedule</i> is executed.
	 */
	private boolean isInNewScheduleMode=false;
	private JButton saveButton;
	private JButton btnUnschedule;
	
	/**
	 * Allows the examiner to schedule an exam.
	 */
	public ExamScheduler() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				prevWindowQueue.poll().setVisible(true);
			}
		});
		
		String servResp = sendAndReceive("group query *");
		if (!servResp.startsWith("OK")) throw new ExamsException("You aren't currently teaching any courses. Ask the administrator to create groups for you.");
		groupsList = tokenize(servResp, Delimiter.SEMICOLON);
		
		servResp = sendAndReceive("exam query *");
		if (!servResp.startsWith("OK")) throw new ExamsException("You haven't created any exams yet. Click \"Create or edit exams\" in the main panel");
		examsList = tokenize(servResp, Delimiter.SEMICOLON);
		
		scheduledList=sendReceiveAndDeserialize("exam getexaminerscheduled");
		model_schedulelist = (BetterTableModel) createTableModelv3(new String[] {"GrID", "Exam code", "Start", "End", "Type", "Time", "Course", "Exam name"}, scheduledList);
		scheduledTable = new JTable(model_schedulelist);
		scheduledTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refreshInputFields();
			}
		});
		scheduledTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scheduledTable.getColumnModel().getColumn(4).setPreferredWidth(30);
		scheduledTable.getColumnModel().getColumn(5).setPreferredWidth(30);
		
		for (int row=0; row<model_schedulelist.getRowCount(); row++) {
			ldt = LocalDateTime.ofEpochSecond(Long.parseLong((String)model_schedulelist.getValueAt(row, 2)), 0, ZoneOffset.UTC);
			scheduledTable.setValueAt(ldt.format(DateTimeFormatter.ofPattern("dd-MM-y hh:mm")), row, 2);
			
			ldt = LocalDateTime.ofEpochSecond(Long.parseLong((String)model_schedulelist.getValueAt(row, 3)), 0, ZoneOffset.UTC);
			scheduledTable.setValueAt(ldt.format(DateTimeFormatter.ofPattern("dd-MM-y hh:mm")), row, 3);

		}
		
		setBounds(100, 100, 827, 512);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane(scheduledTable);
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refreshInputFields();
			}
		});
		scrollPane.setBounds(0, 0, 813, 177);
		getContentPane().add(scrollPane);
		scrollPane.setAlignmentY(0.0f);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 176, 813, 215);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		lblGroup = new JLabel("Group:");
		lblGroup.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGroup.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblGroup.setBounds(12, 12, 95, 15);
		panel.add(lblGroup);
		
		grpBox = new JComboBox();
		grpBox.setEnabled(false);
		grpBox.setBounds(121, 7, 680, 24);
		panel.add(grpBox);
		
		lblExamCode = new JLabel("Exam code:");
		lblExamCode.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblExamCode.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExamCode.setBounds(12, 44, 95, 15);
		panel.add(lblExamCode);
		
		excodeBox = new JComboBox();
		excodeBox.setBounds(121, 39, 680, 24);
		panel.add(excodeBox);
		
		txtStartdate = new JTextField();
		txtStartdate.setBounds(121, 102, 114, 19);
		panel.add(txtStartdate);
		txtStartdate.setColumns(10);
		
		txtStarttime = new JTextField();
		txtStarttime.setColumns(10);
		txtStarttime.setBounds(339, 102, 114, 19);
		panel.add(txtStarttime);
		
		txtEndtime = new JTextField();
		txtEndtime.setColumns(10);
		txtEndtime.setBounds(339, 129, 114, 19);
		panel.add(txtEndtime);
		
		txtEnddate = new JTextField();
		txtEnddate.setColumns(10);
		txtEnddate.setBounds(121, 129, 114, 19);
		panel.add(txtEnddate);
		
		lblStartDt = new JLabel("Start date:");
		lblStartDt.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblStartDt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStartDt.setBounds(12, 104, 95, 15);
		panel.add(lblStartDt);
		
		lblTime = new JLabel("Time:");
		lblTime.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTime.setBounds(237, 102, 95, 15);
		panel.add(lblTime);
		
		label = new JLabel("Time:");
		label.setHorizontalTextPosition(SwingConstants.RIGHT);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(237, 129, 95, 15);
		panel.add(label);
		
		lblEndDate = new JLabel("End date:");
		lblEndDate.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblEndDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEndDate.setBounds(12, 131, 95, 15);
		panel.add(lblEndDate);
		
		lblType = new JLabel("Type:");
		lblType.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblType.setBounds(12, 158, 95, 15);
		panel.add(lblType);
		
		txtType = new JTextField();
		txtType.setColumns(10);
		txtType.setBounds(121, 156, 680, 19);
		panel.add(txtType);
		
		lblTimeLimit = new JLabel("Time limit:");
		lblTimeLimit.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblTimeLimit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTimeLimit.setBounds(12, 186, 95, 15);
		panel.add(lblTimeLimit);
		
		txtTimeLimit = new JTextField();
		txtTimeLimit.setColumns(10);
		txtTimeLimit.setBounds(121, 184, 680, 19);
		panel.add(txtTimeLimit);
		
		lblExamName = new JLabel("Exam name:");
		lblExamName.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblExamName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExamName.setBounds(12, 71, 95, 15);
		panel.add(lblExamName);
		
		lblExName = new JLabel("---");
		lblExName.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblExName.setHorizontalAlignment(SwingConstants.LEFT);
		lblExName.setBounds(131, 71, 575, 15);
		panel.add(lblExName);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 426, 813, 45);
		getContentPane().add(buttonPane);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		btnNewexam = new JButton("New exam");
		btnNewexam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isInNewScheduleMode) {
					scheduledTable.setEnabled(false);
					excodeBox.setSelectedIndex(0);
					grpBox.setSelectedIndex(0);
					Stream.of(txtStarttime, txtEndtime, txtStartdate, txtEnddate, txtType, txtTimeLimit).forEach(x -> ((JTextField)x).setText(""));
					isInNewScheduleMode=true;
					btnNewexam.setText("Abort changes");
					btnNewexam.setMnemonic('a');
					grpBox.setEnabled(true);
					btnUnschedule.setEnabled(false);
				}
				else {
					scheduledTable.setEnabled(true);
					isInNewScheduleMode=false;
					btnNewexam.setText("New exam");
					btnNewexam.setMnemonic('n');
					btnUnschedule.setEnabled(true);
					grpBox.setEnabled(false);
					refreshInputFields();
				}
				
			}
		});
		
		btnUnschedule = new JButton("Unschedule");
		btnUnschedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unschedule();
			}
		});
		btnUnschedule.setMnemonic('u');
		buttonPane.add(btnUnschedule);
		btnNewexam.setMnemonic('n');
		buttonPane.add(btnNewexam);
			
		saveButton = new JButton("Save  changes");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveChanges();
			}
		});
		saveButton.setActionCommand("OK");
		buttonPane.add(saveButton);
		getRootPane().setDefaultButton(saveButton);
				
		JButton exitButton = new JButton("Exit");
		exitButton.setMnemonic('x');
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		exitButton.setActionCommand("Cancel");
		buttonPane.add(exitButton);
			
		for (String g : groupsList) grpBox.addItem(g);
		for (String ex : examsList) excodeBox.addItem(ex);
	}
	
	/**
	 * Refreshes input fields.
	 * Reads from the underlying data model and refreshes the table in the main view.
	 */
	private void refreshInputFields() {
		if (isInNewScheduleMode) return;
		int selrow = scheduledTable.getSelectedRow();
		if (selrow==-1) return;
		grpBox.setSelectedItem(scheduledTable.getValueAt(selrow, 0));
		excodeBox.setSelectedItem(scheduledTable.getValueAt(selrow, 1));
		lblExName.setText((String)scheduledTable.getValueAt(selrow, 7));
			
		txtStartdate.setText( (String) ((String)model_schedulelist.getValueAt(selrow, 2)).split(" ")[0]);
		txtStarttime.setText( (String) ((String)model_schedulelist.getValueAt(selrow, 2)).split(" ")[1]);
		
		txtEnddate.setText( (String) ((String)model_schedulelist.getValueAt(selrow, 3)).split(" ")[0]);
		txtEndtime.setText( (String) ((String)model_schedulelist.getValueAt(selrow, 3)).split(" ")[1]);
		
		txtType.setText((String)scheduledTable.getValueAt(selrow, 4));
		txtTimeLimit.setText((String)scheduledTable.getValueAt(selrow, 5));
	}
	
	/**
	 * Puts the changes to the server.
	 * Schedules or reschedules the exam. The private variable {@link #isInNewScheduleMode} controls which mode is selected.
	 */
	private void saveChanges() {
		StringBuilder sb = new StringBuilder();
		
		if (isInNewScheduleMode) sb.append("exam schedule {");
		else sb.append("exam reschedule {");
		
		sb.append((String)grpBox.getSelectedItem()).append("|");
		sb.append((String)excodeBox.getSelectedItem()).append("|");
		
		System.out.println(txtStartdate.getText() + " " + txtStarttime.getText());
		
		try {
			ldt = LocalDateTime.parse(txtStartdate.getText() + " " + txtStarttime.getText(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
		}
		catch (DateTimeParseException exc)
		{
			throw new ExamsException("Enter start date as DD-MM-YYYY and time as HH:MM");
		}
		sb.append(ldt.toEpochSecond(ZoneOffset.UTC)).append("|");
		
		try {
			ldt = LocalDateTime.parse(txtEnddate.getText() + " " + txtEndtime.getText(), DateTimeFormatter.ofPattern("dd-MM-y HH:mm"));
		}
		catch (DateTimeParseException exc)
		{
			throw new ExamsException("Enter end date as DD-MM-YYYY and time as HH:MM");
		}
		sb.append(ldt.toEpochSecond(ZoneOffset.UTC)).append("|");
		sb.append(txtType.getText()).append("|");
		sb.append(txtTimeLimit.getText()).append("}");
		
		String servResp = sendAndReceive(sb.toString());
		if (servResp.startsWith("OK")) showMsg ("Changes saved to server. @TODO: Autorefresh");
		else showMsg("Unable to save changes");
		
		System.out.println(sb.toString());
	}
	
	/**
	 * Unschedules the exam
	 */
	void unschedule() {
		int selrow = scheduledTable.getSelectedRow();
		if (selrow==-1) return;
		
		String servResp = sendAndReceive("exam unschedule {"+scheduledTable.getValueAt(selrow, 0)+"|"+scheduledTable.getValueAt(selrow, 1)+"}");
		if (servResp.startsWith("OK")) showMsg ("Exam unscheduled. @TODO: Autorefresh");
		else showMsg("Unable to unschedule");
	}
}
