package DesktopServerUI;
//传文件的窗口(教师给学生)
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;

import CommonClass.DesktopRemoteType;




public class FileFrame {
	private static ArrayList<String> ips;
	private static FileFrame fileFrame;
	private JFrame frm;
	private JButton open;
	private JButton confirm;
	private JLabel filePathTF;
	private JPanel row1, row2;
	private File f;
	private WindowsFileChooser fc;
	
	private int flag;
	public static FileFrame getFrame() {
		if(fileFrame == null)
			fileFrame = new FileFrame();
		return fileFrame;
	}
	private FileFrame() {
		frm = new JFrame("发送文件");
		open = new JButton("选择文件");
		confirm = new JButton("确定");
		row1 = new JPanel();
		row2 = new JPanel();
		fc = new WindowsFileChooser();
		filePathTF = new JLabel("请选择文件");
		
		frm.setSize(80, 50);
		Container c = frm.getContentPane();
		c.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		c.add(row2);
		c.add(row1);
		row2.add(filePathTF);
		row1.add(open);
		row1.add(confirm);
		
		open.addActionListener(new FileAction());
		confirm.addActionListener(new FileAction());
		
		
		frm.setSize(300, 300);
		//frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void setIps(ArrayList<String> ip) {
		ips = ip;
	}
	public void setVis(boolean ok) {
		frm.setVisible(ok);
	}
	class FileAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == open) {
				openFile();
			} else if(e.getSource() == confirm) {
				confirmFile();
			}
		}
		
	}

	private void openFile() {
		fc.setDialogTitle("Open File");
		flag = fc.showOpenDialog(frm);
		if(flag == JFileChooser.APPROVE_OPTION) {
			f = fc.getSelectedFile();
			filePathTF.setText(f.getPath());
		}
	}
	
	private void confirmFile() {
		setVis(false);
		if(f.equals(null)) return;
		for(String ip:ips){
			new Thread(new RunSend(ip, f.getPath())).start();
		}
	}
	public String getFileName() {
		return filePathTF.getText();
	}
	public class RunSend implements Runnable {
		String ip;
		private DataInputStream fis;
		public RunSend(String IP, String path) {
			ip = IP;
			try {
				fis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Socket ss = new Socket(ip, DesktopRemoteType.ClientFile.getPort());
				DataOutputStream ps = new DataOutputStream(ss.getOutputStream());
				ps.writeUTF(f.getName());
	            ps.flush();
	            ps.writeLong((long) f.length());
	            ps.flush();
	
	            int bufferSize = 8192;
	            byte[] buf = new byte[bufferSize];
	
	            while (true) {
	                int read = 0;
	                if (fis != null) {
	                    read = fis.read(buf);
	                }
	
	                if (read <= 0) {
	                    break;
	                }
	                ps.write(buf, 0, read);
	            }
	            System.out.println("文件发送完成");
	            ps.flush();
	            ps.close();
	            
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public class WindowsFileChooser extends JFileChooser {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5196282709462572561L;

		public WindowsFileChooser() {
			String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SwingUtilities.updateComponentTreeUI(this);
		}
	}
}