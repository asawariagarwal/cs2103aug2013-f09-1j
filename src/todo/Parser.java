package todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import todo.Command.ViewCommand;

/**
 * This class parses the user input to convert it into a command type object
 * 
 * @author:Asawari
 */

public class Parser {

	/**
	 * Stores the user input
	 */
	private String _userInput;

	/**
	 * Constructor to initialize the user input
	 */

	Parser(String userInput) {
		_userInput = userInput;
	}

	/*
	 * Not sure if main is necessary or the function will be called directly
	 * from the other class public boolean main(string userInput) { Parser
	 * parse_obj=new Parser(userInput); if(parse_obj.parseInput()) return true;
	 * else return false; }
	 */

	/**
	 * This function is used to check if the string inputed by the user is valid
	 * or not If the input is valid it returns true and creates a command object
	 * Else it returns false
	 * 
	 * Return Type: boolean
	 */

	public boolean parseInput() { // return true is the user Input is valid else
									// return false

		String first_word;
		first_word = extractFirstWord();

		// Switch the first word to see command type and process it accordingly
		switch (first_word) {

		case "add": {
			if (_userInput.equals("")) {
				return false;
			} else {
				return parseAdd();
			}

		}

		case "view": {
			if (_userInput.equals("")) {
				return false;
			} else {
				if (_userInput.equals("all ordered by tags")) {
					ViewCommand command = new ViewCommand(false, true);
				} else if (_userInput.equals("floating")) {
					ViewCommand command = new ViewCommand(true);
				} else if (_userInput.startsWith("#")) {
					String hashtag = extractFirstWord();
					if (_userInput != "") {
						return false;
					}
					ViewCommand command = new ViewCommand(hashtag);
				} else {
					String Date_str;
					Date_str = extractTillKeyword("ordered");
					if (isDateValid(Date_str) && (_userInput.equals("by tags"))) {

						// The Following lines are used to create a calendar
						// type object using a date string
						SimpleDateFormat curFormater = new SimpleDateFormat(
								"dd/MM/yyyy");
						Date dateObj = curFormater.parse(Date_str);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(dateObj);

					} else {
						return false;
					}
					ViewCommand command = new ViewCommand(calendar);
				}

			}
			return true;
		}

		/*
		 * The following commands haven't been implemented yet so the code has
		 * been commeted out for the walking skeleton
		 * 
		 * case "delete":{ if(_userInput.equals("")){ return false; } else {
		 * if(_userInput.equals("all expired")){ //create DeleteAllExpiredObject
		 * and call command using it } else if(_userInput.equals("all done")){
		 * //create DeleteAllDoneObject and call command using it } else
		 * if(_userInput.startsWith("all #")){ String hashtag=
		 * extractFirstWord(); if(_userInput!=""){ return false; } //create
		 * DeleteHashtagObject and call command using it } else{ String
		 * taskTitle=_userInput; //create DeleteTaskObject and call command
		 * using it } return true; } }
		 * 
		 * 
		 * 
		 * case "change":{ if(_userInput.equals("")){ return false; } else{
		 * String old_task, new_task; old_task= extractTillKeyword("to");
		 * new_task=_userInput; //create ChangeObject and call command using it
		 * } return true; }
		 * 
		 * 
		 * case "reschedule":{ if(_userInput.equals("")){ return false; } else{
		 * String task_name; task_name=extractTillKeyword("to");
		 * if(_userInput.contains("to")){ String
		 * date_start=extractTillKeyword("to"); String date_end=_userInput;
		 * if((isDateValid(date_start))&&(isDateValid(date_end))){ //Create Date
		 * Type objects for both } else{ return false; } //Create
		 * Reschedule2TimeObject and call command using it } else{ String date=
		 * _userInput; if(isdateValid(date)){ //Create date type object } else{
		 * return false; } //Create RescheduleObject and call command using it }
		 * 
		 * } }
		 * 
		 * 
		 * case "mark":{ if(_userInput.equals("")){ return false; } else{ String
		 * TaskDes= extractTillKeyword(_userInput,"as"); String status =
		 * _userInput; //Create MarkObject and call command using it } return
		 * true; }
		 * 
		 * 
		 * case "drop":{ if(_userInput.equals("")){ return false; } else{
		 * if(_userInput.startsWith("#")){ String hashtag=
		 * extractTillKeyword(_userInput,"from"); String status = _userInput; }
		 * else{ return false; } //Create DropObject and call command using it }
		 * return true; }
		 * 
		 * 
		 * case "undo":{ if(_userInput.equals("")){ //create Undotype object and
		 * call command using it } else{ String num_to_undo = _userInput;
		 * //create UndoNumberObject and call command using it } return true;
		 * 
		 * }
		 * 
		 * 
		 * case "search":{ if(_userInput.equals("")){ return false; } else{
		 * String taskDes = _userInput; //create SearchtaskObject and call
		 * command using it } return true;
		 * 
		 * }
		 */

		default:
			return false;

		}// end of switch

	}// end of function

	/**
	 * This function returns the first word of the user input and also truncates
	 * the user input
	 * 
	 * Return Type: string
	 * 
	 */
	private String extractFirstWord()// removes first word from user Input
	{
		String first_word;
		int posOfSpace;
		posOfSpace = _userInput.indexOf(" ");
		first_word = _userInput.substring(0, posOfSpace);
		_userInput = _userInput.substring(posOfSpace + 1);
		return first_word; // add , delete, view, change, reschedule, mark ,
							// drop , undo , search , get
	}

	/**
	 * This function extracts and returns the string from the beginning to the
	 * keyword specified from the user input and also truncates the user input
	 * 
	 * Return Type: string
	 * 
	 */

	private String extractTillKeyword(String next_keyword)// removes the
															// extracted string
															// and keyword from
															// the Input
	{

		String extractedString;
		int posOfKeyword;
		posOfKeyword = _userInput.indexOf(next_keyword);
		extractedString = _userInput.substring(0, posOfKeyword);
		_userInput = (_userInput
				.substring(posOfKeyword + next_keyword.length())).trim();
		return extractedString;// returns extracted string just before keyword
	}

	/**
	 * This function uses a regex to check if the format of the date enetered is
	 * correct
	 * 
	 * Return Type is Boolean
	 */
	private boolean isDateValid(String Date_str)// Checks if the date format is
												// correct or not use regex here
												// to check
	{
		String regex = "\\d{2}[- /]\\d{2}[- /]\\d{4}";
		if (Date_str.matches(regex))
			return true;
		else
			return false;
	}

	/**
	 * This function Parses the input in case the command type is add
	 */

	private boolean  parseAdd()
	{
		
		if(!parseDeadlineTask())
		{
			if(!parseTimedTask())
			{
				return parseFloatingTask());
			}
			else 
			{
				return true;
			}
		}
		else
		{
			return true;
		}

	}

	/**
	 * This function is used in case the Task is a deadline task and it parses
	 * the input to create a deadline type object
	 */
	private boolean parseDeadlineTask() {

		ArrayList<String> hashtags = new ArrayList<String>();

		if (!_userInput.contains("by")) {
			return false;
		}
		String TaskDes = extractTillKeyword("by");
		while (_userInput.contains("by")) {
			TaskDes.concat(" ");
			TaskDes.concat(extractTillKeyword("by"));
		}
		String Date_str;
		Date_str = extractTillHashtagOrEnd();
		if (!isDateValid(Date_str)) {
			return false;
		}

		// The Following lines are used to create a calendar type object using a
		// date string
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
		Date dateObj = curFormater.parse(Date_str);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateObj);

		int i = 0;
		while (!_userInput.equals("")) {
			hashtags[i++] = extractHashtag(_userInput);
			if (hashtags[i].equals(""))
				return false;
		}

		DeadlineTask TaskObj = new DeadLineTask(TaskDes, hashtags, calendar);
		AddCommand command = new AddCommand(TaskObj);
		return true;
	}

	/**
	 * This function is used in case the task is a floating task and it parses
	 * the input to create a floating type object
	 */
	private boolean parseFloatingTask() {

		ArrayList<String> hashtags = new ArrayList<String>();
		String TaskDes = extractTillHashtagOrEnd(_userInput);
		int i = 0;
		while (input != "") {
			hashtags[i++] = extractHashtag(_userInput);
			if (hashtags[i] == "")
				return false;
		}

		FloatingTask TaskObj = new FloatinfTask(TaskDes, hashtags);
		AddCommand command = new AddCommand(TaskObj);
		return true;
	}

	/**
	 * This function is used in case the Task is a timed task and it parses the
	 * input to create a timed task type object
	 */
	private boolean parseTimedTask(string _userInput) {
		return true;
		ArrayList<String> hashtags = new ArrayList<String>();

		if (!_userInput.contains("from")) {
			return false;
		}
		String TaskDes = extractTillKeyword("from");
		while (_userInput.contains("from")) {
			TaskDes.concat(" ");
			TaskDes.concat(extractTillKeyword("from"));
		}
		if (!_userInput.contains("to")) {
			return false;
		}
		String Date1, Date2, Day = "";
		Date1 = extractTillKeyword("to");
		if (_userInput.contains("on")) {
			Date2 = extractTillKeyword("on");
			Day = extractTillHashtagOrEnd();
			Date1 = Date.concat(Day);
			Date2 = Date.concat(Day);
		} else {
			Date2 = extractTillHashtagOrEnd();
		}
		if (!isDateValid(Date1) || !isDateValid(Date2)) {
			return false;
		}

		// The Following lines are used to create a calendar type objects using
		// a date string
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
		Date dateObj = curFormater.parse(Date1);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateObj);

		SimpleDateFormat curFormater2 = new SimpleDateFormat("dd/MM/yyyy");
		Date dateObj2 = curFormater2.parse(Date2);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(dateObj2);

		int i = 0;
		while (!_userInput.equals("")) {
			hashtags[i++] = extractHashtag(_userInput);
			if (hashtags[i].equals(""))
				return false;
		}
		TimedTask taskObj = new TimedTask(TaskDes, hashtags, calendar1,
				calendar2);
		AddCommand command = new AddCommand(TaskObj);
		return true;

	}

	/**
	 * This function extract the string in user input upto the end or upto the
	 * hashtags
	 * 
	 * Return Type : String
	 */
	private String extractTillHashtagOrEnd() {

		String extractedString;
		if (_userInput.contains("#")) {
			int posOfHash = _userInput.indexOf("#");
			extractedString = _userInput.substring(0, posOfHash);
			_userInput = _userInput.substring(posOfHash);
		} else {
			extractString = _userInput;
		}
		return extractedString;
	}

	/**
	 * This function extracts the hashtags
	 */
	private String extractHashtag() {

		if (_userInput.startsWith("#"))
			return extractFirstWord(_userInput);
		else
			return "";

	}

}