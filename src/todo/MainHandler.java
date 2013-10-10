package todo;

public class MainHandler {
	public static void main(String[] args){
		State initState = StorageManager.readStore();
		CommandHandler initCommandHandler = new CommandHandler(initState);
		IUserInterface initUI = new SimpleUI();
		initUI.loadCommandHandler(initCommandHandler);
		initUI.init();
	}
}
