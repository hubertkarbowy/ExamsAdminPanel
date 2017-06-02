package pl.hubertkarbowy.ExamsAdmin;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JScrollPane;

import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.prevWindowQueue;
import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.*;


import java.util.*;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;

public class ExamsSchedulerPanel extends JDialog {

	private JPanel contentPane;
	private JTable table;
	private TableModel model;
	private JTextField excode;
	private JTextField exname;
	private JTextField coursename;
	private JScrollPane spTable;

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
	public ExamsSchedulerPanel() throws ExamsException
	{
		String temp;
		setModal(true);
		setTitle("My exams");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 705, 522);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel upperPanel = new JPanel();
		upperPanel.setBounds(5, 5, 686, 155);
		contentPane.add(upperPanel);
		
		
		
		temp=sendAndReceive("exam query *");
		JOptionPane.showMessageDialog(this, temp);
		if (!temp.startsWith("OK=")) throw new ExamsException("Error getting your exams.");
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		
		parseExams(temp, table);
		spTable = new JScrollPane(table);
		upperPanel.add(spTable);
	
		
		
		
		
		JButton btnNewExam = new JButton("New exam");
		btnNewExam.setHorizontalAlignment(SwingConstants.RIGHT);
		btnNewExam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog newExam = new AddNewExam();
				prevWindowQueue.peek().setVisible(false);
				newExam.setVisible(true);
			}
		});
		btnNewExam.setBounds(557, 56, 117, 25);
		upperPanel.add(btnNewExam);
		upperPanel.setVisible(true);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBounds(5, 434, 686, 47);
		contentPane.add(bottomPanel);
		bottomPanel.setLayout(null);
		
		JButton btnApplyChanges = new JButton("Apply changes");
		btnApplyChanges.setBounds(62, 12, 137, 25);
		bottomPanel.add(btnApplyChanges);
		
		JButton btnReturnToMain = new JButton("Exit without saving");
		btnReturnToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prevWindowQueue.peek().setVisible(true);
				dispose();
			}
		});
		btnReturnToMain.setBounds(482, 12, 175, 25);
		bottomPanel.add(btnReturnToMain);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 180, 686, 242);
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
		label_3.setBounds(26, 117, 140, 15);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("Exam scope:");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setBounds(26, 191, 140, 15);
		panel.add(label_4);
		
		JTextArea examdesc = new JTextArea();
		JScrollPane spExamDesc = new JScrollPane(examdesc);
		spExamDesc.setBounds(171, 86, 503, 76);
		panel.add(spExamDesc);
		
		JTextArea textArea = new JTextArea();
		JScrollPane spExamScope = new JScrollPane(textArea);
		spExamScope.setBounds(171, 174, 503, 56);
		panel.add(spExamScope);
		upperPanel.revalidate();
		upperPanel.repaint();
	}
	
	private void parseExams(String stringCode, JTable dest)
	{
		String temp;
		String response;
		java.util.List<String[]> values = new ArrayList<>(); 
		
		Object[][] vals2;
        int howmany;
        
        String[] columns = {"Exam code", "Exam name"};
		
		// do tego powinna byc utility method
        temp=stringCode.substring(3);
        temp=temp.replaceAll("[{|}]", "");
		StringTokenizer token = new StringTokenizer(temp, ";"); 
		
		howmany=token.countTokens();
		vals2 = new Object[howmany][];
		for (int i = 0; i < howmany; i++) vals2[i] = new Object[] {token.nextToken(), "TBC: Exam name"};
		
		
		
		table = new JTable(vals2, columns);
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(0).setWidth(300);
		
		// JOptionPane.showMessageDialog(this, temp+"tokens="+howmany);	
		
		
	} 
}
