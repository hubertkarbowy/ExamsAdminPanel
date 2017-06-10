package pl.hubertkarbowy.ExamsAdmin;

import java.io.*;
import java.net.*;
import java.util.ArrayDeque;
import java.util.Queue;

import static pl.hubertkarbowy.ExamsAdmin.ExamsAdmin.*;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

class ExamsGlobalSettings {
	
	protected static final int PORTNO=55110;
	private static String uid;
	private static char[] pw = null;
	
	private static InetAddress serverAddr;
	private static Socket sock;
	private static BufferedReader sockRead;
	private static PrintWriter sockWrite;
	
	protected static JDialog prevWindow = null;
	protected static Queue<JDialog> prevWindowQueue = new ArrayDeque<>();
	
	private static final ExamsGlobalSettings instance = new ExamsGlobalSettings();
	
	public static ExamsGlobalSettings getMutableInstance(String userid, char[] passw, String serverAddr)
	{
		uid=userid;
		pw = new char[passw.length];
		for (int i=0; i<passw.length; i++) pw[i]=passw[i];
		// System.arraycopy(passw, 0, pw, 0, passw.length);
		System.out.println(new String(passw));
		try
		{
			 connectToServer(serverAddr);
			 // sockRead=new BufferedReader(new InputStreamReader(sock.getInputStream()));
			 // sockWrite=new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(frmExamsOnline, "Error " + e.getMessage());
			return null;
		}
		
		return instance;
	}
	
	public static ExamsGlobalSettings getMutableInstance()
	{
		return instance;
	}
	
	public static BufferedReader getSockRead() {
		return sockRead;
	}

	public static PrintWriter getSockWrite() {
		return sockWrite;
	}

	private static void connectToServer (String passedAddr) throws ExamsException
	{
		if (uid==null || pw==null || uid.equals("") || pw.equals("")) throw new ExamsException("Please enter a server address, user id and password.");
		if (sock!=null)
			if (sock.isConnected())
				if (!sock.isClosed()) 
					try {
						sock.close();
					}
					catch (IOException e)
					{
						throw new ExamsException("Error - cannot close connection.");
					}
		
		try {
			System.setProperty("line.separator", "\r\n");
			String authInput;
			String pwAsStr = new String(pw);
			setIP(passedAddr);
			// sock = new Socket(serverAddr, PORTNO);
			sock = new Socket();
			sock.connect(new InetSocketAddress(serverAddr,PORTNO), 2000);
			sockRead=new BufferedReader(new InputStreamReader(sock.getInputStream()));
			sockWrite=new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), true);
			authInput=sockRead.readLine();
			System.out.print(authInput);
			if (!authInput.equals("Tester app - welcome")) throw new ExamsException("Protocol error welcome.");
			System.out.println("2nd");
			sockWrite.println("uname="+uid);
		//	sockWrite.flush();
			System.out.println("3rd");
			authInput=sockRead.readLine();
			System.out.println("4th");
			System.out.print(authInput);
			if (!authInput.equals("OK")) throw new ExamsException("Protocol error uid.");
			System.out.println("Len: " + pwAsStr.length());
			sockWrite.println("pass="+new String(pw));
			authInput=sockRead.readLine();
			System.out.print(authInput);
			if (!authInput.equals("OK")) throw new ExamsException("Protocol error pw.");
			System.out.print(authInput);
			if (sockRead.read() != '>') throw new ExamsException("Protocol error first >");
			System.out.print(authInput);
			if (sockRead.read() != '>') throw new ExamsException("Protocol error second >");
			if (sockRead.read() != ' ') throw new ExamsException("Protocol error second space");
			System.out.print(authInput);
		}
		catch (UnknownHostException e)
		{
			// JOptionPane.showMessageDialog(frmExamsOnline, "Error - cannot find the specified IP address.");
			throw new ExamsException("Error - cannot find the specified IP address.");
		}
		catch (IOException e)
		{
			// JOptionPane.showMessageDialog(frmExamsOnline, "Something went wrong...");
			throw new ExamsException("Error - cannot open connection");
		}
	}
	
	private static void setIP(String ipAddr) throws UnknownHostException
	{
			serverAddr=InetAddress.getByName(ipAddr);
	}
	
	protected static String sendAndReceive(String msg) throws ExamsException
	{	
		String response = "";
		if (!sock.isConnected()) throw new ExamsException("<html>Error - cannot write to socket!<br>It is <b>highly</b> recommended that you stop doing whatever you are doing and reestablish the connection.</html>");	
		else sockWrite.println(msg);
		try {
			response = sockRead.readLine();
			sockRead.read(); sockRead.read(); sockRead.read(); // for reading >>
		}
		catch (IOException e)
		{
			throw new ExamsException("Error - cannot read server reply. How strange...");
		}
		return response;
	}
	
	protected static void showMsg(String msg)
	{
		JOptionPane.showMessageDialog(frmExamsOnline, msg);
	}
	
	protected static String getUid()
	{
		return uid;
	}
}
