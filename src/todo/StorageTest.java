package todo;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.BeforeClass;
import org.junit.Test;

public class StorageTest {
	
	private static State initState;
	
	private static State nullState;
	
	private static JSONStorage store;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		nullState = new State();
		
		initState = new State();
		ArrayList<String> tagsList = new ArrayList<String>();
		tagsList.add("tags");
		tagsList.add("#tags");
		Calendar calOne = Calendar.getInstance();
		Calendar calTwo = Calendar.getInstance();
		initState.addTask(new FloatingTask("Testing Floating",new ArrayList<String>()));
		initState.addTask(new TimedTask("Testing Timed",tagsList,calOne,calTwo));
		initState.addTask(new DeadlineTask("Testing Deadline",tagsList,calOne));
		
		store = new JSONStorage();
		
	}

	@Test
	public void testStoreWithNull() {
		boolean taskEqual = false;
		try {
			setUpBeforeClass();
		} catch (Exception e1) {
			fail("Failed setup");
		}
		try {
			store.writeStore(nullState);
		} catch (IOException e) {
			fail("Failed in Write of Null");
		}
		try {
			State readState = store.readStore();
			taskEqual = checkStringEquality(nullState.getAllTasks(),readState.getAllTasks());
		} catch (Exception e) {
			fail("Failed in Read of Null");
		}
		assertTrue(taskEqual);
	}
	
	@Test
	public void testStoreWithInit() {
		boolean taskEqual = false;
		try {
			setUpBeforeClass();
		} catch (Exception e) {
			fail("Failed setup");
		}
		try {
			store.writeStore(initState);
		} catch (IOException e) {
			fail("Failed in Write of Init");
		}
		try {
			State readState = store.readStore();
			taskEqual = checkStringEquality(initState.getAllTasks(),readState.getAllTasks());
		} catch (Exception e) {
			fail("Failed in Read of Init");
		}
		assertTrue(taskEqual);
	}
	
	private static boolean checkStringEquality(ArrayList<Task> taskList, ArrayList<Task> otherTaskList){
		if (taskList.size() != otherTaskList.size()){
			return false;
		} else {
			boolean boolCounter = true;
			for (int i=0;i<taskList.size();i++){
				if (!taskList.get(i).toString().equals(otherTaskList.get(i).toString())){
					boolCounter = false;
				}
			}
			return boolCounter;
		}
	}
	

}
