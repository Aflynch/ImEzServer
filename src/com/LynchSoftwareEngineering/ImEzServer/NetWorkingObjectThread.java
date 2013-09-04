package com.LynchSoftwareEngineering.ImEzServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * NetWorkingObjectThread.java 
 * This class manages network input and output. This class has a {@link SocketInListenerThread} object
 * that waits for data and sends it to {@link SocketContaner} for processing. 
 * 
 * @author Andrew F. Lynch
 *
 */

public class NetWorkingObjectThread extends Thread{
	private SocketInListenerThread socketInListenerThread;
	private SocketContaner socketContaner;
	private Socket socket;
	private BufferedWriter bufferedWriter;
	
	public NetWorkingObjectThread(SocketContaner socketContaner, Socket socket) {
		this.socketContaner = socketContaner;
		this.socket = socket;
		System.out.println(this + "NetWorkOject()");
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		socketInListenerThread = new SocketInListenerThread(socketContaner,this, socket);
		socketInListenerThread.start();
	}

	public void send(String string){
		try {
			bufferedWriter.write(string+"\n");
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendHashTagData(String string) {
		try {
			bufferedWriter.write(string+"\n");
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void kill() {
		socketInListenerThread.getServerSideConnectoinValidatorThread().kill();
		socketInListenerThread.setDaemon(true);
		socketInListenerThread = null;		
	}

}
