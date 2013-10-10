package todo;

/**
 * Subclass to encapsulate add commands
 * 
 * @author Eugene
 * 
 */	
public class AddCommand extends Command {
	private Task task;
	
	/**
	 * Constructor for AddCommand
	 * 
	 * @param t
	 * 			the task to be added
	 * 
	 */
	AddCommand(Task t) {
		super(true);
		task = t;
	}
	
	@Override
	protected boolean isValid(State state) {
		if (task == null) {
			return false;
		}
		
		if (!isValidDescription(task.getTaskDescription())) {
			return false;
		}

		if (task instanceof DeadlineTask) {
			return isValidDeadlineTask((DeadlineTask) task);
		}
		if (task instanceof TimedTask) {
			return isValidTimedTask((TimedTask) task);
		}
		return true;
	}
	
	/**
	 * Checks whether a task's description is valid
	 * Used in isValid method
	 * Description should not be null or empty string.
	 * 
	 * @param desc
	 * 			the description of the task
	 * 
	 * @return true if description is valid. false if otherwise.
	 * 
	 */
	private boolean isValidDescription(String desc) {
		return (desc != null && !desc.isEmpty());
	}
	
	/**
	 * Checks whether a DeadlineTask is valid
	 * Used in isValid method
	 * Deadline must not be null for it to be valid.
	 * 
	 * @param task
	 * 			the DeadlineTask
	 * 
	 * @return true if DeadlineTask is valid. false if otherwise.
	 * 
	 */
	private boolean isValidDeadlineTask(DeadlineTask task) {
		return (task.getDeadline() != null);
	}
	
	/**
	 * Checks whether a TimedTask is valid
	 * Used in isValid method
	 * Start time and end time must not be null.
	 * Start time must be before end time.
	 * 
	 * @param task
	 * 			the TimedTask
	 * 
	 * @return true if TimedTask is valid. false if otherwise.
	 * 
	 */
	private boolean isValidTimedTask(TimedTask task) {
		if (task.getStartDate() == null || task.getEndDate() == null) {
			return false;
		}
		return task.getStartDate().before(task.getEndDate());
	}
	
	@Override
	protected State execute(State state) {
		State s = new State(state);
		if (task instanceof TimedTask) {
			s.addTask((TimedTask) task);
			s.setFeedback("added new timed task: " + task.getTaskDescription());
			return s;
		}
		if (task instanceof DeadlineTask) {
			s.addTask((DeadlineTask) task);
			s.setFeedback("added new deadline task: " + task.getTaskDescription());
			return s;
		}
		if (task instanceof FloatingTask) {
			s.addTask((FloatingTask) task);
			s.setFeedback("added new floating task: " + task.getTaskDescription());
			return s;
		}
		return null;
	}
}
