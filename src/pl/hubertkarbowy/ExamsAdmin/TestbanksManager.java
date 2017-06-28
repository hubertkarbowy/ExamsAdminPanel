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
import java.util.stream.IntStream;

import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

// import pl.hubertkarbowy.ExamsAdmin.Questions;
import pl.hubertkarbowy.ExamsAdmin.Questions.Question;
import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.*;
import pl.hubertkarbowy.ExamsAdmin.Testbanks.Testbank;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import javafx.scene.control.RadioButton;


public class TestbanksManager extends JDialog implements ActionListener {
	private JPanel panel;
	private JTextField txtQid;
	private JTextField qtext;
	private JTextField txtOpt1;
	private JSplitPane TBankTab;
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
	private JTextField txtOpt4;
	private JLabel lblOption_4;
	private JRadioButton opt4;
	private JLabel lblOption_3;
	private JTextField txtOpt5;
	private JLabel lblOption_5;
	private JRadioButton opt5;
	
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
	private boolean isInNewTestbankMode=false;
	private JLabel lblSelmode;
	private String setOpt;
	private JButton btnNewQuestion;
	
	private Consumer<Component> clearTextFields = new Consumer<Component>() {
		@Override
		public void accept(Component x) { 
				if (x instanceof JTextField) ((JTextField)x).setText("");
		}
	};
	private JScrollPane testbanks_panel;
	private JPanel questions_panel;
	private JPanel filter_panel;
	private JPanel bottom_pane;
	private JPanel datafields_pane;
	private JLabel lblOption;
	private JLabel lblOption_2;
	private JPanel editmode_pane;
	private JPanel buttons_pane;
	private JScrollPane scrollPane_questionsshort;
	private JTable table_questionsshort;
	private JList<String> list;
	private JPanel tbButtons;
	private JButton btnAddQuestion;
	private JButton btnRemoveQuestion;
	private JButton btnSaveTestbank;
	private JButton btnExit_1;
	private JScrollPane list_scrPane;
	private JButton btnNewTestbank;
	private JList<String> listOfTbids;
	private DefaultListModel<String> listmodel;
	private DefaultListModel<String> listmodel_tbids;
	private JButton btnRemoveTestbank;
	private JLabel lblTbid;
	private JLabel label;
	private JLabel label_1;
	private JTextField txtTodowyrezana;
	
	
	
	/**
	 * Create the dialog.
	 */
	public TestbanksManager() {
	//	getContentPane().setSize(new Dimension(0, 30));
	//	getContentPane().setPreferredSize(new Dimension(0, 30));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				prevWindowQueue.poll().setVisible(true);
				// prevWindowQueue.poll();
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 940, 637);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		listmodel = new DefaultListModel<>();
		listmodel_tbids = new DefaultListModel<>();
		
		filter_panel = new JPanel();
		filter_panel.setMaximumSize(new Dimension(300, 30));
		filter_panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		getContentPane().add(filter_panel);
		
		lblFilter = new JLabel("Filter:");
		lblFilter.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtFilter = new JTextField();
		txtFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				initTimer();
			}
		});
		txtFilter.setColumns(10);
		filter_panel.setLayout(new BoxLayout(filter_panel, BoxLayout.X_AXIS));
		filter_panel.add(lblFilter);
		filter_panel.add(txtFilter);
		
		JTabbedPane viewSwitcher = new JTabbedPane(JTabbedPane.TOP);
		viewSwitcher.setMinimumSize(new Dimension(0, 0));
		viewSwitcher.setPreferredSize(new Dimension(0, 0));
		getContentPane().add(viewSwitcher);
		
		TBankTab = new JSplitPane();
		viewSwitcher.addTab("Testbanks", null, TBankTab, null);
		
		
	//	panel_1.setPreferredSize(new Dimension(200, 10));
		
		
		listOfTbids = new JList<String>(listmodel_tbids);
		listOfTbids.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				switchToADifferentTestbank();
			}
		});
		listOfTbids.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listOfTbids.setMinimumSize(new Dimension(50, 50));
		testbanks_panel = new JScrollPane(listOfTbids);
		testbanks_panel.setMinimumSize(new Dimension(200, 22));
		testbanks_panel.setPreferredSize(new Dimension(200, 10));
		
		TBankTab.setLeftComponent(testbanks_panel);
		
		questions_panel = new JPanel();
		TBankTab.setRightComponent(questions_panel);
		questions_panel.setLayout(new BoxLayout(questions_panel, BoxLayout.Y_AXIS));
		
		
	
		
		table_questionsshort = new JTable();
		scrollPane_questionsshort = new JScrollPane(table_questionsshort);
		questions_panel.add(scrollPane_questionsshort);
		
		
		list = new JList<String>(listmodel);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setMinimumSize(new Dimension(0, 50));
		list_scrPane = new JScrollPane(list);
		list_scrPane.setPreferredSize(new Dimension(200, 131));
		questions_panel.add(list_scrPane);
		
		tbButtons = new JPanel();
		tbButtons.setMinimumSize(new Dimension(10, 300));
		FlowLayout flowLayout = (FlowLayout) tbButtons.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		questions_panel.add(tbButtons);
		
		btnAddQuestion = new JButton("Add questions");
		btnAddQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listOfTbids.isSelectionEmpty()) {showMsg("Please select a testbank first"); return;}
				int selectedrows[] = table_questionsshort.getSelectedRows();
				for (int singlerow : selectedrows) {
					String singleValue = (String)table_questionsshort.getValueAt(singlerow, 0);
					if (!listmodel.contains(singleValue)) listmodel.addElement(singleValue);
				}
			}
		});
		btnAddQuestion.setMnemonic('a');
		tbButtons.add(btnAddQuestion);
		
		btnRemoveQuestion = new JButton("Remove questions");
		btnRemoveQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				List<String> toRem = list.getSelectedValuesList();
				toRem.stream().forEach(x -> listmodel.removeElement(x));
				}
				catch (Exception ee) {
					JOptionPane.showMessageDialog(null, "Error deleting questions.\n" + ee.getMessage());
				}
			}
		});
		
		btnRemoveQuestion.setMnemonic('r');
		tbButtons.add(btnRemoveQuestion);
		
		btnNewTestbank = new JButton("New testbank");
		btnNewTestbank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tbid = JOptionPane.showInputDialog("Enter new testbank id (must be unique)");
				if (tbid==null || tbid.isEmpty()) return;
				if (!sendAndReceive("testbank get " + tbid).equals("ERR=NOT_FOUND")) throw new ExamsException("A testbank with this id already exists");
				String tbname = JOptionPane.showInputDialog("Enter new testbank name");
				if (tbname==null || tbname.isEmpty()) return;
				String srvResp = sendAndReceive("testbank new {"+tbid+"|"+tbname+"}");
				if (srvResp.startsWith("OK")) showMsg("New testbank created successfully. @TODO: Autorefresh");
				else showMsg("Oops - something went wrong while creating a new testbank");
				refreshTbIds();
			}
		});
		btnNewTestbank.setMnemonic('n');
		tbButtons.add(btnNewTestbank);
		
		btnRemoveTestbank = new JButton("Remove testbank");
		tbButtons.add(btnRemoveTestbank);
		
		btnSaveTestbank = new JButton("Save testbank");
		btnSaveTestbank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTestbank();
			}
		});
		btnSaveTestbank.setMnemonic('s');
		tbButtons.add(btnSaveTestbank);
		
		btnExit_1 = new JButton("Exit");
		btnExit_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnExit_1.setMnemonic('x');
		tbButtons.add(btnExit_1);
		
		lblTbid = new JLabel("");
		lblTbid.setForeground(Color.GRAY);
		tbButtons.add(lblTbid);
		
		
		
		/*
		 * ***********************************************************
		*/
		
		QBankTab = new JPanel();
		viewSwitcher.addTab("Questions bank", null, QBankTab, null);
		viewSwitcher.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (viewSwitcher.getSelectedIndex()==0) txtFilter.setText(filterContent_testbanks);
				else if (viewSwitcher.getSelectedIndex()==1) txtFilter.setText(filterContent_questions);
			}
		});
		QBankTab.setLayout(new BoxLayout(QBankTab, BoxLayout.Y_AXIS));
		
		upperPane = new JPanel();
		upperPane.setPreferredSize(new Dimension(10, 300));
		upperPane.setLayout(new BoxLayout(upperPane, BoxLayout.X_AXIS));
		QBankTab.add(upperPane);
		
		initFillQuestionsTable(new String[] {"ID", "Question", "Opt1", "Opt2", "Opt3", "Opt4", "Opt5", "Correct", "KW", "Owner"}, Delimiter.PIPE);
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
		
		datafields_pane = new JPanel();
		datafields_pane.setPreferredSize(new Dimension(10, 250));
		
		createShortTable();
		
		QBankTab.add(datafields_pane);
		
		JLabel lblQuestionId = new JLabel("Question ID:");
		lblQuestionId.setHorizontalAlignment(SwingConstants.LEFT);
		
		txtQid = new JTextField();
		txtQid.setEditable(false);
		txtQid.setColumns(10);
		clist.add(txtQid); 
		
		qtext = new JTextField();
		qtext.setColumns(10);
		
		clist.add(qtext);
		
		JLabel lblQuestion = new JLabel("Question:");
		
		txtKwords = new JTextField();
		txtKwords.setColumns(10);
		clist.add(txtKwords);
		
		lblKeywordsl = new JLabel("Keywords:");
		lblKeywordsl.setHorizontalAlignment(SwingConstants.LEFT);
		
		btnRemoveThisQuestion = new JButton("Remove question");
		btnRemoveThisQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeFromDBAndModel();
			}
		});
		btnRemoveThisQuestion.setMnemonic('r');
		
		lblSelmode = new JLabel("EDITING A NEW QUESTION. Press 'Apply' when ready or 'Abort' to drop changes.");
		lblSelmode.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelmode.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSelmode.setVisible(false);
		lblSelmode.setForeground(Color.RED);
		GridBagLayout gbl_datafields_pane = new GridBagLayout();
		gbl_datafields_pane.columnWidths = new int[] {100, 10, 0};
		gbl_datafields_pane.rowHeights = new int[] {30, 30, 30, 30, 30, 30, 30, 30};
		gbl_datafields_pane.columnWeights = new double[]{0.01, 0.01, 1.0};
		gbl_datafields_pane.rowWeights = new double[]{0.03, 0.03, 0.03, 0.03, 0.03, 0.03, 0};
		datafields_pane.setLayout(gbl_datafields_pane);
		
		
		GridBagConstraints gbc_lbl_qid  = new GridBagConstraints();
		gbc_lbl_qid.fill = GridBagConstraints.BOTH;
		gbc_lbl_qid.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_qid.gridx = 0;
		gbc_lbl_qid.gridy = 0;
		datafields_pane.add(lblQuestionId, gbc_lbl_qid);
		
		GridBagConstraints gbc_txtQid  = new GridBagConstraints();
		gbc_txtQid.fill = GridBagConstraints.BOTH;
		gbc_txtQid.insets = new Insets(0, 0, 5, 0);
		gbc_txtQid.gridwidth=2;
		gbc_txtQid.gridx = 1;
		gbc_txtQid.gridy = 0;
		datafields_pane.add(txtQid, gbc_txtQid);
		
		
		
		
		
		GridBagConstraints gbc_lbl_qtxt  = new GridBagConstraints();
		gbc_lbl_qtxt.fill = GridBagConstraints.BOTH;
		gbc_lbl_qtxt.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_qtxt.gridx = 0;
		gbc_lbl_qtxt.gridy = 1;
		datafields_pane.add(lblQuestion, gbc_lbl_qtxt);
		
		GridBagConstraints gbc_qtxt  = new GridBagConstraints();
		gbc_qtxt.fill = GridBagConstraints.BOTH;
		gbc_qtxt.insets = new Insets(0, 0, 5, 0);
		gbc_qtxt.gridwidth = 2;
		gbc_qtxt.gridx = 1;
		gbc_qtxt.gridy = 1;
		datafields_pane.add(qtext, gbc_qtxt);
		
		
		lblOption = new JLabel("Option 1:");
		lblOption.setMaximumSize(new Dimension(30, 15));
		lblOption.setPreferredSize(new Dimension(30, 15));
		
			
			GridBagConstraints gbc_lblOption = new GridBagConstraints();
			gbc_lblOption.fill = GridBagConstraints.BOTH;
			gbc_lblOption.insets = new Insets(0, 0, 5, 5);
			gbc_lblOption.gridx = 0;
			gbc_lblOption.gridy = 2;
			datafields_pane.add(lblOption, gbc_lblOption);
		
		opt1 = new JRadioButton("");
		buttonGroup.add(opt1);
		clist.add(opt1); 
		GridBagConstraints gbc_opt1 = new GridBagConstraints();
		gbc_opt1.fill = GridBagConstraints.BOTH;
		gbc_opt1.insets = new Insets(0, 0, 5, 5);
		gbc_opt1.gridx = 1;
		gbc_opt1.gridy = 2;
		datafields_pane.add(opt1, gbc_opt1);
		//lblQuestion.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtOpt1 = new JTextField();
		//	txtOpt1.setMaximumSize(new Dimension(2147483647, 20));
		//	txtOpt1.setPreferredSize(new Dimension(4, 20));
		//	txtOpt1.setColumns(10);
			clist.add(txtOpt1); 
			GridBagConstraints gbc_txtOpt1 = new GridBagConstraints();
			gbc_txtOpt1.fill = GridBagConstraints.BOTH;
			gbc_txtOpt1.insets = new Insets(0, 0, 5, 0);
			gbc_txtOpt1.gridx = 2;
			gbc_txtOpt1.gridy = 2;
			datafields_pane.add(txtOpt1, gbc_txtOpt1);
		
		lblOption_2 = new JLabel("Option 2:");
		lblOption_2.setMaximumSize(new Dimension(30, 15));
		lblOption_2.setPreferredSize(new Dimension(30, 15));
		
			GridBagConstraints gbc_lblOption_2 = new GridBagConstraints();
			gbc_lblOption_2.fill = GridBagConstraints.BOTH;
			gbc_lblOption_2.insets = new Insets(0, 0, 5, 5);
			gbc_lblOption_2.gridx = 0;
			gbc_lblOption_2.gridy = 3;
			datafields_pane.add(lblOption_2, gbc_lblOption_2);

		
		opt2 = new JRadioButton("");
		buttonGroup.add(opt2);
		clist.add(opt2);
		GridBagConstraints gbc_opt2 = new GridBagConstraints();
		gbc_opt2.fill = GridBagConstraints.BOTH;
		gbc_opt2.insets = new Insets(0, 0, 5, 5);
		gbc_opt2.gridx = 1;
		gbc_opt2.gridy = 3;
		datafields_pane.add(opt2, gbc_opt2);
		
		txtOpt2 = new JTextField();
	
		txtOpt2.setColumns(10);
		clist.add(txtOpt2);
		GridBagConstraints gbc_txtOpt2 = new GridBagConstraints();
		gbc_txtOpt2.fill = GridBagConstraints.BOTH;
		gbc_txtOpt2.insets = new Insets(0, 0, 5, 0);
		gbc_txtOpt2.gridx = 2;
		gbc_txtOpt2.gridy = 3;
		datafields_pane.add(txtOpt2, gbc_txtOpt2);
		
		lblOption_3 = new JLabel("Option 3:");
		lblOption_3.setMaximumSize(new Dimension(30, 15));
		lblOption_3.setPreferredSize(new Dimension(30, 15));
		
			GridBagConstraints gbc_lblOption_3 = new GridBagConstraints();
			gbc_lblOption_3.fill = GridBagConstraints.BOTH;
			gbc_lblOption_3.insets = new Insets(0, 0, 5, 5);
			gbc_lblOption_3.gridx = 0;
			gbc_lblOption_3.gridy = 4;
			datafields_pane.add(lblOption_3, gbc_lblOption_3);
		
		opt3 = new JRadioButton("");
		buttonGroup.add(opt3);
		clist.add(opt3); 
		GridBagConstraints gbc_opt3 = new GridBagConstraints();
		gbc_opt3.fill = GridBagConstraints.BOTH;
		gbc_opt3.insets = new Insets(0, 0, 5, 5);
		gbc_opt3.gridx = 1;
		gbc_opt3.gridy = 4;
		datafields_pane.add(opt3, gbc_opt3);
		
		txtOpt3 = new JTextField();
		txtOpt3.setMaximumSize(new Dimension(2147483647, 20));
		txtOpt3.setColumns(10);
		clist.add(txtOpt3); 
		GridBagConstraints gbc_txtOpt3 = new GridBagConstraints();
		gbc_txtOpt3.fill = GridBagConstraints.BOTH;
		gbc_txtOpt3.insets = new Insets(0, 0, 5, 0);
		gbc_txtOpt3.gridx = 2;
		gbc_txtOpt3.gridy = 4;
		datafields_pane.add(txtOpt3, gbc_txtOpt3);
		
		lblOption_4 = new JLabel("Option 4:");
		lblOption_4.setMaximumSize(new Dimension(30, 15));
		lblOption_4.setPreferredSize(new Dimension(30, 15));
		lblOption_4.setHorizontalAlignment(SwingConstants.LEFT);
		
		GridBagConstraints gbc_lblOption_4 = new GridBagConstraints();
		gbc_lblOption_4.fill = GridBagConstraints.BOTH;
		gbc_lblOption_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblOption_4.gridx = 0;
		gbc_lblOption_4.gridy = 5;
		datafields_pane.add(lblOption_4, gbc_lblOption_4);
		
		opt4 = new JRadioButton("");
		buttonGroup.add(opt4);
		clist.add(opt4); 
		GridBagConstraints gbc_opt4 = new GridBagConstraints();
		gbc_opt4.fill = GridBagConstraints.BOTH;
		gbc_opt4.insets = new Insets(0, 0, 5, 5);
		gbc_opt4.gridx = 1;
		gbc_opt4.gridy = 5;
		datafields_pane.add(opt4, gbc_opt4);
		//lblOption_3.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtOpt4 = new JTextField();
		txtOpt4.setColumns(10);
		clist.add(txtOpt4); 
		GridBagConstraints gbc_txtOpt4 = new GridBagConstraints();
		gbc_txtOpt4.fill = GridBagConstraints.BOTH;
		gbc_txtOpt4.insets = new Insets(0, 0, 5, 0);
		gbc_txtOpt4.gridx = 2;
		gbc_txtOpt4.gridy = 5;
		datafields_pane.add(txtOpt4, gbc_txtOpt4);
		
		lblOption_5 = new JLabel("Option 5:");
		lblOption_5.setMaximumSize(new Dimension(30, 15));
		lblOption_5.setPreferredSize(new Dimension(30, 15));
		lblOption_5.setHorizontalAlignment(SwingConstants.LEFT);
		
			GridBagConstraints gbc_lblOption_5 = new GridBagConstraints();
			gbc_lblOption_5.fill = GridBagConstraints.BOTH;
			gbc_lblOption_5.insets = new Insets(0, 0, 5, 5);
			gbc_lblOption_5.gridx = 0;
			gbc_lblOption_5.gridy = 6;
			datafields_pane.add(lblOption_5, gbc_lblOption_5);
		
		opt5 = new JRadioButton("");
		buttonGroup.add(opt5);
		clist.add(opt5);
		GridBagConstraints gbc_opt5 = new GridBagConstraints();
		gbc_opt5.fill = GridBagConstraints.BOTH;
		gbc_opt5.insets = new Insets(0, 0, 5, 5);
		gbc_opt5.gridx = 1;
		gbc_opt5.gridy = 6;
		datafields_pane.add(opt5, gbc_opt5);
		
		txtOpt5 = new JTextField();
		txtOpt5.setColumns(10);
		clist.add(txtOpt5);
		GridBagConstraints gbc_txtOpt5 = new GridBagConstraints();
		gbc_txtOpt5.insets = new Insets(0, 0, 5, 0);
		gbc_txtOpt5.fill = GridBagConstraints.BOTH;
		gbc_txtOpt5.gridx = 2;
		gbc_txtOpt5.gridy = 6;
		datafields_pane.add(txtOpt5, gbc_txtOpt5);
		
		
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 7;
		datafields_pane.add(lblKeywordsl, gbc_label);
		
		label_1 = new JLabel("-");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 0, 5);
		gbc_label_1.gridx = 1;
		gbc_label_1.gridy = 7;
		datafields_pane.add(label_1, gbc_label_1);
		
		GridBagConstraints gbc_txtTodowyrezana = new GridBagConstraints();
		gbc_txtTodowyrezana.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTodowyrezana.gridx = 2;
		gbc_txtTodowyrezana.gridy = 7;
		datafields_pane.add(txtKwords, gbc_txtTodowyrezana);
		
		
/*		GridBagConstraints gbc_labKWords = new GridBagConstraints();
		gbc_labKWords.fill = GridBagConstraints.BOTH;
		gbc_labKWords.insets = new Insets(0, 0, 0, 5);
		gbc_labKWords.gridx = 2;
		gbc_labKWords.gridy = 6;
		datafields_pane.add(lblKeywordsl, gbc_labKWords);
		
		GridBagConstraints gbc_labDummy = new GridBagConstraints();
		gbc_labDummy.fill = GridBagConstraints.BOTH;
		gbc_labDummy.insets = new Insets(0, 0, 0, 5);
		gbc_labDummy.gridx = 2;
		gbc_labDummy.gridy = 6;
		datafields_pane.add(new JLabel(" "), gbc_labDummy);
		
		
		
		GridBagConstraints gbc_txtKWords = new GridBagConstraints();
		gbc_txtKWords.fill = GridBagConstraints.BOTH;
		gbc_txtKWords.insets = new Insets(0, 0, 0, 5);
		gbc_txtKWords.gridx = 2;
		gbc_txtKWords.gridy = 6;
		datafields_pane.add(txtKwords, gbc_txtKWords); */
		
		
		

		
		
		bottom_pane = new JPanel();
		QBankTab.add(bottom_pane);
		bottom_pane.setLayout(new BoxLayout(bottom_pane, BoxLayout.Y_AXIS));
		
		editmode_pane = new JPanel();
		editmode_pane.add(lblSelmode);
		bottom_pane.add(editmode_pane);
		
		buttons_pane = new JPanel();
		bottom_pane.add(buttons_pane);
		buttons_pane.setLayout(new BoxLayout(buttons_pane, BoxLayout.X_AXIS));
		
		JButton btnApplyChanges = new JButton("Apply changes");
		buttons_pane.add(btnApplyChanges);
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
		
		btnNewQuestion = new JButton("New question");
		buttons_pane.add(btnNewQuestion);
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
		
		JButton btnExit = new JButton("Exit");
		buttons_pane.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnExit.setMnemonic('x');
		
		
	buttons_pane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnApplyChanges, btnNewQuestion, btnExit}));
	timer = new Timer(1500, this);
	
	
	
	/*
	 * ***********************************************************
	*/
		
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
		table_questionsshort.setModel(tablemodel_questions);

//		table.getColumnModel().getColumn(0).setPreferredWidth(100);
	//	table.getColumnModel().getColumn(1).setPreferredWidth(300);
		
		
	}
	
	void createShortTable()
	{
		table_questionsshort.getColumnModel().removeColumn(table_questionsshort.getColumnModel().getColumn(2)); // usuwanie z widoku - nie z modelu
		table_questionsshort.getColumnModel().removeColumn(table_questionsshort.getColumnModel().getColumn(2));
		table_questionsshort.getColumnModel().removeColumn(table_questionsshort.getColumnModel().getColumn(2));
		table_questionsshort.getColumnModel().removeColumn(table_questionsshort.getColumnModel().getColumn(2));
		table_questionsshort.getColumnModel().removeColumn(table_questionsshort.getColumnModel().getColumn(2));
		table_questionsshort.getColumnModel().removeColumn(table_questionsshort.getColumnModel().getColumn(2));
		table_questionsshort.getColumnModel().removeColumn(table_questionsshort.getColumnModel().getColumn(2));
		table_questionsshort.getColumnModel().removeColumn(table_questionsshort.getColumnModel().getColumn(2)); 
		table_questionsshort.getColumnModel().getColumn(0).setMaxWidth(40);
		refreshTbIds();
	}
	
	void refreshTbIds()
	{
		listmodel_tbids.clear();
		String serverResponse = sendAndReceive("testbank query *");
		if (serverResponse.startsWith("ERR")) return;
		System.out.println(serverResponse);
		List<String> tbidAsList = tokenize(serverResponse, semicolon);
		System.out.println(tbidAsList);
		for (String tbid : tbidAsList) listmodel_tbids.addElement(tbid);
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
		int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete question " + table_questions.getValueAt(table_questions.getSelectedRow(), 0), "Deleting a question", JOptionPane.YES_NO_OPTION);
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
	
	void switchToADifferentTestbank() {
		String serverResponse = sendAndReceive("testbank get " + listOfTbids.getSelectedValue());
		if (!serverResponse.startsWith("OK")) throw new ExamsException("Failed to get testbank data from server.");
		List<String> tbInfo = tokenize(serverResponse, Delimiter.PIPE);
		lblTbid.setText(tbInfo.get(1));
		
		serverResponse = sendAndReceive("testbank getitems " + listOfTbids.getSelectedValue());
		if (serverResponse.equals("ERR=NO_RECORDS_FOUND")) {
			JOptionPane.showMessageDialog(null, "Editing an empty testbank");
			listmodel.clear();
			return;
		}
		System.out.println(serverResponse);
		if (!serverResponse.startsWith("OK")) throw new ExamsException("Something went wrong with fetching testbank data from server");
		tbInfo.clear();
		tbInfo = tokenize(serverResponse, Delimiter.SEMICOLON);
		listmodel.clear();
		tbInfo.stream().forEach(x -> listmodel.addElement(x));
	}
	
	void updateTestbank() {
		List<String> localList = IntStream.rangeClosed(0, listmodel.getSize()-1).mapToObj(x -> (String)listmodel.getElementAt(x)).collect(Collectors.toList());
		List<String> serverList = new ArrayList<>();
		List<String> intersectionList = new ArrayList<>();
		String intersectionString;
		String serverResponse = new String();
		
			serverResponse = sendAndReceive("testbank getitems " + listOfTbids.getSelectedValue());
			if (serverResponse.startsWith("OK")) {
				intersectionList = tokenize(serverResponse, Delimiter.SEMICOLON);
				System.out.println(intersectionList);
				serverList.addAll(intersectionList); // serverlist must be effectively final for lambdas... silly.
			}
			
			System.out.println("Server list: " + serverList);
			intersectionList = serverList.stream().filter(x -> !localList.contains(x)).collect(Collectors.toList());
			System.out.println("To delete:" + intersectionList);
			if (!intersectionList.isEmpty()) {
				intersectionString = String.join(";", intersectionList);
				serverResponse = sendAndReceiveMultiline("testbank removeitems " + listOfTbids.getSelectedValue() + " {" + intersectionString + "}");
				if (!serverResponse.startsWith("OK")) throw new ExamsException("Oops... Something went wrong while deleting records from the server.");
			}
			
			
			intersectionList=localList.stream().filter(x -> !serverList.contains(x)).collect(Collectors.toList());
			System.out.println("To add:" + intersectionList);
			if (!intersectionList.isEmpty()) {
				intersectionString = String.join(";", intersectionList);
				System.out.println("testbank additems " + listOfTbids.getSelectedValue() + " {" + intersectionString + "}");
				serverResponse = sendAndReceiveMultiline("testbank additems " + listOfTbids.getSelectedValue() + " {" + intersectionString + "}");
				if (!serverResponse.startsWith("OK")) throw new ExamsException("Oops... Something went wrong while deleting records from the server.");
			}
			System.out.println("Looks like a happy end?");
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (QBankTab.isVisible() || TBankTab.isVisible()) {
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
