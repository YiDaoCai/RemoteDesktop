package DesktopServerProcess;
//教师端接收文件，以及教师端程序main函数
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import CommonClass.DesktopRemoteType;
import DesktopServerUI.ServerMainFrame;


public class Server {
	private final String SelfAddress;
	private final String HostName;
	private int port;
	private ServerSocket server;
	private ServerSocket fileserver;
	private static String name = "Server";

	public Server(DesktopRemoteType type) throws IOException {
		InetAddress ia = null;
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
		fileserver = new ServerSocket(DesktopRemoteType.ServerFile.getPort(),
				30);
		new Thread(new openSocket()).start();
		new Thread(new Runnable() {

			@Override
			public synchronized void run() {
				// TODO Auto-generated method stub
				Socket client;
				DataInputStream inputStream = null;
				DataInputStream getMessageStream = null;
				while (true) {
					try {
						client = fileserver.accept();
						getMessageStream = new DataInputStream(new BufferedInputStream(client.getInputStream()));

						inputStream = getMessageStream;
						String savePath = "D:\\receive\\";
						File path = new File(savePath);
						path.mkdir();
						int bufferSize = 8192;
						byte[] buf = new byte[bufferSize];
						int passedlen = 0;
						long len = 0;
						savePath += client.getInetAddress().getHostAddress();
						String filename = inputStream.readUTF();
						savePath += " - " + filename;
						
						DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(savePath))));
						len = inputStream.readLong();
						System.out.println("文件的长度为:" + len + "\n");
						System.out.println("开始接收文件!" + "\n");
						while (true) {
							int read = 0;
							if (inputStream != null) {
								read = inputStream.read(buf);
							}
							passedlen += read;
							if (read <= 0) {
								break;
							}
							// 下面进度条本为图形界面的prograssBar做的，这里如果是打文件，可能会重复打印出一些相同的百分比
							System.out.println("文件接收了" + (passedlen * 100 / len) + "%\n");
							fileOut.write(buf, 0, read);
						}
						System.out.println("接收完成，文件存为" + savePath + "\n");
						ServerMainFrame.getFrame().addSession("收到文件 \"" + filename + "\"");
						fileOut.close();
						inputStream.close();
						getMessageStream.close();

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * @return 打开Socket通信
	 * @throws IOException
	 */
	public class openSocket implements Runnable {

		@Override
		public void run() {
			while (true) {
				ServerThread conn;
				// Socket ss;
				try {
					conn = new ServerThread(server.accept());
					ServerThread.addUserList(conn.getSocket().getInetAddress()
							.getHostAddress(), conn);
					conn.listen(true);
					// ss = fileserver.accept();
					// ServerThread.getServerThread(ss.getInetAddress()).setFileSocket(ss);
					// System.out.println("Server : " + conn);
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

	public static void main(String[] args) throws Exception {
		ServerMainFrame.getFrame();
		new Server(DesktopRemoteType.OtherType);
		ServerShotHandler.getThread().start();
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Server.name = name;
	}
}
