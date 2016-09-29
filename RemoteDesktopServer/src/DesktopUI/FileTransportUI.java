package DesktopUI;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DesktopProcess.Server;


public class FileTransportUI {

}
class FileFrame {
	private Server server;
	private JFrame frm;
	private JButton open;
	private JButton confirm;
	private JTextField tf;
	private JPanel p, p2;
	private File f;
	private JFileChooser fc;
	private int flag;
	public FileFrame() {
		server = new Server();
		server.openServer();
		frm = new JFrame("文件传输");
		open = new JButton("选择文件");
		confirm = new JButton("确定");
		p = new JPanel();
		p2 = new JPanel();
		fc = new JFileChooser();
		tf = new JTextField("请选择文件", 20);
		
		frm.setSize(80, 50);
		Container c = frm.getContentPane();
		c.setLayout(new FlowLayout());
		//c.resize(80, 50);
		
		c.add(p2);
		c.add(p);
		p2.add(tf);
		p.add(open);
		p.add(confirm);
		
		open.addActionListener(new FileAction());
		confirm.addActionListener(new FileAction());
		
		frm.setSize(300, 300);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			tf.setText(f.getPath());
		}
	}
	
	private void confirmFile() {
		server.openSocket();
		System.out.println("loading......");
		server.send(tf.getText());
		setVis(false);
		System.out.println("Done......");
		//return tf.getText();
	}
	public String getFileName() {
		return tf.getText();
	}
//	public static void main(String[] args) {
//		new FileFrame();
//		//Server server = new Server();
////		if(server.openServer()) {
////			//String filepath = ff.getFileName();
////			server.send(filepath);
////			System.out.println("Done......");
////			server.closeSocket();
////		}
//	}
}