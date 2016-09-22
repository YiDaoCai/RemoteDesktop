package desktop;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame {

	private final static int  WIDTH = 400;
	private final static int HEIGTH = 200;
	private final static int X = 200;
	private final static int Y = 200;
	private JFrame frame = new JFrame("登录");
	private Container c = frame.getContentPane();
	private JTextField username = new JTextField();
	private JPasswordField password = new JPasswordField();
	private JButton ok = new JButton("确定");
	private JButton cancel = new JButton("取消");
	
	public Login() {
		frame.setSize(WIDTH,HEIGTH);
		frame.setLocation(X,Y);
		c.setLayout(new BorderLayout());
		initFrame();
		frame.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	void initFrame() {
		//顶部
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout());
		titlePanel.add(new Label("System Administrator Login"));
		c.add(titlePanel,BorderLayout.NORTH); //
		
		//中部
		JPanel  fieldPanel = new JPanel();
		fieldPanel.setLayout(null);
		JLabel L1 = new JLabel("Username:");
		L1.setBounds(80,20,100,20);
		JLabel L2 = new JLabel("Password:");
		L2.setBounds(80,60,100,20);
		fieldPanel.add(L1);
		fieldPanel.add(L2);
		username.setBounds(150,20,120,20);
		password.setBounds(150,60,120,20);
		fieldPanel.add(username);
		fieldPanel.add(password);
		c.add(fieldPanel,BorderLayout.CENTER);
		
		//底部按钮
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		c.add(buttonPanel,BorderLayout.SOUTH);
		
	}
	
	public static void main(String[] args){
		new Login();
	}
}
