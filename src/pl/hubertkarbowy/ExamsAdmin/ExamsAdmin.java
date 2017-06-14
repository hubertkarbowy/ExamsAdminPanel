package pl.hubertkarbowy.ExamsAdmin;

// import java.awt.EventQueue;
// komentarz dodany 

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.getMutableInstance;
import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.prevWindowQueue;

public class ExamsAdmin {

	protected static JFrame frmExamsOnline;
	protected static ExamsGlobalSettings gs;
	private JLabel lblExamsServer;
	private JTextField txtServer;
	private JLabel lblUsername;
	private JTextField txtUserId;
	private JLabel lblPassword;
	private JPasswordField passwordField;
	
	protected MainPanel mainPanel;
	private JButton btnTime;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
	        public void run() {
				try {
					ExamsAdmin window = new ExamsAdmin();
					window.frmExamsOnline.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	        });
	}

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public ExamsAdmin() throws Exception {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws Exception {
		frmExamsOnline = new JFrame();
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
	        @Override
	        public void uncaughtException(Thread t, Throwable e) {
	        	JOptionPane.showMessageDialog(frmExamsOnline, "Error: " + e.getMessage());
	        }
	    });
		
		frmExamsOnline.setTitle("Exams Online - Admin / Examiner panel");
		frmExamsOnline.setBounds(100, 100, 450, 300);
		frmExamsOnline.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmExamsOnline.getContentPane().setLayout(null);
		frmExamsOnline.setLocationRelativeTo(null);
		
		JLabel lblExamsAdmin = new JLabel("Exams - Admin / Examiner Panel");
		lblExamsAdmin.setFont(new Font("Dialog", Font.BOLD, 15));
		lblExamsAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		lblExamsAdmin.setBounds(12, 12, 416, 15);
		frmExamsOnline.getContentPane().add(lblExamsAdmin);
		
		lblExamsServer = new JLabel("Exams xserver:");
		lblExamsServer.setBounds(30, 67, 117, 15);
		frmExamsOnline.getContentPane().add(lblExamsServer);
		
		txtServer = new JTextField();
		txtServer.setText("127.0.0.1");
		txtServer.setBounds(149, 65, 114, 19);
		frmExamsOnline.getContentPane().add(txtServer);
		txtServer.setColumns(10);
		
		lblUsername = new JLabel("Username:");
		lblUsername.setBounds(30, 94, 117, 15);
		frmExamsOnline.getContentPane().add(lblUsername);
		
		txtUserId = new JTextField();
		txtUserId.setText("1");
		txtUserId.setColumns(10);
		txtUserId.setBounds(149, 92, 114, 19);
		frmExamsOnline.getContentPane().add(txtUserId);
		
		lblPassword = new JLabel("Password");
		lblPassword.setBounds(30, 121, 117, 15);
		frmExamsOnline.getContentPane().add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(149, 119, 114, 19);
		passwordField.setText("foo");
		frmExamsOnline.getContentPane().add(passwordField);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gs=getMutableInstance(txtUserId.getText(), passwordField.getPassword(), txtServer.getText());
				if (gs==null) return;
				mainPanel = new MainPanel();
				mainPanel.setLocationRelativeTo(null);
				prevWindowQueue.offer(mainPanel);
				mainPanel.setVisible(true);
				frmExamsOnline.setVisible(false);
				
			}
		});
		btnLogin.setMnemonic('l');
		btnLogin.setBounds(311, 208, 117, 25);
		frmExamsOnline.getContentPane().add(btnLogin);
		
		JButton btnquit = new JButton("Quit");
		btnquit.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.exit(0);
			}
		});
		btnquit.setMnemonic('q');
		btnquit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		btnquit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnquit.setBounds(30, 208, 117, 25);
		frmExamsOnline.getContentPane().add(btnquit);
		
		btnTime = new JButton("Time");
		btnTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gs=getMutableInstance("bzz", "bzzz".toCharArray(), "localhost");
				// gs=getMutableInstance();
			}
		});
		btnTime.setBounds(162, 166, 117, 25);
		frmExamsOnline.getContentPane().add(btnTime);
	}
}
