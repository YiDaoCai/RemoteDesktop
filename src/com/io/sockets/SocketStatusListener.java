package com.io.sockets;


import java.io.IOException;
import java.net.Socket;

public interface SocketStatusListener
{
	public static final int STATUS_OPEN = 0x01<<0;
	public static final int STATUS_CLOSE = 0x01<<1;
	public static final int STATUS_RESET = 0x01<<2;
	public static final int STATUS_PIP_BROKEN = 0x01<<3;
	public static final int STATUS_UNKOWN = 0x01<<4;
		
	public void onSocketStatusChanged(Socket socket,int status,IOException e);
}