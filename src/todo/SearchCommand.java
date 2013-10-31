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
	private static final String FEEDBACK_NONE = "no tasks found for keyword: %1$s";
	private static final String FEEDBACK_FOUND = "tasks found for keyword: %1$s";
	
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
		
		State s = new State();
		ArrayList<Task> found = state.getTasks(keyword);
		if (found.isEmpty()) {
			logger.log(Level.INFO, LOG_NONE);
			s.setFeedback(String.format(FEEDBACK_NONE, keyword));
			return s;
		} else {
			logger.log(Level.INFO, LOG_FOUND);
			for (Task t: found) {
				s.addTask(t);
			}
			s.setFeedback(String.format(FEEDBACK_FOUND, keyword));
			return s;
		}
	}
}