import java.io.*;
import java.net.*;
public class Client {
	private final String SelfAddress;
	private final String HostName;
	private String ServerAddress;
	private int port;
	private Socket socket;
	public Client() {
		this.ServerAddress = "127.0.0.1";
		this.port = 1234;
		InetAddress ia=null;
        try {
            ia=ia.getLocalHost();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.HostName = ia.getHostName();
        this.SelfAddress = ia.getHostAddress();
	}
	/**
	 * @return
	 * 打开Socket通信
	 */
	public boolean openSocket() {
		try {
			this.socket = new Socket(this.ServerAddress, this.port);
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
	public boolean send(Infomation info) {
		
		return false;
	}
	/**
	 * @return
	 * 接收来自服务端的消息
	 */
	public boolean receive() {
		
		return false;
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
            ia=ia.getLocalHost();
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
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public static void main(String args[]) {
		Client test = new Client();
		System.out.println(test);
		/*
		try{
			Socket socket=new Socket("127.0.0.1",4700);
			//向本机的4700端口发出客户请求
			BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
			//由系统标准输入设备构造BufferedReader对象
			PrintWriter os=new PrintWriter(socket.getOutputStream());
			//由Socket对象得到输出流，并构造PrintWriter对象
			BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//由Socket对象得到输入流，并构造相应的BufferedReader对象
			String readline;
			readline=sin.readLine(); //从系统标准输入读入一字符串
			while(!readline.equals("bye")){
				//若从标准输入读入的字符串为 "bye"则停止循环
				os.println(readline);
				//将从系统标准输入读入的字符串输出到Server
				os.flush();
				//刷新输出流，使Server马上收到该字符串
				System.out.println("Client:"+readline);
				//在系统标准输出上打印读入的字符串
				System.out.println("Server:"+is.readLine());
				//从Server读入一字符串，并打印到标准输出上
				readline=sin.readLine(); //从系统标准输入读入一字符串
			} //继续循环
			os.close(); //关闭Socket输出流
			is.close(); //关闭Socket输入流
			socket.close(); //关闭Socket
		}catch(Exception e) {
			System.out.println("Error: "+ e);
			//出错，则打印出错信息
		}
		*/
	}
}
