package todo;


import static org.junit.Assert.*;

import org.junit.Test;


public class ParserUnitTests {

	
	@Test
	//Exploratory white box testing
	public void AddTimedTaskTest(){
		
	Interpreter parseObj=  new Interpreter("add timedTask from 23/12/2014 to 24/12/2015 #work");
	
	String commandType =parseObj.getCommandType();
	String taskDes = parseObj.extractTillKeyword("from");
	String Date1 = parseObj.extractTillKeyword("to");
	String Date2 = parseObj.extractTillHashtagOrEnd();
	String hashtag = parseObj.extractHashtag();
	
	//boolean dateValidity= parseObj.isDateValid("23/12/2014");
	//boolean dateValidity2 =parseObj.isDateValid("24/12/2015"); 
	
	//boolean TestDate =parseObj.isDateValid("999999");
	
	//only checking formatting of date
	
	//boundary testing for month
	//boolean TestDate1 = parseObj.isDateValid("23/-1/2014");// should return false
	//boolean TestDate2 = parseObj.isDateValid("23/0/2014");//should return true
	
	//boolean TestDate3 = parseObj.isDateValid("23/99/2015");//should return true
	//boolean TestDate4 = parseObj.isDateValid("23/100/2015");//should return false
	
	//boundary testing for day
	//boolean TestDate5 = parseObj.isDateValid("-1/5/2014");// should return false
	//boolean TestDate6 = parseObj.isDateValid("0/5/2014");//should return true
	
	//boolean TestDate7 = parseObj.isDateValid("99/5/2015");//should return true
	//boolean TestDate8 = parseObj.isDateValid("100/5/2015");//should return false
	
	
	
	assertEquals("add", commandType);
	assertEquals("timedTask",taskDes);
	assertEquals("date failed","23/12/2014",Date1);
	assertEquals("date 2 failed","24/12/2015",Date2);
	assertEquals("work",hashtag);
	
	/*assertTrue(dateValidity);
	assertTrue(dateValidity2);
	
	assertFalse(TestDate);
	
	assertFalse(TestDate1);
	assertTrue(TestDate2);
	
	assertTrue(TestDate3);
	assertFalse(TestDate4);
			
	
	assertFalse(TestDate5);
	assertTrue(TestDate6);
	
	assertTrue(TestDate7);
	assertFalse(TestDate8);
	*/
			
	}
	}
