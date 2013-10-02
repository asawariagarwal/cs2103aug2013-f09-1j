package todo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StorageManager {

	private static String _filename = "taskstore.txt";
	private final static String TASK_SEPARATOR = "\n";
	
	protected static State readStore(){
		//TODO
	}
	
	protected static void writeStore(State saveState) throws IOException{
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
