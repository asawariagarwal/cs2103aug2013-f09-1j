package todo;

import java.io.IOException;
import java.util.LinkedList;

/**
 * This class is the main driver for handling user inputs.
 * It takes a user input string and sends it to the Parser.
 * A Command would be returned and this will check whether the Command is valid.
 * If valid, Command will be executed, and a new State will be returned.
 * 
 * @author Eugene
 *
 */
class CommandHandler {
	
	Parser parser;
	LinkedList<State> stateList;
	JSONStorage store;
	
	/**
	 * Constructor for CommandHandler
	 * Initializes parser and stateList
	 * 
	 */
	public CommandHandler(){
		parser = new Parser();
		stateList = new LinkedList<State>();
		store = new JSONStorage();
		State initState = readStorage();
		stateList.add(initState);
	}
	
	
	/**
	 * Takes in a user input string and returns the new state
	 * 
	 * 
	 * @param commandString
	 * 			user input string
	 * 
	 * @return a State to be displayed
	 */
	protected State handleInput(String commandString){
		Command command = parser.parseInput(commandString);
		return handle(command);
	}
	
	/**
	 * Checks if command is valid
	 * 
	 * @param command
	 * 			Command created by parser
	 * 
	 * @return true if command is valid, false otherwise.
	 * 
	 */
	protected boolean isValidCommand(Command command) {
		return command != null && command.isValid(getCurrentState());
	}
	
	/**
	 * Handles the execution of a command
	 * If command is not valid, returns an invalid state.
	 * 
	 * @param command
	 * 			Command created by parser
	 * 
	 * @return State
	 */
	protected State handle(Command command) {
		if (isValidCommand(command)) {
			State newState = command.execute(getCurrentState());
			if (command.isMutator()) {
				stateList.add(newState);
				updateStorage(newState);
			}
			return newState;
		} else {
			return makeInvalidState();
		}
	}
	
	/**
	 * Gets the current state from stateList
	 * Currently returns the last item in stateList
	 * 
	 * @return the current State
	 */
	protected State getCurrentState() {
		return stateList.getLast();
	}
	
	/**
	 * Makes an invalid state by copying the current state
	 * and adding a feedback expressing an invalid command
	 * 
	 * @return an invalid state
	 */
	protected State makeInvalidState() {
		State s = new State(getCurrentState());
		s.setFeedback("Invalid Command");
		return s;
	}
	
	/**
	 * Updates text file with new state
	 * 
	 * @param state
	 * 			state to be used to update Storage
	 * 
	 * @return true if update is successful.
	 */
	private boolean updateStorage(State state) {
		try {
			store.writeStore(state);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Reads storage to get init state
	 * 
	 * 
	 * @return initial state if successful, empty state otherwise.
	 */
	private State readStorage() {
		State initState = new State();
		try {
			initState = store.readStore();
			return initState;
		} catch (Exception e) {
			initState.setFeedback("Corrupted Previous State");
			return initState;
		}
	}
}
