package coustomer;

import java.awt.*;

import javax.swing.*;

import java.io.*;

public class WnetWScreenRecordPlayer extends JFrame {
	BorderLayout borderLayout1 = new BorderLayout();
	Dimension screenSize;

	@SuppressWarnings("deprecation")
	public WnetWScreenRecordPlayer() {
		super();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize);
		Screen p = new Screen();
		Container c = this.getContentPane();
		c.setLayout(borderLayout1);
		c.add(p, "Center");
		new Thread(p).start();
		this.show();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

//	public static void main(String[] args) {
//		new WnetWScreenRecordPlayer();
//	}

}

class Screen extends JPanel implements Runnable {
	private BorderLayout borderLayout1 = new BorderLayout();
	private Image cimage;
	int serialNum = 0;
	
	public Screen() {
		super();
		this.setLayout(null);
	}
	
	public void run() {
		
		while (serialNum <98) {
			try {
				cimage = loadImage(serialNum + ".jpg");
				serialNum++;
				repaint();
				Thread.sleep(200);// 与录像时每秒帧数一致

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
		}
	}
	Image image = null;
	public Image loadImage(String name) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.getImage("D:\\records\\Hello" + name);
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(image, 0);
		try {
			mt.waitForID(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return image;
	}

	

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(cimage, 0, 0, null);
	}
}
