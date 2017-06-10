package pl.hubertkarbowy.ExamsAdmin;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.prevWindowQueue;
import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.sendAndReceive;
import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.showMsg;
import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.formatErrorNicely;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.Delimiter;
import pl.hubertkarbowy.ExamsAdmin.Testbanks.Testbank;

import java.awt.Color;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.awt.Dialog.ModalityType;
import javax.swing.JComboBox;

public class AddNewExam extends JDialog {

	private JPanel contentPane;
	private JTextField exname;
	private JTextField coursename;
	private JScrollPane spDesc;
	private JComboBox testbankSelector;
	private JTextArea examdesc;
	private JTextArea examscope;
	
	private String allExamCodes;
	private List<String> allExamCodesAsList = new ArrayList<>();
	private List<List<String>> completeContent = new ArrayList<>();
	private Testbanks allTestbanks;
	private List<Testbank> allTestbanksAsList = new ArrayList<>();
	
	private Delimiter semicolon = Delimiter.SEMICOLON;
	private Delimiter pipe = Delimiter.PIPE;
	private JTextField excode;

	/**
	 * Create the frame.
	 */
	public AddNewExam() {
		/* addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				prevWindowQueue.peek().setVisible(true);;
			}
		});*/
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("New exam");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 763, 456);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(50, 63, 686, 292);
		contentPane.add(panel);
		
		JLabel label = new JLabel("Exam code:");
		label.setBounds(87, 14, 79, 15);
		panel.add(label);
		
		excode = new JTextField();
		excode.setColumns(10);
		excode.setBounds(171, 12, 503, 19);
		panel.add(excode);
		
		exname = new JTextField();
		exname.setColumns(10);
		exname.setBounds(171, 35, 503, 19);
		panel.add(exname);
		
		JLabel label_1 = new JLabel("Exam name:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(57, 37, 109, 15);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("Course name:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(57, 59, 109, 15);
		panel.add(label_2);
		
		coursename = new JTextField();
		coursename.setColumns(10);
		coursename.setBounds(171, 57, 503, 19);
		panel.add(coursename);
		
		JLabel label_3 = new JLabel("Exam description:");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(26, 145, 140, 15);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("Exam scope:");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setBounds(26, 220, 140, 15);
		panel.add(label_4);
		
		examscope = new JTextArea();
		examscope.setBorder(null);
		examscope.setBounds(171, 174, 503, 56);
		// panel.add(ExamScope);
		
		examdesc = new JTextArea();
		examdesc.setBorder(null);
		examdesc.setBounds(171, 87, 503, 75);
		
		spDesc = new JScrollPane(examdesc);
		spDesc.setBounds(171, 116, 503, 76);
		panel.add(spDesc);
		
		JScrollPane spScope = new JScrollPane(examscope);
		spScope.setBounds(171, 204, 503, 56);
		panel.add(spScope);
		
		testbankSelector = new JComboBox();
		testbankSelector.setBounds(171, 85, 503, 19);
		panel.add(testbankSelector);
		
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
		
		JLabel label_5 = new JLabel("Testbank:");
		label_5.setHorizontalAlignment(SwingConstants.RIGHT);
		label_5.setBounds(57, 86, 109, 15);
		panel.add(label_5);
		
		
	//	scrollPane.add(ExamDesc);
		
		JLabel lblPlease = new JLabel("<html>Please complete the fields below to create a new exam. Clicking \"Submit\" will add a new exam to the server database.</html>");
		lblPlease.setHorizontalAlignment(SwingConstants.LEFT);
		lblPlease.setBounds(40, 20, 686, 31);
		contentPane.add(lblPlease);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbAddNewExam();
			}
		});
		btnSubmit.setBounds(40, 367, 117, 25);
		contentPane.add(btnSubmit);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(609, 367, 117, 25);
		contentPane.add(btnCancel);
	}
	
	private void dbAddNewExam()
	{
		String serverResponse = "";
		StringBuilder sb = new StringBuilder();
		
		if (excode.getText().equals("")) throw new ExamsException("Please provide exam code.");
		if (exname.getText().equals("")) throw new ExamsException("Please provide exam name.");
		if (testbankSelector.getSelectedItem().toString().equals("")) throw new ExamsException("Please select a testbank.");
		
		sb.append("exam new {");
		sb.append(excode.getText() + "|");
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
		sb.append(examdesc.getText() + "|");
		sb.append(examscope.getText() + "}");
		System.out.println(sb.toString());
		
		serverResponse = sendAndReceive(sb.toString());
		if (!serverResponse.startsWith("OK")) throw new ExamsException(formatErrorNicely(serverResponse));
		else showMsg("New exam added. @TODO: Autorefresh.");
		this.dispose();
	}
	
	
}
