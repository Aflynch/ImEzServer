package com.LynchSoftwareEngineering.ImEzServer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;
/** ImEzServer.java
 * This class acts as a port listener. As connections are made ServerSocket
 * dispatches a new Socket. That Socket is add to a new {@link SocketContaner} 
 * each time the connection is made. The {@link SocketContaner}s are managed by 
 * the {@link ConectionManger}.
 * 
 * @author Andrew F. Lynch
 *
 */
public class ImEzServer {

	ServerSocket serverSocket;
	ImEzDataBase imEzDataBase = ImEzDataBase.getInstance();
	
	public ImEzServer() {
		System.out.println("Server is starting.");
        try {
        	serverSocket = new ServerSocket(9902);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
        try {
        	ConectionManger conectionManger = new ConectionManger();
        	conectionManger.start();
			while(true){
				Socket socket = serverSocket.accept();
				SocketContaner socketContaner = new SocketContaner(socket, conectionManger);
				socketContaner.start();
				System.out.println("conection made");
				conectionManger.add(socketContaner);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
