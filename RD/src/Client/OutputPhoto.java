package Client;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;

public class OutputPhoto extends Thread {
	private Dimension screenSize;
	private Rectangle rectangle;
	private Robot robot;

	public OutputPhoto() {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		rectangle = new Rectangle(screenSize);// 可以指定捕获屏幕区域
		try {
			robot = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public void run() {
		ZipOutputStream os = null;
		Socket socket = null;
		
		
		while (true) {
			System.out.println("尝试和服务器建立连接");
			try {
				socket = new Socket("172.00.00.1", 8888);// 连接远程IP
				System.out.println("建立连接成功");
			} catch (UnknownHostException e) {
	            System.out.println("连接失败，域名错误");
	        } catch(IOException e) {
	            System.out.println("连接失败，发生操作套接字异常");
	        } catch (Exception e) {
				e.printStackTrace();
	        }
			
			BufferedImage image = robot.createScreenCapture(rectangle);// 捕获制定屏幕矩形区域

			try {
				os = new ZipOutputStream(socket.getOutputStream());// 加入压缩流
				// os = new ZipOutputStream(new FileOutputStream("C:/1.zip"));
				os.putNextEntry(new ZipEntry("test.jpg"));
				os.setLevel(9);
				JPEGCodec.createJPEGEncoder(os).encode(image);// 图像编码成JPEG
				os.close();
				Thread.sleep(50);// 每秒20帧
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(InterruptedException e1){
				e1.printStackTrace();
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

	
	public static void main(String[] args) {
		new OutputPhoto().start();
	}
}

