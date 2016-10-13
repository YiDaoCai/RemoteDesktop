package com.io.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import DesktopServerProcess.ServerSocketHandler;


public class TcpSocketServer {

	public static void main(String[] args) {
		
		List<ServerSocketHandler> serverHandlers = new CopyOnWriteArrayList<ServerSocketHandler>();
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(8090, 5);
			while(true)
			{
				Socket clientSocket = serverSocket.accept();
				if(clientSocket.isConnected()) 
				{
					ServerSocketHandler serverHandler = new ServerSocketHandler(clientSocket);
					serverHandlers.add(serverHandler);
					serverHandler.listen(true);
					
					serverHandler.sendMessage("Host:"+serverSocket.getInetAddress().getHostAddress()+"\r\n");
					
					while (true) 
					{
						Scanner sc = new Scanner(System.in);
						String next = sc.nextLine()+"\r\n";
						for (ServerSocketHandler scItem : serverHandlers) {
							scItem.sendMessage(next);
						}
					}
				}
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				for (ServerSocketHandler serverHandler : serverHandlers) 
				{
					serverHandler.shutDown();
				}
				serverHandlers.clear();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}