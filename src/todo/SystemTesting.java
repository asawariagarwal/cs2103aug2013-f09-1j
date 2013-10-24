package todo;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class SystemTesting {
	
	CommandHandler handler;
	
@Before
public void SetUp(){
	handler = new CommandHandler();
}
	//Test to check if adding of a deadline task works 
	@Test
	public void DeadlineTest() throws ParseException {
		
		//Checks whether the feedback send to the GUi is correct 
		State displayState  =handler.handleInput("add complete V0.3 by 25/10/2013 #CS2103");
		String feedback = displayState.getFeedback();
		assertEquals("added new deadline task: complete V0.3", feedback);
		
		//Checks whether the task description stored in the state is correct
		int len = displayState.getDeadlineTasks().size();
		String taskDes = displayState.getDeadlineTasks().get(len-1).getTaskDescription();
		assertEquals("complete V0.3",taskDes);
		
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
		Date dateObj = curFormater.parse("25/10/2013");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateObj);
		
		//Checks if the calendar object stored in the state is correct
		String deadlineActual = displayState.getDeadlineTasks().get(len-1).getDeadline().toString();
		String deadlineExpected = calendar.toString();
		assertEquals(deadlineExpected, deadlineActual);
		
		//Checks if the hashtags stored in the task object in the state are correct
		String hashtag = displayState.getDeadlineTasks().get(len-1).getTags().get(0);
		assertEquals("CS2103",hashtag);
		
	}
	
	
	//Test to check if adding of a floating task works
	@Test
	public void FloatingTest(){
		
		//Checks whether the feedback send to the GUi is correct 
		State displayState2  =handler.handleInput("add read a book #hobby #interest ");
		String feedback = displayState2.getFeedback();
		assertEquals("added new floating task: read a book", feedback);
		
		//Checks whether the task description stored in the state is correct
		int len = displayState2.getFloatingTasks().size();
		String taskDes = displayState2.getFloatingTasks().get(len-1).getTaskDescription();
		assertEquals("read a book",taskDes);
		
		//Checks if the hashtags stored in the task object in the state are correct
		String hashtag = displayState2.getFloatingTasks().get(len-1).getTags().get(0);
		assertEquals("hobby",hashtag);
		
		String hashtag2 = displayState2.getFloatingTasks().get(len-1).getTags().get(1);
		assertEquals("interest",hashtag2);
		
		
	}
	
	//Test to check if adding of a timed task works
	@Test 
	public void TimedTest() throws ParseException{
		
		//Checks whether the feedback send to the GUi is correct 
		State displayState3  =handler.handleInput("add attend seminar from 2/12/2013 to 5/12/2013 #work");
		String feedback = displayState3.getFeedback();
		assertEquals("added new timed task: attend seminar", feedback);
		
		//Checks whether the task description stored in the state is correct
		int len = displayState3.getTimedTasks().size();
		String taskDes = displayState3.getTimedTasks().get(len-1).getTaskDescription();
		assertEquals("attend seminar",taskDes);
		
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
		Date dateObj = curFormater.parse("2/12/2013");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateObj);
		
		
		Date dateObj2 = curFormater.parse("5/12/2013");
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(dateObj2);
		
		//Checks if the calendar object stored in the state is correct
		String startActual = displayState3.getTimedTasks().get(len-1).getStartDate().toString();
		String startExpected = calendar.toString();
		assertEquals(startExpected, startActual);
		
		String endActual = displayState3.getTimedTasks().get(len-1).getEndDate().toString();
		String endExpected = calendar2.toString();
		assertEquals(endExpected, endActual);
		
		//Checks if the hashtags stored in the task object in the state are correct
		String hashtag = displayState3.getTimedTasks().get(len-1).getTags().get(0);
		assertEquals("work",hashtag);
		
	}
	
	//Test to check if the feedback sent to the user for invalid command is correct
	@Test
	public void InvalidTask(){
		
		State displayState3  =handler.handleInput("bagweuigfwebuigcigcuw");
		String feedback = displayState3.getFeedback();
		assertEquals("Invalid command", feedback);
		
	}
	
}
