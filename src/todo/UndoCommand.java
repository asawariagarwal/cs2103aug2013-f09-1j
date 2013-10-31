package todo;

/**
 * Subclass to encapsulate undo/redo commands
 * 
 * @author Eugene
 * 
 */
public class UndoCommand extends Command {
	private static final String FEEDBACK_UNDO_NONE = "Undo cannot be performed";
	private static final String FEEDBACK_UNDO_SINGLE = "Undo last command";
	private static final String FEEDBACK_UNDO_MULTIPLE = "Undo last %1$s commands";
	private static final String FEEDBACK_REDO_NONE = "Redo cannot be performed";
	private static final String FEEDBACK_REDO_SINGLE = "Redo last command";
	private static final String FEEDBACK_REDO_MULTIPLE = "Redo last %1$s commands";
	
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
		assert(num_undo > 0);
		State currentState = state;
		int done = 0;
		while (done < num_undo) {
			if (!currentState.hasPrevious())  {
				break;
			}
			currentState = currentState.getPrevious();
			done++;
		}
		if (done == 0) {
			currentState.setFeedback(FEEDBACK_UNDO_NONE);
		} else if (done == 1) {
			currentState.setFeedback(FEEDBACK_UNDO_SINGLE);
		} else {
			currentState.setFeedback(String.format(FEEDBACK_UNDO_MULTIPLE, String.valueOf(done)));
		}
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
		assert(num_undo < 0);
		State currentState = state;
		int done = 0;
		int num_redo = Math.abs(num_undo);
		while (done < num_redo) {
			if (!currentState.hasNext())  {
				break;
			}
			currentState = currentState.getNext();
			done++;
		}
		if (done == 0) {
			currentState.setFeedback(FEEDBACK_REDO_NONE);
		} else if (done == 1) {
			currentState.setFeedback(FEEDBACK_REDO_SINGLE);
		} else {
			currentState.setFeedback(String.format(FEEDBACK_REDO_MULTIPLE, String.valueOf(done)));
		}
		return currentState;
	}
}