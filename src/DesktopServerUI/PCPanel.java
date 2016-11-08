package DesktopServerUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import util.Information;

import commonUI.watchFrame;

import DesktopServerProcess.ServerThread;

@SuppressWarnings("serial")
public class PCPanel extends JPanel {
	private JButton scan;
	private JCheckBox ip;
	private Socket client;
	private JPopupMenu popup = new JPopupMenu();
	public PCPanel() {
		super(new BorderLayout());
		scan = new JButton();
		scan.setPreferredSize(new Dimension(200, 150));
		ip = new JCheckBox("10.0.0.1");
		add(scan, BorderLayout.CENTER);
		add(ip, BorderLayout.SOUTH);
		setVisible(false);
		createPopMenu();
		//scan.setIcon(new ImageIcon("D:\\receive\\Hello0.jpg"));
	}
	public void setIcon(ImageIcon ii) {
		scan.setIcon(ii);
	}
	public void setClient(Socket s) {
		setVisible(true);
		client = s;
		ip.setText(s.getInetAddress().getHostAddress());
	}
	public void addActionListener(ActionListener al) {
		scan.addActionListener(al);
	}
	public boolean isSelected() {
		return ip.isSelected();
	}
	public String getIp() {
		return ip.getText();
	}
	public void createPopMenu() {
		popup.add(new JMenuItem(new AbstractAction("发文件") {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ServerMainFrame.ClearIp();
				ServerMainFrame.add(client.getInetAddress().getHostAddress());
				FileFrame.getFrame().setVis(true);
			}
		}));
		popup.add(new JMenuItem(new AbstractAction("关机") {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ServerThread.getServerThread(ip.getText()).sendMessage(new Information("cmd", "Server", "shutdown -s -t 0", ip.getText()));
			}
		}));
		popup.add(new JMenuItem(new AbstractAction("进程监控") {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				taskViewFrame.getFrame().setIp(ip.getText());
				ServerThread.getServerThread(ip.getText()).sendMessage(new Information("tasklist", "Server", "tasklist", ip.getText()));
			}
		}));
		popup.add(new JMenuItem(new AbstractAction("对话") {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ServerMainFrame.getFrame().setSelected(ip.getText());
			}
		}));
		scan.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
			     int mods = e.getModifiers();
			     int iClickCount = e.getClickCount();

			     if ((mods & InputEvent.BUTTON3_MASK) != 0) {
			      // 判断是鼠标右键
			      // 弹出菜单
			      popup.show(e.getComponent(), e.getX(), e.getY());
			     } else if(iClickCount>=2) {      //判断是否是双击
			    	 watchFrame.getFrame().setIp(ip.getText());
			     }

			    }
		});
	}
}