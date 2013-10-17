package todo;

import java.util.logging.*;

/**
 * Subclass to encapsulate delete commands
 * 
 * @author Eugene
 * 
 */
public class DeleteCommand extends Command {
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
	protected boolean isValid(State state) {
		return (taskString == null); 
	}

	@Override
	protected State execute(State state) throws Exception {
		assert(this.isValid(state));
		
		State s = new State(state);
		if (!s.hasTask(taskString)) {
			logger.log(Level.INFO, "delete: task not found");
			s.setFeedback("Could not find tasks matching: " + taskString);
			return s;
		} else {
			Task deletedTask = findTask(s);
			s.removeTask(deletedTask);
			s.setFeedback("Removed: " + deletedTask.getTaskDescription());
			return s;
		}
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