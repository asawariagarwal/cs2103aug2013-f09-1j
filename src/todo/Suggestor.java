package todo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a GUI-Helper class which
 * is used to implement autocomplete functionality
 * 
 * @author A0100784L
 *
 */
public class Suggestor {
	
	/**
	 * Helpful string constants
	 */
	private static final String HASHTAG = "#";
	
	/**
	 * Command syntax string constants
	 */
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
	
	/**
	 * View argument string constants
	 */
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
	
	/**
	 * List to store all possible commands
	 */
	private static ArrayList<String> _commandList = new ArrayList<String>();	
	
	/**
	 * Variable to store current state
	 */
	private State _currentState;
	
	/**
	 * Hashmap from command to possible suggestions
	 */
	private HashMap<String,ArrayList<String>> _commandSuggestionMap =
			new HashMap<String,ArrayList<String>>();
	
	
	/**
	 * Constructors that loads constant suggestions
	 */
	public Suggestor(){
		
		loadCommandList();
		
		ArrayList<String> typeSuggestions = new ArrayList<String>();
		ArrayList<String> viewSuggestions = new ArrayList<String>();
		
		loadTaskTypeSuggestions(typeSuggestions);
		
		_commandSuggestionMap.put(CLEAR, typeSuggestions);
		
		viewSuggestions.addAll(typeSuggestions);
		addViewSpecificSuggestions(viewSuggestions);
		
		_commandSuggestionMap.put(VIEW, viewSuggestions);		
	}

	/**
	 * Method to load constant commands to commandList
	 */
	private void loadCommandList() {
		_commandList.add(ADD);
		_commandList.add(DELETE);
		_commandList.add(VIEW);
		_commandList.add(CHANGE);
		_commandList.add(RESCHEDULE);
		_commandList.add(SEARCH);
		_commandList.add(UNDO);
		_commandList.add(REDO);
		_commandList.add(HELP);
		_commandList.add(CLEAR);
		_commandList.add(EXIT);
		_commandList.add(CLEAR);
		_commandList.add(MARK);
	}
	
	/**
	 * Method to load suggestions based on task types
	 * 
	 * @param typeSuggestions contains the arrayList to be updated
	 */
	private void loadTaskTypeSuggestions(ArrayList<String> typeSuggestions) {
		typeSuggestions.add(ALL_LABEL);
		typeSuggestions.add(FLOATING_LABEL);
		typeSuggestions.add(TIMED_LABEL);
		typeSuggestions.add(DEADLINE_LABEL);
		typeSuggestions.add(MARKED_LABEL);
		typeSuggestions.add(EXPIRED_LABEL);
		typeSuggestions.add(UNMARKED_LABEL);
	}
	
	/**
	 * Method to load suggestions specific to view
	 * 
	 * @param viewSuggestions contains the arrayList to be updated
	 */
	private void addViewSpecificSuggestions(ArrayList<String> viewSuggestions) {
		viewSuggestions.add(TODAY_LABEL);
		viewSuggestions.add(NEXTDAY_LABEL);
		viewSuggestions.add(FROM_LABEL);
	}
	
	
	/**
	 * Method to generate a suggestion given a certain input
	 * 
	 * @param inputString specifies input entered by user
	 * @return the complete suggested String
	 */
	protected String getSuggestion(String inputString){
		if (inputString.contains(" ")){
			return getArgument(inputString);
		} else {
			return getCommand(inputString);
		}
	}
	
	/**
	 * Method to complete user input if input is
	 * substring of a command keyword
	 * 
	 * @param inputString specifies user input
	 * @return completed command keyword
	 */
	private String getCommand(String inputString){
		int inputLength = inputString.length();	
		
		if (inputString.isEmpty()){
			return ADD;
		} else {
			for (String command : _commandList){
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
	
	/**
	 * Method to return suggestions of
	 * command argument based on which
	 * command it is
	 * 
	 * @param inputString specifies input by user
	 * @return completed suggestion
	 */
	private String getArgument(String inputString){
		int indexOfSpace = inputString.indexOf(' ');
		
		String command = inputString.substring(0,indexOfSpace);
		
		String argSnippet = inputString.substring(indexOfSpace+1);
		int snipLength = argSnippet.length();
		
		if (_commandSuggestionMap.containsKey(command.toLowerCase())){
			ArrayList<String> argSuggestions = _commandSuggestionMap.get(command);
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
	
	/**
	 * Method to update state information
	 * for generation of dynamic (state-based) suggestions
	 * 
	 * @param newState specifies the latest state
	 */
	protected void updateState(State newState){
		_currentState = newState;
		
		ArrayList<Task> taskList = _currentState.getAllTasks();
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
		
		updateMapping(descriptionList, hashtagList, completedList, pendingList);	
	}

	/**
	 * Updates HashMap based on State information
	 * 
	 * @param descriptionList contains a List of Task Descriptions
	 * @param hashtagList contains a List of Hashtags used
	 * @param completedList contains a List of descriptions of completed tasks
	 * @param pendingList contains a List of descriptions of pending tasks
	 */
	private void updateMapping(ArrayList<String> descriptionList,
			ArrayList<String> hashtagList, ArrayList<String> completedList,
			ArrayList<String> pendingList) {
		_commandSuggestionMap.put(DELETE, descriptionList);
		_commandSuggestionMap.put(CHANGE, descriptionList);
		_commandSuggestionMap.put(RESCHEDULE, descriptionList);
		_commandSuggestionMap.put(TAG, descriptionList);
		_commandSuggestionMap.put(UNTAG, descriptionList);
		_commandSuggestionMap.put(MARK,pendingList);
		_commandSuggestionMap.put(UNMARK,completedList);
		
		ArrayList<String> standardViewSugg = _commandSuggestionMap.get(VIEW);
		standardViewSugg.addAll(hashtagList);
		_commandSuggestionMap.put(VIEW,standardViewSugg);
	}
	
	
}
