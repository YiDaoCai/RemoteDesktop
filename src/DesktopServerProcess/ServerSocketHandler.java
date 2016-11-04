package DesktopServerProcess;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.io.sockets.SocketStatusListener;

import DesktopServerUI.ServerMainFrame;
import DesktopServerUI.taskViewFrame;

import util.Information;

/**
 * @author Administrator
 *
 */
public class ServerSocketHandler implements SocketStatusListener {
	
	private static ServerMainFrame mainframe = ServerMainFrame.getFrame();
	
	protected Socket filesocket = null;

	protected Socket socket = null;

	protected ReaderTask reader;

	protected WriterTask writer;

	public ServerSocketHandler(Socket socket) throws IOException {

		this.socket = socket;
		this.socket.setTcpNoDelay(true);
		reader = new ReaderTask(socket);
		writer = new WriterTask(socket);
		onSocketStatusChanged(socket, STATUS_OPEN, null);
	}

	public void setFileSocket(Socket socket) {
		filesocket = socket;
	}
	
	public void sendFile(String filename) {
		new SendFile(filename).start();
	}
	/**
	 * sendMessage:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 */
	public void sendMessage(String msg) {
		writer.send(msg);
	}

	public void listen(boolean isListen) {
		reader.startListener(this);

	}

	public void shutDown() {

		if (!socket.isClosed() && socket.isConnected()) {
			try {
				writer.finish();
				reader.finish();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				onSocketStatusChanged(socket, STATUS_CLOSE, e);
			} finally {
				reader = null;
				writer = null;
				System.out.println("Socket连接已关闭！！");
			}
		}

	}

	@Override
	public void onSocketStatusChanged(Socket socket, int status, IOException e) {

		switch (status) {

		case SocketStatusListener.STATUS_CLOSE:
		case SocketStatusListener.STATUS_RESET:
		case SocketStatusListener.STATUS_PIP_BROKEN:
			shutDown();
			break;

		default:
			break;
		}
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
	            DataOutputStream ps = new DataOutputStream(filesocket.getOutputStream());
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
	 * @author Administrator
	 * 读操作进程
	 */
	public class ReaderTask extends Thread {

		private SocketStatusListener socketStatusListener;

		private BufferedReader bufferedReader;

		private Socket socket;

		private boolean listening;

		
		public ReaderTask(Socket socket) throws IOException {
			bufferedReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
			this.socket = socket;
		}

		/**
		 * finish:(这里用一句话描述这个方法的作用). <br/>
		 * TODO(这里描述这个方法适用条件 – 可选).<br/>
		 * 
		 * @throws IOException
		 * 
		 */
		public void finish() throws IOException {
			listening = false;
			interrupt();
			if (bufferedReader != null && socket != null) {
				if (socket.isInputShutdown()) {
					socket.shutdownInput();
				}
				bufferedReader.close();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public synchronized void run() {
			System.out.println(1);
			while (listening) {
				String readStr = null;
				try {
					mainframe.addSession("[" + socket.getInetAddress().getHostAddress() + "] 已上线");
		            mainframe.setOnlineMember("当前在线人数为：" + ServerThread.getUserList());
		            //System.out.println("当前在线人数为：" + ServerThread.getUserList());
					while ((readStr = bufferedReader.readLine()) != null) {
						// System.err.println("[Server]:"+readStr);
						Information reci = new Information(readStr);
						
						if(reci.getType().equals("session")) {
		                	mainframe.addSession("[" + socket.getInetAddress().getHostAddress() + "] 对话 \n" + reci.getContent());
		                	
		                	Iterator<Entry<String, ServerThread>> iter = ServerThread.getUser();
		    				while(iter.hasNext()) {
		    					Map.Entry<String, ServerThread> val = iter.next();
		    					if(!val.getKey().equals(socket.getInetAddress()))
		    						val.getValue().sendMessage(reci);
		    				}
		                	
		                } else if(reci.getType().equals("raisehand")) {
		                	mainframe.addSession("[" + socket.getInetAddress().getHostAddress() + "] 举手");
		                	mainframe.showRaiseHand("[" + socket.getInetAddress().getHostAddress() + "] 举手");
		                } else if(reci.getType().equals("tasklist")) {
		                	taskViewFrame.getFrame().view(reci);
		                	System.out.println(reci);
		                }
					}
					//System.out.println(4);
				} catch (IOException e) {
					listening = false;
					if (socketStatusListener != null) {
						int status = parseSocketStatus(e);
						socketStatusListener.onSocketStatusChanged(socket,
								status, e);
					}
					e.printStackTrace();
					return;// 终止线程继续运行,这里也可以使用continue
				} finally {
					//System.out.println(socket.getInetAddress()+":"+socket.getPort()+"已下线");
					ServerShot.get(socket.getInetAddress().getHostAddress()).setVisible(false);
					mainframe.addSession("[" + socket.getInetAddress().getHostAddress() + "] 已下线");
					ServerThread.removeUserList(socket.getInetAddress().getHostAddress());
					mainframe.setOnlineMember("当前在线人数为：" + ServerThread.getUserList());
					 
				}

			}
		}

		private int parseSocketStatus(IOException e) {
			if (SocketException.class.isInstance(e)) {
				String msg = e.getLocalizedMessage().trim();
				if ("Connection reset".equalsIgnoreCase(msg)) {
					return SocketStatusListener.STATUS_RESET;
				} else if ("Socket is closed".equalsIgnoreCase(msg)) {
					return SocketStatusListener.STATUS_CLOSE;
				} else if ("Broken pipe".equalsIgnoreCase(msg)) {
					return SocketStatusListener.STATUS_PIP_BROKEN;
				}

			}
			return SocketStatusListener.STATUS_UNKOWN;
		}

		/**
		 * listen:(这里用一句话描述这个方法的作用). <br/>
		 * TODO(这里描述这个方法适用条件 – 可选).<br/>
		 * 
		 */
		public void startListener(SocketStatusListener ssl) {
			listening = true;
			this.socketStatusListener = ssl;
			start();
		}

	}
	
	
	/**
	 * @author Administrator
	 * 写操作进程
	 */
	public  class WriterTask extends Thread{

		private PrintWriter bufferedWriter;
		
		private String msg = null;
		
		private Socket socket = null;
	
		public WriterTask(Socket socket) throws IOException {
			this.bufferedWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			this.socket = socket;
		}
		
		/**
		 * finishTask:(这里用一句话描述这个方法的作用). <br/>
		 * TODO(这里描述这个方法适用条件 – 可选).<br/>
		 * @throws IOException 
		 *
		 */
		public void finish() throws IOException {
			if(bufferedWriter!=null && socket!=null)
			{
				if(!socket.isOutputShutdown())
				{
					socket.shutdownOutput();
				}
				bufferedWriter.close();
			}
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public synchronized void run() {
			bufferedWriter.println(msg);
			bufferedWriter.flush();
			//System.out.println(msg);
		}
		
		public void send(String msg){
			this.msg = msg;
			//System.out.println(msg);
			new Thread(this).start();
		}
		
	}

}