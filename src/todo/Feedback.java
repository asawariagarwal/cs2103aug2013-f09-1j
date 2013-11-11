package todo;

/**
 * This class implements the Feedback object
 * which contains the feedback string and 
 * a boolean representing the positive or negative
 * nature of the feedback
 * 
 * @author A0100784L
 *
 */
public class Feedback {
	
	private String _displayFeedback;
	private boolean _positiveFeedback;
	
	public Feedback(){
		_displayFeedback = "";
		_positiveFeedback = false;
	}
	
	public Feedback(String input, boolean status){
		_displayFeedback = input;
		_positiveFeedback = status;
	}
	
	public Feedback(Feedback otherFeedback){
		_displayFeedback = otherFeedback.getDisplay();
		_positiveFeedback = otherFeedback.isPositive();
	}
	
	protected boolean isPositive(){
		return _positiveFeedback;
	}
	
	protected String getDisplay(){
		return _displayFeedback;
	}
	
	protected void setDisplay(String input){
		_displayFeedback = input;
	}
	
	protected void setPositive(){
		_positiveFeedback = true;
	}
	
	protected void setNegative(){
		_positiveFeedback = false;
	}
}
