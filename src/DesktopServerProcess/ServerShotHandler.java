package DesktopServerProcess;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.sun.image.codec.jpeg.JPEGCodec;
import commonUI.watchFrame;

import util.DesktopRemoteType;
import util.Information;

import DesktopServerUI.PCPanel;
import DesktopServerUI.ServerMainFrame;

public class ServerShotHandler extends Thread{
	private static Map<String, PCPanel> UserList = new HashMap<String, PCPanel>();
	private Broadcast broadcast;
    private Thread last;
    private static ServerShotHandler servershot;
    public static ServerShotHandler getThread() {
    	if(servershot == null)
    		servershot = new ServerShotHandler();
    	return servershot;
    }
    private ServerShotHandler() {  
        super();  
        Toolkit.getDefaultToolkit().getScreenSize();  
        broadcast = new Broadcast();
        Screen p = new Screen();   
        new Thread(p).start();   
    }  
  
    public static void main(String[] args) {  
        new ServerShotHandler();  
    }  
    
    public static int getTotal() {
    	return UserList.size();
    }

    public static PCPanel get(String string) {
    	return UserList.get(string);
    }
    
    public void openBroadcast() {
    	if(last != null) {
    		closeBroadcast();
    	}
    	last = new Thread(broadcast);
    	last.start();
    }
	public void closeBroadcast() {
    	last.interrupt();
    }
    class Broadcast implements Runnable {
    	private Dimension screenSize;  
	    private Rectangle rectangle;  
	    private Robot robot;
	    public Broadcast() {
	    	screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
	        rectangle = new Rectangle(screenSize);// 可以指定捕获屏幕区域  
	        try {  
	            robot = new Robot();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            System.out.println(e);  
	        }
	    }
	    
		public synchronized void run() {
			Socket socket = null;
			try {
				while (true) {
					BufferedImage image = robot.createScreenCapture(rectangle);// 捕获制定屏幕矩形区域
					Iterator<Entry<String, ServerThread>> iter = ServerThread
							.getUser();
					while (iter.hasNext()) {
						Map.Entry<String, ServerThread> val = iter.next();
						socket = new Socket(val.getKey(),DesktopRemoteType.ClientShot.getPort());
						new Thread(new Send(socket, image)).start();
					}
					Thread.sleep(500);// 每秒2帧
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Iterator<Entry<String, ServerThread>> iter = ServerThread
						.getUser();
				while (iter.hasNext()) {
					Map.Entry<String, ServerThread> val = iter.next();
					val.getValue().sendMessage(new Information("boardcastclosed", "Server", "boardcastclosed", "all"));
				}
			}

		}

	    class Send implements Runnable {
	    	ZipOutputStream os = null;
	    	Socket socket;
	    	BufferedImage image;
	    	public Send(Socket s, BufferedImage i) {
	    		socket = s;
	    		image = i;
	    	}
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ZipOutputStream os = null;
				try {
					os = new ZipOutputStream(socket.getOutputStream());
					os.setLevel(9);  
	                os.putNextEntry(new ZipEntry("test.jpg"));  
	                JPEGCodec.createJPEGEncoder(os).encode(image);// 图像编码成JPEG  
	                os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
	                if (os != null) {  
	                    try {  
	                        os.close();  
	                    } catch (Exception ioe) {  
	                    }  
	                }  
	                if (socket != null) {
	                	
	                    try {  
	                        socket.close();  
	                    } catch (IOException e) {  
	                    }  
	                }  
	            }
			}
	    	
	    }
		
    }
    class Screen extends JPanel implements Runnable {  
  
        private static final long serialVersionUID = 1L;  
        private Image cimage;  
  
        public synchronized void run() {  
            ServerSocket ss = null;  
            //int i = 0;
            try {  
                ss = new ServerSocket(DesktopRemoteType.ServerShot.getPort()); 
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
                        if(!jb.isVisible()) jb.setVisible(true);
                        //jb.setVisible(true);
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