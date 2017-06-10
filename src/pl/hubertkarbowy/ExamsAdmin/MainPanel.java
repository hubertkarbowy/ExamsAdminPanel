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
		setBounds(100, 100, 624, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel ExaminerMenu = new JPanel();
		contentPane.add(ExaminerMenu, "name_6612826238745");
		ExaminerMenu.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome to the Administrator Panel!");
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
		btnQuit.setBounds(483, 354, 117, 25);
		ExaminerMenu.add(btnQuit);
		
		JButton btnManageExams = new JButton("Create or edit exams");
		// JPanel spanel - zdefiniowany na gorze
		
		
		btnManageExams.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					spanel = new ExamsManagerPanel();
				}
				catch (ExamsException e2)
				{
					JOptionPane.showMessageDialog(null, "Cos sie stalo niedobrego..."+e2.getMessage());
				}
				if (spanel != null) {
				prevWindowQueue.peek().setVisible(false);
				prevWindowQueue.offer(spanel);
				spanel.setLocationRelativeTo(null);
				spanel.setVisible(true);
				}
			}
		});
		btnManageExams.setMnemonic('c');
		btnManageExams.setIcon(new ImageIcon(MainPanel.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
		btnManageExams.setBounds(22, 49, 269, 53);
		ExaminerMenu.add(btnManageExams);
		
		JButton btnSubmissions = new JButton("Submissions and grading");
		btnSubmissions.setMnemonic('g');
		btnSubmissions.setIcon(new ImageIcon(MainPanel.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnSubmissions.setBounds(22, 124, 269, 53);
		ExaminerMenu.add(btnSubmissions);
		
		JButton btnManageTestbanks = new JButton("Manage testbanks");
		btnManageTestbanks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spanel = new TestbanksManager();
				if (spanel == null) throw new ExamsException("Hmm...");
				prevWindowQueue.peek().setVisible(false);
				prevWindowQueue.offer(spanel);
				spanel.setLocationRelativeTo(null);
				spanel.setVisible(true);
			}
		});
		btnManageTestbanks.setMnemonic('t');
		btnManageTestbanks.setIcon(new ImageIcon(MainPanel.class.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
		btnManageTestbanks.setBounds(22, 198, 269, 53);
		ExaminerMenu.add(btnManageTestbanks);
		
		JButton btnUsers = new JButton("User accounts");
		btnUsers.setIcon(new ImageIcon(MainPanel.class.getResource("/com/sun/java/swing/plaf/windows/icons/DetailsView.gif")));
		btnUsers.setMnemonic('u');
		btnUsers.setBounds(22, 275, 269, 53);
		ExaminerMenu.add(btnUsers);
		
		JButton btnScheduleExams = new JButton("Schedule exams for groups");
		btnScheduleExams.setMnemonic('s');
		btnScheduleExams.setBounds(319, 49, 269, 53);
		ExaminerMenu.add(btnScheduleExams);
		
		JButton btnQuickHelp = new JButton("Quick help");
		btnQuickHelp.setMnemonic('h');
		btnQuickHelp.setBounds(319, 275, 269, 53);
		ExaminerMenu.add(btnQuickHelp);
		
		JPanel ExaminerTestItems = new JPanel();
		contentPane.add(ExaminerTestItems, "name_6616537042625");
		ExaminerTestItems.setLayout(null);
		
		JPanel ExaminerTestbanks = new JPanel();
		contentPane.add(ExaminerTestbanks, "name_6619303613000");
	}
}
