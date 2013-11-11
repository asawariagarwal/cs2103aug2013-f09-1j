package todo;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemTesting {

	CommandHandler handler;
	State savedState;
	JSONStorage storage;

	@Before
	public void SetUp() {
		handler = new CommandHandler();
		storage = new JSONStorage();
		savedState = handler.getCurrentState();
		// Hack until delete is modified.
		ClearCommand clear = new ClearCommand();
		handler.handle(clear);
	}

	@Test
	// Test to check edge case of all tasks cleared after adding two task
	// @author A0098219
	public void checkClearCommandAfterAdd() {

		// Add task
		State displayState = handler.handleInput("do assignment");

		// Check Feedback
		String feedbackAdd = displayState.getFeedback().getDisplay();
		assertEquals("added new floating task: do assignment", feedbackAdd);

		// Check task fields
		TreeSet<FloatingTask> floatingTasks = displayState.getFloatingTasks();
		assertTrue(!floatingTasks.isEmpty());

		TreeSet<DeadlineTask> deadlineTasks = displayState.getDeadlineTasks();
		assertTrue(deadlineTasks.isEmpty());

		TreeSet<TimedTask> timedTasks = displayState.getTimedTasks();
		assertTrue(timedTasks.isEmpty());

		// Add task
		displayState = handler.handleInput("do assignment by tomorrow");

		// Check Feedback
		feedbackAdd = displayState.getFeedback().getDisplay();
		assertEquals("added new deadline task: do assignment", feedbackAdd);

		// Check task fields
		floatingTasks = displayState.getFloatingTasks();
		assertTrue(!floatingTasks.isEmpty());

		deadlineTasks = displayState.getDeadlineTasks();
		assertTrue(!deadlineTasks.isEmpty());

		timedTasks = displayState.getTimedTasks();
		assertTrue(timedTasks.isEmpty());

		// Run Clear
		displayState = handler.handleInput("clear");

		// Check Feedback
		String feedback = displayState.getFeedback().getDisplay();
		assertEquals("all tasks cleared", feedback);

		// Check task fields
		floatingTasks = displayState.getFloatingTasks();
		assertTrue(floatingTasks.isEmpty());

		deadlineTasks = displayState.getDeadlineTasks();
		assertTrue(deadlineTasks.isEmpty());

		timedTasks = displayState.getTimedTasks();
		assertTrue(timedTasks.isEmpty());

		State currentState = handler.getCurrentState();
		// Check Feedback in Current State
		feedback = currentState.getFeedback().getDisplay();
		assertEquals("all tasks cleared", feedback);

		// Check task fields
		floatingTasks = currentState.getFloatingTasks();
		assertTrue(floatingTasks.isEmpty());

		deadlineTasks = currentState.getDeadlineTasks();
		assertTrue(deadlineTasks.isEmpty());

		timedTasks = currentState.getTimedTasks();
		assertTrue(timedTasks.isEmpty());
	}

	// Test to check if adding of a deadline task works
	@Test
	public void addDeadlineTest() throws ParseException {

		// Checks whether the feedback send to the GUI is correct
		State displayState = handler
				.handleInput("add complete V0.3 by 05/10/2013 #CS2103");
		assertEquals("added new deadline task: complete V0.3", displayState
				.getFeedback().getDisplay());

		// Checks whether the task description stored in the state is correct
		String taskDes = displayState.getDeadlineTasks().last()
				.getTaskDescription();
		assertEquals("complete V0.3", taskDes);

		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
		Date dateObj = curFormater.parse("05/10/2013");
		Calendar deadlineExpected = Calendar.getInstance();
		deadlineExpected.setTime(dateObj);

		// Checks if the calendar object stored in the state is correct
		Calendar deadlineActual = displayState.getDeadlineTasks().last()
				.getDeadline();
		assertEquals(deadlineExpected.get(Calendar.DAY_OF_YEAR), deadlineActual
				.get(Calendar.DAY_OF_YEAR));

		// Checks if the hashtags stored in the task object in the state are
		// correct
		assert (displayState.getDeadlineTasks().last().getTags()
				.contains("CS2103"));

	}

	// Test to check if adding of a floating task works
	@Test
	public void addFloatingTest() {

		// Checks whether the feedback send to the GUi is correct
		State displayState2 = handler
				.handleInput("add read a book #hobby #interest ");
		assertEquals("added new floating task: read a book", displayState2
				.getFeedback().getDisplay());

		// Checks whether the task description stored in the state is correct
		String taskDes = displayState2.getFloatingTasks().last()
				.getTaskDescription();
		assertEquals("read a book", taskDes);

		// Checks if the hashtags stored in the task object in the state are
		// correct
		assert (displayState2.getFloatingTasks().last().getTags()
				.contains("hobby"));

		assert (displayState2.getFloatingTasks().last().getTags()
				.contains("interest"));

	}

	// Test to check if adding of a timed task works
	@Test
	public void addTimedTest() throws ParseException {

		// Checks whether the feedback send to the GUi is correct
		State displayState3 = handler
				.handleInput("add attend seminar from 2/12/2013 to 5/12/2013 #work");
		assertEquals("added new timed task: attend seminar", displayState3
				.getFeedback().getDisplay());

		// Checks whether the task description stored in the state is correct
		String taskDes = displayState3.getTimedTasks().last()
				.getTaskDescription();
		assertEquals("attend seminar", taskDes);

		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
		Date dateObj = curFormater.parse("2/12/2013");
		Calendar startExpected = Calendar.getInstance();
		startExpected.setTime(dateObj);

		Date dateObj2 = curFormater.parse("5/12/2013");
		Calendar endExpected = Calendar.getInstance();
		endExpected.setTime(dateObj2);

		// Checks if the calendar object stored in the state is correct
		Calendar startActual = displayState3.getTimedTasks().last()
				.getStartDate();
		assertEquals(startExpected.get(Calendar.DAY_OF_YEAR), startActual
				.get(Calendar.DAY_OF_YEAR));

		Calendar endActual = displayState3.getTimedTasks().last().getEndDate();
		assertEquals(endExpected.get(Calendar.DAY_OF_YEAR), endActual
				.get(Calendar.DAY_OF_YEAR));

		// Checks if the hashtags stored in the task object in the state are
		// correct
		assert (displayState3.getTimedTasks().last().getTags().contains("work"));

	}

	// Test to check if the feedback sent to the user for invalid command is
	// correct
	@Test
	public void deleteInvalidTask() {

		State displayState3 = handler.handleInput("delete nosuchtask");
		String feedback = displayState3.getFeedback().getDisplay();
		assertEquals("no tasks containing nosuchtask can be found", feedback);

	}

	@After
	public void restoreState() {
		try {
			storage.writeStore(savedState);
		} catch (IOException e) {
			fail("Write failed");
		}
	}
}
