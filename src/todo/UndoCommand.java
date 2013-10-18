package todo;

/**
 * Subclass to encapsulate undo commands
 * 
 * @author Eugene
 * 
 */
public class UndoCommand extends Command {
	UndoCommand() {
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