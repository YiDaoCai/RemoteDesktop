package DesktopServerUI;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;

import DesktopServerProcess.Server;
import DesktopServerProcess.ServerThread;

import util.Information;

public class taskViewFrame {
	
	private JFrame main;
	private JPanel list;
	private JScrollPane listRoll;
	private static taskViewFrame instance;
	private Object source;
	private String ip;
	private taskViewFrame() {
		main = new JFrame("进程监控");
		list = new JPanel(new GridLayout(0, 1));
		listRoll = new JScrollPane(list);
		Container c = main.getContentPane();
		
		c.add(listRoll);
		
		main.setSize(new Dimension(600, 800));
	}
	public void setIp(String Ip) {
		ip = Ip;
	}
	public static taskViewFrame getFrame() {
		if(instance == null) instance = new taskViewFrame();
		return instance;
	}
	
	private void add(String value) {
		list.add(new ProcessView(value));
	}
	
	public void view(Information task) {
		main.setTitle("进程监控 : " + task.getFromAdd());
		list.removeAll();
		list.add(new ProcessView());
		String content = task.getContent();
		String[] lines = content.split("\r\n");
		Arrays.sort(lines);
		for(String line:lines) {
			add(line);
		}
		main.setVisible(true);
	}
	
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}

	public class ProcessView extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JLabel name;
		private JLabel memory;
		private JButton over;
		public ProcessView(String value) {
			// TODO Auto-generated constructor stub
			super(new GridLayout(1, 3));
			String[] a = value.split("#");
			name = new JLabel(a[0]);
			memory = new JLabel(a[1]);
			over = new JButton("结束");
			over.addActionListener(new Action());
			
			add(name);
			add(memory);
			add(over);
		}
		
		public ProcessView() {
			// TODO Auto-generated constructor stub
			//jp = new JPanel(new FlowLayout());
			super(new GridLayout(1, 3));
			name = new JLabel("进程名称");
			memory = new JLabel("内存占用");
			JLabel over = new JLabel("结束进程");
			//over.addActionListener(new Action());
			
			add(name);
			add(memory);
			add(over);
		}
		public class Action implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ServerThread.getServerThread(ip).sendMessage(new Information("cmd", Server.getName(), "taskkill /F /IM " + name.getText() + " /T", false));
			}
			
		}
	}
}
