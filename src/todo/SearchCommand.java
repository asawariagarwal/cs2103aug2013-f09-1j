package todo;

/**
 * Subclass to encapsulate search commands
 * 
 * @author Eugene
 * 
 */
public class SearchCommand extends Command {
	SearchCommand() {
		super(false);
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