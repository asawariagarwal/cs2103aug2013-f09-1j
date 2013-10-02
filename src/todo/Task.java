package todo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class encapsulates the Tasks of particular types and provides routines
 * to manipulate them.
 * 
 * Subclasses : FloatingTasks, DeadlineTasks, TimedTasks
 * 
 * @author Karan
 * 
 */
public class Task {
	/**
	 * Stores the task descriptions and user defined tags as private members
	 */
	private String _description;
	private ArrayList<String> _tags;

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
	 * Subclass to encapsulate floating tasks
	 * 
	 * @author Karan
	 * 
	 */
	class FloatingTask extends Task {
		/**
		 * Constructor to set up FloatingTasks object
		 * 
		 * @param description
		 *            Task description
		 * @param tags
		 *            tags for this task
		 */
		FloatingTask(String description, ArrayList<String> tags) {
			super(description, tags);
		}
	}

	/**
	 * Subclass to encapsulate Deadline based tasks
	 * 
	 * @author Karan
	 * 
	 */
	class DeadlineTask extends Task {
		/**
		 * Variable to store the deadline
		 */
		private Calendar _deadline;

		/**
		 * Constructor to set up a deadline task
		 * 
		 * @param description
		 *            Task description
		 * @param tags
		 *            Task tags
		 * @param deadline
		 *            Task deadline
		 */
		DeadlineTask(String description, ArrayList<String> tags,
				Calendar deadline) {
			super(description, tags);
			_deadline = deadline;
		}

		/**
		 * Routine to set Task deadline
		 * 
		 * @param deadline
		 *            deadline to be set
		 */
		protected void setDeadline(Calendar deadline) {
			_deadline = deadline;
		}

		/**
		 * Routine to get the Task deadline
		 * 
		 * @return the Task deadline as a Calendar object
		 */
		protected Calendar getDeadline() {
			return _deadline;
		}
	}

	/**
	 * Subclass to encapsulate Timed Tasks
	 * 
	 * @author Karan
	 * 
	 */
	class TimedTask extends Task {
		/**
		 * Variables to store start and end times of tasks as Calendar objects
		 */
		private Calendar _startDate;
		private Calendar _endDate;

		/**
		 * Constructor to set up TimedTasks
		 * 
		 * @param description
		 *            Task description
		 * @param tags
		 *            Task tags
		 * @param startDate
		 *            Task start Calendar object
		 * @param endDate
		 *            Task end Calendar object
		 */
		TimedTask(String description, ArrayList<String> tags,
				Calendar startDate, Calendar endDate) {
			super(description, tags);
			_startDate = startDate;
			_endDate = endDate;
		}

		/**
		 * Routine to set the start date of a Task
		 * 
		 * @param startDate
		 *            start date to be set
		 */
		protected void setStartDate(Calendar startDate) {
			_startDate = startDate;
		}

		/**
		 * Routine to set the end date of a Task
		 * 
		 * @param endDate
		 *            end date to be set
		 */
		protected void setEndDate(Calendar endDate) {
			_endDate = endDate;
		}

		/**
		 * Routine to get the start date of a Task
		 * 
		 * @return the start date as a Calendar object
		 */
		protected Calendar getStartDate() {
			return _startDate;
		}

		/**
		 * Routine to get the end date of a Task
		 * 
		 * @return the end date as a Calendar object
		 */
		protected Calendar getEndDate() {
			return _endDate;
		}
	}
}
