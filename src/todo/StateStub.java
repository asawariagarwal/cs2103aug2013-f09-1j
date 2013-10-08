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
		cal.add(Calendar.DAY_OF_WEEK_IN_MONTH, Calendar.THURSDAY);
		DeadlineTask task2 = new DeadlineTask("Do V0.1", tags, cal);
		task2.addTag("Work");
		task2.removeTag("Home");
		stub.addTask(task2);
		
		cal.add(Calendar.DAY_OF_WEEK_IN_MONTH, Calendar.SATURDAY);
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.DAY_OF_WEEK_IN_MONTH, Calendar.MONDAY);
		TimedTask task3 = new TimedTask("Watch TV", tags, cal, cal2 );
		task3.addTag("Leisure");
		task3.removeTag("Work");
		stub.addTask(task3);
		
		return stub;
	}
}
