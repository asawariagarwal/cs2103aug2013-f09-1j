package todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

/**
 * Subclass to encapsulate modify commands
 * 
 * @author Eugene
 * 
 */
public class ModifyCommand extends Command {
	public static final int INDEX_FLOATING = 0;
	public static final int INDEX_TIMED = 1;
	public static final int INDEX_DEADLINE = 2;
	
	private static final int MODE_CHANGE = 0;
	private static final int MODE_RESCHEDULE_DEADLINE = 1;
	private static final int MODE_RESCHEDULE_TIMED = 2;
	private static final int MODE_MARK = 3;
	private static final int MODE_TAG = 4;
	
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
	private static final String FEEDBACK_TAG_SUCCESS = "added tag(s): %1$s to %2$s";
	private static final String FEEDBACK_TAG_FAILURE = "%1$s already has tag(s): %2$s";
	private static final String FEEDBACK_UNTAG_SUCCESS = "removed tags: %1$s from %2$s";
	private static final String FEEDBACK_UNTAG_FAILURE = "%1$s does not have tag(s): %2$s";
	private static final String FEEDBACK_BAD_INDEX = "invalid index";
	
	private String taskString;
	private String newTaskString;
	private Calendar newDeadline;
	private Calendar newStartDate;
	private Calendar newEndDate;
	private boolean mark;
	private boolean tag;
	private ArrayList<String> tags;
	private int mode;
	private boolean isByIndex;
	private int indexPos;
	private int indexType;
	
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
		isByIndex = false;
	}
	
	/**
	 * Constructor for ModifyCommand
	 * 
	 * @param indexPos
	 * 			task index position
	 * 
	 * @param indexType
	 * 			type of task,
	 * 			can be the following: INDEX_FLOATING, INDEX_DEADLINE, INDEX_TIMED
	 * 
	 * @param newTaskString
	 * 			modified task name/description
	 * 
	 */
	ModifyCommand(int indexPos, int indexType, String newTaskString) {
		super(true);
		this.indexPos = indexPos;
		this.indexType = indexType;
		this.newTaskString = newTaskString;
		this.mode = MODE_CHANGE;
		isByIndex = true;
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
		isByIndex = false;
	}
	
	/**
	 * Constructor for ModifyCommand
	 * 
	 * @param indexPos
	 * 			task index position
	 * 
	 * @param indexType
	 * 			type of task,
	 * 			can be the following: INDEX_FLOATING, INDEX_DEADLINE, INDEX_TIMED
	 * 
	 * @param newDeadline
	 * 			modified deadline
	 * 
	 */
	ModifyCommand(int indexPos, int indexType, Calendar newDeadline) {
		super(true);
		this.indexPos = indexPos;
		this.indexType = indexType;
		this.newDeadline = newDeadline;
		this.mode = MODE_RESCHEDULE_DEADLINE;
		isByIndex = true;
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
		isByIndex = false;
	}
	
	/**
	 * Constructor for ModifyCommand
	 * 
	 * @param indexPos
	 * 			task index position
	 * 
	 * @param indexType
	 * 			type of task,
	 * 			can be the following: INDEX_FLOATING, INDEX_DEADLINE, INDEX_TIMED
	 * 
	 * @param mark
	 * 			true to mark, false to unmark
	 * 
	 */
	ModifyCommand(int indexPos, int indexType, boolean mark) {
		super(true);
		this.indexPos = indexPos;
		this.indexType = indexType;
		this.mark = mark;
		this.mode = MODE_MARK;
		isByIndex = true;
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
		isByIndex = false;
	}
	
	/**
	 * Constructor for ModifyCommand
	 * 
	 * @param indexPos
	 * 			task index position
	 * 
	 * @param indexType
	 * 			type of task,
	 * 			can be the following: INDEX_FLOATING, INDEX_DEADLINE, INDEX_TIMED
	 * 
	 * @param newStartDate
	 * 			modified start date
	 * 
	 * @param newEndDate
	 * 			modified end date
	 * 
	 */
	ModifyCommand(int indexPos, int indexType, Calendar newStartDate, Calendar newEndDate) {
		super(true);
		this.indexPos = indexPos;
		this.indexType = indexType;
		this.newStartDate = newStartDate;
		this.newEndDate = newEndDate;
		this.mode = MODE_RESCHEDULE_TIMED;
		isByIndex = true;
	}
	
	/**
	 * Constructor for ModifyCommand
	 * 
	 * @param taskString
	 * 			task name/description
	 * 
	 * @param tags
	 * 			tags to be tagged/untagged
	 * 
	 * @param tag
	 * 			true to tag, false to untag
	 * 
	 */
	ModifyCommand(String taskString, ArrayList<String> tags, boolean tag) {
		super(true);
		this.taskString = taskString;
		this.tags = tags;
		this.tag = tag;
		this.mode = MODE_TAG;
		isByIndex = false;
	}
	
	/**
	 * Constructor for ModifyCommand
	 * 
	 * @param indexPos
	 * 			task index position
	 * 
	 * @param indexType
	 * 			type of task,
	 * 			can be the following: INDEX_FLOATING, INDEX_DEADLINE, INDEX_TIMED
	 * 
	 * @param tags
	 * 			tags to be tagged/untagged
	 * 
	 * @param tag
	 * 			true to tag, false to untag
	 * 
	 */
	ModifyCommand(int indexPos, int indexType, ArrayList<String> tags, boolean tag) {
		super(true);
		this.indexPos = indexPos;
		this.indexType = indexType;
		this.tags = tags;
		this.tag = tag;
		this.mode = MODE_TAG;
		isByIndex = true;
	}

	@Override
	protected boolean isValid() {
		return (isValidTaskString() || isValidByIndex()) && 
				(isChange() || isRescheduleDeadline() ||
				 isRescheduleTimed() || isMark() ||
				 isTag());
	}
	
	/**
	 * Checks if taskString is valid
	 * 
	 * @return true if taskString is valid, false otherwise
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
	 * Checks if ModifyCommand is a tag command
	 * 
	 * @return true if command is a tag command, false otherwise.
	 */
	private boolean isTag() {
		return mode == MODE_TAG && tags != null && !tags.isEmpty();
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
		
		if (isByIndex) {
			return executeByIndex(state, displayState);
		}
		
		if (!state.hasTask(taskString)) {
			return makeErrorState(displayState,
								  new Feedback(String.format(FEEDBACK_NOT_FOUND, taskString),false));
		} else if (isOnlyTask(state)) {
			return executeTaskFound(state, displayState, false);
		} else if (isOnlyTask(displayState)) {
			return executeTaskFound(state, displayState, true);
		} else {
			return makeErrorState(displayState,
								  new Feedback(String.format(FEEDBACK_MULTIPLE_FOUND, taskString),false));
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
	private State makeErrorState(State state, Feedback feedback) {
		this.setMutator(false);
		State s = new State(state);
		s.setFeedback(feedback);
		return s;
	}
	
	/**
	 * Execution by index
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @param displayState
	 * 			display state of program
	 * 
	 * @return state after execution of command
	 */
	private State executeByIndex(State state, State displayState) throws Exception {
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
		boolean isFound = false;
		Task t = null;
		State newState = new State(state);
		for (Task task : tasks) {
			if (i > indexPos) {
				break;
			} else if (i == indexPos) {
				isFound = true;
				t = task;
				break;
			}
			i++;
		}
		if (!isFound) {
			return makeErrorState(displayState, new Feedback(FEEDBACK_BAD_INDEX, false));
		}
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
									  new Feedback(String.format(FEEDBACK_NOT_DEADLINE, t.getTaskDescription()),false));
			}
		} else if (isRescheduleTimed()) {
			if (t instanceof TimedTask) {
				newState.removeTask(t);
				newState.addTask(cloned);
				return executeRescheduleTimed(newState, (TimedTask) cloned);
			} else {
				return makeErrorState(displayState,
									  new Feedback(String.format(FEEDBACK_NOT_TIMED, t.getTaskDescription()),false));
			}
		} else if (isMark()) {
			newState.removeTask(t);
			newState.addTask(cloned);
			return executeMark(newState, cloned);
		} else if (isTag()) {
			newState.removeTask(t);
			newState.addTask(cloned);
			return executeTag(newState, cloned);
		} else {
			throw new Exception();
		}		
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
									  new Feedback(String.format(FEEDBACK_NOT_DEADLINE, t.getTaskDescription()),false));
			}
		} else if (isRescheduleTimed()) {
			if (t instanceof TimedTask) {
				newState.removeTask(t);
				newState.addTask(cloned);
				return executeRescheduleTimed(newState, (TimedTask) cloned);
			} else {
				return makeErrorState(displayState,
									  new Feedback(String.format(FEEDBACK_NOT_TIMED, t.getTaskDescription()),false));
			}
		} else if (isMark()) {
			newState.removeTask(t);
			newState.addTask(cloned);
			return executeMark(newState, cloned);
		} else if (isTag()) {
			newState.removeTask(t);
			newState.addTask(cloned);
			return executeTag(newState, cloned);
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
		state.setFeedback(new Feedback (String.format(FEEDBACK_CHANGED, old, newTaskString), true));
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
		state.setFeedback(new Feedback(String.format(FEEDBACK_RESCHEDULE_DEADLINE, task.getTaskDescription()),true));
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
		state.setFeedback(new Feedback(String.format(FEEDBACK_RESCHEDULE_TIMED, task.getTaskDescription()), true));
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
				state.setFeedback(new Feedback(String.format(FEEDBACK_ALREADY_MARKED, task.getTaskDescription()),false));
			} else {
				task.markAsDone();
				state.setFeedback(new Feedback(String.format(FEEDBACK_MARK_SUCCESS, task.getTaskDescription()),true));
			}
		} else {
			if (!task.isComplete()) {
				state.setFeedback(new Feedback(String.format(FEEDBACK_ALREADY_UNMARKED, task.getTaskDescription()),false));
			} else {
				task.markAsPending();
				state.setFeedback(new Feedback(String.format(FEEDBACK_UNMARK_SUCCESS, task.getTaskDescription()), true));
			}
		}
		return state;
	}
	
	/**
	 * Executes tag command
	 * 
	 * @param state
	 * 			state of program
	 * 
	 * @param task
	 * 			task being tag/untagged
	 * 
	 * @return state after executing command
	 */
	private State executeTag(State state, Task task) {
		String feedback = "";
		ArrayList<String> success = new ArrayList<String>();
		ArrayList<String> failure = new ArrayList<String>();
		for (String t : tags) {
			if (task.hasTag(t)) {
				if (tag) {
					addTagToList(failure, t);
				} else {
					task.removeTag(t);
					addTagToList(success, t);
				}
			} else {
				if (tag) {
					task.addTag(t);
					addTagToList(success, t);
				} else {
					addTagToList(failure, t);
				}
			}
		}
		if (!success.isEmpty()) {
			String msg;
			if (tag) {
				msg = FEEDBACK_TAG_SUCCESS;
			} else {
				msg = FEEDBACK_UNTAG_SUCCESS;
			}
			feedback += String.format(msg,
					getTagString(success),
					task.getTaskDescription());
		}
		if (!failure.isEmpty()) {
			if (!feedback.isEmpty()) {
				feedback += System.lineSeparator();
			}
			String msg;
			if (tag) {
				msg = FEEDBACK_TAG_FAILURE;
			} else {
				msg = FEEDBACK_UNTAG_FAILURE;
			}
			feedback += String.format(msg,
					task.getTaskDescription(),
					getTagString(failure));
		}
		state.setFeedback(new Feedback(feedback, false));
		return state;
	}
	
	/**
	 * Adds a tag to a tags list, appending "#" in front
	 * 
	 * @param tagList
	 * 			list of tags
	 * 
	 * @param tagStr
	 * 			tag to be added to list
	 * 
	 * @return string of tags
	 */
	private void addTagToList(ArrayList<String> tagList, String tagStr) {
		if (tagList == null) {
			return;
		} else {
			tagList.add("#" + tagStr);
		}
	}
	
	/**
	 * Gets string representation of tags list, without square brackets
	 * 
	 * @param tagList
	 * 			list of tags
	 * 
	 * @return string of tags
	 */
	private String getTagString(ArrayList<String> tagList) {
		if (tagList == null || tagList.isEmpty()) {
			return "";
		} else {
			String tagStr = tagList.toString();
			return tagStr.substring(1, tagStr.length()-1);
		}
	}
}