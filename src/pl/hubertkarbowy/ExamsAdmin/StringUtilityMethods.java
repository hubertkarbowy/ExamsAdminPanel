package pl.hubertkarbowy.ExamsAdmin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

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
		// StringTokenizer token = new StringTokenizer(temp, delimiter.getDelimiter()); 
		
		// howmany=token.countTokens();
		// System.out.println("There are " + howmany + "tokens");
		// for (int i = 0; i < howmany; i++) parsed.add(token.nextToken());
	    parsed=Arrays.stream(temp.split("\\"+delimiter.getDelimiter())).collect(Collectors.toList());
	    // System.out.println(parsed);
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
		// String[] columnNames = {"Exam code", "Exam name"};
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
		};
		tm.setDataVector(data, columnNames);
		// tm.setColumnCount(2);
		// tm.setColumnIdentifiers(columnNames);
		
		return tm;
		
	}
	
	static String formatErrorNicely(String serverResponse)
	{
		return serverResponse;
	}
	
}
