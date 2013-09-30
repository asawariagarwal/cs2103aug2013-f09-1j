package logic;

public class Parser {

	String _userInput;

	boolean parseInput(String userInput) //return true is the input is valid else return false
	{
		_userInput= userInput;
		String first_word;
		first_word=extractFirstWord(_userInput);


		switch(first_word)
		{
		case "add":
		{
			if(_userInput.equals(""))
			{
				return false;
			}
			else
			{
				return parseAdd(_userInput);
			}

		}



		case "view":
		{
			if(_userInput.equals(""))
			{
				return false;
			}
			else
			{
				if(_userInput.equals("all ordered by tags"))
					//create ViewAllObject and call command using it
					else if(_userInput.equals("floating"))
						//create ViewFloatingObject and call command using it
						else if(_userInput.startsWith("#"))
						{
							String hashtag= extractFirstWord(_userInput);
							if(_userInput!="")
								return false;
							//create ViewHashtagObject and call command using it
						}
						else
						{
							String Date;
							Date= extractTillKeyword(_userInput,"ordered");
							if(isDateValid(Date)&&(_userInput.equals("by tags")))
								//create Date Type object
								else 
									return false;
							//create ViewObject and call command using it	
						}

			}
			return true;
		}


		case "delete":
		{
			if(_userInput.equals(""))
			{
				return false;
			}
			else
			{
				if(_userInput.equals("all expired"))
					//create DeleteAllExpiredObject and call command using it
					else if(_userInput.equals("all done"))
						//create DeleteAllDoneObject and call command using it
						else if(_userInput.startsWith("all #"))
						{
							String hashtag= extractFirstWord(_userInput);
							if(_userInput!="")
								return false;
							//create DeleteHashtagObject and call command using it
						}
						else
						{
							String taskTitle=_userInput;
							//create DeleteTaskOnject and call command using it
						}
				return true;
			}
		}



		case "change":
		{
			if(_userInput.equals(""))
			{
				return false;
			}
			else
			{
				String old_task, new_task;
				old_task= extractTillKeyword(_userInput,"to");
				new_task=_userInput;
				//create ChangeObject and call command using it 
			}
			return true;
		}


		case "reschedule":
		{
			if(_userInput.equals(""))
			{
				return false;
			}
			else
			{
				String task_name;
				task_name=extractTillKeyword(_userInput,"to");
				if(_userInput.contains("to"))
				{
					String date_start=extractTillKeyword(_userInput,"to");
					String date_end=_userInput;
					if((isDateValid(date_start))&&(isDateValid(date_end)))
						//Create Date Type objects for both 
						else
							return false;
					//Create Reschedule2TimeObject and call command using it 
				}
				else
				{
					String date= _userInput;
					if(isdateValid(date))
						//Create date type object
						else
							return false;
					//Create RescheduleObject and call command using it 
				}

			}
		}


		case "mark":
		{
			if(_userInput.equals(""))
			{
				return false;
			}
			else
			{
				String TaskDes= extractTillKeyword(_userInput,"as");
				String status = _userInput;
				//Create MarkObject and call command using it 
			}
			return true;
		}


		case "drop":
		{
			if(_userInput.equals(""))
			{
				return false;
			}
			else
			{
				if(_userInput.startsWith("#"))
				{
					String hashtag= extractTillKeyword(_userInput,"from");
					String status = _userInput;
				}
				else
					return false;
				//Create MarkObject and call command using it 
			}
			return true;
		}


		case "undo":
		{
			if(_userInput.equals(""))
			{
				//create Undotype objec tnad call command using it
			}
			else
			{
				String num_to_undo = _userInput;
				//create UndoNumberObject and call command using it
			}
			return true;

		}


		case "search":
		{
			if(_userInput.equals(""))
			{
				return false;
			}
			else
			{
				String taskDes = _userInput;
				//create SearchtaskObject and call command using it
			}
			return true;

		}


		default:
			return false;

		}//end of switch 


		}//end of function 

		String  extractFirstWord(String _userInput)//removes first word from user Input 
		{
			String first_word;
			int posOfSpace;
			posOfSpace= _userInput.indexOf(" ");
			first_word=_userInput.substring(0,posOfSpace);
			_userInput=_userInput.substring(posOfSpace+1);
			return first_word; //add , delete, view, change, reschedule, mark , drop , undo , search , get
		}


		String extractTillKeyword(String _userInput, String next_keyword)//removes the extracted string and keyword from the Input
		{
			String extractedString;
			int posOfKeyword;
			posOfKeyword=_userInput.indexOf(next_keyword);
			extractedString= _userInput.substring(0,posOfKeyword);
			_userInput=(_userInput.substring(posOfKeyword+next_keyword.length())).trim();
			return extractedString;//returns extracted string just before keyword 
		}


		boolean isDateValid(String Date)//Checks if the date format is correct or not 
		{
			return true;
		}


		boolean  parseAdd(String _userInput)
		{
			if(!parseDeadlineTask(_userInput))
			{
				if(!parseTimedTask(_userInput))
				{
					return parseFloatingTask(_userInput);
				}
				else 
				{
					return true;
				}
			}
			else
			{
				return true;
			}

		}

		boolean parseDeadlineTask(String _userInput)
		{
			ArrayList <String> hashtags = new ArrayList<String>();

			if(!_userInput.contains("by"))
			{
				return false;
			}
			String TaskDes=extractTillKeyword(_userInput,"by");
			while(_userInput.contains("by"))
			{
				TaskDes.concat(" ");
				TaskDes.concat(extractTillKeyword(_userInput,"by"));
			}
			String Date;
			Date=extractTillHashtagOrEnd(_userInput);
			if(!Date.isDateValid())
			{
				return false;
			}
			//Create date type object
			if(_userInput.equals(""))
				//Create addDeadline without hashtag type object and call command using it

				int i=0;
			while(!_userInput.equals(""))
			{
				hashtags[i++]=extractHashtag(_userInput);
				if (hashtags[i].equals(""))
					return false;
			}
			//Create AddDeadLine with Hashtag type object and call command using it
			return true;
		}


		boolean parseFloatingTask(string _userInput)
		{
			ArrayList <String> hashtags = new ArrayList<String>();
            String TaskDes=extractTillHashtagOrEnd(_userInput);
            if(_userInput.equals(""))
				//Create addFloating  without hashtag type object and call command using it
			
			int i=0;
			while(input!="")
			{
				hashtags[i++]=extractHashtag(_userInput);
				if (hashtags[i]=="")
					return false;
			}
			//Create addFloating with hashtags type onject and call command using it
			return true;
		}
		
		
		String extractTillHashtagOrEnd(String _userInput)
		{
			String extractedString;
			if(_userInput.contains("#"))
			{
				int posOfHash=_userInput.indexOf("#");
				extractedString=_userInput.substring(0,posOfHash);
				_userInput=_userInput.substring(posOfHash);
			}
			else
			{
				extractString=_userInput;
			}
			return extractedString;
		}


		String extractHashtag(string _userInput)
	    {
			if(_userInput.startsWith("#"))
				return extractFirstWord(_userInput);
			else
				return "";

	    }
	}
		
		
		
		/*
	boolean processTimedTask(string _userInput)
	{
		//Task description = extract till "from" keyword      //If TaskDescription is null return false      
		//Time/Date string1 = extract till "to" keyword    
		//Check for "on" keyword 
		//if(found)
		//Time/Date string2 = extract till "on" keyword ,
		//
		//else
		// 
		//
		//                                                             |
		//hashtag is an arraylist
				int i=0;
				while(input!="")
				{
					hashtag[i++]=extractHashtag(_userInput);
					if (hashtag[i]=="")
						return false;
				}
				//Call command with Task description, date object and hashtags
				return true;
	}
*/
		