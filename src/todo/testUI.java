package todo;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.Timer;

import java.awt.Color;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import java.awt.GridLayout;

public class testUI implements ActionListener {

	private JFrame frmTodo;
	protected static JTextArea _currentDateTimeArea = new JTextArea();
	private Timer timer;
	private JTextField UserInputField;
	private JTextField Prompt;
	private JPanel UserInputArea;
	private JTextPane TimedTaskView;
	private JTextPane DeadlineTaskView;
	private JTextPane FloatingTaskView;
	private JTextPane FeedbackPane;
	private JPanel MainViewArea;
	private ArrayList<String> previousInputs;
	private static CommandHandler _handler;
	private static State _displayState;
	private static int UP_KEYPRESS_COUNTER;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testUI window = new testUI();
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
	public testUI() {
		_handler = new CommandHandler();
		_displayState = new State();
		_displayState.setFeedback("Corrupted Previous State");
		try {
			_displayState = _handler.getCurrentState();
		} catch (Exception e) {
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

		initMainViewArea();

		initFeedbackPane();

		initTimedTaskView();

		initDeadlineTaskView();
		// MainViewArea.add(DeadlineTaskView);

		initFloatingTaskView();
		// MainViewArea.add(FloatingTaskView);

		updateTaskFields();
		initNotificationsArea();

		initUserInputArea();

		/*
		 * if (!TimedTaskView.getText().equals("")) {
		 * MainViewArea.add(TimedTaskView); }
		 * 
		 * if (!DeadlineTaskView.getText().equals("")) {
		 * MainViewArea.add(DeadlineTaskView); }
		 * 
		 * if (!FloatingTaskView.getText().equals("")) {
		 * MainViewArea.add(FloatingTaskView); }
		 */

		frmTodo.setTitle("ToDo");
		frmTodo.setBounds(1100, 0, 800, 850);
		frmTodo.setLocationRelativeTo(null);
		// frmTodo.setLocationByPlatform(true);
		frmTodo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void initUserInputArea() {
		UserInputArea = new JPanel();
		frmTodo.getContentPane().add(UserInputArea, BorderLayout.SOUTH);
		UserInputArea.setLayout(new BorderLayout(0, 0));

		Prompt = new JTextField();
		UserInputArea.add(Prompt, BorderLayout.WEST);
		Prompt.setEditable(false);
		Prompt.setText(">");
		Prompt.setForeground(Color.GREEN);
		Prompt.setFont(new Font("Courier New", Font.PLAIN, 15));
		Prompt.setColumns(1);
		Prompt.setBorder(null);
		Prompt.setBackground(Color.BLACK);
		Prompt.setText(">");

		UserInputField = new JTextField();
		UserInputField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String input = UserInputField.getText();
					if (input.equals("exit")) {
						System.exit(0);
					}

					_displayState = _handler.handleInput(input);
					UserInputField.setText("");
					FeedbackPane.setText(_displayState.getFeedback());
					previousInputs.add(input);
					UP_KEYPRESS_COUNTER = 1;
					updateTaskFields();
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (previousInputs.size() - UP_KEYPRESS_COUNTER >= 0) {
						UserInputField.setText(previousInputs
								.get(previousInputs.size()
										- UP_KEYPRESS_COUNTER++));
					} else {
						UP_KEYPRESS_COUNTER = 1;
						UserInputField.setText(previousInputs
								.get(previousInputs.size()
										- UP_KEYPRESS_COUNTER++));
					}
				}
			}
		});
		UserInputField.setCaretColor(Color.GREEN);
		UserInputField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				UserInputField.setText("");
			}
		});
		UserInputArea.add(UserInputField, BorderLayout.CENTER);
		UserInputField
				.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		UserInputField.setBorder(null);
		UserInputField.setForeground(Color.GREEN);
		UserInputField.setBackground(new Color(0, 0, 0));
		UserInputField.setText("Start Typing Here....");
		UserInputField.setFont(new Font("Courier New", Font.PLAIN, 15));
		UserInputField.setSize(20, 1);
		UserInputField.requestFocusInWindow();
	}

	private void initNotificationsArea() {
		JPanel NotificationsArea = new JPanel();
		NotificationsArea.setBackground(new Color(0, 0, 0));
		frmTodo.getContentPane().add(NotificationsArea, BorderLayout.EAST);
		NotificationsArea.setLayout(new BorderLayout(0, 0));

		_currentDateTimeArea.setFont(new Font("Courier New", Font.BOLD, 13));
		_currentDateTimeArea.setForeground(Color.GREEN);
		_currentDateTimeArea.setText("Fetching System Time...\n\n");
		_currentDateTimeArea.setBackground(new Color(0, 0, 0));

		_currentDateTimeArea.setEditable(false);

		NotificationsArea.add(_currentDateTimeArea, BorderLayout.EAST);
	}

	private void initFloatingTaskView() {
		FloatingTaskView = new JTextPane();
		FloatingTaskView.setEditable(false);
		FloatingTaskView.setForeground(Color.GREEN);
		FloatingTaskView.setFont(new Font("Book Antiqua", Font.BOLD, 15));
		FloatingTaskView.setBackground(Color.BLACK);
	}

	private void initDeadlineTaskView() {
		DeadlineTaskView = new JTextPane();
		DeadlineTaskView.setEditable(false);
		DeadlineTaskView.setForeground(Color.GREEN);
		DeadlineTaskView.setFont(new Font("Book Antiqua", Font.BOLD, 15));
		DeadlineTaskView.setBackground(Color.BLACK);
	}

	private void initTimedTaskView() {
		TimedTaskView = new JTextPane();
		TimedTaskView.setEditable(false);
		TimedTaskView.setFont(new Font("Book Antiqua", Font.BOLD, 15));
		TimedTaskView.setForeground(Color.GREEN);
		TimedTaskView.setBackground(new Color(0, 0, 0));
	}

	private void initFeedbackPane() {
		FeedbackPane = new JTextPane();
		FeedbackPane.setForeground(Color.YELLOW);
		FeedbackPane.setBackground(new Color(0, 0, 0));
		FeedbackPane.setFont(new Font("Times New Roman", Font.BOLD, 15));
		FeedbackPane.setEditable(false);
		FeedbackPane.setDisabledTextColor(Color.BLUE);
		if (!_displayState.getFeedback().equals("")) {
			FeedbackPane.setText(_displayState.getFeedback());
		}
		MainViewArea.add(FeedbackPane);
	}

	private void initMainViewArea() {
		MainViewArea = new JPanel();
		MainViewArea.setForeground(Color.GREEN);
		MainViewArea.setBackground(new Color(0, 0, 0));
		// frmTodo.getContentPane().add(MainViewArea, BorderLayout.WEST);
		JScrollPane TaskScrollPane = new JScrollPane(MainViewArea);
		TaskScrollPane.setBorder(null);
		TaskScrollPane.setViewportBorder(null);
		TaskScrollPane
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		frmTodo.getContentPane().add(TaskScrollPane, BorderLayout.CENTER);
		MainViewArea.setLayout(new GridLayout(0, 1, 0, 0));
	}

	private void initMainWindow() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		frmTodo = new JFrame();
		frmTodo.getContentPane().setForeground(new Color(0, 0, 0));
		frmTodo.getContentPane().setBackground(new Color(0, 0, 0));
		frmTodo.getContentPane().setLayout(new BorderLayout(0, 0));
	}

	private void updateTaskFields() {
		updateTimedTaskField();
		updateDeadlineTaskField();
		updateFloatingTaskField();
	}

	private void updateTimedTaskField() {
		if (!_displayState.getTimedTasks().isEmpty()) {
			String timedTaskText = "Timed Tasks :\n";
			for (TimedTask task : _displayState.getTimedTasks()) {
				timedTaskText += (task.toString() + "\n");
			}
			TimedTaskView.setText(timedTaskText);
			MainViewArea.add(TimedTaskView);
		} else if (_displayState.getTimedTasks().isEmpty()) {
			MainViewArea.remove(TimedTaskView);
			TimedTaskView.setText("");
		}
	}

	private void updateDeadlineTaskField() {
		if (!_displayState.getDeadlineTasks().isEmpty()) {
			String deadlineTaskText = "Deadline Tasks :\n";
			for (DeadlineTask task : _displayState.getDeadlineTasks()) {
				deadlineTaskText += (task.toString() + "\n");
			}
			DeadlineTaskView.setText(deadlineTaskText);
			MainViewArea.add(DeadlineTaskView);
		} else if (_displayState.getDeadlineTasks().isEmpty()) {
			DeadlineTaskView.setText("");
			MainViewArea.remove(DeadlineTaskView);
		}
	}

	private void updateFloatingTaskField() {
		if (!_displayState.getFloatingTasks().isEmpty()) {
			String floatingTaskText = "Floating Tasks :\n";
			for (FloatingTask task : _displayState.getFloatingTasks()) {
				floatingTaskText += (task.toString() + "\n");
			}
			FloatingTaskView.setText(floatingTaskText);
			MainViewArea.add(FloatingTaskView);
		} else if (_displayState.getFloatingTasks().isEmpty()) {
			FloatingTaskView.setText("");
			MainViewArea.remove(FloatingTaskView);
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
