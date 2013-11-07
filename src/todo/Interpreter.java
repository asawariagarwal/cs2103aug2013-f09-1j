package todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.ParseException;

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
 * @author:Asawari
 */



public class Interpreter {

	protected static Logger interpreterLogger = Logger.getLogger("Interpreter");

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

	private void setInput(String inputString) {
		_userInput = inputString;
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
					interpreterLogger.log(Level.INFO,"Interpretation Complete:ADD");
				}else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return addObj;

			case VIEW:
				ViewCommand viewObj;
				viewObj = parseView();
				if(viewObj!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:VIEW");
				}else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return viewObj;

			case DELETE:
				DeleteCommand delObj;
				delObj = parseDelete();
				if(delObj!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:DELETE");}
				else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return delObj;

			case CLEAR:
				ClearCommand clrObj;
				clrObj = parseClear();
				if(clrObj!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:CLEAR");
				}
				else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return clrObj;


			case CHANGE:
				ModifyCommand modObj;
				modObj = parseChange();
				if(modObj!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:CHANGE");
				}else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return modObj;

			case RESCHEDULE:
				ModifyCommand resObj;
				resObj = parseReschedule();
				if(resObj!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:RESCEDULE");
				}else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return resObj;

			case SEARCH:
				SearchCommand searchObj;
				searchObj = parseSearch();
				if(searchObj!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:SEARCH");
				}else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return searchObj;

			case MARK:
				ModifyCommand markObj;
				markObj = parseMark();
				if(markObj!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:MARK");
				}else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				} return markObj;

			case UNMARK:
				ModifyCommand unmarkObj;
				unmarkObj = parseUnmark();
				if(unmarkObj!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:UNMARK");
				}else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return unmarkObj;

			case TAG:
				//TODO:TagCommand tagObj;
				//tagObj = parseTag();
				//return tagObjj;

			case UNTAG:
				//TODO:TagCommand untagObj;
				//untagObj = parseUntag();
				//return untagObj;		

			case UNDO:
				UndoCommand undoObj;
				undoObj = parseUndo();
				if(undoObj!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:PARSE");
				}else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return undoObj;

			case REDO:
				UndoCommand redoObj;
				redoObj=parseRedo();
				if(redoObj!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:REDO");
				}else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return redoObj;

				//The default command mode is ADD	
			default:
				getOriginalInput(commandTypeKeyword);
				AddCommand addObj2;
				addObj2 = parseAdd();
				if(addObj2!=null){
					interpreterLogger.log(Level.INFO,"Interpretation Complete:ADD");
				}else{
					interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");	
				}return addObj2;

			}
		} catch (Exception e) {
			System.err.println(EXCEPTION_MSG+ e.getMessage());
		}
		interpreterLogger.log(Level.INFO,"Interpretation Complete:INVALID");
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
	 * 
	 */

	private AddCommand parseAdd() throws Exception {
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
	 * the input to create a deadline type object
	 * 
	 * @return AddCommand type object or null
	 * 
	 */

	private AddCommand parseDeadlineTask()throws Exception  {

		ArrayList<String> hashtags = new ArrayList<String>();
		String TaskDes = getDeadlineTaskDescription();
		String dateStr;

		if(!TaskDes.equals("")){

			dateStr = extractTillHashtagOrEnd();
			Calendar deadline = isValid(dateStr);
			if (deadline!=(null)) {

				if(populateHashtags(hashtags)==0)
					return null;

				DeadlineTask TaskObj = new DeadlineTask(TaskDes, hashtags, deadline);
				AddCommand command = new AddCommand(TaskObj);

				return command;

			}
		}

		return null;
	}

	/**
	 * This function is used in case the Task is a timed task and it parses 
	 * the input to create a timed task type object
	 * 
	 * @return AddCommand type object or null
	 * @throws ParseException
	 */

	private AddCommand parseTimedTask()throws Exception {

		ArrayList<String> hashtags = new ArrayList<String>();
		String date1, date2, day;
		String TaskDes = getTimedTaskDescription();

		if(!TaskDes.equals("")){

			date1 = extractTillKeyword(PREFIX_END_TIME);

			//Special case if user specifies day separate from time 
			if(_userInput.contains(" on ")){

				date2 = extractTillKeyword(PREFIX_DAY);
				day= extractTillHashtagOrEnd();
				date1=date1.concat(" "+day+" ");
				date2=date2.concat(" "+day+" ");
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
	 * the input to create a floating type object
	 * 
	 * @return AddCommand type object or null
	 */

	private AddCommand parseFloatingTask() throws Exception{

		ArrayList<String> hashtags = new ArrayList<String>();
		String TaskDes = extractTillHashtagOrEnd();


		if(populateHashtags(hashtags)==0)
			return null;

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

	private ViewCommand parseView() throws Exception  {
		ViewCommand command;
		Calendar calendar[] = new Calendar[1];
		calendar[0] = null;
		if (isViewCommandDisplayingAllTasks()) {
			command = new ViewCommand(ViewCommand.MODE_VIEW_ALL);
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

	private DeleteCommand parseDelete() throws Exception {
		if (!_userInput.equals("")) {
			DeleteCommand command = new DeleteCommand(_userInput);
			return command;
		} else {
			return null;
		}
	}

	/**
	 * This function performs the actual parsing of a clear type command and
	 * creates a clear command type object
	 * 
	 * @return ClearCommand type object or null
	 * 
	 */

	private ClearCommand parseClear() throws Exception {
		ClearCommand command;
		_userInput=_userInput.trim();
		if(_userInput.equals("")){
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
		}
		else{
			return null;
		}
		return command;
	}



	/**
	 * This function performs the actual parsing of a change type command and
	 * creates a modify command type object
	 * 
	 * @return ModifyCommand type object
	 * 
	 */

	private ModifyCommand parseChange()throws Exception {
		String oldTask, newTask;
		oldTask = getModifyTaskDescription().trim();
		newTask = _userInput.trim();
		ModifyCommand command = new ModifyCommand(oldTask, newTask);
		return command;
	}

	/**
	 * This function performs the actual parsing of a tag type command and
	 * creates a tag command type object
	 * 
	 * @return TagCommand type object or null
	 * 
	 */

	private ModifyCommand parseReschedule()throws Exception{

		ModifyCommand command;
		String copyInput; 
		copyInput=_userInput;

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
	 * This function performs the actual parsing of a reschedule type command
	 * and creates a modify command type object
	 * 
	 * @return ModifyCommand type object or null
	 * 
	 */

	private ModifyCommand parseRescheduleTimed()throws Exception  {
		String taskName ;
		String startDate, endDate;
		taskName= getModifyTaskDescription().trim();

		if(!taskName.equals("")){

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



	private ModifyCommand parseRescheduleDeadline()throws Exception {
		String taskName ;
		String deadline;

		taskName= getModifyTaskDescription().trim();
		if(!taskName.equals("")){

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
	 * This function performs the actual parsing of a search type command and
	 * creates a search command type object
	 * 
	 * @return SearchCommand type object or null
	 * 
	 */

	private SearchCommand parseSearch()throws Exception{
		if(!_userInput.equals("")){
			SearchCommand command = new SearchCommand(_userInput.trim());
			return command;
		}else{
			return null;
		}
	}

	/**
	 * This function performs the actual parsing of a mark type command and
	 * creates a mark command type object
	 * 
	 * @return MarkCommand type object or null
	 * 
	 */

	private ModifyCommand parseMark()throws Exception{
		if(!_userInput.equals("")){
			ModifyCommand command = new ModifyCommand(_userInput.trim(),true);
			return command;
		}else{
			return null;
		}
	}

	/**
	 * This function performs the actual parsing of a unmark type command and
	 * creates a mark command type object
	 * 
	 * @return MarkCommand type object or null
	 * 
	 */

	private ModifyCommand parseUnmark()throws Exception{
		if(!_userInput.equals("")){
			ModifyCommand command = new ModifyCommand(_userInput.trim(),false);
			return command;
		}else{
			return null;
		}
	}


	/**
	 * This function performs the actual parsing of a tag type command and
	 * creates a tag command type object
	 * 
	 * @return TagCommand type object or null
	 * 
	 */

	//TODO: private TagCommand parseTag(){
	//ArrayList<String> hashtags = new ArrayList<String>();
	//String taskDes=extractTillHashtagOrEnd();
	//if(!taskDes.equals("")){
	//while (!(_userInput.equals(""))) {
	//String hashtag=extractHashtag();
	//if(hashtag.equals("")){
	//return null;
	//}
	//hashtags.add(hashtag);
	//TagCommand command = new TagCommand(taskDes,hashtags, 1);
	//return command;
	//}else{
	//return null;
	//}
	//}

	/**
	 * This function performs the actual parsing of a untag type command and
	 * creates a tag command type object
	 * 
	 * @return TagCommand type object or null
	 * 
	 */

	//TODO: private TagCommand parseUntag(){
	//ArrayList<String> hashtags = new ArrayList<String>();
	//String taskDes=extractTillHashtagOrEnd();
	//if(!taskDes.equals("")){
	//while (!(_userInput.equals(""))) {
	//String hashtag=extractHashtag();
	//if(hashtag.equals("")){
	//return null;
	//}
	//hashtags.add(hashtag);
	//TagCommand command = new TagCommand(taskDes,hashtags, -1);
	//return command;
	//}else{
	//return null;
	//}
	//}



	/**
	 * This function performs the actual parsing of a undo type command
	 * and creates a undo command type object
	 * 
	 * @return UndoCommand type object or null
	 */

	private UndoCommand parseUndo()throws Exception{
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

	private UndoCommand parseRedo()throws Exception{
		UndoCommand command;
		_userInput=_userInput.trim();
		if(_userInput.equals("")){
			command = new UndoCommand(1, true);
		}else{
			command = new UndoCommand((Integer.parseInt(_userInput)), true);
		}
		return command;
	}


	private boolean isViewCommandDisplayingAllTasks() throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_ALL)) {
			return true;
		}
		return false;
	}

	private boolean isViewCommandDisplayingFloatingTasks() throws Exception {
		if (_userInput.equals(KEYWORD_FLOATING)) {
			return true;
		}
		return false;
	}

	private boolean isViewCommandDisplayingTimedTasks()throws Exception {
		if (_userInput.equals(KEYWORD_TIMED)) {
			return true;
		}
		return false;
	}

	private boolean isViewCommandDisplayingDeadLineTasks()throws Exception {
		if (_userInput.equals(KEYWORD_DEADLINE)) {
			return true;
		}
		return false;
	}


	private boolean isViewCommandDisplayingTasksWithHashtag()throws Exception {
		if (_userInput.startsWith("#")) {
			return true;
		}
		return false;
	}

	private boolean isViewCommandDisplayingTasksOnADate(Calendar[] calendar)throws Exception {
		String dateStr=_userInput.trim();

		calendar[0] = isValid(dateStr);
		if(calendar[0]!=(null)){
			return true;
		}

		return false;
	}

	private boolean isClearCommandDeadline()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_DEADLINE)) {
			return true;
		}
		return false;
	}

	private boolean isClearCommandTimed()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_TIMED)) {
			return true;
		}
		return false;
	}

	private boolean isClearCommandFloating()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_FLOATING)) {
			return true;
		}
		return false;
	}

	private boolean isClearCommandDone()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_DONE)) {
			return true;
		}
		return false;
	}

	private boolean isClearCommandExpired()throws Exception {
		_userInput=_userInput.trim();
		if (_userInput.equals(KEYWORD_EXPIRED)) {
			return true;
		}
		return false;
	}


	/**
	 * This function returns the command type part of the user input and also
	 * truncates the user input
	 * 
	 * Return Type: string
	 * 
	 */

	private String getCommandType() throws Exception{
		String commandType;
		int locationOfSpace = _userInput.indexOf(" ");
		if(locationOfSpace==-1)
		{
			_userInput=_userInput.concat(" ");
			locationOfSpace = _userInput.indexOf(" ");
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
	 */

	private void getOriginalInput(String commandTypeKeyword)throws Exception{
		_userInput = commandTypeKeyword.concat(" "+_userInput);
	}

	/**
	 * This function is used to extract the task description of a deadline type task
	 * @return String TaskDes
	 */

	private String getDeadlineTaskDescription()throws Exception {
		String TaskDes = extractTillKeyword(PREFIX_DEADLINE);
		while (_userInput.contains(PREFIX_DEADLINE)) {
			TaskDes= TaskDes.concat(PREFIX_DEADLINE);
			TaskDes=TaskDes.concat(extractTillKeyword(PREFIX_DEADLINE));
		}
		return TaskDes;
	}

	/**
	 * This function is used to extract the task description of a timed type task
	 * @return String TaskDes
	 */

	private String getTimedTaskDescription()throws Exception {
		String TaskDes = extractTillKeyword(PREFIX_START_TIME);
		while (_userInput.contains(PREFIX_START_TIME)) {
			TaskDes=TaskDes.concat(PREFIX_START_TIME);
			TaskDes=TaskDes.concat(extractTillKeyword(PREFIX_START_TIME));
		}
		return TaskDes;
	}


	private String getModifyTaskDescription()throws Exception {
		String TaskDes = extractTillKeyword(PREFIX_END_TIME);
		while (_userInput.contains(PREFIX_END_TIME)) {
			TaskDes=TaskDes.concat(PREFIX_END_TIME);
			TaskDes=TaskDes.concat(extractTillKeyword(PREFIX_END_TIME));
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
	 */

	private int populateHashtags(ArrayList<String> hashtags)throws Exception {
		while (!(_userInput.equals(""))) {
			String hashtag=extractHashtag();
			if(hashtag.equals("")){
				return 0;
			}
			hashtags.add(hashtag);
		}
		return 1;
	}

	String dateManipulator(String dateStr)throws Exception{
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
			newdate=mm+"-"+dd;
			dateStr=dateStr.replaceFirst("\\d{2}/\\d{2}", newdate);	
		}
		else if (dateStr.matches(sregex21)){
			slash =dateStr.indexOf("/");
			dd=dateStr.substring(slash-2,slash);
			m=dateStr.charAt(slash+1);
			newdate=m+"-"+dd;
			dateStr=dateStr.replaceFirst("\\d{2}/\\d{1}", newdate);	
		}
		else if (dateStr.matches(sregex12)){
			slash =dateStr.indexOf("/");
			d=dateStr.charAt(slash-1);
			mm=dateStr.substring(slash+1,slash+3);
			newdate=mm+"-"+d;
			dateStr=dateStr.replaceFirst("\\d{1}/\\d{2}", newdate);	
		}
		else if (dateStr.matches(sregex11)){
			slash =dateStr.indexOf("-");
			d=dateStr.charAt(slash-1);
			m=dateStr.charAt(slash+1);
			newdate=m+"-"+d;
			dateStr=dateStr.replaceFirst("\\d{1}/\\d{1}", newdate);	
		}		
		return dateStr;
	}


	/**This functions checks the validity of the date using the Parser defined by natty
	 * 
	 *It returns a calendar object if date is valid else returns null
	 * 
	 * @param Date_str
	 * @return Calendar object
	 */

	private Calendar isValid(String Date_str)throws Exception {
		try{
			Date_str=dateManipulator(Date_str);
			interpreterLogger.log(Level.INFO,Date_str);
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
		}
		catch (NoClassDefFoundError e) {
			System.err.println("NoClassDefFoundError: " + e.getMessage());
		}
		return null;
	}

	/**
	 * This function extracts and returns the string from the beginning to the
	 * keyword specified from the user input and also truncates the user input
	 * 
	 * Return Type: string
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
		return "";// returns extracted string just before keyword
	}

	
	/**
	 * This function extract the string in user input upto the end or upto the
	 * hashtags
	 * 
	 * Return Type : String
	 */
	private String extractTillHashtagOrEnd()throws Exception {

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
	private String extractHashtag()throws Exception {
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

	private String getHashtag()throws Exception {
		String hashtag ;
		hashtag=_userInput.substring(1);
		return hashtag;
	}


}//end of class Interpreter
