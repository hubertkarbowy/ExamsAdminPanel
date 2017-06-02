package pl.hubertkarbowy.ExamsAdmin;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.prevWindowQueue;

class MainPanel extends JDialog {
	JDialog spanel=null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) { // to jest tylko dla wywolania testowego
		try {
			MainPanel frame = new MainPanel();
			prevWindowQueue.offer((JDialog) frame);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the frame.
	 */
	public MainPanel()  {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 589, 326);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel ExaminerMenu = new JPanel();
		contentPane.add(ExaminerMenu, "name_6612826238745");
		ExaminerMenu.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome to the Examiner Panel!");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 12, 553, 15);
		ExaminerMenu.add(lblNewLabel);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnQuit.setMnemonic('q');
		btnQuit.setBounds(448, 239, 117, 25);
		ExaminerMenu.add(btnQuit);
		
		JButton btnManageExams = new JButton("Create or schedule an exam");
		// JPanel spanel - zdefiniowany na gorze
		
		
		btnManageExams.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					spanel = new ExamsSchedulerPanel();
				}
				catch (ExamsException e2)
				{
					JOptionPane.showMessageDialog(null, "Cos sie stalo niedobrego..."+e2.getMessage());
				}
				if (spanel != null) {
				prevWindowQueue.peek().setVisible(false);
				prevWindowQueue.offer(spanel);
				spanel.setVisible(true);
				}
			}
		});
		btnManageExams.setMnemonic('s');
		btnManageExams.setIcon(new ImageIcon(MainPanel.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
		btnManageExams.setBounds(22, 49, 269, 53);
		ExaminerMenu.add(btnManageExams);
		
		JButton btnSubmissions = new JButton("Submissions and grading");
		btnSubmissions.setMnemonic('g');
		btnSubmissions.setIcon(new ImageIcon(MainPanel.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnSubmissions.setBounds(22, 124, 269, 53);
		ExaminerMenu.add(btnSubmissions);
		
		JButton btnManageTestbanks = new JButton("Manage testbanks");
		btnManageTestbanks.setMnemonic('t');
		btnManageTestbanks.setIcon(new ImageIcon(MainPanel.class.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
		btnManageTestbanks.setBounds(22, 198, 269, 53);
		ExaminerMenu.add(btnManageTestbanks);
		
		JPanel ExaminerTestItems = new JPanel();
		contentPane.add(ExaminerTestItems, "name_6616537042625");
		ExaminerTestItems.setLayout(null);
		
		JPanel ExaminerTestbanks = new JPanel();
		contentPane.add(ExaminerTestbanks, "name_6619303613000");
	}

}
