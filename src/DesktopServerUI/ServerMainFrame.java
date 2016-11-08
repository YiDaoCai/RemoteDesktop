package DesktopServerUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.*;

import commonUI.watchFrame;

import util.Information;

import DesktopServerProcess.ServerShotHandler;
import DesktopServerProcess.ServerThread;

public class ServerMainFrame extends JFrame {
	/**
	 * 
	 */
	private static final int total = 30;
	private static final long serialVersionUID = 1L;
	private static ServerMainFrame servermainframe;
	static private ArrayList<String> ips = new ArrayList<String>();
	public static void ClearIp() {
		ips.clear();
	}
	public static void add(String ip) {
		ips.add(ip);
	}
	public static boolean checkboxes() {
		ClearIp();
		for(int i=0; i<total; i++) {
			if(user[i].isVisible() && user[i].isSelected()) add(user[i].getIp());
		}
		return ips.size() != 0;
	}
	public static ServerMainFrame getFrame() {
		if(servermainframe == null) {
			servermainframe = new ServerMainFrame();
		}
		return servermainframe;
	}
	// NORTH
	private JButton tranFileBtn,prtScBtn,shutdown_upBtn;// setterBtn  taskViewBtn
	//private FileFrame fileframe;
	
	// EAST
	private JButton send;
	private JPanel row1, row2; //, row3;
	private JScrollPane sessionRoll, msgRoll, userRoll;
	private JTextArea session;
	private JTextArea msg;
	private JPanel onlineUserList;
	private JLabel onlineMember;
	private JComboBox object;
	
	// OTHER
	private FileFrame singleTranFileFrame ;
	//public static JButton[] user = new JButton[total];
	public static PCPanel[] user = new PCPanel[total];
	private int statu = 0;
	protected DesktopServerRaiseHandFrame raisehandframe;
	private ServerMainFrame() {
		super("YiDaoCai远程桌面监控");
		
		setSingleTranFileFrame(FileFrame.getFrame());
		raisehandframe = DesktopServerRaiseHandFrame.getFrame();
		
		watchFrame.getFrame();
		
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
		object = new JComboBox();
		object.addItem("至全体");
		object.addItem("右键在钱的学生可私聊");
		
		chat.setSize(250, 560);
		row1.add(onlineMember, BorderLayout.NORTH);
		row1.add(sessionRoll, BorderLayout.CENTER);
		row2.add(object, BorderLayout.NORTH);
		row2.add(msgRoll, BorderLayout.CENTER);
		row2.add(send, BorderLayout.SOUTH);
		//row3.add(send);
		chat.add(row1, BorderLayout.NORTH);
		chat.add(row2, BorderLayout.SOUTH);
		//chat.add(row3, BorderLayout.SOUTH);
		
		send.addActionListener(new Action());
		onlineUserList = new JPanel();
		onlineUserList.setLayout(new GridLayout(10,3));//new GridLayout(18, 3)
		for(int i = 0; i < total;i++){
			user[i]  = new PCPanel();
			user[i].setPreferredSize(new Dimension(200, 150));
			onlineUserList.add(user[i]);
		}
		userRoll = new JScrollPane(onlineUserList);
		
		//setterBtn = createBtn("设 置", "./image/set.png");
		prtScBtn = createBtn("屏幕广播", "./bin/image/prtSc.png");
		tranFileBtn = createBtn("传输文件", "./bin/image/tranFile.png");
		//taskViewBtn = createBtn("进程监控", "./image/taskView.png");
		shutdown_upBtn = createBtn("关 机", "./bin/image/shutdown_up.png");
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		JPanel p = new JPanel(new GridLayout(1, 5));
		//tranFileBtn.addActionListener(new Action());
		
		
		//p.add(setterBtn);
		p.add(prtScBtn);
		p.add(tranFileBtn);
		//p.add(taskViewBtn);
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
	
	public static PCPanel addBtn() {
		PCPanel jb = user[ServerShotHandler.getTotal()];
		jb.setVisible(true);
		return jb;
	}
	public void setSelected(String ip) {
		//object.addItem(ip);
		if(object.getItemCount() > 2)
			object.removeItemAt(2);
		object.addItem(ip);
		object.setSelectedIndex(object.getItemCount() - 1);
	}
	public class Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == tranFileBtn) {
				if(checkboxes()) {
					FileFrame.setIps(ips);
					singleTranFileFrame.setVis(true);
				} else {
					javax.swing.JOptionPane.showMessageDialog(null, "您未选择任何计算机,不能进行文件传输!", "警告", JOptionPane.ERROR_MESSAGE);
				}
			} else if(e.getSource() == send) {
				if(msg.getText().length() < 1) return;
				if(object.getSelectedItem().equals("至全体")) {
					Iterator<Entry<String, ServerThread>> iter = ServerThread.getUser();
					while(iter.hasNext()) {
						Map.Entry<String, ServerThread> val = iter.next();
						System.out.println("interface : " + val);
						val.getValue().sendMessage(Information.createSession("ServerToEveryone", msg.getText(), "all"));
					}
					addSession("[ToEveryone]" + msg.getText());
				} else {
					ServerThread.getServerThread((String)object.getSelectedItem()).sendMessage(Information.createSession("Server", msg.getText(), (String)object.getSelectedItem()));
					addSession("[to" + (String)object.getSelectedItem() + "]" + msg.getText());
				}
				
				msg.setText(null);
			} else if(e.getSource() == prtScBtn) {
				statu = 1 - statu;
				if(statu == 1) {
					prtScBtn.setText("关闭广播");
					ServerShotHandler.getThread().openBroadcast();
				} else {
					prtScBtn.setText("屏幕广播");
					ServerShotHandler.getThread().closeBroadcast();
				}
			} else if(e.getSource() == shutdown_upBtn) {
				if(!checkboxes()) {
					javax.swing.JOptionPane.showMessageDialog(null, "您未选择任何计算机,不能关机!", "警告", JOptionPane.ERROR_MESSAGE);
				} else {
					for(String ip:ips) {
						ServerThread.getServerThread(ip).sendMessage(new Information("cmd", "Server", "shutdown -s -t 0", ip));
					}
				}
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
		
		btn.setPreferredSize(new Dimension(64, 64));
		btn.setFont(new Font("宋体", Font.PLAIN, 15));
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.addActionListener(new Action());
		return btn;
	}
	public void addSession(String content) {
		session.append(content + "\r\n");
		JScrollBar jsb = sessionRoll.getVerticalScrollBar();
		jsb.setValue(jsb.getMaximum() + 1);
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

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}
	
	
}
