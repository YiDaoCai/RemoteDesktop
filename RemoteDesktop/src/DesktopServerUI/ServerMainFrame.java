package DesktopServerUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import util.DesktopRemoteType;
import util.Information;

import DesktopServerProcess.Server;
import DesktopServerProcess.ServerThread;

public class ServerMainFrame extends JFrame {
	/**
	 * 
	 */
	private static final int total = 18;
	private static final long serialVersionUID = 1L;
	private static ServerMainFrame servermainframe;
	
	public static ServerMainFrame getFrame() {
		if(servermainframe == null) {
			servermainframe = new ServerMainFrame();
		}
		return servermainframe;
	}
	// NORTH
	private JButton setterBtn,prtScBtn,tranFileBtn,taskViewBtn,shutdown_upBtn;
	//private FileFrame fileframe;
	
	// EAST
	private JButton send;
	private JPanel row1, row2, row3;
	private JScrollPane sessionRoll, msgRoll, userRoll;
	private JTextArea session;
	private JTextArea msg;
	private JPanel onlineUserList;
	private JLabel onlineMember;
	
	// OTHER
	private FileFrame singleTranFileFrame ;
	public static JButton[] jp = new JButton[total];
	
	protected DesktopServerRaiseHandFrame raisehandframe;
	private ServerMainFrame() {
		super("YiDaoCai远程桌面监控");
		
		setSingleTranFileFrame(new FileFrame(null));
		raisehandframe = DesktopServerRaiseHandFrame.getFrame();
		
		
		
		JPanel chat = new JPanel(new BorderLayout());
		send = new JButton("发送");
		row1 = new JPanel(new BorderLayout());
		row2 = new JPanel(new BorderLayout());
		//row3 = new JPanel(new GridLayout(1, 2));
		
		onlineMember = new JLabel("当前在线人数：0");
		msg = new JTextArea(5, 25);
		msg.setTabSize(4);
		msg.setFont(new Font("宋体", Font.BOLD, 16));
		msg.setLineWrap(true);
		msg.setWrapStyleWord(true);
		
		session = new JTextArea(20, 25);
		session.setLineWrap(true);
		session.setWrapStyleWord(true);
		session.setFont(new Font("宋体", Font.BOLD, 16));
		session.append("欢迎进入聊天室\r\n");
		session.setEditable(false);
		sessionRoll = new JScrollPane(session);
		msgRoll = new JScrollPane(msg);
		
		chat.setSize(250, 560);
		row1.add(onlineMember, BorderLayout.NORTH);
		row1.add(sessionRoll, BorderLayout.CENTER);
		row2.add(msgRoll, BorderLayout.CENTER);
		row2.add(send, BorderLayout.SOUTH);
		//row3.add(send);
		chat.add(row1, BorderLayout.NORTH);
		chat.add(row2, BorderLayout.SOUTH);
		//chat.add(row3, BorderLayout.SOUTH);
		
		send.addActionListener(new Action());
		onlineUserList = new JPanel();
		onlineUserList.setLayout(new GridLayout((total-1)/3 + 1,3));//new GridLayout(18, 3)
		for(int i = 0; i < total;i++){
			jp[i] = new JButton();
			jp[i].setPreferredSize(new Dimension(200, 150));
			onlineUserList.add(jp[i]);
			//写个双击放大的
		}
		userRoll = new JScrollPane(onlineUserList);
		
		setterBtn = createBtn("设 置", "./image/set.png");
		prtScBtn = createBtn("监 控", "./image/prtSc.png");
		tranFileBtn = createBtn("传输文件", "./image/tranFile.png");
		taskViewBtn = createBtn("进程监控", "./image/taskView.png");
		shutdown_upBtn = createBtn("开关机", "./image/shutdown_up.png");
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		JPanel p = new JPanel(new GridLayout(1, 5));
		//tranFileBtn.addActionListener(new Action());
		
		
		p.add(setterBtn);
		p.add(prtScBtn);
		p.add(tranFileBtn);
		p.add(taskViewBtn);
		p.add(shutdown_upBtn);
		
		cp.add(p, BorderLayout.NORTH);
		cp.add(chat, BorderLayout.EAST);
		cp.add(userRoll, BorderLayout.CENTER);
		
		this.setSize(900, 640);
		this.setLocation(500,40);
		this.setResizable(false);
		this.setVisible(true);
		this.setBackground(Color.yellow);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void addBtn() {
		JButton jb = new JButton();
		jb.setPreferredSize(new Dimension(200,150));
		onlineUserList.add(jb);
	}
	
	public class Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == tranFileBtn) {
				singleTranFileFrame.setVis(true);
			} else if(e.getSource() == send) {
				Iterator<Entry<String, ServerThread>> iter = ServerThread.getUser();
				while(iter.hasNext()) {
					Map.Entry<String, ServerThread> val = iter.next();
					System.out.println("interface : " + val);
					val.getValue().sendMessage(Information.createSession("Server", msg.getText()));
				}
				addSession("[Server]" + msg.getText());
				msg.setText(null);
			}
		}
		
	}
	public void setOnlineMember(String value) {
		onlineMember.setText(value);
	}
	
	private JButton createBtn(String text, String icon) {
		ImageIcon ii = new ImageIcon(icon);
		Image image = ii.getImage(); 
		Image smallImage = image.getScaledInstance(64,64,Image.SCALE_FAST);
		JButton btn = new JButton(text, new ImageIcon(smallImage));
		btn.setUI(new BasicButtonUI());
		btn.setPreferredSize(new Dimension(64, 64));
		btn.setContentAreaFilled(false);
		btn.setFont(new Font("宋体", Font.PLAIN, 15));
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.addActionListener(new Action());
		//btn.addMouseListener(new MyMouseListener(this));
		return btn;
	}
	public void addSession(String content) {
		session.append(content + "\r\n");
	}

	public FileFrame getSingleTranFileFrame() {
		return singleTranFileFrame;
	}

	public void setSingleTranFileFrame(FileFrame singleTranFileFrame) {
		this.singleTranFileFrame = singleTranFileFrame;
	}
	public void showRaiseHand(String value) {
		try {
			raisehandframe.setText(value);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
