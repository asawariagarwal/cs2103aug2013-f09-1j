package todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;
import java.util.logging.*;

import todo.Feedback;

/**
 * Subclass to encapsulate view commands
 * 
 * @author A0097199H
 * 
 */
public class ViewCommand extends Command {
	private static final String TASK_DESCRIPTION_EMPTY = "[EMPTY]";

	public static final int MODE_VIEW_ALL = 0;
	public static final int MODE_VIEW_FLOATING = 1;
	public static final int MODE_VIEW_TIMED = 2;
	public static final int MODE_VIEW_DEADLINE = 3;
	public static final int MODE_VIEW_DATE = 4;
	public static final int MODE_VIEW_TAG = 5;
	public static final int MODE_VIEW_EXPIRED = 6;
	public static final int MODE_VIEW_DONE = 7;
	public static final int MODE_VIEW_INTERVAL = 8;

	private static final String FEEDBACK_VIEW_ALL = "viewing all tasks";
	private static final String FEEDBACK_VIEW_FLOATING = "viewing flexible tasks";
	private static final String FEEDBACK_VIEW_TIMED = "viewing events";
	private static final String FEEDBACK_VIEW_DEADLINE = "viewing deadlines";
	private static final String FEEDBACK_VIEW_DATE = "viewing date: %1$s";
	private static final String FEEDBACK_VIEW_TAG = "viewing tag: #%1$s";
	private static final String FEEDBACK_VIEW_DATE_NOT_FOUND = "no tasks found for %1$s";
	private static final String FEEDBACK_VIEW_EXPIRED = "viewing expired tasks";
	private static final String FEEDBACK_VIEW_DONE = "viewing completed tasks";
	private static final String FEEDBACK_VIEW_INTERVAL = "viewing from %1$s to %2$s";

	private static final String LOG_ERROR = "error executing view";
	private static final String LOG_VIEW_ALL = "executing view all";
	private static final String LOG_VIEW_FLOATING = "executing view floating";
	private static final String LOG_VIEW_TIMED = "executing view timed";
	private static final String LOG_VIEW_DEADLINE = "executing view deadline";
	private static final String LOG_VIEW_DATE = "executing view date";
	private static final String LOG_VIEW_TAG = "executing view tag";
	private static final String LOG_VIEW_EXPIRED = "executing view expired";
	private static final String LOG_VIEW_DONE = "executing view done";
	private static final String LOG_VIEW_INTERVAL = "executing view interval";

	private static final String DATE_FORMAT = "%1$s/%2$s/%3$s";
	private static final int MONTH_OFFSET = 1;

	private Calendar date;
	private Calendar startDate;
	private Calendar endDate;
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
	 * @param date
	 *            date to view tasks by
	 * 
	 */
	ViewCommand(Calendar startDate, Calendar endDate) {
		this();
		this.startDate = startDate;
		this.endDate = endDate;
		this.mode = MODE_VIEW_INTERVAL;
		setTime(this.startDate, 0, 0, 0, 0);
		setTime(this.endDate, 23, 59, 59, 999);
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
				|| isViewExpired() || isViewDone() || isViewInterval());
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

	/**
	 * Checks if command is a "view interval" command
	 * 
	 */
	private boolean isViewInterval() {
		return mode == MODE_VIEW_INTERVAL && startDate != null
				&& endDate != null;
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
		} else if (isViewInterval()) {
			logger.log(Level.INFO, LOG_VIEW_INTERVAL);
			return executeViewInterval(state);
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
	 * 
	 * @author A0098219
	 */
	private State executeViewDate(State state) {
		State s = new State();
		int dd = date.get(Calendar.DATE);
		int mm = date.get(Calendar.MONTH);
		int yy = date.get(Calendar.YEAR);
		TreeSet<DeadlineTask> deadline = state.getDeadlineTasks();
		TreeSet<TimedTask> timed = state.getTimedTasks();
		for (DeadlineTask cur : deadline) {
			if (isSameDate(dd, mm, yy, cur)) {
				s.addTask(cur);
			}
		}
		Calendar previousEndTime = Calendar.getInstance();
		Calendar nextStartTime = Calendar.getInstance();

		if (!timed.isEmpty()) {
			Calendar startOfDay = Calendar.getInstance();
			startOfDay.clear();
			previousEndTime.clear();
			nextStartTime.clear();
			
			startOfDay.set(Calendar.YEAR, yy);
			startOfDay.set(Calendar.MONTH, mm);
			startOfDay.set(Calendar.DATE, dd);

			startOfDay.set(Calendar.HOUR_OF_DAY, 0);
			startOfDay.set(Calendar.MINUTE, 0);
			startOfDay.set(Calendar.SECOND, 0);
			startOfDay.set(Calendar.MILLISECOND, 0);

			for(TimedTask task: timed) {
				if (startOfDay.getTimeInMillis() > task.getStartDate()
					.getTimeInMillis() && startOfDay.getTimeInMillis() < task.getEndDate().getTimeInMillis() ) {
					previousEndTime.setTimeInMillis(task.getEndDate().getTimeInMillis());
					break;
				} else {
					previousEndTime.setTimeInMillis(startOfDay.getTimeInMillis());
				}
			}
		}

		for (TimedTask cur : timed) {
			if (startDayMatches(dd, mm, yy, cur)
					|| endDayMatches(dd, mm, yy, cur)) {
				nextStartTime.setTimeInMillis(cur.getStartDate()
						.getTimeInMillis());

				TimedTask blankTask = getEmptyTask(previousEndTime,
						nextStartTime);

				if (previousEndTime.getTimeInMillis() < cur.getEndDate()
						.getTimeInMillis()) {
					previousEndTime.setTimeInMillis(cur.getEndDate()
							.getTimeInMillis());
				}

				if (blankTask != null) {
					s.addTask(blankTask);
				}
				s.addTask(cur);
			}
		}
		
		if(previousEndTime.get(Calendar.DATE) == dd) {
			Calendar last = Calendar.getInstance();
			last.clear();
			last.set(Calendar.DATE, dd);
			last.set(Calendar.MONTH, mm);
			last.set(Calendar.YEAR, yy);
			last.set(Calendar.HOUR, 23);
			last.set(Calendar.MINUTE, 59);
			last.set(Calendar.SECOND, 59);
			last.set(Calendar.MILLISECOND, 999);
			TimedTask lastEmptyTask = getEmptyTask(previousEndTime, last);
			s.addTask(lastEmptyTask);
		}
		
		String dateStr = String.format(DATE_FORMAT, String.valueOf(dd), String
				.valueOf(mm + MONTH_OFFSET), String.valueOf(yy));
		s.setFeedback(new Feedback(String.format(FEEDBACK_VIEW_DATE, dateStr),
				true));

		if (!s.hasDateTasks()) {
			state.setFeedback(new Feedback(String.format(
					FEEDBACK_VIEW_DATE_NOT_FOUND, dateStr), false));
			return state;
		}

		return s;
	}

	/**
	 * Routine to check whether the date equality with a deadline task
	 * 
	 * @param dd
	 *            Date viewed
	 * @param mm
	 *            Month viewed
	 * @param yy
	 *            Year viewed
	 * @param cur
	 *            Current Deadline task
	 * @return whether the view date matches a deadline
	 */
	private boolean isSameDate(int dd, int mm, int yy, DeadlineTask cur) {
		return dd == cur.getDeadline().get(Calendar.DATE)
				&& mm == cur.getDeadline().get(Calendar.MONTH)
				&& yy == cur.getDeadline().get(Calendar.YEAR);
	}

	/**
	 * Routine that creates and return an empty timed task to be added to
	 * current state
	 * 
	 * @param previousEndTime
	 *            The end time of the previous timed task
	 * @param nextStartTime
	 *            The start time of the next timed task
	 * @return a new blank timed task if possible, else null
	 */
	private TimedTask getEmptyTask(Calendar previousEndTime,
			Calendar nextStartTime) {
		if (previousEndTime.getTimeInMillis() >= nextStartTime
				.getTimeInMillis())
			return null;

		Calendar start = Calendar.getInstance();
		start.clear();
		start.setTimeInMillis(previousEndTime.getTimeInMillis());

		Calendar end = Calendar.getInstance();
		end.clear();
		end.setTimeInMillis(nextStartTime.getTimeInMillis());

		TimedTask blankTask = new TimedTask(TASK_DESCRIPTION_EMPTY,
				new ArrayList<String>(), start, end);
		return blankTask;
	}

	/**
	 * Checks whether the ending Day for a timed task is the same as what need
	 * to be viewed
	 * 
	 * @param dd
	 *            Date Viewed
	 * @param mm
	 *            Month Viewed
	 * @param yy
	 *            Year Viewed
	 * @param cur
	 *            Current Timed Task
	 * @return equality of end date
	 */
	private boolean endDayMatches(int dd, int mm, int yy, TimedTask cur) {
		return dd == cur.getEndDate().get(Calendar.DATE)
				&& mm == cur.getEndDate().get(Calendar.MONTH)
				&& yy == cur.getEndDate().get(Calendar.YEAR);
	}

	/**
	 * Checks whether the starting Day for a timed task is the same as what
	 * needs to be viewed
	 * 
	 * @param dd
	 *            Date viewed
	 * @param mm
	 *            Month viewed
	 * @param yy
	 *            Year viewed
	 * @param cur
	 *            Current Timed Task
	 * @return equality of start date
	 */
	private boolean startDayMatches(int dd, int mm, int yy, TimedTask cur) {
		return dd == cur.getStartDate().get(Calendar.DATE)
				&& mm == cur.getStartDate().get(Calendar.MONTH)
				&& yy == cur.getStartDate().get(Calendar.YEAR);
	}

	/**
	 * Executes view tag command
	 * 
	 * @param state
	 *            current state of program
	 * @return state after executing command
	 * 
	 * @author A0097199H
	 */
	private State executeViewTag(State state) {
		State s = new State();
		ArrayList<Task> tasks = state.getAllTasks();
		for (Task t : tasks) {
			if (t.hasTag(tag)) {
				s.addTask(t);
			}
		}
		s.setFeedback(new Feedback(String.format(FEEDBACK_VIEW_TAG, tag), true));
		return s;
	}

	/**
	 * Executes view expired command
	 * 
	 * @param state
	 *            state of current program
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
	 *            state of current program
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

	/**
	 * Executes view interval command
	 * 
	 * @param state
	 *            current state of program
	 * @return state after executing command
	 */
	private State executeViewInterval(State state) {
		State s = new State();
		TreeSet<DeadlineTask> deadline = state.getDeadlineTasks();
		TreeSet<TimedTask> timed = state.getTimedTasks();
		for (DeadlineTask cur : deadline) {
			if (startDate.compareTo(cur.getDeadline()) <= 0
					&& endDate.compareTo(cur.getDeadline()) >= 0) {
				s.addTask(cur);
			}
		}

		for (TimedTask cur : timed) {
			if ((startDate.compareTo(cur.getStartDate()) <= 0 && endDate
					.compareTo(cur.getStartDate()) >= 0)
					|| (startDate.compareTo(cur.getEndDate()) <= 0 && endDate
							.compareTo(cur.getEndDate()) >= 0)) {
				s.addTask(cur);
			}
		}

		String startDateStr = String.format(DATE_FORMAT, String
				.valueOf(startDate.get(Calendar.DATE)), String
				.valueOf(startDate.get(Calendar.MONTH) + MONTH_OFFSET), String
				.valueOf(startDate.get(Calendar.YEAR)));

		String endDateStr = String.format(DATE_FORMAT, String.valueOf(endDate
				.get(Calendar.DATE)), String.valueOf(endDate
				.get(Calendar.MONTH)
				+ MONTH_OFFSET), String.valueOf(endDate.get(Calendar.YEAR)));

		s.setFeedback(new Feedback(String.format(FEEDBACK_VIEW_INTERVAL,
				startDateStr, endDateStr), true));

		return s;
	}

	/**
	 * Sets the time parameters of a Calendar object
	 * 
	 * @param calObj
	 *            Calendar object which time is being set
	 * @param h
	 *            hour to set to (0 to 23)
	 * @param m
	 *            minute to set to (0 to 59)
	 * @param s
	 *            second to set to (0 to 59)
	 * @param ms
	 *            milliseconds to set to (0 to 999)
	 */
	private void setTime(Calendar calObj, int h, int m, int s, int ms) {
		calObj.set(Calendar.HOUR_OF_DAY, h);
		calObj.set(Calendar.MINUTE, m);
		calObj.set(Calendar.SECOND, s);
		calObj.set(Calendar.MILLISECOND, ms);
	}
}