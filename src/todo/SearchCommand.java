package todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.*;

/**
 * Subclass to encapsulate search commands
 * 
 * @author Eugene
 * 
 */
public class SearchCommand extends Command {
	private static final String FEEDBACK_NONE = "no tasks found";
	private static final String FEEDBACK_FOUND = "search results found";
	
	private static final String LOG_MESSAGE = "executing search";
	private static final String LOG_NONE = "no tasks found";
	private static final String LOG_FOUND = "tasks found";
	
	private ArrayList<String> includedWords;
	private ArrayList<String> excludedWords;
	private ArrayList<String> includedTags;
	private ArrayList<String> excludedTags;
	private Calendar date;
	private Calendar startDate;
	private Calendar endDate;
	
	/**
	 * Constructor for SearchCommand
	 * 
	 * @param includedWords
	 * 				words to be included in results
	 *
	 * @param excludedWords
	 * 				words to be excluded in results
	 * 
	 * @param includedTags
	 * 				tags to be included in results
	 * 
	 * @param excludedTags
	 * 				tags to be excluded in results
	 * 
	 */
	SearchCommand(ArrayList<String> includedWords, ArrayList<String> excludedWords,
			ArrayList<String> includedTags, ArrayList<String> excludedTags) {
		super(false);
		this.includedWords = includedWords;
		this.excludedWords = excludedWords;
		this.includedTags = includedTags;
		this.excludedTags = excludedTags;
	}

	/**
	 * Constructor for SearchCommand
	 * 
	 * @param includedWords
	 * 				words to be included in results
	 *
	 * @param excludedWords
	 * 				words to be excluded in results
	 * 
	 * @param includedTags
	 * 				tags to be included in results
	 * 
	 * @param excludedTags
	 * 				tags to be excluded in results
	 * 
	 * @param date
	 * 				date which tasks occur
	 * 
	 */
	SearchCommand(ArrayList<String> includedWords, ArrayList<String> excludedWords,
			ArrayList<String> includedTags, ArrayList<String> excludedTags,
			Calendar date) {
		super(false);
		this.includedWords = includedWords;
		this.excludedWords = excludedWords;
		this.includedTags = includedTags;
		this.excludedTags = excludedTags;
		this.date = date;
		setTime(this.date, 0, 0, 0, 0);
	}
	
	/**
	 * Constructor for SearchCommand
	 * 
	 * @param includedWords
	 * 				words to be included in results
	 *
	 * @param excludedWords
	 * 				words to be excluded in results
	 * 
	 * @param includedTags
	 * 				tags to be included in results
	 * 
	 * @param excludedTags
	 * 				tags to be excluded in results
	 * 
	 * @param startDate
	 * 				start date of interval which tasks occur in
	 * 
	 * @param endDate
	 * 				end date of interval which tasks occur in
	 * 
	 */
	SearchCommand(ArrayList<String> includedWords, ArrayList<String> excludedWords,
			ArrayList<String> includedTags, ArrayList<String> excludedTags,
			Calendar startDate, Calendar endDate) {
		super(false);
		this.includedWords = includedWords;
		this.excludedWords = excludedWords;
		this.includedTags = includedTags;
		this.excludedTags = excludedTags;
		this.startDate = startDate;
		this.endDate = endDate;
		setTime(this.startDate, 0, 0, 0, 0);
		setTime(this.endDate, 23, 59, 59, 999);
	}
	
	@Override
	protected boolean isValid() {
		return isSearchWithoutDate() || isSearchDate() || isSearchInterval();
	}
	
	/**
	 * Checks if search is valid is without dates
	 * 
	 * @return true if search is valid without dates
	 */
	private boolean isSearchWithoutDate() {
		return includedWords != null &&
				excludedWords != null &&
				includedTags != null &&
				excludedTags != null;
	}
	
	/**
	 * Checks if search is valid is with single dates
	 * 
	 * @return true if search is valid with single date
	 */
	private boolean isSearchDate() {
		return includedWords != null &&
				excludedWords != null &&
				includedTags != null &&
				excludedTags != null &&
				date != null;
	}
	
	/**
	 * Checks if search is valid is with start and end dates
	 * 
	 * @return true if search is valid with start and end dates
	 */
	private boolean isSearchInterval() {
		return includedWords != null &&
				excludedWords != null &&
				includedTags != null &&
				excludedTags != null &&
				startDate != null &&
				endDate != null &&
				startDate.before(endDate);
	}

	@Override
	protected State execute(State state) throws Exception {
		assert(this.isValid());
		logger.log(Level.INFO, LOG_MESSAGE);
		
		ArrayList<Task> found = state.getAllTasks();
		removeIncludedWords(found);
		removeExcludedWords(found);
		removeIncludedTags(found);
		removeExcludedTags(found);
		
		if (isSearchDate()) {
			removeDate(found);
		} else if (isSearchInterval()) {
			removeInterval(found);
		}
		
		State s = new State();
		if (found.isEmpty()) {
			logger.log(Level.INFO, LOG_NONE);
			s.setFeedback(new Feedback(FEEDBACK_NONE, false));
			return s;
		} else {
			logger.log(Level.INFO, LOG_FOUND);
			for (Task t: found) {
				s.addTask(t);
			}
			s.setFeedback(new Feedback(FEEDBACK_FOUND, true));
			return s;
		}
	}
	
	private void removeIncludedWords(ArrayList<Task> tasks) {
		for (String word : includedWords) {
			int i = 0;
			while (i<tasks.size()) {
				Task task = tasks.get(i);
				if (task.getTaskDescription().indexOf(word) == -1) {
					tasks.remove(i);
				} else {
					i++;
				}
			}
		}
	}
	
	private void removeExcludedWords(ArrayList<Task> tasks) {
		for (String word : excludedWords) {
			int i = 0;
			while (i<tasks.size()) {
				Task task = tasks.get(i);
				if (task.getTaskDescription().indexOf(word) != -1) {
					tasks.remove(i);
				} else {
					i++;
				}
			}
		}
	}
	
	private void removeIncludedTags(ArrayList<Task> tasks) {
		for (String tag : includedTags) {
			int i = 0;
			while (i<tasks.size()) {
				Task task = tasks.get(i);
				if (!task.hasTag(tag)) {
					tasks.remove(i);
				} else {
					i++;
				}
			}
		}
	}
	
	private void removeExcludedTags(ArrayList<Task> tasks) {
		for (String tag : excludedTags) {
			int i = 0;
			while (i<tasks.size()) {
				Task task = tasks.get(i);
				if (task.hasTag(tag)) {
					tasks.remove(i);
				} else {
					i++;
				}
			}
		}
	}
	
	private void removeDate(ArrayList<Task> tasks) {
		int i = 0;
		while (i<tasks.size()) {
			Task task = tasks.get(i);
			if (task instanceof DeadlineTask) {
				DeadlineTask deadline = (DeadlineTask) task;
				Calendar tempDate = Calendar.getInstance();
				tempDate.setTimeInMillis(deadline.getDeadline().getTimeInMillis());
				setTime(tempDate, 0, 0, 0, 0);
				if (tempDate.getTimeInMillis() != date.getTimeInMillis()) {
					tasks.remove(i);
				} else {
					i++;
				}
			} else if (task instanceof TimedTask) {
				TimedTask timed = (TimedTask) task;
				Calendar tempStartDate = Calendar.getInstance();
				Calendar tempEndDate = Calendar.getInstance();
				tempStartDate.setTimeInMillis(timed.getStartDate().getTimeInMillis());
				setTime(tempStartDate, 0, 0, 0, 0);
				tempEndDate.setTimeInMillis(timed.getEndDate().getTimeInMillis());
				setTime(tempEndDate, 23, 59, 59, 999);
				if (tempStartDate.compareTo(date) > 0 ||
						tempEndDate.compareTo(date) < 0) {
					tasks.remove(i);
				} else {
					i++;
				}
			} else {
				i++;
			}
		}
	}
	
	private void removeInterval(ArrayList<Task> tasks) {
		int i = 0;
		while (i<tasks.size()) {
			Task task = tasks.get(i);
			if (task instanceof DeadlineTask) {
				DeadlineTask deadline = (DeadlineTask) task;
				if (startDate.compareTo(deadline.getDeadline()) > 0 ||
						endDate.compareTo(deadline.getDeadline()) < 0) {
					tasks.remove(i);
				} else {
					i++;
				}
			} else if (task instanceof TimedTask) {
				TimedTask timed = (TimedTask) task;
				if (endDate.compareTo(timed.getStartDate()) < 0 ||
						startDate.compareTo(timed.getEndDate()) > 0) {
					tasks.remove(i);
				} else {
					i++;
				}
			} else {
				i++;
			}
		}
	}
	
	/**
	 * Sets the time parameters of a Calendar object
	 * 
	 * @param calObj
	 * 			Calendar object which time is being set
	 * @param h
	 * 			hour to set to (0 to 23)
	 * @param m
	 * 			minute to set to (0 to 59)
	 * @param s
	 * 			second to set to (0 to 59)
	 * @param ms
	 * 			milliseconds to set to (0 to 999)
	 */
	private void setTime(Calendar calObj, int h, int m, int s, int ms) {
		calObj.set(Calendar.HOUR_OF_DAY, h);
		calObj.set(Calendar.MINUTE, m);
		calObj.set(Calendar.SECOND, s);
		calObj.set(Calendar.MILLISECOND, ms);
	}
}