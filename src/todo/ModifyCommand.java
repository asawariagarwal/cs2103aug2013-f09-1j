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
	private State executeTaskFound(State state) throws Exception {
		Task t = findTask(state);
		if (isChange()) {
			return executeChange(state, t);
		} else if (isRescheduleDeadline()) {
			return executeRescheduleDeadline(state, (DeadlineTask) t);
		} else if (isRescheduleTimed()) {
			return executeRescheduleTimed(state, (TimedTask) t);
		} else if (isMark()) {
			return executeMark(state, t);
		} else {
			throw new Exception();
		}
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
	private State executeTaskFoundDisplay(State state, State displayState) throws Exception {
		Task t = findTask(displayState);
		if (isChange()) {
			return executeChange(state, t);
		} else if (isRescheduleDeadline()) {
			return executeRescheduleDeadline(state, (DeadlineTask) t);
		} else if (isRescheduleTimed()) {
			return executeRescheduleTimed(state, (TimedTask) t);
		} else if (isMark()) {
			return executeMark(state, t);
		} else {
			throw new Exception();
		}
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
		State s = new State(state);
		String old = task.getTaskDescription();
		task.setTaskDescription(newTaskString);
		s.setFeedback("changed: \"" + old + "\" to \"" + newTaskString + "\"");
		return s;
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
		State s = new State(state);
		task.setDeadline(newDeadline);
		s.setFeedback("rescheduled deadline of: " + task.getTaskDescription());
		return s;
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
		State s = new State(state);
		task.setStartDate(newStartDate);
		task.setEndDate(newEndDate);
		s.setFeedback("reschedule date of: " + task.getTaskDescription());
		return s;
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
		State s = new State(state);
		if (mark) {
			if (task.isComplete()) {
				s.setFeedback(task.getTaskDescription() + " is already marked");
			} else {
				task.markAsDone();
				s.setFeedback("marked: " + task.getTaskDescription());
			}
		} else {
			if (!task.isComplete()) {
				s.setFeedback(task.getTaskDescription() + " is already unmarked");
			} else {
				task.markAsPending();
				s.setFeedback("unmarked: " + task.getTaskDescription());
			}
		}
		return s;
	}
}