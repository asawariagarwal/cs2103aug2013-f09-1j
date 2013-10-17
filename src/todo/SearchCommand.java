package todo;

import java.util.ArrayList;

/**
 * Subclass to encapsulate search commands
 * 
 * @author Eugene
 * 
 */
public class SearchCommand extends Command {
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
	protected boolean isValid(State state) {
		return (keyword != null && !keyword.equals(""));
	}

	@Override
	protected State execute(State state) throws Exception {
		assert(this.isValid(state));
		
		State s = new State();
		ArrayList<Task> found = state.getTasks(keyword);
		if (found.isEmpty()) {
			s.setFeedback("No tasks found for keyword: " + keyword);
			return s;
		} else {
			for (Task t: found) {
				s.addTask(t);
			}
			s.setFeedback("Tasks found for keyword: " + keyword);
			return s;
		}
	}
}