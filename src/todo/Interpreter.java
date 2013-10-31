package todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

import com.joestelmach.natty.*;

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

public class Interpreter {

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
	private static final String REDO="redo";

	/**
	 * Stores the user input
	 */
	private String _userInput;

	/**
	 * Empty Constructor to initialize the _userInput
	 */
	Interpreter() {
		_userInput = null;
	}
	
	/**
	 * Parameterized Constructor to initialize the _userInput
	 */
	Interpreter(String userInput) {
		_userInput = userInput;
	}
	
	/**
	 * Setter method to initialize the _userInput 
	 * @param inputString
	 */
	private void setInput(String inputString) {
		_userInput = inputString;
	}
	
	/**
	 * This function does the main parsing - high level of abstraction
	 * This function is called from the CommandHandler and returns a Command object
	 * If the Command is invalid it returns a null object 
	 * 
	 * Handles ParseException thrown by private methods through try-catch
	 * @return Command object
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
				
			case REDO:
				UndoCommand redoObj;
				redoObj=parseRedo();
				return redoObj;

			default:
				_userInput = commandTypeKeyword.concat(" "+_userInput);
				AddCommand addObj2;
				addObj2 = parseAdd();
				return addObj2;

			}
		} catch (Exception e) {
			System.err.println("Invalid input: " + e.getMessage());
		}
		return null;
	}

	/**
	 * This function performs the actual parsing of an "add" type Command and
	 * creates a AddCommand type object
	 * 
	 * It calls three different methods to add the three different types of tasks
	 * in the following order(if-else if pattern):
	 * 1.Deadline tasks
	 * 2.Timed tasks
	 * 3.Floating
	 * 
	 * @return AddCommand type object
	 * @throws ParseException
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
        if(!TaskDes.equals("")){
		String dateStr;
		dateStr = extractTillHashtagOrEnd();
		Calendar deadline = isValid(dateStr);
		if (!deadline.equals(null)) {

			while (!(_userInput.equals(""))) {
				String hashtag=extractHashtag();
				if(hashtag.equals("")){
					return null;
				}
				hashtags.add(hashtag);
			}

			DeadlineTask TaskObj = new DeadlineTask(TaskDes, hashtags, deadline);

			AddCommand command = new AddCommand(TaskObj);

			return command;

		}
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
         
		if(!TaskDes.equals("")){
		String date1, date2, day;

		date1 = extractTillKeyword(" to ");
		
		if(_userInput.contains(" on ")){
			
			date2 = extractTillKeyword(" on ");
			day= extractTillHashtagOrEnd();
			date1=date1.concat(" "+day+" ");
			date2=date2.concat(" "+day+" ");
		}else{
		date2 = extractTillHashtagOrEnd();
		}
		
		Calendar calendar1 = isValid(date1);
		
		Calendar calendar2 = isValid(date2);

		if ((!calendar1.equals(null)) && (!calendar2.equals(null))) {

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
		Calendar calendar[] = new Calendar[1];
		calendar[0] = null;
		if (isViewCommandDisplayingAllTasks()) {
			command = new ViewCommand(ViewCommand.MODE_VIEW_ALL);
		}else if (isViewCommandDisplayingAllTasksOrderedByTags()) {
			command = new ViewCommand(); //TODO: Ordered by tags?
		}else if (isViewCommandDisplayingFloatingTasks()) {
			command = new ViewCommand(ViewCommand.MODE_VIEW_FLOATING);
		}else if (isViewCommandDisplayingTimedTasks()) {
			command = new ViewCommand(ViewCommand.MODE_VIEW_TIMED);
		}else if (isViewCommandDisplayingDeadLineTasks()) {
			command = new ViewCommand(ViewCommand.MODE_VIEW_DEADLINE);
		}else if (isViewCommandDisplayingTasksWithHashtag()) {
			String hashtag = getHashtag();
			command = new ViewCommand(hashtag);
		}else if(isViewCommandDisplayingTasksOnADate(calendar)){
			command = new ViewCommand(calendar[0]);
		}else if (isViewCommandDisplayingTasksOnADateOrderedByTags(calendar)) {
			command = new ViewCommand(calendar[0]); //TODO: Ordered by tags?
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
		if(!taskName.equals("")){
		String startDate, endDate;
		startDate = extractTillKeyword(" till ");
		endDate = _userInput.trim();
        
		Calendar calendar1 = isValid(startDate);
		Calendar calendar2 = isValid(endDate);
		if ((!calendar1.equals(null)) && (!calendar2.equals(null))) {
			ModifyCommand command = new ModifyCommand(taskName, calendar1,
					calendar2);
			return command;
		} 
		}else {
			return null;
		}
     return null;
	}

	/**
	 * This function performs the actual parsing of a undo type command
	 * and creates a undo command type object
	 * 
	 * @return UndoCommand type object or null
	 */
		
	private UndoCommand parseUndo(){
		UndoCommand command;
		_userInput=_userInput.trim();
		if(_userInput.equals("")){
			command = new UndoCommand();
		}else{
			command = new UndoCommand(Integer.parseInt(_userInput));
		}
		return command;
	}
	
	
	/**
	 * This function performs the actual parsing of a redo type command
	 * and creates a redo command type object
	 * 
	 * @return UndoCommand type object or null
	 */
		
	private UndoCommand parseRedo(){
		UndoCommand command;
		_userInput=_userInput.trim();
		if(_userInput.equals("")){
			command = new UndoCommand(-1);
		}else{
			command = new UndoCommand((Integer.parseInt(_userInput)*-1));
		}
		return command;
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
		if (_userInput.equals("flexible")) {
			return true;
		}
		return false;
	}

	private boolean isViewCommandDisplayingTimedTasks() {
		if (_userInput.equals("events")) {
			return true;
		}
		return false;
	}
	
	private boolean isViewCommandDisplayingDeadLineTasks() {
		if (_userInput.equals("deadlines")) {
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

	private boolean isViewCommandDisplayingTasksOnADate(Calendar[] calendar){
		String dateStr=_userInput.trim();
		calendar[0] = isValid(dateStr);
		if(!calendar[0].equals(null)){
			return true;
		}
		return false;
	}
	
	private boolean isViewCommandDisplayingTasksOnADateOrderedByTags(Calendar[] calendar) {
		String dateStr = extractDate().trim();
		_userInput=_userInput.trim();
		calendar[0] = isValid(dateStr);
		if ((!calendar[0].equals(null)) && (_userInput.equals("by tags"))) {
			return true;
		}
		return false;
	}

	private String extractTaskDescription() {
		String TaskDes = extractTillKeyword(" to ");
		while (_userInput.contains(" to ")) {
			TaskDes=TaskDes.concat(" to ");
			TaskDes=TaskDes.concat(extractTillKeyword(" to "));
		}
		return TaskDes;
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


	 /**This functions checks the validity of the date using the Parser defined my natty
	  * 
	  *It returns a calendar object if date is valid else returns null
	  * 
	  * @param Date_str
	  * @return Calendar object
	  */
	 
	 
	 private Calendar isValid(String Date_str)
	 {
		 Parser parser = new Parser();
		 List<DateGroup> groups = parser.parse(Date_str);
		 if(groups.isEmpty()){
		    return null;
		    }
		 for(DateGroup group:groups)  {
	     Date dates = group.getDates().get(0); 
	     Calendar calendar = Calendar.getInstance();
	 	 calendar.setTime(dates);
		 return calendar;
		 }
		return null;
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
			_userInput=_userInput.concat(" ");
			locationOfSpace = _userInput.indexOf(" ");
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
