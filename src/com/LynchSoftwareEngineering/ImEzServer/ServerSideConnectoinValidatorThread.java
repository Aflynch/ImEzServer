package com.LynchSoftwareEngineering.ImEzServer;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * ServerSideConnectoinValidatorThread.java 
 * 	This class check at an interval, defined by the int timeInterval, to see if the client is
 * 	still connected to the server. The client is set up to send data at least once every fifteen seconds.
 * 
 * @author Andrew F. Lynch
 */
public class ServerSideConnectoinValidatorThread extends Thread{
	TimeStampBufferedReader timeStampBufferedReader;
	SocketContaner socketContaner;
	final long timeInterval = 25000;
	long timeToNextInterval;
	private boolean kill = false;
	
	public ServerSideConnectoinValidatorThread(TimeStampBufferedReader timeStampBufferedReader, SocketContaner socketContaner) {
		this.timeStampBufferedReader = timeStampBufferedReader;
		this.socketContaner = socketContaner;
		setDaemon(true);
	}
	
	@Override
	public void run() {
		super.run();
		while(true){
			if(kill) break;
			validateClient();
			try {
				sleep(timeToNextInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void validateClient() {
		long timeDifference = System.currentTimeMillis() - timeStampBufferedReader.getTimeOfLastReadLine();
		if (timeDifference > timeInterval && kill) {
			System.out.println("Connection Time Out : "+timeDifference +" Ez2 see -------------------------");
			socketContaner.kill();
		} else {
			timeToNextInterval  = timeInterval - timeDifference;
		}
	}

	public void kill() {
		kill = true;
	}
}
