package DesktopProcess;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerThread extends Thread {
	private Socket client;
    private static Map<String, ServerThread> UserList = new HashMap<String, ServerThread>();
    public ServerThread(Socket c) {  
        this.client = c;
    }
    public void setClient(Socket c) {
    	this.client = c;
    }
    public void run() {  
        try {  
            BufferedReader in = new BufferedReader(new InputStreamReader(  
                    client.getInputStream()));  
            PrintWriter out = new PrintWriter(client.getOutputStream());  
            
            // Mutil User but can't parallel  
            System.out.println(client.getInetAddress()+":"+client.getPort()+"已上线");
            addUserList(client.getInetAddress()+":"+client.getPort(), this);
            System.out.println("当前在线人数为：" + getUserList());
            while (true) {  
                String str = in.readLine();  
                System.out.println(client.getInetAddress() + ":" + client.getPort() + "\n" + str);  
                out.println("has received");  
                out.flush();  
                if (str.equals("end"))  
                    break;  
            }
            client.close();  
        } catch (IOException ex) {  
        } finally {  
        	System.out.println(client.getInetAddress()+":"+client.getPort()+"已下线");
            removeUserList(client.getInetAddress()+":"+client.getPort());
            System.out.println("当前在线人数为：" + getUserList());
        }  
    }  
  
//    public static void main(String[] args) throws IOException {  
//        ServerSocket server = new ServerSocket(5678);  
//        while (true) {  
//            // transfer location change Single User or Multi User  
//        	Server mc = new Server(server.accept());  
//            mc.start();  
//        }  
//    }

	public int getUserList() {
		return UserList.size();
	}

	public void addUserList(String intelAdd, ServerThread st) {
		UserList.put(intelAdd, st);
	}
	public void removeUserList(String intelAdd) {
		UserList.remove(intelAdd);
	}
	/**
	 * @return
	 * 向客户端发送消息
	 */
	public boolean send(String filepath) {
		File file = new File(filepath);
		//int flen = (int)file.length();
		 //DataInputStream dis;
		try {
			//dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			//dis.readByte();
			DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filepath)));
            DataOutputStream ps = new DataOutputStream(client.getOutputStream());
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
         
		return false;
	}
	/**
	 * @return
	 * 接收来自客户端的消息
	 */
	public boolean receive() {
		
		return false;
	}
}