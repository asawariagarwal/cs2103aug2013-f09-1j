package todo;

/**
 * Subclass to encapsulate undo/redo commands
 * 
 * @author Eugene
 * 
 */
public class UndoCommand extends Command {
	private static final String FEEDBACK_UNDO_NONE = "undo cannot be performed";
	private static final String FEEDBACK_UNDO_SINGLE = "undo last command";
	private static final String FEEDBACK_UNDO_MULTIPLE = "undo last %1$s commands";
	private static final String FEEDBACK_REDO_NONE = "redo cannot be performed";
	private static final String FEEDBACK_REDO_SINGLE = "redo last command";
	private static final String FEEDBACK_REDO_MULTIPLE = "redo last %1$s commands";
	
	private int n;
	private boolean redo;
	
	/**
	 * Constructor for UndoCommand
	 * Default of 1 undo
	 */
	UndoCommand() {
		super(true, true);
		n = 1;
		redo = false;
	}
	
	/**
	 * Constructor for UndoCommand
	 * 
	 * @param number
	 * 			number of undos to be performed
	 */
	UndoCommand(int number) {
		this();
		n = number;
	}
	
	/**
	 * Constructor for UndoCommand
	 * 
	 * @param number
	 * 			number of undos/redo to be performed
	 * 
	 * @param redo
	 * 			true if command is redo, false if undo
	 */
	UndoCommand(int number, boolean redo) {
		this(number);
		this.redo = redo;
	}

	@Override
	protected boolean isValid() {
		return n > 0;
	}

	@Override
	protected State execute(State state) {
		assert(this.isValid());
		if (!this.redo) {
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
		while (done < n) {
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
		State currentState = state;
		int done = 0;
		while (done < n) {
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