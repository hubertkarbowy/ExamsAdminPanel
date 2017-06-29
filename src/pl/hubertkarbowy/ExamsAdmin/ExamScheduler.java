package pl.hubertkarbowy.ExamsAdmin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import java.awt.Component;

public class ExamScheduler extends JDialog {

	
	public ExamScheduler() {
		setBounds(100, 100, 731, 512);
		getContentPane().setLayout(null);
		
			
		
		
			JScrollPane scrollPane = new JScrollPane((Component) null);
			scrollPane.setBounds(0, 0, 729, 177);
			getContentPane().add(scrollPane);
			scrollPane.setAlignmentY(0.0f);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 176, 729, 215);
		getContentPane().add(panel);
		panel.setLayout(null);
		
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 426, 729, 45);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				
					JButton cancelButton = new JButton("Cancel");
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
			
		
	}

}
