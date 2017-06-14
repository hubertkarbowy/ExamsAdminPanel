package pl.hubertkarbowy.ExamsAdmin;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.prevWindowQueue;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

// import pl.hubertkarbowy.ExamsAdmin.Questions;
import pl.hubertkarbowy.ExamsAdmin.Questions.Question;
import pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.Delimiter;
import pl.hubertkarbowy.ExamsAdmin.Testbanks.Testbank;

public class TestbanksManager extends JDialog implements ActionListener {
	private JPanel panel;
	private JTextField qid;
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
	private JTextField textField;
	private JLabel lblKeywordsl;
	private JButton btnRemoveThisQuestion;
	
	private boolean isRunning = false;
	private Timer timer;
	
	private JTable table_questions;
	private Questions allQuestions;
	private List<Question> allQuestionsAsList = new ArrayList<>();
	private DefaultTableModel tablemodel_questions;
	
	Delimiter semicolon = Delimiter.SEMICOLON;
	Delimiter pipe = Delimiter.PIPE;
	
	
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 745, 200);
		QBankTab.add(scrollPane);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnExit.setMnemonic('x');
		btnExit.setBounds(640, 472, 117, 25);
		QBankTab.add(btnExit);
		
		JButton btnApplyChanges = new JButton("Apply changes");
		btnApplyChanges.setMnemonic('s');
		btnApplyChanges.setBounds(12, 472, 160, 25);
		QBankTab.add(btnApplyChanges);
		
		JButton btnNewQuestion = new JButton("New question");
		btnNewQuestion.setMnemonic('n');
		btnNewQuestion.setBounds(184, 472, 146, 25);
		QBankTab.add(btnNewQuestion);
		
		qid = new JTextField();
		qid.setEditable(false);
		qid.setBounds(671, 211, 86, 19);
		QBankTab.add(qid);
		qid.setColumns(10);
		
		JLabel lblQuestionId = new JLabel("Question ID:");
		lblQuestionId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQuestionId.setBounds(550, 211, 103, 19);
		QBankTab.add(lblQuestionId);
		
		qtext = new JTextField();
		qtext.setBounds(133, 242, 624, 19);
		QBankTab.add(qtext);
		qtext.setColumns(10);
		
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
		
		JRadioButton opt1 = new JRadioButton("");
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
		
		txtOpt3 = new JTextField();
		txtOpt3.setColumns(10);
		txtOpt3.setBounds(162, 330, 595, 19);
		QBankTab.add(txtOpt3);
		
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
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(133, 416, 624, 19);
		QBankTab.add(textField);
		
		lblKeywordsl = new JLabel("Keywords:");
		lblKeywordsl.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKeywordsl.setBounds(22, 416, 108, 15);
		QBankTab.add(lblKeywordsl);
		
		btnRemoveThisQuestion = new JButton("Remove question");
		btnRemoveThisQuestion.setMnemonic('r');
		btnRemoveThisQuestion.setBounds(342, 472, 171, 25);
		QBankTab.add(btnRemoveThisQuestion);
		
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
		
		lblFilter = new JLabel("Filter:");
		lblFilter.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFilter.setBounds(505, 10, 70, 15);
		getContentPane().add(lblFilter);
		
		timer = new Timer(1500, this);
		// TabsPane.addTab("Questionsbank", (Component)new JPanel(false));
		
	}
	
	void initTimer()
	{
		// timer = new Timer(1000, this);
		if (!isRunning)	{ timer.restart(); isRunning=true;}
		else return;
	}
	
	void initFillQuestionsTable()
	{
		allQuestions = new Questions();
		allQuestions.populate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (QBankTab.isVisible()) {
			JOptionPane.showMessageDialog(this, "Questions bank tab is open");
			filterContent_questions = txtFilter.getText();
		}
		else if (TBankTab.isVisible()) {
			JOptionPane.showMessageDialog(this, "Testbanks bank tab is open");
			filterContent_testbanks = txtFilter.getText();
		}
		isRunning=false;
		timer.stop();
		
	}
}
