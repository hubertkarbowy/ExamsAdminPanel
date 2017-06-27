package pl.hubertkarbowy.ExamsStudent;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import static pl.hubertkarbowy.ExamsStudent.ExamsGlobalSettings.*;
import static pl.hubertkarbowy.ExamsStudent.StringUtilityMethods.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import java.awt.Color;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class StudentTestScreen extends JDialog {

	private final JPanel contentPanel = new JPanel();
	List<List<String>> questionsList = new ArrayList<>();
	private JLabel questionCounter;
	private JButton button;
	private JButton btnNext;
	private Map<String,String> answers = new HashMap<>();
	private JTextArea questionContent;
	private int currentQ=1;
	private ButtonGroup ansGrp;
	private JRadioButton ansA;
	private JRadioButton ansB;
	private JRadioButton ansC;
	private JRadioButton ansD;
	private JRadioButton ansE;
	private JLabel qid;
	private String examCode;
	
	

	public StudentTestScreen(String examcode) {
		setBounds(100, 100, 827, 546);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		this.examCode = examcode;
		questionCounter = new JLabel("1");
		questionCounter.setBounds(743, 12, 70, 15);
		contentPanel.add(questionCounter);
		
		questionsList=sendReceiveAndDeserialize("exam take "+examcode);
		questionCounter.setText(""+currentQ+" / "+questionsList.size());
		for (List<String> item : questionsList) answers.put(item.get(0), "0");
		
		questionContent = new JTextArea();
		questionContent.setFont(new Font("Dialog", Font.BOLD, 16));
		questionContent.setBackground(UIManager.getColor("Button.background"));
		questionContent.setBorder(null);
		questionContent.setBounds(24, 39, 789, 83);
		contentPanel.add(questionContent);
		questionContent.setLineWrap(true);
		questionContent.setEditable(false);
		
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLocation(0, 476);
		contentPanel.add(buttonPane);
		buttonPane.setSize(new Dimension(825, 41));
		buttonPane.setLayout(null);
		
			JButton okButton = new JButton("Submit");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(null, "Are you sure you want to submit this exam?", "Confirm", JOptionPane.YES_NO_OPTION)==0) submitExam();
				}
			});
			okButton.setBounds(651, 5, 83, 25);
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					prevWindowQueue.poll().setVisible(true);
					dispose();
				}
			});
			cancelButton.setBounds(739, 5, 81, 25);
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
			
			button = new JButton("<< Prev");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (currentQ <=1) return;
					currentQ--;
					questionCounter.setText(""+currentQ+" / "+questionsList.size());
					setQuestionID(currentQ);
				}
			});
			button.setBounds(26, 5, 90, 25);
			buttonPane.add(button);
			
			btnNext = new JButton("Next >>");
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (currentQ >= questionsList.size()) return;
					currentQ++;
					questionCounter.setText(""+currentQ+" / "+questionsList.size());
					setQuestionID(currentQ);
				}
			});
			btnNext.setBounds(128, 5, 90, 25);
			buttonPane.add(btnNext);
			
			
			ansGrp = new ButtonGroup();
			ansA = new JRadioButton("AnswerA");
			ansA.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (ansA.isSelected()) answers.put(qid.getText(), "1");
				}
			});
			ansA.setBounds(44, 162, 750, 47);
			contentPanel.add(ansA);
			
			
			ansB = new JRadioButton("AnswerB");
			ansB.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (ansB.isSelected()) answers.put(qid.getText(), "2");
				}
			});
			ansB.setBounds(44, 201, 750, 47);
			contentPanel.add(ansB);
			
			ansC = new JRadioButton("AnswerC");
			ansC.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (ansC.isSelected()) answers.put(qid.getText(), "3");
				}
			});
			ansC.setBounds(44, 241, 750, 47);
			contentPanel.add(ansC);
			
			ansD = new JRadioButton("AnswerD");
			ansD.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (ansD.isSelected()) answers.put(qid.getText(), "4");
				}
			});
			ansD.setBounds(44, 281, 750, 47);
			contentPanel.add(ansD);
			
			ansE = new JRadioButton("AnswerE");
			ansE.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (ansD.isSelected()) answers.put(qid.getText(), "5");
				}
			});
			ansE.setBounds(44, 321, 750, 47);
			contentPanel.add(ansE);
			
			ansGrp.add(ansA);
			ansGrp.add(ansB);
			ansGrp.add(ansC);
			ansGrp.add(ansD);
			ansGrp.add(ansE);
			
			
			qid = new JLabel("quid");
			qid.setVisible(false);
			qid.setBounds(724, 39, 70, 15);
			contentPanel.add(qid);
			
			setQuestionID(1);
	
	}
	
	private void setQuestionID(int id) {
		List<String> qToDisp = questionsList.get(currentQ-1);
		
		questionContent.setText(qToDisp.get(1));
		if (qToDisp.get(2).equals("")) ansA.setVisible(false); else {ansA.setText(qToDisp.get(2)); ansA.setVisible(true);}
		if (qToDisp.get(3).equals("")) ansB.setVisible(false); else {ansB.setText(qToDisp.get(3)); ansB.setVisible(true);}
		if (qToDisp.get(4).equals("")) ansC.setVisible(false); else {ansC.setText(qToDisp.get(4)); ansC.setVisible(true);}
		if (qToDisp.get(5).equals("")) ansD.setVisible(false); else {ansD.setText(qToDisp.get(5)); ansD.setVisible(true);}
		if (qToDisp.get(6).equals("")) ansE.setVisible(false); else {ansE.setText(qToDisp.get(5)); ansE.setVisible(true);}
		
		qid.setText(qToDisp.get(0));
		// jesli nie ma w mapie to 
		  ansGrp.clearSelection();
		
		 if (answers.get(qid.getText()).equals("1")) ansA.setSelected(true);
		 else if (answers.get(qid.getText()).equals("2")) ansB.setSelected(true);
		 else if (answers.get(qid.getText()).equals("3")) ansC.setSelected(true);
		 else if (answers.get(qid.getText()).equals("4")) ansD.setSelected(true);
		 else if (answers.get(qid.getText()).equals("5")) ansE.setSelected(true);
		 
		 
		
		//ustaw tez niewidzialna etykiete z QID z bazy -DONE
	}
	
	private void submitExam() {
		String servResp;
		StringBuilder sb = new StringBuilder();
		
		sb.append("exam submit " + examCode + " {");
		answers.forEach( (k,v) -> sb.append(k+";"+v+"|"));
		sb.deleteCharAt(sb.length()-1); // remove final pipe
		sb.append("}");
		System.out.println(sb.toString());
		servResp=sendAndReceive(sb.toString());
		if (servResp.startsWith("OK")) showMsg("Successfully submitted the exam");
		else showMsg("Error submitting the exam: "+servResp);
		
	}
}
