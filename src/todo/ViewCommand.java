package todo;

import java.util.Calendar;
import java.util.TreeSet;
import java.util.logging.*;

/**
 * Subclass to encapsulate view commands
 * 
 * @author Eugene
 * 
 */
public class ViewCommand extends Command {
	public static final int MODE_VIEW_ALL = 0;
	public static final int MODE_VIEW_FLOATING = 1;
	public static final int MODE_VIEW_TIMED = 2;
	public static final int MODE_VIEW_DEADLINE = 3;
	public static final int MODE_VIEW_DATE = 4;
	public static final int MODE_VIEW_TAG = 5;
	public static final int MODE_VIEW_EXPIRED = 6;
	public static final int MODE_VIEW_DONE = 7;

	private static final String FEEDBACK_VIEW_ALL = "viewing all tasks";
	private static final String FEEDBACK_VIEW_FLOATING = "viewing flexible tasks";
	private static final String FEEDBACK_VIEW_TIMED = "viewing events";
	private static final String FEEDBACK_VIEW_DEADLINE = "viewing deadlines";
	private static final String FEEDBACK_VIEW_DATE = "viewing date: %1$s";
	private static final String FEEDBACK_VIEW_TAG = "viewing tag: #%1$s";
	private static final String FEEDBACK_VIEW_DATE_NOT_FOUND = "no tasks found for %1$s";
	private static final String FEEDBACK_VIEW_EXPIRED = "viewing expired tasks";
	private static final String FEEDBACK_VIEW_DONE = "viewing completed tasks";
	
	private static final String LOG_ERROR = "error executing view";
	private static final String LOG_VIEW_ALL = "executing view all";
	private static final String LOG_VIEW_FLOATING = "executing view floating";
	private static final String LOG_VIEW_TIMED = "executing view timed";
	private static final String LOG_VIEW_DEADLINE = "executing view deadline";
	private static final String LOG_VIEW_DATE = "executing view date";
	private static final String LOG_VIEW_TAG = "executing view tag";
	private static final String LOG_VIEW_EXPIRED = "executing view expired";
	private static final String LOG_VIEW_DONE = "executing view done";

	private static final String DATE_FORMAT = "%1$s/%2$s/%3$s";
	private static final int MONTH_OFFSET = 1;

	private Calendar date;
	private String tag;
	private int mode;

	/**
	 * Empty constructor for ViewCommand
	 * 
	 * Represents a view all command
	 * 
	 */
	ViewCommand() {
		super(false);
		mode = MODE_VIEW_ALL;
	}

	/**
	 * Constructor for ViewCommand
	 * 
	 * @param mode
	 *            defines what the viewCommand does possible modes:
	 *            MODE_VIEW_ALL, MODE_VIEW_FLOATING, MODE_VIEW_DEADLINE,
	 *            MODE_VIEW_TIMED, MODE_VIEW_EXPIRED, MODE_VIEW_DONE
	 * 
	 */
	ViewCommand(int mode) {
		this();
		this.mode = mode;
	}

	/**
	 * Constructor for ViewCommand
	 * 
	 * @param date
	 *            date to view tasks by
	 * 
	 */
	ViewCommand(Calendar date) {
		this();
		this.date = date;
		this.mode = MODE_VIEW_DATE;
	}

	/**
	 * Constructor for ViewCommand
	 * 
	 * @param tag
	 *            tag to view tasks by
	 * 
	 */
	ViewCommand(String tag) {
		this();
		this.tag = tag;
		this.mode = MODE_VIEW_TAG;
	}

	@Override
	protected boolean isValid() {
		return (isViewAll() || isViewFloating() || isViewDeadline()
				|| isViewTimed() || isViewDate() || isViewTag()
				|| isViewExpired() || isViewDone());
	}

	/**
	 * Checks if command is a "view all" command
	 * 
	 */
	private boolean isViewAll() {
		return mode == MODE_VIEW_ALL;
	}

	/**
	 * Checks if command is a "view floating" command
	 * 
	 */
	private boolean isViewFloating() {
		return mode == MODE_VIEW_FLOATING;
	}

	/**
	 * Checks if command is a "view deadline" command
	 * 
	 */
	private boolean isViewDeadline() {
		return mode == MODE_VIEW_DEADLINE;
	}

	/**
	 * Checks if command is a "view timed" command
	 * 
	 */
	private boolean isViewTimed() {
		return mode == MODE_VIEW_TIMED;
	}

	/**
	 * Checks if command is a "view date" command
	 * 
	 */
	private boolean isViewDate() {
		return (mode == MODE_VIEW_DATE && date != null);
	}

	/**
	 * Checks if command is a "view tags" command
	 * 
	 */
	private boolean isViewTag() {
		return (mode == MODE_VIEW_TAG && tag != null);
	}
	
	/**
	 * Checks if command is a "view expired" command
	 * 
	 */
	private boolean isViewExpired() {
		return mode == MODE_VIEW_EXPIRED;
	}
	
	/**
	 * Checks if command is a "view done" command
	 * 
	 */
	private boolean isViewDone() {
		return mode == MODE_VIEW_DONE;
	}

	@Override
	protected State execute(State state) throws Exception {
		assert (this.isValid());
		if (isViewAll()) {
			logger.log(Level.INFO, LOG_VIEW_ALL);
			return executeViewAll(state);
		} else if (isViewFloating()) {
			logger.log(Level.INFO, LOG_VIEW_FLOATING);
			return executeViewFloating(state);
		} else if (isViewDeadline()) {
			logger.log(Level.INFO, LOG_VIEW_DEADLINE);
			return executeViewDeadline(state);
		} else if (isViewTimed()) {
			logger.log(Level.INFO, LOG_VIEW_TIMED);
			return executeViewTimed(state);
		} else if (isViewDate()) {
			logger.log(Level.INFO, LOG_VIEW_DATE);
			return executeViewDate(state);
		} else if (isViewTag()) {
			logger.log(Level.INFO, LOG_VIEW_TAG);
			return executeViewTag(state);
		} else if (isViewExpired()) {
			logger.log(Level.INFO, LOG_VIEW_EXPIRED);
			return executeViewExpired(state);
		} else if (isViewDone()) {
			logger.log(Level.INFO, LOG_VIEW_DONE);
			return executeViewDone(state);
		} else {
			logger.log(Level.WARNING, LOG_ERROR);
			throw new Exception();
		}
	}

	/**
	 * Executes view all command
	 * 
	 * @param state
	 *            current state of program
	 * @return state after executing command
	 */
	private State executeViewAll(State state) {
		State s = new State(state);
		s.setFeedback(new Feedback(FEEDBACK_VIEW_ALL, true));
		return s;
	}

	/**
	 * Executes view floating command
	 * 
	 * @param state
	 *            current state of program
	 * @return state after executing command
	 */
	private State executeViewFloating(State state) {
		State s = new State(state);
		s.getDeadlineTasks().clear();
		s.getTimedTasks().clear();
		s.setFeedback(new Feedback(FEEDBACK_VIEW_FLOATING, true));
		return s;
	}

	/**
	 * Executes view deadline command
	 * 
	 * @param state
	 *            current state of program
	 * @return state after executing command
	 */
	private State executeViewDeadline(State state) {
		State s = new State(state);
		s.getFloatingTasks().clear();
		s.getTimedTasks().clear();
		s.setFeedback(new Feedback(FEEDBACK_VIEW_DEADLINE, true));
		return s;
	}

	/**
	 * Executes view timed command
	 * 
	 * @param state
	 *            current state of program
	 * @return state after executing command
	 */
	private State executeViewTimed(State state) {
		State s = new State(state);
		s.getDeadlineTasks().clear();
		s.getFloatingTasks().clear();
		s.setFeedback(new Feedback(FEEDBACK_VIEW_TIMED, true));
		return s;
	}

	/**
	 * Executes view date command
	 * 
	 * @param state
	 *            current state of program
	 * @return state after executing command
	 */
	private State executeViewDate(State state) {
		State s = new State();
		int dd = date.get(Calendar.DATE);
		int mm = date.get(Calendar.MONTH);
		int yy = date.get(Calendar.YEAR);
		TreeSet<DeadlineTask> deadline = state.getDeadlineTasks();
		TreeSet<TimedTask> timed = state.getTimedTasks();
		for (DeadlineTask cur : deadline) {
			if (dd == cur.getDeadline().get(Calendar.DATE)
					&& mm == cur.getDeadline().get(Calendar.MONTH)
					&& yy == cur.getDeadline().get(Calendar.YEAR)) {
				s.addTask(cur);
			}
		}
		for (TimedTask cur : timed) {
			if (date.compareTo(cur.getStartDate()) >= 0
					&& date.compareTo(cur.getEndDate()) <= 0) {
				s.addTask(cur);
			}
		}
		String dateStr = String.format(DATE_FORMAT, String.valueOf(dd), String
				.valueOf(mm + MONTH_OFFSET), String.valueOf(yy));
		s.setFeedback(new Feedback(String.format(FEEDBACK_VIEW_DATE, dateStr), true));

		if (!s.hasDateTasks()) {
			state.setFeedback(new Feedback(String.format(FEEDBACK_VIEW_DATE_NOT_FOUND, dateStr),false));
			return state;
		}

		return s;
	}

	/**
	 * Executes view tag command
	 * 
	 * @param state
	 *            current state of program
	 * @return state after executing command
	 */
	private State executeViewTag(State state) {
		State s = new State();
		TreeSet<DeadlineTask> deadline = state.getDeadlineTasks();
		TreeSet<TimedTask> timed = state.getTimedTasks();
		TreeSet<FloatingTask> floating = state.getFloatingTasks();
		for (DeadlineTask cur : deadline) {
			if (cur.hasTag(tag)) {
				s.addTask(cur);
			}
		}
		for (TimedTask cur : timed) {
			if (cur.hasTag(tag)) {
				s.addTask(cur);
			}
		}
		for (FloatingTask cur : floating) {
			if (cur.hasTag(tag)) {
				s.addTask(cur);
			}
		}
		s.setFeedback(new Feedback(String.format(FEEDBACK_VIEW_TAG, tag),true));
		return s;
	}
	
	/**
	 * Executes view expired command
	 * 
	 * @param state
	 * 			state of current program
	 * 
	 * @return state after execution
	 */
	private State executeViewExpired(State state) {
		State s = new State();
		for (Task t : state.getAllTasks()) {
			if (t.isExpired() && !t.isComplete()) {
				s.addTask(t);
			}
		}
		s.setFeedback(new Feedback(FEEDBACK_VIEW_EXPIRED, true));
		return s;
	}
	
	/**
	 * Executes view done command
	 * 
	 * @param state
	 * 			state of current program
	 * 
	 * @return state after execution
	 */
	private State executeViewDone(State state) {
		State s = new State();
		for (Task t : state.getAllTasks()) {
			if (t.isComplete()) {
				s.addTask(t);
			}
		}
		s.setFeedback(new Feedback(FEEDBACK_VIEW_DONE, true));
		return s;
	}
}