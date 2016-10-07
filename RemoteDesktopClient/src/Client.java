import java.io.*;
import java.net.*;
public class Client {
	private final String SelfAddress;
	private final String HostName;
	private String ServerAddress;
	private int port;
	private Socket socket;
	private BufferedReader is;
	private PrintWriter os;
	public Client() {
		this.ServerAddress = "127.0.0.1";
		this.port = 1234;
		InetAddress ia=null;
        try {
            ia=InetAddress.getLocalHost();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.HostName = ia.getHostName();
        this.SelfAddress = ia.getHostAddress();
        openSocket();
	}
	/**
	 * @return
	 * 打开Socket通信
	 */
	public boolean openSocket() {
		try {
			this.socket = new Socket(this.ServerAddress, this.port);
			is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			os = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			//receive();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * @return
	 * 关闭Socket通信
	 */
	public boolean closeSocket() {
		try {
			os.close();
			is.close();
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * @return
	 * 向服务端发送消息
	 */
	public void send(Information info) {
		os.println(info.toString());
		os.flush();;
	}
	/**
	 * @return
	 * 接收服务端的消息
	 */
	public Information receive() {
		try {
			return new Information(is.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @return
	 * 接收来自服务端的文件
	 */
	public boolean receiveFile() {
		if(socket == null) return false;
		 DataInputStream inputStream = null;
		 DataInputStream getMessageStream = null;
		try {
			getMessageStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 inputStream = getMessageStream;
		 String savePath = "D:\\receive\\";
		 int bufferSize = 8192;
		 byte[] buf = new byte[bufferSize];
		 int passedlen = 0;
		 long len=0;
		 try {
			savePath += inputStream.readUTF();
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
                //下面进度条本为图形界面的prograssBar做的，这里如果是打文件，可能会重复打印出一些相同的百分比
                System.out.println("文件接收了" +  (passedlen * 100/ len) + "%\n");
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
		 //inputStream = socket.getMessageStream();
		return true;
	}
	
	/**
	 * @return
	 * 对本地进行简单操作
	 */
	public boolean operator(Operator oper) {
		
		return false;
	}
	public String toString() {
		return HostName + ":" + SelfAddress;
	}
	public Client(String ServerAddress, int port) {
		this.ServerAddress = ServerAddress;
		this.port = port;
		InetAddress ia=null;
        try {
            ia=InetAddress.getLocalHost();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.HostName = ia.getHostName();
        this.SelfAddress = ia.getHostAddress();
	}
	public String getServerAddress() {
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
	public void setPort(int port) {
		this.port = port;
	}
	public static void main(String args[]) {
		//new WnetWScreenRecordPlayer();
		//Client test = 
		//test.openSocket();
		new ClientDialogUI(new Client());
	}
	
}
