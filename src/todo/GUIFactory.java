package todo;

/**
 * Class to return a static instance of the GUI class
 * 
 * @author Karan
 *
 */
public class GUIFactory {
	static GUI _currentGUI;
	/**
	 * Routine to return a static instance of a GUI class
	 * 
	 * @return a static instance of a GUI class
	 */
	public static GUI getInstance() {
		if(_currentGUI == null) {
			return new GUI();
		}
		return _currentGUI;
	}
}
