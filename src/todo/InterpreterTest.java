package todo;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Asawari
 *
 */
public class InterpreterTest {

	
	Interpreter interpreter;
	
	@Before
	public void SetUp() {
		interpreter = new Interpreter();
	}
	
	@Test
	public void AddTimedTaskTest() throws Exception{
		
	interpreter.setInput("add Attend meeting for CS2103 project from 4 pm to 6pm on 20/12/2013 #work #CS2103");
	assertEquals("add",interpreter.getCommandType());
	AddCommand addComm1 = interpreter.parseTimedTask();
	assertFalse(addComm1==null);
	interpreter.setInput("Attend meeting for CS2103 project from 4 pm to 6pm on 20/12/2013 #work #CS2103");
	AddCommand addComm2 = interpreter.parseTimedTask();
	assertFalse(addComm2==null);
	
	interpreter.setInput("Attend meeting for CS2103 project from 4 pm to 6pm on 20/12/2013");
	AddCommand addComm3 = interpreter.parseTimedTask();
	assertFalse(addComm3==null);
	
	interpreter.setInput("Attend meeting for CS2103 project from 20/12/2014 to 1/1/2015 #work #CS2103");
	AddCommand addComm4 = interpreter.parseTimedTask();
	assertFalse(addComm4==null);
	
	interpreter.setInput("Attend meeting for CS2103 project from 20/12/2014 to 1/1/2015");
	AddCommand addComm5 = interpreter.parseTimedTask();
	assertFalse(addComm5==null);

	}

	@Test
	public void AddDeadlineTaskTest() throws Exception{
		
	interpreter.setInput("add Attend meeting for CS2103 project by 4 pm 20/12/2013 #work #CS2103");
	assertEquals("add",interpreter.getCommandType());
	AddCommand addComm1 = interpreter.parseDeadlineTask();
	assertFalse(addComm1==null);
	interpreter.setInput("Attend meeting for CS2103 project by 4 pm on 20/12/2013 #work #CS2103");
	AddCommand addComm2 = interpreter.parseDeadlineTask();
	assertFalse(addComm2==null);
	
	}
	
	@Test
	public void TaskDescriptionTest() throws Exception{
		
		interpreter.setInput("Watch the movie called Bye by bye birdie by 4 pm 20/12/2013 #work #CS2103");
		assertEquals("Watch the movie called Bye by bye birdie", interpreter.getDeadlineTaskDescription());
		assertEquals("4 pm 20/12/2013 #work #CS2103",interpreter.getInput());
		
		interpreter.setInput("Collect medicine from NUH from today to tomorrow");
		assertEquals("Collect medicine from NUH",interpreter.getTimedTaskDescription());
		assertEquals("today to tomorrow",interpreter.getInput());
		
		interpreter.setInput("have to complete this by today to something else");
		assertEquals("have to complete this by today",interpreter.getModifyTaskDescription());
		assertEquals("something else", interpreter.getInput());
		
		interpreter.setInput("");
		assertEquals("",interpreter.getTimedTaskDescription());
		
		interpreter.setInput("");
		assertEquals("",interpreter.getDeadlineTaskDescription());
		
		interpreter.setInput("");
		assertEquals("",interpreter.getModifyTaskDescription());
			
	}
	
	@Test
	public void viewCommandTest() throws Exception{
		
		//Positive tests
		ViewCommand viewComm1;
		interpreter.setInput("view all");
		assertEquals("view",interpreter.getCommandType());
		viewComm1 = interpreter.parseView();
		assertFalse(viewComm1==null);
		
		interpreter.setInput("view flexible");
		assertEquals("view",interpreter.getCommandType());
		viewComm1 = interpreter.parseView();
		assertFalse(viewComm1==null);
		
		interpreter.setInput("view events");
		assertEquals("view",interpreter.getCommandType());
		viewComm1 = interpreter.parseView();
		assertFalse(viewComm1==null);
		
		interpreter.setInput("view deadlines");
		assertEquals("view",interpreter.getCommandType());
		viewComm1 = interpreter.parseView();
		assertFalse(viewComm1==null);
		
		interpreter.setInput("view today");
		assertEquals("view",interpreter.getCommandType());
		viewComm1 = interpreter.parseView();
		assertFalse(viewComm1==null);
		
		interpreter.setInput("view #cs2103");
		assertEquals("view",interpreter.getCommandType());
		viewComm1 = interpreter.parseView();
		assertFalse(viewComm1==null);
		
		interpreter.setInput("view from today to tomorrow");
		assertEquals("view",interpreter.getCommandType());
		viewComm1 = interpreter.parseView();
		assertFalse(viewComm1==null);
		
		//Negative tests
		interpreter.setInput("view ");
		assertEquals("view",interpreter.getCommandType());
		viewComm1 = interpreter.parseView();
		assertTrue(viewComm1==null);
		
		interpreter.setInput("view fliahd");
		assertEquals("view",interpreter.getCommandType());
		viewComm1 = interpreter.parseView();
		assertTrue(viewComm1==null);	
	}
	
	@Test
	public void DeleteTest()throws Exception{
		DeleteCommand delComm1;
		interpreter.setInput("delete -d1");
		assertEquals("delete",interpreter.getCommandType());
		delComm1 = interpreter.parseDelete();
		assertFalse(delComm1==null);
		
		interpreter.setInput("delete any string");
		assertEquals("delete",interpreter.getCommandType());
		delComm1 = interpreter.parseDelete();
		assertFalse(delComm1==null);
		
		//Invalid command format
		interpreter.setInput("delete ");
		assertEquals("delete",interpreter.getCommandType());
		delComm1 = interpreter.parseDelete();
		assertTrue(delComm1==null);
		
	}
	
	@Test
	public void clearCommandTest() throws Exception{
		
		//Positive tests
		ClearCommand clearComm1;
		interpreter.setInput("clear");
		assertEquals("clear",interpreter.getCommandType());
		clearComm1 = interpreter.parseClear();
		assertFalse(clearComm1==null);
		
		interpreter.setInput("clear flexible");
		assertEquals("clear",interpreter.getCommandType());
		clearComm1 = interpreter.parseClear();
		assertFalse(clearComm1==null);
		
		interpreter.setInput("clear events");
		assertEquals("clear",interpreter.getCommandType());
		clearComm1 = interpreter.parseClear();
		assertFalse(clearComm1==null);
		
		interpreter.setInput("clear deadlines");
		assertEquals("clear",interpreter.getCommandType());
		clearComm1 = interpreter.parseClear();
		assertFalse(clearComm1==null);
		
		interpreter.setInput("clear done");
		assertEquals("clear",interpreter.getCommandType());
		clearComm1 = interpreter.parseClear();
		assertFalse(clearComm1==null);
		
		interpreter.setInput("clear expired");
		assertEquals("clear",interpreter.getCommandType());
		clearComm1 = interpreter.parseClear();
		assertFalse(clearComm1==null);
			
		//Negative tests
		
		interpreter.setInput("clear fliahd");
		assertEquals("clear",interpreter.getCommandType());
		clearComm1 = interpreter.parseClear();
		assertTrue(clearComm1==null);	
	}
	
	@Test
	public void UndoRedoTest()throws Exception{
		
		UndoCommand undoComm;
		interpreter.setInput("undo");
		assertEquals("undo",interpreter.getCommandType());
		undoComm = interpreter.parseUndo();
		assertFalse(undoComm==null);
		
		interpreter.setInput("undo 1");
		assertEquals("undo",interpreter.getCommandType());
		undoComm = interpreter.parseUndo();
		assertFalse(undoComm==null);
		
		interpreter.setInput("redo 1");
		assertEquals("redo",interpreter.getCommandType());
		undoComm = interpreter.parseUndo();
		assertFalse(undoComm==null);
		
		interpreter.setInput("redo");
		assertEquals("redo",interpreter.getCommandType());
		undoComm = interpreter.parseRedo();
		assertFalse(undoComm==null);
			
	}
		
	@Test
	public void populatingHashtags()throws Exception{
		ArrayList<String> hashtags = new ArrayList<String>();
		interpreter.setInput("#cs2103 #ee2020");
		int tags = interpreter.populateHashtags(hashtags);
		assertEquals("cs2103",hashtags.get(0));
		assertEquals("ee2020",hashtags.get(1));
		assertTrue(tags==1);
		
		interpreter.setInput("#cs2103 #ee20 20");
		tags = interpreter.populateHashtags(hashtags);
		assertTrue(tags==0);	
			
	}
	
	@Test
	public void dateManipulationTest() throws Exception{
		
		assertEquals("12/20/2013",interpreter.manipulateDate("20/12/2013"));
		assertEquals(" 4 pm 1/2/2013",interpreter.manipulateDate(" 4 pm 2/1/2013"));
		assertEquals("2/20/2013 4 pm ",interpreter.manipulateDate("20/2/2013 4 pm "));
		assertEquals("12/20/13",interpreter.manipulateDate("20/12/13"));
		assertEquals("12/20/13 4 pm",interpreter.manipulateDate("20/12/13 4 pm"));
		assertEquals("2/20/2013",interpreter.manipulateDate("20/2/2013"));
		assertEquals("12/2/2013",interpreter.manipulateDate("2/12/2013"));
		assertEquals("11/12/13",interpreter.manipulateDate("12/11/13"));
		
		assertEquals("12-20-2013",interpreter.manipulateDate("20-12-2013"));
		assertEquals(" 4 pm 1-2-2013",interpreter.manipulateDate(" 4 pm 2-1-2013"));
		assertEquals("2-20-2013 4 pm ",interpreter.manipulateDate("20-2-2013 4 pm "));
		assertEquals("12-20-13",interpreter.manipulateDate("20-12-13"));
		assertEquals("12-20-13 4 pm",interpreter.manipulateDate("20-12-13 4 pm"));
		assertEquals("2-20-2013",interpreter.manipulateDate("20-2-2013"));
		assertEquals("12-2-2013",interpreter.manipulateDate("2-12-2013"));
		assertEquals("11-12-13",interpreter.manipulateDate("12-11-13"));
		
		assertEquals("blah blah blah",interpreter.manipulateDate("blah blah blah"));
	}
	
	@Test
	public void NattyParsingTest() throws Exception{
		
		Calendar calendar ;
		//Different date and time formats Natty is expected to recognize
		
		calendar = interpreter.isValid("today");
		assertFalse(calendar==null);

		
		//For relative time intervals It just tests if Natty recognizes it as a valid input
		//and parses i into a calendar object
		calendar = interpreter.isValid("tomorrow noon");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("10 seconds ago");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("4 mins from now");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("day after tomorrow");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("the day before monday");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("the monday after tomorrow");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("the monday before today");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("2 fridays before today");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("4 tuesdays after today");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("next thursday 400");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("midnight last wednesday ");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("3 days from now 4:00");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("three weeks ago");
		assertFalse(calendar==null);
		
		
		//For specific times , it checks if the calendar object returned by Natty 
		//is the same as expected
		
		calendar = interpreter.isValid("The 30th of April in the year 2008 4pm");
		assertFalse(calendar==null);
		SimpleDateFormat curFormater = new SimpleDateFormat(
				"dd/MM/yyyy ha");
		Date dateObj = curFormater.parse("30/04/2008 4pm");
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(dateObj);
		assertTrue(calendar.equals(calendar2));
		
		calendar = interpreter.isValid("Fri, 21 Nov 1997 6pm");
		assertFalse(calendar==null);
		curFormater = new SimpleDateFormat(
				"dd/MM/yyyy ha");
		dateObj = curFormater.parse("21/11/1997 6pm");
		calendar2 = Calendar.getInstance();
		calendar2.setTime(dateObj);
		assertTrue(calendar.equals(calendar2));
		
		
		calendar = interpreter.isValid("Jan 21, '97 6pm");
		assertFalse(calendar==null);
		curFormater = new SimpleDateFormat(
				"dd/MM/yyyy ha");
		dateObj = curFormater.parse("21/01/1997 6pm");
		calendar2 = Calendar.getInstance();
		calendar2.setTime(dateObj);
		assertTrue(calendar.equals(calendar2));
		
		
		calendar = interpreter.isValid("jan 1st 6pm");
		assertFalse(calendar==null);
		curFormater = new SimpleDateFormat(
				"dd/MM/yyyy ha");
		dateObj = curFormater.parse("01/01/2013 6pm");
		calendar2 = Calendar.getInstance();
		calendar2.setTime(dateObj);
		assertTrue(calendar.equals(calendar2));
		
	
		calendar = interpreter.isValid("february twenty-eighth 8am");
		assertFalse(calendar==null);
		curFormater = new SimpleDateFormat(
				"dd/MM/yyyy ha");
		dateObj = curFormater.parse("28/02/2013 8am");
		calendar2 = Calendar.getInstance();
		calendar2.setTime(dateObj);
		assertTrue(calendar.equals(calendar2));
		
		//Exceptions
		//Natty recognizes numbers from 0 t0 23 as hours of the day
		calendar = interpreter.isValid("2");
		assertTrue(calendar!=null);
		
		calendar = interpreter.isValid("24");
		assertTrue(calendar==null);
	
		//Boundary case
		calendar = interpreter.isValid("");
		assertTrue(calendar==null);
		
		//It does not support holidays
		calendar = interpreter.isValid("christmas");
		assertTrue(calendar==null);
		
		//Cannot use the prefix without specifying modifier
		calendar = interpreter.isValid("day after");
		assertTrue(calendar==null);
		
		calendar = interpreter.isValid("hgvweoqygfohjwbc");
		assertTrue(calendar==null);
	
	}
	
}
	