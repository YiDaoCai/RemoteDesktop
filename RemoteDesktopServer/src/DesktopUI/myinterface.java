package DesktopUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import DesktopProcess.Information;
import DesktopProcess.Server;
import DesktopProcess.ServerThread;

public class myinterface extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// NORTH
	private JButton setterBtn,prtScBtn,tranFileBtn,taskViewBtn,shutdown_upBtn;
	private FileFrame fileframe;
	
	// EAST
	private JButton send;
	private JPanel row1, row2, row3;
	private JScrollPane sessionRoll, msgRoll;
	private JTextArea session;
	private JTextArea msg;
	
	public myinterface() throws Exception {
		super("YiDaoCai远程桌面监控");
		
		JPanel chat = new JPanel(new BorderLayout());
		send = new JButton("发送");
		row1 = new JPanel();
		row2 = new JPanel();
		row3 = new JPanel(new GridLayout(1, 2));
		msg = new JTextArea(5, 25);
		msg.setTabSize(4);
		msg.setFont(new Font("宋体", Font.BOLD, 16));
		msg.setLineWrap(true);
		msg.setWrapStyleWord(true);
		
		session = new JTextArea(20, 25);
		session.setLineWrap(true);
		session.setWrapStyleWord(true);
		session.setFont(new Font("宋体", Font.BOLD, 16));
		session.append("欢迎来到聊天室\n");
		session.setEditable(false);
		sessionRoll = new JScrollPane(session);
		msgRoll = new JScrollPane(msg);
		
		chat.setSize(250, 560);
		//Container con = chat.getContentPane();
		//con.setLayout(new BorderLayout());
		row1.add(sessionRoll);
		row2.add(msgRoll);
		row3.add(send);
		chat.add(row1, BorderLayout.NORTH);
		chat.add(row2, BorderLayout.CENTER);
		chat.add(row3, BorderLayout.SOUTH);
		
		send.addActionListener(new Action());
		
		//chat.setResizable(false);
		//chat.setLocation(1050, 200);
		//chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//chat.setVisible(true);
		
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
		cp.add(chat, BorderLayout.EAST);
	}
	
	public class Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == tranFileBtn) {
				fileframe.setVis(true);
			} else if(e.getSource() == send) {
				Iterator<Entry<String, ServerThread>> iter = ServerThread.getUser();
				while(iter.hasNext()) {
					Map.Entry<String, ServerThread> val = iter.next();
					System.out.println("interface : " + val);
					val.getValue().send(Information.createSession("Server", msg.getText()));
				}
				addSession("[Server]" + msg.getText());
				msg.setText(null);
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
		ServerThread.setMainframe(frame);
		new Server().openSocket();
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
	public void addSession(String content) {
		session.append(content + "\n");
	}
}
