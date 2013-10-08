package todo;

import java.util.ArrayList;
import java.util.Calendar;

public class StateStub {
	protected State getStateStub() {
		State stub = new State();
		
		ArrayList<String> tags =  new ArrayList<String>();
		tags.add("priority");
		tags.add("home");
		FloatingTask task1 = new FloatingTask("do something important", tags);
		stub.addTask(task1);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 2);
		DeadlineTask task2 = new DeadlineTask("Do V0.1", new ArrayList<String>(), cal);
		task2.addTag("Work");
		stub.addTask(task2);
		
		Calendar cal3 = Calendar.getInstance();
		cal3.add(Calendar.MONTH, 1);
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.MONTH, 1);
		cal2.add(Calendar.MINUTE, 300);
		TimedTask task3 = new TimedTask("Watch TV", new ArrayList<String>(), cal3, cal2 );
		task3.addTag("Leisure");
		task3.removeTag("Work");
		stub.addTask(task3);
		
		return stub;
	}
}
