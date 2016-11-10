package DesktopClientProcess;
//学生端发送图片流
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;  
import java.net.*;  
import java.util.zip.*;

import javax.imageio.ImageIO;


import CommonClass.DesktopRemoteType;

import com.sun.image.codec.jpeg.JPEGCodec;
import commonUI.watchFrame;
  
public class ClientShot extends Thread {  
    private Dimension screenSize;  
    private Rectangle rectangle;  
    private Robot robot;
    private ServerSocket shotsocket;
    private watchFrame BCFrame;
    public ClientShot() {  
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
        rectangle = new Rectangle(screenSize);// 可以指定捕获屏幕区域 
        BCFrame = watchFrame.getFrame();
        BCFrame.setTitle("教师");
        BCFrame.setSize(screenSize);
        BCFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        //BCFrame.setResizable(false);
        try {  
            robot = new Robot();
            shotsocket = new ServerSocket(DesktopRemoteType.ClientShot.getPort());
            new Boardcast().start();
            this.start();
        } catch (Exception e) {  
            e.printStackTrace();  
            System.out.println(e);  
        }  
    } 
    
    public synchronized void run() {  
        ZipOutputStream os = null;  
        Socket socket = null;  
        while (true) {  
            try {  
                socket = new Socket(Client.getServerAddress(), DesktopRemoteType.ServerShot.getPort());// 连接远程IP  
                BufferedImage image = robot.createScreenCapture(rectangle);// 捕获制定屏幕矩形区域  
                os = new ZipOutputStream(socket.getOutputStream());// 加入压缩流 
  
                os.setLevel(9);  
                os.putNextEntry(new ZipEntry("test.jpg"));  
                JPEGCodec.createJPEGEncoder(os).encode(image);// 图像编码成JPEG  
                os.close();  
                Thread.sleep(500);// 每秒2帧  
            } catch (Exception e) {  
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
    public class Boardcast extends Thread {
		/**
		 * @return 接收来自服务端的屏幕广播
		 */
		public void run() {
			Socket client;
			Image cimage;
			while (true) {  
                try {  
                	client = shotsocket.accept();
                	watchFrame.getFrame().setVisible(true);
                    ZipInputStream zis = new ZipInputStream(client.getInputStream());  
                    zis.getNextEntry();  
                    cimage = ImageIO.read(zis);// 把ZIP流转换为图片
                    int width = watchFrame.getFrame().getWidth();
                    int height = watchFrame.getFrame().getHeight();
                    cimage = cimage.getScaledInstance(width, height, Image.SCALE_FAST);
                    watchFrame.getFrame().paint(cimage);
                } catch (Exception e) {  
                    e.printStackTrace();  
                }   
            }  
		}
	}
    
}  