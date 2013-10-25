package todo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Subclass to encapsulate view commands
 * 
 * @author Eugene
 * 
 */
public class ViewCommand extends Command {
	private Calendar date;
	private String tag;
	private boolean isOrderedByTags = false;
	private boolean isFloating = false;
	
	/**
	 * Empty constructor for ViewCommand
	 * 
	 * Represents a view all command
	 * 
	 */
	ViewCommand() {
		super(false);
	}
	
	/**
	 * Constructor for ViewCommand
	 * 
	 * @param floating
	 * 			true if ViewCommand is a view floating command
	 * 
	 * Represents a view floating command, if floating is true
	 * If not, it is a view all command
	 * 
	 */
	ViewCommand(boolean floating) {
		this();
		isFloating = floating;
	}
	
	/**
	 * Constructor for ViewCommand
	 * 
	 * @param floating
	 * 			true if ViewCommand is a view floating command
	 * 
	 * @param orderByTag
	 * 			true if ViewCommand is ordered by tags
	 * 
	 * Represents a view floating command, if floating is true
	 * If not, it is a view all command
	 * View is ordered by tags if orderByTag is true.
	 * 
	 */
	ViewCommand(boolean floating, boolean orderByTag) {
		this();
		isFloating = floating;
		isOrderedByTags = orderByTag;
	}
	
	/**
	 * Constructor for ViewCommand
	 * 
	 * @param date
	 * 			date to view tasks by
	 * 
	 * If date is null, ViewCommand will be a view all command
	 * 
	 */
	ViewCommand(Calendar date) {
		this();
		this.date = date;
	}
	
	/**
	 * Constructor for ViewCommand
	 * 
	 * @param tag
	 * 			tag to view tasks by
	 * 
	 * If tag is null, ViewCommand will be a view all command
	 * 
	 */
	ViewCommand(String tag) {
		this();
		this.tag = tag;
	}

	@Override
	protected boolean isValid(State state) {
		return (isViewAll() ||
				isViewFloating() ||
				isViewOrderedByTags() ||
				isViewDate() ||
				isViewTag());
	}
	
	/**
	 * Checks if command is a "view all" command
	 * 
	 */
	private boolean isViewAll() {
		return (date == null && tag == null && !isOrderedByTags && !isFloating);
	}
	
	/**
	 * Checks if command is a "view floating" command
	 * 
	 */
	private boolean isViewFloating() {
		return (date == null && tag == null && !isOrderedByTags && isFloating);
	}
	
	/**
	 * Checks if command is a "view ordered by tags" command
	 * 
	 */
	private boolean isViewOrderedByTags() {
		return (date == null && tag == null && isOrderedByTags);
	}
	
	/**
	 * Checks if command is a "view date" command
	 * 
	 */
	private boolean isViewDate() {
		return (date != null && tag == null && !isOrderedByTags && !isFloating);
	}
	
	/**
	 * Checks if command is a "view tags" command
	 * 
	 */
	private boolean isViewTag() {
		return (date == null && tag != null && !isOrderedByTags && !isFloating);
	}

	@Override
	protected State execute(State state) {
		State s = new State(state);
		if (isViewAll()) {
			s.setFeedback("viewing all tasks");
			return s;
		}
		if (isViewFloating()) {
			s.getDeadlineTasks().clear();
			s.getTimedTasks().clear();
			s.setFeedback("viewing floating tasks");
			return s;
		}
		if (isViewOrderedByTags()) {
			// TODO How to represent a view ordered by tags??
		}
		if (isViewDate()) {
			s = new State();
			int dd = date.get(Calendar.DATE);
			int mm = date.get(Calendar.MONTH);
			int yy = date.get(Calendar.YEAR);
			ArrayList<DeadlineTask> deadline = state.getDeadlineTasks();
			ArrayList<TimedTask> timed = state.getTimedTasks();
			for (int i=0; i<deadline.size(); i++) {
				DeadlineTask cur = deadline.get(i);
				if (dd == cur.getDeadline().get(Calendar.DATE) &&
					mm == cur.getDeadline().get(Calendar.MONTH) &&
					yy == cur.getDeadline().get(Calendar.YEAR)) {
					s.addTask(cur);
				}
			}
			for (int i=0; i<timed.size(); i++) {
				TimedTask cur = timed.get(i);
				if (date.compareTo(cur.getStartDate()) >= 0 && date.compareTo(cur.getEndDate()) <= 0) {
					s.addTask(cur);
				}
			}
			s.setFeedback("viewing tasks by date: " + String.valueOf(dd) + "/" + String.valueOf(mm) + "/" + String.valueOf(yy));
			return s;
		}
		if (isViewTag()) {
			s = new State();
			ArrayList<DeadlineTask> deadline = state.getDeadlineTasks();
			ArrayList<TimedTask> timed = state.getTimedTasks();
			ArrayList<FloatingTask> floating = state.getFloatingTasks();
			for (int i=0; i<deadline.size(); i++) {
				DeadlineTask cur = deadline.get(i);
				if (cur.hasTag(tag)) {
					s.addTask(cur);
				}
			}
			for (int i=0; i<timed.size(); i++) {
				TimedTask cur = timed.get(i);
				if (cur.hasTag(tag)) {
					s.addTask(cur);
				}
			}
			for (int i=0; i<floating.size(); i++) {
				FloatingTask cur = floating.get(i);
				if (cur.hasTag(tag)) {
					s.addTask(cur);
				}
			}
			s.setFeedback("viewing tasks by tag: " + tag);
			return s;
		}
		return null;
	}
}