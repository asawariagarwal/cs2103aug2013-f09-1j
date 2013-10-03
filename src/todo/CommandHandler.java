package todo;

class CommandHandler {
	
	public CommandHandler(State state){
		//TODO
	}
	
	public void handle(Command command) {
		if(isValidCommand(command)) {
			command.execute(new State());
		}
	}
	
	public State handleInput(String commandString){
		return new State();
		//parse and handle
	}
	
	private boolean isValidCommand(Command command) {
		return false;
	}
}
