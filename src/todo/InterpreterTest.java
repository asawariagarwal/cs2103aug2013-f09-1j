package todo;

import static org.junit.Assert.*;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

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
		
		calendar = interpreter.isValid("today afternoon");
		assertFalse(calendar==null);
		
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
		
		calendar = interpreter.isValid("The 31st of April in the year 2008");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("Fri, 21 Nov 1997");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("Jan 21, '97");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("Sun, Nov 21");
		assertFalse(calendar==null);
		
		calendar = interpreter.isValid("jan 1st");
		assertFalse(calendar==null);
	
		calendar = interpreter.isValid("february twenty-eighth");
		assertFalse(calendar==null);
	
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
	