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

public class testUI implements ActionListener {

	private JFrame frmTodo;
	protected static JTextArea _currentDateTimeArea = new JTextArea();
	private Timer timer;
	private JTextField UserInputField;
	private JTextField Prompt;
	private JPanel UserInputArea;
	private JTextPane textPane;

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
		initialize();
		timer = new Timer(1000, this);
		timer.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTodo = new JFrame();
		frmTodo.getContentPane().setForeground(new Color(0, 0, 0));
		frmTodo.getContentPane().setBackground(new Color(0, 0, 0));
		frmTodo.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel MainViewArea = new JPanel();
		MainViewArea.setForeground(new Color(0, 153, 51));
		MainViewArea.setBackground(new Color(0, 0, 0));
		frmTodo.getContentPane().add(MainViewArea, BorderLayout.WEST);
		MainViewArea.setLayout(new BorderLayout(0, 0));

		textPane = new JTextPane();
		textPane.setFont(new Font("Courier New", Font.BOLD, 15));
		textPane.setForeground(new Color(0, 153, 51));
		textPane.setBackground(new Color(0, 0, 0));
		MainViewArea.add(textPane);

		JPanel NotificationsArea = new JPanel();
		NotificationsArea.setBackground(new Color(0, 0, 0));
		frmTodo.getContentPane().add(NotificationsArea, BorderLayout.NORTH);
		NotificationsArea.setLayout(new BorderLayout(0, 0));

		_currentDateTimeArea.setFont(new Font("Courier New", Font.BOLD, 13));
		_currentDateTimeArea.setForeground(new Color(0, 153, 51));
		_currentDateTimeArea.setText("Fetching System Time...");
		_currentDateTimeArea.setBackground(new Color(0, 0, 0));

		_currentDateTimeArea.setEditable(false);

		// _currentDateTimeArea.setText(getCurrentDisplayTime());

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
					String currMainText = textPane.getText();
					currMainText += ("\n" + input);
					UserInputField.setText("");
					textPane.setText(currMainText);
					
					if(input.equals("exit")) {
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

		frmTodo.setTitle("ToDo");
		frmTodo.setBounds(1100, 0, 500, 850);
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
