package DesktopServerProcess;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



public class ServerFileThread {
    private static Map<InetAddress, ServerFileThread> UserList = new HashMap<InetAddress, ServerFileThread>();
    //private BufferedReader is;
    private Socket socket;
	public ServerFileThread(Socket c) throws IOException {  
        this.socket = c;
    }
    public void setClient(Socket c) {
    	this.socket = c;
    }
    public Socket getSocket() {
    	return this.socket;
    }

	public static int getUserList() {
		return UserList.size();
	}
	public static Iterator<Entry<InetAddress, ServerFileThread>> getUser() {
		return UserList.entrySet().iterator();
	}
	public static void addUserList(InetAddress intelAdd, ServerFileThread st) {
		UserList.put(intelAdd, st);
	}
	public static void removeUserList(InetAddress inetAddress) {
		UserList.remove(inetAddress);
	}
	/**
	 * @return
	 * 向客户端发送文件
	 */
	private class SendFile extends Thread {
		
		private String filepath;
		public SendFile(String filepath) {
			this.filepath = filepath;
		}
		public void run() {
			File file = new File(filepath);
			try {
				DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filepath)));
	            DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
	            ps.writeUTF(file.getName());
	            ps.flush();
	            ps.writeLong((long) file.length());
	            ps.flush();
	
	            int bufferSize = 8192;
	            byte[] buf = new byte[bufferSize];
	
	            while (true) {
	                int read = 0;
	                if (fis != null) {
	                    read = fis.read(buf);
	                }
	
	                if (read <= 0) {
	                    break;
	                }
	                ps.write(buf, 0, read);
	            }
	            System.out.println("文件发送完成");
	            ps.flush();
	            ps.close();
	            fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
	/**
	 * @return
	 * 向客户端发送消息
	 */
	public void send(String filename) {
		(new SendFile(filename)).start();
		//将从系统标准输入读入的字符串输出到Server
	}
}