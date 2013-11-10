package todo;

import java.util.ArrayList;
import java.util.logging.*;

/**
 * Subclass to encapsulate search commands
 * 
 * @author Eugene
 * 
 */
public class SearchCommand extends Command {
	private static final String FEEDBACK_NONE = "no tasks found for: %1$s";
	private static final String FEEDBACK_FOUND = "tasks found for: %1$s";
	
	private static final String LOG_MESSAGE = "executing search";
	private static final String LOG_NONE = "no tasks found";
	private static final String LOG_FOUND = "tasks found";
	
	private String keyword;
	
	/**
	 * Constructor for SearchCommand
	 * 
	 * @param keyword
	 * 			keyword to be searched in tasks
	 */
	SearchCommand(String keyword) {
		super(false);
		this.keyword = keyword;
	}

	@Override
	protected boolean isValid() {
		return (keyword != null && !keyword.isEmpty());
	}

	@Override
	protected State execute(State state) throws Exception {
		assert(this.isValid());
		logger.log(Level.INFO, LOG_MESSAGE);
		
		return executePowerSearch(state);
		/*
		State s = new State();
		ArrayList<Task> found = state.getTasks(keyword);
		if (found.isEmpty()) {
			logger.log(Level.INFO, LOG_NONE);
			s.setFeedback(new Feedback(String.format(FEEDBACK_NONE, keyword),false));
			return s;
		} else {
			logger.log(Level.INFO, LOG_FOUND);
			for (Task t: found) {
				s.addTask(t);
			}
			s.setFeedback(new Feedback(String.format(FEEDBACK_FOUND, keyword),true));
			return s;
		}
		*/
	}
	
	//hackish powersearch
	private State executePowerSearch(State state) throws Exception {
		String[] keywords = keyword.split(" +");
		ArrayList<Task> found = state.getAllTasks();
		for (int i=0; i<keywords.length; i++) { 
			if (keywords[i].charAt(0) == '#' && keywords[i].length() > 1) {
				String tag = keywords[i].substring(1);
				int j = 0;
				while (j < found.size()) {
					Task t = found.get(j);
					if (!t.hasTag(tag)) {
						found.remove(j);
					} else {
						j++;
					}
				}
			} else if (keywords[i].charAt(0) == '-' && keywords[i].length() > 1) {
				if (keywords[i].charAt(1) == '#') {
					if (keywords[i].length() > 2) {
						String tag = keywords[i].substring(2);
						int j = 0;
						while (j < found.size()) {
							Task t = found.get(j);
							if (t.hasTag(tag)) {
								found.remove(j);
							} else {
								j++;
							}
						}
					}
				} else {
					String word = keywords[i].substring(1);
					int j = 0;
					while (j < found.size()) {
						Task t = found.get(j);
						if (t.getTaskDescription().indexOf(word) != -1) {
							found.remove(j);
						} else {
							j++;
						}
					}
				}
			} else {
				String word = keywords[i].substring(1);
				int j = 0;
				while (j < found.size()) {
					Task t = found.get(j);
					if (t.getTaskDescription().indexOf(word) == -1) {
						found.remove(j);
					} else {
						j++;
					}
				}
			}
		}
		State s = new State();
		if (found.isEmpty()) {
			logger.log(Level.INFO, LOG_NONE);
			s.setFeedback(new Feedback(String.format(FEEDBACK_NONE, keyword),false));
			return s;
		} else {
			logger.log(Level.INFO, LOG_FOUND);
			for (Task t: found) {
				s.addTask(t);
			}
			s.setFeedback(new Feedback(String.format(FEEDBACK_FOUND, keyword),true));
			return s;
		}
	}
}