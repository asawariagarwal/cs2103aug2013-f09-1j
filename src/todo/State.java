package todo;

import java.util.ArrayList;
import java.util.Collections;

import todo.Task.*;

/**
 * This class helps maintain the state of the application, containing all
 * neccessary data and methods to manipulate them
 * 
 * @author Karan
 * 
 */

public class State {

	/**
	 * Stores the feedback to be displayed
	 */
	private String _feedback;
	/**
	 * ArrayLists to store the three types of tasks
	 */
	private ArrayList<TimedTask> _currentTimedTasks;
	private ArrayList<FloatingTask> _currentFloatingTasks;
	private ArrayList<DeadlineTask> _currentDeadlineTasks;

	State() {
		_currentTimedTasks = new ArrayList<TimedTask>();
		_currentFloatingTasks = new ArrayList<FloatingTask>();
		_currentDeadlineTasks = new ArrayList<DeadlineTask>();
		_feedback = new String();
	}

	/**
	 * Initialize a new State with the context of an earlier state
	 * 
	 * @param previous
	 */
	State(State previous) {
		this();
		Collections.copy(_currentTimedTasks, previous.getTimedTasks());
		Collections.copy(_currentDeadlineTasks, previous.getDeadlineTasks());
		Collections.copy(_currentFloatingTasks, previous.getFloatingTasks());
	}

	/**
	 * Routine to get a list of all timed tasks
	 * 
	 * @return an ArrayList with all timed tasks
	 */
	protected ArrayList<TimedTask> getTimedTasks() {
		return _currentTimedTasks;
	}

	/**
	 * Routine to get a list of all floating tasks
	 * 
	 * @return an ArrayList with all floating tasks
	 */
	protected ArrayList<FloatingTask> getFloatingTasks() {
		return _currentFloatingTasks;
	}

	/**
	 * Routine to get a list of all deadline tasks
	 * 
	 * @return an ArrayList with all deadline tasks
	 */
	protected ArrayList<DeadlineTask> getDeadlineTasks() {
		return _currentDeadlineTasks;
	}

	/**
	 * routine to get a list of all tasks
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
	 * Routine to add a task to the timed tasks list
	 * 
	 * @param task
	 *            Timed task to be added
	 */
	protected void addTask(TimedTask task) {
		_currentTimedTasks.add(task);
	}

	/**
	 * Routine to add a task to the floating tasks list
	 * 
	 * @param task
	 *            floating task to be added
	 */
	protected void addTask(FloatingTask task) {
		_currentFloatingTasks.add(task);
	}

	/**
	 * Routine to add a task to the deadline tasks list
	 * 
	 * @param task
	 *            deadline task to be added
	 */
	protected void addTask(DeadlineTask task) {
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
			ArrayList<? extends Task> taskList) {
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
	protected void removeTask(TimedTask task) {
		_currentTimedTasks.remove(task);
	}

	/**
	 * Routine to remove a particular task from floating tasks
	 * 
	 * @param task
	 *            Task to be removed
	 */
	protected void removeTask(FloatingTask task) {
		_currentFloatingTasks.remove(task);
	}

	/**
	 * Routine to remove a particular task from deadline tasks
	 * 
	 * @param task
	 *            Task to be removed
	 */
	protected void removeTask(DeadlineTask task) {
		_currentDeadlineTasks.remove(task);
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
	protected void setFeedback(String feedback) {
		_feedback = feedback;
	}

	/**
	 * Routine to get the feedback to be displayed
	 * 
	 * @return the feedback currently set
	 */
	protected String getFeedback() {
		return _feedback;
	}
}
