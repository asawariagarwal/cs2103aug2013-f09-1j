package todo;

import java.io.BufferedReader;
import java.util.logging.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class implements storage to file
 * saving a textfile formatted in JSON-encoding
 * 
 * @author A0100784L
 *
 */
public class JSONStorage {
	
	/**
	 * Specifies storage filepath
	 */
	private static String _filename = "C:\\ToDo\\taskstore.txt";
	
	/**
	 * Specifies constants for converting Calendar to string
	 */
	private static final String TIMED_FORMAT = "hh:mm aa 'on' EEEEEEEEE ',' dd MMMMMMMMM, yyyy ";
	private static final String DEADLINE_FORMAT = " 'by' hh:mm aa 'on' EEEEEEEEE',' dd MMMMMMMMM',' yyyy ";
	private static final String INITIAL_FEEDBACK = "Welcome";
	
	protected static Logger storageLogger = Logger.getLogger("JSONStorage");
	
	/**
	 * Specifies labels used in Storage
	 */
	private static final String FLOATING_LABEL = "floating";
	private static final String TIMED_LABEL = "timed";
	private static final String DEADLINE_LABEL = "deadline";
	
	/**
	 * Specifies labels for task properties in Storage
	 */
	private static final String TASK_END_LABEL = "to";
	private static final String TASK_START_LABEL = "from";
	private static final String TASK_DONE_FLAG = "done";
	private static final String TASK_TAGS_LABEL = "tags";
	private static final String TASK_DESCRIPTION_LABEL = "description";
	
	/**
	 * Defines Log Message String
	 */
	private static final String LOG_FILE_WRITE_START = "Writing to file";
	private static final String LOG_JSON_ARRAY_START = "Putting Task Arrays into JSON";
	private static final String LOG_DEADLINE_WRITE_BEGIN = "Adding Deadline";
	private static final String LOG_TIMED_WRITE_BEGIN = "Adding Timed";
	private static final String LOG_FLOATING_WRITE_BEGIN = "Adding Floating";
	private static final String LOG_SINGLE_FLOATING_WRITE_SUCCESS = "Created Floating JSON";
	private static final String LOG_SINGLE_TIMED_WRITE_SUCCESS = "Created Timed JSON";
	private static final String LOG_SINGLE_DEADLINE_WRITE_SUCCESS = "Created Deadline JSON";
	private static final String LOG_SINGLE_FLOATING_READ_SUCCESS = "Floating Task Created";
	private static final String LOG_SINGLE_TIMED_READ_SUCCESS = "Timed Task Created";
	private static final String LOG_SINGLE_DEADLINE_READ_SUCCESS = "Deadline Task Created";
	private static final String LOG_PARSE_END_SUCCESS = "Done Parsing";
	private static final String LOG_DEADLINE_PARSE_START = "Parsing Deadline";
	private static final String LOG_TIMED_PARSE_START = "Parsing Timed";
	private static final String LOG_FLOATING_PARSE_START = "Parsing Floating";
	private static final String LOG_READ_SUCCESS = "read done";
	private static final String LOG_EXISTING_STORAGE = "reading from existing file";
	private static final String LOG_NEW_STORAGE = "creating new file";
	private static final String LOG_AFTER_JSON_READ = "Got from JSON";
	private static final String LOG_BEFORE_JSON_READ = "Getting from JSON";
	private static final String LOG_ALL_TAG_ADDED_SUCCESS = "Added all tags";
	private static final String LOG_SAVE_STATE_CHECK_END = "Checks finished successfully";
	private static final String LOG_SAVE_STATE_CHECK_START = "Checking Save State";
	
	/**
	 * Defines Exception Messages
	 */
	private static final String NULL_TIMED_EXCEPTION = "Storage received null timed task set";
	private static final String NULL_FLOATING_EXCEPTION = "Storage received null floating task set";
	private static final String NULL_DEADLINE_EXCEPTION = "Storage received null deadline task set";
	private static final String NULL_STATE_EXCEPTION = "Storage received null state";
	
	
	/**
	 * Method to read from store 
	 * 
	 * @return state containing information read from store
	 * @throws IOException in case of IOError in opening/closing file
	 * @throws ParseException in case of error when parsing file data
	 * @throws java.text.ParseException in case of error when parsing file data
	 */
	protected State readStore() throws IOException, ParseException, java.text.ParseException{
		File storeFile = new File(_filename);
		boolean isCreated = storeFile.isFile();
		if (!isCreated){
			storageLogger.log(Level.INFO, LOG_NEW_STORAGE);
			storeFile.getParentFile().mkdirs();
			storeFile.createNewFile();
			return new State();
		} else {
			storageLogger.log(Level.INFO, LOG_EXISTING_STORAGE);
			BufferedReader storeReader = new BufferedReader (new FileReader (storeFile));
			String jsonStore = storeReader.readLine();
			storeReader.close();
			storageLogger.log(Level.INFO, LOG_READ_SUCCESS);
			return parseStore(jsonStore);
		}
	}
	
	/**
	 * Method to write state to storage
	 * 
	 * @param saveState contains state to write
	 * @throws IOException in case of error opening/writing file
	 */
	@SuppressWarnings("unchecked")
	protected void writeStore(State saveState) throws IOException{
		
		JSONObject jsonStore = new JSONObject();
		
		JSONArray floatingArray = new JSONArray();
		JSONArray timedArray = new JSONArray();
		JSONArray deadlineArray = new JSONArray();
		checkUsableState(saveState);
		
		storageLogger.log(Level.FINE, LOG_FLOATING_WRITE_BEGIN); 
		for(FloatingTask floatingTask: saveState.getFloatingTasks()){
			floatingArray.add(getFloatingJSON(floatingTask));
		}
		
		storageLogger.log(Level.FINE, LOG_TIMED_WRITE_BEGIN); 
		for(TimedTask timedTask: saveState.getTimedTasks()){
			timedArray.add(getTimedJSON(timedTask));
		}
		
		storageLogger.log(Level.FINE, LOG_DEADLINE_WRITE_BEGIN); 
		for(DeadlineTask deadlineTask: saveState.getDeadlineTasks()){
			deadlineArray.add(getDeadlineJSON(deadlineTask));
		}
		
		storageLogger.log(Level.INFO, LOG_JSON_ARRAY_START);
		jsonStore.put(FLOATING_LABEL,floatingArray);
		jsonStore.put(TIMED_LABEL, timedArray);
		jsonStore.put(DEADLINE_LABEL, deadlineArray);
		
		File saveFile = new File(_filename);
		
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(saveFile));
		
		storageLogger.log(Level.INFO, LOG_FILE_WRITE_START);
		fileWriter.write(jsonStore.toJSONString());
		
		fileWriter.close();
		
	}
	
	/**
	 * Method to parse a JSONString to a state
	 * 
	 * @param jsonString contains input in JSON formatting passed to it
	 * @return a State containing the information from JSON
	 * @throws ParseException if the file is not formatted correctly
	 * @throws java.text.ParseException if the file is not formatted correctly
	 */
	private State parseStore(String jsonString) throws ParseException, java.text.ParseException{
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(jsonString);
		JSONObject storeObject = (JSONObject) obj;
		
		JSONArray floatingArray = (JSONArray) storeObject.get(FLOATING_LABEL);
		JSONArray timedArray = (JSONArray) storeObject.get(TIMED_LABEL);
		JSONArray deadlineArray = (JSONArray) storeObject.get(DEADLINE_LABEL);
		
		State savedState = new State();
		
		storageLogger.log(Level.FINE, LOG_FLOATING_PARSE_START); 
		for (Object floatObj : floatingArray){
			FloatingTask floatingTask = getFloatingTask(floatObj);
			savedState.addTask(floatingTask);
		}
		
		storageLogger.log(Level.FINE, LOG_TIMED_PARSE_START); 
		for (Object timedObj : timedArray){
			TimedTask timedTask = getTimedTask(timedObj);
			savedState.addTask(timedTask);
		}
		
		storageLogger.log(Level.FINE, LOG_DEADLINE_PARSE_START); 
		for (Object deadlineObj : deadlineArray){
			DeadlineTask deadlineTask = getDeadlineTask(deadlineObj);
			savedState.addTask(deadlineTask);
		}
		
		savedState.setFeedback(new Feedback(INITIAL_FEEDBACK,true));
		
		storageLogger.log(Level.INFO, LOG_PARSE_END_SUCCESS);
		return savedState;
	}

	/**
	 * Method to generate a DeadlineTask object from a JSONObject
	 * 
	 * @param deadlineObj specifies JSONObject containing DeadlineTask information
	 * @return the DeadlineTask specified
	 * @throws java.text.ParseException if incorrectly formatted JSON
	 */
	@SuppressWarnings("unchecked")
	private DeadlineTask getDeadlineTask(Object deadlineObj)
			throws java.text.ParseException {
		
		storageLogger.log(Level.FINE, LOG_BEFORE_JSON_READ);
		JSONObject deadlineTaskJSON = (JSONObject) deadlineObj;
		String taskDescription = (String) deadlineTaskJSON.get(TASK_DESCRIPTION_LABEL);
		ArrayList<String> tags = (ArrayList<String>) deadlineTaskJSON.get(TASK_TAGS_LABEL);
		String deadlineString = (String) deadlineTaskJSON.get(DEADLINE_LABEL);
		boolean isCompleted = (boolean) deadlineTaskJSON.get(TASK_DONE_FLAG);
		storageLogger.log(Level.FINE, LOG_AFTER_JSON_READ);
		
		String deadlineFormat = DEADLINE_FORMAT;
		SimpleDateFormat curFormater = new SimpleDateFormat(deadlineFormat);
		Date deadlineDate = curFormater.parse(deadlineString);
		
		Calendar deadlineCalendar = Calendar.getInstance();
		deadlineCalendar.setTime(deadlineDate);
		
		DeadlineTask deadlineTask = new DeadlineTask(taskDescription, tags, deadlineCalendar);
		if (isCompleted){
			deadlineTask.markAsDone();
		}
		storageLogger.log(Level.FINE, LOG_SINGLE_DEADLINE_READ_SUCCESS);
		return deadlineTask;
	}

	/**
	 * Method to generate a TimedTask object from a JSONObject
	 * 
	 * @param timedObj specifies JSONObject containing TimedTask information
	 * @return the TimedTask specified
	 * @throws java.text.ParseException if incorrectly formatted JSON
	 */
	@SuppressWarnings("unchecked")
	private TimedTask getTimedTask(Object timedObj)
			throws java.text.ParseException {
		storageLogger.log(Level.FINER, LOG_BEFORE_JSON_READ);
		JSONObject timedTaskJSON = (JSONObject) timedObj;
		String taskDescription = (String) timedTaskJSON.get(TASK_DESCRIPTION_LABEL);
		ArrayList<String> tags = (ArrayList<String>) timedTaskJSON.get(TASK_TAGS_LABEL);
		boolean isCompleted = (boolean) timedTaskJSON.get(TASK_DONE_FLAG);
		
		String startString = (String) timedTaskJSON.get(TASK_START_LABEL);
		String endString = (String) timedTaskJSON.get(TASK_END_LABEL);
		storageLogger.log(Level.FINER, LOG_AFTER_JSON_READ);
		
		String timedTaskFormat = TIMED_FORMAT;
		SimpleDateFormat curFormater = new SimpleDateFormat(timedTaskFormat);
		Date fromDate = curFormater.parse(startString);
		Date toDate = curFormater.parse(endString);
		
		Calendar fromCalendar = Calendar.getInstance();
		Calendar toCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);
		toCalendar.setTime(toDate);
		
		TimedTask timedTask = new TimedTask(taskDescription, tags, fromCalendar, toCalendar);
		if (isCompleted){
			timedTask.markAsDone();
		}
		storageLogger.log(Level.FINER, LOG_SINGLE_TIMED_READ_SUCCESS);
		return timedTask;
	}

	/**
	 * Method to generate a FloatingTask object from a JSONObject
	 * 
	 * @param floatObj specifies JSONObject containing FloatingTask information
	 * @return the FloatingTask specified
	 */
	@SuppressWarnings("unchecked")
	private FloatingTask getFloatingTask(Object floatObj) {
		storageLogger.log(Level.FINER, LOG_BEFORE_JSON_READ);
		JSONObject floatingTaskJSON = (JSONObject) floatObj;
		String taskDescription = (String) floatingTaskJSON.get(TASK_DESCRIPTION_LABEL);
		ArrayList<String> tags = (ArrayList<String>) floatingTaskJSON.get(TASK_TAGS_LABEL);
		boolean isCompleted = (boolean) floatingTaskJSON.get(TASK_DONE_FLAG);
		storageLogger.log(Level.FINER, LOG_AFTER_JSON_READ);
		
		FloatingTask floatingTask = new FloatingTask(taskDescription, tags);
		if (isCompleted){
			floatingTask.markAsDone();
		}
		storageLogger.log(Level.FINER, LOG_SINGLE_FLOATING_READ_SUCCESS);
		return floatingTask;
	}

	/**
	 * Method to check if any null elements in state
	 * 
	 * @param saveState specifies state to check
	 * @throws IOException if any null objects
	 */
	private void checkUsableState(State saveState) throws IOException {
		storageLogger.log(Level.INFO, LOG_SAVE_STATE_CHECK_START);
		if (saveState == null){
			IOException e = new IOException(NULL_STATE_EXCEPTION);
			throw e;
		} else{
			if (saveState.getDeadlineTasks()==null){
				IOException e = new IOException(NULL_DEADLINE_EXCEPTION);
				throw e;
			} if (saveState.getFloatingTasks()==null){
				IOException e = new IOException(NULL_FLOATING_EXCEPTION);
				throw e;
			} if (saveState.getTimedTasks() == null){
				IOException e = new IOException(NULL_TIMED_EXCEPTION);
				throw e;
			}
		}
		storageLogger.log(Level.FINE,LOG_SAVE_STATE_CHECK_END);
	}
	
	/**
	 * Method to get JSONObject from FloatingTask
	 * 
	 * @param task specifies FloatingTask to convert
	 * @return JSONObject with floating task information
	 */
	@SuppressWarnings("unchecked")
	private JSONObject getFloatingJSON(FloatingTask task){
		JSONObject taskObject = new JSONObject();
		taskObject.put(TASK_DESCRIPTION_LABEL,task.getTaskDescription());
		JSONArray tags = getTagsJSON(task.getTags());
		taskObject.put(TASK_TAGS_LABEL,tags);
		taskObject.put(TASK_DONE_FLAG,task.isComplete());
		storageLogger.log(Level.FINE, LOG_SINGLE_FLOATING_WRITE_SUCCESS);
		return taskObject;
	}
	
	/**
	 * Method to get JSONObject from TimedTask
	 * 
	 * @param task specifies TimedTask to convert
	 * @return JSONObject with timed task information
	 */
	@SuppressWarnings("unchecked")
	private JSONObject getTimedJSON(TimedTask task){
		JSONObject taskObject = new JSONObject();
		taskObject.put(TASK_DESCRIPTION_LABEL,task.getTaskDescription());
		JSONArray tags = getTagsJSON(task.getTags());
		taskObject.put(TASK_TAGS_LABEL,tags);
		
		String timedTaskFormat = TIMED_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(timedTaskFormat);
		
		String startString = sdf.format(task.getStartDate().getTime());
		String endString = sdf.format(task.getEndDate().getTime());
		
		taskObject.put(TASK_START_LABEL, startString);
		taskObject.put(TASK_END_LABEL, endString);
		taskObject.put(TASK_DONE_FLAG,task.isComplete());
		storageLogger.log(Level.FINE, LOG_SINGLE_TIMED_WRITE_SUCCESS);
		return taskObject;
		
	}
	
	/**
	 * Method to get JSONObject from DeadlineTask
	 * 
	 * @param task specifies DeadlineTask to convert
	 * @return JSONObject with TimedTask information
	 */
	@SuppressWarnings("unchecked")
	private JSONObject getDeadlineJSON(DeadlineTask task){
		JSONObject taskObject = new JSONObject();
		taskObject.put(TASK_DESCRIPTION_LABEL,task.getTaskDescription());
		JSONArray tags = getTagsJSON(task.getTags());
		taskObject.put(TASK_TAGS_LABEL,tags);

		String deadlineFormat = DEADLINE_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(deadlineFormat);
		
		String deadlineString = sdf.format(task.getDeadline().getTime());
		
		taskObject.put(DEADLINE_LABEL, deadlineString);
		taskObject.put(TASK_DONE_FLAG,task.isComplete());
		storageLogger.log(Level.FINE, LOG_SINGLE_DEADLINE_WRITE_SUCCESS);
		return taskObject;
	}
	
	/**
	 * Method to get JSONArray of tags from ArrayList of tags
	 * 
	 * @param tagArray containing tag Strings
	 * @return required JSONArray
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getTagsJSON(TreeSet<String> tagArray){
		JSONArray tagJSON = new JSONArray();
		for (String tag : tagArray){
			tagJSON.add(tag);
		}
		storageLogger.log(Level.FINE, LOG_ALL_TAG_ADDED_SUCCESS);
		return tagJSON;
	}
	
}
