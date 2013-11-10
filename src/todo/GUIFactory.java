package todo;

public class GUIFactory {
	static GUI _currentGUI;
	public static GUI getInstance() {
		if(_currentGUI == null) {
			return new GUI();
		}
		return _currentGUI;
	}
}
