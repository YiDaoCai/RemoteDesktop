package DesktopServerProcess;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import util.PCPanel;

import DesktopServerUI.ServerMainFrame;
import DesktopServerUI.watchFrame;

public class ServerShot extends Thread{  
	//private static Map<InetAddress, JButton> UserList = new HashMap<InetAddress, JButton>();
	private static Map<String, PCPanel> UserList = new HashMap<String, PCPanel>();
    Dimension screenSize;  
    
    public ServerShot() {  
        super();  
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
       
        Screen p = new Screen();   
        new Thread(p).start();   
    }  
  
    public static void main(String[] args) {  
        new ServerShot();  
    }  
    
    public static int getTotal() {
    	return UserList.size();
    }
//    public static JButton get(InetAddress ia) {
//    	return UserList.get(ia);
//    }
    public static PCPanel get(String string) {
    	return UserList.get(string);
    }
    class Screen extends JPanel implements Runnable {  
  
        private static final long serialVersionUID = 1L;  
        private Image cimage;  
  
        public synchronized void run() {  
            ServerSocket ss = null;  
            //int i = 0;
            try {  
                ss = new ServerSocket(5001);// 探听5001端口的连接  
                while (true) {  
                	//System.out.println(i++);
                    Socket s = null;  
                    try {  
                        s = ss.accept();
                        if(!UserList.containsKey(s.getInetAddress().getHostAddress())) {
                        	PCPanel jb = ServerMainFrame.addBtn();
                        	jb.setClient(s);
                        	UserList.put(s.getInetAddress().getHostAddress(), jb);
                        }
                        PCPanel jb = UserList.get(s.getInetAddress().getHostAddress());
                        jb.setVisible(true);
                        ZipInputStream zis = new ZipInputStream(s  
                                .getInputStream());  
                        zis.getNextEntry();  
                        cimage = ImageIO.read(zis);// 把ZIP流转换为图片
                        if(watchFrame.getFrame().isVis()) {
                        	if(s.getInetAddress().getHostAddress().equals(watchFrame.getFrame().getIp())) {
                        		int width = watchFrame.getFrame().getWidth();
                        		int height = watchFrame.getFrame().getHeight();
                        		cimage = cimage.getScaledInstance(width, height, Image.SCALE_FAST);
                        		watchFrame.getFrame().paint(cimage);
                        	}
                        } else {
                        	cimage = cimage.getScaledInstance(200,150,Image.SCALE_FAST);
                        	ImageIcon cim = new ImageIcon(cimage);
                        	jb.setIcon(cim);
                        }
                        
            			
                        //ImageIO.write((RenderedImage) cimage, "jpg", new File("D:\\records\\" + (new Date()).getTime() + ".jpg"));
                        //repaint();  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    } finally {  
                        if (s != null) {  
                            try {  
                                s.close();  
                            } catch (IOException e) {  
                                e.printStackTrace();  
                            }  
                        }  
                    }  
                }  
            } catch (Exception e) {  
            } finally {  
                if (ss != null) {  
                    try {  
                        ss.close();  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
  
        public Screen() {  
            super();  
            this.setLayout(null);  
        }  
   
    }  
}  