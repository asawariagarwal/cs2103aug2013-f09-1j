package todo;

import java.util.ArrayList;

/**
 * This class encapsulates the Tasks of particular types and provides routines
 * to manipulate them.
 * 
 * Subclasses : FloatingTasks, DeadlineTasks, TimedTasks
 * 
 * @author Karan
 * 
 */
class Task {
	/**
	 * Stores the task descriptions and user defined tags as well as expiry as
	 * private members
	 */
	private String _description;
	private ArrayList<String> _tags;
	private boolean _expired;

	/**
	 * Default constructor
	 */
	Task() {
		_description = "";
		_tags = new ArrayList<String>();
		_expired = false;
	}

	/**
	 * Constructor for Tasks
	 * 
	 * @param description
	 *            Description of task
	 * 
	 * @param tags
	 *            Task tags
	 */
	Task(String description, ArrayList<String> tags) {
		_description = description;
		_tags = tags;
		_expired = false;
	}

	/**
	 * Routine to set task description
	 * 
	 * @param description
	 *            Task description to be set
	 */
	protected void setTaskDescription(String description) {
		_description = description;
	}

	/**
	 * Routine to get Task description
	 * 
	 * @return task description as a string
	 */
	protected String getTaskDescription() {
		return _description;
	}

	/**
	 * Routine to add a tag to a task
	 * 
	 * @param tag
	 *            tag to be added
	 */
	protected void addTag(String tag) {
		if (!hasTag(tag)) {
			_tags.add(tag);
		}
	}

	/**
	 * Routine to check whether a Task has a particular tag
	 * 
	 * @param tag
	 *            tag to check for
	 * 
	 * @return a boolean for whether a tag was found in this Task
	 */
	protected boolean hasTag(String tag) {
		if (_tags.contains(tag)) {
			return true;
		}
		return false;
	}

	/**
	 * Routine to remove a tag from a Task
	 * 
	 * @param tag
	 *            tag to be removed
	 */
	protected void removeTag(String tag) {
		if (hasTag(tag)) {
			_tags.remove(tag);
		}
	}

	/**
	 * Routine to get list of tags
	 * 
	 * @return tags list for a task
	 */
	protected ArrayList<String> getTags() {
		return _tags;
	}

	/**
	 * Routine to check expiry status
	 * 
	 * @return expiry status
	 */
	protected boolean isExpired() {
		return _expired;
	}

	/**
	 * Routine to set a task as expired
	 */
	protected void expire() {
		_expired = true;
	}

	/**
	 * Routine to unmark an expired task
	 */
	protected void renew() {
		_expired = false;
	}
}
