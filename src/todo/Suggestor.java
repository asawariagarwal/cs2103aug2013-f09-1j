package todo;

import java.util.ArrayList;
import java.util.HashMap;

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
	private static final String CLEAR = "clear";
	private static final String MARK = "mark";
	
	private static ArrayList<String> commandList = new ArrayList<String>();	
	
	private State currentState;
	
	private HashMap<String,ArrayList<String>> commandSuggestionMap =
			new HashMap<String,ArrayList<String>>();
	
	
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
		commandList.add(CLEAR);
		commandList.add(EXIT);
		commandList.add(CLEAR);
		commandList.add(MARK);
		
		ArrayList<String> viewSuggestions = new ArrayList<String>();
		viewSuggestions.add("all");
		viewSuggestions.add("flexible");
		viewSuggestions.add("events");
		viewSuggestions.add("deadlines");
		viewSuggestions.add("today");
		viewSuggestions.add("tomorrow");
		viewSuggestions.add("completed");
		viewSuggestions.add("expired");
		viewSuggestions.add("pending");
		
		commandSuggestionMap.put(VIEW, viewSuggestions);
		
	}
	
	protected String getSuggestion(String inputString){
		if (inputString.contains(" ")){
			return getArgument(inputString);
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
				if (command.length()>=inputLength){
					if (command.substring(0, inputLength).equalsIgnoreCase(inputString)){
						return command;
					}
				}
			}
			return inputString;
		}
	}
	
	private String getArgument(String inputString){
		int indexOfSpace = inputString.indexOf(' ');
		
		String command = inputString.substring(0,indexOfSpace);
		
		String argSnippet = inputString.substring(indexOfSpace+1);
		int snipLength = argSnippet.length();
		
		if (commandSuggestionMap.containsKey(command.toLowerCase())){
			ArrayList<String> argSuggestions = commandSuggestionMap.get(command);
			for (String suggestion : argSuggestions){
				if (suggestion.length()>=snipLength){
					if (suggestion.substring(0, snipLength).equalsIgnoreCase(argSnippet)){
						return command + " " + suggestion;
					}
				}
			}
			return inputString;
		} else {
			return inputString;
		}
		
	}
	
	protected void updateState(State newState){
		currentState = newState;
		
		ArrayList<Task> taskList = currentState.getAllTasks();
		ArrayList<String> descriptionList = new ArrayList<String>();
		
		for (Task task : taskList){
			descriptionList.add(task.getTaskDescription());
		}
		
		commandSuggestionMap.put(DELETE, descriptionList);
		commandSuggestionMap.put(CHANGE, descriptionList);
		commandSuggestionMap.put(RESCHEDULE, descriptionList);
		commandSuggestionMap.put(MARK, descriptionList);
		
	}
	
	
}
