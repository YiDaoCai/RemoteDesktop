package DesktopServerProcess;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import DesktopServerUI.ServerMainFrame;

import util.DesktopRemoteType;


public class Server {
	private final String SelfAddress;
	private final String HostName;
	private int port;
	private ServerSocket server;
	//private ServerSocket fileserver;
	private static String name = "Server";
	public Server(DesktopRemoteType type) throws IOException {
		InetAddress ia=null;
        try {
            ia = InetAddress.getLocalHost();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.HostName = ia.getHostName();
        this.SelfAddress = ia.getHostAddress();
        port = type.getPort();
        server = new ServerSocket(port, 30);
        //fileserver = new ServerSocket(DesktopRemoteType.FileTranType.getPort(), 30);
        new Thread(new openSocket()).start();
	}
	/**
	 * @return
	 * 打开Socket通信
	 * @throws IOException 
	 */
	public class openSocket implements Runnable {
		
		@Override
		public void run() {
			while(true) {
				ServerThread conn;
				//Socket ss;
				try {
					conn = new ServerThread(server.accept());
					ServerThread.addUserList(conn.getSocket().getInetAddress().getHostAddress(), conn);
					conn.listen(true);
					//ss = fileserver.accept();
					//ServerThread.getServerThread(ss.getInetAddress()).setFileSocket(ss);
					//System.out.println("Server : " + conn);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
		
	public String getHostName() {
		return HostName;
	}
	public String getSelfAddress() {
		return SelfAddress;
	}
	
	public static void main(String[] args) throws Exception
	{
		ServerMainFrame.getFrame();
		new Server(DesktopRemoteType.OtherType);
		new ServerShot().start();
	}
	public static String getName() {
		return name;
	}
	public static void setName(String name) {
		Server.name = name;
	}
}
