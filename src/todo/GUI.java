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

public class GUI implements ActionListener {

	private static final String FONT_NAME = "Consolas";
	private static final String HELP_TEXT_1 = "Command List:\n\n";
	private static final String HELP_TEXT_2 = "add\ndelete\nview\nundo\nredo\nchange\nreschedule\nmark\nmute/unmute\nexit";
	private static final String HELP_TEXT_3 = "\n\nPress ";
	private static final String HELP_TEXT_4 = "tab ";
	private static final String HELP_TEXT_5 = "to \nauto-complete\n\nPress ";
	private static final String HELP_TEXT_6 = "arrow-up ";
	private static final String HELP_TEXT_7 = "to \ncycle through\nprevious commands\n\nPress ";
	private static final String HELP_TEXT_8 = "F3 ";
	private static final String HELP_TEXT_9 = "to minimize\nto/maximize from the\nSystem Tray\n\nPress ";
	private static final String HELP_TEXT_10 = "Alt+Enter ";
	private static final String HELP_TEXT_11 = "to \nswitch between Min \nand Full Modes";

	private static AudioFeedBack audio;

	private static class CustomScrollBar extends MetalScrollBarUI {

		private Image imageThumb, imageTrack;
		private JButton b = new JButton() {

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 0);
			}

		};

		CustomScrollBar() {
			imageThumb = ScrollImage.create(32, 32, Color.blue.darker());
			imageTrack = ScrollImage.create(32, 32, Color.white);
		}

		@Override
		protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
			g.setColor(Color.blue);
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
		private static boolean AUDIO_ENABLED;

		AudioFeedBack() {
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
			AudioInputStream ais = null;
			try {
				ais = AudioSystem.getAudioInputStream(_urlSuccess);
			} catch (UnsupportedAudioFileException | IOException e) {
				GUILogger.log(Level.WARNING, LOG_AUDIO_FAILED_TO_STREAM);
				e.printStackTrace();
			}
			try {
				_successClip.open(ais);
			} catch (LineUnavailableException e) {
				GUILogger.log(Level.WARNING, LOG_AUDIO_FILE_FAILED_TO_OPEN);
				e.printStackTrace();
			} catch (IOException e) {
				GUILogger.log(Level.WARNING, LOG_AUDIO_FILE_FAILED_TO_OPEN);
				e.printStackTrace();
			}

			ais = null;
			try {
				ais = AudioSystem.getAudioInputStream(_urlFailure);
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
			try {
				_failureClip.open(ais);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.enable();
		}

		protected void enable() {
			AUDIO_ENABLED = true;
		}

		protected void playSuccess() {
			if (AUDIO_ENABLED) {
				_successClip.setFramePosition(_successClip.getFrameLength());
				_successClip.loop(1);
				GUILogger.log(Level.INFO, LOG_SUCCESS_AUDIO_PLAYED);
			}
		}

		protected void playFailure() {
			if (AUDIO_ENABLED) {
				_failureClip.setFramePosition(_failureClip.getFrameLength());
				_failureClip.loop(1);
				GUILogger.log(Level.INFO, LOG_FAILURE_AUDIO_PLAYED);
			}
		}

		protected void disable() {
			AUDIO_ENABLED = false;
		}
	}

	private final class InputProcessor extends KeyAdapter {

		private static final String LOG_TAB_KEY_PRESSED = "Tab key pressed";
		private static final String LOG_UP_KEY_PRESSED = "Up key pressed";
		boolean altPressed = false;

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				GUILogger.log(Level.INFO, "Enter key pressed");
				if (altPressed) {
					if (min) {
						deactivateMinMode();
						min = false;
					} else {
						activateMinMode();
						min = true;
					}
				} else {
					String input = _userInputField.getText();
					if (input.equals("exit")) {
						System.exit(0);
					} else if (input.trim().equals("help")) {
						_helpPane.setText("");
						_userInputField.setText("");
						updateHelpPane();

					} else if (input.trim().equals("mute")) {
						audio.disable();
						_userInputField.setText("");
						_displayState.setFeedback(new Feedback(
								"Sound turned off", true));
					} else if (input.trim().equals("unmute")) {
						audio.enable();
						_userInputField.setText("");
						_displayState.setFeedback(new Feedback(
								"Sound turned on", true));
					} else {
						_helpPane.setText("");
						appendToPane(_helpPane, HELP_PROMPT, _headerAttributes);

						_displayState = _handler.handleInput(input);
						_userInputField.setText("");
						_autoComplete.updateState(_handler.getCurrentState());
						_previousInputs.add(input);
						UP_KEYPRESS_COUNTER = 1;
						updateTaskFields();
						updateFeedbackPane();
						playAudioFeedback();
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				GUILogger.log(Level.INFO, LOG_UP_KEY_PRESSED);
				if (_previousInputs.size() - UP_KEYPRESS_COUNTER >= 0) {
					_userInputField.setText(_previousInputs.get(_previousInputs
							.size()
							- UP_KEYPRESS_COUNTER++));
				} else {
					UP_KEYPRESS_COUNTER = 1;
					_userInputField.setText(_previousInputs.get(_previousInputs
							.size()
							- UP_KEYPRESS_COUNTER++));
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_TAB) {
				GUILogger.log(Level.INFO, LOG_TAB_KEY_PRESSED);
				String current = _userInputField.getText();
				_userInputField.setText(_autoComplete.getSuggestion(current));
			}
			if (e.getKeyCode() == KeyEvent.VK_ALT) {
				GUILogger.log(Level.INFO, "Alt key pressed");
				altPressed = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ALT) {
				GUILogger.log(Level.INFO, "Alt key released");
				altPressed = false;
			}
		}
	}

	private void activateMinMode() {
		GUILogger.log(Level.INFO, "Activating MinMode");
		_frmTodo.getContentPane().remove(_notificationsArea);
		_frmTodo.getContentPane().remove(TaskScrollPane);
		_frmTodo.setPreferredSize(new Dimension(700, 100));
		_frmTodo.pack();
		_frmTodo.setVisible(true);
		_frmTodo.setExtendedState(Frame.NORMAL);
	}

	private void deactivateMinMode() {
		GUILogger.log(Level.INFO, "Deactivating MinMode");
		_frmTodo.getContentPane().add(_notificationsArea, BorderLayout.EAST);
		_frmTodo.getContentPane().add(TaskScrollPane, BorderLayout.CENTER);
		_frmTodo.setBounds(1100, 0, 800, 850);
		_frmTodo.setExtendedState(Frame.MAXIMIZED_BOTH);
		_frmTodo.setVisible(true);
	}

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
	private JScrollPane TaskScrollPane;
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

	private static boolean min = true;

	private String HELP_PROMPT = "\nFeeling Lost?\nTry keying in 'help'";
	private SimpleAttributeSet completedAttributes;
	private SimpleAttributeSet expiredAttributes;

	protected static Logger GUILogger = Logger.getLogger("GUILogger");

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
		GUILogger.log(Level.INFO, "GUI Setting Up");

		_handler = new CommandHandler();
		_displayState = new State();
		_autoComplete = new Suggestor();
		_displayState.setFeedback(new Feedback("Corrupted Previous State",
				false));
		try {
			_displayState = _handler.getCurrentState();
			_autoComplete.updateState(_displayState);
		} catch (Exception e) {
			GUILogger.log(Level.WARNING,
					"Previous State was corrupted. New State called");
			e.printStackTrace();
		} finally {
			initialize();
			UP_KEYPRESS_COUNTER = 1;
			_previousInputs = new ArrayList<String>();
			_timer = new Timer(1000, this);
			_timer.start();
		}
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

		// I know. But hey, whatever works.

		deactivateMinMode();

		activateMinMode();

		initAudioFeedBack();
	}

	private void initAudioFeedBack() {
		audio = new AudioFeedBack();
	}

	private void playAudioFeedback() {
		if (audio == null) {
			return;
		}

		if (_displayState.getFeedback().isPositive()) {
			audio.playSuccess();
		} else {
			audio.playFailure();
		}
	}

	private void initHelpPane() {
		_helpPane = new JTextPane();
		_helpPane.setBackground(Color.BLACK);
		_helpPane.setFont(new Font("Consolas", Font.PLAIN, 17));
		_helpPane.setEditable(false);
		appendToPane(_helpPane, HELP_PROMPT, _headerAttributes);
		_notificationsArea.add(_helpPane, BorderLayout.CENTER);
	}

	private void setUpShortcutKey() {
		GUILogger.log(Level.INFO, "Shortcut Key Being Initialized");
		_shortcutKey = new JHotKeys("./lib");
		if (System.getProperty("os.name").contains("Windows")) {
			GUILogger.log(Level.INFO, "Windows Detected");
			JIntellitype.setLibraryLocation("./lib/windows/JIntellitype.dll");
			try {
				_shortcutKey.registerHotKey(0, 0, KeyEvent.VK_F3);
			} catch (Exception e) {
				GUILogger.log(Level.INFO,
						"Attempting to use 64 bit dll as 32 bit failed.");
				System.out.println("Error: " + e.getMessage()
						+ "\nAttempting Resolution..");
				JIntellitype
						.setLibraryLocation("./lib/windows/JIntellitype64.dll");
			}
		}
		_shortcutKey.registerHotKey(0, 0, KeyEvent.VK_F3);
		JHotKeyListener hotkeyListener = new JHotKeyListener() {
			@Override
			public void onHotKey(int id) {
				GUILogger.log(Level.INFO, "Shortcut Trigerred");
				if (id == 0) {
					toggleGUI();
				}
			}
		};
		_shortcutKey.addHotKeyListener(hotkeyListener);
	}

	private void toggleGUI() {
		if (_frmTodo.isShowing() == true) {
			_frmTodo.dispose();
		} else {
			_frmTodo.setVisible(true);
		}
	}

	private void assignFocusToInput() {
		_frmTodo.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				_userInputField.requestFocusInWindow();
			}
		});
	}

	private void updateFeedbackPane() {
		_feedbackPane.setText(_displayState.getFeedback().getDisplay());
		_userInputArea.add(_feedbackPane);
	}

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

	private void updateSystemTray() {
		GUILogger.log(Level.INFO, "Attempting to enable systray support");
		if (SystemTray.isSupported()) {
			_systemTray = SystemTray.getSystemTray();

			_trayImage = Toolkit.getDefaultToolkit().getImage(
					"./src/img/Two.jpg");

			_menu = new PopupMenu();

			MenuItem pullUpItem = new MenuItem("Pull Up");
			pullUpItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					_frmTodo.setVisible(true);
				}
			});
			_menu.add(pullUpItem);

			MenuItem exitItem = new MenuItem("Exit");
			exitItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			_menu.add(exitItem);

			_trayIcon = new TrayIcon(_trayImage, "ToDo", _menu);
			_trayIcon.setImageAutoSize(true);

			_trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					_frmTodo.setVisible(true);
				}
			});

			try {
				_systemTray.add(_trayIcon);
			} catch (AWTException e) {
				GUILogger.log(Level.SEVERE,
						"Systray enable failed for unknown reason");
				System.out.println(e.getMessage());
			}
		} else {
			GUILogger.log(Level.INFO, "Systray Unsupported");
		}
	}

	private void initUserInputArea() {
		_userInputArea = new JPanel();
		_frmTodo.getContentPane().add(_userInputArea, BorderLayout.SOUTH);
		_userInputArea.setLayout(new BorderLayout(0, 0));
		initUserPromptArea();
		initPromptSymbol();
		initPromptInputField();
	}

	private void initUserPromptArea() {
		_userPromptArea = new JPanel();
		_userPromptArea.setLayout(new BorderLayout(0, 0));
		_userInputArea.add(_userPromptArea, BorderLayout.SOUTH);

	}

	private void initPromptInputField() {
		_userInputField = new JTextField();
		_userInputField.addKeyListener(new InputProcessor());
		_userInputField.setCaretColor(Color.WHITE);
		_userInputField.setFocusTraversalKeysEnabled(false);
		_userInputField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				_userInputField.setText("");
			}
		});
		_userPromptArea.add(_userInputField, BorderLayout.CENTER);
		_userInputField.setCursor(Cursor
				.getPredefinedCursor(Cursor.TEXT_CURSOR));
		_userInputField.setBorder(null);
		_userInputField.setForeground(Color.WHITE);
		_userInputField.setBackground(new Color(0, 0, 0));
		_userInputField.setText("Start Typing Here....");
		_userInputField.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		_userInputField.setSize(20, 1);
		_userInputField.requestFocusInWindow();
	}

	private void initPromptSymbol() {
		_promptSymbol = new JTextField();
		_userPromptArea.add(_promptSymbol, BorderLayout.WEST);
		_promptSymbol.setEditable(false);
		_promptSymbol.setText(">");
		_promptSymbol.setForeground(Color.WHITE);
		_promptSymbol.setFont(new Font("Courier New", Font.PLAIN, 20));
		_promptSymbol.setColumns(1);
		_promptSymbol.setBorder(null);
		_promptSymbol.setBackground(Color.BLACK);
		_promptSymbol.setText(">");
	}

	private void initNotificationsArea() {
		_notificationsArea = new JPanel();
		_notificationsArea.setBackground(new Color(0, 0, 0));
		_frmTodo.getContentPane().add(_notificationsArea, BorderLayout.EAST);
		_notificationsArea.setLayout(new BorderLayout(0, 0));
	}

	private void initDateTimeArea() {
		_currentDateTimeArea = new JTextPane();
		_currentDateTimeArea.setFont(new Font("Courier New", Font.BOLD, 17));
		_currentDateTimeArea.setForeground(Color.WHITE);
		_currentDateTimeArea.setText("Fetching System Time...\n\n");
		_currentDateTimeArea.setBackground(new Color(0, 0, 0));

		_currentDateTimeArea.setEditable(false);

		_notificationsArea.add(_currentDateTimeArea, BorderLayout.NORTH);
	}

	private void initFloatingTaskView() {
		_floatingTaskView = new JTextPane();
		_floatingTaskView.setEditable(false);
		_floatingTaskView.setForeground(Color.WHITE);
		_floatingTaskView.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		_floatingTaskView.setBackground(Color.BLACK);
		_floatingTaskView.setAutoscrolls(false);
	}

	private void initDeadlineTaskView() {
		_deadlineTaskView = new JTextPane();
		_deadlineTaskView.setEditable(false);
		_deadlineTaskView.setForeground(Color.WHITE);
		_deadlineTaskView.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		_deadlineTaskView.setBackground(Color.BLACK);
		_deadlineTaskView.setAutoscrolls(false);

	}

	private void initTimedTaskView() {
		_timedTaskView = new JTextPane();
		_timedTaskView.setEditable(false);
		_timedTaskView.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		_timedTaskView.setForeground(Color.WHITE);
		_timedTaskView.setBackground(new Color(0, 0, 0));
		_timedTaskView.setAutoscrolls(false);
	}

	private void initFeedbackPane() {
		_feedbackPane = new JTextPane();
		_feedbackPane.setForeground(Color.YELLOW);
		_feedbackPane.setBackground(new Color(0, 0, 0));
		_feedbackPane.setFont(new Font(FONT_NAME, Font.PLAIN, 30));
		_feedbackPane.setEditable(false);
		_feedbackPane.setDisabledTextColor(Color.BLUE);
		if (!_displayState.getFeedback().getDisplay().equals("")) {
			_feedbackPane.setText(_displayState.getFeedback().getDisplay());
		}
		_userInputArea.add(_feedbackPane, BorderLayout.CENTER);
	}

	private void initMainViewArea() {
		_mainViewArea = new JPanel();
		_mainViewArea.setForeground(Color.GREEN);
		_mainViewArea.setBackground(new Color(0, 0, 0));

		TaskScrollPane = new JScrollPane(_mainViewArea);
		TaskScrollPane.setBorder(null);
		TaskScrollPane.setViewportBorder(null);
		TaskScrollPane
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		TaskScrollPane.setViewportView(_mainViewArea);
		TaskScrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());

		_frmTodo.getContentPane().add(TaskScrollPane, BorderLayout.CENTER);
		_mainViewArea.setLayout(new GridLayout(0, 1, 0, 0));
	}

	private void initMainWindow() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		_frmTodo = new JFrame();
		_frmTodo.setIconImage(_trayImage);
		_frmTodo.getContentPane().setForeground(new Color(0, 0, 0));
		_frmTodo.getContentPane().setBackground(new Color(0, 0, 0));
		_frmTodo.getContentPane().setLayout(new BorderLayout(0, 0));
		_frmTodo.setTitle("ToDo");
		_frmTodo.setBounds(1100, 0, 800, 850);
		_frmTodo.setLocationRelativeTo(null);
		// frmTodo.setLocationByPlatform(true);
		_frmTodo.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		_frmTodo.setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	private void updateTaskFields() {
		updateTimedTaskField();
		updateDeadlineTaskField();
		updateFloatingTaskField();
	}

	private void appendToPane(JTextPane textPane, String text,
			SimpleAttributeSet attributes) {
		StyledDocument document = textPane.getStyledDocument();

		try {
			document.insertString(document.getLength(), text, attributes);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private SimpleAttributeSet getHeadingAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.BLUE);
		StyleConstants.setBackground(attributes, Color.BLACK);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	private SimpleAttributeSet getEmptyAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.GRAY);
		StyleConstants.setBackground(attributes, Color.BLACK);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	private SimpleAttributeSet getBodyAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.WHITE);
		StyleConstants.setBackground(attributes, Color.BLACK);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	private SimpleAttributeSet getTagAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.YELLOW);
		StyleConstants.setBackground(attributes, Color.BLACK);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	private SimpleAttributeSet getFeedbackAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.YELLOW);
		StyleConstants.setBackground(attributes, Color.BLACK);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	private SimpleAttributeSet getExpiredAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.RED);
		StyleConstants.setBackground(attributes, Color.BLACK);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	private SimpleAttributeSet getCompletedAttributeSet() {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.GREEN);
		StyleConstants.setBackground(attributes, Color.BLACK);
		StyleConstants.setFontFamily(attributes, FONT_NAME);
		return attributes;
	}

	private void setUpAttributes() {

		_headerAttributes = getHeadingAttributeSet();
		_emptyAttributes = getEmptyAttributeSet();
		_bodyAttributes = getBodyAttributeSet();
		_tagAttributes = getTagAttributeSet();
		_feedbackTextAttributes = getFeedbackAttributeSet();
		completedAttributes = getCompletedAttributeSet();
		expiredAttributes = getExpiredAttributeSet();
	}

	private void updateTimedTaskField() {
		if (!_displayState.getTimedTasks().isEmpty()) {

			_timedTaskView.setText("");

			appendToPane(_timedTaskView, "Events :\n\n", _headerAttributes);
			String timedTaskText = "";
			String taskTags = "";
			String taskStart = "";
			String taskEnd = "";
			String taskNum = "";
			int index = 0;
			for (TimedTask task : _displayState.getTimedTasks()) {
				taskTags = "";
				timedTaskText = "";
				taskNum = "\t" + ++index + ". ";
				timedTaskText += task.getTaskDescription() + "\t";

				taskTags = task.getTagString() + "\n";
				taskStart = task.getStartString();
				taskEnd = task.getEndString();

				appendToPane(_timedTaskView, taskNum, _bodyAttributes);

				if (task.isEmpty()) {
					appendToPane(_timedTaskView, timedTaskText,
							_emptyAttributes);
				} else if (task.isComplete()) {
					appendToPane(_timedTaskView, timedTaskText,
							completedAttributes);
				} else if (task.isExpired()) {
					appendToPane(_timedTaskView, timedTaskText,
							expiredAttributes);
				} else {
					appendToPane(_timedTaskView, timedTaskText, _bodyAttributes);
				}

				appendToPane(_timedTaskView, taskTags, _tagAttributes);

				appendToPane(_timedTaskView, "\t\tfrom: ", _tagAttributes);
				appendToPane(_timedTaskView, taskStart + "\n", _bodyAttributes);

				appendToPane(_timedTaskView, "\t\tto:   ", _tagAttributes);
				appendToPane(_timedTaskView, taskEnd + "\n", _bodyAttributes);
			}

			_mainViewArea.add(_timedTaskView);
		} else if (_displayState.getTimedTasks().isEmpty()) {
			GUILogger.log(Level.INFO, "Timed Tasks are empty");
			_timedTaskView.setText("");
			_mainViewArea.remove(_timedTaskView);
		}
	}

	private void updateDeadlineTaskField() {
		if (!_displayState.getDeadlineTasks().isEmpty()) {

			_deadlineTaskView.setText("");

			appendToPane(_deadlineTaskView, "Deadlines :\n\n",
					_headerAttributes);
			String deadlineTaskText = "";
			String taskTags = "";
			String taskDeadline = "";
			String taskNum = "";

			int index = 0;
			for (DeadlineTask task : _displayState.getDeadlineTasks()) {
				taskTags = "";
				deadlineTaskText = "";
				taskNum = "\t" + (++index) + ". ";
				taskTags = task.getTagString() + "\n";
				taskDeadline = task.getDeadlineString();

				deadlineTaskText += task.getTaskDescription() + "\t";
				appendToPane(_deadlineTaskView, taskNum, _bodyAttributes);

				if (task.isComplete()) {
					appendToPane(_deadlineTaskView, deadlineTaskText,
							completedAttributes);
				} else if (task.isExpired()) {
					appendToPane(_deadlineTaskView, deadlineTaskText,
							expiredAttributes);
				} else {
					appendToPane(_deadlineTaskView, deadlineTaskText,
							_bodyAttributes);
				}

				appendToPane(_deadlineTaskView, taskTags, _tagAttributes);

				appendToPane(_deadlineTaskView, "\t\tby:   ", _tagAttributes);
				appendToPane(_deadlineTaskView, taskDeadline + "\n",
						_bodyAttributes);
			}

			_mainViewArea.add(_deadlineTaskView);
		} else if (_displayState.getDeadlineTasks().isEmpty()) {
			GUILogger.log(Level.INFO, "Deadline Tasks are empty");
			_deadlineTaskView.setText("");
			_mainViewArea.remove(_deadlineTaskView);
		}
	}

	private void updateFloatingTaskField() {
		if (!_displayState.getFloatingTasks().isEmpty()) {
			_floatingTaskView.setText("");

			appendToPane(_floatingTaskView, "Flexible Tasks :\n\n",
					_headerAttributes);
			String floatingTaskText = "";
			String taskTags = "";
			String taskNum = "";
			int index = 0;
			for (FloatingTask task : _displayState.getFloatingTasks()) {
				taskTags = "";
				floatingTaskText = "";
				taskNum = "\t" + (++index) + ". ";
				floatingTaskText += task.getTaskDescription() + "\t";
				appendToPane(_floatingTaskView, taskNum, _bodyAttributes);
				if (task.isComplete()) {
					appendToPane(_floatingTaskView, floatingTaskText,
							completedAttributes);
				} else {
					appendToPane(_floatingTaskView, floatingTaskText,
							_bodyAttributes);
				}
				taskTags = task.getTagString() + "\n";
				appendToPane(_floatingTaskView, taskTags, _tagAttributes);
			}

			_mainViewArea.add(_floatingTaskView);
		} else if (_displayState.getFloatingTasks().isEmpty()) {
			GUILogger.log(Level.INFO, "Floating Tasks are empty");
			_floatingTaskView.setText("");
			_mainViewArea.remove(_floatingTaskView);
		}
	}

	private static String getCurrentDisplayTime() {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
				"hh:mm:ss aa\nEEEEEEEEE\ndd MMMMMMMMMMM, yyyy");
		Calendar currTime = Calendar.getInstance();
		return dateTimeFormat.format(currTime.getTime());

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		_currentDateTimeArea.setText(getCurrentDisplayTime());
	}
}
