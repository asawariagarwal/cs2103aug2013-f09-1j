package todo;
import java.util.logging.*;

/**
 * Subclass to encapsulate clear commands
 * 
 * @author Eugene
 * 
 */
public class ClearCommand extends Command {
	public static final int MODE_CLEAR_ALL = 0;
	public static final int MODE_CLEAR_FLOATING = 1;
	public static final int MODE_CLEAR_DEADLINE = 2;
	public static final int MODE_CLEAR_TIMED = 3;
	public static final int MODE_CLEAR_EXPIRED = 4;
	public static final int MODE_CLEAR_DONE = 5;
	
	private int mode;
	/**
	 * Constructor for ClearCommand
	 */
	public ClearCommand() {
		super(true);
		this.mode = MODE_CLEAR_ALL;
	}
	
	/**
	 * Constructor for ClearCommand
	 * 
	 * @param mode
	 * 			defines what clear command does
	 * 			possible modes:
	 * 				MODE_CLEAR_ALL, MODE_CLEAR_DEADLINE,
	 * 				MODE_CLEAR_TIMED, MODE_CLEAR_EXPIRED,
	 * 				MODE_CLEAR_DONE
	 */
	public ClearCommand(int mode) {
		super(true);
		this.mode = mode;
	}

	@Override
	protected boolean isValid() {
		return mode >= MODE_CLEAR_ALL && mode <= MODE_CLEAR_DONE;
	}

	@Override
	protected State execute(State state) throws Exception {
		State s = new State(state);
		if (mode == MODE_CLEAR_ALL) {
			s.getDeadlineTasks().clear();
			s.getFloatingTasks().clear();
			s.getTimedTasks().clear();
			s.setFeedback("all tasks cleared");
			return s;
		} else if (mode == MODE_CLEAR_DEADLINE) {
			s.getDeadlineTasks().clear();
			s.setFeedback("deadline tasks cleared");
			return s;
		} else if (mode == MODE_CLEAR_TIMED) {
			s.getTimedTasks().clear();
			s.setFeedback("timed tasks cleared");
			return s;
		} else if (mode == MODE_CLEAR_FLOATING) {
			s.getFloatingTasks().clear();
			s.setFeedback("floating tasks cleared");
			return s;
		} else if (mode == MODE_CLEAR_EXPIRED) {
			for (Task t : s.getAllTasks()) {
				if (t.isExpired()) {
					s.removeTask(t);
				}
			}
			s.setFeedback("expired tasks cleared");
			return s;
		} else if (mode == MODE_CLEAR_DONE) {
			for (Task t : s.getAllTasks()) {
				if (t.isComplete()) {
					s.removeTask(t);
				}
			}
			s.setFeedback("completed tasks cleared");
			return s;
		} else {
			throw new Exception();
		}
	}
}
