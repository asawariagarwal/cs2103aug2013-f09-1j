package todo;
import java.util.logging.*;

/**
 * Subclass to encapsulate delete commands
 * 
 * @author Eugene
 * 
 */
public class DeleteCommand extends Command {
	private static final String FEEDBACK_NOT_FOUND = "no tasks containing %1$s can be found";
	private static final String FEEDBACK_MULTIPLE_FOUND = "multiple tasks containing %1$s found - refine your keywords";
	private static final String FEEDBACK_DELETED = "deleted: %1$s";
	
	private static final String LOG_ERROR = "error executing delete";
	
	
	private String taskString;
	
	/**
	 * Constructor for DeleteCommand
	 * 
	 * @param taskString
	 * 			task name/description
	 * 
	 * 
	 */
	DeleteCommand(String taskString) {
		super(true);
		this.taskString = taskString;
	}

	@Override
	protected boolean isValid() {
		return taskString != null && !taskString.isEmpty(); 
	}

	@Override
	protected State execute(State state) throws Exception {
		logger.log(Level.WARNING, LOG_ERROR);
		throw new Exception();			
	}
	
	@Override
	protected State execute(State state, State displayState) throws Exception {
		assert(this.isValid());
		
		if (!state.hasTask(taskString)) {
			return executeTaskNotFound(displayState);
		} else if (isOnlyTask(state)) {
			return executeTaskFound(state);
		} else if (isOnlyTask(displayState)) {
			return executeTaskFoundDisplay(state, displayState);
		} else {
			return executeTaskMultiple(displayState);
		}
	}

	/**
	 * Execution when task cannot be found
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @return state after execution of command
	 */
	private State executeTaskNotFound(State state) {
		this.setMutator(false);
		State s = new State(state);
		s.setFeedback(String.format(FEEDBACK_NOT_FOUND, taskString));
		return s;
	}
	
	/**
	 * Execution when single task found in state
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @return state after execution of command
	 */
	private State executeTaskFound(State state) {
		Task deletedTask = findTask(state);
		State s = new State(state);
		s.removeTask(deletedTask);
		s.setFeedback(String.format(FEEDBACK_DELETED, deletedTask.getTaskDescription()));
		return s;
	}
	
	/**
	 * Execution when single task found in display
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @param displayState
	 * 			state being displayed by program
	 * 
	 * @return state after execution of command
	 */
	private State executeTaskFoundDisplay(State state, State displayState) {
		Task deletedTask = findTask(displayState);
		State s = new State(state);
		s.removeTask(deletedTask);
		s.setFeedback(String.format(FEEDBACK_DELETED, deletedTask.getTaskDescription()));
		return s;
	}
	
	/**
	 * Execution when multiple tasks found
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @return state after execution of command
	 */
	private State executeTaskMultiple(State state) {
		this.setMutator(false);
		State s = new State(state);
		s.setFeedback(String.format(FEEDBACK_MULTIPLE_FOUND, taskString));
		return s;
	}
	
	/**
	 * Checks if state has only a single task that contains taskString
	 * 
	 * @param state
	 * 			state which contains task to be found
	 * 
	 * @return true if state has single Task with occurrence of taskString
	 */
	private boolean isOnlyTask(State state) {
		return state.getTasks(taskString).size() == 1;
	}

	/**
	 * Finds task that contains taskString in state
	 * 
	 * @param state
	 * 			state which contains task to be found
	 * 
	 * @return first Task which is found in state with occurrence of taskString
	 */
	private Task findTask(State state) {
		return state.getTasks(taskString).get(0);
	}
}