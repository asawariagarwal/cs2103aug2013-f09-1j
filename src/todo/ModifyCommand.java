package todo;

import java.util.Calendar;

/**
 * Subclass to encapsulate modify commands
 * 
 * @author Eugene
 * 
 */
public class ModifyCommand extends Command {
	private static final int MODE_CHANGE = 0;
	private static final int MODE_RESCHEDULE_DEADLINE = 1;
	private static final int MODE_RESCHEDULE_TIMED = 2;
	private static final int MODE_MARK = 3;
	
	private static final String FEEDBACK_NOT_FOUND = "no tasks containing %1$s can be found";
	private static final String FEEDBACK_MULTIPLE_FOUND = "multiple tasks containing %1$s found - refine your keywords";
	private static final String FEEDBACK_NOT_DEADLINE = "%1$s is not a deadline";
	private static final String FEEDBACK_NOT_TIMED = "%1$s is not an event";
	private static final String FEEDBACK_CHANGED = "changed: \"%1$s\" to \"%2$s\"";
	private static final String FEEDBACK_RESCHEDULE_DEADLINE = "rescheduled deadline of: %1$s";
	private static final String FEEDBACK_RESCHEDULE_TIMED = "reschedule event: %1$s";
	private static final String FEEDBACK_ALREADY_MARKED = "%1$s is already marked";
	private static final String FEEDBACK_ALREADY_UNMARKED = "%1$s is already unmarked";
	private static final String FEEDBACK_MARK_SUCCESS = "marked: %1$s";
	private static final String FEEDBACK_UNMARK_SUCCESS = "unmarked: %1$s";
	
	private String taskString;
	private String newTaskString;
	private Calendar newDeadline;
	private Calendar newStartDate;
	private Calendar newEndDate;
	private boolean mark;
	private int mode;
	
	/**
	 * Constructor for ModifyCommand
	 * 
	 * @param taskString
	 * 			task name/description
	 * 
	 * @param newTaskString
	 * 			modified task name/description
	 * 
	 */
	ModifyCommand(String taskString, String newTaskString) {
		super(true);
		this.taskString = taskString;
		this.newTaskString = newTaskString;
		this.mode = MODE_CHANGE;
	}
	
	/**
	 * Constructor for ModifyCommand
	 * 
	 * @param taskString
	 * 			task name/description
	 * 
	 * @param newDeadline
	 * 			modified deadline
	 * 
	 */
	ModifyCommand(String taskString, Calendar newDeadline) {
		super(true);
		this.taskString = taskString;
		this.newDeadline = newDeadline;
		this.mode = MODE_RESCHEDULE_DEADLINE;
	}
	
	/**
	 * Constructor for ModifyCommand
	 * 
	 * @param taskString
	 * 			task name/description
	 * 
	 * @param mark
	 * 			true to mark, false to unmark
	 * 
	 */
	ModifyCommand(String taskString, boolean mark) {
		super(true);
		this.taskString = taskString;
		this.mark = mark;
		this.mode = MODE_MARK;
	}
	
	/**
	 * Constructor for ModifyCommand
	 * 
	 * @param taskString
	 * 			task name/description
	 * 
	 * @param newStartDate
	 * 			modified start date
	 * 
	 * @param newEndDate
	 * 			modified end date
	 * 
	 */
	ModifyCommand(String taskString, Calendar newStartDate, Calendar newEndDate) {
		super(true);
		this.taskString = taskString;
		this.newStartDate = newStartDate;
		this.newEndDate = newEndDate;
		this.mode = MODE_RESCHEDULE_TIMED;
	}

	@Override
	protected boolean isValid() {
		return isValidTaskString() && 
				(isChange() ||
				 isRescheduleDeadline() ||
				 isRescheduleTimed() ||
				 isMark());
	}
	
	/**
	 * Checks if taskString is valid
	 * 
	 * @return true if taskString is valid, false otherwise
	 */
	private boolean isValidTaskString() {
		return taskString != null && !taskString.isEmpty();
	}
	
	/**
	 * Checks if ModifyCommand is a change command (modify task's name)
	 * 
	 * @return true if command is a change command, false otherwise.
	 */
	private boolean isChange() {
		return mode == MODE_CHANGE && newTaskString != null && !newTaskString.isEmpty();
	}
	
	/**
	 * Checks if ModifyCommand is a reschedule deadline (modify task's deadline)
	 * 
	 * @return true if command is a reschedule deadline, false otherwise.
	 */
	private boolean isRescheduleDeadline() {
		return mode == MODE_RESCHEDULE_DEADLINE && newDeadline != null;
	}
	
	/**
	 * Checks if ModifyCommand is a reschedule timed (modify a timed task start and end dates)
	 * 
	 * @return true if command is a reschedule timed, false otherwise.
	 */
	private boolean isRescheduleTimed() {
		return mode == MODE_RESCHEDULE_TIMED &&
				newStartDate != null &&
				newEndDate != null &&
				newStartDate.before(newEndDate);
	}
	
	/**
	 * Checks if ModifyCommand is a mark command
	 * 
	 * @return true if command is a mark command, false otherwise.
	 */
	private boolean isMark() {
		return mode == MODE_MARK;
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

	@Override
	protected State execute(State state) throws Exception {
		throw new Exception();
	}

	@Override
	protected State execute(State state, State displayState) throws Exception {
		assert(this.isValid());
		
		if (!state.hasTask(taskString)) {
			return makeErrorState(displayState,
								  String.format(FEEDBACK_NOT_FOUND, taskString));
		} else if (isOnlyTask(state)) {
			return executeTaskFound(state, displayState, false);
		} else if (isOnlyTask(displayState)) {
			return executeTaskFound(state, displayState, true);
		} else {
			return makeErrorState(displayState,
								  String.format(FEEDBACK_MULTIPLE_FOUND, taskString));
		}
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
	private State makeErrorState(State state, String feedback) {
		this.setMutator(false);
		State s = new State(state);
		s.setFeedback(feedback);
		return s;
	}
	
	/**
	 * Execution when single task found in state
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @param displayState
	 * 			display state of program
	 * 
	 * @param isFromDisplay
	 * 			true if task found is from display state, false if task is from state
	 * 
	 * @return state after execution of command
	 */
	private State executeTaskFound(State state, State displayState, boolean isFromDisplay) throws Exception {
		Task t;
		if (isFromDisplay) {
			t = findTask(displayState);
		} else {
			t = findTask(state);
		}
		State newState = new State(state);
		Task cloned = t.clone();
		if (isChange()) {
			newState.removeTask(t);
			newState.addTask(cloned);
			return executeChange(newState, cloned);
		} else if (isRescheduleDeadline()) {
			if (t instanceof DeadlineTask) {
				newState.removeTask(t);
				newState.addTask(cloned);
				return executeRescheduleDeadline(newState, (DeadlineTask) cloned);
			} else {
				return makeErrorState(displayState,
									  String.format(FEEDBACK_NOT_DEADLINE, t.getTaskDescription()));
			}
		} else if (isRescheduleTimed()) {
			if (t instanceof TimedTask) {
				newState.removeTask(t);
				newState.addTask(cloned);
				return executeRescheduleTimed(newState, (TimedTask) cloned);
			} else {
				return makeErrorState(displayState,
									  String.format(FEEDBACK_NOT_TIMED, t.getTaskDescription()));
			}
		} else if (isMark()) {
			newState.removeTask(t);
			newState.addTask(cloned);
			return executeMark(newState, cloned);
		} else {
			throw new Exception();
		}
	}
	
	/**
	 * Executes change command
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @param task
	 * 			task being changed
	 * 
	 * @return state after executing command
	 */
	private State executeChange(State state, Task task) {
		String old = task.getTaskDescription();
		task.setTaskDescription(newTaskString);
		state.setFeedback(String.format(FEEDBACK_CHANGED, old, newTaskString));
		return state;
	}
	
	/**
	 * Executes reschedule deadline
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @param task
	 * 			deadline task being rescheduled
	 * 
	 * @return state after executing command
	 */
	private State executeRescheduleDeadline(State state, DeadlineTask task) {
		task.setDeadline(newDeadline);
		state.setFeedback(String.format(FEEDBACK_RESCHEDULE_DEADLINE, task.getTaskDescription()));
		return state;
	}
	
	/**
	 * Executes reschedule timed
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @param task
	 * 			timed task being rescheduled
	 * 
	 * @return state after executing command
	 */
	private State executeRescheduleTimed(State state, TimedTask task) {
		task.setStartDate(newStartDate);
		task.setEndDate(newEndDate);
		state.setFeedback(String.format(FEEDBACK_RESCHEDULE_TIMED, task.getTaskDescription()));
		return state;
	}
	
	/**
	 * Executes mark command
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @param task
	 * 			task being marked/unmarked
	 * 
	 * @return state after executing command
	 */
	private State executeMark(State state, Task task) {
		if (mark) {
			if (task.isComplete()) {
				state.setFeedback(String.format(FEEDBACK_ALREADY_MARKED, task.getTaskDescription()));
			} else {
				task.markAsDone();
				state.setFeedback(String.format(FEEDBACK_MARK_SUCCESS, task.getTaskDescription()));
			}
		} else {
			if (!task.isComplete()) {
				state.setFeedback(String.format(FEEDBACK_ALREADY_UNMARKED, task.getTaskDescription()));
			} else {
				task.markAsPending();
				state.setFeedback(String.format(FEEDBACK_UNMARK_SUCCESS, task.getTaskDescription()));
			}
		}
		return state;
	}
}