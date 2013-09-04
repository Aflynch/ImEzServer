package com.LynchSoftwareEngineering.ImEzServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.DatabaseMetaData;
import javax.swing.JLabel;


/**SocketInListenerThread.java
 * This class listener for in coming data and for forwards the data to the proper part of 
 * the {@link SocketContaner} that it is associated with. 
 * 
 * @author Andrew F. Lynch
 *
 */
public class SocketInListenerThread extends Thread {
	private ImEzDataBase imEzDataBase;
	private SocketContaner socketContaner;
	private NetWorkingObjectThread netWorkingObject;
	private ServerSideConnectoinValidatorThread serverSideConnectoinValidatorThread;

	private boolean readyForNextStep = false;
	private Socket socket;
	private TimeStampBufferedReader timeStampBufferedReader;
	private String userName = null;
	private String password = null;

	public SocketInListenerThread(SocketContaner socketContaner,
			NetWorkingObjectThread netWorkingObject, Socket socket) {
		this.socketContaner = socketContaner;
		this.netWorkingObject = netWorkingObject;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			imEzDataBase = ImEzDataBase.getInstance();
			timeStampBufferedReader = new TimeStampBufferedReader(new InputStreamReader(socket.getInputStream()));
			startLogInProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void startLogInProcess() {
		String newUser, nextLineString;
		
		try {
			
			nextLineString = timeStampBufferedReader.readLine();
			serverSideConnectoinValidatorThread = new ServerSideConnectoinValidatorThread(timeStampBufferedReader, socketContaner);
			serverSideConnectoinValidatorThread.start();
			serverInputCaseCheck(nextLineString);
			while (true) {
				serverInputCaseCheck(timeStampBufferedReader.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void serverInputCaseCheck(String nextLineString){
		while (true) {
			try {
			switch (nextLineString){
			case"#newUser"  : handelNewUserFlage();
					break;
			case"#checkIn"  : // No data needs to be sent
					break;
			case"#oldUser"  : handelOldUserFlage();
					break;
			case"#kill"     : socketContaner.kill();
					break;
			case"#ChatBuddy": socketContaner.plusMinus(timeStampBufferedReader.readLine());
					break;
			default			: socketContaner.sendToChatBuddies(nextLineString);
					break;
			}
			nextLineString = timeStampBufferedReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handelOldUserFlage() {
		try {
					userName = timeStampBufferedReader.readLine();
					password = timeStampBufferedReader.readLine();
					readyForNextStep = imEzDataBase.oldUserRequestsLongIn(userName, password);
					System.out.println("Username :" + userName + " password : "+ password+ "Ready for next step"+ readyForNextStep);
				if (readyForNextStep == false) {
					socketContaner.getNetWorkingObject().sendHashTagData("#BadLongIn");
				} else {
					ImEzDataBase im = ImEzDataBase.getInstance();
					if(im.oldUserRequestsLongIn(userName, password)){
					socketContaner.setUserName(userName);
					socketContaner.getUpDateChatReadyUsers();
					}else{
						netWorkingObject.sendHashTagData("#BadLongIn");
					}
				}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handelNewUserFlage() {
		try {
					userName = timeStampBufferedReader.readLine();
					password = timeStampBufferedReader.readLine();
					readyForNextStep = imEzDataBase.newUser(userName, password);
					System.out.println("Username :" + userName + " password : "+ password+ "Ready for next step"+ readyForNextStep);
				if (readyForNextStep == false) {
					socketContaner.getNetWorkingObject().sendHashTagData("#BadLongIn");
				} else {
					socketContaner.setUserName(userName);
					socketContaner.getUpDateChatReadyUsers();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ServerSideConnectoinValidatorThread getServerSideConnectoinValidatorThread() {
		return serverSideConnectoinValidatorThread;
	}

}
