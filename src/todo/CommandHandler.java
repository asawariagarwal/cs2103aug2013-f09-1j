package todo;

class CommandHandler {
	
	public static void handle(Command command) {
		if(isValidCommand(command)) {
			command.execute();
		}
	}
	
	private static boolean isValidCommand(Command command) {
		if(command isInstanceOf )
	}
}
