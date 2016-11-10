package DesktopClientProcess;
//学生端的main函数，输入ip连接，以及接收文件
import java.io.*;
import java.net.*;

import CommonClass.DesktopRemoteType;
import CommonClass.Information;
import CommonClass.XMLUtil;


/**
 * @author Administrator
 * 学生端类
 */
public class Client {
	private final String SelfAddress;
	private final String HostName;
	private static String ServerAddress;
	private int port;
	private Socket socket;
	public Socket getSocket() {
		return socket;
	}

	private ServerSocket filesocket;
	
	private ClientSocketHandler handler;

	public Client(DesktopRemoteType type) throws IOException {
		this.port = type.getPort();
		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.HostName = ia.getHostName();
		this.SelfAddress = ia.getHostAddress();
		openSocket();
		handler = new ClientSocketHandler(this);
		handler.listen(true);
		new ClientShot();
		filesocket = new ServerSocket(DesktopRemoteType.ClientFile.getPort());
		new FileReceive().start();
		System.out.println("shot start...");
	}

	/**
	 * @return 打开Socket通信
	 */
	public void openSocket() {
		boolean ok = false;
		String ip = null;
		if (!XMLUtil.exist()) {
			ip = javax.swing.JOptionPane.showInputDialog(null, " 输入服务器地址");
			XMLUtil.createXML(ip);
		}
		while (!ok) {
			try {
				Client.ServerAddress = XMLUtil.getBean("server");
				socket = new Socket(Client.ServerAddress, this.port);
				socket.setKeepAlive(true);
				socket.setTcpNoDelay(true);
				ok = true;
			} catch (UnknownHostException e) {
				ip = javax.swing.JOptionPane.showInputDialog(null,
						"服务器地址有误，请重新输入服务器地址");
				XMLUtil.createXML(ip);
			} catch (ConnectException ex) {
				ip = javax.swing.JOptionPane.showInputDialog(null,
						"连接服务器超时，请重新输入服务器地址");
				XMLUtil.createXML(ip);
			} catch (Exception e) {
				ip = javax.swing.JOptionPane.showInputDialog(null,
						"连接服务器失败，请重新输入服务器地址");
				XMLUtil.createXML(ip);
			}
		}
	}

	/**
	 * @return 关闭Socket通信
	 */
	public boolean closeSocket() {
		try {
			this.socket.close();
			// this.filesocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @return 向服务端发送消息
	 */
	public void send(Information info) {
		handler.sendMessage(info.toString());
	}

	/**
	 * @return 接收服务端的消息
	 */
	public Information receive() {
		return null;
	}
	
	public class FileReceive extends Thread {
		/**
		 * @return 接收来自服务端的文件
		 */
		public void run() {
			Socket client;
			DataInputStream inputStream = null;
			DataInputStream getMessageStream = null;
			while (true) {
				try {
					client = filesocket.accept();
					getMessageStream = new DataInputStream(
							new BufferedInputStream(client.getInputStream()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				inputStream = getMessageStream;
				String savePath = "D:\\receive\\";
				File path = new File(savePath);
				path.mkdir();
				int bufferSize = 8192;
				byte[] buf = new byte[bufferSize];
				int passedlen = 0;
				long len = 0;
				try {
					savePath += inputStream.readUTF();
					DataOutputStream fileOut = new DataOutputStream(
							new BufferedOutputStream(new BufferedOutputStream(
									new FileOutputStream(savePath))));
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
						System.out.println("文件接收了" + (passedlen * 100 / len)
								+ "%\n");
						fileOut.write(buf, 0, read);
					}
					System.out.println("接收完成，文件存为" + savePath + "\n");
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
	}

	public String toString() {
		return HostName + ":" + SelfAddress;
	}

	public static String getServerAddress() {
		return ServerAddress;
	}

	public void setServerAddress(String serverAddress) {
		ServerAddress = serverAddress;
	}

	public String getSelfAddress() {
		return SelfAddress;
	}

	public int getPort() {
		return port;
	}

	public static void main(String args[]) throws IOException {
		// String ip = javax.swing.JOptionPane.showInputDialog(null,
		// "服务器地址有误，请重新输入服务器地址");
		new Client(DesktopRemoteType.OtherType);
	}

}