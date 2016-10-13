package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Client.OutputPhoto;
import Srever.InputPhoto;
import Srever.server;

public class PanelFrame {
	private static final long serialVersionUID = 1L;
	private static final int MAXN = 54;
	private static int WIDTH = 800;
	private static int HEIGH = 640;
	private Dimension screenSize;
	public static JButton[] jp = new JButton[MAXN];
//	public JButton getButton(int ca){
//		return button[ca];
//	}
	public PanelFrame() {
		//JFrame jf = new JFrame();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//Container c = jf.getContentPane();
		//jf.setSize(WIDTH,HEIGH);
		JPanel jsp = new JPanel();
		jsp.setLayout(new GridLayout((int)Math.ceil(MAXN/3),3));//new FlowLayout(FlowLayout.LEFT,10,10)
		
		for(int i = 0; i < MAXN;i++){
			jp[i] = new JButton();
			jp[i].setPreferredSize(new Dimension(200,150));
			jsp.add(jp[i]);
			//写个双击放大的
		}
		JScrollPane scroll = new JScrollPane(jsp);
		
		//c.setVisible(true);
		//c.add(scroll,BorderLayout.CENTER);
		//jf.pack();
		
		//jf.setVisible(true);
		//jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//server ss = new server();
	}
	//
	
	public static void main(String[] args) {
//		
		new PanelFrame();
//		//new OutputPhoto().start();
//		//new OutputPhoto01().start();
//		
	}
}
