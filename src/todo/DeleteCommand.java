package todo;

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
		return (taskString == null || state.hasTask(taskString)); 
	}

	@Override
	protected State execute(State state) {
		State s = new State(state);
		Task deletedTask = findTask(s);
		s.removeTask(deletedTask);
		return s;			
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