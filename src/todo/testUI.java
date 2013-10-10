package todo;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.Timer;

import java.awt.Window.Type;
import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JTextArea;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;

import java.awt.FlowLayout;

import javax.swing.JTextField;

import java.awt.Cursor;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;

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

	private static CommandHandler _handler;
	private static State _displayState;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testUI window = new testUI(_handler);
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
	public testUI(CommandHandler handler) {
		State _displayState = handler.getCurrentState();
		// StateStub stateGen = new StateStub();
		// State testState = stateGen.getStateStub();
		initialize(_displayState);
		timer = new Timer(1000, this);
		timer.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(State testState) {

		frmTodo = new JFrame();
		frmTodo.getContentPane().setForeground(new Color(0, 0, 0));
		frmTodo.getContentPane().setBackground(new Color(0, 0, 0));
		frmTodo.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel MainViewArea = new JPanel();
		MainViewArea.setForeground(new Color(0, 153, 51));
		MainViewArea.setBackground(new Color(0, 0, 0));
		// frmTodo.getContentPane().add(MainViewArea, BorderLayout.WEST);
		JScrollPane TaskScrollPane = new JScrollPane(MainViewArea);
		TaskScrollPane.setBorder(null);
		TaskScrollPane.setViewportBorder(null);
		TaskScrollPane
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		frmTodo.getContentPane().add(TaskScrollPane, BorderLayout.CENTER);
		MainViewArea.setLayout(new GridLayout(0, 1, 0, 0));

		FeedbackPane = new JTextPane();
		FeedbackPane.setForeground(new Color(0, 0, 204));
		FeedbackPane.setBackground(new Color(0, 0, 0));
		FeedbackPane.setFont(new Font("Tahoma", Font.BOLD, 15));
		FeedbackPane.setEditable(false);
		FeedbackPane.setDisabledTextColor(Color.BLUE);
		if (!testState.getFeedback().equals("")) {
			FeedbackPane.setText(testState.getFeedback());
			MainViewArea.add(FeedbackPane);
		}

		TimedTaskView = new JTextPane();
		TimedTaskView.setEditable(false);
		TimedTaskView.setFont(new Font("Courier New", Font.BOLD, 15));
		TimedTaskView.setForeground(new Color(0, 153, 51));
		TimedTaskView.setBackground(new Color(0, 0, 0));

		DeadlineTaskView = new JTextPane();
		DeadlineTaskView.setEditable(false);
		DeadlineTaskView.setForeground(new Color(0, 153, 51));
		DeadlineTaskView.setFont(new Font("Courier New", Font.BOLD, 15));
		DeadlineTaskView.setBackground(Color.BLACK);
		// MainViewArea.add(DeadlineTaskView);

		FloatingTaskView = new JTextPane();
		FloatingTaskView.setEditable(false);
		FloatingTaskView.setForeground(new Color(0, 153, 51));
		FloatingTaskView.setFont(new Font("Courier New", Font.BOLD, 15));
		FloatingTaskView.setBackground(Color.BLACK);
		// MainViewArea.add(FloatingTaskView);

		if (testState.getTimedTasks() != null) {
			String timedTaskText = "Timed Tasks :\n";
			for (TimedTask task : testState.getTimedTasks()) {
				timedTaskText += (task.toString() + "\n");
			}
			TimedTaskView.setText(timedTaskText);
		}
		if (testState.getDeadlineTasks() != null) {
			String deadlineTaskText = "Deadline Tasks :\n";
			for (DeadlineTask task : testState.getDeadlineTasks()) {
				deadlineTaskText += (task.toString() + "\n");
			}
			DeadlineTaskView.setText(deadlineTaskText);
		}
		if (testState.getFloatingTasks() != null) {
			String floatingTaskText = "Floating Tasks :\n";
			for (FloatingTask task : testState.getFloatingTasks()) {
				floatingTaskText += (task.toString() + "\n");
			}
			FloatingTaskView.setText(floatingTaskText);
		}
		JPanel NotificationsArea = new JPanel();
		NotificationsArea.setBackground(new Color(0, 0, 0));
		frmTodo.getContentPane().add(NotificationsArea, BorderLayout.EAST);
		NotificationsArea.setLayout(new BorderLayout(0, 0));

		_currentDateTimeArea.setFont(new Font("Courier New", Font.BOLD, 13));
		_currentDateTimeArea.setForeground(new Color(0, 153, 51));
		_currentDateTimeArea.setText("Fetching System Time...\n\n");
		_currentDateTimeArea.setBackground(new Color(0, 0, 0));

		_currentDateTimeArea.setEditable(false);

		NotificationsArea.add(_currentDateTimeArea, BorderLayout.EAST);

		UserInputArea = new JPanel();
		frmTodo.getContentPane().add(UserInputArea, BorderLayout.SOUTH);
		UserInputArea.setLayout(new BorderLayout(0, 0));

		Prompt = new JTextField();
		UserInputArea.add(Prompt, BorderLayout.WEST);
		Prompt.setEditable(false);
		Prompt.setText(">");
		Prompt.setForeground(new Color(0, 153, 51));
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
					_displayState = _handler.handleInput(input);
					/*
					 * Dummy Event test String currMainText =
					 * TimedTaskView.getText(); currMainText += ("\n" + input);
					 * UserInputField.setText("");
					 * TimedTaskView.setText(currMainText);
					 */
					if (input.equals("exit")) {
						System.exit(0);
					}
				}
			}
		});
		UserInputField.setCaretColor(new Color(0, 153, 51));
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
		UserInputField.setForeground(new Color(0, 153, 51));
		UserInputField.setBackground(new Color(0, 0, 0));
		UserInputField.setText("Start Typing Here....");
		UserInputField.setFont(new Font("Courier New", Font.PLAIN, 15));
		UserInputField.setSize(20, 1);
		UserInputField.requestFocusInWindow();

		if (!TimedTaskView.getText().equals("")) {
			MainViewArea.add(TimedTaskView);
		}

		if (!DeadlineTaskView.getText().equals("")) {
			MainViewArea.add(DeadlineTaskView);
		}

		if (!FloatingTaskView.getText().equals("")) {
			MainViewArea.add(FloatingTaskView);
		}

		frmTodo.setTitle("ToDo");
		frmTodo.setBounds(1100, 0, 800, 850);
		frmTodo.setLocationRelativeTo(null);
		// frmTodo.setLocationByPlatform(true);
		frmTodo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
