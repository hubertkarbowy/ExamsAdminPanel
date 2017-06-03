package pl.hubertkarbowy.ExamsAdmin;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.prevWindowQueue;

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
import java.awt.Color;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.awt.Dialog.ModalityType;
import javax.swing.JComboBox;

public class AddNewExam extends JDialog {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JScrollPane spDesc;

	/**
	 * Launch the application.
	 */
	/* public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddNewExam frame = new AddNewExam();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

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
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
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
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(171, 12, 503, 19);
		panel.add(textField);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(171, 35, 503, 19);
		panel.add(textField_1);
		
		JLabel label_1 = new JLabel("Exam name:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(57, 37, 109, 15);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("Course name:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(57, 59, 109, 15);
		panel.add(label_2);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(171, 57, 503, 19);
		panel.add(textField_2);
		
		JLabel label_3 = new JLabel("Exam description:");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(26, 145, 140, 15);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("Exam scope:");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setBounds(26, 220, 140, 15);
		panel.add(label_4);
		
		JTextArea ExamScope = new JTextArea();
		ExamScope.setBorder(null);
		ExamScope.setBounds(171, 174, 503, 56);
		// panel.add(ExamScope);
		
		JTextArea ExamDesc = new JTextArea();
		ExamDesc.setBorder(null);
		ExamDesc.setBounds(171, 87, 503, 75);
		
		spDesc = new JScrollPane(ExamDesc);
		spDesc.setBounds(171, 116, 503, 76);
		panel.add(spDesc);
		
		JScrollPane spScope = new JScrollPane(ExamScope);
		spScope.setBounds(171, 204, 503, 56);
		panel.add(spScope);
		
		JComboBox testbankselector = new JComboBox();
		testbankselector.setBounds(171, 85, 503, 19);
		panel.add(testbankselector);
		
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
}
