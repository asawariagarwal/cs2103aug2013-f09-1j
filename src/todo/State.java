package todo;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * This class helps maintain a state of the application, containing all
 * neccessary data and methods to manipulate them
 * 
 * @author A0098219
 * 
 */

public class State {

	/**
	 * Stores the feedback to be displayed
	 */
	private Feedback _feedback;
	/**
	 * ArrayLists to store the three types of tasks
	 */

	private TreeSet<TimedTask> _currentTimedTasks;
	private TreeSet<FloatingTask> _currentFloatingTasks;
	private TreeSet<DeadlineTask> _currentDeadlineTasks;

	// Stores pointers to previous and next states for undo/redo
	private State _previous;
	private State _next;

	State() {
		_currentTimedTasks = new TreeSet<TimedTask>();
		_currentFloatingTasks = new TreeSet<FloatingTask>();
		_currentDeadlineTasks = new TreeSet<DeadlineTask>();
		_feedback = new Feedback();
		_previous = null;
		_next = null;
	}

	/**
	 * Initialize a new State with the context of an earlier state
	 * 
	 * @param previous
	 */
	State(State previous) {
		this();
		_currentTimedTasks = new TreeSet<TimedTask>(previous.getTimedTasks());
		_currentDeadlineTasks = new TreeSet<DeadlineTask>(previous
				.getDeadlineTasks());
		_currentFloatingTasks = new TreeSet<FloatingTask>(previous
				.getFloatingTasks());
		_feedback = new Feedback(previous.getFeedback());
		_previous = previous.getPrevious();
		_next = previous.getNext();
	}

	/**
	 * Routine to get a list of all timed tasks
	 * 
	 * @return an ArrayList with all timed tasks
	 */
	protected TreeSet<TimedTask> getTimedTasks() {
		return _currentTimedTasks;
	}

	/**
	 * Routine to get a list of all floating tasks
	 * 
	 * @return an ArrayList with all floating tasks
	 */
	protected TreeSet<FloatingTask> getFloatingTasks() {
		return _currentFloatingTasks;
	}

	/**
	 * Routine to get a list of all deadline tasks
	 * 
	 * @return an ArrayList with all deadline tasks
	 */
	protected TreeSet<DeadlineTask> getDeadlineTasks() {
		return _currentDeadlineTasks;
	}

	/**
	 * Routine to get a list of all tasks
	 * 
	 * @return an arraylist containing all tasks
	 */
	protected ArrayList<Task> getAllTasks() {
		ArrayList<Task> allTasks = new ArrayList<Task>();

		allTasks.addAll(getFloatingTasks());
		allTasks.addAll(getTimedTasks());
		allTasks.addAll(getDeadlineTasks());

		return allTasks;
	}

	/**
	 * Routine to add a generic task object
	 * 
	 * @param task
	 *            task to be added
	 */
	protected void addTask(Task task) {
		if (task instanceof FloatingTask) {
			addTask((FloatingTask) (task));
		} else if (task instanceof TimedTask) {
			addTask((TimedTask) (task));
		} else if (task instanceof DeadlineTask) {
			addTask((DeadlineTask) (task));
		}
	}

	/**
	 * Routine to add a task to the timed tasks list
	 * 
	 * @param task
	 *            Timed task to be added
	 */
	private void addTask(TimedTask task) {
		_currentTimedTasks.add(task);
	}

	/**
	 * Routine to add a task to the floating tasks list
	 * 
	 * @param task
	 *            floating task to be added
	 */
	private void addTask(FloatingTask task) {
		_currentFloatingTasks.add(task);
	}

	/**
	 * Routine to add a task to the deadline tasks list
	 * 
	 * @param task
	 *            deadline task to be added
	 */
	private void addTask(DeadlineTask task) {
		_currentDeadlineTasks.add(task);
	}

	/**
	 * Routine to check whether a particular string is in the description of any
	 * task
	 * 
	 * @param taskSnippet
	 *            String segment to search for
	 * 
	 * @return a boolean signifying whether String was found
	 */
	protected boolean hasTask(String taskSnippet) {
		if (hasTask(taskSnippet, getTimedTasks())) {
			return true;
		}
		if (hasTask(taskSnippet, getDeadlineTasks())) {
			return true;
		}
		if (hasTask(taskSnippet, getFloatingTasks())) {
			return true;
		}
		return false;
	}

	/**
	 * Routine to check whether a particular string is in the description of any
	 * task
	 * 
	 * @param taskSnippet
	 * @param taskList
	 * @return
	 */
	protected boolean hasTask(String taskSnippet,
			TreeSet<? extends Task> taskList) {
		for (Task task : taskList) {
			if (task.getTaskDescription().contains(taskSnippet)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Routine to remove a particular task from timed tasks
	 * 
	 * @param task
	 *            Task to be removed
	 */
	private void removeTask(TimedTask task) {
		_currentTimedTasks.remove(task);
	}

	/**
	 * Routine to remove a particular task from floating tasks
	 * 
	 * @param task
	 *            Task to be removed
	 */
	private void removeTask(FloatingTask task) {
		_currentFloatingTasks.remove(task);
	}

	/**
	 * Routine to remove a particular task from deadline tasks
	 * 
	 * @param task
	 *            Task to be removed
	 */
	private void removeTask(DeadlineTask task) {
		_currentDeadlineTasks.remove(task);
	}

	/**
	 * Routine to remove generic task object
	 * 
	 * @param task
	 *            task object to be removed
	 */
	protected void removeTask(Task task) {
		if (task instanceof FloatingTask) {
			removeTask((FloatingTask) (task));
		} else if (task instanceof TimedTask) {
			removeTask((TimedTask) (task));
		} else if (task instanceof DeadlineTask) {
			removeTask((DeadlineTask) (task));
		}
	}

	/**
	 * Routine to get all tasks containing a given snippet
	 * 
	 * @param taskSnippet
	 *            Task snippet contained
	 * @return list of all tasks with a given snippet
	 */
	protected ArrayList<Task> getTasks(String taskSnippet) {
		ArrayList<Task> requiredTasks = new ArrayList<Task>();

		ArrayList<Task> allTasks = getAllTasks();

		for (Task task : allTasks) {
			if (task.getTaskDescription().contains(taskSnippet)) {
				requiredTasks.add(task);
			}
		}

		return requiredTasks;
	}

	/**
	 * Routine to set the feedback to a string
	 * 
	 * @param feedback
	 *            feedback to be set
	 */
	protected void setFeedback(Feedback feedback) {
		_feedback = feedback;
	}

	/**
	 * Routine to get the feedback to be displayed
	 * 
	 * @return the feedback currently set
	 */
	protected Feedback getFeedback() {
		return _feedback;
	}

	/**
	 * Routine to check whether a task with a date/time is present in the state
	 * 
	 * @return true if the current state has tasks with date/time
	 */
	protected boolean hasDateTasks() {
		boolean hasDeadlineTasks = !this.getDeadlineTasks().isEmpty();
		boolean hasTimedTasks = !this.getTimedTasks().isEmpty();
		return hasDeadlineTasks || hasTimedTasks;
	}

	/**
	 * Gets previous state in state chain
	 * 
	 * @return previous state
	 */
	protected State getPrevious() {
		return _previous;
	}

	/**
	 * Sets previous state in state chain to given state
	 * 
	 * @param state
	 *            state which previous state is to be set to
	 * 
	 */
	protected void setPrevious(State state) {
		_previous = state;
	}

	/**
	 * Checks if there is a previous state in state chain
	 * 
	 * @return true if there is a previous state, false otherwise
	 */
	protected boolean hasPrevious() {
		return _previous != null;
	}

	/**
	 * Get next state in state chain
	 * 
	 * @return next state
	 */
	protected State getNext() {
		return _next;
	}

	/**
	 * Set next state in state chain to given state
	 * 
	 * @param state
	 *            state which next state is to be set to
	 */
	protected void setNext(State state) {
		_next = state;
	}

	/**
	 * Checks if there is next state in state chain
	 * 
	 * @return true if there is a next state, false otherwise
	 */
	protected boolean hasNext() {
		return _next != null;
	}
}
