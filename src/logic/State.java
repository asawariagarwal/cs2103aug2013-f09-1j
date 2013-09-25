package logic;

import java.util.ArrayList;
import java.util.Collections;

import logic.Task.*;

public class State {

	private ArrayList<TimedTask> _currentTimedTasks;
	private ArrayList<FloatingTask> _currentFloatingTasks;
	private ArrayList<DeadlineTask> _currentDeadlineTasks;

	State() {
		_currentTimedTasks = new ArrayList<TimedTask>();
		_currentFloatingTasks = new ArrayList<FloatingTask>();
		_currentDeadlineTasks = new ArrayList<DeadlineTask>();
	}

	State(State previous) {
		this();
		Collections.copy(_currentTimedTasks, previous.getTimedTasks());
		Collections.copy(_currentDeadlineTasks, previous.getDeadlineTasks());
		Collections.copy(_currentFloatingTasks, previous.getFloatingTasks());
	}

	protected ArrayList<TimedTask> getTimedTasks() {
		return _currentTimedTasks;
	}

	protected ArrayList<FloatingTask> getFloatingTasks() {
		return _currentFloatingTasks;
	}

	protected ArrayList<DeadlineTask> getDeadlineTasks() {
		return _currentDeadlineTasks;
	}

	protected ArrayList<Task> getAllTasks() {
		ArrayList<Task> allTasks = new ArrayList<Task>();

		allTasks.addAll(getFloatingTasks());
		allTasks.addAll(getTimedTasks());
		allTasks.addAll(getDeadlineTasks());

		return allTasks;
	}

	protected void addTask(TimedTask task) {
		_currentTimedTasks.add(task);
	}

	protected void addTask(FloatingTask task) {
		_currentFloatingTasks.add(task);
	}

	protected void addTask(DeadlineTask task) {
		_currentDeadlineTasks.add(task);
	}

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

	protected boolean hasTask(String taskSnippet,
			ArrayList<? extends Task> taskList) {
		for (Task task : taskList) {
			if (task.getTaskDescription().contains(taskSnippet)) {
				return true;
			}
		}
		return false;
	}

	protected void removeTask(TimedTask task) {
		_currentTimedTasks.remove(task);
	}

	protected void removeTask(FloatingTask task) {
		_currentFloatingTasks.remove(task);
	}

	protected void removeTask(DeadlineTask task) {
		_currentDeadlineTasks.remove(task);
	}


	protected ArrayList<Task> getTasks(String taskSnippet) {
		ArrayList<Task> requiredTasks = new ArrayList<Task>();
		
		ArrayList<Task> allTasks = getAllTasks();
		
		for(Task task : allTasks) {
			if(task.getTaskDescription().contains(taskSnippet)) {
				requiredTasks.add(task);
			}
		}
		
		return requiredTasks;
	}

}
