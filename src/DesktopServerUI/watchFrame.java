package DesktopServerUI;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class watchFrame extends JFrame {
	private static Map<JButton, InetAddress> btnList = new HashMap<JButton, InetAddress>();
    private static final long serialVersionUID = 1L;  
    private static InetAddress inetAddress;
    private static watchFrame frame;
    private static boolean statu = false;
    private static Screen sc;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
    public static watchFrame getFrame() {
    	if(frame == null)
    		frame = new watchFrame();
    	return frame;
    }
    public static InetAddress getAddress(Object jb) {
    	return btnList.get((JButton)jb);
    }
    public static void addBtnList(JButton key, InetAddress value) {
    	btnList.put(key, value);
    }
    public static InetAddress getInetAddress() {
    	return inetAddress;
    }
    public static boolean isVis() {
    	return statu;
    }
    public static void setInetAddress(InetAddress ineladdress) {
    	inetAddress = ineladdress;
    }
    private watchFrame() {  
        super();
        this.setSize(screenSize.width*8/10, screenSize.height*8/10);
        sc = new Screen();  
        Container c = this.getContentPane();  
        c.setLayout(new BorderLayout());  
        c.add(sc, SwingConstants.CENTER);  
        this.addWindowListener(new WindowAdapter(){
        	   public void windowClosing(WindowEvent we){
        		   statu = false;
        	   }
        });
    }  
  
	public static void setStatu(boolean statu) {
		watchFrame.statu = statu;
	}
	public static void paint(Image cimage) {
		sc.setImage(cimage);
		sc.repaint();
	}
	public static Dimension getScreenSize() {
		return screenSize;
	}
	class Screen extends JPanel  {  
  
        private static final long serialVersionUID = 1L;  
        private Image cimage;  
        
        public void setImage(Image cimage) {
        	this.cimage = cimage;
        }
        public Screen() {  
            super();  
            this.setLayout(null);  
        }  
  
        public void paint(Graphics g) {  
            super.paint(g);  
            Graphics2D g2 = (Graphics2D) g;  
            g2.drawImage(cimage, 0, 0, null);  
        }  
    }  
}  