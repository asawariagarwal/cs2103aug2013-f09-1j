package todo;

import java.io.IOException;

public class MainHandler {
	public static void main(String[] args){
		State initState = new State();
		initState.setFeedback("Corrupted Previous State");
		try {
			initState = StorageManager.readStore();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} finally {
			CommandHandler initCommandHandler = new CommandHandler(initState);
			testUI initUI = new testUI(initCommandHandler);
			initUI.main(null);
		}
		//CommandHandler initCommandHandler = new CommandHandler(initState);
		//State dummy = new State();
		//dummy.setFeedback("Nothing to display");
		//CommandHandler initCommandHandler = new CommandHandler(dummy);
		//UserInterface initUI = new SimpleUI();
		//testUI initUI = new testUI(initCommandHandler);
		//initUI.main(null);
		//initUI.loadCommandHandler(initCommandHandler);
		//initUI.init();
	}
}
