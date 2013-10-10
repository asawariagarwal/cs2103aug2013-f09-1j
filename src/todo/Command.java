package todo;

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
	
	/**
	 * Constructor for Command
	 * 
	 */
	Command(boolean isMutator) {
		mutator = isMutator;
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
	abstract protected State execute(State state);
	
	/**
	 * Returns whether command mutates state
	 * 
	 * @return true if command mutates state, false otherwise.
	 */
	protected boolean isMutator() {
		return mutator;
	}
}