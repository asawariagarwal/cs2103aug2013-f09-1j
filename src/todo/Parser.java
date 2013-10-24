package todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import todo.Command;
import todo.ViewCommand;
import todo.AddCommand;
import todo.DeleteCommand;
import todo.ModifyCommand;

/**
 * This class parses the user input to convert it into a command type object
 * 
 * @author:Asawari
 */

public class Parser {

	/**
	 * Command Type Keywords
	 */

	private static final String ADD = "add";
	private static final String VIEW = "view";
	private static final String DELETE = "delete";
	private static final String CHANGE = "change";
	private static final String RESCHEDULE = "reschedule";
	private static final String SEARCH="search";
	private static final String UNDO="undo";

	/**
	 * Stores the user input
	 */
	private String _userInput;

	/**
	 * Constructor to initialize the user input
	 */
	Parser() {
		_userInput = null;
	}
	
	Parser(String userInput) {
		_userInput = userInput;
	}
	
	private void setInput(String inputString) {
		_userInput = inputString;
	}
	
	/**
	 * This function does the main parsing - high level of abstraction
	 * 
	 * @return
	 */

	public Command parseInput(String inputString) {
		setInput(inputString);
		try {

			String commandTypeKeyword = getCommandType();

			switch (commandTypeKeyword) {
			case ADD:
				AddCommand addObj;
				addObj = parseAdd();
				return addObj;

			case VIEW:
				ViewCommand viewObj;
				viewObj = parseView();
				return viewObj;

			case DELETE:
				DeleteCommand delObj;
				delObj = parseDelete();
				return delObj;

			case CHANGE:
				ModifyCommand modObj;
				modObj = parseChange();
				return modObj;

			case RESCHEDULE:
				ModifyCommand resObj;
				resObj = parseReschedule();
				return resObj;
				
			case SEARCH:
				SearchCommand searchObj;
				searchObj = parseSearch();
				return searchObj;
				
			case UNDO:
				UndoCommand undoObj;
				undoObj = parseUndo();
				return undoObj;

			default:
				return null;

			}
		} catch (Exception e) {
			System.err.println("Invalid input: " + e.getMessage());
		}
		return null;

	}

	/**
	 * This function performs the actual parsing of a add type command and
	 * creates a add command type object
	 * 
	 * @return AddCommand type object
	 * @throws Parseexception
	 */

	private AddCommand parseAdd() throws ParseException {
		AddCommand command;
		String copyInput1 , copyInput2;
		copyInput1=_userInput;
		copyInput2=_userInput;
		command = parseDeadlineTask();
		if (command != null) {
			return command;
		}
		_userInput=copyInput1;
		command = parseTimedTask();
		if (command != null) {
			return command;
		}
		_userInput=copyInput2;
		command = parseFloatingTask();
		return command;
	}
	
	
	
	/**
	 * This function is used in case the Task is a deadline task and it parses
	 * the input to create a deadline type object
	 * 
	 * @return AddCommand type object or null
	 * @throws ParseException
	 */
	private AddCommand parseDeadlineTask() throws ParseException {

		ArrayList<String> hashtags = new ArrayList<String>();

		String TaskDes = extractTillKeyword(" by ");
		while (_userInput.contains(" by ")) {
			TaskDes= TaskDes.concat(" by ");
			TaskDes=TaskDes.concat(extractTillKeyword(" by "));
		}

		String dateStr;
		dateStr = extractTillHashtagOrEnd();
		if (isDateValid(dateStr)) {

			// The Following lines are used to create a calendar type object
			// using a
			// date string
			SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
			Date dateObj = curFormater.parse(dateStr);
			
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateObj);

			
			while (!(_userInput.equals(""))) {
				String hashtag=extractHashtag();
				if(hashtag.equals("")){
					return null;
				}
				hashtags.add(hashtag);
			}

			DeadlineTask TaskObj = new DeadlineTask(TaskDes, hashtags, calendar);

			AddCommand command = new AddCommand(TaskObj);

			return command;

		}
		return null;
	}
	
	/**
	 * This function is used in case the Task is a timed task and it parses the
	 * input to create a timed task type object
	 * 
	 * @return AddCommand type object or null
	 * @throws ParseException
	 */
	
	private AddCommand parseTimedTask() throws ParseException {
		// return true;
		ArrayList<String> hashtags = new ArrayList<String>();

		String TaskDes = extractTillKeyword(" from ");
		while (_userInput.contains(" from ")) {
			TaskDes=TaskDes.concat(" from ");
			TaskDes=TaskDes.concat(extractTillKeyword(" from "));
		}

		String date1, date2;

		date1 = extractTillKeyword("to");

		date2 = extractTillHashtagOrEnd();

		if ((isDateValid(date1)) && (isDateValid(date2))) {

			// The Following lines are used to create a calendar type objects
			// using
			// a date string
			SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
			Date dateObj = curFormater.parse(date1);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(dateObj);

			SimpleDateFormat curFormater2 = new SimpleDateFormat("dd/MM/yyyy");
			Date dateObj2 = curFormater2.parse(date2);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(dateObj2);

			while (!(_userInput.equals(""))) {
				String hashtag=extractHashtag();
				if(hashtag.equals("")){
					return null;
				}
				hashtags.add(hashtag);
			}
			
			
			TimedTask taskObj = new TimedTask(TaskDes, hashtags, calendar1,
					calendar2);
			AddCommand command = new AddCommand(taskObj);

			return command;

		}
		return null;

	}
	
	/**
	 * This function is used in case the task is a floating task and it parses
	 * the input to create a floating type object
	 * 
	 * @return AddCommand type object or null
	 */

	private AddCommand parseFloatingTask() {

		ArrayList<String> hashtags = new ArrayList<String>();
		String TaskDes = extractTillHashtagOrEnd();
		
		
		while (!(_userInput.equals(""))) {
			String hashtag=extractHashtag();
			if(hashtag.equals("")){
				return null;
			}
			hashtags.add(hashtag);
		}
		
		FloatingTask TaskObj = new FloatingTask(TaskDes, hashtags);
		AddCommand command = new AddCommand(TaskObj);
		return command;
	}
	

	
	/**
	 * This function performs the actual parsing of a view type command and
	 * creates a view command type object
	 * 
	 * @return ViewCommand type object or null
	 * @throws ParseException
	 */

	private ViewCommand parseView() throws ParseException {
		ViewCommand command;
		String dateStr[] = new String[1];
		dateStr[0] = "";
		if (isViewCommandDisplayingAllTasks()) {
			command = new ViewCommand(false, false);
		}else if (isViewCommandDisplayingAllTasksOrderedByTags()) {
			command = new ViewCommand(false,true);
		}else if (isViewCommandDisplayingFloatingTasks()) {
			command = new ViewCommand(true);
		}else if (isViewCommandDisplayingTimedTasks()) {
			command = new ViewCommand();//TODO: calls default constructor
		}else if (isViewCommandDisplayingDeadLineTasks()) {
			command = new ViewCommand();//TODO: calls default constructor
		}else if (isViewCommandDisplayingTasksWithHashtag()) {
			String hashtag = getHashtag();
			command = new ViewCommand(hashtag);
		}else if(isViewCommandDisplayingTasksOnADate(dateStr)){

			// The Following lines are used to create a calendar
			// type object using a date string
			SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
			Date dateObj = curFormater.parse(dateStr[0]);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateObj);

			command = new ViewCommand(calendar);
		}else if (isViewCommandDisplayingTasksOnADateOrderedByTags(dateStr)) {

			// The Following lines are used to create a calendar
			// type object using a date string
			SimpleDateFormat curFormater2 = new SimpleDateFormat("dd/MM/yyyy");
			Date dateObj2 = curFormater2.parse(dateStr[0]);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(dateObj2);

			command = new ViewCommand(calendar2);
		} else {
			return null;
		}
		return command;
	}

	/**
	 * This function performs the actual parsing of a delete type command and
	 * creates a delete command type object
	 * 
	 * @return DeleteCommand type object or null
	 * 
	 */

	private DeleteCommand parseDelete() {
		if (!_userInput.equals("")) {
			DeleteCommand command = new DeleteCommand(_userInput);
			return command;
		} else {
			return null;
		}
	}

	/**
	 * This function performs the actual parsing of a search type command and
	 * creates a search command type object
	 * 
	 * @return SearchCommand type object or null
	 * 
	 */
	
	private SearchCommand parseSearch(){
		if(!_userInput.equals("")){
			SearchCommand command = new SearchCommand(_userInput.trim());
			return command;
		}else{
			return null;
		}
	}
	
	/**
	 * This function performs the actual parsing of a change type command and
	 * creates a modify command type object
	 * 
	 * @return ModifyCommand type object
	 * 
	 */

	private ModifyCommand parseChange() {
		String oldTask, newTask;
		oldTask = extractTaskDescription().trim();
		newTask = _userInput.trim();
		ModifyCommand command = new ModifyCommand(oldTask, newTask);
		return command;
	}

	/**
	 * This function performs the actual parsing of a reschedule type command
	 * and creates a modify command type object
	 * 
	 * @return ModifyCommand type object or null
	 * @throws Parseexception
	 */

	private ModifyCommand parseReschedule() throws ParseException {
		String taskName ;
		taskName= extractTaskDescription().trim();
		String startDate, endDate;
		startDate = extractTillKeyword("till");
		endDate = _userInput.trim();

		if ((isDateValid(startDate)) && (isDateValid(endDate))) {

			// The Following lines are used to create a calendar
			// type object using a date string
			SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
			Date dateObj1 = curFormater.parse(startDate);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(dateObj1);

			SimpleDateFormat curFormater2= new SimpleDateFormat("dd/MM/yyyy");
			Date dateObj2 = curFormater2.parse(endDate);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(dateObj2);

			ModifyCommand command = new ModifyCommand(taskName, calendar1,
					calendar2);
			return command;
		} else {
			return null;
		}

	}

	/**
	 * This function performs the actual parsing of a undo type command
	 * and creates a undo command type object
	 * 
	 * @return UndoCommand type object or null
	 */
	
   private UndoCommand parseUndo(){//TODO :calls default constructor 
	   UndoCommand command;
	   if(!_userInput.equals("")){
		   command = new UndoCommand();
		   return command;
	   }
	   return null;  
   }
	
	private String extractTaskDescription() {
		String TaskDes = extractTillKeyword(" to ");
		while (_userInput.contains(" to ")) {
			TaskDes=TaskDes.concat(" to ");
			TaskDes=TaskDes.concat(extractTillKeyword(" to "));
		}
		return TaskDes;
	}

	private boolean isViewCommandDisplayingAllTasks() {
		_userInput=_userInput.trim();
		if (_userInput.equals("all")) {
			return true;
		}
		return false;
	}

	private boolean isViewCommandDisplayingAllTasksOrderedByTags() {
		_userInput=_userInput.trim();
		if (_userInput.equals("all ordered by tags")) {
			return true;
		}
		return false;
	}
	
	
	private boolean isViewCommandDisplayingFloatingTasks() {
		if (_userInput.equals("floating")) {
			return true;
		}
		return false;
	}

	private boolean isViewCommandDisplayingTimedTasks() {
		if (_userInput.equals("timed")) {
			return true;
		}
		return false;
	}
	
	private boolean isViewCommandDisplayingDeadLineTasks() {
		if (_userInput.equals("deadline")) {
			return true;
		}
		return false;
	}
	
	
	private boolean isViewCommandDisplayingTasksWithHashtag() {
		if (_userInput.startsWith("#")) {
			return true;
		}
		return false;
	}

	private boolean isViewCommandDisplayingTasksOnADate(String[] date){
		date[0]=_userInput.trim();
		if((isDateValid(date[0]))){
			return true;
		}
		return false;
	}
	
	private boolean isViewCommandDisplayingTasksOnADateOrderedByTags(String[] date) {
		date[0] = extractDate().trim();
		_userInput=_userInput.trim();
		if ((isDateValid(date[0])) && (_userInput.equals("by tags"))) {
			return true;
		}
		return false;
	}


	
	private String getHashtag() {
		String hashtag ;
		hashtag=_userInput.substring(1);
		return hashtag;
	}

	private String extractDate() {
		String dateStr;
		dateStr = extractTillKeyword("ordered");
		return dateStr;
	}

	/**
	 * This function uses a regex to check if the format of the date entered is
	 * correct
	 * 
	 * Return Type is Boolean
	 */
	 boolean isDateValid(String Date_str)// Checks if the date format is
												// correct or not use regex here
												// to check
	{
		String regex = "\\d{1,2}[- /]\\d{1,2}[- /]\\d{4}";
		if (Date_str.matches(regex))
			return true;
		else
			return false;
	}

	/**
	 * This function returns the command type part of the user input and also
	 * truncates the user input
	 * 
	 * Return Type: string
	 * 
	 */

	String getCommandType() {
		String commandType;
		int locationOfSpace = _userInput.indexOf(" ");
		if(locationOfSpace==-1)
		{
			return "";
		}
		commandType = (_userInput.substring(0, locationOfSpace)).trim();
		_userInput = (_userInput.substring(locationOfSpace + 1)).trim();
		return commandType; // add , delete, view, change, reschedule, mark ,
							// drop , undo , search
	}

	/**
	 * This function extracts and returns the string from the beginning to the
	 * keyword specified from the user input and also truncates the user input
	 * 
	 * Return Type: string
	 * 
	 */

	String extractTillKeyword(String next_keyword)// removes the
															// extracted string
															// and keyword from
															// the Input
	{

		String extractedString;
		int posOfKeyword;
		posOfKeyword = _userInput.indexOf(next_keyword);
		if(posOfKeyword!=-1){
		extractedString = (_userInput.substring(0, posOfKeyword)).trim();
		_userInput = (_userInput
				.substring(posOfKeyword + next_keyword.length())).trim();
		return extractedString;
		}
		return "";// returns extracted string just before keyword
	}

	/**
	 * This function extract the string in user input upto the end or upto the
	 * hashtags
	 * 
	 * Return Type : String
	 */
	 String extractTillHashtagOrEnd() {

		String extractedString;
		if (_userInput.contains("#")) {
			int posOfHash = _userInput.indexOf("#");
			extractedString = (_userInput.substring(0, posOfHash)).trim();
			_userInput = (_userInput.substring(posOfHash)).trim();
		} else {
			extractedString = (_userInput).trim();
			_userInput="";
		}
		return extractedString;
	}

	
	/**
	 * This function extracts the hashtags - hashtags cannot contain spaces 
	 */
	String extractHashtag() {
		String hashtag;
		if (_userInput.startsWith("#")){
			_userInput=_userInput.substring(1);
			if(_userInput!=""){
			hashtag=extractTillHashtagOrEnd();
			if(hashtag.contains(" ")){
				return "";
			}
			return hashtag;
			}
			else{
				return "";
			}
		}
	return "";
	}

}
