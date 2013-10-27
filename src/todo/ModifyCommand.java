package todo;

import java.util.Calendar;

/**
 * Subclass to encapsulate modify commands
 * 
 * @author Eugene
 * 
 */
public class ModifyCommand extends Command {
	private String taskString;
	private String newTaskString;
	private Calendar newDeadline;
	private Calendar newStartDate;
	private Calendar newEndDate;
	
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
	}

	@Override
	protected boolean isValid(State state) {
		if (taskString == null || !state.hasTask(taskString)) {
			return false;
		}
		if (isModifyTaskName()) {
			return true;
		}
		if (isModifyDeadline()) {
			Task t = findTask(state);
			return t instanceof DeadlineTask;
		}
		if (isModifyStartAndEnd()) {
			Task t = findTask(state);
			return t instanceof TimedTask;
		}
		return false;
	}
	
	/**
	 * Checks if ModifyCommand is to modify a task's name
	 * 
	 * @return true if command modifies task's name, false otherwise.
	 */
	private boolean isModifyTaskName() {
		return newTaskString != null &&
				newDeadline == null &&
				newStartDate == null &&
				newEndDate == null;
	}
	
	/**
	 * Checks if ModifyCommand is to modify a task's deadline
	 * 
	 * @return true if command modifies deadline, false otherwise.
	 */
	private boolean isModifyDeadline() {
		return newTaskString == null && 
				newDeadline != null &&
				newStartDate == null &&
				newEndDate == null;
	}
	
	/**
	 * Checks if ModifyCommand is to modify a timed task start and end dates
	 * 
	 * @return true if command modifies start and end dates, false otherwise.
	 */
	private boolean isModifyStartAndEnd() {
		return newTaskString == null && 
				newDeadline == null &&
				newStartDate != null &&
				newEndDate != null;
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
	protected State execute(State state) {
		State s = new State(state);
		Task t = findTask(s);
		if (isModifyTaskName()) {
			String old = t.getTaskDescription();
			t.setTaskDescription(newTaskString);
			s.setFeedback("modified: \"" + old + "\" to \"" + newTaskString + "\"");
			return s;
		}
		if (isModifyDeadline()) {
			DeadlineTask d = (DeadlineTask) t;
			d.setDeadline(newDeadline);
			s.setFeedback("modified deadline of " + d.getTaskDescription());
		}
		if (isModifyStartAndEnd()) {
			TimedTask tt = (TimedTask) t;
			tt.setStartDate(newStartDate);
			tt.setEndDate(newEndDate);
			s.setFeedback("modified dates of " + tt.getTaskDescription());
		}
		return null;
	}
}