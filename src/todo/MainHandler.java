package todo;

public class MainHandler {
	public static void main(){
		State initState = StorageManager.readStore();
		CommandHandler initCommandHandler = new CommandHandler(initState);
		UserInterface initUI = new SimpleUI();
		initUI.loadCommandHandler(initCommandHandler);
		initUI.init();
	}
}
