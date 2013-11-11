package todo;

import java.util.logging.*;

/**
 * This class encapsulates different types of Commands
 * 
 * Subclasses:	AddCommand,
 * 				ViewCommand,
 * 				ModifyCommand,
 * 				DeleteCommand,
 * 				UndoCommand,
 * 				SearchCommand
 * 
 * @author A0097199H
 * 
 */
abstract class Command {
	
	private boolean mutator;
	private boolean oldState;
	protected static Logger logger = Logger.getLogger("Command");
	
	
	/**
	 * Constructor for Command
	 * 
	 * @param isMutator
	 * 			whether command changes data
	 * 			ie. needs to write to store
	 * 
	 * This is for most commands.
	 * Most commands are not an old state.
	 * 
	 */
	Command(boolean isMutator) {
		mutator = isMutator;
		oldState = false;
	}
	
	/**
	 * Constructor for Command
	 * 
	 * @param isMutator
	 * 			whether command changes data
	 * 			ie. needs to write to store
	 * 
	 * @param isOldState
	 * 			whether command gets an old state
	 * 			ie. on execution, command returns a previous state
	 * 
	 */
	Command(boolean isMutator, boolean isOldState) {
		mutator = isMutator;
		oldState = isOldState;
	}
	
	/**
	 * Checks whether Command is valid
	 * 
	 * @return true if command is valid. false if otherwise.
	 * 
	 */
	abstract protected boolean isValid();
	
	/**
	 * Executes Command by modifying state
	 * 
	 * @param state
	 * 			the current state of the program
	 * 
	 * @return new state if execution of command is successful. null if otherwise.
	 * 
	 */
	abstract protected State execute(State state) throws Exception;
	
	/**
	 * Executes Command by modifying state
	 * Default behaviour: Ignores displayState and executes based on state.
	 * 
	 * @param state
	 * 			the current state of the program
	 * 
	 * @param displayState
	 * 			the current state being displayed
	 * 
	 * @return new state if execution of command is successful. null if otherwise.
	 * 
	 */
	protected State execute(State state, State displayState) throws Exception {
		return execute(state);
	}
	
	/**
	 * Returns whether command mutates state
	 * 
	 * @return true if command mutates state, false otherwise.
	 */
	protected boolean isMutator() {
		return mutator;
	}
	
	/**
	 * Sets whether command mutates state
	 * 
	 * @param value
	 * 			true if command mutates state, false otherwise.
	 */
	protected void setMutator(boolean value) {
		mutator = value;
	}
	
	/**
	 * Returns whether command is a previous state
	 * 
	 * @return true if command is a previous state, false otherwise.
	 */
	protected boolean isOldState() {
		return oldState;
	}
	
	/**
	 * Sets whether command is a previous state
	 * 
	 * @param value
	 * 			true if command is a previous state, false otherwise.
	 */
	protected void setOldState(boolean value) {
		oldState = value;
	}
}