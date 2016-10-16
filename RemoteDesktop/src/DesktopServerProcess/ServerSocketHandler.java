package DesktopServerProcess;

import java.io.BufferedReader;
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

import util.Information;

/**
 * @author Administrator
 *
 */
public class ServerSocketHandler implements SocketStatusListener {
	
	private static ServerMainFrame mainframe = ServerMainFrame.getFrame();

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
					mainframe.addSession("[" + socket.getInetAddress() + "] 已上线");
		            mainframe.setOnlineMember("当前在线人数为：" + ServerThread.getUserList());
		            //System.out.println("当前在线人数为：" + ServerThread.getUserList());
					while ((readStr = bufferedReader.readLine()) != null) {
						// System.err.println("[Server]:"+readStr);
						Information reci = new Information(readStr);
						
						if(reci.getType().equals("session")) {
		                	mainframe.addSession("[" + socket.getInetAddress() + "] 对话 \n" + reci.getContent());
		                	
		                	Iterator<Entry<InetAddress, ServerThread>> iter = ServerThread.getUser();
		    				while(iter.hasNext()) {
		    					Map.Entry<InetAddress, ServerThread> val = iter.next();
		    					if(!val.getKey().equals(socket.getInetAddress()))
		    						val.getValue().sendMessage(reci);
		    				}
		                	
		                } else if(reci.getType().equals("raisehand")) {
		                	mainframe.addSession("[" + socket.getInetAddress() + "] 举手");
		                	mainframe.showRaiseHand("[" + socket.getInetAddress() + "] 举手");
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
					mainframe.addSession("[" + socket.getInetAddress() + "] 已下线");
					ServerThread.removeUserList(socket.getInetAddress());
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