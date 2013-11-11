package todo;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * This class encapsulates the Tasks of particular types and provides routines
 * to manipulate them.
 * 
 * Subclasses : FloatingTasks, DeadlineTasks, TimedTasks
 * 
 * @author A0098219
 * 
 */
class Task {
	/**
	 * Stores the task descriptions and user defined tags as well as expiry as
	 * private members
	 */
	private String _description;
	private TreeSet<String> _tags;
	private boolean _expired;
	private boolean _done;

	/**
	 * Default constructor
	 */
	Task() {
		_description = "";
		_tags = new TreeSet<String>();
		_expired = false;
		_done = false;
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
		_tags = new TreeSet<String>(tags);;
		_expired = false;
		_done = false;
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
	Task(String description, TreeSet<String> tags) {
		_description = description;
		_tags = tags;
		_expired = false;
		_done = false;
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
	protected TreeSet<String> getTags() {
		return _tags;
	}

	/**
	 * Routine to get the tag list as a String
	 * 
	 * @return the tag list as a String
	 */
	protected String getTagString() {
		String tagString = "";
		for (String tag : getTags()) {
			tagString += "#" + tag + " ";
		}
		return tagString;
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

	/**
	 * Marks a task as done
	 */
	protected void markAsDone() {
		_done = true;
	}

	/**
	 * Marks a task as not done
	 */
	protected void markAsPending() {
		_done = false;
	}

	/**
	 * Gets the completion status of a task
	 */
	protected boolean isComplete() {
		return _done;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected Task clone() {
		String desc = this.getTaskDescription();
		ArrayList<String> tags = new ArrayList<String>(this.getTags());
		Task result = new Task(desc, tags);
		if (this.isComplete()) {
			result.markAsDone();
		}
		if (this.isExpired()) {
			result.expire();
		}
		return result;
	}
}
