package todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.*;

/**
 * Subclass to encapsulate search commands
 * 
 * @author A0097199H
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
		setStartTime(this.date);
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
		setStartTime(this.startDate);
		setEndTime(this.endDate);
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
	
	/**
	 * Remove tasks that do not contain included words
	 * 
	 * @param tasks
	 * 			list of remaining search results
	 */
	private void removeIncludedWords(ArrayList<Task> tasks) {
		if (tasks.isEmpty()) {
			return;
		}
		for (String word : includedWords) {
			removeIncludedWord(tasks, word);
		}
	}
	
	/**
	 * Checks tasks for word and remove those without word
	 * 
	 * @param tasks
	 * 			remaining search results
	 * @param word
	 * 			search keyword
	 */
	private void removeIncludedWord(ArrayList<Task> tasks, String word) {
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
	
	/**
	 * Remove tasks that contain excluded words
	 * 
	 * @param tasks
	 * 			list of remaining search results
	 */
	private void removeExcludedWords(ArrayList<Task> tasks) {
		if (tasks.isEmpty()) {
			return;
		}
		for (String word : excludedWords) {
			removeExcludedWord(tasks, word);
		}
	}
	
	/**
	 * Checks tasks for word and remove those with word
	 * 
	 * @param tasks
	 * 			remaining search results
	 * @param word
	 * 			search keyword
	 */
	private void removeExcludedWord(ArrayList<Task> tasks, String word) {
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
	
	/**
	 * Remove tasks that do not contain included tags
	 * 
	 * @param tasks
	 * 			list of remaining search results
	 */
	private void removeIncludedTags(ArrayList<Task> tasks) {
		if (tasks.isEmpty()) {
			return;
		}
		for (String tag : includedTags) {
			removeIncludedTag(tasks, tag);
		}
	}
	
	/**
	 * Checks tasks for tag and remove those without tag
	 * 
	 * @param tasks
	 * 			remaining search results
	 * @param tag
	 * 			search tag
	 */
	private void removeIncludedTag(ArrayList<Task> tasks, String tag) {
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
	
	/**
	 * Remove tasks that contain excluded tags
	 * 
	 * @param tasks
	 * 			list of remaining search results
	 */
	private void removeExcludedTags(ArrayList<Task> tasks) {
		if (tasks.isEmpty()) {
			return;
		}
		for (String tag : excludedTags) {
			removeExcludedTag(tasks, tag);
		}
	}
	
	/**
	 * Checks tasks for tag and remove those with tag
	 * 
	 * @param tasks
	 * 			remaining search results
	 * @param tag
	 * 			search tag
	 */
	private void removeExcludedTag(ArrayList<Task> tasks, String tag) {
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
	
	/**
	 * Remove tasks not on date
	 * 
	 * @param tasks
	 * 			remaining search results
	 */
	private void removeDate(ArrayList<Task> tasks) {
		int i = 0;
		while (i<tasks.size()) {
			Task task = tasks.get(i);
			boolean toRemove = false;
			if (task instanceof DeadlineTask) {
				toRemove = !isSameDate((DeadlineTask) task);
			} else if (task instanceof TimedTask) {
				toRemove = !isSameDate((TimedTask) task);
			} else {
				toRemove = false;
			}
			if (toRemove) {
				tasks.remove(i);
			} else {
				i++;
			}
		}
	}
	
	/**
	 * Checks if deadline task is same date as search date
	 * 
	 * @param task
	 * 			deadline task to be compared
	 * @return true if same date, false otherwise
	 */
	private boolean isSameDate(DeadlineTask task) {
		Calendar tempDate = Calendar.getInstance();
		tempDate.setTimeInMillis(task.getDeadline().getTimeInMillis());
		setStartTime(tempDate);
		return (tempDate.getTimeInMillis() == date.getTimeInMillis());
	}
	
	/**
	 * Checks if timed task is same date as search date
	 * 
	 * @param task
	 * 			timed task to be compared
	 * @return true if same date, false otherwise
	 */
	private boolean isSameDate(TimedTask task) {
		Calendar tempStartDate = Calendar.getInstance();
		Calendar tempEndDate = Calendar.getInstance();
		tempStartDate.setTimeInMillis(task.getStartDate().getTimeInMillis());
		setStartTime(tempStartDate);
		tempEndDate.setTimeInMillis(task.getEndDate().getTimeInMillis());
		setEndTime(tempEndDate);
		return (tempStartDate.compareTo(date) <= 0 &&
				tempEndDate.compareTo(date) >= 0);
	}
	
	/**
	 * Remove tasks not within interval
	 * 
	 * @param tasks
	 * 			remaining search results
	 */
	private void removeInterval(ArrayList<Task> tasks) {
		int i = 0;
		while (i<tasks.size()) {
			Task task = tasks.get(i);
			boolean toRemove = false;
			if (task instanceof DeadlineTask) {
				toRemove = !isWithinInterval((DeadlineTask) task);
			} else if (task instanceof TimedTask) {
				toRemove = !isWithinInterval((TimedTask) task);
			} else {
				toRemove = false;
			}
			if (toRemove) {
				tasks.remove(i);
			} else {
				i++;
			}
		}
	}
	
	/**
	 * Checks if deadline task is within search interval
	 * 
	 * @param task
	 * 			deadline task to be compared
	 * @return true if within interval, false otherwise
	 */
	private boolean isWithinInterval(DeadlineTask task) {
		return (startDate.compareTo(task.getDeadline()) <= 0 &&
				endDate.compareTo(task.getDeadline()) >= 0);
	}
	
	/**
	 * Checks if timed task is within search interval
	 * 
	 * @param task
	 * 			timed task to be compared
	 * @return true if within interval, false otherwise
	 */
	private boolean isWithinInterval(TimedTask task) {
		return (endDate.compareTo(task.getStartDate()) >= 0 &&
				startDate.compareTo(task.getEndDate()) <= 0);
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
	
	/**
	 * Sets time of calendar to 00:00:00:000
	 * 
	 * @param calObj
	 * 			calendar obj to be set
	 */
	private void setStartTime(Calendar calObj) {
		setTime(calObj, 0, 0, 0, 0);
	}
	
	/**
	 * Sets time of calendar to 23:59:59:999
	 * 
	 * @param calObj
	 * 			calendar obj to be set
	 */
	private void setEndTime(Calendar calObj) {
		setTime(calObj, 23, 59, 59, 999);
	}
}