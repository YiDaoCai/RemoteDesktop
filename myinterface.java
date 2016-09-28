import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class myinterface extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton btn1,btn2,btn3,btn4,btn5;
	public myinterface() {
		super("YiDaoCai远程桌面监控");
		btn1=new JButton("设置");
		btn2=new JButton("截屏");
		btn3=new JButton("传输文件");
		btn4=new JButton("进程监控");
		btn5=new JButton("开关机");
		Container cp=getContentPane();
		cp.setLayout(null);
		
		
		//设置按钮大小与边距
		btn1.setBounds(50,50,100,50);
		btn2.setBounds(200,50,100,50);
		btn3.setBounds(350,50,140,50);
		btn4.setBounds(540,50,140,50);
		btn5.setBounds(730,50,120,50);
		
		//设置按钮字体与大小
		btn1.setFont(new Font("宋体",Font.PLAIN,20));
		btn2.setFont(new Font("宋体",Font.PLAIN,20));
		btn3.setFont(new Font("宋体",Font.PLAIN,20));
		btn4.setFont(new Font("宋体",Font.PLAIN,20));
		btn5.setFont(new Font("宋体",Font.PLAIN,20));
		
		cp.add(btn1);
		cp.add(btn2);
		cp.add(btn3);
		cp.add(btn4);
		cp.add(btn5);
	}
	
	
	
	public static void main(String[] args)
	{
		myinterface frame=new myinterface();
		frame.setSize(1000,900);
		frame.setLocation(500,40);
		frame.setVisible(true);
		frame.setBackground(Color.yellow);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}


