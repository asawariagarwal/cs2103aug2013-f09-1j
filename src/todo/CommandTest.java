package todo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

/**
 * Unit tests for Command and Command subclasses
 * 
 * @author A0097199H
 *
 */
public class CommandTest {

	@Test
	public void testAdd() {
		Task testAddTask;
		Command addCommand;
		State testState;
		State resultState;
		
		// null task name
		testAddTask = new FloatingTask(null, new ArrayList<String>());
		addCommand = new AddCommand(testAddTask);
		assertFalse(addCommand.isValid());
		
		// empty task name
		testAddTask = new FloatingTask("", new ArrayList<String>());
		addCommand = new AddCommand(testAddTask);
		assertFalse(addCommand.isValid());
		
		// valid floating task
		testAddTask = new FloatingTask("floating task", new ArrayList<String>());
		addCommand = new AddCommand(testAddTask);
		assertTrue(addCommand.isValid());
		
		// add floating task
		testState = new State();
		try {
			resultState = addCommand.execute(testState);
			assertTrue(resultState.getFloatingTasks().contains(testAddTask));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// null deadline
		testAddTask = new DeadlineTask("deadline task", new ArrayList<String>(),
				null);
		addCommand = new AddCommand(testAddTask);
		assertFalse(addCommand.isValid());
		
		// valid deadline task
		testAddTask = new DeadlineTask("deadline task", new ArrayList<String>(),
				Calendar.getInstance());
		addCommand = new AddCommand(testAddTask);
		assertTrue(addCommand.isValid());
		
		// add deadline task
		try {
			resultState = addCommand.execute(testState);
			assertTrue(resultState.getDeadlineTasks().contains(testAddTask));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// null start time
		testAddTask = new TimedTask("timed task", new ArrayList<String>(),
				null, Calendar.getInstance());
		addCommand = new AddCommand(testAddTask);
		assertFalse(addCommand.isValid());
		
		// null end time
		testAddTask = new TimedTask("timed task", new ArrayList<String>(),
				Calendar.getInstance(), null);
		addCommand = new AddCommand(testAddTask);
		assertFalse(addCommand.isValid());
		
		// start time after end time
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DATE, 1);
		Calendar endDate = Calendar.getInstance();
		testAddTask = new TimedTask("timed task", new ArrayList<String>(),
				startDate, endDate);
		addCommand = new AddCommand(testAddTask);
		assertFalse(addCommand.isValid());
		
		// valid timed task
		startDate = Calendar.getInstance();
		endDate = Calendar.getInstance();
		endDate.add(Calendar.DATE, 1);
		testAddTask = new TimedTask("timed task", new ArrayList<String>(),
				startDate, endDate);
		addCommand = new AddCommand(testAddTask);
		assertTrue(addCommand.isValid());
		
		// add timed task
		try {
			resultState = addCommand.execute(testState);
			assertTrue(resultState.getTimedTasks().contains(testAddTask));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
	}
	
	@Test
	public void testView() {
		Command viewCommand;
		State testState;
		State resultState;
		
		Task floating = new FloatingTask("floating", new ArrayList<String>());
		Task deadline = new DeadlineTask("deadline", new ArrayList<String>(), Calendar.getInstance());
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DATE, 1);
		Task timed = new TimedTask("timed task", new ArrayList<String>(),
				startDate, endDate);
		
		testState = new State();
		testState.addTask(floating);
		testState.addTask(deadline);
		testState.addTask(timed);
		
		// view all
		viewCommand = new ViewCommand(ViewCommand.MODE_VIEW_ALL);
		assertTrue(viewCommand.isValid());
		
		try {
			resultState = viewCommand.execute(testState);
			assertTrue(resultState.getAllTasks().contains(floating) &&
					resultState.getAllTasks().contains(deadline) &&
					resultState.getAllTasks().contains(timed));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// view floating
		viewCommand = new ViewCommand(ViewCommand.MODE_VIEW_FLOATING);
		assertTrue(viewCommand.isValid());

		try {
			resultState = viewCommand.execute(testState);
			assertTrue(resultState.getAllTasks().contains(floating) &&
					!resultState.getAllTasks().contains(deadline) &&
					!resultState.getAllTasks().contains(timed));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// view timed
		viewCommand = new ViewCommand(ViewCommand.MODE_VIEW_TIMED);
		assertTrue(viewCommand.isValid());

		try {
			resultState = viewCommand.execute(testState);
			assertTrue(!resultState.getAllTasks().contains(floating) &&
					!resultState.getAllTasks().contains(deadline) &&
					resultState.getAllTasks().contains(timed));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// view deadline
		viewCommand = new ViewCommand(ViewCommand.MODE_VIEW_DEADLINE);
		assertTrue(viewCommand.isValid());

		try {
			resultState = viewCommand.execute(testState);
			assertTrue(!resultState.getAllTasks().contains(floating) &&
					resultState.getAllTasks().contains(deadline) &&
					!resultState.getAllTasks().contains(timed));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// view done
		floating.markAsDone();
		viewCommand = new ViewCommand(ViewCommand.MODE_VIEW_DONE);
		assertTrue(viewCommand.isValid());
		try {
			resultState = viewCommand.execute(testState);
			assertTrue(resultState.getAllTasks().contains(floating) &&
					!resultState.getAllTasks().contains(deadline) &&
					!resultState.getAllTasks().contains(timed));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// view expired
		floating.markAsPending();
		floating.expire();
		viewCommand = new ViewCommand(ViewCommand.MODE_VIEW_EXPIRED);
		assertTrue(viewCommand.isValid());
		try {
			resultState = viewCommand.execute(testState);
			assertTrue(resultState.getAllTasks().contains(floating) &&
					resultState.getAllTasks().contains(deadline) &&
					!resultState.getAllTasks().contains(timed));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// view expired
		deadline.markAsDone();
		viewCommand = new ViewCommand(ViewCommand.MODE_VIEW_EXPIRED);
		assertTrue(viewCommand.isValid());
		try {
			resultState = viewCommand.execute(testState);
			assertTrue(resultState.getAllTasks().contains(floating) &&
					!resultState.getAllTasks().contains(deadline) &&
					!resultState.getAllTasks().contains(timed));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
	}
	
	@Test
	public void testModify() {
		Command modifyCommand;
		State testState;
		State resultState;
		
		// change 
		testState = new State();
		Task floating = new FloatingTask("floating", new ArrayList<String>());
		testState.addTask(floating);
		modifyCommand = new ModifyCommand("floating", "renamed floating");
		assertTrue(modifyCommand.isValid());
		try {
			resultState = modifyCommand.execute(testState, testState);
			assertTrue(resultState.getFloatingTasks().first().getTaskDescription().equals("renamed floating"));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// change by index
		testState = new State();
		floating = new FloatingTask("floating", new ArrayList<String>());
		testState.addTask(floating);
		modifyCommand = new ModifyCommand(1, ModifyCommand.INDEX_FLOATING, "renamed floating");
		assertTrue(modifyCommand.isValid());
		try {
			resultState = modifyCommand.execute(testState, testState);
			assertTrue(resultState.getFloatingTasks().first().getTaskDescription().equals("renamed floating"));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// index not in range
		testState = new State();
		floating = new FloatingTask("floating", new ArrayList<String>());
		testState.addTask(floating);
		modifyCommand = new ModifyCommand(2, ModifyCommand.INDEX_FLOATING, "renamed floating");
		assertTrue(modifyCommand.isValid());
		try {
			resultState = modifyCommand.execute(testState, testState);
			assertTrue(resultState.getFeedback().getDisplay().equals("invalid index"));
			assertTrue(resultState.getFloatingTasks().first().equals(floating));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
	}
	
	@Test
	public void testClear() {
		Command clearCommand;
		State testState;
		State resultState;
		
		Task floating = new FloatingTask("floating", new ArrayList<String>());
		Task deadline = new DeadlineTask("deadline", new ArrayList<String>(), Calendar.getInstance());
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DATE, 1);
		Task timed = new TimedTask("timed task", new ArrayList<String>(),
				startDate, endDate);
		
		testState = new State();
		testState.addTask(floating);
		testState.addTask(deadline);
		testState.addTask(timed);
		
		// clear all
		clearCommand = new ClearCommand(ClearCommand.MODE_CLEAR_ALL);
		assertTrue(clearCommand.isValid());
		try {
			resultState = clearCommand.execute(testState);
			assertTrue(resultState.getAllTasks().isEmpty());
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// clear floating
		clearCommand = new ClearCommand(ClearCommand.MODE_CLEAR_FLOATING);
		assertTrue(clearCommand.isValid());
		try {
			resultState = clearCommand.execute(testState);
			assertTrue(resultState.getFloatingTasks().isEmpty() &&
					resultState.getDeadlineTasks().contains(deadline) &&
					resultState.getTimedTasks().contains(timed));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// clear timed
		clearCommand = new ClearCommand(ClearCommand.MODE_CLEAR_TIMED);
		assertTrue(clearCommand.isValid());
		try {
			resultState = clearCommand.execute(testState);
			assertTrue(resultState.getTimedTasks().isEmpty() &&
					resultState.getDeadlineTasks().contains(deadline) &&
					resultState.getFloatingTasks().contains(floating));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// clear deadline
		clearCommand = new ClearCommand(ClearCommand.MODE_CLEAR_DEADLINE);
		assertTrue(clearCommand.isValid());
		try {
			resultState = clearCommand.execute(testState);
			assertTrue(resultState.getDeadlineTasks().isEmpty() &&
					resultState.getTimedTasks().contains(timed) &&
					resultState.getFloatingTasks().contains(floating));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
	}
	
	public void testDelete() {
		Command deleteCommand;
		State testState;
		State resultState;
		
		Task floating = new FloatingTask("floating", new ArrayList<String>());
		Task deadline = new DeadlineTask("deadline", new ArrayList<String>(), Calendar.getInstance());
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DATE, 1);
		Task timed = new TimedTask("timed task", new ArrayList<String>(),
				startDate, endDate);
		
		testState = new State();
		testState.addTask(floating);
		testState.addTask(deadline);
		testState.addTask(timed);
		
		// delete by name
		deleteCommand = new DeleteCommand("floating");
		assertTrue(deleteCommand.isValid());
		try {
			resultState = deleteCommand.execute(testState);
			assertFalse(resultState.getAllTasks().contains(floating));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// delete by index
		deleteCommand = new DeleteCommand(1, DeleteCommand.INDEX_FLOATING);
		assertTrue(deleteCommand.isValid());
		try {
			resultState = deleteCommand.execute(testState);
			assertFalse(resultState.getAllTasks().contains(floating));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// index not in range
		deleteCommand = new DeleteCommand(2, DeleteCommand.INDEX_TIMED);
		assertTrue(deleteCommand.isValid());
		try {
			resultState = deleteCommand.execute(testState);
			assertTrue(resultState.getAllTasks().size() == 3);
			assertTrue(resultState.getFeedback().getDisplay().equals("invalid index"));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
	}
	
	public void testUndo() {
		Command undoCommand;
		State testState;
		State testState2;
		State testState3;
		State resultState;
		
		testState = new State();
		testState2 = new State();
		testState3 = new State();
		testState.setNext(testState2);
		testState2.setPrevious(testState);
		testState2.setNext(testState3);
		testState3.setPrevious(testState2);
		
		// single undo
		undoCommand = new UndoCommand();
		assertTrue(undoCommand.isValid());
		try {
			resultState = undoCommand.execute(testState2);
			assertEquals(resultState, testState);
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// invalid undo
		undoCommand = new UndoCommand(-1);
		assertFalse(undoCommand.isValid());
		
		// multiple undo
		undoCommand = new UndoCommand(2);
		try {
			resultState = undoCommand.execute(testState3);
			assertEquals(resultState, testState);
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// multiple undo larger than number of states
		undoCommand = new UndoCommand(10);
		try {
			resultState = undoCommand.execute(testState3);
			assertEquals(resultState, testState);
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// single redo
		undoCommand = new UndoCommand(1, true);
		assertTrue(undoCommand.isValid());
		try {
			resultState = undoCommand.execute(testState2);
			assertEquals(resultState, testState3);
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// invalid redo
		undoCommand = new UndoCommand(-1, true);
		assertFalse(undoCommand.isValid());
		
		// multiple undo
		undoCommand = new UndoCommand(2, true);
		try {
			resultState = undoCommand.execute(testState);
			assertEquals(resultState, testState3);
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// multiple undo larger than number of states
		undoCommand = new UndoCommand(10, true);
		try {
			resultState = undoCommand.execute(testState);
			assertEquals(resultState, testState3);
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
	}
	
	public void testSearch() {
		Command searchCommand;
		State testState;
		State resultState;
		
		Task floating = new FloatingTask("floating task", new ArrayList<String>());
		Task deadline = new DeadlineTask("deadline task", new ArrayList<String>(), Calendar.getInstance());
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DATE, 1);
		Task timed = new TimedTask("timed task", new ArrayList<String>(),
				startDate, endDate);
		testState = new State();
		testState.addTask(floating);
		testState.addTask(deadline);
		testState.addTask(timed);
		
		// search "task"
		ArrayList<String> includedWords = new ArrayList<String>();
		includedWords.add("task");
		ArrayList<String> excludedWords = new ArrayList<String>();
		ArrayList<String> includedTags = new ArrayList<String>();
		ArrayList<String> excludedTags = new ArrayList<String>();
		searchCommand = new SearchCommand(includedWords, excludedWords,
				includedTags, excludedTags);
		assertTrue(searchCommand.isValid());
		try {
			resultState = searchCommand.execute(testState);
			assertTrue(resultState.getAllTasks().size() == 3);
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
		
		// search "task" without "deadline"
		includedWords = new ArrayList<String>();
		includedWords.add("task");
		excludedWords = new ArrayList<String>();
		excludedWords.add("deadline");
		includedTags = new ArrayList<String>();
		excludedTags = new ArrayList<String>();
		searchCommand = new SearchCommand(includedWords, excludedWords,
				includedTags, excludedTags);
		assertTrue(searchCommand.isValid());
		try {
			resultState = searchCommand.execute(testState);
			assertFalse(resultState.getAllTasks().contains(deadline));
		} catch (Exception e) {
			fail("exception occurred in execution");
		}
	}
}
