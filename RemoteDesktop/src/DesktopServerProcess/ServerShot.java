package DesktopServerProcess;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import DesktopServerUI.ServerMainFrame;
import DesktopServerUI.watchFrame;

public class ServerShot extends Thread{  
	private static Map<InetAddress, JButton> UserList = new HashMap<InetAddress, JButton>();
    private static final long serialVersionUID = 1L;  
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
    class Screen extends JPanel implements Runnable {  
  
        private static final long serialVersionUID = 1L;  
        private Image cimage;  
  
        public synchronized void run() {  
            ServerSocket ss = null;  
            int i = 0;
            try {  
                ss = new ServerSocket(5001);// 探听5001端口的连接  
                while (true) {  
                	System.out.println(i++);
                    Socket s = null;  
                    try {  
                        s = ss.accept();
                        if(!UserList.containsKey(s.getInetAddress())) {
                        	JButton jb = ServerMainFrame.addBtn();
                        	UserList.put(s.getInetAddress(), jb);
                        	watchFrame.addBtnList(jb, s.getInetAddress());
                        }
                        JButton jb = UserList.get(s.getInetAddress());
                        ZipInputStream zis = new ZipInputStream(s  
                                .getInputStream());  
                        zis.getNextEntry();  
                        cimage = ImageIO.read(zis);// 把ZIP流转换为图片
                        if(watchFrame.isVis()) {
                        	if(s.getInetAddress().equals(watchFrame.getInetAddress())) {
                        		int width = watchFrame.getFrame().getWidth();
                        		int height = watchFrame.getFrame().getHeight();
                        		cimage = cimage.getScaledInstance(width, height, Image.SCALE_FAST);
                        		watchFrame.paint(cimage);
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