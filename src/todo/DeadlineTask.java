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

		/**
		 * Routine to return the deadline task as a String
		 */
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

	}