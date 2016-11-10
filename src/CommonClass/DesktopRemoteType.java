package CommonClass;
//定义不同功能使用不同端口
public class DesktopRemoteType {

	private DesktopRemoteType (String type, int port) {
		this.type = type;
		this.port = port;
	}
	
	public String getType() {
		return type;
	}

	public int getPort() {
		return port;
	}
	
	private String type;
	private int port;
	
	
	public static final DesktopRemoteType ServerFile = new DesktopRemoteType("ServerFile", 1234);
	public static final DesktopRemoteType OtherType = new DesktopRemoteType("Other", 5678);
	public static final DesktopRemoteType ServerShot = new DesktopRemoteType("ServerShot", 3456);
	public static final DesktopRemoteType ClientShot = new DesktopRemoteType("ClientShot", 6543);
	public static final DesktopRemoteType ClientFile = new DesktopRemoteType("ClientFile", 6789);
}