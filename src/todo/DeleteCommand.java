package todo;
import java.util.TreeSet;
import java.util.logging.*;

/**
 * Subclass to encapsulate delete commands
 * 
 * @author Eugene
 * 
 */
public class DeleteCommand extends Command {
	public static final int INDEX_FLOATING = 0;
	public static final int INDEX_TIMED = 1;
	public static final int INDEX_DEADLINE = 2;
	
	
	private static final String FEEDBACK_NOT_FOUND = "no tasks containing %1$s can be found";
	private static final String FEEDBACK_MULTIPLE_FOUND = "multiple tasks containing %1$s found - refine your keywords";
	private static final String FEEDBACK_DELETED = "deleted: %1$s";
	private static final String FEEDBACK_BAD_INDEX = "invalid index";
	
	private static final String LOG_ERROR = "error executing delete";
	
	
	private String taskString;
	private int indexPos;
	private int indexType;
	private boolean isByIndex;
	
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
		this.isByIndex = false;
	}
	
	/**
	 * Constructor for DeleteCommand
	 * 
	 * @param indexPos
	 * 			index of task in display state
	 * 
	 * @param indexType
	 * 			type of task,
	 * 			can be the following: INDEX_FLOATING, INDEX_DEADLINE, INDEX_TIMED
	 * 
	 */
	DeleteCommand(int indexPos, int indexType) {
		super(true);
		this.indexPos = indexPos;
		this.indexType = indexType;
		this.isByIndex = true;
	}

	@Override
	protected boolean isValid() {
		return isValidTaskString() || isValidByIndex();
	}
	
	/**
	 * Checks validity of taskString
	 * 
	 * @return true if taskString is valid
	 */
	private boolean isValidTaskString() {
		return !isByIndex && taskString != null && !taskString.isEmpty();
	}
	
	/**
	 * Checks validity of index type and position
	 * 
	 * @return true if index type and position is valid
	 */
	private boolean isValidByIndex() {
		return isByIndex && isValidIndexType() && indexPos > 0;
	}
	
	/**
	 * Checks validity of index type
	 * 
	 * @return true if index type is valid
	 */
	private boolean isValidIndexType() {
		return (indexType == INDEX_FLOATING || 
				indexType == INDEX_TIMED || 
				indexType == INDEX_DEADLINE);
	}

	@Override
	protected State execute(State state) throws Exception {
		logger.log(Level.WARNING, LOG_ERROR);
		throw new Exception();			
	}
	
	@Override
	protected State execute(State state, State displayState) throws Exception {
		assert(this.isValid());
		
		if (isByIndex) {
			return executeByIndex(state, displayState);
		}
		
		if (!state.hasTask(taskString)) {
			return makeErrorState(displayState, new Feedback(String.format(FEEDBACK_NOT_FOUND, taskString),false));
		} else if (isOnlyTask(state)) {
			return executeTaskFound(state, displayState, false);
		} else if (isOnlyTask(displayState)) {
			return executeTaskFound(state, displayState, true);
		} else {
			return makeErrorState(displayState, new Feedback(String.format(FEEDBACK_MULTIPLE_FOUND, taskString),false));
		}
	}
	
	/**
	 * Executes delete by index
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @param displayState
	 * 			state program is displaying
	 * 
	 * @return state after execution
	 */
	protected State executeByIndex(State state, State displayState) throws Exception {
		TreeSet<? extends Task> tasks;
		if (indexType == INDEX_FLOATING) {
			tasks = displayState.getFloatingTasks();
		} else if (indexType == INDEX_TIMED) {
			tasks = displayState.getTimedTasks();
		} else if (indexType == INDEX_DEADLINE) {
			tasks = displayState.getDeadlineTasks();
		} else {
			throw new Exception();
		}
		
		int i = 1;
		State s = new State(state);
		for (Task t : tasks) {
			if (i > indexPos) {
				break;
			} else if (i == indexPos) {
				s.removeTask(t);
				Feedback f = new Feedback(String.format(FEEDBACK_DELETED, t.getTaskDescription()),true);
				s.setFeedback(f);
				return s;
			}
			i++;
		}
		return makeErrorState(displayState, new Feedback(FEEDBACK_BAD_INDEX, false));
	}

	/**
	 * Makes an error state to be returned
	 * 
	 * @param state
	 * 			current state of program
	 * 
	 * @param feedback
	 * 			feedback of error state
	 * @return
	 */
	private State makeErrorState(State state, Feedback feedback) {
		this.setMutator(false);
		State s = new State(state);
		s.setFeedback(feedback);
		return s;
	}
	
	/**
	 * Execution when single task found
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @param displayState
	 * 			state being displayed by program
	 * 
	 * @param isFromDisplay
	 * 			whether task found is from display state
	 * 
	 * @return state after execution of command
	 */
	private State executeTaskFound(State state, State displayState, boolean isFromDisplay) {
		Task deletedTask;
		if (isFromDisplay) {
			deletedTask = findTask(displayState);
		} else {
			deletedTask = findTask(state);
		}
		State s = new State(state);
		s.removeTask(deletedTask);
		Feedback f = new Feedback(String.format(FEEDBACK_DELETED, deletedTask.getTaskDescription()),true);
		s.setFeedback(f);
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