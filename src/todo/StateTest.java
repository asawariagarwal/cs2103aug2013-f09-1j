package todo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Tests for State Class
 * @author A0098219
 *
 */
public class StateTest {

	private State s;
	private boolean cleared;
	FloatingTask ftask;
	DeadlineTask dtask;
	TimedTask etask;
	/**
	 * Sets up sample state
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		cleared = false;

		s = new State();

		Feedback f = new Feedback("added tasks", true);
		s.setFeedback(f);

		ftask = new FloatingTask("study hard",
				new ArrayList<String>());
		s.addTask(ftask);

		ArrayList<String> dtags = new ArrayList<String>();
		dtags.add("EE2020");
		Calendar deadline = Calendar.getInstance();
		dtask = new DeadlineTask("complete assignment 1", dtags,
				deadline);
		s.addTask(dtask);

		ArrayList<String> etags = new ArrayList<String>();
		etags.add("EE2021");
		etags.add("priority");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		etask = new TimedTask("complete report", etags, start, end);
		s.addTask(etask);
	}

	/**
	 * Routine to test get tasks 
	 */
	@Test
	public void getTasksTest() {
		if (!cleared) {
			assertFalse(s.getAllTasks().isEmpty());
			ArrayList<Task> taskList = s.getAllTasks();
			assertTrue(taskList.size() == 3);
		} else {
			assertTrue(s.getAllTasks().isEmpty());
		}
	}

	/**
	 * Routine to test for feedback 
	 */
	@Test
	public void getFeedbackTest() {
		if (!cleared) {
			assertTrue(s.getFeedback().getDisplay().equals("added tasks"));
			assertTrue(s.getFeedback().isPositive());
		} else {
			assertTrue(s.getFeedback().getDisplay().equals(null));
		}
	}

	/**
	 *	Routine to check for correct floating tasks 
	 */
	@Test
	public void getFloatingTasksTest() {
		if (!cleared) {
			assertFalse(s.getFloatingTasks() == null);
			TreeSet<FloatingTask> taskList = s.getFloatingTasks();
			FloatingTask task = taskList.first();
			assertFalse(task.hasTag("priority"));
		} else {
			assertTrue(s.getFloatingTasks() == null);
		}
	}

	/**
	 * Routine to test for correct timed tasks
	 */
	@Test
	public void getTimedTasksTest() {
		if (!cleared) {
			assertFalse(s.getTimedTasks() == null);
			TreeSet<TimedTask> taskList = s.getTimedTasks();
			TimedTask task = taskList.first();
			assertTrue(task.getTaskDescription().equals("complete report"));
			assertFalse(task.hasTag("EE2020"));
		} else {
			assertTrue(s.getTimedTasks() == null);
		}
	}

	/**
	 *	Routine to test for correct deadline tasks 
	 */
	@Test
	public void getDeadlineTasksTest() {
		if (!cleared) {
			assertFalse(s.getDeadlineTasks() == null);
		} else {
			assertTrue(s.getDeadlineTasks() == null);
		}
	}

	/**
	 * Routine to test for correct floating tasks 
	 */
	@Test
	public void getFlotingTasksTest() {
		if (!cleared) {
			assertTrue(s.hasDateTasks());
		} else {
			assertFalse(s.hasDateTasks());
		}
	}
	
	/**
	 * Routine to test for removeTask
	 */
	@Test
	public void removeTaskTest() {
		if(!cleared) {
			assertTrue(s.hasTask("study"));
			s.removeTask(ftask);
			assertFalse(s.hasTask("study hard"));
			
			assertTrue(s.hasTask("assignment"));
			s.removeTask(dtask);
			assertFalse(s.hasTask("assignment"));
			
			assertTrue(s.hasTask("report"));
			s.removeTask(etask);
			assertFalse(s.hasTask("report"));
			
			s.addTask(etask);
			s.addTask(dtask);
			s.addTask(ftask);
		} else {
			assertFalse(s.hasDateTasks());
		}
	}

	/**
	 * Routine to test for next and previous functionality and clear the state for testing 
	 */
	@Test
	public void nextPreviousTestAndClear() {
		State newState = new State();
		s.setNext(newState);
		newState.setPrevious(s);
		assertTrue(s.hasNext());
		assertTrue(newState.hasPrevious());
		
		s = s.getNext();
		cleared = true;
	}
}
