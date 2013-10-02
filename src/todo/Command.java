package todo;
 
import java.util.ArrayList;
import java.util.Calendar;
import todo.Task.DeadlineTask;
import todo.Task.TimedTask;
import todo.Task.FloatingTask;

/**
 * This class encapsulates different types of Commands
 * 
 * Subclasses:	AddCommand,
 * 				ViewCommand,
 * 				ModifyCommand
 * 				DeleteCommand
 * 				UndoCommand
 * 				SearchCommand
 * 
 * @author Eugene
 * 
 */
abstract class Command {
	
	/**
	 * Empty constructor for Command
	 * 
	 */
	Command() {
		
	}
	
	/**
	 * Checks whether Command is valid
	 * 
	 * @param state
	 * 			the current state of the program
	 * 
	 * @return true if command is valid. false if otherwise.
	 * 
	 */
	abstract protected boolean isValid(State state);
	
	/**
	 * Executes Command by modifying state
	 * 
	 * @param state
	 * 			the current state of the program
	 * 
	 * @return new state if execution of command is successful. null if otherwise.
	 * 
	 */
	abstract protected State execute(State state);
	
	/**
	 * Subclass to encapsulate add commands
	 * 
	 * @author Eugene
	 * 
	 */
	class AddCommand extends Command {
		private Task task;
		
		/**
		 * Constructor for AddCommand
		 * 
		 * @param t
		 * 			the task to be added
		 * 
		 */
		AddCommand(Task t) {
			super();
			task = t;
		}
		
		@Override
		protected boolean isValid(State state) {
			if (task == null) {
				return false;
			}
			
			if (!isValidDescription(task.getTaskDescription())) {
				return false;
			}

			if (task instanceof DeadlineTask) {
				return isValidDeadlineTask((DeadlineTask) task);
			}
			if (task instanceof TimedTask) {
				return isValidTimedTask((TimedTask) task);
			}
			return true;
		}
		
		/**
		 * Checks whether a task's description is valid
		 * Used in isValid method
		 * Description should not be null or empty string.
		 * 
		 * @param desc
		 * 			the description of the task
		 * 
		 * @return true if description is valid. false if otherwise.
		 * 
		 */
		private boolean isValidDescription(String desc) {
			return (desc != null && !desc.isEmpty());
		}
		
		/**
		 * Checks whether a DeadlineTask is valid
		 * Used in isValid method
		 * Deadline must not be null for it to be valid.
		 * 
		 * @param task
		 * 			the DeadlineTask
		 * 
		 * @return true if DeadlineTask is valid. false if otherwise.
		 * 
		 */
		private boolean isValidDeadlineTask(DeadlineTask task) {
			return (task.getDeadline() != null);
		}
		
		/**
		 * Checks whether a TimedTask is valid
		 * Used in isValid method
		 * Start time and end time must not be null.
		 * Start time must be before end time.
		 * 
		 * @param task
		 * 			the TimedTask
		 * 
		 * @return true if TimedTask is valid. false if otherwise.
		 * 
		 */
		private boolean isValidTimedTask(TimedTask task) {
			if (task.getStartDate() == null || task.getEndDate() == null) {
				return false;
			}
			return task.getStartDate().before(task.getEndDate());
		}
		
		@Override
		protected State execute(State state) {
			State s = new State(state);
			if (task instanceof TimedTask) {
				s.addTask((TimedTask) task);
				s.setFeedback("added new timed task: " + task.getTaskDescription());
				return s;
			}
			if (task instanceof DeadlineTask) {
				s.addTask((DeadlineTask) task);
				s.setFeedback("added new deadline task: " + task.getTaskDescription());
				return s;
			}
			if (task instanceof FloatingTask) {
				s.addTask((FloatingTask) task);
				s.setFeedback("added new floating task: " + task.getTaskDescription());
				return s;
			}
			return null;
		}
	}
	
	/**
	 * Subclass to encapsulate view commands
	 * 
	 * @author Eugene
	 * 
	 */
	class ViewCommand extends Command {
		private Calendar date;
		private String tag;
		private boolean isOrderedByTags = false;
		private boolean isFloating = false;
		
		/**
		 * Empty constructor for ViewCommand
		 * 
		 * Represents a view all command
		 * 
		 */
		ViewCommand() {
			super();
		}
		
		/**
		 * Constructor for ViewCommand
		 * 
		 * @param floating
		 * 			true if ViewCommand is a view floating command
		 * 
		 * Represents a view floating command, if floating is true
		 * If not, it is a view all command
		 * 
		 */
		ViewCommand(boolean floating) {
			super();
			isFloating = floating;
		}
		
		/**
		 * Constructor for ViewCommand
		 * 
		 * @param floating
		 * 			true if ViewCommand is a view floating command
		 * 
		 * @param orderByTag
		 * 			true if ViewCommand is ordered by tags
		 * 
		 * Represents a view floating command, if floating is true
		 * If not, it is a view all command
		 * View is ordered by tags if orderByTag is true.
		 * 
		 */
		ViewCommand(boolean floating, boolean orderByTag) {
			super();
			isFloating = floating;
			isOrderedByTags = orderByTag;
		}
		
		/**
		 * Constructor for ViewCommand
		 * 
		 * @param date
		 * 			date to view tasks by
		 * 
		 * If date is null, ViewCommand will be a view all command
		 * 
		 */
		ViewCommand(Calendar date) {
			super();
			this.date = date;
		}
		
		/**
		 * Constructor for ViewCommand
		 * 
		 * @param tag
		 * 			tag to view tasks by
		 * 
		 * If tag is null, ViewCommand will be a view all command
		 * 
		 */
		ViewCommand(String tag) {
			super();
			this.tag = tag;
		}

		@Override
		protected boolean isValid(State state) {
			return (isViewAll() ||
					isViewFloating() ||
					isViewOrderedByTags() ||
					isViewDate() ||
					isViewTag());
		}
		
		/**
		 * Checks if command is a "view all" command
		 * 
		 */
		private boolean isViewAll() {
			return (date == null && tag == null && !isOrderedByTags && !isFloating);
		}
		
		/**
		 * Checks if command is a "view floating" command
		 * 
		 */
		private boolean isViewFloating() {
			return (date == null && tag == null && !isOrderedByTags && isFloating);
		}
		
		/**
		 * Checks if command is a "view ordered by tags" command
		 * 
		 */
		private boolean isViewOrderedByTags() {
			return (date == null && tag == null && isOrderedByTags);
		}
		
		/**
		 * Checks if command is a "view date" command
		 * 
		 */
		private boolean isViewDate() {
			return (date != null && tag == null && !isOrderedByTags && !isFloating);
		}
		
		/**
		 * Checks if command is a "view tags" command
		 * 
		 */
		private boolean isViewTag() {
			return (date == null && tag != null && !isOrderedByTags && !isFloating);
		}

		@Override
		protected State execute(State state) {
			State s = new State(state);
			if (isViewAll()) {
				s.setFeedback("viewing all tasks");
				return s;
			}
			if (isViewFloating()) {
				s.getDeadlineTasks().clear();
				s.getTimedTasks().clear();
				s.setFeedback("viewing floating tasks");
				return s;
			}
			if (isViewOrderedByTags()) {
				// TODO How to represent a view ordered by tags??
			}
			if (isViewDate()) {
				s = new State();
				int dd = date.get(Calendar.DATE);
				int mm = date.get(Calendar.MONTH);
				int yy = date.get(Calendar.YEAR);
				ArrayList<DeadlineTask> deadline = state.getDeadlineTasks();
				ArrayList<TimedTask> timed = state.getTimedTasks();
				for (int i=0; i<deadline.size(); i++) {
					DeadlineTask cur = deadline.get(i);
					if (dd == cur.getDeadline().get(Calendar.DATE) ||
						mm == cur.getDeadline().get(Calendar.MONTH) ||
						yy == cur.getDeadline().get(Calendar.YEAR)) {
						s.addTask(cur);
					}
				}
				for (int i=0; i<timed.size(); i++) {
					TimedTask cur = timed.get(i);
					if (date.compareTo(cur.getStartDate()) >= 0 && date.compareTo(cur.getEndDate()) <= 0) {
						s.addTask(cur);
					}
				}
				s.setFeedback("viewing tasks by date");
				return s;
			}
			if (isViewTag()) {
				s = new State();
				ArrayList<DeadlineTask> deadline = state.getDeadlineTasks();
				ArrayList<TimedTask> timed = state.getTimedTasks();
				ArrayList<FloatingTask> floating = state.getFloatingTasks();
				for (int i=0; i<deadline.size(); i++) {
					DeadlineTask cur = deadline.get(i);
					if (cur.hasTag(tag)) {
						s.addTask(cur);
					}
				}
				for (int i=0; i<timed.size(); i++) {
					TimedTask cur = timed.get(i);
					if (cur.hasTag(tag)) {
						s.addTask(cur);
					}
				}
				for (int i=0; i<floating.size(); i++) {
					FloatingTask cur = floating.get(i);
					if (cur.hasTag(tag)) {
						s.addTask(cur);
					}
				}
				s.setFeedback("viewing tasks by tag");
				return s;
			}
			return null;
		}
	}

	/**
	 * Subclass to encapsulate modify commands
	 * 
	 * @author Eugene
	 * 
	 */
	class ModifyCommand extends Command {
		ModifyCommand() {
			super();
		}

		@Override
		protected boolean isValid(State state) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected State execute(State state) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * Subclass to encapsulate delete commands
	 * 
	 * @author Eugene
	 * 
	 */
	class DeleteCommand extends Command {
		DeleteCommand(String deleted) {
			super();
		}

		@Override
		protected boolean isValid(State state) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected State execute(State state) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * Subclass to encapsulate undo commands
	 * 
	 * @author Eugene
	 * 
	 */
	class UndoCommand extends Command {
		UndoCommand() {
			super();
		}

		@Override
		protected boolean isValid(State state) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected State execute(State state) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * Subclass to encapsulate search commands
	 * 
	 * @author Eugene
	 * 
	 */
	class SearchCommand extends Command {
		SearchCommand() {
			super();
		}

		@Override
		protected boolean isValid(State state) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected State execute(State state) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}