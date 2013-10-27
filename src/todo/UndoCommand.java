package todo;

/**
 * Subclass to encapsulate undo commands
 * 
 * @author Eugene
 * 
 */
public class UndoCommand extends Command {
	
	private int num_undo;
	
	/**
	 * Constructor for UndoCommand
	 * Default of 1 undo
	 */
	UndoCommand() {
		super(true, true);
		num_undo = 1;
	}
	
	/**
	 * Constructor for UndoCommand
	 * 
	 * @param number
	 * 			number of undos to be performed
	 */
	UndoCommand(int number) {
		this();
		num_undo = number;
	}

	@Override
	protected boolean isValid(State state) {
		return num_undo != 0;
	}

	@Override
	protected State execute(State state) {
		assert(this.isValid(state));
		if (num_undo > 0) {
			return executeUndo(state);
		} else {
			return executeRedo(state);
		}
		
	}
	
	/**
	 * Performs execution for undo
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @return new state after undo executed
	 */
	private State executeUndo(State state) {
		State currentState = state;
		int done = 0;
		while (done < num_undo) {
			if (!currentState.hasPrevious())  {
				break;
			}
			currentState = currentState.getPrevious();
			done++;
		}
		currentState.setFeedback("Undo last " + String.valueOf(done) + " commands");
		return currentState;
	}
	
	/**
	 * Performs execution for redo
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @return new state after redo executed
	 */
	private State executeRedo(State state) {
		State currentState = state;
		int done = 0;
		while (done > num_undo) {
			if (!currentState.hasNext())  {
				break;
			}
			currentState = currentState.getNext();
			done++;
		}
		currentState.setFeedback("Redo next " + String.valueOf(done) + " commands");
		return currentState;
	}
}