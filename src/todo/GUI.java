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

import javax.swing.JTextArea;

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
	private static final String HELP_TEXT_2 = "add\ndelete\nview\nundo\nredo\nchange\nexit";
	private static final String HELP_TEXT_3 = "\n\nPress ";
	private static final String HELP_TEXT_4 = "tab ";
	private static final String HELP_TEXT_5 = "to \nauto-complete\n\nPress ";
	private static final String HELP_TEXT_6 = "arrow-up ";
	private static final String HELP_TEXT_7 = "to \ncycle through\nprevious commands\n\nPress ";
	private static final String HELP_TEXT_8 = "F3 ";
	private static final String HELP_TEXT_9 = "to minimize\nto/maximize from the\nSystem Tray";

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
		private static Clip _successClip;
		private static Clip _failureClip;
		private static URL _urlSuccess;
		private static URL _urlFailure;

		AudioFeedBack() {
			try {
				_urlSuccess = new URL("file:./src/sound/beep1.wav");
				_successClip = AudioSystem.getClip();
				_urlFailure = new URL("file:./src/sound/beep2.wav");
				_failureClip = AudioSystem.getClip();
				GUILogger.log(Level.INFO, "Audio Set Up is successful");
			} catch (MalformedURLException | LineUnavailableException e) {
				GUILogger.log(Level.WARNING, "Audio Set Up has failed");
				e.printStackTrace();
			}
			AudioInputStream ais = null;
			try {
				ais = AudioSystem.getAudioInputStream(_urlSuccess);
			} catch (UnsupportedAudioFileException | IOException e) {
				GUILogger.log(Level.WARNING, "Audio failed to stream");
				e.printStackTrace();
			}
			try {
				_successClip.open(ais);
			} catch (LineUnavailableException e) {
				GUILogger.log(Level.WARNING, "Audio file failed to open");
				e.printStackTrace();
			} catch (IOException e) {
				GUILogger.log(Level.WARNING, "Audio file failed to open");
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
		}

		protected void playSuccess() {
			_successClip.setFramePosition(_successClip.getFrameLength());
			_successClip.loop(1);
			GUILogger.log(Level.INFO, "Success Audio Played");
		}

		protected void playFailure() {
			_failureClip.setFramePosition(_failureClip.getFrameLength());
			_failureClip.loop(1);
			GUILogger.log(Level.INFO, "Failure Audio Played");
		}

	}

	private final class InputProcessor extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				GUILogger.log(Level.INFO, "Enter key pressed");
				String input = UserInputField.getText();
				if (input.equals("exit")) {
					System.exit(0);
				} else if (input.trim().equals("help")) {
					_helpPane.setText("");
					updateHelpPane();
					UserInputField.setText("");
				} else {
					_helpPane.setText("");
					appendToPane(_helpPane, HELP_PROMPT, headerAttributes);

					_displayState = _handler.handleInput(input);
					UserInputField.setText("");
					_autoComplete.updateState(_handler.getCurrentState());
					previousInputs.add(input);
					UP_KEYPRESS_COUNTER = 1;
					updateTaskFields();
					updateFeedbackPane();
					playAudioFeedback();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				GUILogger.log(Level.INFO, "Up key pressed");
				if (previousInputs.size() - UP_KEYPRESS_COUNTER >= 0) {
					UserInputField.setText(previousInputs.get(previousInputs
							.size()
							- UP_KEYPRESS_COUNTER++));
				} else {
					UP_KEYPRESS_COUNTER = 1;
					UserInputField.setText(previousInputs.get(previousInputs
							.size()
							- UP_KEYPRESS_COUNTER++));
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_TAB) {
				GUILogger.log(Level.INFO, "Tab key pressed");
				String current = UserInputField.getText();
				UserInputField.setText(_autoComplete.getSuggestion(current));
			}
		}
	}

	private JFrame frmTodo;
	private static JTextPane _currentDateTimeArea;
	private static JTextPane _helpPane;
	private static JPanel NotificationsArea;
	private Timer timer;
	private JTextField UserInputField;
	private JTextField PromptSymbol;
	private JPanel UserInputArea;
	private JPanel UserPromptArea;
	private JTextPane TimedTaskView;
	private JTextPane DeadlineTaskView;
	private JTextPane FloatingTaskView;
	private JTextPane FeedbackPane;
	private JPanel MainViewArea;
	private ArrayList<String> previousInputs;
	private SystemTray systemTray;
	private Image trayImage;
	private PopupMenu menu;
	private TrayIcon trayIcon;
	private static CommandHandler _handler;
	private static State _displayState;
	private static Suggestor _autoComplete;
	private static int UP_KEYPRESS_COUNTER;
	private JHotKeys shortcutKey;
	private SimpleAttributeSet headerAttributes;
	private SimpleAttributeSet bodyAttributes;
	private SimpleAttributeSet tagAttributes;
	private SimpleAttributeSet feedbackTextAttributes;

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
					window.frmTodo.setVisible(true);
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
		_displayState.setFeedback("Corrupted Previous State");
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
			previousInputs = new ArrayList<String>();
			timer = new Timer(1000, this);
			timer.start();
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

		initAudioFeedBack();
	}

	private void initAudioFeedBack() {
		audio = new AudioFeedBack();
	}

	private void playAudioFeedback() {
		if (audio == null) {
			return;
		}

		if (_displayState.getFeedback().trim().substring(0, 15)
				.equalsIgnoreCase("Invalid Command")) {
			audio.playFailure();
		} else {
			audio.playSuccess();
		}
	}

	private void initHelpPane() {
		_helpPane = new JTextPane();
		_helpPane.setBackground(Color.BLACK);
		_helpPane.setFont(new Font("Consolas", Font.PLAIN, 17));
		_helpPane.setEditable(false);
		appendToPane(_helpPane, HELP_PROMPT, headerAttributes);
		NotificationsArea.add(_helpPane, BorderLayout.CENTER);
	}

	private void setUpShortcutKey() {
		GUILogger.log(Level.INFO, "Shortcut Key Being Initialized");
		shortcutKey = new JHotKeys("./lib");
		if (System.getProperty("os.name").contains("Windows")) {
			GUILogger.log(Level.INFO, "Windows Detected");
			JIntellitype.setLibraryLocation("./lib/windows/JIntellitype.dll");
			try {
				shortcutKey.registerHotKey(0, 0, KeyEvent.VK_F3);
			} catch (Exception e) {
				GUILogger.log(Level.INFO,
						"Attempting to use 64 bit dll as 32 bit failed.");
				System.out.println("Error: " + e.getMessage()
						+ "\nAttempting Resolution..");
				JIntellitype
						.setLibraryLocation("./lib/windows/JIntellitype64.dll");
			}
		}
		shortcutKey.registerHotKey(0, 0, KeyEvent.VK_F3);
		JHotKeyListener hotkeyListener = new JHotKeyListener() {
			@Override
			public void onHotKey(int id) {
				GUILogger.log(Level.INFO, "Shortcut Trigerred");
				if (id == 0) {
					if (frmTodo.isShowing() == true) {
						frmTodo.dispose();
					} else {
						frmTodo.setVisible(true);
					}
				}
			}
		};
		shortcutKey.addHotKeyListener(hotkeyListener);
	}

	private void assignFocusToInput() {
		frmTodo.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				UserInputField.requestFocusInWindow();
			}
		});
	}

	private void updateFeedbackPane() {
		FeedbackPane.setText(_displayState.getFeedback());
		UserInputArea.add(FeedbackPane);
	}

	private void updateHelpPane() {
		appendToPane(_helpPane, HELP_TEXT_1, headerAttributes);
		appendToPane(_helpPane, HELP_TEXT_2, feedbackTextAttributes);
		appendToPane(_helpPane, HELP_TEXT_3, headerAttributes);
		appendToPane(_helpPane, HELP_TEXT_4, feedbackTextAttributes);
		appendToPane(_helpPane, HELP_TEXT_5, headerAttributes);
		appendToPane(_helpPane, HELP_TEXT_6, feedbackTextAttributes);
		appendToPane(_helpPane, HELP_TEXT_7, headerAttributes);
		appendToPane(_helpPane, HELP_TEXT_8, feedbackTextAttributes);
		appendToPane(_helpPane, HELP_TEXT_9, headerAttributes);
	}

	private void updateSystemTray() {
		GUILogger.log(Level.INFO, "Attempting to enable systray support");
		if (SystemTray.isSupported()) {
			systemTray = SystemTray.getSystemTray();

			trayImage = Toolkit.getDefaultToolkit().getImage(
					"./src/img/Two.jpg");

			menu = new PopupMenu();

			MenuItem pullUpItem = new MenuItem("Pull Up");
			pullUpItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frmTodo.setVisible(true);
				}
			});
			menu.add(pullUpItem);

			MenuItem exitItem = new MenuItem("Exit");
			exitItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			menu.add(exitItem);

			trayIcon = new TrayIcon(trayImage, "ToDo", menu);
			trayIcon.setImageAutoSize(true);

			trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					frmTodo.setVisible(true);
				}
			});

			try {
				systemTray.add(trayIcon);
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
		UserInputArea = new JPanel();
		frmTodo.getContentPane().add(UserInputArea, BorderLayout.SOUTH);
		UserInputArea.setLayout(new BorderLayout(0, 0));
		initUserPromptArea();
		initPromptSymbol();
		initPromptInputField();
	}

	private void initUserPromptArea() {
		UserPromptArea = new JPanel();
		UserPromptArea.setLayout(new BorderLayout(0, 0));
		UserInputArea.add(UserPromptArea, BorderLayout.SOUTH);

	}

	private void initPromptInputField() {
		UserInputField = new JTextField();
		UserInputField.addKeyListener(new InputProcessor());
		UserInputField.setCaretColor(Color.WHITE);
		UserInputField.setFocusTraversalKeysEnabled(false);
		UserInputField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				UserInputField.setText("");
			}
		});
		UserPromptArea.add(UserInputField, BorderLayout.CENTER);
		UserInputField
				.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		UserInputField.setBorder(null);
		UserInputField.setForeground(Color.WHITE);
		UserInputField.setBackground(new Color(0, 0, 0));
		UserInputField.setText("Start Typing Here....");
		UserInputField.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		UserInputField.setSize(20, 1);
		UserInputField.requestFocusInWindow();
	}

	private void initPromptSymbol() {
		PromptSymbol = new JTextField();
		UserPromptArea.add(PromptSymbol, BorderLayout.WEST);
		PromptSymbol.setEditable(false);
		PromptSymbol.setText(">");
		PromptSymbol.setForeground(Color.WHITE);
		PromptSymbol.setFont(new Font("Courier New", Font.PLAIN, 20));
		PromptSymbol.setColumns(1);
		PromptSymbol.setBorder(null);
		PromptSymbol.setBackground(Color.BLACK);
		PromptSymbol.setText(">");
	}

	private void initNotificationsArea() {
		NotificationsArea = new JPanel();
		NotificationsArea.setBackground(new Color(0, 0, 0));
		frmTodo.getContentPane().add(NotificationsArea, BorderLayout.EAST);
		NotificationsArea.setLayout(new BorderLayout(0, 0));

	}

	private void initDateTimeArea() {
		_currentDateTimeArea = new JTextPane();
		_currentDateTimeArea.setFont(new Font("Courier New", Font.BOLD, 17));
		_currentDateTimeArea.setForeground(Color.WHITE);
		_currentDateTimeArea.setText("Fetching System Time...\n\n");
		_currentDateTimeArea.setBackground(new Color(0, 0, 0));

		_currentDateTimeArea.setEditable(false);

		NotificationsArea.add(_currentDateTimeArea, BorderLayout.NORTH);
	}

	private void initFloatingTaskView() {
		FloatingTaskView = new JTextPane();
		FloatingTaskView.setEditable(false);
		FloatingTaskView.setForeground(Color.WHITE);
		FloatingTaskView.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		FloatingTaskView.setBackground(Color.BLACK);
		FloatingTaskView.setAutoscrolls(false);
	}

	private void initDeadlineTaskView() {
		DeadlineTaskView = new JTextPane();
		DeadlineTaskView.setEditable(false);
		DeadlineTaskView.setForeground(Color.WHITE);
		DeadlineTaskView.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		DeadlineTaskView.setBackground(Color.BLACK);
		DeadlineTaskView.setAutoscrolls(false);

	}

	private void initTimedTaskView() {
		TimedTaskView = new JTextPane();
		TimedTaskView.setEditable(false);
		TimedTaskView.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		TimedTaskView.setForeground(Color.WHITE);
		TimedTaskView.setBackground(new Color(0, 0, 0));
		TimedTaskView.setAutoscrolls(false);

	}

	private void initFeedbackPane() {
		FeedbackPane = new JTextPane();
		FeedbackPane.setForeground(Color.YELLOW);
		FeedbackPane.setBackground(new Color(0, 0, 0));
		FeedbackPane.setFont(new Font(FONT_NAME, Font.PLAIN, 30));
		FeedbackPane.setEditable(false);
		FeedbackPane.setDisabledTextColor(Color.BLUE);
		if (!_displayState.getFeedback().equals("")) {
			FeedbackPane.setText(_displayState.getFeedback());
		}
		UserInputArea.add(FeedbackPane, BorderLayout.CENTER);
	}

	private void initMainViewArea() {
		MainViewArea = new JPanel();
		MainViewArea.setForeground(Color.GREEN);
		MainViewArea.setBackground(new Color(0, 0, 0));

		JScrollPane TaskScrollPane = new JScrollPane(MainViewArea);
		TaskScrollPane.setBorder(null);
		TaskScrollPane.setViewportBorder(null);
		TaskScrollPane
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		TaskScrollPane.setViewportView(MainViewArea);
		TaskScrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());

		frmTodo.getContentPane().add(TaskScrollPane, BorderLayout.CENTER);
		MainViewArea.setLayout(new GridLayout(0, 1, 0, 0));
	}

	private void initMainWindow() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		frmTodo = new JFrame();
		frmTodo.setIconImage(trayImage);
		frmTodo.getContentPane().setForeground(new Color(0, 0, 0));
		frmTodo.getContentPane().setBackground(new Color(0, 0, 0));
		frmTodo.getContentPane().setLayout(new BorderLayout(0, 0));
		frmTodo.setTitle("ToDo");
		frmTodo.setBounds(1100, 0, 800, 850);
		frmTodo.setLocationRelativeTo(null);
		// frmTodo.setLocationByPlatform(true);
		frmTodo.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		frmTodo.setExtendedState(Frame.MAXIMIZED_BOTH);
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

		headerAttributes = getHeadingAttributeSet();
		bodyAttributes = getBodyAttributeSet();
		tagAttributes = getTagAttributeSet();
		feedbackTextAttributes = getFeedbackAttributeSet();
		completedAttributes = getCompletedAttributeSet();
		expiredAttributes = getExpiredAttributeSet();
	}

	private void updateTimedTaskField() {
		if (!_displayState.getTimedTasks().isEmpty()) {

			TimedTaskView.setText("");

			appendToPane(TimedTaskView, "Events :\n\n", headerAttributes);
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

				appendToPane(TimedTaskView, taskNum, bodyAttributes);

				if (task.isComplete()) {
					appendToPane(TimedTaskView, timedTaskText,
							completedAttributes);
				} else if (task.isExpired()) {
					appendToPane(TimedTaskView, timedTaskText,
							expiredAttributes);
				} else {
					appendToPane(TimedTaskView, timedTaskText, bodyAttributes);
				}

				appendToPane(TimedTaskView, taskTags, tagAttributes);

				appendToPane(TimedTaskView, "\t\tfrom: ", tagAttributes);
				appendToPane(TimedTaskView, taskStart + "\n", bodyAttributes);

				appendToPane(TimedTaskView, "\t\tto:   ", tagAttributes);
				appendToPane(TimedTaskView, taskEnd + "\n", bodyAttributes);
			}

			MainViewArea.add(TimedTaskView);
		} else if (_displayState.getTimedTasks().isEmpty()) {
			MainViewArea.remove(TimedTaskView);
			TimedTaskView.setText("");
		}
	}

	private void updateDeadlineTaskField() {
		if (!_displayState.getDeadlineTasks().isEmpty()) {

			DeadlineTaskView.setText("");

			appendToPane(DeadlineTaskView, "Deadlines :\n\n", headerAttributes);
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
				appendToPane(DeadlineTaskView, taskNum, bodyAttributes);

				if (task.isComplete()) {
					appendToPane(DeadlineTaskView, deadlineTaskText,
							completedAttributes);
				} else if (task.isExpired()) {
					appendToPane(DeadlineTaskView, deadlineTaskText,
							expiredAttributes);
				} else {
					appendToPane(DeadlineTaskView, deadlineTaskText,
							bodyAttributes);
				}

				appendToPane(DeadlineTaskView, taskTags, tagAttributes);

				appendToPane(DeadlineTaskView, "\t\tby:   ", tagAttributes);
				appendToPane(DeadlineTaskView, taskDeadline + "\n",
						bodyAttributes);
			}

			MainViewArea.add(DeadlineTaskView);
		} else if (_displayState.getDeadlineTasks().isEmpty()) {
			MainViewArea.remove(DeadlineTaskView);
			DeadlineTaskView.setText("");
		}
	}

	private void updateFloatingTaskField() {
		if (!_displayState.getFloatingTasks().isEmpty()) {
			FloatingTaskView.setText("");

			appendToPane(FloatingTaskView, "Flexible Tasks :\n\n",
					headerAttributes);
			String floatingTaskText = "";
			String taskTags = "";
			String taskNum = "";
			int index = 0;
			for (FloatingTask task : _displayState.getFloatingTasks()) {
				taskTags = "";
				floatingTaskText = "";
				taskNum = "\t" + (++index) + ". ";
				floatingTaskText += task.getTaskDescription() + "\t";
				appendToPane(FloatingTaskView, taskNum, bodyAttributes);
				if (task.isComplete()) {
					appendToPane(FloatingTaskView, floatingTaskText,
							completedAttributes);
				} else {
					appendToPane(FloatingTaskView, floatingTaskText,
							bodyAttributes);
				}
				taskTags = task.getTagString() + "\n";
				appendToPane(FloatingTaskView, taskTags, tagAttributes);
			}

			MainViewArea.add(FloatingTaskView);
		} else if (_displayState.getFloatingTasks().isEmpty()) {
			MainViewArea.remove(FloatingTaskView);
			FloatingTaskView.setText("");
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
