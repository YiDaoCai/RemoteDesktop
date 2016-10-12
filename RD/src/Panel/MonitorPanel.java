package Panel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Client.OutputPhoto;
import Srever.InputPhoto;
//为了加进主界面，目前没用到
public class MonitorPanel extends JPanel {
	
	 public MonitorPanel(){
		super();
		this.setSize(400, 400);
		InputPhoto p = new InputPhoto();
		JPanel c = new JPanel();
		c.setLayout(new BorderLayout());
		c.add(p, SwingConstants.CENTER);
		new Thread(p).start();
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				setVisible(true);
			}});
	}

}
