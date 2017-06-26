package pl.hubertkarbowy.ExamsStudent;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.*;
import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ExamsStudent extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtServer;
	private JTextField txtUserId;
	private JPasswordField pwdBaz;
	
	protected static ExamsGlobalSettings gs;
	protected JDialog mainPanel;
	protected JDialog loginwindow = this;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		        @Override
		        public void uncaughtException(Thread t, Throwable e) {
		        	JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
		        }
		    });
			ExamsStudent dialog = new ExamsStudent();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ExamsStudent() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
			JLabel lblExamsTool = new JLabel("Exams Student");
			lblExamsTool.setBounds(93, 12, 266, 18);
			lblExamsTool.setHorizontalAlignment(SwingConstants.CENTER);
			lblExamsTool.setFont(new Font("Dialog", Font.BOLD, 15));
			contentPanel.add(lblExamsTool);
		
		
			JLabel lblExamsServer = new JLabel("Exams server:");
			lblExamsServer.setBounds(36, 63, 105, 15);
			contentPanel.add(lblExamsServer);
		
		
			txtServer = new JTextField();
			txtServer.setBounds(148, 61, 114, 19);
			// txtServer.setText("127.0.0.1");
			txtServer.setText("192.168.1.7");
			txtServer.setColumns(10);
			contentPanel.add(txtServer);
		
		
			JLabel label = new JLabel("Username:");
			label.setBounds(46, 40, 77, 15);
			contentPanel.add(label);
		
		
			txtUserId = new JTextField();
			txtUserId.setBounds(148, 38, 114, 19);
			txtUserId.setText("102");
			txtUserId.setColumns(10);
			contentPanel.add(txtUserId);
		
		
			JLabel label1 = new JLabel("Password");
			label1.setBounds(63, 90, 70, 15);
			contentPanel.add(label1);
		
		
			pwdBaz = new JPasswordField();
			pwdBaz.setBounds(148, 92, 114, 19);
			pwdBaz.setText("baz");
			contentPanel.add(pwdBaz);
		
		
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
				JButton button = new JButton("Login");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						gs=getMutableInstance(txtUserId.getText(), pwdBaz.getPassword(), txtServer.getText());
						if (gs==null) return;
						String servResp = sendAndReceive("user mygroups");
						enrolled = tokenize(servResp, Delimiter.SEMICOLON);
						
						mainPanel = new MainPanelStudent();
						mainPanel.setLocationRelativeTo(null);
						
						prevWindowQueue.offer(mainPanel);
						mainPanel.setVisible(true);
						loginwindow.setVisible(false);
					}
				});
				buttonPane.add(button);
				button.setMnemonic('l');
			
			
				JButton button1 = new JButton("Quit");
				buttonPane.add(button1);
				button1.setMnemonic('q');
			
		
		setLocationRelativeTo(null);
	}

}
