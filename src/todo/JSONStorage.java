package todo;

import java.io.BufferedReader;
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
	
	protected State readStore() throws IOException, ParseException, java.text.ParseException{
		File storeFile = new File(_filename);
		boolean isCreated = storeFile.isFile();
		if (!isCreated){
			storeFile.getParentFile().mkdirs();
			storeFile.createNewFile();
			return new State();
		} else {
			BufferedReader storeReader = new BufferedReader (new FileReader (storeFile));
			String jsonStore = storeReader.readLine();
			storeReader.close();
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
		
		for (Object floatObj : floatingArray){
			FloatingTask floatingTask = getFloatingTask(floatObj);
			savedState.addTask(floatingTask);
		}
		
		for (Object timedObj : timedArray){
			TimedTask timedTask = getTimedTask(timedObj);
			savedState.addTask(timedTask);
		}
		
		for (Object deadlineObj : deadlineArray){
			DeadlineTask deadlineTask = getDeadlineTask(deadlineObj);
			savedState.addTask(deadlineTask);
		}
		
		savedState.setFeedback("Welcome");
		
		return savedState;
	}

	@SuppressWarnings("unchecked")
	private DeadlineTask getDeadlineTask(Object deadlineObj)
			throws java.text.ParseException {
		JSONObject deadlineTaskJSON = (JSONObject) deadlineObj;
		String taskDescription = (String) deadlineTaskJSON.get("description");
		ArrayList<String> tags = (ArrayList<String>) deadlineTaskJSON.get("tags");
		String deadlineString = (String) deadlineTaskJSON.get("deadline");
		
		String deadlineFormat = " 'by' EEEEEEEEE',' dd MMMMMMMMM',' yyyy ";
		SimpleDateFormat curFormater = new SimpleDateFormat(deadlineFormat);
		Date deadlineDate = curFormater.parse(deadlineString);
		
		Calendar deadlineCalendar = Calendar.getInstance();
		deadlineCalendar.setTime(deadlineDate);
		
		DeadlineTask deadlineTask = new DeadlineTask(taskDescription, tags, deadlineCalendar);
		return deadlineTask;
	}

	@SuppressWarnings("unchecked")
	private TimedTask getTimedTask(Object timedObj)
			throws java.text.ParseException {
		JSONObject timedTaskJSON = (JSONObject) timedObj;
		String taskDescription = (String) timedTaskJSON.get("description");
		ArrayList<String> tags = (ArrayList<String>) timedTaskJSON.get("tags");
		
		String startString = (String) timedTaskJSON.get("from");
		String endString = (String) timedTaskJSON.get("to");
		
		String timedTaskFormat = "hh:mm aa 'on' EEEEEEEEE ',' dd MMMMMMMMM, yyyy ";
		SimpleDateFormat curFormater = new SimpleDateFormat(timedTaskFormat);
		Date fromDate = curFormater.parse(startString);
		Date toDate = curFormater.parse(endString);
		
		Calendar fromCalendar = Calendar.getInstance();
		Calendar toCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);
		toCalendar.setTime(toDate);
		
		TimedTask timedTask = new TimedTask(taskDescription, tags, fromCalendar, toCalendar);
		return timedTask;
	}

	@SuppressWarnings("unchecked")
	private FloatingTask getFloatingTask(Object floatObj) {
		JSONObject floatingTaskJSON = (JSONObject) floatObj;
		String taskDescription = (String) floatingTaskJSON.get("description");
		ArrayList<String> tags = (ArrayList<String>) floatingTaskJSON.get("tags");
		FloatingTask floatingTask = new FloatingTask(taskDescription, tags);
		return floatingTask;
	}
	
	@SuppressWarnings("unchecked")
	protected void writeStore(State saveState) throws IOException{
		
		JSONObject jsonStore = new JSONObject();
		
		JSONArray floatingArray = new JSONArray();
		JSONArray timedArray = new JSONArray();
		JSONArray deadlineArray = new JSONArray();
		
		for(FloatingTask floatingTask: saveState.getFloatingTasks()){
			floatingArray.add(getFloatingJSON(floatingTask));
		}
		
		for(TimedTask timedTask: saveState.getTimedTasks()){
			timedArray.add(getTimedJSON(timedTask));
		}
		
		for(DeadlineTask deadlineTask: saveState.getDeadlineTasks()){
			deadlineArray.add(getDeadlineJSON(deadlineTask));
		}
		
		jsonStore.put("floating",floatingArray);
		jsonStore.put("timed", timedArray);
		jsonStore.put("deadline", deadlineArray);
		
		File saveFile = new File(_filename);
		
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(saveFile));
		
		fileWriter.write(jsonStore.toJSONString());
		
		fileWriter.close();
		
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getFloatingJSON(FloatingTask task){
		JSONObject taskObject = new JSONObject();
		taskObject.put("description",task.getTaskDescription());
		JSONArray tags = getTagsJSON(task.getTags());
		taskObject.put("tags",tags);
		return taskObject;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getTimedJSON(TimedTask task){
		JSONObject taskObject = new JSONObject();
		taskObject.put("description",task.getTaskDescription());
		JSONArray tags = getTagsJSON(task.getTags());
		taskObject.put("tags",tags);
		
		String timedTaskFormat = "hh:mm aa 'on' EEEEEEEEE ',' dd MMMMMMMMM, yyyy ";
		SimpleDateFormat sdf = new SimpleDateFormat(timedTaskFormat);
		
		String startString = sdf.format(task.getStartDate().getTime());
		String endString = sdf.format(task.getEndDate().getTime());
		
		taskObject.put("from", startString);
		taskObject.put("to", endString);
		
		return taskObject;
		
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getDeadlineJSON(DeadlineTask task){
		JSONObject taskObject = new JSONObject();
		taskObject.put("description",task.getTaskDescription());
		JSONArray tags = getTagsJSON(task.getTags());
		taskObject.put("tags",tags);

		String deadlineFormat = " 'by' EEEEEEEEE',' dd MMMMMMMMM',' yyyy ";
		SimpleDateFormat sdf = new SimpleDateFormat(deadlineFormat);
		
		String deadlineString = sdf.format(task.getDeadline().getTime());
		
		taskObject.put("deadline", deadlineString);
		
		return taskObject;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray getTagsJSON(ArrayList<String> tagArray){
		JSONArray tagJSON = new JSONArray();
		for (String tag : tagArray){
			tagJSON.add(tag);
		}
		return tagJSON;
	}
	
}
