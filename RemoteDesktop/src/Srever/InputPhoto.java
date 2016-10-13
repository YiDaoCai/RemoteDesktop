package Srever;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import frame.PanelFrame;


public class InputPhoto extends JPanel implements Runnable {

	private Image cimage;
	int ca;
	Socket s;
	public InputPhoto(Socket s,int ca){
		this.ca = ca;
		this.s = s;
	}
	public synchronized void run() {
		
			//while (true) {
		try {
			
			ZipInputStream zis = new ZipInputStream(s.getInputStream());
			zis.getNextEntry();
			cimage = ImageIO.read(zis);// 把ZIP流转换为图片
			//缩小到适合按钮
			cimage = cimage.getScaledInstance(200,150,Image.SCALE_FAST);
			ImageIcon cim = new ImageIcon(cimage);
			PanelFrame.jp[ca].setIcon( cim);
			System.out.println("第"+ca+"个绘画成功");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(cimage, 0, 0, null);
	}

}


