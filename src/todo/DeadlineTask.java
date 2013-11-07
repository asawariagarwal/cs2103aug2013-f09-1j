package todo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
	 * Subclass to encapsulate Deadline based tasks
	 * 
	 * @author Karan
	 * 
	 */
	class DeadlineTask extends Task implements Comparable<DeadlineTask> {
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
		 * Default Constructor
		 */
		public DeadlineTask() {
			super();
			_deadline = Calendar.getInstance();
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
		
		/**
		 * Routine to get the deadline as a String
		 * 
		 * @return the deadline as a String
		 */
		protected String getDeadlineString() {
			String deadlineString = "";
			String timeFormat = "EEEEEEEEE',' dd MMMMMMMMM, yyyy ";

			SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);

			deadlineString = sdf.format(getDeadline().getTime());
			return deadlineString;
		}
		
		/* (non-Javadoc)
		 * @see todo.Task#isExpired()
		 */
		protected boolean isExpired() {
			Calendar currentTime  = Calendar.getInstance();
			
			if(currentTime.getTimeInMillis() > this.getDeadline().getTimeInMillis()) {
				return true;
			}
			
			return false;
		}
		/**
		 * Routine to return the deadline task as a String
		 */
		@Override
		public String toString() {
			String taskString = "";

			String deadlineFormat = " 'by' EEEEEEEEE',' dd MMMMMMMMM',' yyyy ";

			SimpleDateFormat sdf = new SimpleDateFormat(deadlineFormat);

			taskString = (getTaskDescription() + sdf.format(getDeadline()
					.getTime()));

			for (String tag : getTags()) {
				taskString += (" #" + tag);
			}

			return taskString;
		}

		@Override
		public int compareTo(DeadlineTask otherTask) {
			if (this.getDeadline().before(otherTask.getDeadline())){
				return -1;
			} else if (this.getDeadline().after(otherTask.getDeadline())){
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
		
		@Override
		public boolean equals(Object o){
			if (!(o instanceof DeadlineTask)){
				return false;
			} else {
				DeadlineTask deadlineO = (DeadlineTask) o;
				boolean sameDeadline = this.getDeadline().equals(deadlineO.getDeadline());
				boolean sameName = this.getTaskDescription().equals(deadlineO.getTaskDescription());
				boolean sameTags = this.getTags().equals(deadlineO.getTags());
				return sameDeadline && sameName && sameTags;
			}
		}
		
		@Override
		public int hashCode(){
			String deadlineString = this.getDeadline().toString();
			String nameString = this.getTaskDescription();
			String tagString = this.getTags().toString();
			return (deadlineString + nameString + tagString).hashCode();
		}
		
		/*
		 * (non-Javadoc)
		 * @see todo.Task#clone()
		 */
		protected DeadlineTask clone() {
			String desc = this.getTaskDescription();
			ArrayList<String> tags = new ArrayList<String>(this.getTags());
			Calendar deadline = Calendar.getInstance();
			deadline.setTimeInMillis(this.getDeadline().getTimeInMillis());
			DeadlineTask result = new DeadlineTask(desc, tags, deadline);
			if (this.isComplete()) {
				result.markAsDone();
			}
			return result;
		}

	}