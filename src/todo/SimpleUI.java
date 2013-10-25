package todo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;


public class SimpleUI{
	
	private final String TASK_STRING_SEPARATOR = "\n";
	private final String TASK_CATEGORY_SEPARATOR = "\n\n";
	
	private final String INIT_WINDOW_COMMAND = "view today";
	private final String EXIT_COMMAND = "exit";
	private final String ERROR_MESSAGE = "oops";
	private final String USER_PROMPT = "command:";
	
	private final String CATEGORY_LABEL_TIMED = "Timed Tasks\n";
	private final String CATEGORY_LABEL_DEADLINE = "Deadline Tasks\n";
	private final String CATEGORY_LABEL_FLOATING = "Floating Tasks\n";
	
	CommandHandler uiCommandHandler;
	
	public void loadCommandHandler(CommandHandler handler){
		uiCommandHandler = handler;
	}
	
	public void init() {
		State initState = uiCommandHandler.handleInput(INIT_WINDOW_COMMAND);
		display(initState);
		String userInput;
		BufferedReader reader = 
				new BufferedReader(new InputStreamReader(System.in));
		try {
			do{
				userInput = receiveInput(reader);
				State windowState = uiCommandHandler.handleInput(userInput);
				display(windowState);
			} while (!(userInput.equals(EXIT_COMMAND)));
		} catch (Exception e){
			System.out.println(ERROR_MESSAGE);
			init();
		}
		return;
	}
	
	private String receiveInput(BufferedReader reader) throws IOException{
		System.out.print(USER_PROMPT);
		return reader.readLine();
	}
	
	private void display(State displayState){
		TreeSet<? extends Task> timedTasks = displayState.getTimedTasks();
		TreeSet<? extends Task> deadlineTasks = displayState.getDeadlineTasks();
		TreeSet<? extends Task> floatingTasks = displayState.getFloatingTasks();
		String userFeedback = displayState.getFeedback();
		
		String out = "";
		
		out += CATEGORY_LABEL_TIMED;
		out += taskstoString(timedTasks);
		
		out += CATEGORY_LABEL_DEADLINE;
		out += taskstoString(deadlineTasks);
		
		out += CATEGORY_LABEL_FLOATING;
		out += taskstoString(floatingTasks);
		
		out += userFeedback;
		
		System.out.println(out);
		
		return;
	}
	
	private String taskstoString(TreeSet<? extends Task> taskList){
		String output = "";
		for (Task task : taskList){
			output += (task.toString() + TASK_STRING_SEPARATOR);
		}
		output += TASK_CATEGORY_SEPARATOR;
		return output;
	}
}
