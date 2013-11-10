package todo;

public class Feedback {
	private String displayFeedback;
	private boolean positiveFeedback;
	
	public Feedback(){
		displayFeedback = "";
		positiveFeedback = false;
	}
	
	public Feedback(String input, boolean status){
		displayFeedback = input;
		positiveFeedback = status;
	}
	
	public Feedback(Feedback otherFeedback){
		displayFeedback = otherFeedback.getDisplay();
		positiveFeedback = otherFeedback.isPositive();
	}
	
	protected boolean isPositive(){
		return positiveFeedback;
	}
	
	protected String getDisplay(){
		return displayFeedback;
	}
	
	protected void setDisplay(String input){
		displayFeedback = input;
	}
	
	protected void setPositive(){
		positiveFeedback = true;
	}
	
	protected void setNegative(){
		positiveFeedback = false;
	}
}
