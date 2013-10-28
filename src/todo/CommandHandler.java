package todo;

import java.io.IOException;

/**
 * This class is the main driver for handling user inputs.
 * It takes a user input string and sends it to the Interpreter.
 * A Command would be returned and this will check whether the Command is valid.
 * If valid, Command will be executed, and a new State will be returned.
 * 
 * @author Eugene
 *
 */
class CommandHandler {
	
	private Interpreter interpreter;
	private State state;
	private JSONStorage store;
	
	/**
	 * Constructor for CommandHandler
	 * Initializes interpreter and stateList
	 * 
	 */
	public CommandHandler(){
		interpreter = new Interpreter();
		store = new JSONStorage();
		state = readStorage();
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
		Command command = interpreter.parseInput(commandString);
		return handle(command);
	}
	
	/**
	 * Checks if command is valid
	 * 
	 * @param command
	 * 			Command created by interpreter
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
	 * 			Command created by interpreter
	 * 
	 * @return State
	 */
	protected State handle(Command command) {
		if (isValidCommand(command)) {
			State newState;
			try {
				newState = command.execute(getCurrentState());
			} catch (Exception e) {
				return makeInvalidState("Error encountered executing command");
			}
			if (command.isMutator()) {
				handleMutator(command, newState);
			}
			return newState;
		} else {
			return makeInvalidState("Invalid command");
		}
	}
	
	/**
	 * Handles the execution of a mutator command
	 * 
	 * @param command
	 * 			Command created by parser
	 * 
	 * @param newState
	 * 			state after execution of command
	 * 
	 */
	protected void handleMutator(Command command, State newState) {
		updateStorage(newState);
		if (!command.isOldState()) {
			addNewState(newState);
		}
		state = newState;
	}
	
	/**
	 * Gets the current state
	 * 
	 * @return the current State
	 */
	protected State getCurrentState() {
		return state;
	}
	
	/**
	 * Adds new state to state chain
	 * Updates current state
	 * 
	 */
	private void addNewState(State newState) {
		state.setNext(newState);
		newState.setPrevious(state);
	}
	
	/**
	 * Makes an invalid state by copying the current state
	 * and adding a feedback
	 * 
	 * @param feedback
	 * 			String that represents the feedback of the invalid state
	 * 
	 * @return an invalid State
	 */
	protected State makeInvalidState(String feedback) {
		State s = new State(getCurrentState());
		s.setFeedback(feedback);
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
