package com.LynchSoftwareEngineering.ImEzServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.DatabaseMetaData;
import javax.swing.JLabel;

public class SocketInListener extends Thread {
	private ImEzDataBase imEzDataBase;
	private SocketContaner socketContaner;
	private NetWorkingObject netWorkingObject;
	private ServerSideConnectoinValidatorThread serverSideConnectoinValidatorThread;
	private boolean readyForNextStep = false;
	private Socket socket;
	private TimeStampBufferedReader timeStampBufferedReader;
	private String userName = null;
	private String password = null;

	public SocketInListener(SocketContaner socketContaner,
			NetWorkingObject netWorkingObject, Socket socket) {
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
				
				/*
				
			newUser = timeStampBufferedReader.readLine();
			if (newUser.equals("#newUser")) {
				handelNewUserFlage();
				System.out.println("newUser flag cought");
			} else if (newUser.equals("#oldUser")) {
				handelOldUserFlage();
			} 
			String buffer = newUser;
				if (buffer == null || buffer.equals("#kill")) {
					socketContaner.kill();
				} else if (buffer.equals("#ChatBuddy")) {
					socketContaner.plusMinus(timeStampBufferedReader.readLine());
				} else {
					socketContaner.sendToChatBuddies(buffer);
				}
			}
		*/
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
				if (readyForNextStep == false) {// need to send user feed back
					socketContaner.getNetWorkingObject().sendHashTagData("#BadLongIn");
				} else {
					socketContaner.setUserName(userName);
					socketContaner.getUpDateChatReadyUsers();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
