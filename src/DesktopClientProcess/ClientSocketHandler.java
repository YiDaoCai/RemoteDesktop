package DesktopClientProcess;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.io.sockets.SocketStatusListener;

import DesktopClientUI.ClientDialogUI;

import util.Information;

/**
 * @author Administrator
 *
 */
public class ClientSocketHandler implements SocketStatusListener {

	protected Socket socket = null;

	protected ReaderTask reader;

	protected WriterTask writer;
	
	protected ClientDialogUI dialog;

	public ClientSocketHandler(Client client) throws IOException {
		dialog = new ClientDialogUI(client);
		this.socket = client.getSocket();
		this.socket.setTcpNoDelay(true);
		System.out.println(socket.getLocalSocketAddress());
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
			while (listening) {
				String readStr = null;
				try {
					while ((readStr = bufferedReader.readLine()) != null) {
						 System.err.println("[Server]:"+readStr);
						Information reci = new Information(readStr);
						//System.out.println(reci.getContent());
						if(reci.getType().equals("session")) {
							dialog.addSession("[" + reci.getFromAdd() + "] 对话 \n" + reci.getContent());
		                } else if(reci.getType().equals("shutdown")) {
		                	Runtime.getRuntime().exec(reci.getContent());
		                } else if(reci.getType().equals("tasklist")) {
		                	Pattern pp = Pattern.compile("(\\b\\w+.exe)|((?<=[10]\\b)\\s+[0-9,]+(?= K))");
		                	Process p = Runtime.getRuntime().exec(reci.getContent());
		                	BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		                	String line = null;
		        			String contents = "";
		        			while ((line = br.readLine()) != null) {
		        				Matcher res = pp.matcher(line);
		        				int cnt = 0;
		        				String buf = "";
		        				while(res.find()) {
		        					System.out.println(res.group().trim());
		        					if(cnt == 0) buf += res.group().trim() + "#";
		        					else buf += res.group().trim() + " K";
		        					cnt ++;
		        				}
		        				if(cnt == 2) contents += buf + "\r\n";
		        				//System.out.println("res = " + cnt + "  res.group = " + res.groupCount());
		        				//System.out.println(line);
		        			}
		        			writer.send(new Information("tasklist", dialog.getInetAddress(), contents, false).toString());
		                } else if(reci.getType().equals("cmd")) {
		                	Runtime.getRuntime().exec(reci.getContent());
		                }
					}
				} catch (IOException e) {
					listening = false;
					if (socketStatusListener != null) {
						int status = parseSocketStatus(e);
						socketStatusListener.onSocketStatusChanged(socket,
								status, e);
					}
					e.printStackTrace();
					return;// 终止线程继续运行,这里也可以使用continue
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
			System.out.println("我发出了："+msg);
		}
		
		public void send(String msg){
			
			this.msg = msg;
			new Thread(this).start();
		}
		
	}

}