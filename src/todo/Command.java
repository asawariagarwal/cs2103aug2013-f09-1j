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
 * @author Eugene
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
	 * @param state
	 * 			the current state of the program
	 * 
	 * @return true if command is valid. false if otherwise.
	 * 
	 */
	abstract protected boolean isValid(State state);
	
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
	 * Returns whether command mutates state
	 * 
	 * @return true if command mutates state, false otherwise.
	 */
	protected boolean isMutator() {
		return mutator;
	}
	
	/**
	 * Returns whether command is a previous state
	 * 
	 * @return true if command is a previous state, false otherwise.
	 */
	protected boolean isOldState() {
		return oldState;
	}
}