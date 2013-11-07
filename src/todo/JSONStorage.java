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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONStorage {
	
	private static String _filename = "C:\\ToDo\\taskstore.txt";
	private static final String TIMED_FORMAT = "hh:mm aa 'on' EEEEEEEEE ',' dd MMMMMMMMM, yyyy ";
	private static final String DEADLINE_FORMAT = " 'by' hh:mm aa 'on' EEEEEEEEE',' dd MMMMMMMMM',' yyyy ";
	
	protected static Logger storageLogger = Logger.getLogger("JSONStorage");
	
	/**
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	protected State readStore() throws IOException, ParseException, java.text.ParseException{
		File storeFile = new File(_filename);
		boolean isCreated = storeFile.isFile();
		if (!isCreated){
			storageLogger.log(Level.INFO, "creating new file");
			storeFile.getParentFile().mkdirs();
			storeFile.createNewFile();
			return new State();
		} else {
			storageLogger.log(Level.INFO, "reading from existing file");
			BufferedReader storeReader = new BufferedReader (new FileReader (storeFile));
			String jsonStore = storeReader.readLine();
			storeReader.close();
			storageLogger.log(Level.INFO, "read done");
			return parseStore(jsonStore);
		}
	}
	
	private State parseStore(String jsonString) throws ParseException, java.text.ParseException{
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(jsonString);
		JSONObject storeObject = (JSONObject) obj;
		
		JSONArray floatingArray = (JSONArray) storeObject.get("floating");
		JSONArray timedArray = (JSONArray) storeObject.get("timed");
		JSONArray deadlineArray = (JSONArray) storeObject.get("deadline");
		
		State savedState = new State();
		
		storageLogger.log(Level.FINE, "Parsing Floating"); 
		for (Object floatObj : floatingArray){
			FloatingTask floatingTask = getFloatingTask(floatObj);
			savedState.addTask(floatingTask);
		}
		
		storageLogger.log(Level.FINE, "Parsing Timed"); 
		for (Object timedObj : timedArray){
			TimedTask timedTask = getTimedTask(timedObj);
			savedState.addTask(timedTask);
		}
		
		storageLogger.log(Level.FINE, "Parsing Deadline"); 
		for (Object deadlineObj : deadlineArray){
			DeadlineTask deadlineTask = getDeadlineTask(deadlineObj);
			savedState.addTask(deadlineTask);
		}
		
		savedState.setFeedback(new Feedback("Welcome",true));
		
		storageLogger.log(Level.INFO, "Done Parsing");
		return savedState;
	}

	@SuppressWarnings("unchecked")
	private DeadlineTask getDeadlineTask(Object deadlineObj)
			throws java.text.ParseException {
		
		storageLogger.log(Level.FINE, "Getting from JSON");
		JSONObject deadlineTaskJSON = (JSONObject) deadlineObj;
		String taskDescription = (String) deadlineTaskJSON.get("description");
		ArrayList<String> tags = (ArrayList<String>) deadlineTaskJSON.get("tags");
		String deadlineString = (String) deadlineTaskJSON.get("deadline");
		boolean isCompleted = (boolean) deadlineTaskJSON.get("done");
		storageLogger.log(Level.FINE, "Got from JSON");
		
		String deadlineFormat = DEADLINE_FORMAT;
		SimpleDateFormat curFormater = new SimpleDateFormat(deadlineFormat);
		Date deadlineDate = curFormater.parse(deadlineString);
		
		Calendar deadlineCalendar = Calendar.getInstance();
		deadlineCalendar.setTime(deadlineDate);
		
		DeadlineTask deadlineTask = new DeadlineTask(taskDescription, tags, deadlineCalendar);
		if (isCompleted){
			deadlineTask.markAsDone();
		}
		storageLogger.log(Level.FINE, "Deadline Task Created");
		return deadlineTask;
	}

	@SuppressWarnings("unchecked")
	private TimedTask getTimedTask(Object timedObj)
			throws java.text.ParseException {
		storageLogger.log(Level.FINER, "Getting from JSON");
		JSONObject timedTaskJSON = (JSONObject) timedObj;
		String taskDescription = (String) timedTaskJSON.get("description");
		ArrayList<String> tags = (ArrayList<String>) timedTaskJSON.get("tags");
		boolean isCompleted = (boolean) timedTaskJSON.get("done");
		
		String startString = (String) timedTaskJSON.get("from");
		String endString = (String) timedTaskJSON.get("to");
		storageLogger.log(Level.FINER, "Got from JSON");
		
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
		storageLogger.log(Level.FINER, "Timed Task Created");
		return timedTask;
	}

	@SuppressWarnings("unchecked")
	private FloatingTask getFloatingTask(Object floatObj) {
		storageLogger.log(Level.FINER, "Getting from JSON");
		JSONObject floatingTaskJSON = (JSONObject) floatObj;
		String taskDescription = (String) floatingTaskJSON.get("description");
		ArrayList<String> tags = (ArrayList<String>) floatingTaskJSON.get("tags");
		boolean isCompleted = (boolean) floatingTaskJSON.get("done");
		storageLogger.log(Level.FINER, "Got from JSON");
		
		FloatingTask floatingTask = new FloatingTask(taskDescription, tags);
		if (isCompleted){
			floatingTask.markAsDone();
		}
		storageLogger.log(Level.FINER, "Floating Task Created");
		return floatingTask;
	}
	
	/**
	 * @param saveState
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected void writeStore(State saveState) throws IOException{
		
		JSONObject jsonStore = new JSONObject();
		
		JSONArray floatingArray = new JSONArray();
		JSONArray timedArray = new JSONArray();
		JSONArray deadlineArray = new JSONArray();
		if (saveState == null){
			storageLogger.log(Level.WARNING, "Null Saved State");
			IOException e = new IOException("Don't pass me null states");
			throw e;
		}
		
		storageLogger.log(Level.FINE, "Adding Floating"); 
		for(FloatingTask floatingTask: saveState.getFloatingTasks()){
			floatingArray.add(getFloatingJSON(floatingTask));
		}
		
		storageLogger.log(Level.FINE, "Adding Timed"); 
		for(TimedTask timedTask: saveState.getTimedTasks()){
			timedArray.add(getTimedJSON(timedTask));
		}
		
		storageLogger.log(Level.FINE, "Adding Deadline"); 
		for(DeadlineTask deadlineTask: saveState.getDeadlineTasks()){
			deadlineArray.add(getDeadlineJSON(deadlineTask));
		}
		
		storageLogger.log(Level.INFO, "Putting Task Arrays into JSON");
		jsonStore.put("floating",floatingArray);
		jsonStore.put("timed", timedArray);
		jsonStore.put("deadline", deadlineArray);
		
		File saveFile = new File(_filename);
		
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(saveFile));
		
		storageLogger.log(Level.INFO, "Writing to file");
		fileWriter.write(jsonStore.toJSONString());
		
		fileWriter.close();
		
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getFloatingJSON(FloatingTask task){
		JSONObject taskObject = new JSONObject();
		taskObject.put("description",task.getTaskDescription());
		JSONArray tags = getTagsJSON(task.getTags());
		taskObject.put("tags",tags);
		taskObject.put("done",task.isComplete());
		storageLogger.log(Level.FINE, "Created Floating JSON");
		return taskObject;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getTimedJSON(TimedTask task){
		JSONObject taskObject = new JSONObject();
		taskObject.put("description",task.getTaskDescription());
		JSONArray tags = getTagsJSON(task.getTags());
		taskObject.put("tags",tags);
		
		String timedTaskFormat = TIMED_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(timedTaskFormat);
		
		String startString = sdf.format(task.getStartDate().getTime());
		String endString = sdf.format(task.getEndDate().getTime());
		
		taskObject.put("from", startString);
		taskObject.put("to", endString);
		taskObject.put("done",task.isComplete());
		storageLogger.log(Level.FINE, "Created Timed JSON");
		return taskObject;
		
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getDeadlineJSON(DeadlineTask task){
		JSONObject taskObject = new JSONObject();
		taskObject.put("description",task.getTaskDescription());
		JSONArray tags = getTagsJSON(task.getTags());
		taskObject.put("tags",tags);

		String deadlineFormat = DEADLINE_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(deadlineFormat);
		
		String deadlineString = sdf.format(task.getDeadline().getTime());
		
		taskObject.put("deadline", deadlineString);
		taskObject.put("done",task.isComplete());
		storageLogger.log(Level.FINE, "Created Deadline JSON");
		return taskObject;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray getTagsJSON(ArrayList<String> tagArray){
		JSONArray tagJSON = new JSONArray();
		for (String tag : tagArray){
			tagJSON.add(tag);
		}
		storageLogger.log(Level.FINE, "Added all tags");
		return tagJSON;
	}
	
}
