package todo;

/**
 * Subclass to encapsulate clear commands
 * 
 * @author A0097199H
 * 
 */
public class ClearCommand extends Command {
	public static final int MODE_CLEAR_ALL = 0;
	public static final int MODE_CLEAR_FLOATING = 1;
	public static final int MODE_CLEAR_DEADLINE = 2;
	public static final int MODE_CLEAR_TIMED = 3;
	public static final int MODE_CLEAR_EXPIRED = 4;
	public static final int MODE_CLEAR_DONE = 5;
	
	private static final String FEEDBACK_CLEAR_ALL = "all tasks cleared";
	private static final String FEEDBACK_CLEAR_DEADLINE = "deadlines cleared";
	private static final String FEEDBACK_CLEAR_TIMED = "events cleared";
	private static final String FEEDBACK_CLEAR_FLOATING = "flexible tasks cleared";
	private static final String FEEDBACK_CLEAR_EXPIRED = "expired tasks cleared";
	private static final String FEEDBACK_CLEAR_DONE = "completed tasks cleared";
	
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
		return mode == MODE_CLEAR_ALL ||
				mode == MODE_CLEAR_FLOATING ||
				mode == MODE_CLEAR_DEADLINE ||
				mode == MODE_CLEAR_TIMED ||
				mode == MODE_CLEAR_EXPIRED ||
				mode == MODE_CLEAR_DONE;
	}

	@Override
	protected State execute(State state) throws Exception {
		State s = new State(state);
		if (mode == MODE_CLEAR_ALL) {
			s.getDeadlineTasks().clear();
			s.getFloatingTasks().clear();
			s.getTimedTasks().clear();
			Feedback f = new Feedback(FEEDBACK_CLEAR_ALL, true);
			s.setFeedback(f);
			return s;
		} else if (mode == MODE_CLEAR_DEADLINE) {
			s.getDeadlineTasks().clear();
			Feedback f = new Feedback(FEEDBACK_CLEAR_DEADLINE, true);
			s.setFeedback(f);
			return s;
		} else if (mode == MODE_CLEAR_TIMED) {
			s.getTimedTasks().clear();
			Feedback f = new Feedback(FEEDBACK_CLEAR_TIMED, true);
			s.setFeedback(f);
			return s;
		} else if (mode == MODE_CLEAR_FLOATING) {
			s.getFloatingTasks().clear();
			Feedback f = new Feedback(FEEDBACK_CLEAR_FLOATING, true);
			s.setFeedback(f);
			return s;
		} else if (mode == MODE_CLEAR_EXPIRED) {
			for (Task t : s.getAllTasks()) {
				if (t.isExpired()) {
					s.removeTask(t);
				}
			}
			Feedback f = new Feedback(FEEDBACK_CLEAR_EXPIRED, true);
			s.setFeedback(f);
			return s;
		} else if (mode == MODE_CLEAR_DONE) {
			for (Task t : s.getAllTasks()) {
				if (t.isComplete()) {
					s.removeTask(t);
				}
			}
			Feedback f = new Feedback(FEEDBACK_CLEAR_DONE, true);
			s.setFeedback(f);
			return s;
		} else {
			throw new Exception();
		}
	}
}
