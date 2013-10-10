package todo;

import java.util.ArrayList;

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

	/**
	 * Routine to get floating task as a String
	 */
	public String toString() {
		String taskString = "";

		for (String tag : getTags()) {
			taskString += " #" + tag;
		}
		taskString = getTaskDescription() + taskString;

		return taskString;
	}
}