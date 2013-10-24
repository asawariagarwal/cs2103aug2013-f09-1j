package todo;

import java.util.ArrayList;

public class Suggestor {
	
	private static final String ADD = "add";
	private static final String VIEW = "view";
	private static final String DELETE = "delete";
	private static final String CHANGE = "change";
	private static final String RESCHEDULE = "reschedule";
	private static final String SEARCH ="search";
	private static final String UNDO = "undo";
	private static final String EXIT = "exit";
	private static final String NULLSTRING = "";
	
	private State currentState;
	
	protected String getSuggestion(String inputString){
		if (inputString.contains(" ")){
			return getTask(inputString);
		} else {
			return getCommand(inputString);
		}
	}
	
	private String getCommand(String inputString){
		//Refactor to Switch + enum later
		
	    if (ADD.contains(inputString)){
			return ADD;
		} else if (VIEW.contains(inputString)){
			return VIEW;
		} else if (DELETE.contains(inputString)){
			return DELETE;
		} else if (RESCHEDULE.contains(inputString)){
			return RESCHEDULE;
		} else if (SEARCH.contains(inputString)){
			return SEARCH;
		} else if (UNDO.contains(inputString)){
			return UNDO;
		} else if (CHANGE.contains(inputString)){
			return CHANGE;
		} else if (EXIT.contains(inputString)) {
			return EXIT;
		} else { 
			return NULLSTRING;
		}
	}
	
	private String getTask(String inputString){
		int indexOfSpace = inputString.indexOf(' ');
		
		String command = inputString.substring(0,indexOfSpace);
		
		String taskSnippet = inputString.substring(indexOfSpace+1);
		
		ArrayList<Task> possibleTasks = currentState.getTasks(taskSnippet);
		
		return command + " " + possibleTasks.get(0).toString();
		
	}
	
	protected void updateState(State newState){
		currentState = newState;
	}
	
	
}
