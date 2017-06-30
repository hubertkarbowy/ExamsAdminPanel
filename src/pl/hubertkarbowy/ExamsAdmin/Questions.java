package pl.hubertkarbowy.ExamsAdmin;
import static pl.hubertkarbowy.ExamsAdmin.ExamsGlobalSettings.*;
import static pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.tokenize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.hubertkarbowy.ExamsAdmin.StringUtilityMethods.Delimiter;
import pl.hubertkarbowy.ExamsAdmin.Testbanks.Testbank;



/**
 * Holds test items (questions) for the exam
 *
 */
final class Questions {

	/**
	 * List of question IDs created by the examiner
	 */
	private static List<Question> allQuestions = new ArrayList<>();
	/**
	 * List of questions owner by the examiner.
	 * Each sublist contains: question id, content, options, correct answer, keywords, owner
	 */
	private static List<List<String>> allQuestionsAsList = new ArrayList<>();
	
	/**
	 * Nested class holding a single question
	 * Two ways of instantiating are provided - via parameters (ugly, but legacy) and via a list (nice)
	 *
	 */
	protected class Question implements Comparable<Question>{
		int id;
		String question="";
		String opt1="";
		String opt2="";
		String opt3="";
		String opt4="";
		String opt5="";
		String correct="";
		String keywords="";
		String owner="";
		List<String> optionsList = new ArrayList<String>();
		
		/**
		 * Creates a new test item (question) to be stored in {@link #optionsList}
		 * @param id - question ID
		 * @param question - content
		 * @param opt1 - first answer
		 * @param opt2 - second answer
		 * @param opt3 - third answer
		 * @param opt4 - fourth answer
		 * @param opt5 - fifth answer
		 * @param correct - the number indicating which answer is correct
		 * @param keywords - for filtering in search
		 * @param owner - id of examiner who created this question
		 * @deprecated
		 */
		private Question(int id, String question, String opt1, String opt2, String opt3, String opt4, String opt5, String correct, String keywords, String owner) {
			this.id=id;
			this.question=question;
			this.opt1=opt1; optionsList.add(opt1);
			this.opt2=opt2; optionsList.add(opt2);
			this.opt3=opt3; optionsList.add(opt3);
			this.opt4=opt4; optionsList.add(opt4);
			this.opt5=opt5; optionsList.add(opt5);
			this.correct=correct;
			this.keywords=keywords;
			this.owner=owner;
		}
		
		/**
		 * Same as the other constructor, but arguments are passed as a list
		 * @param argsTuple - 
		 */
		private Question(List<String> argsTuple) {
			this.id=Integer.parseInt(argsTuple.get(0));
			this.question=argsTuple.get(1);
			this.opt1=argsTuple.get(2); optionsList.add(argsTuple.get(2));
			this.opt2=argsTuple.get(3); optionsList.add(argsTuple.get(3));
			this.opt3=argsTuple.get(4); optionsList.add(argsTuple.get(4));
			this.opt4=argsTuple.get(5); optionsList.add(argsTuple.get(5));
			this.opt5=argsTuple.get(6); optionsList.add(argsTuple.get(6));
			this.correct=argsTuple.get(7);
			this.keywords=argsTuple.get(8);
			this.owner=argsTuple.get(9);
		}

		protected int getId() {
			return id;
		}

		protected String getOwner() {
			return owner;
		}
		
		protected String getQuestion() {
			return question;
		}
		
		protected List<String> getOptions() {
			return optionsList;
		}
		
		protected String getCorrect() {
			return correct;
		}
		
		public int compareTo(Question other) {
			return this.id - other.id;
		}
		
		protected List<String> getAsStringList()
		{
			List<String> sa = new ArrayList<>();
			sa.add(Integer.toString(id));
			sa.add(question);
			sa.add(opt1);
			sa.add(opt2);
			sa.add(opt3);
			sa.add(opt4);
			sa.add(opt5);
			sa.add(correct);
			sa.add(keywords);
			sa.add(owner);
			return sa;
		}
		
	}
	
	/**
	 * Fetches questions list from the server
	 * @param filterString allows to filter the result by keywords. If null, returns all test items owned by the user.
	 * @throws ExamsException
	 */
	protected void populate(String filterString) throws ExamsException
	{
		String allQuestionsOfThisUser;
		List<String> allQuestionsOfThisUserAsList = new ArrayList<>();
		Delimiter semicolon = Delimiter.SEMICOLON;
		Delimiter pipe = Delimiter.PIPE;
		
		allQuestions.clear();
		
		if (filterString==null)	allQuestionsOfThisUser=sendAndReceive("testitem query *");
		else allQuestionsOfThisUser=sendAndReceive("testitem query " + filterString);
		if (allQuestionsOfThisUser.startsWith("ERR")) {
			allQuestionsOfThisUser="OK={}";
			// if (!allQuestionsOfThisUser.startsWith("OK=")) throw new ExamsException("Unable to retrieve the list of your questions.");
		}
		else 
			{
			if (!allQuestionsOfThisUser.startsWith("OK")) allQuestions.add(new Question(-99, "NEW", "opt1", "", "", "", "", "", "", getUid()));
		}
		
		allQuestionsOfThisUserAsList=tokenize(allQuestionsOfThisUser, semicolon);
		for (String singleQuestion : allQuestionsOfThisUserAsList) {
			String response = sendAndReceive("testitem get "+singleQuestion);
			// System.out.println(singleTestbank + " " + response);
			if (!response.startsWith("OK")) return; // {// throw new ExamsException("Invalid question format.");
			allQuestions.add(new Question(tokenize(response, pipe)));
			allQuestionsAsList.add(tokenize(response, pipe));
		}
		Collections.sort(allQuestions);
	}
	
	/**
	 * Does what it says...
	 * @return all questions as list
	 */
	protected static List<List<String>> getAllQuestionsAsList() {
		allQuestionsAsList.clear();
		for (Question q : allQuestions) {
			allQuestionsAsList.add(q.getAsStringList());
		}
		return allQuestionsAsList;
	}
}
