package todo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class StorageManager {

	private static String _filename = "C:\\ToDo\\taskstore.txt";
	private final static String TASK_SEPARATOR = "\r\n";
	
	protected State readStore() throws IOException{
		File storeFile = new File(_filename);
		boolean isCreated = storeFile.isFile();
		if (!isCreated){
			storeFile.getParentFile().mkdirs();
			storeFile.createNewFile();
			return new State();
		} else {
			BufferedReader storeReader = new BufferedReader (new FileReader (storeFile));
			boolean endOfFile = false;
			State storeState = new State();
			do{
				String taskString = storeReader.readLine();
				if (taskString == null){
					endOfFile = true;
				}
				else {
					Task task = parseTaskString(taskString);
					storeState.addTask(task);
				}
			} while (!endOfFile);
			storeReader.close();
			return storeState;
		}
		
		
	}
	
	private Task parseTaskString(String taskString) {
		if (taskString.contains("by")){
			return parseDeadlineTaskString(taskString);
		} else if (taskString.contains("from") && taskString.contains("to")){
			return parseTimedTaskString(taskString);
		} else {
			return parseFloatingTaskString(taskString);
		}
	}

	private Task parseFloatingTaskString(String taskString) {
		
		ArrayList<String> tags = new ArrayList<String>();
		
		int firstTagCounter = taskString.indexOf('#');
		
		int tagCounter = firstTagCounter;
		int nextCounter = taskString.indexOf('#',tagCounter+1);
		while (tagCounter != -1){
			String nextString;
			if (nextCounter == -1){
				nextString = taskString.substring(tagCounter);
			} else {
				nextString = taskString.substring(tagCounter,nextCounter-1);
			}
			tags.add(nextString);
			tagCounter = nextCounter;
			nextCounter = taskString.indexOf('#',nextCounter+1);
		}
		
		String taskDescription;
		if (firstTagCounter == -1){
			taskDescription = taskString;
		} else {
			taskDescription = taskString.substring(0,firstTagCounter-1);
		}
		
		FloatingTask parsedTask = new FloatingTask(taskDescription, tags);
		return parsedTask;
	}

	private Task parseTimedTaskString(String taskString) {
		
		int indexOfFrom = taskString.indexOf("from");
		int indexOfTo = taskString.indexOf("to",indexOfFrom);
		
		String taskDescription = taskString.substring(0,indexOfFrom);
		
		String timedTaskFormat = "hh:mm aa 'on' EEEEEEEEE ',' dd MMMMMMMMM, yyyy ";
		
		ArrayList<String> tags = new ArrayList<String>();
		
		int firstTagCounter = taskString.indexOf('#');
		
		int tagCounter = firstTagCounter;
		int nextCounter = taskString.indexOf('#',tagCounter+1);
		while (tagCounter != -1){
			String nextString;
			if (nextCounter == -1){
				nextString = taskString.substring(tagCounter);
			} else {
				nextString = taskString.substring(tagCounter,nextCounter-1);
			}
			tags.add(nextString);
			tagCounter = nextCounter;
			nextCounter = taskString.indexOf('#',nextCounter+1);
		}
		
		int fromSpace = taskString.indexOf(' ', indexOfFrom);
		int toSpace = taskString.indexOf(' ', indexOfTo);
		
		String fromString = taskString.substring(fromSpace, indexOfTo);
		
		String toString;
		if (firstTagCounter == -1){
			toString = taskString.substring(toSpace);
		} else{
			toString = taskString.substring(toSpace, firstTagCounter-1); 
		}
		
		SimpleDateFormat curFormater = new SimpleDateFormat(timedTaskFormat);
		Date fromObj;
		Date toObj;
		try {
			fromObj = curFormater.parse(fromString);
			toObj = curFormater.parse(toString);
		} catch (ParseException e) {
			System.out.println("Read Error - Format - Timed");
			fromObj = null;
			toObj = null;
		}
		Calendar fromCalendar = Calendar.getInstance();
		Calendar toCalendar = Calendar.getInstance();
		
		fromCalendar.setTime(fromObj);
		toCalendar.setTime(toObj);
		
		TimedTask parsedTask = new TimedTask(taskDescription, tags, fromCalendar, toCalendar);
		return parsedTask;
		
	}

	private Task parseDeadlineTaskString(String taskString) {
		
		int indexOfBy = taskString.indexOf("by");
		String taskDescription = taskString.substring(0,indexOfBy-1);
		
		ArrayList<String> tags = new ArrayList<String>();
		
		int firstTagCounter = taskString.indexOf('#');
		
		int tagCounter = firstTagCounter;
		int nextCounter = taskString.indexOf('#',tagCounter+1);
		while (tagCounter != -1){
			String nextString;
			if (nextCounter == -1){
				nextString = taskString.substring(tagCounter);
			} else {
				nextString = taskString.substring(tagCounter,nextCounter-1);
			}
			tags.add(nextString);
			tagCounter = nextCounter;
			nextCounter = taskString.indexOf('#',nextCounter+1);
		}
		
		String deadlineFormat = " 'by' EEEEEEEEE',' dd MMMMMMMMM',' yyyy ";
		SimpleDateFormat curFormater = new SimpleDateFormat(deadlineFormat);
		Date dateObj;
		try {
			if (firstTagCounter == -1){
				dateObj = curFormater.parse(taskString.substring(indexOfBy-1));
			} else {
				dateObj = curFormater.parse(taskString.substring(indexOfBy-1,firstTagCounter-1));
			}
		} catch (ParseException e) {
			System.out.println("Read Error - Format");
			dateObj = null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateObj);

		
		DeadlineTask parsedTask = new DeadlineTask(taskDescription, tags, calendar);
		return parsedTask;
	}
	
	

	protected void writeStore(State saveState) throws IOException{
		File saveFile = new File(_filename);
		
		String writeOut = "";
		
		for(Task floatingTask: saveState.getFloatingTasks()){
			String taskString = floatingTask.toString();
			writeOut += (taskString + TASK_SEPARATOR);
		}
		
		for(Task timedTask: saveState.getTimedTasks()){
			String taskString = timedTask.toString();
			writeOut += (taskString + TASK_SEPARATOR);
		}
		
		for(Task deadlineTask: saveState.getDeadlineTasks()){
			String taskString = deadlineTask.toString();
			writeOut += (taskString + TASK_SEPARATOR);
		}
		
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(saveFile));
		
		fileWriter.write(writeOut);
		
		fileWriter.close();
		
	}
}
