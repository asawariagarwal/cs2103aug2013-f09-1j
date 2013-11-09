package todo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Tests for State Class
 * @author Karan
 *
 */
public class StateTest {

	private State s;
	private boolean cleared;

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

		FloatingTask ftask = new FloatingTask("study hard",
				new ArrayList<String>());
		s.addTask(ftask);

		ArrayList<String> dtags = new ArrayList<String>();
		dtags.add("EE2020");
		Calendar deadline = Calendar.getInstance();
		DeadlineTask dtask = new DeadlineTask("complete report 1", dtags,
				deadline);
		s.addTask(dtask);

		ArrayList<String> etags = new ArrayList<String>();
		etags.add("EE2021");
		etags.add("priority");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		TimedTask etask = new TimedTask("complete report", etags, start, end);
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
