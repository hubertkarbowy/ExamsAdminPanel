package pl.hubertkarbowy.ExamsAdmin;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JScrollPane;
import javax.swing.table.*;

import pl.hubertkarbowy.ExamsAdmin.Testbanks.Testbank;

import java.awt.*;
import java.awt.Dialog.*;
import java.awt.event.*;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.*;
import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.*;



public class ExamsManagerPanel extends JDialog {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel tablemodel;
	JPanel upperPanel;

	private JTextField excode;
	private JTextField exname;
	private JTextField coursename;
	private JScrollPane spTable;
	private JTextArea exdesc;
	private JTextArea exscope;
	
	private String allExamCodes;
	private List<String> allExamCodesAsList = new ArrayList<>();
	private List<List<String>> completeContent = new ArrayList<>();
	private List<List<String>> tableContent = new ArrayList<>();
	
	private Testbanks allTestbanks;
	private List<Testbank> allTestbanksAsList = new ArrayList<>();
	
	Delimiter semicolon = Delimiter.SEMICOLON;
	Delimiter pipe = Delimiter.PIPE;
	private JComboBox<String> testbankSelector;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) { // tylko do obslugi "lokalnej"
				try {
					ExamsSchedulerPanel frame = new ExamsSchedulerPanel();
					frame.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosed(WindowEvent e) {
								prevWindowQueue.poll();
							}
						});
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}*/

	/**
	 * Create the frame.
	 */
	public ExamsManagerPanel() throws ExamsException
	{
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				prevWindowQueue.peek().setVisible(true);
			}
		});
		
		
		setModal(true);
		setLocationRelativeTo(null);
		setTitle("My exams");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 707, 575);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		upperPanel = new JPanel();
		upperPanel.setBounds(5, 5, 686, 155);
		contentPane.add(upperPanel);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBounds(5, 472, 686, 47);
		contentPane.add(bottomPanel);
		bottomPanel.setLayout(null);
		
		JButton btnApplyChanges = new JButton("Apply changes");
		btnApplyChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				applyChanges();
			}
		});
		btnApplyChanges.setBounds(62, 12, 137, 25);
		bottomPanel.add(btnApplyChanges);
		
		JButton btnReturnToMain = new JButton("Exit without saving");
		btnReturnToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prevWindowQueue.peek().setVisible(true);
				prevWindowQueue.poll();
				dispose();
			}
		});
		btnReturnToMain.setBounds(482, 12, 175, 25);
		bottomPanel.add(btnReturnToMain);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 180, 686, 280);
		contentPane.add(panel);
		
		JLabel label = new JLabel("Exam code:");
		label.setBounds(87, 14, 79, 15);
		panel.add(label);
		
		excode = new JTextField();
		excode.setEnabled(false);
		excode.setColumns(10);
		excode.setBounds(171, 12, 503, 19);
		panel.add(excode);
		
		JLabel label_1 = new JLabel("Exam name:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(57, 37, 109, 15);
		panel.add(label_1);
		
		exname = new JTextField();
		exname.setColumns(10);
		exname.setBounds(171, 35, 503, 19);
		panel.add(exname);
		
		JLabel label_2 = new JLabel("Course name:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(57, 60, 109, 15);
		panel.add(label_2);
		
		coursename = new JTextField();
		coursename.setColumns(10);
		coursename.setBounds(171, 60, 503, 19);
		panel.add(coursename);
		
		JLabel lblTestbank = new JLabel("Testbank:");
		lblTestbank.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTestbank.setBounds(57, 86, 109, 15);
		panel.add(lblTestbank);
		
		testbankSelector = new JComboBox();
		testbankSelector.setBounds(171, 85, 503, 19);
		panel.add(testbankSelector);
		
		JLabel label_3 = new JLabel("Exam description:");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(26, 146, 140, 15);
		panel.add(label_3);
		
		exdesc = new JTextArea();
		JScrollPane spExamDesc = new JScrollPane(exdesc);
		spExamDesc.setBounds(171, 116, 503, 76);
		panel.add(spExamDesc);
		
		JLabel label_4 = new JLabel("Exam scope:");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setBounds(26, 230, 140, 15);
		panel.add(label_4);
		
		exscope = new JTextArea();
		JScrollPane spExamScope = new JScrollPane(exscope);
		spExamScope.setBounds(171, 212, 503, 56);
		panel.add(spExamScope);
		
		
		// ----------------------  DB access code -------------------------- //
		
		populateEntries();
		
		
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		spTable = new JScrollPane(table);
		upperPanel.add(spTable);
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		upperPanel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.PAGE_AXIS));
		
		
		JButton btnNewExam = new JButton("New exam");
		panel_1.add(btnNewExam);
		btnNewExam.setHorizontalAlignment(SwingConstants.RIGHT);
		btnNewExam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog newExam = new AddNewExam();
				prevWindowQueue.peek().setVisible(false);
				newExam.setVisible(true);
			}
		});
		btnNewExam.setBounds(557, 56, 117, 25);
		
		JButton btnRemoveExam = new JButton("Remove exam");
		btnRemoveExam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeExam();
			}
		});
		panel_1.add(btnRemoveExam);
		btnRemoveExam.setHorizontalAlignment(SwingConstants.RIGHT);
		upperPanel.setVisible(true);
		
		upperPanel.revalidate();
		upperPanel.repaint();
	}
	
	private void populateEntries()
	{
		allExamCodes=sendAndReceive("exam query *");
		if (!allExamCodes.startsWith("ERR=NO_RECORDS_FOUND")) {
			if (!allExamCodes.startsWith("OK=")) throw new ExamsException("Unable to retrieve the list of your exams.");
			allExamCodesAsList=tokenize(allExamCodes, semicolon);
			tableContent.clear();
			completeContent.clear();
			for (String singleExam : allExamCodesAsList) {
				String response = sendAndReceive("exam get \""+singleExam+"\"");
				if (!response.startsWith("OK=")) throw new ExamsException("Invalid exam format.");
				
				tableContent.add(tokenize(response, pipe));
				
				completeContent.add(tokenize(response, pipe));
			}
			parseExamsv2(new String[]{"Code", "Name"}, tableContent);
			
			allTestbanks = new Testbanks();
			allTestbanks.populate();
			allTestbanksAsList = allTestbanks.getTestbanks();
			Collections.sort(allTestbanksAsList);
			
			testbankSelector.removeAllItems();
			testbankSelector.addItem("");
			for (Testbank tbId : allTestbanksAsList) // ugly... but no time for developing a ComboBoxModel...
			{
				testbankSelector.addItem(tbId.getName());
			}
			
		}
		else
		{
			Object[][] emptyTable= new Object[1][1];
			table = new JTable(emptyTable, new String[]{"Exam code", "Exam name"}   ){
				@Override
				public boolean isCellEditable(int row, int col)
		        { return false; }
			};
			table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		}
	}
	
	private void parseExamsv2(String[] columns, List<List<String>> columnsContent)
	{
		for (List<String> singleList : columnsContent) // what an ugly and unsafe hack!
		{
			singleList.remove(1);
			for (int x=0; x<4; x++) singleList.remove(2);
		}

		Object[][] vals2=createTableModel(columnsContent);
		System.out.println("Size :" + vals2[0][1]);
		// table = new JTable(vals2, columns) {
		tablemodel = createTableModelv2(columns, columnsContent);
		table = new JTable(tablemodel) {
			@Override
			public boolean isCellEditable(int row, int col)
	        { return false; }
			
		};
		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse clicked at exam" + table.getValueAt(table.getSelectedRow(), 0));
                refreshInputFields();
                
            }
        });
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		// upperPanel.revalidate();
		// upperPanel.repaint();
	}
	
	private void refreshInputFields()
	{
		String selectedExamCode = (String) table.getValueAt(table.getSelectedRow(), 0);
		for (List<String> singleList : completeContent)
		{
			if (singleList.get(0).equals(selectedExamCode)) {
				/*
				 * 0 - code, 1 - testbank id, 2 - name, 3 - desc, 4 - req, 5 - examiner
				 */
				System.out.println(singleList);
				excode.setText(singleList.get(0));
				exname.setText(singleList.get(2));
				exdesc.setText(singleList.get(3));
				exscope.setText(singleList.get(5));
				coursename.setText(singleList.get(3));
				// testbankSelector.setSelectedIndex(anIndex);
				for (Testbank tbid : allTestbanksAsList)
				{
					if (singleList.get(1).equals(tbid.getId())) {
						testbankSelector.setSelectedItem(tbid.getName());
					}
				}
				
				break;
			}
		}
	}
	
	private void applyChanges() throws ExamsException
	{
		if (excode.getText().equals("")) throw new ExamsException("No exam selected");
		
		StringBuilder sb = new StringBuilder();
		String serverResponse = "";
		int selectedRow=0;
		
		sb.append("exam modify ");
		sb.append(excode.getText());
		sb.append(" {" + excode.getText() + "|");
		for (Testbank tbId : allTestbanksAsList) // ugly ugly ugly...
		{
			if (testbankSelector.getSelectedItem().toString().equals(tbId.getName()))
			{
				sb.append(tbId.getId() + "|");
				break;
			}
		}
		sb.append(exname.getText() + "|");
		sb.append(coursename.getText() + "|");
		sb.append(exdesc.getText() + "|");
		sb.append(exscope.getText() + "}");
		// sb.append(getUid() + "|");
//		System.out.println(sb.toString());
		serverResponse = sendAndReceive(sb.toString());
		if (!serverResponse.equals("OK")) throw new ExamsException(formatErrorNicely(serverResponse));
		else showMsg("Changes saved on server. @TODO: Autorefresh.");
		
		selectedRow=table.getSelectedRow();
		tablemodel.setValueAt(exname.getText(), selectedRow, 1);
		// table.setModel(tablemodel);
		// ((AbstractTableModel) table.getModel()).fireTableDataChanged();
		// table.repaint();
		// spTable.repaint();
				
	}
	
	private void removeExam() throws ExamsException
	{
		StringBuilder sb = new StringBuilder();
		String serverResponse = "";
		String examName = "";
		
		int selectedRow=table.getSelectedRow();
		if (selectedRow==-1) throw new ExamsException("Please select an exam to remove.");
		examName = table.getValueAt(table.getSelectedRow(), 1).toString();
		sb.append("exam remove "+table.getValueAt(table.getSelectedRow(), 0).toString());
		System.out.println(sb.toString());
		
		serverResponse = sendAndReceive(sb.toString());
		if (!serverResponse.equals("OK")) throw new ExamsException(formatErrorNicely(serverResponse));
		else showMsg("Exam " + examName + " removed. @TODO: Autorefresh.");
		tablemodel.removeRow(selectedRow);
		tablemodel.fireTableDataChanged();
	}
}
