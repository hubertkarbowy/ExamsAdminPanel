package pl.hubertkarbowy.ExamsAdmin;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.prevWindowQueue;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;

public class TestbanksManager extends JDialog {
	private JPanel panel;
	private JTextField qid;
	private JTextField qtext;
	private JTextField textField;
	
	
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
		
		JPanel TBankTab = new JPanel();
		viewSwitcher.addTab("Testbanks", null, TBankTab, null);
		
		JPanel QBankTab = new JPanel();
		viewSwitcher.addTab("Questions bank", null, QBankTab, null);
		QBankTab.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 745, 200);
		QBankTab.add(scrollPane);
		
		JButton btnExit = new JButton("Exit");
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
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(162, 273, 595, 19);
		QBankTab.add(textField);
		
		JLabel lblOption = new JLabel("Option 1:");
		lblOption.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOption.setBounds(22, 271, 108, 15);
		QBankTab.add(lblOption);
		
		JRadioButton rdbtnZzz = new JRadioButton("zzz");
		rdbtnZzz.setBounds(143, 300, 149, 23);
		QBankTab.add(rdbtnZzz);
		
		// TabsPane.addTab("Questionsbank", (Component)new JPanel(false));
		
	}
}
