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
	private static final String REDO = "redo";
	private static final String EXIT = "exit";
	private static final String HELP = "help";
	
	private static ArrayList<String> commandList = new ArrayList<String>();	
	
	private State currentState;
	
	public Suggestor(){
		commandList.add(ADD);
		commandList.add(DELETE);
		commandList.add(VIEW);
		commandList.add(CHANGE);
		commandList.add(RESCHEDULE);
		commandList.add(SEARCH);
		commandList.add(UNDO);
		commandList.add(REDO);
		commandList.add(HELP);
		commandList.add(EXIT);
	}
	
	protected String getSuggestion(String inputString){
		if (inputString.contains(" ")){
			return getTask(inputString);
		} else {
			return getCommand(inputString);
		}
	}
	
	private String getCommand(String inputString){
		//Refactor to Switch + enum later
		int inputLength = inputString.length();	
		
		if (inputString.isEmpty()){
			return ADD;
		} else {
			for (String command : commandList){
				if (!(inputString.length()>command.length())){
					if (command.substring(0, inputLength).equals(inputString)){
						return command;
					}
				}
			}
			return inputString;
		}
	}
	
	private String getTask(String inputString){
		int indexOfSpace = inputString.indexOf(' ');
		
		String command = inputString.substring(0,indexOfSpace);
		
		String taskSnippet = inputString.substring(indexOfSpace+1);
		
		ArrayList<Task> possibleTasks = currentState.getTasks(taskSnippet);
		
		return command + " " + possibleTasks.get(0).getTaskDescription();
		
	}
	
	protected void updateState(State newState){
		currentState = newState;
	}
	
	
}
