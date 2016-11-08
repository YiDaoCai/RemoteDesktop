package DesktopServerProcess;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


import util.Information;

public class ServerThread extends ServerSocketHandler {
    private static Map<String, ServerThread> UserList = new HashMap<String, ServerThread>();
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
	public static ServerThread getServerThread(String ip) {
		return UserList.get(ip);
	}
	public static Iterator<Entry<String, ServerThread>> getUser() {
		return UserList.entrySet().iterator();
	}
	public static void addUserList(String intelAdd, ServerThread st) {
		UserList.put(intelAdd, st);
	}
	public static void removeUserList(String string) {
		UserList.remove(string);
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