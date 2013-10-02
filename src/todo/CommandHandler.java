package todo;

class CommandHandler {
	
	public CommandHandler(State state){
		//TODO
	}
	
	public State handle(Command command) {
		if(isValidCommand(command)) {
			command.execute();
		}
	}
	
	public State handleInput(String commandString){
		//parse and handle
	}
	
	private boolean isValidCommand(Command command) {
		if(command isInstanceOf )
	}
}
