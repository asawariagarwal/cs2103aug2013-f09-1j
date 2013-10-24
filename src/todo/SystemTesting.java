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
	
	@Test
	public void test() throws ParseException {
		State displayState  =handler.handleInput("add complete V0.3 by 25/10/2013 #CS2103");
		String feedback = displayState.getFeedback();
		assertEquals("added new deadline task: complete V0.3", feedback);
		
		int len = displayState.getDeadlineTasks().size();
		String taskDes = displayState.getDeadlineTasks().get(len-1).getTaskDescription();
		assertEquals("complete V0.3",taskDes);
		
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
		Date dateObj = curFormater.parse("25/10/2013");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateObj);
		
		
		String deadlineActual = displayState.getDeadlineTasks().get(len-1).getDeadline().toString();
		String deadlineExcpected = calendar.toString();
		assertEquals(deadlineExcpected, deadlineActual);
		
		String hashtag = displayState.getDeadlineTasks().get(len-1).getTags().get(0);
		assertEquals("CS2103",hashtag);
		
	}

}
