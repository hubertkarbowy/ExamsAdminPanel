package pl.hubertkarbowy.ExamsAdmin;

import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.sendAndReceive;
import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.*;

import java.util.*;

final class Testbanks {

	private List<Testbank> allTestbanks = new ArrayList<>();
	
	protected class Testbank implements Comparable<Testbank>{
		String id;
		String name;
		String owner;
		
		private Testbank(String id, String name, String owner) {
			this.id=id;
			this.name=name;
			this.owner=owner;
		}
		
		private Testbank(List<String> argsTuple) {
			this.id=argsTuple.get(0);
			this.name=argsTuple.get(1);
			this.owner=argsTuple.get(2);
		}

		protected String getId() {
			return id;
		}

		protected String getName() {
			return name;
		}

		protected String getOwner() {
			return owner;
		}
		
		public int compareTo(Testbank other) {
			return this.name.compareToIgnoreCase(other.name);
		}
		
	}
	
	protected void populate() throws ExamsException
	{
		String allTestbankCodes;
		List<String> allTestbankCodesAsList = new ArrayList<>();
		Delimiter semicolon = Delimiter.SEMICOLON;
		Delimiter pipe = Delimiter.PIPE;
		
		allTestbanks.clear();
		
		allTestbankCodes=sendAndReceive("testbank query *");
		if (!allTestbankCodes.startsWith("ERR=NO_RECORDS_FOUND")) {
			if (!allTestbankCodes.startsWith("OK=")) throw new ExamsException("Unable to retrieve the list of your testbanks.");
		}
		else return;
		
		allTestbankCodesAsList=tokenize(allTestbankCodes, semicolon);
		for (String singleTestbank : allTestbankCodesAsList) {
			String response = sendAndReceive("testbank get "+singleTestbank);
			// System.out.println(singleTestbank + " " + response);
			if (!response.startsWith("OK=")) throw new ExamsException("Invalid testbank format.");
			allTestbanks.add(new Testbank(tokenize(response, pipe)));
		}
	}
	
	protected final Map<String, List<String>> getAsMap() {
		Map<String, List<String>> map = new HashMap<>();
		for (Testbank t: allTestbanks)
		{
			List<String> args2 = new ArrayList<>();
			args2.add(t.getId());
			args2.add(t.getName());
			args2.add(t.getOwner());
			map.put(t.getId(), args2);
		}
		return map;
		
	}
	
	protected final List<Testbank> getTestbanks() {
		return allTestbanks;
	}
	
}
