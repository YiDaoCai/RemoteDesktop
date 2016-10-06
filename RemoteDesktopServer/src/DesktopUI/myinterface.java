package DesktopUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javafx.scene.layout.Border;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.IconView;

import sun.awt.IconInfo;

public class myinterface extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton setterBtn,prtScBtn,tranFileBtn,taskViewBtn,shutdown_upBtn;
	FileFrame fileframe;
	public myinterface() throws Exception {
		super("YiDaoCai远程桌面监控");
		//fileframe = new FileFrame();
		//setterBtn=new JButton("设置");
		setterBtn = createBtn("设置", "./image/set.png");
		prtScBtn=new JButton("截屏");
		tranFileBtn=new JButton("传输文件");
		taskViewBtn=new JButton("进程监控");
		shutdown_upBtn=new JButton("开关机");
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		JPanel p = new JPanel(new GridLayout(1, 5));
		//设置按钮监听
		//p.preferredSize();
		tranFileBtn.addActionListener(new Action());
		
		//设置按钮大小与边距
		//setterBtn.setBounds(50,50,100,50);
		//prtScBtn.setBounds(200,50,100,50);
		//tranFileBtn.setBounds(350,50,140,50);
		//taskViewBtn.setBounds(540,50,140,50);
		//shutdown_upBtn.setBounds(730,50,120,50);
		
		//设置按钮字体与大小
		setterBtn.setFont(new Font("宋体",Font.PLAIN,20));
		prtScBtn.setFont(new Font("宋体",Font.PLAIN,20));
		tranFileBtn.setFont(new Font("宋体",Font.PLAIN,20));
		taskViewBtn.setFont(new Font("宋体",Font.PLAIN,20));
		shutdown_upBtn.setFont(new Font("宋体",Font.PLAIN,20));
		
		p.add(setterBtn);
		p.add(prtScBtn);
		p.add(tranFileBtn);
		p.add(taskViewBtn);
		p.add(shutdown_upBtn);
		
		cp.add(p, BorderLayout.NORTH);
	}
	
	public class Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == tranFileBtn) {
				fileframe.setVis(true);
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception
	{
		myinterface frame = new myinterface();
		frame.setSize(1000,900);
		frame.setLocation(500,40);
		frame.setVisible(true);
		frame.setBackground(Color.yellow);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JButton createBtn(String text, String icon) {
		ImageIcon ii = new ImageIcon(icon);
		Image image = ii.getImage(); 
		Image smallImage = image.getScaledInstance(64,64,Image.SCALE_FAST);
		JButton btn = new JButton(text, new ImageIcon(smallImage));
		btn.setUI(new BasicButtonUI());// 恢复基本视觉效果
		btn.setPreferredSize(new Dimension(64, 64));// 设置按钮大小
		btn.setContentAreaFilled(false);// 设置按钮透明
		btn.setFont(new Font("粗体", Font.PLAIN, 15));// 按钮文本样式
		btn.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离
		//btn.addMouseListener(new MyMouseListener(this));
		return btn;
	}
}
