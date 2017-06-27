package pl.hubertkarbowy.ExamsStudent;

import java.io.*;
import java.net.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import static pl.hubertkarbowy.ExamsStudent.StringUtilityMethods.*;

public class ExamsGlobalSettings {
	
	protected static final int PORTNO=55110;
	private static String uid;
	private static String rights;
	private static char[] pw = null;
	public static List<String> enrolled = new ArrayList<>();
	
	private static InetAddress serverAddr;
	private static Socket sock;
	private static BufferedReader sockRead;
	private static PrintWriter sockWrite;
	
	protected static JDialog prevWindow = null;
	public static Queue<JDialog> prevWindowQueue = new ArrayDeque<>();
	
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
			JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
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
			sockWrite.println("user get " + uid);
			authInput = sockRead.readLine();
			if (authInput.contains("\\|examiner")) rights = "examiner";
			else if (authInput.contains("\\|admin")) rights = "admin";
			else rights = "student";
			sockRead.read(); sockRead.read(); sockRead.read();
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
	
	public static String sendAndReceive(String msg) throws ExamsException
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
	
	protected static String sendAndReceiveMultiline(String msg) throws ExamsException
	{	
		String response = "";
		StringBuilder sb = new StringBuilder();
		if (!sock.isConnected()) throw new ExamsException("<html>Error - cannot write to socket!<br>It is <b>highly</b> recommended that you stop doing whatever you are doing and reestablish the connection.</html>");	
		else sockWrite.println(msg);
		try {
			char buf[] = new char[5];
			int val=0;
			System.out.println("Reading from socket:");
			while ((val=sockRead.read())!=1) {
				if ((char)val=='>') { sockRead.read(); sockRead.read(); break;} // o jak brzyyyydko!
				System.out.print((char)val);
				sb.append((char)val);
			}
		}
		catch (IOException e)
		{
			throw new ExamsException("Error - cannot read server reply. How strange...");
		}
		return sb.toString();
	}
	
	protected static List<List<String>> sendReceiveAndDeserialize(String msg) throws ExamsException
	{	
		String response = "";
		StringBuilder sb = new StringBuilder();
		List<List<String>> localList = new ArrayList<>();
		if (!sock.isConnected()) throw new ExamsException("<html>Error - cannot write to socket!<br>It is <b>highly</b> recommended that you stop doing whatever you are doing and reestablish the connection.</html>");	
		else sockWrite.println(msg);
		try {
			char buf[] = new char[5];
			int val=0;
			System.out.println("Reading from socket:");
			sockRead.mark(10);
			sockRead.read(buf, 0, 3);
			if (buf[0]=='E' && buf[1]=='R' && buf[2]=='R') {
				sockRead.reset();
				response = sockRead.readLine();
				sockRead.read(); sockRead.read(); sockRead.read();
				throw new ExamsException(response);
			}
			sockRead.reset();
			sockRead.mark(10);
			while ((val=sockRead.read())!=1) {
				if ((char)val=='>') { sockRead.read(); sockRead.read(); break;} // o jak brzyyyydko!
				else {
					sockRead.reset();
					response = sockRead.readLine();
					if (response.startsWith("#$#$")) {sockRead.mark(3); continue;}
					localList.add(tokenize(response, Delimiter.PIPE, 0));
					
				//	sb.append(response);
					// sb.append((char)val);
					System.out.println(response);
					sockRead.mark(3);
				}
				
				
			}
		}
		catch (IOException e)
		{
			throw new ExamsException("Error - cannot read server reply. How strange...");
		}
		// return sb.toString();
		return localList;
	}
	
	protected static void showMsg(String msg)
	{
		JOptionPane.showMessageDialog(null, msg);
	}
	
	protected static String getUid()
	{
		return uid;
	}
}
