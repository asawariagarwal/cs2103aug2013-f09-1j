package todo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a GUI-Helper class which
 * is used to implement autocomplete functionality
 * 
 * @author Jishnu
 *
 */
public class Suggestor {
	
	private static final String HASHTAG = "#";
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
	private static final String UNMARK = "unmark";
	private static final String TAG = "tag";
	private static final String UNTAG = "untag";
	
	private static final String NEXTDAY_LABEL = "tomorrow";
	private static final String TODAY_LABEL = "today";
	private static final String UNMARKED_LABEL = "pending";
	private static final String EXPIRED_LABEL = "expired";
	private static final String MARKED_LABEL = "done";
	private static final String DEADLINE_LABEL = "deadlines";
	private static final String TIMED_LABEL = "events";
	private static final String FLOATING_LABEL = "flexible";
	private static final String ALL_LABEL = "all";
	private static final String FROM_LABEL = "from";
	
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
		
		ArrayList<String> typeSuggestions = new ArrayList<String>();
		ArrayList<String> viewSuggestions = new ArrayList<String>();
		typeSuggestions.add(ALL_LABEL);
		typeSuggestions.add(FLOATING_LABEL);
		typeSuggestions.add(TIMED_LABEL);
		typeSuggestions.add(DEADLINE_LABEL);
		typeSuggestions.add(MARKED_LABEL);
		typeSuggestions.add(EXPIRED_LABEL);
		typeSuggestions.add(UNMARKED_LABEL);
		
		commandSuggestionMap.put(CLEAR, typeSuggestions);
		
		viewSuggestions.addAll(typeSuggestions);
		viewSuggestions.add(TODAY_LABEL);
		viewSuggestions.add(NEXTDAY_LABEL);
		viewSuggestions.add(FROM_LABEL);
		
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
		int inputLength = inputString.length();	
		
		if (inputString.isEmpty()){
			return ADD;
		} else {
			for (String command : commandList){
				if (command.length()>=inputLength){
					if (command.substring(0, inputLength)
							.equalsIgnoreCase(inputString)){
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
		ArrayList<String> hashtagList = new ArrayList<String>();
		ArrayList<String> completedList = new ArrayList<String>();
		ArrayList<String> pendingList = new ArrayList<String>();
		
		for (Task task : taskList){
			descriptionList.add(task.getTaskDescription());
			for (String hashtag : task.getTags()){
				hashtagList.add(HASHTAG+hashtag);
			}
			if (task.isComplete()){
				completedList.add(task.getTaskDescription());
			} else {
				pendingList.add(task.getTaskDescription());
			}
		}
		
		commandSuggestionMap.put(DELETE, descriptionList);
		commandSuggestionMap.put(CHANGE, descriptionList);
		commandSuggestionMap.put(RESCHEDULE, descriptionList);
		commandSuggestionMap.put(TAG, descriptionList);
		commandSuggestionMap.put(UNTAG, descriptionList);
		commandSuggestionMap.put(MARK,pendingList);
		commandSuggestionMap.put(UNMARK,completedList);
		
		ArrayList<String> standardViewSugg = commandSuggestionMap.get(VIEW);
		standardViewSugg.addAll(hashtagList);
		commandSuggestionMap.put(VIEW,standardViewSugg);
		
	}
	
	
}
