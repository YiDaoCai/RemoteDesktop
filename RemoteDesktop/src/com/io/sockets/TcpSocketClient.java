package com.io.sockets;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import DesktopServerProcess.ServerSocketHandler;
public class TcpSocketClient {

		public static void main(String[] args) {
			
			ServerSocketHandler clientHandler = null;
			try {
				Socket clientSocket = new Socket("localhost", 8090);
				clientSocket.setKeepAlive(true);
				clientSocket.setTcpNoDelay(true);
				
				if(clientSocket.isConnected()) 
				{
					 clientHandler = new ServerSocketHandler(clientSocket);
					 clientHandler.listen(true);
					 
					 while (true) 
					 {
						Scanner sc = new Scanner(System.in);
						String next = sc.nextLine()+"\r\n";
						if(!clientSocket.isClosed())
						{
							clientHandler.sendMessage(next);
						}else{
							break;
						}
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				clientHandler.shutDown();
			}
			
		}
		
		
	}