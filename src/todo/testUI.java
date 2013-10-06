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

public class testUI implements ActionListener {

	private JFrame frmTodo;
	protected static JTextArea _currentDateTimeArea = new JTextArea();
	private Timer timer;
	private JTextField UserInputField;
	private JTextField Prompt;
	private JPanel UserInputArea;

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
		MainViewArea.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel NotificationsArea = new JPanel();
		NotificationsArea.setBackground(new Color(0, 0, 0));
		frmTodo.getContentPane().add(NotificationsArea, BorderLayout.EAST);
		NotificationsArea.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		_currentDateTimeArea.setFont(new Font("Courier New", Font.PLAIN, 13));
		_currentDateTimeArea.setForeground(new Color(0, 153, 51));
		_currentDateTimeArea.setText("Fetching System Time...");
		_currentDateTimeArea.setBackground(new Color(0, 0, 0));

		_currentDateTimeArea.setEditable(false);

		// _currentDateTimeArea.setText(getCurrentDisplayTime());

		NotificationsArea.add(_currentDateTimeArea);
		
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
		UserInputField.setCaretColor(new Color(0, 153, 51));
		UserInputField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				UserInputField.setText("");
			}
		});
		UserInputArea.add(UserInputField, BorderLayout.CENTER);
		UserInputField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		UserInputField.setBorder(null);
		UserInputField.setForeground(new Color(0, 153, 51));
		UserInputField.setBackground(new Color(0, 0, 0));
		UserInputField.setText("Start Typing Here....");
		UserInputField.setFont(new Font("Courier New", Font.PLAIN, 15));
		UserInputField.setSize(20, 1);
		UserInputField.requestFocusInWindow();
		
		frmTodo.setTitle("ToDo");
		frmTodo.setBounds(1100, 0, 500, 850);
		//frmTodo.setLocationByPlatform(true);
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
