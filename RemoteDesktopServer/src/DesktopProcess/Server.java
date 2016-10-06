package DesktopProcess;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;


public class Server {
	private final String SelfAddress;
	private final String HostName;
	private int port;
	private ServerSocket server;
	public Server() {
		InetAddress ia=null;
        try {
            ia = InetAddress.getLocalHost();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.HostName = ia.getHostName();
        this.SelfAddress = ia.getHostAddress();
        port = 1234;
	}
	public boolean openServer() {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * @return
	 * 打开Socket通信
	 * @throws IOException 
	 */
	public void openSocket() throws IOException {
		while(true) {
			ServerThread conn = new ServerThread(server.accept());
			conn.start();
		}
	}
	public String getHostName() {
		return HostName;
	}
	public String getSelfAddress() {
		return SelfAddress;
	}
}
