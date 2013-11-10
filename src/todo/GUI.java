package todo;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import java.awt.Color;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import java.awt.Cursor;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.GridLayout;

import com.dstjacques.jhotkeys.JHotKeys;
import com.dstjacques.jhotkeys.JHotKeyListener;
import com.melloware.jintellitype.JIntellitype;

/**
 * Principal GUI class designed for user interaction with the application.
 * 
 * Contains GUI state variables and methods to manipulate them, as well as a
 * nested input processor class and audio class to communicate with the rest of
 * the architecture and give audio feedback.
 * 
 * @author Karan
 * 
 */
public class GUI implements ActionListener {

	private static final String DEADLINE_PREFIX = "\t\tby:   ";

	private static final String EVENTS_TO_PREFIX = "\t\tto:   ";

	private static final String EVENTS_FROM_PREFIX = "\t\tfrom: ";

	private static final String NEW_LINE = "\n";

	private static final String INDEX_SUFFIX = ". ";

	private static final String TAB_SPACE = "\t";

	private static final int MIN_MODE_HEIGHT = 100;

	private static final int MIN_MODE_WIDTH = 700;

	private static final String TO_DO = "ToDo";

	private static final String CLOCK_DISPLAY_FORMAT = "hh:mm:ss aa\nEEEEEEEEE\ndd MMMMMMMMMMM, yyyy";
	private static final int TIMER_INTERVAL = 1000;

	private static final String FLEXIBLE_HEADER = "Flexible Tasks :\n\n";
	private static final String DEADLINES_HEADER = "Deadlines :\n\n";
	private static final String EVENTS_HEADER = "Events :\n\n";

	private static final String EMPTY_STRING = "";
	private static final String PROMPT_SYMBOL = ">";
	private static final int PROMPT_COLUMNS = 1;

	private static final String STRING_START_TYPING_HERE = "Start Typing Here....";
	private static final String STRING_FETCHING_SYSTEM_TIME = "Fetching System Time...\n\n";

	/**
	 * Constants for logging
	 */
	private static final String GUI_LOGGER = "GUILogger";

	private static final String LOG_SYSTRAY_UNSUPPORTED = "Systray Unsupported";
	private static final String LOG_DEACTIVATING_MIN_MODE = "Deactivating MinMode";
	private static final String LOG_ACTIVATING_MIN_MODE = "Activating MinMode";
	private static final String LOG_FLOATING_TASKS_ARE_EMPTY = "Floating Tasks are empty";
	private static final String LOG_DEADLINE_TASKS_ARE_EMPTY = "Deadline Tasks are empty";
	private static final String LOG_TIMED_TASKS_ARE_EMPTY = "Timed Tasks are empty";
	private static final String LOG_SYSTRAY_ENABLE_FAILED_FOR_UNKNOWN_REASON = "Systray enable failed for unknown reason";
	private static final String LOG_ATTEMPTING_TO_ENABLE_SYSTRAY_SUPPORT = "Attempting to enable systray support";
	private static final String LOG_ATTEMPTING_RESOLUTION = "\nAttempting Resolution..";
	private static final String LOG_ERROR = "Error: ";
	private static final String LOG_ATTEMPTING_TO_USE_64_BIT_DLL_AS_32_BIT_FAILED = "Attempting to use 64 bit dll as 32 bit failed.";
	private static final String LOG_WINDOWS_DETECTED = "Windows Detected";
	private static final String LOG_SHORTCUT_KEY_BEING_INITIALIZED = "Shortcut Key Being Initialized";
	private static final String LOG_PREVIOUS_STATE_WAS_CORRUPTED_NEW_STATE_CALLED = "Previous State was corrupted. New State called";
	private static final String LOG_GUI_SETTING_UP = "GUI Setting Up";
	private static final String LOG_SHORTCUT_TRIGERRED = "Shortcut Trigerred";

	/**
	 * System tray menu options
	 */
	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String MENU_OPTION_PULL_UP = "Pull Up";

	/**
	 * Important filepaths
	 */
	private static final String PATH_TO_SYSTRAY_IMAGE = "./src/img/Two.jpg";
	private static final String LIB_PATH_WINDOWS_J_INTELLITYPE_DLL = "./lib/windows/JIntellitype.dll";
	private static final String LIB_PATH_JHOTKEYS = "./lib";
	private static final String FILEPATH_LIB_WINDOWS_J_INTELLITYPE64_DLL = "./lib/windows/JIntellitype64.dll";

	private static final String OS_NAME_WINDOWS = "Windows";
	private static final String OS_NAME = "os.name";

	private static final Color GUI_BACKGROUND_COLOR = Color.BLACK;
	private static final Color FOREGROUND_COLOR_WHITE = Color.WHITE;

	private static final String FEEDBACK_CORRUPTED_PREVIOUS_STATE = "Corrupted Previous State";

	private static final String FONT_NAME = "Consolas";
	private static final String FONT_TIME_AND_DATE_PANE = "Courier New";

	/**
	 * Help Strings
	 */
	private static final String HELP_TEXT_1 = "Command List:\n\n";
	private static final String HELP_TEXT_2 = "add\ndelete\nview\nsearch\nundo/redo\nchange\nreschedule\nmark/unmark\ntag/untag\nmute/unmute\nexit";
	private static final String HELP_TEXT_3 = "\n\nPress ";
	private static final String HELP_TEXT_4 = "tab ";
	private static final String HELP_TEXT_5 = "to \nauto-complete\n\nPress ";
	private static final String HELP_TEXT_6 = "arrow-up ";
	private static final String HELP_TEXT_7 = "to \ncycle through\nprevious commands\n\nPress ";
	private static final String HELP_TEXT_8 = "F3 ";
	private static final String HELP_TEXT_9 = "to minimize\nto/maximize from the\nSystem Tray\n\nPress ";
	private static final String HELP_TEXT_10 = "Alt+Enter ";
	private static final String HELP_TEXT_11 = "to \nswitch between Min \nand Full Modes";

	/**
	 * Audio feedback class
	 */
	private static AudioFeedBack _audio;
	

	/**
	 * Class for custom scroll bar implementation
	 * 
	 * @author Karan
	 * 
	 */
	private static class CustomScrollBar extends MetalScrollBarUI {

		/**
		 * Constants for CustomScrollBar
		 */
		private static final int DIMENSION_HEIGHT_ZERO = 0;
		private static final int DIMENSION_WIDTH_ZERO = 0;
		private static final Color SCROLL_BAR_COLOR = Color.blue;
		private static final Color SCROLL_BAR_TRACK_IMAGE = Color.white;
		private static final Color SCROLL_BAR_THUMB_IMAGE = SCROLL_BAR_COLOR
				.darker();
		private static final int SCROLL_IMAGE_DIMENSION = 32;
		private Image imageThumb, imageTrack;
		private JButton b = new JButton() {

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(DIMENSION_WIDTH_ZERO,
						DIMENSION_HEIGHT_ZERO);
			}

		};

		/**
		 * Creates a custom scroll bar
		 */
		CustomScrollBar() {
			imageThumb = ScrollImage.create(SCROLL_IMAGE_DIMENSION,
					SCROLL_IMAGE_DIMENSION, SCROLL_BAR_THUMB_IMAGE);
			imageTrack = ScrollImage.create(SCROLL_IMAGE_DIMENSION,
					SCROLL_IMAGE_DIMENSION, SCROLL_BAR_TRACK_IMAGE);
		}

		@Override
		protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
			g.setColor(SCROLL_BAR_COLOR);
			((Graphics2D) g).drawImage(imageThumb, r.x, r.y, r.width, r.height,
					null);
		}

		@Override
		protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
			((Graphics2D) g).drawImage(imageTrack, r.x, r.y, r.width, r.height,
					null);
		}

		@Override
		protected JButton createDecreaseButton(int orientation) {
			return b;
		}

		@Override
		protected JButton createIncreaseButton(int orientation) {
			return b;
		}
	}

	/**
	 * Class that creates the Scroll Image
	 * 
	 * @author Karan
	 * 
	 */
	private static class ScrollImage {

		static public Image create(int w, int h, Color c) {
			BufferedImage bi = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = bi.createGraphics();
			g2d.setPaint(c);
			g2d.fillRect(0, 0, w, h);
			g2d.dispose();
			return bi;
		}
	}

	/**
	 * Nested class for Audio feedback
	 * 
	 * @author Karan
	 * 
	 */
	private final static class AudioFeedBack {
		/**
		 * Log messages
		 */
		private static final String LOG_FAILURE_AUDIO_PLAYED = "Failure Audio Played";
		private static final String LOG_SUCCESS_AUDIO_PLAYED = "Success Audio Played";
		private static final String LOG_AUDIO_FILE_FAILED_TO_OPEN = "Audio file failed to open";
		private static final String LOG_AUDIO_FAILED_TO_STREAM = "Audio failed to stream";
		private static final String LOG_AUDIO_SET_UP_HAS_FAILED = "Audio Set Up has failed";
		private static final String LOG_AUDIO_SET_UP_IS_SUCCESSFUL = "Audio Set Up is successful";
		private static final String FILE_SRC_SOUND_FAILURE_WAV = "file:./src/sound/beep2.wav";
		private static final String FILE_SRC_SOUND_SUCCESS_WAV = "file:./src/sound/beep1.wav";

		/**
		 * To store clips and URL's to them
		 */
		private static Clip _successClip;
		private static Clip _failureClip;
		private static URL _urlSuccess;
		private static URL _urlFailure;
		/**
		 * To store whether audio is enabled
		 */
		private static boolean AUDIO_ENABLED;

		AudioFeedBack() {
			setUpAudioClips();
			openAudioClips();
			this.enable();
		}

		/**
		 * Opens the audio clips
		 */
		private void openAudioClips() {
			openSuccessClip();
			openFailureClip();
		}

		/**
		 * Opens the Failure Clip
		 */
		private void openFailureClip() {
			AudioInputStream ais;
			ais = null;
			ais = initFailureAudioStream(ais);
			openFailureAudioStream(ais);
		}

		/**
		 * Opens the failure stream and sets it to ais
		 * 
		 * @param ais
		 *            Stream to be assigned
		 */
		private void openFailureAudioStream(AudioInputStream ais) {
			try {
				_failureClip.open(ais);
			} catch (LineUnavailableException e) {
				GUILogger.log(Level.WARNING, e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				GUILogger.log(Level.WARNING, e.getMessage());
				e.printStackTrace();
			}
		}

		/**
		 * Initializes the Failure audio stream
		 * 
		 * @param ais
		 *            stream to be initialized
		 * 
		 * @return the initializes stream
		 */
		private AudioInputStream initFailureAudioStream(AudioInputStream ais) {
			try {
				ais = AudioSystem.getAudioInputStream(_urlFailure);
			} catch (UnsupportedAudioFileException | IOException e) {
				GUILogger.log(Level.WARNING, e.getMessage());
				e.printStackTrace();
			}
			return ais;
		}

		/**
		 * Opens the success audio clip
		 */
		private void openSuccessClip() {
			AudioInputStream ais = null;
			ais = initSuccessAudioStream(ais);
			openSuccessAudioStream(ais);
		}

		/**
		 * Opens the success audio stream
		 * 
		 * @param ais
		 *            Stream to be assigned
		 */
		private void openSuccessAudioStream(AudioInputStream ais) {
			try {
				_successClip.open(ais);
			} catch (LineUnavailableException e) {
				GUILogger.log(Level.WARNING, LOG_AUDIO_FILE_FAILED_TO_OPEN);
				e.printStackTrace();
			} catch (IOException e) {
				GUILogger.log(Level.WARNING, LOG_AUDIO_FILE_FAILED_TO_OPEN);
				e.printStackTrace();
			}
		}

		/**
		 * Initializes the Success audio stream
		 * 
		 * @param ais
		 *            Stream to be assigned
		 * 
		 * @return the initialized stream
		 */
		private AudioInputStream initSuccessAudioStream(AudioInputStream ais) {
			try {
				ais = AudioSystem.getAudioInputStream(_urlSuccess);
			} catch (UnsupportedAudioFileException | IOException e) {
				GUILogger.log(Level.WARNING, LOG_AUDIO_FAILED_TO_STREAM);
				e.printStackTrace();
			}
			return ais;
		}

		/**
		 * Sets up the audio clips
		 */
		private void setUpAudioClips() {
			try {
				_urlSuccess = new URL(FILE_SRC_SOUND_SUCCESS_WAV);
				_successClip = AudioSystem.getClip();
				_urlFailure = new URL(FILE_SRC_SOUND_FAILURE_WAV);
				_failureClip = AudioSystem.getClip();
				GUILogger.log(Level.INFO, LOG_AUDIO_SET_UP_IS_SUCCESSFUL);
			} catch (MalformedURLException | LineUnavailableException e) {
				GUILogger.log(Level.WARNING, LOG_AUDIO_SET_UP_HAS_FAILED);
				e.printStackTrace();
			}
		}

		/**
		 * Enables audio
		 */
		protected void enable() {
			AUDIO_ENABLED = true;
		}

		/**
		 * Plays the success audio
		 */
		protected void playSuccess() {
			if (AUDIO_ENABLED) {
				_successClip.setFramePosition(_successClip.getFrameLength());
				_successClip.loop(1);
				GUILogger.log(Level.INFO, LOG_SUCCESS_AUDIO_PLAYED);
			}
		}

		/**
		 * Plays the failure audio
		 */
		protected void playFailure() {
			if (AUDIO_ENABLED) {
				_failureClip.setFramePosition(_failureClip.getFrameLength());
				_failureClip.loop(1);
				GUILogger.log(Level.INFO, LOG_FAILURE_AUDIO_PLAYED);
			}
		}

		/**
		 * Disables audio
		 */
		protected void disable() {
			AUDIO_ENABLED = false;
		}
	}

	/**
	 * Nested class to handle user input
	 * 
	 * @author Karan
	 * 
	 */
	private final class InputProcessor extends KeyAdapter {

		private static final int INITIAL_UP_KEYPRESS_COUNT = 1;

		private static final String EMPTY_STRING = "";

		private static final String FEEDBACK_SOUND_TURNED_ON = "Sound turned on";
		private static final String FEEDBACK_SOUND_TURNED_OFF = "Sound turned off";

		private static final String UNMUTE_INPUT = "unmute";
		private static final String MUTE_INPUT = "mute";
		private static final String HELP_INPUT = "help";
		private static final String EXIT_INPUT = "exit";

		/**
		 * Log constants
		 */
		private static final String LOG_ALT_KEY_RELEASED = "Alt key released";
		private static final String LOG_ENTER_KEY_PRESSED = "Enter key pressed";
		private static final String LOG_TAB_KEY_PRESSED = "Tab key pressed";
		private static final String LOG_UP_KEY_PRESSED = "Up key pressed";

		boolean altPressed = false;

		@Override
		public void keyPressed(KeyEvent e) {
			if (isEnterKeyPress(e)) {
				handleEnterKeyPress();
			}
			if (isUpKeyPress(e)) {
				GUILogger.log(Level.INFO, LOG_UP_KEY_PRESSED);
				executePrompt();
			}
			if (isTabKeyPress(e)) {
				GUILogger.log(Level.INFO, LOG_TAB_KEY_PRESSED);
				autocompleteCurrentInput();
			}
			if (isAltKeyPress(e)) {
				GUILogger.log(Level.INFO, "Alt key pressed");
				altPressed = true;
			}
		}

		/**
		 * Checks whether keypress is alt
		 * 
		 * @param e
		 *            Key Event generated
		 * 
		 * @return whether keypress was alt
		 */
		private boolean isAltKeyPress(KeyEvent e) {
			return e.getKeyCode() == KeyEvent.VK_ALT;
		}

		/**
		 * Autocompletes current input
		 */
		private void autocompleteCurrentInput() {
			String current = _userInputField.getText();
			_userInputField.setText(_autoComplete.getSuggestion(current));
		}

		/**
		 * Checks whether keypress is tab
		 * 
		 * @param e
		 *            Key Event generated
		 * 
		 * @return whether keypress is tab
		 */
		private boolean isTabKeyPress(KeyEvent e) {
			return e.getKeyCode() == KeyEvent.VK_TAB;
		}

		/**
		 * Handles an event when the user hits enter
		 */
		private void handleEnterKeyPress() {
			GUILogger.log(Level.INFO, LOG_ENTER_KEY_PRESSED);
			if (altPressed) {
				toggleMinMode();
			} else {
				handleEnteredInput();
			}
		}

		/**
		 * Prompts with recent values from session
		 */
		private void executePrompt() {
			if (_previousInputs.size() - UP_KEYPRESS_COUNTER >= 0) {
				_userInputField.setText(_previousInputs.get(_previousInputs
						.size()
						- UP_KEYPRESS_COUNTER++));
			} else {
				UP_KEYPRESS_COUNTER = INITIAL_UP_KEYPRESS_COUNT;
				_userInputField.setText(_previousInputs.get(_previousInputs
						.size()
						- UP_KEYPRESS_COUNTER++));
			}
		}

		/**
		 * Determines whether the keypress is the up arrow
		 * 
		 * @param e
		 *            Key event
		 * @return whether the keypress is up
		 */
		private boolean isUpKeyPress(KeyEvent e) {
			return e.getKeyCode() == KeyEvent.VK_UP;
		}

		/**
		 * Handles the user entered input
		 */
		private void handleEnteredInput() {
			String input = _userInputField.getText();
			if (isExit(input)) {
				System.exit(0);
			} else if (isHelp(input)) {
				_helpPane.setText(EMPTY_STRING);
				_userInputField.setText(EMPTY_STRING);
				updateHelpPane();
			} else if (isMute(input)) {
				_audio.disable();
				_userInputField.setText(EMPTY_STRING);
				_displayState.setFeedback(new Feedback(
						FEEDBACK_SOUND_TURNED_OFF, true));
			} else if (isUnmute(input)) {
				_audio.enable();
				_userInputField.setText(EMPTY_STRING);
				_displayState.setFeedback(new Feedback(
						FEEDBACK_SOUND_TURNED_ON, true));
			} else {
				clearHelpPane();
				_displayState = _handler.handleInput(input);
				updateUI(input);
			}
		}

		/**
		 * Clears the help pane
		 */
		private void clearHelpPane() {
			_helpPane.setText(EMPTY_STRING);
			appendToPane(_helpPane, HELP_PROMPT, _headerAttributes);
		}

		/**
		 * Updates the UI on input submission
		 * 
		 * @param input
		 *            Input submitted
		 */
		private void updateUI(String input) {
			_userInputField.setText(EMPTY_STRING);
			_autoComplete.updateState(_handler.getCurrentState());
			_previousInputs.add(input);
			UP_KEYPRESS_COUNTER = INITIAL_UP_KEYPRESS_COUNT;
			updateTaskFields();
			updateFeedbackPane();
			playAudioFeedback();
		}

		/**
		 * Checks whether input is unmute command
		 * 
		 * @param input
		 *            User Input
		 * 
		 * @return whether the user input is unmute
		 */
		private boolean isUnmute(String input) {
			return input.trim().equals(UNMUTE_INPUT);
		}

		/**
		 * Checks whether the input command is mute
		 * 
		 * @param input
		 *            User input
		 * 
		 * @return whether user input is mute command
		 */
		private boolean isMute(String input) {
			return input.trim().equals(MUTE_INPUT);
		}

		/**
		 * Checks whether user input is help command
		 * 
		 * @param input
		 *            User input
		 * @return whether user input is help command
		 */
		private boolean isHelp(String input) {
			return input.trim().equals(HELP_INPUT);
		}

		/**
		 * Checks whether user input is exit command
		 * 
		 * @param input
		 *            User input
		 * 
		 * @return whether user input is exit command
		 */
		private boolean isExit(String input) {
			return input.equals(EXIT_INPUT);
		}

		/**
		 * Toggles to and from min mode
		 */
		private void toggleMinMode() {
			if (min) {
				deactivateMinMode();
				min = false;
			} else {
				activateMinMode();
				min = true;
			}
		}

		/**
		 * Determines whether keypress is enter
		 * 
		 * @param e
		 *            Key event generated
		 * 
		 * @return whether keypress is enter
		 */
		private boolean isEnterKeyPress(KeyEvent e) {
			return e.getKeyCode() == KeyEvent.VK_ENTER;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (isAltKeyPress(e)) {
				GUILogger.log(Level.INFO, LOG_ALT_KEY_RELEASED);
				altPressed = false;
			}
		}
	}

	/**
	 * Triggers the min mode
	 */
	private void activateMinMode() {
		GUILogger.log(Level.INFO, LOG_ACTIVATING_MIN_MODE);
		_frmTodo.getContentPane().remove(_notificationsArea);
		_frmTodo.getContentPane().remove(_taskScrollPane);
		_frmTodo.setPreferredSize(new Dimension(MIN_MODE_WIDTH, MIN_MODE_HEIGHT));
		_frmTodo.pack();
		_frmTodo.setVisible(true);
		_frmTodo.setExtendedState(Frame.NORMAL);
	}

	/**
	 * Escapes from min mode
	 */
	private void deactivateMinMode() {
		GUILogger.log(Level.INFO, LOG_DEACTIVATING_MIN_MODE);
		_frmTodo.getContentPane().add(_notificationsArea, BorderLayout.EAST);
		_frmTodo.getContentPane().add(_taskScrollPane, BorderLayout.CENTER);
		_frmTodo.setExtendedState(Frame.MAXIMIZED_BOTH);
		_frmTodo.setVisible(true);
	}

	/**
	 * GUI state constants
	 */
	private JFrame _frmTodo;
	private static JTextPane _currentDateTimeArea;
	private static JTextPane _helpPane;
	private static JPanel _notificationsArea;
	private Timer _timer;
	private JTextField _userInputField;
	private JTextField _promptSymbol;
	private JPanel _userInputArea;
	private JPanel _userPromptArea;
	private JTextPane _timedTaskView;
	private JTextPane _deadlineTaskView;
	private JTextPane _floatingTaskView;
	private JTextPane _feedbackPane;
	private JPanel _mainViewArea;
	private static JScrollPane _taskScrollPane;
	private ArrayList<String> _previousInputs;
	private SystemTray _systemTray;
	private Image _trayImage;
	private PopupMenu _menu;
	private TrayIcon _trayIcon;
	private static CommandHandler _handler;
	private static State _displayState;
	private static Suggestor _autoComplete;
	private static int UP_KEYPRESS_COUNTER;
	private JHotKeys _shortcutKey;
	private SimpleAttributeSet _headerAttributes;
	private SimpleAttributeSet _bodyAttributes;
	private SimpleAttributeSet _tagAttributes;
	private SimpleAttributeSet _feedbackTextAttributes;
	private SimpleAttributeSet _emptyAttributes;

	/**
	 * Stores whether in min or max mode
	 */
	private static boolean min = true;

	private String HELP_PROMPT = "\nFeeling Lost?\nTry keying in 'help'";
	private SimpleAttributeSet completedAttributes;
	private SimpleAttributeSet expiredAttributes;

	/**
	 * Logger for the GUI
	 */
	protected static Logger GUILogger = Logger.getLogger(GUI_LOGGER);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GUI window = new GUI();
					window._frmTodo.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		GUILogger.log(Level.INFO, LOG_GUI_SETTING_UP);

		_handler = new CommandHandler();
		_displayState = new State();
		_autoComplete = new Suggestor();
		_displayState.setFeedback(new Feedback(
				FEEDBACK_CORRUPTED_PREVIOUS_STATE, false));
		try {
			_displayState = _handler.getCurrentState();
			_autoComplete.updateState(_displayState);
		} catch (Exception e) {
			GUILogger.log(Level.WARNING,
					LOG_PREVIOUS_STATE_WAS_CORRUPTED_NEW_STATE_CALLED);
			e.printStackTrace();
		} finally {
			initialize();
			UP_KEYPRESS_COUNTER = 1;
			_previousInputs = new ArrayList<String>();
			initTimer();
		}
	}

	/**
	 * Starts the timer
	 */
	private void initTimer() {
		_timer = new Timer(TIMER_INTERVAL, this);
		_timer.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		initMainWindow();

		setUpAttributes();

		initMainViewArea();

		initTimedTaskView();

		initDeadlineTaskView();

		initFloatingTaskView();

		updateTaskFields();

		initNotificationsArea();

		initDateTimeArea();

		initUserInputArea();

		initFeedbackPane();

		assignFocusToInput();

		updateSystemTray();

		setUpShortcutKey();

		initHelpPane();

		deactivateMinMode();

		activateMinMode();

		initAudioFeedBack();

	}

	/**
	 * Sets up the audio feedback
	 */
	private void initAudioFeedBack() {
		_audio = new AudioFeedBack();
	}

	/**
	 * Plays the audio feedback
	 */
	private void playAudioFeedback() {
		if (isUndefinedAudio()) {
			return;
		}

		if (isPositiveFeedback()) {
			_audio.playSuccess();
		} else {
			_audio.playFailure();
		}
	}

	/**
	 * Checks whether the feedback is positive
	 * 
	 * @return whether the feedback is positive
	 */
	private boolean isPositiveFeedback() {
		return _displayState.getFeedback().isPositive();
	}

	/**
	 * Checks whether the audio is undefined
	 * 
	 * @return whether the audio is undefined
	 */
	private boolean isUndefinedAudio() {
		return _audio == null;
	}

	/**
	 * Initializes the help pane
	 */
	private void initHelpPane() {
		createHelpPane();
		appendToPane(_helpPane, HELP_PROMPT, _headerAttributes);
		_notificationsArea.add(_helpPane, BorderLayout.CENTER);
	}

	/**
	 * Creates teh help pane
	 */
	private void createHelpPane() {
		_helpPane = new JTextPane();
		_helpPane.setBackground(GUI_BACKGROUND_COLOR);
		_helpPane.setFont(new Font(FONT_NAME, Font.PLAIN, 17));
		_helpPane.setEditable(false);
	}

	/**
	 * Sets up the shortcut key
	 */
	private void setUpShortcutKey() {
		GUILogger.log(Level.INFO, LOG_SHORTCUT_KEY_BEING_INITIALIZED);
		_shortcutKey = new JHotKeys(LIB_PATH_JHOTKEYS);
		if (isWindows()) {
			GUILogger.log(Level.INFO, LOG_WINDOWS_DETECTED);
			setUpJIntellitype();
		}
		assignShortcutHotKeyFunctionality();
	}

	/**
	 * Assigns a handler to the shortcut
	 */
	private void assignShortcutHotKeyFunctionality() {
		_shortcutKey.registerHotKey(0, 0, KeyEvent.VK_F3);
		JHotKeyListener hotkeyListener = new JHotKeyListener() {
			@Override
			public void onHotKey(int id) {
				GUILogger.log(Level.INFO, LOG_SHORTCUT_TRIGERRED);
				if (id == 0) {
					toggleGUI();
				}
			}
		};
		_shortcutKey.addHotKeyListener(hotkeyListener);
	}

	/**
	 * Sets up JIntellitype
	 */
	private void setUpJIntellitype() {
		JIntellitype.setLibraryLocation(LIB_PATH_WINDOWS_J_INTELLITYPE_DLL);
		try {
			_shortcutKey.registerHotKey(0, 0, KeyEvent.VK_F3);
		} catch (Exception e) {
			GUILogger.log(Level.INFO,
					LOG_ATTEMPTING_TO_USE_64_BIT_DLL_AS_32_BIT_FAILED);
			GUILogger.log(Level.WARNING, LOG_ERROR + e.getMessage()
					+ LOG_ATTEMPTING_RESOLUTION);
			JIntellitype
					.setLibraryLocation(FILEPATH_LIB_WINDOWS_J_INTELLITYPE64_DLL);
		}
	}

	/**
	 * Checks whether the OS is Windows
	 * 
	 * @return whether the OS is Windows
	 */
	private boolean isWindows() {
		return System.getProperty(OS_NAME).contains(OS_NAME_WINDOWS);
	}

	/**
	 * Toggles the GUI
	 */
	private void toggleGUI() {
		if (_frmTodo.isShowing()) {
			_frmTodo.dispose();
		} else {
			_frmTodo.setVisible(true);
		}
	}

	/**
	 * Assigns the default focus to the input field
	 */
	private void assignFocusToInput() {
		_frmTodo.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				_userInputField.requestFocusInWindow();
			}
		});
	}

	/**
	 * Updates the Feedback Pane
	 */
	private void updateFeedbackPane() {
		_feedbackPane.setText(EMPTY_STRING);
		String feedbackText = _displayState.getFeedback().getDisplay();
		if (isPositiveFeedback()) {
			appendToPane(_feedbackPane, feedbackText, completedAttributes);
		} else {
			appendToPane(_feedbackPane, feedbackText, expiredAttributes);
		}
		_userInputArea.add(_feedbackPane);
	}

	/**
	 * Updates the Help Pane
	 */
	private void updateHelpPane() {
		appendToPane(_helpPane, HELP_TEXT_1, _headerAttributes);
		appendToPane(_helpPane, HELP_TEXT_2, _feedbackTextAttributes);
		appendToPane(_helpPane, HELP_TEXT_3, _headerAttributes);
		appendToPane(_helpPane, HELP_TEXT_4, _feedbackTextAttributes);
		appendToPane(_helpPane, HELP_TEXT_5, _headerAttributes);
		appendToPane(_helpPane, HELP_TEXT_6, _feedbackTextAttributes);
		appendToPane(_helpPane, HELP_TEXT_7, _headerAttributes);
		appendToPane(_helpPane, HELP_TEXT_8, _feedbackTextAttributes);
		appendToPane(_helpPane, HELP_TEXT_9, _headerAttributes);
		appendToPane(_helpPane, HELP_TEXT_10, _feedbackTextAttributes);
		appendToPane(_helpPane, HELP_TEXT_11, _headerAttributes);
	}

	/**
	 * Updates the System Tray
	 */
	private void updateSystemTray() {
		GUILogger.log(Level.INFO, LOG_ATTEMPTING_TO_ENABLE_SYSTRAY_SUPPORT);
		if (SystemTray.isSupported()) {
			_systemTray = SystemTray.getSystemTray();

			_trayImage = Toolkit.getDefaultToolkit().getImage(
					PATH_TO_SYSTRAY_IMAGE);

			_menu = new PopupMenu();

			populateSysTrayMenu();

			setUpTrayIcon();

			addIconToSysTray();
		} else {
			GUILogger.log(Level.WARNING, LOG_SYSTRAY_UNSUPPORTED);
		}
	}

	/**
	 * Sets up tray icon
	 */
	private void setUpTrayIcon() {
		_trayIcon = new TrayIcon(_trayImage, TO_DO, _menu);
		_trayIcon.setImageAutoSize(true);

		_trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				_frmTodo.setVisible(true);
			}
		});
	}

	/**
	 * Adds an icon to System Tray
	 */
	private void addIconToSysTray() {
		try {
			_systemTray.add(_trayIcon);
		} catch (AWTException e) {
			GUILogger.log(Level.SEVERE,
					LOG_SYSTRAY_ENABLE_FAILED_FOR_UNKNOWN_REASON);
			GUILogger.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Populates the System tray Menu
	 */
	private void populateSysTrayMenu() {
		addPullUpOption();
		addExitOption();
	}

	/**
	 * Adds the exit option to the systray menu
	 */
	private void addExitOption() {
		MenuItem exitItem = new MenuItem(MENU_OPTION_EXIT);
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		_menu.add(exitItem);
	}

	/**
	 * Adds the pull up option to the systray menu
	 */
	private void addPullUpOption() {
		MenuItem pullUpItem = new MenuItem(MENU_OPTION_PULL_UP);
		pullUpItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_frmTodo.setVisible(true);
			}
		});
		_menu.add(pullUpItem);
	}

	/**
	 * Initializes the user input area
	 */
	private void initUserInputArea() {
		_userInputArea = new JPanel();
		_frmTodo.getContentPane().add(_userInputArea, BorderLayout.SOUTH);
		_userInputArea.setLayout(new BorderLayout(0, 0));
		initUserPromptArea();
		initPromptSymbol();
		initPromptInputField();
	}

	/**
	 * Initializes the user prompt area
	 */
	private void initUserPromptArea() {
		_userPromptArea = new JPanel();
		_userPromptArea.setLayout(new BorderLayout(0, 0));
		_userInputArea.add(_userPromptArea, BorderLayout.SOUTH);

	}

	/**
	 * Sets up the Prompt for the Input Field
	 */
	private void initPromptInputField() {
		_userInputField = new JTextField();
		_userInputField.addKeyListener(new InputProcessor());
		_userInputField.setCaretColor(FOREGROUND_COLOR_WHITE);
		_userInputField.setFocusTraversalKeysEnabled(false);
		_userInputField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				_userInputField.setText(EMPTY_STRING);
			}
		});
		_userPromptArea.add(_userInputField, BorderLayout.CENTER);
		_userInputField.setCursor(Cursor
				.getPredefinedCursor(Cursor.TEXT_CURSOR));
		_userInputField.setBorder(null);
		_userInputField.setForeground(FOREGROUND_COLOR_WHITE);
		_userInputField.setBackground(new Color(0, 0, 0));
		_userInputField.setText(STRING_START_TYPING_HERE);
		_userInputField.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		_userInputField.setSize(20, 1);
		_userInputField.requestFocusInWindow();
	}

	/**
	 * Sets up prompt symbol
	 */
	private void initPromptSymbol() {
		_promptSymbol = new JTextField();
		_userPromptArea.add(_promptSymbol, BorderLayout.WEST);
		_promptSymbol.setEditable(false);
		_promptSymbol.setText(PROMPT_SYMBOL);
		_promptSymbol.setForeground(FOREGROUND_COLOR_WHITE);
		_promptSymbol
				.setFont(new Font(FONT_TIME_AND_DATE_PANE, Font.PLAIN, 20));
		_promptSymbol.setColumns(PROMPT_COLUMNS);
		_promptSymbol.setBorder(null);
		_promptSymbol.setBackground(GUI_BACKGROUND_COLOR);
		_promptSymbol.setText(PROMPT_SYMBOL);
	}

	/**
	 * Sets up the notifications area
	 */
	private void initNotificationsArea() {
		_notificationsArea = new JPanel();
		_notificationsArea.setBackground(new Color(0, 0, 0));
		_frmTodo.getContentPane().add(_notificationsArea, BorderLayout.EAST);
		_notificationsArea.setLayout(new BorderLayout(0, 0));
	}

	/**
	 * Sets up the Date Time Area
	 */
	private void initDateTimeArea() {
		_currentDateTimeArea = new JTextPane();
		_currentDateTimeArea.setFont(new Font(FONT_TIME_AND_DATE_PANE,
				Font.BOLD, 17));
		_currentDateTimeArea.setForeground(FOREGROUND_COLOR_WHITE);
		_currentDateTimeArea.setText(STRING_FETCHING_SYSTEM_TIME);
		_currentDateTimeArea.setBackground(new Color(0, 0, 0));

		_currentDateTimeArea.setEditable(false);

		_notificationsArea.add(_currentDateTimeArea, BorderLayout.NORTH);
	}

	/**
	 * Sets up the pane for floating tasks
	 */
	private void initFloatingTaskView() {
		_floatingTaskView = new JTextPane();
		_floatingTaskView.setEditable(false);
		_floatingTaskView.setForeground(FOREGROUND_COLOR_WHITE);
		_floatingTaskView.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		_floatingTaskView.setBackground(GUI_BACKGROUND_COLOR);
		_floatingTaskView.setAutoscrolls(false);
	}

	/**
	 * Sets up the pane for deadline tasks
	 */
	private void initDeadlineTaskView() {
		_deadlineTaskView = new JTextPane();
		_deadlineTaskView.setEditable(false);
		_deadlineTaskView.setForeground(FOREGROUND_COLOR_WHITE);
		_deadlineTaskView.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		_deadlineTaskView.setBackground(GUI_BACKGROUND_COLOR);
		_deadlineTaskView.setAutoscrolls(false);

	}

	/**
	 * Sets up the pane for timed tasks
	 */
	private void initTimedTaskView() {
		_timedTaskView = new JTextPane();
		_timedTaskView.setEditable(false);
		_timedTaskView.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		_timedTaskView.setForeground(FOREGROUND_COLOR_WHITE);
		_timedTaskView.setBackground(new Color(0, 0, 0));
		_timedTaskView.setAutoscrolls(false);
	}

	/**
	 * Sets up the pane for feedback
	 */
	private void initFeedbackPane() {
		_feedbackPane = new JTextPane();
		_feedbackPane.setForeground(Color.YELLOW);
		_feedbackPane.setBackground(new Color(0, 0, 0));
		_feedbackPane.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		_feedbackPane.setEditable(false);
		_feedbackPane.setDisabledTextColor(Color.BLUE);
		if (!_displayState.getFeedback().getDisplay().equals("")) {
			updateFeedbackPane();
		}
		_userInputArea.add(_feedbackPane, BorderLayout.CENTER);
	}

	/**
	 * Sets up the main view area
	 */
	private void initMainViewArea() {
		_mainViewArea = new JPanel();
		_mainViewArea.setForeground(Color.GREEN);
		_mainViewArea.setBackground(new Color(0, 0, 0));

		_taskScrollPane = new JScrollPane(_mainViewArea);
		_taskScrollPane.setBorder(null);
		_taskScrollPane.setViewportBorder(null);
		_taskScrollPane
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		_taskScrollPane.setViewportView(_mainViewArea);
		_taskScrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
		_taskScrollPane.getHorizontalScrollBar().setUI(new CustomScrollBar());

		_frmTodo.getContentPane().add(_taskScrollPane, BorderLayout.CENTER);
		_mainViewArea.setLayout(new GridLayout(0, 1, 0, 0));
	}

	/**
	 * Sets up the main window
	 */
	private void initMainWindow() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		_frmTodo = new JFrame();
		_frmTodo.setIconImage(_trayImage);
		_frmTodo.getContentPane().setForeground(new Color(0, 0, 0));
		_frmTodo.getContentPane().setBackground(new Color(0, 0, 0));
		_frmTodo.getContentPane().setLayout(new BorderLayout(0, 0));
		_frmTodo.setTitle(TO_DO);
		_frmTodo.setBounds(1100, 0, 800, 850);
		_frmTodo.setLocationRelativeTo(null);
		// frmTodo.setLocationByPlatform(true);
		_frmTodo.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		_frmTodo.setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	/**
	 * Updates the task fields
	 */
	private void updateTaskFields() {
		updateTimedTaskField();
		updateDeadlineTaskField();
		updateFloatingTaskField();
		_mainViewArea.repaint();
	}

	/**
	 * Appends text to a JTextPane
	 * 
	 * @param textPane
	 *            Pane to append to
	 * @param text
	 *            text to append
	 * @param attributes
	 *            attributes to assign to appended text
	 */
	private void appendToPane(JTextPane textPane, String text,
			SimpleAttributeSet attributes) {
		StyledDocument document = textPane.getStyledDocument();

		try {
			document.insertString(document.getLength(), text, attributes);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Routine to get the attributes for heading text
	 * 
	 * @return attributes for heading text
	 */
	private SimpleAttributeSet getHeadingAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.BLUE);
		StyleConstants.setBackground(attributes, GUI_BACKGROUND_COLOR);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	/**
	 * Routine to get attributes for empty task text
	 * 
	 * @return attributes for empty task text
	 */
	private SimpleAttributeSet getEmptyAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.GRAY);
		StyleConstants.setBackground(attributes, GUI_BACKGROUND_COLOR);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	/**
	 * Routine to get attributes for standard body text
	 * 
	 * @return attributes for standard body text
	 */
	private SimpleAttributeSet getBodyAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, FOREGROUND_COLOR_WHITE);
		StyleConstants.setBackground(attributes, GUI_BACKGROUND_COLOR);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	/**
	 * Routine to get attributes for tag text
	 * 
	 * @return attributes for tag text
	 */
	private SimpleAttributeSet getTagAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.YELLOW);
		StyleConstants.setBackground(attributes, GUI_BACKGROUND_COLOR);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	/**
	 * Routine to get attributes for feedback text
	 * 
	 * @return attributes for feedback text
	 */
	private SimpleAttributeSet getFeedbackAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.YELLOW);
		StyleConstants.setBackground(attributes, GUI_BACKGROUND_COLOR);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	/**
	 * Routine to get attributes for expired task text
	 * 
	 * @return attributes for expired task text
	 */
	private SimpleAttributeSet getExpiredAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.RED);
		StyleConstants.setBackground(attributes, GUI_BACKGROUND_COLOR);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	/**
	 * Routine to get attributes for completed task text
	 * 
	 * @return attributes for completed task text
	 */
	private SimpleAttributeSet getCompletedAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.GREEN);
		StyleConstants.setBackground(attributes, GUI_BACKGROUND_COLOR);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	/**
	 * Routine to set up attributes
	 */
	private void setUpAttributes() {

		_headerAttributes = getHeadingAttributeSet();
		_emptyAttributes = getEmptyAttributeSet();
		_bodyAttributes = getBodyAttributeSet();
		_tagAttributes = getTagAttributeSet();
		_feedbackTextAttributes = getFeedbackAttributeSet();
		completedAttributes = getCompletedAttributeSet();
		expiredAttributes = getExpiredAttributeSet();
	}

	/**
	 * Routine to update field for timed tasks
	 */
	private void updateTimedTaskField() {
		if (!_displayState.getTimedTasks().isEmpty()) {

			_timedTaskView.setText(EMPTY_STRING);

			appendToPane(_timedTaskView, EVENTS_HEADER, _headerAttributes);

			int index = 0;
			for (TimedTask task : _displayState.getTimedTasks()) {
				index = addTimedTaskToPane(index, task);
			}

			_mainViewArea.add(_timedTaskView);
		} else if (_displayState.getTimedTasks().isEmpty()) {
			GUILogger.log(Level.INFO, LOG_TIMED_TASKS_ARE_EMPTY);
			_timedTaskView.setText(EMPTY_STRING);
			_mainViewArea.remove(_timedTaskView);
		}
	}

	/**
	 * Routine to add a timed task to the GUI
	 * 
	 * @param index
	 *            The index of the task to be added
	 * @param task
	 *            The task to be added
	 * @return the index of the next task
	 */
	private int addTimedTaskToPane(int index, TimedTask task) {
		String taskTags = EMPTY_STRING;
		String timedTaskText = EMPTY_STRING;
		String taskNum = TAB_SPACE + ++index + INDEX_SUFFIX;
		timedTaskText += task.getTaskDescription() + TAB_SPACE;

		taskTags = task.getTagString() + NEW_LINE;
		String taskStart = task.getStartString();
		String taskEnd = task.getEndString();

		appendToPane(_timedTaskView, taskNum, _bodyAttributes);

		if (task.isEmpty()) {
			appendToPane(_timedTaskView, timedTaskText, _emptyAttributes);
		} else if (task.isComplete()) {
			appendToPane(_timedTaskView, timedTaskText, completedAttributes);
		} else if (task.isExpired()) {
			appendToPane(_timedTaskView, timedTaskText, expiredAttributes);
		} else {
			appendToPane(_timedTaskView, timedTaskText, _bodyAttributes);
		}

		appendToPane(_timedTaskView, taskTags, _tagAttributes);
		appendToPane(_timedTaskView, EVENTS_FROM_PREFIX, _tagAttributes);
		appendToPane(_timedTaskView, taskStart + NEW_LINE, _bodyAttributes);
		appendToPane(_timedTaskView, EVENTS_TO_PREFIX, _tagAttributes);
		appendToPane(_timedTaskView, taskEnd + NEW_LINE, _bodyAttributes);
		return index;
	}

	/**
	 * Routine to update the deadline task field
	 */
	private void updateDeadlineTaskField() {
		if (!_displayState.getDeadlineTasks().isEmpty()) {

			_deadlineTaskView.setText(EMPTY_STRING);

			appendToPane(_deadlineTaskView, DEADLINES_HEADER, _headerAttributes);

			int index = 0;

			for (DeadlineTask task : _displayState.getDeadlineTasks()) {
				index = addDeadlineTaskToPane(index, task);
			}

			_mainViewArea.add(_deadlineTaskView);
		} else if (_displayState.getDeadlineTasks().isEmpty()) {
			GUILogger.log(Level.INFO, LOG_DEADLINE_TASKS_ARE_EMPTY);
			_deadlineTaskView.setText(EMPTY_STRING);
			_mainViewArea.remove(_deadlineTaskView);
		}
	}

	/**
	 * Routine to add the deadline task to the GUI
	 * 
	 * @param index
	 *            Index of the task to be added
	 * @param task
	 *            The task to be added
	 * @return The index of the next task
	 */
	private int addDeadlineTaskToPane(int index, DeadlineTask task) {
		String taskTags = EMPTY_STRING;
		String deadlineTaskText = EMPTY_STRING;
		String taskNum = TAB_SPACE + (++index) + INDEX_SUFFIX;
		taskTags = task.getTagString() + NEW_LINE;
		String taskDeadline = task.getDeadlineString();

		deadlineTaskText += task.getTaskDescription() + TAB_SPACE;
		appendToPane(_deadlineTaskView, taskNum, _bodyAttributes);

		if (task.isComplete()) {
			appendToPane(_deadlineTaskView, deadlineTaskText,
					completedAttributes);
		} else if (task.isExpired()) {
			appendToPane(_deadlineTaskView, deadlineTaskText, expiredAttributes);
		} else {
			appendToPane(_deadlineTaskView, deadlineTaskText, _bodyAttributes);
		}

		appendToPane(_deadlineTaskView, taskTags, _tagAttributes);

		appendToPane(_deadlineTaskView, DEADLINE_PREFIX, _tagAttributes);
		appendToPane(_deadlineTaskView, taskDeadline + NEW_LINE,
				_bodyAttributes);
		return index;
	}

	/**
	 * Routine to update the floating tasks field
	 */
	private void updateFloatingTaskField() {
		if (!_displayState.getFloatingTasks().isEmpty()) {
			_floatingTaskView.setText(EMPTY_STRING);

			appendToPane(_floatingTaskView, FLEXIBLE_HEADER, _headerAttributes);

			int index = 0;
			for (FloatingTask task : _displayState.getFloatingTasks()) {
				index = addTasksToFloatingPane(index, task);
			}

			_mainViewArea.add(_floatingTaskView);
		} else if (_displayState.getFloatingTasks().isEmpty()) {
			GUILogger.log(Level.INFO, LOG_FLOATING_TASKS_ARE_EMPTY);
			_floatingTaskView.setText(EMPTY_STRING);
			_mainViewArea.remove(_floatingTaskView);
		}
	}

	/**
	 * Routine to add floating tasks to the GUI
	 * 
	 * @param Index
	 *            Index of the task to be added
	 * @param Task
	 *            the task to be added
	 * @return The index of the next task
	 */
	private int addTasksToFloatingPane(int index, FloatingTask task) {
		String floatingTaskText;
		String taskTags;
		String taskNum;
		taskTags = EMPTY_STRING;
		floatingTaskText = EMPTY_STRING;
		taskNum = TAB_SPACE + (++index) + INDEX_SUFFIX;
		floatingTaskText += task.getTaskDescription() + TAB_SPACE;
		appendToPane(_floatingTaskView, taskNum, _bodyAttributes);
		if (task.isComplete()) {
			appendToPane(_floatingTaskView, floatingTaskText,
					completedAttributes);
		} else {
			appendToPane(_floatingTaskView, floatingTaskText, _bodyAttributes);
		}
		taskTags = task.getTagString() + NEW_LINE;
		appendToPane(_floatingTaskView, taskTags, _tagAttributes);
		return index;
	}

	/**
	 * Routine to get the current time to be displayed
	 * 
	 * @return Current time to be displayed as a string
	 */
	private static String getCurrentDisplayTime() {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
				CLOCK_DISPLAY_FORMAT);
		Calendar currTime = Calendar.getInstance();
		return dateTimeFormat.format(currTime.getTime());

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		_currentDateTimeArea.setText(getCurrentDisplayTime());
	}

	private static void scrollToTop() {
		_taskScrollPane.getVerticalScrollBar().setValue(0);
	}
}
