package pl.hubertkarbowy.ExamsAdmin;

import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.createTableModel;
import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.createTableModelv2;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

final class StringUtilityMethods {
		
	protected static enum Delimiter {
	PIPE("|"), SEMICOLON(";"), TUPLE_PIPEANDSEMICOLON("");
	
	String delim;
	
	Delimiter(String d) { delim=d; }
	
	String getDelimiter() {	return delim; }
	}
	
	/**
	 * Returns an ArrayList which contains items parsed from tokens string. A delimiter is passed in the second parameter.
	 * 
	 * @param  tokens  a string enclosed in curly braces which contains tokens separated by delimiter
	 * @param  delimiter an enum indicating the type of delimiter used: PIPE, SEMICOLON or TUPLE_PIPESEMICOLON
	 * @return      the parsed ArrayList
	 */
	static List<String> tokenize (String tokens, Delimiter delimiter) {
		String temp;
		int howmany;
		List<String> parsed = new ArrayList<>();
		
		temp=tokens.substring(3);
	    temp=temp.replaceAll("[{}]", "");
	    parsed=Arrays.stream(temp.split("\\"+delimiter.getDelimiter())).collect(Collectors.toList());
		return parsed;
	}
	
	static Object[][] parseVals (String tokens, String[] columns, Delimiter delimiter) {
		String temp;
		Object[][] vals2;
        int howmany;

		
        temp=tokens.substring(3);
        temp=temp.replaceAll("[{}]", "");
		StringTokenizer token = new StringTokenizer(temp, delimiter.getDelimiter()); 
		
		howmany=token.countTokens();
		vals2 = new Object[howmany][];
		for (int i = 0; i < howmany; i++) vals2[i] = new Object[] {token.nextToken(), "TBC: Exam name"};	
				
		
		return vals2;
	}
	
	static Object[][] createTableModel (List<List<String>> listModel) {
		
		Object [][] vals2;
		int howmany;
		
		howmany = listModel.size();
		vals2 = new Object[howmany][];
		for (int i=0; i<howmany; i++) vals2[i] = listModel.get(i).toArray();
		
		return vals2;
	}
	
	static DefaultTableModel createTableModelv2 (String[] columnNames, List<List<String>> listModel) {
		Object[][] data =  createTableModel(listModel);
		
				
		DefaultTableModel tm = null;
		
		tm = new DefaultTableModel() {
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return data[rowIndex][columnIndex];
			}
			
			@Override
			public int getRowCount() {
				return data.length; 
			}
			
			@Override
			public int getColumnCount() {
				return columnNames.length;
			}
			
			@Override
			public void setValueAt(Object value, int row, int col)
			{
				data[row][col] = (String) value;
				fireTableCellUpdated(row, col);
			}
			
			@Override
			public void setRowCount(int rc) {
				fireTableRowsUpdated(0, 1);
			}
			
			public void removeRow(int rc) {
				fireTableDataChanged();
			}
			
			@Override
			public boolean isCellEditable(int row, int col)
	        { return false; }
		};
		tm.setDataVector(data, columnNames);
		System.out.println(tm.getDataVector());
		tm.setColumnCount(columnNames.length);
		tm.setColumnIdentifiers(columnNames);
		
		return tm;
		
	}
	
	static DefaultTableModel createTableModelv3 (String[] columnNames, List<List<String>> listModel) {
		Vector<Vector<String>> data = new Vector<>();
		for (List<String> singleEntry : listModel) data.addElement(new Vector<String>(singleEntry));
				
		DefaultTableModel tm = null;
		
		tm = new BetterTableModel() {
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return data.get(rowIndex).get(columnIndex);
			}
			
			@Override
			public int getRowCount() {
				return data.size(); 
			}
			
			@Override
			public int getColumnCount() {
				return columnNames.length;
			}
			
			@Override
			public void setValueAt(Object value, int row, int col)
			{
				data.get(row).set(col, (String) value);
				fireTableCellUpdated(row, col);
			}
			
			@Override
			public void setRowCount(int rc) {
				fireTableRowsUpdated(0, 1);
			}
			
			public void removeRow(int rc) {
				data.remove(rc);
				fireTableDataChanged();
			}
			
			public void addRow2(List<String> newrow) {
				data.add(new Vector<String>(newrow));
				fireTableDataChanged();
			}
			
			public void clear() {
				data.clear();
				fireTableDataChanged();
			}
			
			public void update(List<List<String>> newData) {
				data.clear();
				for (List<String> singleEntry : newData) data.addElement(new Vector<String>(singleEntry));
				fireTableDataChanged();
			}
			
			@Override
			public boolean isCellEditable(int row, int col)
	        { return false; }
			
		};
		tm.setDataVector(data, new Vector<String>(Arrays.asList(columnNames)));
		System.out.println(tm.getDataVector());
		tm.setColumnCount(columnNames.length);
		tm.setColumnIdentifiers(columnNames);
		
		return tm;
		
	}
	
	static DefaultListModel<String> createList(String[] items) {
		
		
		
		DefaultListModel<String> lm = new DefaultListModel<String>() {
			
		 
		};
		
		return lm;
		
	}
	
	static DefaultTableModel parseExamsv3(String[] columns, List<List<String>> columnsContent)
	{
		return createTableModelv3(columns, columnsContent);
	}

	
	static String formatErrorNicely(String serverResponse)
	{
		return serverResponse;
	}
	
}
