package com.LynchSoftwareEngineering.ImEzServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class NetWorkingObject extends Thread{
	private SocketInListener socketInListener;
	private SocketContaner socketContaner;
	private Socket socket;
	private BufferedWriter bufferedWriter;
	public NetWorkingObject(SocketContaner socketContaner, Socket socket) {
		this.socketContaner = socketContaner;
		this.socket = socket;
		System.out.println(this + "NetWorkOject()");
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		socketInListener = new SocketInListener(socketContaner,this, socket);
		socketInListener.start();
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
		socketInListener.interrupt();
		socketInListener.setDaemon(true);
		socketInListener = null;		
	}

}
