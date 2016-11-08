package commonUI;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;


public class watchFrame extends JFrame {
	private String ip;
    private static final long serialVersionUID = 1L;
    private String inetAddress;
    private static watchFrame frame;
    private boolean statu = false;
    private Screen sc;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
    public static watchFrame getFrame() {
    	if(frame == null)
    		frame = new watchFrame();
    	return frame;
    }
    public String getInetAddress() {
    	return inetAddress;
    }
    public boolean isVis() {
    	return statu;
    }
    public void setInetAddress(String ineladdress) {
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
        		   setStatu(false);
        	   }
        });
    }  
  
	public void setStatu(boolean statu) {
		this.statu = statu;
		setVisible(statu);
	}
	public void paint(Image cimage) {
		this.sc.setImage(cimage);
		this.sc.repaint();
	}
	public Dimension getScreenSize() {
		return screenSize;
	}
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
		setStatu(true);
		setTitle("远程监视 :" + ip);
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