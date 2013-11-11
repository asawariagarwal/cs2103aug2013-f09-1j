package todo;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;


/**
 * @author A0098219U
 *
 */
public class TestTask {
	Task task;
	DeadlineTask deadlinetask;
	String description;
	String tag1;
	String tag2;
	TreeSet<String> tags;
	Calendar cal1;
	Calendar cal2;
	TimedTask timedtask;

	@Before
	public void setUp() {
		task = new Task();
		deadlinetask = new DeadlineTask();
		description = "Task description 222*^";
		tag1 = "home";
		tag2 = "priority";
		cal1 = Calendar.getInstance();
		cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(cal1.getTimeInMillis() + 1000000);
		;
		tags = new TreeSet<String>();
		tags.add(tag1);
		tags.add(tag2);
	}

	@Test
	public void testNullTasks() {
		Task task = new Task();
		assertTrue(task.getTagString() == "");
		assertTrue(task.getTaskDescription() == "");
		assertTrue(task.getTagString() == "");
	}

	@Test
	public void testTaskDescriptionMethods() {
		assertTrue(task.getTaskDescription() == "");

		task.setTaskDescription(description);
		assertTrue(task.getTaskDescription().equals(description));
	}

	@Test
	public void testTagMethods() {

		task.addTag(tag1);
		task.addTag(tag2);

		assertTrue(task.hasTag(tag1));

		assertTrue(task.hasTag(tag1));
		assertTrue(task.hasTag(tag2));

		assertTrue(task.getTags().equals(tags));
	}

	@Test
	public void testDeadlineMethods() {
		deadlinetask.setTaskDescription(description);
		deadlinetask.addTag(tag1);
		deadlinetask.setDeadline(cal1);
		assertTrue(deadlinetask.getDeadline().equals(cal1));
	}

	@Test
	public void testTimedMethods() {
		timedtask = new TimedTask(description, tags, cal1, cal2);
		timedtask.setStartDate(cal1);
		timedtask.setEndDate(cal2);

		assertTrue(timedtask.getStartDate().equals(cal1));
		assertTrue(timedtask.getEndDate().equals(cal2));
	}

	@Test
	public void testExpiryMethods() {
		assertFalse(task.isExpired());
		task.expire();
		assertTrue(task.isExpired());
		task.renew();
		assertFalse(task.isExpired());
	}
}
