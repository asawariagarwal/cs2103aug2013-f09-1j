package todo;

import java.util.ArrayList;

/**
 * Subclass to encapsulate floating tasks
 * 
 * @author Karan
 * 
 */
class FloatingTask extends Task implements Comparable<FloatingTask> {
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
	@Override
	public String toString() {
		String taskString = "";

		for (String tag : getTags()) {
			taskString += " #" + tag;
		}
		taskString = getTaskDescription() + taskString;

		return taskString;
	}

	@Override
	public int compareTo(FloatingTask otherTask) {
		int descCompare = this.getTaskDescription().compareTo(otherTask.getTaskDescription());
		if (descCompare != 0){
			return descCompare;
		} else {
			if (this.getTags().equals(otherTask.getTags())){
				return 0;
			} else {
				return this.getTags().toString().compareTo(otherTask.getTags().toString());
			}
		}
	}
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof FloatingTask)){
			return false;
		} else {
			FloatingTask timedO = (FloatingTask) o;
			boolean sameName = this.getTaskDescription().equals(timedO.getTaskDescription());
			boolean sameTags = this.getTags().equals(timedO.getTags());
			return sameName && sameTags;
		}
	}
	
	@Override
	public int hashCode(){
		String nameString = this.getTaskDescription();
		String tagString = this.getTags().toString();
		return (nameString + tagString).hashCode();
	}
	
	/*
	 * (non-Javadoc)
	 * @see todo.Task#clone()
	 */
	protected FloatingTask clone() {
		String desc = this.getTaskDescription();
		ArrayList<String> tags = new ArrayList<String>(this.getTags());
		FloatingTask result = new FloatingTask(desc, tags);
		if (this.isComplete()) {
			result.markAsDone();
		}
		if (this.isExpired()) {
			result.expire();
		}
		return result;
	}
}