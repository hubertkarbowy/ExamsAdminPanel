package pl.hubertkarbowy.ExamsStudent;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

import static pl.hubertkarbowy.ExamsStudent.ExamsGlobalSettings.*;
import static pl.hubertkarbowy.ExamsStudent.StringUtilityMethods.*;

public class MainPanelStudent extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblWelcomeToThe;
	private JLabel lblHereAreThe;
	private JComboBox comboBox;
	private JButton btnNewButton;
	private List<String> myGroups = new ArrayList<>();
	private List<List<String>> scheduleList = new ArrayList<>(); 
	String servResp;
	private JLabel lblInvcode;
	private StudentTestScreen sts;
	private MainPanelStudent mps = this;


	/**
	 * Create the dialog.
	 */
	public MainPanelStudent() {
		setBounds(100, 100, 782, 259);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblWelcomeToThe = new JLabel("Welcome to the Student Exam Tool");
		lblWelcomeToThe.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeToThe.setFont(new Font("Dialog", Font.BOLD, 15));
		lblWelcomeToThe.setBounds(97, 12, 553, 15);
		contentPanel.add(lblWelcomeToThe);
		
		lblHereAreThe = new JLabel("Here are the exams you can take or view:");
		lblHereAreThe.setBounds(214, 58, 436, 15);
		contentPanel.add(lblHereAreThe);
		
		comboBox = new JComboBox();
		comboBox.setBounds(40, 104, 469, 24);
		contentPanel.add(comboBox);
		
		btnNewButton = new JButton("Open this exam");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String excode="";
				for (List<String> l : scheduleList)
				{
					if (l.get(6).equals((String)comboBox.getSelectedItem())) {
						excode = l.get(1);
						break;
					}
				}
				if (!excode.equals("")) {
					sts = new StudentTestScreen(excode);
					sts.setLocationRelativeTo(null);
					prevWindowQueue.add(mps);
					mps.setVisible(false);
					sts.setVisible(true);
				}
				
			}
		});
		btnNewButton.setBounds(563, 104, 156, 25);
		contentPanel.add(btnNewButton);
		
		lblInvcode = new JLabel("invCode");
		lblInvcode.setVisible(false);
		lblInvcode.setBounds(642, 12, 70, 15);
		contentPanel.add(lblInvcode);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Quit");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				cancelButton.setMnemonic('q');
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		servResp = sendAndReceive("user mygroups");
		if (servResp.startsWith("ERR=")) showMsg("There are no scheduled exams for you.");
		else {
			myGroups = tokenize(servResp, Delimiter.SEMICOLON);
			System.out.println(myGroups);
			for (String g : myGroups) {
				scheduleList.addAll(sendReceiveAndDeserialize("exam getschedulebygroup "+g));	
			}
			for (List<String> l : scheduleList) comboBox.addItem(l.get(6));
		}
		System.out.println("SL = " + scheduleList);
		
		
	}
}