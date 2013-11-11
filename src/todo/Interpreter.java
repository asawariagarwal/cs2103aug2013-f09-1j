package todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.joestelmach.natty.*;

import todo.Command;
import todo.ViewCommand;
import todo.AddCommand;
import todo.DeleteCommand;
import todo.ModifyCommand;


/**
 * This class is used to interpret the command entered by the user and create 
 * a corresponding Command object for it 
 * 
 * @author:A0098167N
 */



public class Interpreter {

	/**
	 * Error message
	 */
	private static final String NO_CLASS_DEF_FOUND_ERROR = "NoClassDefFoundError: ";
	/**
	 * Index references
	 */
	private static final char INDEX_FLEXIBLE = 'f';
	private static final char INDEX_DEADLINES = 'd';
	private static final char INDEX_EVENTS = 'e';

	/**
	 * Log messages
	 */
	private static final String LOGGER_COMPLETE_REDO = "Interpretation Complete:REDO";
	private static final String LOGGER_COMPLETE_UNDO = "Interpretation Complete:UNDO";
	private static final String LOGGER_COMPLETE_UNTAG = "Interpretation Complete:UNTAG";
	private static final String LOGGER_COMPLETE_TAG = "Interpretation Complete:TAG";
	private static final String LOGGER_COMPLETE_UNMARK = "Interpretation Complete:UNMARK";
	private static final String LOGGER_COMPLETE_MARK = "Interpretation Complete:MARK";
	private static final String LOGGER_COMPLETE_SEARCH = "Interpretation Complete:SEARCH";
	private static final String LOGGER_COMPLETE_RESCEDULE = "Interpretation Complete:RESCEDULE";
	private static final String LOGGER_COMPLETE_CHANGE = "Interpretation Complete:CHANGE";
	private static final String LOGGER_COMPLETE_CLEAR = "Interpretation Complete:CLEAR";
    private static final String LOGGER_COMPLETE_DELETE = "Interpretation Complete:DELETE";
	private static final String LOGGER_COMPLETE_VIEW = "Interpretation Complete:VIEW";
	private static final String LOGGER_COMPLETE_INVALID = "Interpretation Complete:INVALID";
	private static final String LOGGER_COMPLETE_ADD = "Interpretation Complete:ADD";

	/**
	 * Logger for Interpreter
	 */
	protected static Logger interpreterLogger = Logger.getLogger("Interpreter");

	/**
	 * Exception Message
	 */
	private static final String EXCEPTION_MSG = "Unsupported Format : ";


	/**
	 * Command Type Keywords:
	 */

	private static final String ADD = "add";
	private static final String VIEW = "view";
	private static final String DELETE = "delete";
	private static final String CLEAR ="clear";
	private static final String CHANGE = "change";
	private static final String RESCHEDULE = "reschedule";
	private static final String SEARCH="search";
	private static final String MARK="mark";
	private static final String UNMARK ="unmark";
	private static final String TAG="tag";
	private static final String UNTAG="untag";
	private static final String UNDO="undo";
	private static final String REDO="redo";

	/**
	 * Keywords used in the command format 
	 */

	private static final String PREFIX_DEADLINE = " by ";
	private static final String PREFIX_START_TIME = " from ";
	private static final String PREFIX_END_TIME =" to ";
	private static final String PREFIX_DAY =" on ";
	private static final String PREFIX_RESCHEDULE =" till ";
	private static final String NULL_STRING = "";
	private static final String SPACE_STRING =" ";
	private static final String PREFIX_FROM_WITHOUT_SPACE ="from";
	

	private static final String KEYWORD_ALL = "all";
	private static final String KEYWORD_DEADLINE = "deadlines";
	private static final String KEYWORD_TIMED = "events";
	private static final String KEYWORD_FLOATING = "flexible";
	private static final String KEYWORD_EXPIRED = "expired";
	private static final String KEYWORD_DONE="done";


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

	public void setInput(String inputString) {
		_userInput = inputString;
	}

	public String  getInput(){
		return _userInput;
	}

	/**
	 * This function does the main parsing but is at a high level of abstraction
	 * This function is called from the CommandHandler and returns a Command object
	 * If the Command is invalid it returns a null object 
	 * 
	 * Handles Exception thrown by private methods through try-catch
	 * @param String - inputString
	 * @return Command object
	 */

	public Command parseInput(String inputString) {

		interpreterLogger.log(Level.INFO,"Started Interpretation");
		setInput(inputString);

		try {

			String commandTypeKeyword = getCommandType();

			switch (commandTypeKeyword) {
			case ADD:
				AddCommand addObj;
				addObj = parseAdd();
				if(addObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_ADD);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return addObj;

			case VIEW:
				ViewCommand viewObj;
				viewObj = parseView();
				if(viewObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_VIEW);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return viewObj;

			case DELETE:
				DeleteCommand delObj;
				delObj = parseDelete();
				if(delObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_DELETE);}
				else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return delObj;

			case CLEAR:
				ClearCommand clrObj;
				clrObj = parseClear();
				if(clrObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_CLEAR);
				}
				else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return clrObj;


			case CHANGE:
				ModifyCommand modObj;
				modObj = parseChange();
				if(modObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_CHANGE);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return modObj;

			case RESCHEDULE:
				ModifyCommand resObj;
				resObj = parseReschedule();
				if(resObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_RESCEDULE);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return resObj;

			case SEARCH:
				SearchCommand searchObj;
				searchObj = parseSearch();
				if(searchObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_SEARCH);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return searchObj;

			case MARK:
				ModifyCommand markObj;
				markObj = parseMark();
				if(markObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_MARK);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				} return markObj;

			case UNMARK:
				ModifyCommand unmarkObj;
				unmarkObj = parseUnmark();
				if(unmarkObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_UNMARK);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return unmarkObj;

			case TAG:
				ModifyCommand tagObj;
				tagObj = parseTag();
				if(tagObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_TAG);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);
				}return tagObj;

			case UNTAG:
				ModifyCommand untagObj;
				untagObj = parseUntag();
				if(untagObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_UNTAG);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return untagObj;

			case UNDO:
				UndoCommand undoObj;
				undoObj = parseUndo();
				if(undoObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_UNDO);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return undoObj;

			case REDO:
				UndoCommand redoObj;
				redoObj=parseRedo();
				if(redoObj!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_REDO);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return redoObj;

				//The default command mode is ADD	
			default:
				getOriginalInput(commandTypeKeyword);
				AddCommand addObj2;
				addObj2 = parseAdd();
				if(addObj2!=null){
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_ADD);
				}else{
					interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);	
				}return addObj2;

			}
		} catch (Exception e) {
			System.err.println(EXCEPTION_MSG+ e.getMessage());
		}
		interpreterLogger.log(Level.INFO,LOGGER_COMPLETE_INVALID);
		return null;
	}


	/**
	 * This function is used to parse an ADD Command and
	 * it creates a AddCommand object
	 * 
	 * It calls three different methods to add the three different types of tasks
	 * in the following order(if-else if pattern):
	 * 1.Deadline tasks
	 * 2.Timed tasks
	 * 3.Floating
	 * 
	 * The default interpretation of an add command is floating.
	 * 
	 * @return AddCommand object
	 * @throws Exception
	 */

	AddCommand parseAdd() throws Exception {
		AddCommand command;
		String copyInput;
		copyInput=_userInput;
		command = parseDeadlineTask();
		if (command != null) {
			return command;
		}
		_userInput=copyInput;
		command = parseTimedTask();
		if (command != null) {
			return command;
		}
		_userInput=copyInput;
		command = parseFloatingTask();
		return command;
	}



	/**
	 * This function is used in case the Task is a deadline task and it parses
	 * the input to create a DeadlineTask type object in case of valid input
	 * and return null for invalid input
	 * 
	 * @return AddCommand type object or null
	 * @throws Exception
	 */

	AddCommand parseDeadlineTask()throws Exception  {

		ArrayList<String> hashtags = new ArrayList<String>();
		String TaskDes = getDeadlineTaskDescription();
		String dateStr;

		if(!TaskDes.equals(NULL_STRING)){

			dateStr = extractTillHashtagOrEnd();
			Calendar deadline = isValid(dateStr);
			if (deadline!=(null)) {

				if(populateHashtags(hashtags)==0)
					return null;

				DeadlineTask taskObj = new DeadlineTask(TaskDes, hashtags, deadline);
				AddCommand command = new AddCommand(taskObj);

				return command;

			}
		}

		return null;
	}

	/**
	 * This function is used in case the Task is a timed task and it parses 
	 * the input to create a TimedTask type object in case of valid input
	 * and return null for invalid input
	 * 
	 * @return AddCommand type object or null
	 * @throws Exception
	 */

	AddCommand parseTimedTask()throws Exception {

		ArrayList<String> hashtags = new ArrayList<String>();
		String date1, date2, day;
		String TaskDes = getTimedTaskDescription();

		if(!TaskDes.equals(NULL_STRING)){

			date1 = extractTillKeyword(PREFIX_END_TIME);

			//Special case if user specifies day separate from time 
			if(_userInput.contains(PREFIX_DAY)){

				date2 = extractTillKeyword(PREFIX_DAY);
				day= extractTillHashtagOrEnd();
				date1=date1.concat(SPACE_STRING+day+SPACE_STRING);
				date2=date2.concat(SPACE_STRING+day+SPACE_STRING);
			}else{
				date2 = extractTillHashtagOrEnd();
			}

			Calendar calendar1 = isValid(date1);
			Calendar calendar2 = isValid(date2);

			if ((calendar1!=(null)) && (calendar2!=(null))) {

				if(populateHashtags(hashtags)==0)
					return null;

				TimedTask taskObj = new TimedTask(TaskDes, hashtags, calendar1,calendar2);
				AddCommand command = new AddCommand(taskObj);

				return command;

			}
		}

		return null;
	}


	/**
	 * This function is used in case the task is a floating task and it parses
	 * the input to create a FloatingTask type object in case of valid input
	 * and return null for invalid input
	 * 
	 * @return AddCommand type object or null
	 * @throws Exception
	 */

	AddCommand parseFloatingTask() throws Exception{

		ArrayList<String> hashtags = new ArrayList<String>();
		String TaskDes = extractTillHashtagOrEnd();


		if(populateHashtags(hashtags)==0)
			return null;

		FloatingTask taskObj = new FloatingTask(TaskDes, hashtags);
		AddCommand command = new AddCommand(taskObj);
		return command;
	}



	/**
	 * This function performs the parsing of a view type command and
	 * creates a ViewCommand type object depending on the type of view
	 * It uses helper methods to check which type of view is required 
	 * 
	 * @return ViewCommand type object or null
	 * @throws Exception
	 */

	ViewCommand parseView() throws Exception  {
		ViewCommand command;
		Calendar calendar[] = new Calendar[2];
		calendar[0] = null;
		calendar[1] = null;
		if (isViewCommandDisplayingAllTasks()) {
			command = new ViewCommand(ViewCommand.MODE_VIEW_ALL);
		}else if (isViewCommandDisplayingFloatingTasks()) {
			command = new ViewCommand(ViewCommand.MODE_VIEW_FLOATING);
		}else if (isViewCommandDisplayingTimedTasks()) {
			command = new ViewCommand(ViewCommand.MODE_VIEW_TIMED);
		}else if (isViewCommandDisplayingDeadLineTasks()) {
			command = new ViewCommand(ViewCommand.MODE_VIEW_DEADLINE);
		}else if(isViewCommandDisplayingDone()){
			command = new ViewCommand(ViewCommand.MODE_VIEW_DONE);
		}else if(isViewCommandDisplayingExpired()){
			command = new ViewCommand(ViewCommand.MODE_VIEW_EXPIRED);
		}else if (isViewCommandDisplayingTasksWithHashtag()) {
			String hashtag = getHashtag();
			command = new ViewCommand(hashtag);
		}else if(isViewCommandDisplayingTasksInAnInterval(calendar)){
			command = new ViewCommand(calendar[0],calendar[1]);
		}else if(isViewCommandDisplayingTasksOnADate(calendar)){
			command = new ViewCommand(calendar[0]);
		} else {
			return null;
		}
		return command;
	}


	/**
	 * This function performs the parsing of a delete type command and
	 * creates a DeleteCommand type object
	 * The delete can be by index and by snippet.The function uses regex to
	 * check for delete by index
	 * 
	 * @return DeleteCommand type object or null
	 * @throws Exception
	 * 
	 */

	DeleteCommand parseDelete() throws Exception {
		String regex="-[edf]\\d{1,}";
		if(_userInput.matches(regex)){
			DeleteCommand command;
			command =deleteByIndex();	
			return command;
		}else if(!_userInput.equals(NULL_STRING)) {
			DeleteCommand command = new DeleteCommand(_userInput);
			return command;
		} 
		return null;
	}



	/**
	 * This function performs the parsing of a clear type command and
	 * creates a ClearCommand type object
	 * It calls helper methods to determine what is to be cleared 
	 * 
	 * @return ClearCommand type object or null
	 * @throws Exception
	 */

	ClearCommand parseClear() throws Exception {
		ClearCommand command;
		_userInput=_userInput.trim();
		if(_userInput.equals(NULL_STRING)){
			command = new ClearCommand();
		}else if(isClearCommandDone()){
			command = new ClearCommand(ClearCommand.MODE_CLEAR_DONE);
		}else if(isClearCommandExpired()){
			command = new ClearCommand(ClearCommand.MODE_CLEAR_EXPIRED);
		}else if(isClearCommandDeadline()){
			command = new ClearCommand(ClearCommand.MODE_CLEAR_DEADLINE);
		}else if(isClearCommandTimed()){
			command = new ClearCommand(ClearCommand.MODE_CLEAR_TIMED);
		}else if(isClearCommandFloating()){
			command = new ClearCommand(ClearCommand.MODE_CLEAR_FLOATING);
		}else{
			return null;
		}
		return command;
	}



	/**
	 * This function performs the parsing of a change type command and
	 * creates a ModifyCommand type object
	 * A change command is used to change the task description of a task 
	 * 
	 * @return ModifyCommand type object
	 * @throws Exception
	 */

	ModifyCommand parseChange()throws Exception {
		String regex="-[edf]\\d{1,} {1,}to {1,}.*";
		String oldTask, newTask;
		if(_userInput.matches(regex)){
			ModifyCommand command;
			command=changeByIndex();
			return command;
		}
		oldTask = getModifyTaskDescription().trim();
		newTask = _userInput.trim();
		ModifyCommand command = new ModifyCommand(oldTask, newTask);
		return command;
	}



	/**
	 * This function performs the parsing of a reschedule type command and
	 * creates a ModifyCommand type object
	 * 
	 * @return ModifyCommand type object or null
	 * @throws Exception
	 */

	ModifyCommand parseReschedule()throws Exception{
		ModifyCommand command;
		String copyInput; 
		copyInput=_userInput;
		String regex="-[edf]\\d{1,} {1,}to {1,}.*";
		if(_userInput.matches(regex)){
			command =rescheduleByIndex();
			return command;
		}
		command = parseRescheduleTimed();
		if (command != null) {
			return command;
		}
		_userInput=copyInput;
		command = parseRescheduleDeadline();
		if (command != null) {
			return command;
		}
		return null;
	}

	/**
	 * This function performs the parsing of a reschedule type command on 
	 * a Timed task and creates a ModifyCommand type object
	 * 
	 * @return ModifyCommand type object or null
	 * @throws Exception
	 */

	ModifyCommand parseRescheduleTimed()throws Exception  {
		String taskName ;
		String startDate, endDate;
		taskName= getModifyTaskDescription().trim();

		if(!taskName.equals(NULL_STRING)){

			startDate = extractTillKeyword(PREFIX_RESCHEDULE);
			endDate = _userInput.trim();

			Calendar calendar1 = isValid(startDate);
			Calendar calendar2 = isValid(endDate);

			if ((calendar1!=(null)) && (calendar2!=(null))) {

				ModifyCommand command = new ModifyCommand(taskName, calendar1,calendar2);
				return command;
			}
		}

		return null;
	}

	/**
	 * This function performs the parsing of a reschedule type command on 
	 * a Deadline task and creates a ModifyCommand type object
	 * 
	 * @return ModifyCommand type object or null
	 * @throws Exception
	 */

	ModifyCommand parseRescheduleDeadline()throws Exception {
		String taskName ;
		String deadline;

		taskName= getModifyTaskDescription().trim();
		if(!taskName.equals(NULL_STRING)){

			deadline= _userInput.trim();
			Calendar calendar = isValid(deadline);

			if(calendar!=(null)){
				ModifyCommand command = new ModifyCommand(taskName , calendar);
				return command;
			}
		}

		return null;
	}

	/**
	 * This function performs the parsing of a search type command and
	 * creates a search command type object
	 * 
	 * @return SearchCommand type object or null
	 * @throws Exception
	 */

	    SearchCommand parseSearch()throws Exception{
		SearchCommand command;
		
		ArrayList <String> wordsInclude = new ArrayList<>();
		ArrayList <String> wordsExclude = new ArrayList<>();
		ArrayList <String> tagsInclude = new ArrayList<>();
		ArrayList <String> tagsExclude = new ArrayList<>();
		
		String searchString;
		String copyInput=_userInput;
		Calendar calendar[]= new Calendar[2];
		calendar[0]=null;
		calendar[1]=null;
		
		searchString=SearchInInterval(calendar);
		if(searchString!=NULL_STRING){
	    splitIntoDifferentArrays(searchString,wordsInclude,wordsExclude,tagsInclude,tagsExclude);
		command = new SearchCommand(wordsInclude,wordsExclude,tagsInclude,tagsExclude,calendar[0],calendar[1]);
		return command;
		}
		
		calendar[0]=null;
		calendar[1]=null;
		_userInput=copyInput;
		searchString=SearchOnDate(calendar);
		if(searchString!=NULL_STRING){
			splitIntoDifferentArrays(searchString,wordsInclude,wordsExclude,tagsInclude,tagsExclude);
			command = new SearchCommand(wordsInclude,wordsExclude,tagsInclude,tagsExclude,calendar[0]);
			return command;	
		}
	
		_userInput=copyInput;
		if(!_userInput.equals(NULL_STRING)){
			splitIntoDifferentArrays(_userInput,wordsInclude,wordsExclude,tagsInclude,tagsExclude);
			command = new SearchCommand(wordsInclude,wordsExclude,tagsInclude,tagsExclude);
			return command;
		}
			return null;
			
	}

	
	/**
	 * This function performs the actual parsing of a mark type command and
	 * creates a ModifyCommand type object
	 * 
	 * @return ModifyCommand type object or null
	 * @throws Exception
	 */

	ModifyCommand parseMark()throws Exception{
		ModifyCommand command;
		String regex="-[edf]\\d{1,}";
		if(_userInput.matches(regex)){
			command =markByIndex();
			return command;
		}
		if(!_userInput.equals(NULL_STRING)){
			command = new ModifyCommand(_userInput.trim(),true);
			return command;
		}else{
			return null;
		}
	}


	/**
	 * This function performs the actual parsing of a unmark type command and
	 * creates a ModifyCommand type object
	 * 
	 * @return ModifyCommand type object or null
	 * 
	 */

	ModifyCommand parseUnmark()throws Exception{
		ModifyCommand command;
		String regex="-[edf]\\d{1,}";
		if(_userInput.matches(regex)){
			command=unmarkByIndex();
			return command;
		}
		if(!_userInput.equals(NULL_STRING)){
			command = new ModifyCommand(_userInput.trim(),false);
			return command;
		}else{
			return null;
		}
	}



	/**
	 * This function performs the actual parsing of a tag type command and
	 * creates a tag command type object
	 * 
	 * @return ModifyCommand type object or null
	 * 
	 */

	ModifyCommand parseTag() throws Exception {
		ModifyCommand command;
		String regex="-[edf]\\d{1,} {1,}.*";
		ArrayList<String> hashtags = new ArrayList<String>();
		if(_userInput.matches(regex)){
			command =tagByIndex(hashtags);
			return command;
		}
		String taskDes=extractTillHashtagOrEnd();
		if(!taskDes.equals(NULL_STRING)) {
			while (!(_userInput.equals(NULL_STRING))) {
				String hashtag=extractHashtag();
				if(hashtag.equals(NULL_STRING)){
					break;
				}
				hashtags.add(hashtag);
			}
			command = new ModifyCommand(taskDes, hashtags, true);
			return command;
		} else{
			return null;
		}
	}



	/**
	 * This function performs the actual parsing of a untag type command and
	 * creates a ModifyCommand type object
	 * 
	 * @return ModifyCommand type object or null
	 * 
	 */
	ModifyCommand parseUntag() throws Exception {
		ModifyCommand command;
		String regex="-[edf]\\d{1,} {1,}.*";
		ArrayList<String> hashtags = new ArrayList<String>();
		if(_userInput.matches(regex)){
			command=untagByIndex(hashtags);
			return command;
		}
		String taskDes=extractTillHashtagOrEnd();
		if(!taskDes.equals(NULL_STRING)) {
			while (!(_userInput.equals(NULL_STRING))) {
				String hashtag=extractHashtag();
				if(hashtag.equals(NULL_STRING)){
					break;
				}
				hashtags.add(hashtag);
			}
			command = new ModifyCommand(taskDes, hashtags, false);
			return command;
		} else{
			return null;
		}
	}

	/**
	 * This function performs the actual parsing of a undo type command
	 * and creates a UndoCommand type object
	 * 
	 * @return UndoCommand type object or null
	 * @throws Exception
	 */

	UndoCommand parseUndo()throws Exception{
		UndoCommand command;
		_userInput=_userInput.trim();
		if(_userInput.equals(NULL_STRING)){
			command = new UndoCommand();
		}else{
			command = new UndoCommand(Integer.parseInt(_userInput));
		}
		return command;
	}


	/**
	 * This function performs the actual parsing of a redo type command
	 * and creates a UndoCommand type object
	 * 
	 * @return UndoCommand type object or null
	 * @throws Exception
	 */

	UndoCommand parseRedo()throws Exception{
		UndoCommand command;
		_userInput=_userInput.trim();
		if(_userInput.equals(NULL_STRING)){
			command = new UndoCommand(1, true);
		}else{
			command = new UndoCommand((Integer.parseInt(_userInput)), true);
		}
		return command;
	}

	/**
	 * This function checks if view command refers to displaying all tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */
	private boolean isViewCommandDisplayingAllTasks() throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_ALL)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if view command refers to displaying floating tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */

	private boolean isViewCommandDisplayingFloatingTasks() throws Exception {
		if (_userInput.equals(KEYWORD_FLOATING)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if view command refers to displaying timed tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isViewCommandDisplayingTimedTasks()throws Exception {
		if (_userInput.equals(KEYWORD_TIMED)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if view command refers to displaying deadline tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isViewCommandDisplayingDeadLineTasks()throws Exception {
		if (_userInput.equals(KEYWORD_DEADLINE)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if view command refers to displaying completed tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isViewCommandDisplayingDone()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_DONE)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if view command refers to displaying expired tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isViewCommandDisplayingExpired()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_EXPIRED)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if view command refers to displaying tasks which 
	 * have a specific hashtag
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isViewCommandDisplayingTasksWithHashtag()throws Exception {
		if (_userInput.startsWith("#")) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if view command refers to displaying tasks in 
	 * a particular interval 
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isViewCommandDisplayingTasksInAnInterval(Calendar[] calendar)throws Exception{
		String startDate,endDate;
		if((_userInput.trim()).startsWith(PREFIX_FROM_WITHOUT_SPACE )){
			_userInput = (_userInput
					.substring(_userInput.indexOf(PREFIX_FROM_WITHOUT_SPACE ) + (PREFIX_FROM_WITHOUT_SPACE ).length())).trim();
			startDate=extractTillKeyword(PREFIX_END_TIME);
			calendar[0] =isValid(startDate);
			endDate=extractTillHashtagOrEnd();
			calendar[1] =isValid(endDate);
			if((calendar[0]!=(null))&&(calendar[1]!=(null))){
				return true;
			}else {
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * This function checks if view command refers to displaying tasks on 
	 * a particular date 
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isViewCommandDisplayingTasksOnADate(Calendar[] calendar)throws Exception {
		String dateStr=_userInput.trim();

		calendar[0] = isValid(dateStr);
		if(calendar[0]!=(null)){
			return true;
		}

		return false;
	}
	
/**
 * This function parses the command when user is trying to delete by index
 * 
 * @return DeleteCommand object or null
 */
	
	private DeleteCommand deleteByIndex() {
		char taskType;
		int index;
		DeleteCommand command;
		taskType=_userInput.charAt(1);
		index=Integer.parseInt(_userInput.substring(2));
		if(taskType==INDEX_EVENTS){
			command = new DeleteCommand(index,DeleteCommand.INDEX_TIMED);
			return command;
		}else if(taskType==INDEX_DEADLINES){
			command = new DeleteCommand(index,DeleteCommand.INDEX_DEADLINE);
			return command;
		}else if(taskType==INDEX_FLEXIBLE){
			command = new DeleteCommand(index,DeleteCommand.INDEX_FLOATING);
			return command;
		}
		return null;
	}
	
	/**
	 * This function parses the command when user is trying to change by index
	 * 
	 * @return ModifyCommand object or null
	 */
	
	private ModifyCommand changeByIndex() {
		char taskType;
		int index;
		String newTask;
		ModifyCommand command;
		taskType=_userInput.charAt(1);
		int spaceIndex = _userInput.indexOf(SPACE_STRING);
		index=Integer.parseInt(_userInput.substring(2, spaceIndex));
		int toIndex = _userInput.indexOf(" to ", spaceIndex) + 3;
		newTask = _userInput.substring(toIndex).trim();
		if(taskType==INDEX_EVENTS){
			command = new ModifyCommand(index, ModifyCommand.INDEX_TIMED, newTask);
			return command;
		} else if(taskType==INDEX_DEADLINES){
			command = new ModifyCommand(index, ModifyCommand.INDEX_DEADLINE, newTask);
			return command;
		} else if(taskType==INDEX_FLEXIBLE){
			command = new ModifyCommand(index, ModifyCommand.INDEX_FLOATING, newTask);
			return command;
		}
		return null;
	}
	
	/**
	 * This function parses the command when user is trying to reschedule by index
	 * 
	 * @return ModifyCommand object or null
	 */
	
	
	private ModifyCommand rescheduleByIndex() throws Exception {
		ModifyCommand command;
		char taskType;
		int index;
		taskType=_userInput.charAt(1);
		int spaceIndex = _userInput.indexOf(SPACE_STRING);
		index=Integer.parseInt(_userInput.substring(2, spaceIndex));
		int toIndex = _userInput.indexOf(" to ", spaceIndex) + 3;
		_userInput = _userInput.substring(toIndex).trim();
		if(taskType==INDEX_EVENTS){
			String startDate = extractTillKeyword(PREFIX_RESCHEDULE);
			String endDate = _userInput.trim();
			Calendar calendar1 = isValid(startDate);
			Calendar calendar2 = isValid(endDate);
			command = new ModifyCommand(index, ModifyCommand.INDEX_TIMED, calendar1, calendar2);
			return command;
		} else if(taskType==INDEX_DEADLINES){
			String deadline= _userInput.trim();
			Calendar calendar = isValid(deadline);
			command = new ModifyCommand(index, ModifyCommand.INDEX_DEADLINE, calendar);
			return command;
		} else if(taskType==INDEX_FLEXIBLE){
			return null;
		}
		return null;
	}

	/**
	 * This function parses the command when user is trying to mark by index
	 * 
	 * @return ModifyCommand object or null
	 */
	
	private ModifyCommand markByIndex() {
		ModifyCommand command;
		char taskType;
		int index;
		taskType=_userInput.charAt(1);
		index=Integer.parseInt(_userInput.substring(2));
		if(taskType==INDEX_EVENTS){
			command = new ModifyCommand(index, ModifyCommand.INDEX_TIMED, true);
			return command;
		} else if(taskType==INDEX_DEADLINES){
			command = new ModifyCommand(index, ModifyCommand.INDEX_DEADLINE, true);
			return command;
		} else if(taskType==INDEX_FLEXIBLE){
			command = new ModifyCommand(index, ModifyCommand.INDEX_FLOATING, true);
			return command;
		}
		return null;
	}
	
	/**
	 * This function parses the command when user is trying to unmark by index
	 * 
	 * @return ModifyCommand object or null
	 */
	
	private ModifyCommand unmarkByIndex() {
		ModifyCommand command;
		char taskType;
		int index;
		taskType=_userInput.charAt(1);
		index=Integer.parseInt(_userInput.substring(2));
		if(taskType==INDEX_EVENTS){
			command = new ModifyCommand(index, ModifyCommand.INDEX_TIMED, false);
			return command;
		} else if(taskType==INDEX_DEADLINES){
			command = new ModifyCommand(index, ModifyCommand.INDEX_DEADLINE, false);
			return command;
		} else if(taskType==INDEX_FLEXIBLE){
			command = new ModifyCommand(index, ModifyCommand.INDEX_FLOATING, false);
			return command;
		}
		return null;
	}

	/**
	 * This function parses the command when user is trying to tag by index
	 * 
	 * @return ModifyCommand object or null
	 */
	
	private ModifyCommand tagByIndex(ArrayList<String> hashtags) throws Exception {
		ModifyCommand command;
		char taskType;
		int index;
		taskType=_userInput.charAt(1);
		int spaceIndex = _userInput.indexOf(SPACE_STRING);
		index=Integer.parseInt(_userInput.substring(2, spaceIndex));
		_userInput = _userInput.substring(spaceIndex).trim();
		while (!(_userInput.equals(NULL_STRING))) {
			String hashtag=extractHashtag();
			if(hashtag.equals(NULL_STRING)){
				break;
			}
			hashtags.add(hashtag);
		}
		if(taskType==INDEX_EVENTS){
			command = new ModifyCommand(index, ModifyCommand.INDEX_TIMED, hashtags, true);
			return command;
		} else if(taskType==INDEX_DEADLINES){
			command = new ModifyCommand(index, ModifyCommand.INDEX_DEADLINE, hashtags, true);
			return command;
		} else if(taskType==INDEX_FLEXIBLE){
			command = new ModifyCommand(index, ModifyCommand.INDEX_FLOATING, hashtags, true);
			return command;
		}
		return null;
	}
	
	/**
	 * This function parses the command when user is trying to untag by index
	 * 
	 * @return ModifyCommand object or null
	 */
	
	private ModifyCommand untagByIndex(ArrayList<String> hashtags) throws Exception {
		ModifyCommand command;
		char taskType;
		int index;
		taskType=_userInput.charAt(1);
		int spaceIndex = _userInput.indexOf(SPACE_STRING);
		index=Integer.parseInt(_userInput.substring(2, spaceIndex));
		_userInput = _userInput.substring(spaceIndex).trim();
		while (!(_userInput.equals(NULL_STRING))) {
			String hashtag=extractHashtag();
			if(hashtag.equals(NULL_STRING)){
				break;
			}
			hashtags.add(hashtag);
		}
		if(taskType==INDEX_EVENTS){
			command = new ModifyCommand(index, ModifyCommand.INDEX_TIMED, hashtags, false);
			return command;
		} else if(taskType==INDEX_DEADLINES){
			command = new ModifyCommand(index, ModifyCommand.INDEX_DEADLINE, hashtags, false);
			return command;
		} else if(taskType==INDEX_FLEXIBLE){
			command = new ModifyCommand(index, ModifyCommand.INDEX_FLOATING, hashtags, false);
			return command;
		}
		return null;
	}
	
	/**
	 * This function checks if clear command refers to clearing all deadline tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isClearCommandDeadline()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_DEADLINE)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if clear command refers to clearing all timed tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isClearCommandTimed()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_TIMED)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if clear command refers to clearing all floating tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isClearCommandFloating()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_FLOATING)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if clear command refers to clearing all completed tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isClearCommandDone()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_DONE)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if clear command refers to clearing all expired tasks
	 * 
	 * @return boolean 
	 * @throws Exception
	 */	

	private boolean isClearCommandExpired()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_EXPIRED)) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks is search is called in a particular interval
	 *  
	 * @param calendar
	 * @return String
	 * @throws Exception
	 */
    private String SearchInInterval(Calendar[] calendar) throws Exception{
    	String searchString =getTimedTaskDescription();
    	String startDate,endDate;
			startDate=extractTillKeyword(PREFIX_END_TIME);
			calendar[0] =isValid(startDate);
			endDate=extractTillHashtagOrEnd();
			calendar[1] =isValid(endDate);
			if((calendar[0]!=(null))&&(calendar[1]!=(null))&&(searchString!=NULL_STRING)){
				return searchString;
			}else {
				return NULL_STRING;
			}
		}	
	/**
	 * This function checks if search is called on a particular date
	 * 
	 * @param calendar
	 * @return String
	 * @throws Exception
	 */
    
    
	private String SearchOnDate(Calendar[] calendar) throws Exception{
		String searchString =getSearchKeywords();
		String date;
		date = extractTillHashtagOrEnd();
		calendar[0]=isValid(date);
		if(calendar[0]!=(null)&&(searchString!=NULL_STRING)){
			return searchString;
		}else{
			return NULL_STRING;
		}
	}
  /**
   * This function splits the search string into different ketwords the
   * user wants to include/exclude in the search
   *   
   * @param searchString
   * @param wordsInclude
   * @param wordsExclude
   * @param tagsInclude
   * @param tagsExclude
   * @throws Exception
   */
	
	private void splitIntoDifferentArrays( String searchString, ArrayList<String> wordsInclude, ArrayList<String> wordsExclude, ArrayList<String> tagsInclude,ArrayList<String> tagsExclude)throws Exception{
		
		String[] keywords = searchString.split(" +");
		for (int i=0; i<keywords.length; i++){
			if (keywords[i].charAt(0) == '#' && keywords[i].length() > 1) {
				String tag = keywords[i].substring(1);
				tagsInclude.add(tag);
			}else if (keywords[i].charAt(0) == '-' && keywords[i].length() > 1) {
				if (keywords[i].charAt(1) == '#') {
					if (keywords[i].length() > 2) {
						String tag = keywords[i].substring(2);
						tagsExclude.add(tag);
				}
				} else {
					String word = keywords[i].substring(1);
					wordsExclude.add(word);
				}
			}else {
				String word = keywords[i].substring(1);
				wordsInclude.add(word);
			}	
		}
	}
    
	/**
	 * This function returns the command type part of the user input and also
	 * truncates the user input
	 * 
	 *@return String
	 *@throws Exception
	 */

	String getCommandType() throws Exception{
		String commandType;
		int locationOfSpace = _userInput.indexOf(SPACE_STRING);
		if(locationOfSpace==-1)
		{
			_userInput=_userInput.concat(SPACE_STRING);
			locationOfSpace = _userInput.indexOf(SPACE_STRING);
		}
		assert locationOfSpace!=-1;
		commandType = (_userInput.substring(0, locationOfSpace)).trim();
		_userInput = (_userInput.substring(locationOfSpace + 1)).trim();
		return commandType; // add , delete, view, change, reschedule, mark ,
		// drop , undo , search
	}

	/**
	 * This functions is used when the default command i.e. ADD has to be executed.
	 * It returns the original input to ease the parsing 
	 * @param String - commandTypeKeyword
	 *@return String
	 *@throws Exception
	 */

	private void getOriginalInput(String commandTypeKeyword)throws Exception{
		_userInput = commandTypeKeyword.concat(SPACE_STRING+_userInput);
	}

	/**
	 * This function is used to extract the task description of a deadline type task
	 * @return String 
	 * @throws Exception
	 */

	String getDeadlineTaskDescription()throws Exception {
		String TaskDes = extractTillKeyword(PREFIX_DEADLINE);
		while (_userInput.contains(PREFIX_DEADLINE)) {
			TaskDes= TaskDes.concat(PREFIX_DEADLINE);
			TaskDes=TaskDes.concat(extractTillKeyword(PREFIX_DEADLINE));
		}
		return TaskDes;
	}

	/**
	 * This function is used to extract the task description of a timed type task
	 * @return String 
	 * @throws Exception
	 */

	String getTimedTaskDescription()throws Exception {
		String TaskDes = extractTillKeyword(PREFIX_START_TIME);
		while (_userInput.contains(PREFIX_START_TIME)) {
			TaskDes=TaskDes.concat(PREFIX_START_TIME);
			TaskDes=TaskDes.concat(extractTillKeyword(PREFIX_START_TIME));
		}
		return TaskDes;
	}

	/**
	 * This function is used to extract the task description of a task to be modified
	 * @return String
	 * @throws Exception 
	 */

	String getModifyTaskDescription()throws Exception {
		String TaskDes = extractTillKeyword(PREFIX_END_TIME);
		while (_userInput.contains(PREFIX_END_TIME)) {
			TaskDes=TaskDes.concat(PREFIX_END_TIME);
			TaskDes=TaskDes.concat(extractTillKeyword(PREFIX_END_TIME));
		}
		return TaskDes;
	}

	
	String getSearchKeywords()throws Exception {
		String TaskDes = extractTillKeyword(PREFIX_DAY);
		while (_userInput.contains(PREFIX_DAY)) {
			TaskDes=TaskDes.concat(PREFIX_DAY);
			TaskDes=TaskDes.concat(extractTillKeyword(PREFIX_DAY));
		}
		return TaskDes;
	}
	
	

	/**
	 * This function checks if there are any hash tags in the input
	 * If yes then it populates the array storing the hash tags
	 * It return 0 is something other than a hash tag was found 
	 * or if the hash tag was invalid, else return 1 
	 * 
	 * @param -ArrayList<String> hashtags
	 * @return integer 
	 * @throws Exception
	 */

	  int populateHashtags(ArrayList<String> hashtags)throws Exception {
		while (!(_userInput.equals(NULL_STRING))) {
			String hashtag=extractHashtag();
			if(hashtag.equals(NULL_STRING)){
				return 0;
			}
			hashtags.add(hashtag);
		}
		return 1;
	}

	/**This functions checks the validity of the date using the Parser defined by 
	 *Natty Date Parser
	 *It uses try- catch to handle exceptions thrown by Natty
	 * 
	 *It returns a calendar object if date is valid else returns null
	 * 
	 * @param String Date_str
	 * @return Calendar object
	 * @throws Exception
	 */

	public Calendar isValid(String dateStr)throws Exception {
		try{
			dateStr=manipulateDate(dateStr);
			Parser parser = new Parser();
			List<DateGroup> groups = parser.parse(dateStr);
			if(groups.isEmpty()){
				return null;
			}
			for(DateGroup group:groups)  {
				Date dates = group.getDates().get(0); 
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dates);
				return calendar;
			}
		}
		catch (NoClassDefFoundError e) {
			System.err.println(NO_CLASS_DEF_FOUND_ERROR + e.getMessage());
		}
		return null;
	}

	/**
	 * This is an internal helper method for isValid.
	 * This converts a date format from month followed by date 
	 * to date followed by month as that is more intuitive for our user
	 * than compared to the one implemented by natty
	 * It uses regex to compare date formats 
	 * 
	 * @param String dateStr
	 * @return String
	 * @throws Exception
	 */
	String manipulateDate(String dateStr)throws Exception{
		int slash, dash;
		String dd , mm, newdate;
		char d , m ;
		String dregex11 = ".*\\d{1}-\\d{1}-\\d{0,4}.*";
		String dregex12 = ".*\\d{1}-\\d{2}-\\d{0,4}.*";
		String dregex21 = ".*\\d{2}-\\d{1}-\\d{0,4}.*";
		String dregex22 = ".*\\d{2}-\\d{2}-\\d{0,4}.*";

		String sregex11 = ".*\\d{1}/\\d{1}/\\d{0,4}.*";
		String sregex12 = ".*\\d{1}/\\d{2}/\\d{0,4}.*";
		String sregex21 = ".*\\d{2}/\\d{1}/\\d{0,4}.*";
		String sregex22 = ".*\\d{2}/\\d{2}/\\d{0,4}.*";

		if (dateStr.matches(dregex22)){
			dash =dateStr.indexOf("-");
			dd=dateStr.substring(dash-2,dash);
			mm=dateStr.substring(dash+1,dash+3);
			newdate=mm+"-"+dd;
			dateStr=dateStr.replaceFirst("\\d{2}-\\d{2}", newdate);	
		}
		else if (dateStr.matches(dregex21)){
			dash =dateStr.indexOf("-");
			dd=dateStr.substring(dash-2,dash);
			m=dateStr.charAt(dash+1);
			newdate=m+"-"+dd;
			dateStr=dateStr.replaceFirst("\\d{2}-\\d{1}", newdate);	
		}
		else if (dateStr.matches(dregex12)){
			dash =dateStr.indexOf("-");
			d=dateStr.charAt(dash-1);
			mm=dateStr.substring(dash+1,dash+3);
			newdate=mm+"-"+d;
			dateStr=dateStr.replaceFirst("\\d{1}-\\d{2}", newdate);	
		}
		else if (dateStr.matches(dregex11)){
			dash =dateStr.indexOf("-");
			d=dateStr.charAt(dash-1);
			m=dateStr.charAt(dash+1);
			newdate=m+"-"+d;
			dateStr=dateStr.replaceFirst("\\d{1}-\\d{1}", newdate);	
		}

		else if (dateStr.matches(sregex22)){
			slash =dateStr.indexOf("/");
			dd=dateStr.substring(slash-2,slash);
			mm=dateStr.substring(slash+1,slash+3);
			newdate=mm+"/"+dd;
			dateStr=dateStr.replaceFirst("\\d{2}/\\d{2}", newdate);	
		}
		else if (dateStr.matches(sregex21)){
			slash =dateStr.indexOf("/");
			dd=dateStr.substring(slash-2,slash);
			m=dateStr.charAt(slash+1);
			newdate=m+"/"+dd;
			dateStr=dateStr.replaceFirst("\\d{2}/\\d{1}", newdate);	
		}
		else if (dateStr.matches(sregex12)){
			slash =dateStr.indexOf("/");
			d=dateStr.charAt(slash-1);
			mm=dateStr.substring(slash+1,slash+3);
			newdate=mm+"/"+d;
			dateStr=dateStr.replaceFirst("\\d{1}/\\d{2}", newdate);	
		}
		else if (dateStr.matches(sregex11)){
			slash =dateStr.indexOf("/");
			d=dateStr.charAt(slash-1);
			m=dateStr.charAt(slash+1);
			newdate=m+"/"+d;
			dateStr=dateStr.replaceFirst("\\d{1}/\\d{1}", newdate);	
		}		
		return dateStr;
	}

	/**
	 * This function extracts and returns the string from the beginning to the
	 * keyword specified from the user input and also truncates the user input
	 * 
	 * @return String
	 * @throws Exception
	 * 
	 */

	private String extractTillKeyword(String next_keyword)throws Exception// removes the
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
		assert posOfKeyword==-1;
		return NULL_STRING;// returns extracted string just before keyword
	}


	/**
	 * This function extract the string in user input upto the end or upto the
	 * hashtags
	 * 
	 * @return String
	 * @throws Exception
	 */
	private String extractTillHashtagOrEnd()throws Exception {

		String extractedString;
		if (_userInput.contains("#")) {
			int posOfHash = _userInput.indexOf("#");
			extractedString = (_userInput.substring(0, posOfHash)).trim();
			_userInput = (_userInput.substring(posOfHash)).trim();
		} else {
			extractedString = (_userInput).trim();
			_userInput=NULL_STRING;
		}
		return extractedString;
	}

	/**
	 * This function extracts the hashtags - hashtags cannot contain spaces 
	 * 
	 * @return String
	 * @throws Exception
	 */

	private String extractHashtag()throws Exception {
		String hashtag;
		if (_userInput.startsWith("#")){
			_userInput=_userInput.substring(1);
			if(_userInput!=NULL_STRING){
				hashtag=extractTillHashtagOrEnd();
				if(hashtag.contains(SPACE_STRING)){
					return NULL_STRING;
				}
				return hashtag;
			}else{
				return NULL_STRING;
			}
		}
		return NULL_STRING;
	}

	/**
	 * This function helps extractHashtag in extracts the 
	 * hashtags
	 * 
	 * @return String
	 * @throws Exception
	 */

	private String getHashtag()throws Exception {
		String hashtag ;
		hashtag=_userInput.substring(1);
		return hashtag;
	}


}//end of class Interpreter
