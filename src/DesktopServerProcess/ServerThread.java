package DesktopServerProcess;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


import util.Information;

public class ServerThread extends ServerSocketHandler {
    private static Map<InetAddress, ServerThread> UserList = new HashMap<InetAddress, ServerThread>();
    //private BufferedReader is;
	public ServerThread(Socket c) throws IOException {  
        super(c);
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
	public static ServerThread getServerThread(InetAddress ia) {
		return UserList.get(ia);
	}
	public static Iterator<Entry<InetAddress, ServerThread>> getUser() {
		return UserList.entrySet().iterator();
	}
	public static void addUserList(InetAddress intelAdd, ServerThread st) {
		UserList.put(intelAdd, st);
	}
	public static void removeUserList(InetAddress inetAddress) {
		UserList.remove(inetAddress);
	}
	/**
	 * @return
	 * 向客户端发送消息
	 */
	public void sendMessage(Information info) {
		writer.send(info.toString());
		//将从系统标准输入读入的字符串输出到Server
	}
}