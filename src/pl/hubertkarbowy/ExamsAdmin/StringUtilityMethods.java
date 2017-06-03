package pl.hubertkarbowy.ExamsAdmin;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
		StringTokenizer token = new StringTokenizer(temp, delimiter.getDelimiter()); 
		
		howmany=token.countTokens();
		for (int i = 0; i < howmany; i++) parsed.add(token.nextToken());
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
	
}
