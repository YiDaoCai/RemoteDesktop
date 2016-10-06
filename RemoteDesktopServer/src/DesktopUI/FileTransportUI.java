package DesktopUI;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sun.org.apache.xerces.internal.dom.PSVIDOMImplementationImpl;

import DesktopProcess.ServerThread;


public class FileTransportUI {

}
class FileFrame {
	private ServerThread server;
	private JFrame frm;
	private JButton open;
	private JButton confirm;
	private JLabel filePathTF;
	private JPanel row1, row2;
	private File f;
	private JFileChooser fc;
	private int flag;
	public FileFrame(ServerThread server) {
		this.server = server;
		frm = new JFrame("文件传输");
		open = new JButton("选择文件");
		confirm = new JButton("确定");
		row1 = new JPanel();
		row2 = new JPanel();
		fc = new JFileChooser();
		filePathTF = new JLabel("请选择文件");
		
		frm.setSize(80, 50);
		Container c = frm.getContentPane();
		c.setLayout(new FlowLayout(FlowLayout.CENTER));
		//c.resize(80, 50);
		
		c.add(row2);
		c.add(row1);
		row2.add(filePathTF);
		row1.add(open);
		row1.add(confirm);
		
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
			filePathTF.setText(f.getPath());
		}
	}
	
	private void confirmFile() {
		server.send(filePathTF.getText());
		setVis(false);
	}
	public String getFileName() {
		return filePathTF.getText();
	}
//	public static void main(String[] args) {
//		new FileFrame().setVis(true);
//	}
}