package todo;

import java.util.logging.*;

/**
 * Subclass to encapsulate add commands
 * 
 * @author Eugene
 * 
 */	
public class AddCommand extends Command {
	private static final String FEEDBACK_TIMED = "added new timed task: %1$s";
	private static final String FEEDBACK_DEADLINE = "added new deadline task: %1$s";
	private static final String FEEDBACK_FLOATING = "added new floating task: %1$s";
	
	private static final String LOG_ERROR = "error executing add";
	private static final String LOG_MESSAGE = "executing add";
	
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
	protected boolean isValid() {
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
	protected State execute(State state) throws Exception {
		assert(this.isValid());
		State s = new State(state);
		s.addTask(task);
		String desc = task.getTaskDescription();
		logger.log(Level.INFO, LOG_MESSAGE);
		if (task instanceof TimedTask) {
			s.setFeedback(String.format(FEEDBACK_TIMED, desc));
			return s;
		} else if (task instanceof DeadlineTask) {
			s.setFeedback(String.format(FEEDBACK_DEADLINE, desc));
			return s;
		} else if (task instanceof FloatingTask) {
			s.setFeedback(String.format(FEEDBACK_FLOATING, desc));
			return s;
		} else {
			logger.log(Level.WARNING, LOG_ERROR);
			throw new Exception();
		}
	}
}
