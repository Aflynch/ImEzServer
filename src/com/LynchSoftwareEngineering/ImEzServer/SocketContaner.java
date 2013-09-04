package com.LynchSoftwareEngineering.ImEzServer;

/**SockContaner.java
 * 	This class represents the client on the server. It is responsible being able to send and 
 * 	receive data to the client and also to other SocketContaners. This class contains a 
 *  {@link NetWorkObjectThread} that is multi threaded to handle to sending and receiving data
 *  properly.  
 * 
 * @author Andrew F. Lynch
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.ArrayList;

public class SocketContaner extends Thread {
	//private static ImEzDataBase imEzDataBase;
	private ConectionManger conectionManger;
	private NetWorkingObjectThread netWorkObjectThread; 
	private ArrayList<String> chatBuddyArrayList;
	private String userName;
	private double randomKey;
	
	
	@Override
	public void start() {
		super.start();
		System.out.println(this + "start() was called");
	}
	
	public SocketContaner(Socket socket, ConectionManger conectionManger) {
		this.conectionManger = conectionManger;
		netWorkObjectThread = new NetWorkingObjectThread(this, socket);
		netWorkObjectThread.start();
		chatBuddyArrayList = new ArrayList<String>();
	}
	

	public void sendToChatBuddies(String buffer){
		for(String userName: chatBuddyArrayList){
			conectionManger.getChatReadyUsers(userName).getNetWorkingObject().send(getUserName()+": " +buffer);
		}
	}
	
	public void kill() {
		System.out.println(this+" kill() was called");
		conectionManger.remove(randomKey);
		if (userName != null){
			conectionManger.remover(userName);
			conectionManger.upDataAllChatReadyUsersToSubtractionsFromChatReadyHashSet(userName);
		}
		netWorkObjectThread.kill();
		netWorkObjectThread = null;
		netWorkObjectThread.interrupt();
		netWorkObjectThread = null;
	}
	public boolean equals(SocketContaner socketContaner) {
		if (socketContaner.getUserName().equals(userName)){
			return true;
		}else{
			return false;
		}
	}
	public double getRandomKey() {
		return randomKey;
	}

	public void setRandomKey(double randomKey) {
		this.randomKey = randomKey;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
		conectionManger.addToChatReadyUsers(randomKey, userName);
		netWorkObjectThread.sendHashTagData("#LoggedIn");
	}
	public NetWorkingObjectThread getNetWorkingObject() {
		return netWorkObjectThread;
	}

	public void setNetWorkingObjectThread(NetWorkingObjectThread netWorkingObjectThread) {
		this.netWorkObjectThread = netWorkingObjectThread;
	}

	public void getUpDateChatReadyUsers() { 
		ArrayList<String> allChatReadyUsersArrayList = conectionManger.getAllChatReadyUsers();
		netWorkObjectThread.sendHashTagData("#UserListAdd");
		netWorkObjectThread.sendHashTagData(""+allChatReadyUsersArrayList.size());
		for(String userName:allChatReadyUsersArrayList){
			netWorkObjectThread.sendHashTagData(userName);
		}
	}

	public void plusMinus(String userName) {
		if (chatBuddyArrayList.contains(userName)){
			chatBuddyArrayList.remove(userName);
		}else{
			chatBuddyArrayList.add(userName);
		}
	}

}
