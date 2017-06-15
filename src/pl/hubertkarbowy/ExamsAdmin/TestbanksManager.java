package pl.hubertkarbowy.ExamsAdmin;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

// import pl.hubertkarbowy.ExamsAdmin.Questions;
import pl.hubertkarbowy.ExamsAdmin.Questions.Question;
import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.*;
import pl.hubertkarbowy.ExamsAdmin.Testbanks.Testbank;
import java.awt.Dimension;
import java.awt.Color;

public class TestbanksManager extends JDialog implements ActionListener {
	private JPanel panel;
	private JTextField txtQid;
	private JTextField qtext;
	private JTextField txtOpt1;
	private JPanel TBankTab;
	private JPanel QBankTab;
	private JTextField txtFilter;
	private JLabel lblFilter;
	private String filterContent_questions = "";
	private String filterContent_testbanks = "";
	private JLabel lblOption_1;
	private JRadioButton opt2;
	private JTextField txtOpt2;
	private JTextField txtOpt3;
	private JRadioButton opt3;
	private JLabel lblOption_2;
	private JTextField txtOpt4;
	private JRadioButton opt4;
	private JLabel lblOption_3;
	private JTextField txtOpt5;
	private JRadioButton opt5;
	private JLabel lblOption_4;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField txtKwords;
	private JLabel lblKeywordsl;
	private JButton btnRemoveThisQuestion;
	List<Component> clist = new ArrayList<>();
	
	private boolean isRunning = false;
	private Timer timer;
	
	private JTable table_questions;
	private Questions allQuestions;
	private List<Question> allQuestionsAsList = new ArrayList<>();
	private List<List<String>> tableContent = new ArrayList<>();
	private DefaultTableModel tablemodel_questions;
	
	Delimiter semicolon = Delimiter.SEMICOLON;
	Delimiter pipe = Delimiter.PIPE;
	private JScrollPane scrollPane;
	private JPanel upperPane;
	private JRadioButton opt1;
	
	private boolean isInNewQuestionMode=false;
	private JLabel lblSelmode;
	private String setOpt;
	private JButton btnNewQuestion;
	
	private Consumer<Component> clearTextFields = new Consumer<Component>() {
		@Override
		public void accept(Component x) { 
				if (x instanceof JTextField) ((JTextField)x).setText("");
		}
	};
	
	
	/**
	 * Create the dialog.
	 */
	public TestbanksManager() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				prevWindowQueue.poll().setVisible(true);
				// prevWindowQueue.poll();
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		txtFilter = new JTextField();
		txtFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				initTimer();
			}
		});
		txtFilter.setBounds(586, 8, 200, 19);
		getContentPane().add(txtFilter);
		txtFilter.setColumns(10);
		
		JTabbedPane viewSwitcher = new JTabbedPane(JTabbedPane.TOP);
		viewSwitcher.setBounds(12, 12, 774, 536);
		getContentPane().add(viewSwitcher);
		
		TBankTab = new JPanel();
		viewSwitcher.addTab("Testbanks", null, TBankTab, null);
		
		QBankTab = new JPanel();
		viewSwitcher.addTab("Questions bank", null, QBankTab, null);
		viewSwitcher.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (viewSwitcher.getSelectedIndex()==0) txtFilter.setText(filterContent_testbanks);
				else if (viewSwitcher.getSelectedIndex()==1) txtFilter.setText(filterContent_questions);
			}
		});
		QBankTab.setLayout(null);
		
		upperPane = new JPanel();
		upperPane.setBounds(0, 0, 769, 196);
		upperPane.setLayout(new BoxLayout(upperPane, BoxLayout.X_AXIS));
		QBankTab.add(upperPane);
		
		initFillQuestionsTable(new String[] {"ID", "Pytanie", "Opt1", "Opt2", "Opt3", "Opt4", "Opt5", "Correct", "KW", "Owner"}, Delimiter.PIPE);
		if ( ((String)table_questions.getValueAt(0, 0)).equals("-99") ) tablemodel_questions.removeRow(0); // ugly hack...
		table_questions.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               if (table_questions.isEnabled()) {
            	System.out.println("Mouse clicked at question" + table_questions.getValueAt(table_questions.getSelectedRow(), 0));
        		refreshInputFields();
               }
                
            }
        });
		// scrollPane = new JScrollPane(table_questions);
		scrollPane = new JScrollPane(table_questions);
		// scrollPane.add(table_questions);
		upperPane.add(scrollPane);
		table_questions.setVisible(true);
		table_questions.repaint();
		
		JButton btnApplyChanges = new JButton("Apply changes");
		btnApplyChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isInNewQuestionMode) refreshTableViewAndModelAndSubmit();
				else {
					addNewItemAndSubmit();
					isInNewQuestionMode=false;
					table_questions.setEnabled(true);
					lblSelmode.setVisible(false);
					btnNewQuestion.setText("New question");
					
				}
				
			}
		});
		btnApplyChanges.setMnemonic('s');
		btnApplyChanges.setBounds(12, 472, 160, 25);
		QBankTab.add(btnApplyChanges);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnExit.setMnemonic('x');
		btnExit.setBounds(640, 472, 117, 25);
		QBankTab.add(btnExit);
		
		btnNewQuestion = new JButton("New question");
		btnNewQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isInNewQuestionMode) {
					isInNewQuestionMode=true;
					table_questions.setEnabled(false);
					table_questions.clearSelection();
					lblSelmode.setVisible(true);
					btnNewQuestion.setText("Abort changes");
					clist.stream().forEach(clearTextFields);
					/* for (Component c : clist) {
						if (c instanceof JTextField) ((JTextField) c).setText("");
						if (c instanceof JRadioButton) ((JRadioButton) c).setSelected(false);
					}*/
					buttonGroup.clearSelection();
					
				}
				else {
					isInNewQuestionMode=false;
					table_questions.setEnabled(true);
					lblSelmode.setVisible(false);
					btnNewQuestion.setText("New question");
					/* for (Component c : clist) {
						if (c instanceof JTextField) ((JTextField) c).setText("");
						if (c instanceof JRadioButton) ((JRadioButton) c).setSelected(false);
					}*/
					clist.stream().forEach(clearTextFields);
					buttonGroup.clearSelection();
				}
			}
		});
		btnNewQuestion.setMnemonic('n');
		btnNewQuestion.setBounds(184, 472, 146, 25);
		QBankTab.add(btnNewQuestion);
		
		txtQid = new JTextField();
		txtQid.setEditable(false);
		txtQid.setBounds(671, 211, 86, 19);
		QBankTab.add(txtQid);
		txtQid.setColumns(10);
		
		JLabel lblQuestionId = new JLabel("Question ID:");
		lblQuestionId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQuestionId.setBounds(550, 211, 103, 19);
		QBankTab.add(lblQuestionId);
		
		qtext = new JTextField();
		qtext.setBounds(133, 242, 624, 19);
		QBankTab.add(qtext);
		qtext.setColumns(10);
		
		txtOpt3 = new JTextField();
		txtOpt3.setColumns(10);
		txtOpt3.setBounds(162, 330, 595, 19);
		QBankTab.add(txtOpt3);
		
		JLabel lblQuestion = new JLabel("Question:");
		lblQuestion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQuestion.setBounds(22, 244, 108, 15);
		QBankTab.add(lblQuestion);
		
		txtOpt1 = new JTextField();
		txtOpt1.setColumns(10);
		txtOpt1.setBounds(162, 273, 595, 19);
		QBankTab.add(txtOpt1);
		
		JLabel lblOption = new JLabel("Option 1:");
		lblOption.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOption.setBounds(22, 271, 108, 15);
		QBankTab.add(lblOption);
		
		opt1 = new JRadioButton("");
		buttonGroup.add(opt1);
		opt1.setBounds(133, 269, 21, 23);
		QBankTab.add(opt1);
		
		lblOption_1 = new JLabel("Option 2:");
		lblOption_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOption_1.setBounds(22, 300, 108, 15);
		QBankTab.add(lblOption_1);
		
		opt2 = new JRadioButton("");
		buttonGroup.add(opt2);
		opt2.setBounds(133, 298, 21, 23);
		QBankTab.add(opt2);
		
		txtOpt2 = new JTextField();
		txtOpt2.setColumns(10);
		txtOpt2.setBounds(162, 302, 595, 19);
		QBankTab.add(txtOpt2);
		
		opt3 = new JRadioButton("");
		buttonGroup.add(opt3);
		opt3.setBounds(133, 326, 21, 23);
		QBankTab.add(opt3);
		
		lblOption_2 = new JLabel("Option 3:");
		lblOption_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOption_2.setBounds(22, 328, 108, 15);
		QBankTab.add(lblOption_2);
		
		txtOpt4 = new JTextField();
		txtOpt4.setColumns(10);
		txtOpt4.setBounds(162, 358, 595, 19);
		QBankTab.add(txtOpt4);
		
		opt4 = new JRadioButton("");
		buttonGroup.add(opt4);
		opt4.setBounds(133, 354, 21, 23);
		QBankTab.add(opt4);
		
		lblOption_3 = new JLabel("Option 4:");
		lblOption_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOption_3.setBounds(22, 356, 108, 15);
		QBankTab.add(lblOption_3);
		
		txtOpt5 = new JTextField();
		txtOpt5.setColumns(10);
		txtOpt5.setBounds(162, 385, 595, 19);
		QBankTab.add(txtOpt5);
		
		opt5 = new JRadioButton("");
		buttonGroup.add(opt5);
		opt5.setBounds(133, 381, 21, 23);
		QBankTab.add(opt5);
		
		lblOption_4 = new JLabel("Option 5:");
		lblOption_4.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOption_4.setBounds(22, 383, 108, 15);
		QBankTab.add(lblOption_4);
		
		txtKwords = new JTextField();
		txtKwords.setColumns(10);
		txtKwords.setBounds(133, 416, 624, 19);
		QBankTab.add(txtKwords);
		
		lblKeywordsl = new JLabel("Keywords:");
		lblKeywordsl.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKeywordsl.setBounds(22, 416, 108, 15);
		QBankTab.add(lblKeywordsl);
		
		btnRemoveThisQuestion = new JButton("Remove question");
		btnRemoveThisQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeFromDBAndModel();
			}
		});
		btnRemoveThisQuestion.setMnemonic('r');
		btnRemoveThisQuestion.setBounds(342, 472, 171, 25);
		QBankTab.add(btnRemoveThisQuestion);
		
		lblSelmode = new JLabel("EDITING A NEW QUESTION. Press 'Apply' when ready or 'Abort' to drop changes.");
		lblSelmode.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelmode.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSelmode.setVisible(false);
		lblSelmode.setForeground(Color.RED);
		lblSelmode.setBounds(12, 443, 745, 15);
		QBankTab.add(lblSelmode);
		
		lblFilter = new JLabel("Filter:");
		lblFilter.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFilter.setBounds(505, 10, 70, 15);
		getContentPane().add(lblFilter);
		
		clist.add(qtext); clist.add(txtQid); clist.add(txtOpt1); clist.add(txtOpt2); clist.add(txtOpt3); clist.add(txtOpt4); clist.add(txtOpt5);
		clist.add(opt1); clist.add(opt2); clist.add(opt3); clist.add(opt4); clist.add(opt5);
		clist.add(txtKwords);
		timer = new Timer(1500, this);
		
		
	}
	
	void initTimer()
	{
		// timer = new Timer(1000, this);
		if (!isRunning)	{ timer.restart(); isRunning=true;}
		else return;
	}
	
	@SuppressWarnings("static-access")
	void initFillQuestionsTable(String[] columnNames, Delimiter delimiter)
	{
		allQuestions = new Questions();
		allQuestions.populate(null);
		tableContent = allQuestions.getAllQuestionsAsList();
		System.out.println(tableContent);
		tablemodel_questions = parseExamsv3(columnNames, tableContent);
		table_questions = new JTable(tablemodel_questions);
		table_questions.removeColumn(table_questions.getColumnModel().getColumn(9)); // usuniecie id wlasciciela

		//table.getColumnModel().getColumn(0).setPreferredWidth(100);
		// table.getColumnModel().getColumn(1).setPreferredWidth(300);
		
		
	}
	
	void refreshInputFields()
	{
		int currentRow = table_questions.getSelectedRow();
		List<String> selectedRow = new ArrayList<>();
		for (int i=0; i<table_questions.getColumnCount(); i++) selectedRow.add((String)table_questions.getValueAt(currentRow, i));
		
		/*
		 * 0 - qid, 1 - question, 2 - opt1, 3 - opt2, 4 - opt3, 5 - opt4, 6 - opt5, 7 - correct, 8 - keyw, 9 - owner
		 */
		txtQid.setText(selectedRow.get(0));
		qtext.setText(selectedRow.get(1));
		txtOpt1.setText(selectedRow.get(2));
		txtOpt2.setText(selectedRow.get(3));
		txtOpt3.setText(selectedRow.get(4));
		txtOpt4.setText(selectedRow.get(5));
		txtOpt5.setText(selectedRow.get(6));
		switch (selectedRow.get(7)) {
			case "1": opt1.setSelected(true); break;
			case "2": opt2.setSelected(true); break;
			case "3": opt3.setSelected(true); break;
			case "4": opt4.setSelected(true); break;
			case "5": opt5.setSelected(true); break;
			default: opt1.setSelected(false); opt2.setSelected(false); opt3.setSelected(false); opt4.setSelected(false); opt5.setSelected(false); break;
		}
		txtKwords.setText(selectedRow.get(8));
	}
	
	void refreshTableViewAndModelAndSubmit()
	{
		if (table_questions.getSelectedRow()<0) throw new ExamsException("Please select a question to edit.");
		StringBuilder sb = new StringBuilder();
		String serverResponse;
		
		sb.append("testitem modify " + txtQid.getText() + " {" + txtQid.getText()+"|");
		sb.append(qtext.getText() + "|");
		sb.append(txtOpt1.getText() + "|");
		sb.append(txtOpt2.getText() + "|");
		sb.append(txtOpt3.getText() + "|");
		sb.append(txtOpt4.getText() + "|");
		sb.append(txtOpt5.getText() + "|");
		if (opt1.isSelected()) sb.append("1|");
		if (opt2.isSelected()) sb.append("2|");
		if (opt3.isSelected()) sb.append("3|");
		if (opt4.isSelected()) sb.append("4|");
		if (opt5.isSelected()) sb.append("5|");
		sb.append(txtKwords.getText()+"|");
		sb.append(getUid()+"}");
		System.out.println(sb.toString());
		serverResponse = sendAndReceive(sb.toString());
		if (!serverResponse.startsWith("OK")) throw new ExamsException(formatErrorNicely(serverResponse));
		else showMsg("Changes saved on server.");
		
		
		
		int row = table_questions.getSelectedRow();
		tablemodel_questions.setValueAt(qtext.getText(), row, 1);
		tablemodel_questions.setValueAt(txtOpt1.getText(), row, 2);
		tablemodel_questions.setValueAt(txtOpt2.getText(), row, 3);
		tablemodel_questions.setValueAt(txtOpt3.getText(), row, 4);
		tablemodel_questions.setValueAt(txtOpt4.getText(), row, 5);
		tablemodel_questions.setValueAt(txtOpt5.getText(), row, 6);
		
		if (opt1.isSelected()) tablemodel_questions.setValueAt("1", row, 7);
		if (opt2.isSelected()) tablemodel_questions.setValueAt("2", row, 7);
		if (opt3.isSelected()) tablemodel_questions.setValueAt("3", row, 7);
		if (opt4.isSelected()) tablemodel_questions.setValueAt("4", row, 7);
		if (opt5.isSelected()) tablemodel_questions.setValueAt("5", row, 7);
		tablemodel_questions.setValueAt(txtKwords.getText(), row, 8);
	}
	
	void addNewItemAndSubmit()
	{
		if (qtext.getText().equals("")) throw new ExamsException("Question text cannot be empty.");
		if (!opt1.isSelected() && !opt2.isSelected() && !opt3.isSelected() && !opt4.isSelected() && !opt5.isSelected()) throw new ExamsException("Please mark the correct answer.");
		StringBuilder sb = new StringBuilder();
		String serverResponse;
		
		sb.append("testitem new {null|");
		sb.append(qtext.getText() + "|");
		sb.append(txtOpt1.getText() + "|");
		sb.append(txtOpt2.getText() + "|");
		sb.append(txtOpt3.getText() + "|");
		sb.append(txtOpt4.getText() + "|");
		sb.append(txtOpt5.getText() + "|");
		if (opt1.isSelected()) { sb.append("1|"); setOpt="1";}
		if (opt2.isSelected()) { sb.append("2|"); setOpt="2";}
		if (opt3.isSelected()) { sb.append("3|"); setOpt="3";}
		if (opt4.isSelected()) { sb.append("4|"); setOpt="4";}
		if (opt5.isSelected()) { sb.append("5|"); setOpt="5";}
		sb.append(txtKwords.getText()+"|");
		sb.append(getUid()+"}");
		System.out.println(sb.toString());
		
		List<? super String> ls = Arrays.asList(null, qtext, txtOpt1, txtOpt2, txtOpt3, txtOpt4, txtOpt5, setOpt, txtKwords, getUid());
		
		serverResponse = sendAndReceive(sb.toString());
		if (!serverResponse.startsWith("OK")) throw new ExamsException(formatErrorNicely(serverResponse));
		else showMsg("New question added to server database.");
		String newid = serverResponse.split("=")[1];
		ls.set(0, newid);
		
		List<String> toAdd = ls.stream()
				.sequential().map(x -> {
				if (x == null) return "null"; 
				if (x instanceof JTextField) return ((JTextField)x).getText();
				else return x.toString();
				})
				.collect(Collectors.toList());
		((BetterTableModel)tablemodel_questions).addRow2(toAdd);
		clist.stream().forEach( x -> {if (x instanceof JTextField) ((JTextField)x).setText("");});
		buttonGroup.clearSelection();
		
	}
	
	void removeFromDBAndModel()
	{
		if (table_questions.getSelectedRow()<0) throw new ExamsException("Please select a question to remove.");
		int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete question " + table_questions.getValueAt(table_questions.getSelectedRow(), 0), "tytul", JOptionPane.YES_NO_OPTION);
	    if (answer == JOptionPane.YES_OPTION) {
	      String serverResponse;
	      System.out.println("testitem remove " + table_questions.getValueAt(table_questions.getSelectedRow(), 0));
	      serverResponse = sendAndReceive("testitem remove " + table_questions.getValueAt(table_questions.getSelectedRow(), 0));
	      System.out.println(serverResponse);
	      if (!serverResponse.startsWith("OK")) throw new ExamsException("Cannot remove this item.");
	      tablemodel_questions.removeRow(table_questions.getSelectedRow());
	      table_questions.clearSelection();
	      buttonGroup.clearSelection();
	      List<? extends Component> ls = Arrays.asList(txtQid, qtext, txtOpt1, txtOpt2, txtOpt3, txtOpt4, txtOpt5, txtKwords);
	      ls.stream().forEach(clearTextFields);
	      showMsg("Successfully removed question from server database.");
	    }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (QBankTab.isVisible()) {
		//	JOptionPane.showMessageDialog(this, "Questions bank tab is open");
			filterContent_questions = txtFilter.getText();
			BetterTableModel btm = (BetterTableModel)tablemodel_questions;
			btm.clear();
			allQuestions = new Questions();
			if (filterContent_questions.equals("")) allQuestions.populate(null);
			else allQuestions.populate(txtFilter.getText());
			
			tableContent = allQuestions.getAllQuestionsAsList();
			if (tableContent.get(0).get(0).equals("-99")) btm.clear();
			else btm.update(tableContent);
		}
		else if (TBankTab.isVisible()) {
			JOptionPane.showMessageDialog(this, "Testbanks bank tab is open");
			filterContent_testbanks = txtFilter.getText();
		}
		isRunning=false;
		timer.stop();
		
	}
}
