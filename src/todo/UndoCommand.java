package todo;

/**
 * Subclass to encapsulate undo/redo commands
 * 
 * @author A0097199H
 * 
 */
public class UndoCommand extends Command {
	private static final String FEEDBACK_UNDO_NONE = "undo cannot be performed";
	private static final String FEEDBACK_UNDO_SINGLE = "undo last command";
	private static final String FEEDBACK_UNDO_MULTIPLE = "undo last %1$s commands";
	private static final String FEEDBACK_MAX_UNDO = "no commands left to undo";
	private static final String FEEDBACK_REDO_NONE = "redo cannot be performed";
	private static final String FEEDBACK_REDO_SINGLE = "redo last command";
	private static final String FEEDBACK_REDO_MULTIPLE = "redo last %1$s commands";
	private static final String FEEDBACK_MAX_REDO = "no commands left to redo";
	
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
		String feedback;
		if (done == 0) {
			currentState.setFeedback(new Feedback(FEEDBACK_UNDO_NONE, false));
			return currentState;
		} else if (done == 1) {
			feedback = FEEDBACK_UNDO_SINGLE;
		} else {
			feedback = String.format(FEEDBACK_UNDO_MULTIPLE, String.valueOf(done));
		}
		if (done < n) {
			feedback += System.lineSeparator();
			feedback += FEEDBACK_MAX_UNDO;
		}
		currentState.setFeedback(new Feedback(feedback, true));
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
		String feedback;
		if (done == 0) {
			currentState.setFeedback(new Feedback(FEEDBACK_REDO_NONE,false));
			return currentState;
		} else if (done == 1) {
			feedback = FEEDBACK_REDO_SINGLE;
		} else {
			feedback = String.format(FEEDBACK_REDO_MULTIPLE, String.valueOf(done));
		}
		if (done < n) {
			feedback += System.lineSeparator();
			feedback += FEEDBACK_MAX_REDO;
		}
		currentState.setFeedback(new Feedback(feedback, true));
		return currentState;
	}
}