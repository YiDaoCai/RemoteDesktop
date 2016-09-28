package coustomer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.Component;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;

public class Camera extends Thread{
    private String fileName; //文件的前缀

    int serialNum = 0;

    private String imageFormat; //图像文件的格式

    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

   // 默认的文件前缀为GuiCamera，文件格式为PNG格式
    public Camera() {
    	
        fileName = "Camera";
        imageFormat = "jpg";
    }

    public Camera(String s, String format){
    	
        fileName = s;
        imageFormat = format;
    }

     // 对屏幕进行拍照
    public void run(){
    	FileOutputStream fos = null;
        try{
        	Robot robot = new Robot();
        	Rectangle screen_size = new Rectangle(0, 0,
        			(int) d.getWidth(), (int) d.getHeight());
        	while(serialNum <98) {  //控制次数
	            //拷贝屏幕到一个BufferedImage对象screenshot

	        	//Point p= MouseInfo.getPointerInfo().getLocation();
	            BufferedImage screenshot = robot
	                    .createScreenCapture(screen_size);
	            
	            
	            //根据文件前缀变量和文件格式变量，自动生成文件名
	            String name = fileName + String.valueOf(serialNum) + "."
	                    + imageFormat;
	            serialNum++;
	          //  File f = new File(name);
	            System.out.println("Save File " + name);  //
	            
	            //捕获鼠标并加到图片上
	           // BufferedImage cursor = ImageIO.read(new File("C:\\Windows\\Cursors\\aero_arrow.cur"));
	           // screenshot.createGraphics().drawImage(cursor, p.x, p.y, null);
	            
	            //将screenshot对象写入图像文件
	           // ImageIO.write(screenshot, imageFormat, f);
	            fos = new FileOutputStream(name);
	            JPEGCodec.createJPEGEncoder(fos).encode(screenshot);//图像编码成JPEG
	            fos.close();
	            Thread.sleep(40);//每秒25帧
        	}
            
           // System.out.println("..Finished! ");  //
            
        } catch (AWTException e) {
        	
            e.printStackTrace();  
        } catch (Exception ex){
        	
        	ex.printStackTrace();
        } finally {
        	try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public static void main(String[] args){
        System.out.println("打开连接");  //
        // String imagepath2 = ScreenSnapshot.class.getResource("/").getPath().toString();
        new Camera("D:\\records\\Hello", "jpg").start();
        new WnetWScreenRecordPlayer();
    }
}