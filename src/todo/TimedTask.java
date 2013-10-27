 package todo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Subclass to encapsulate Timed Tasks
 * 
 * @author Karan
 * 
 */
class TimedTask extends Task implements Comparable<TimedTask> {
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
	TimedTask(String description, ArrayList<String> tags, Calendar startDate,
			Calendar endDate) {
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

	/**
	 * Routine that returns a timed task as a String
	 */
	@Override
	public String toString() {
		String taskString = "";
		String timedTaskFormat = "hh:mm aa 'on' EEEEEEEEE ',' dd MMMMMMMMM, yyyy ";
		SimpleDateFormat sdf = new SimpleDateFormat(timedTaskFormat);
		taskString = getTaskDescription() + " from "
				+ sdf.format(getStartDate().getTime()) + "to "
				+ sdf.format(getEndDate().getTime());

		for (String tag : getTags()) {
			taskString += (" #" + tag);
		}

		return taskString;
	}

	@Override
	public int compareTo(TimedTask otherTask) {
		if (this.getStartDate().before(otherTask.getStartDate())){
			return -1;
		} else if (this.getStartDate().after(otherTask.getStartDate())){
			return 1;
		} else {
			if (this.getEndDate().before(otherTask.getEndDate())){
				return -1;
			} else if (this.getEndDate().after(otherTask.getEndDate())){
				return 1;
			} else {
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
		}
	}
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof TimedTask)){
			return false;
		} else {
			TimedTask timedO = (TimedTask) o;
			boolean sameStart = this.getStartDate().equals(timedO.getStartDate());
			boolean sameEnd = this.getEndDate().equals(timedO.getEndDate());
			boolean sameName = this.getTaskDescription().equals(timedO.getTaskDescription());
			boolean sameTags = this.getTags().equals(timedO.getTags());
			return sameStart && sameEnd && sameName && sameTags;
		}
	}
	
	@Override
	public int hashCode(){
		String startString = this.getStartDate().toString();
		String endString = this.getEndDate().toString();
		String nameString = this.getTaskDescription();
		String tagString = this.getTags().toString();
		return (startString + endString + nameString + tagString).hashCode();
	}
}