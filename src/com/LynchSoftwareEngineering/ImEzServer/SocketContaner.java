package com.LynchSoftwareEngineering.ImEzServer;

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
	private NetWorkingObject netWorkingObject; 
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
		netWorkingObject = new NetWorkingObject(this, socket);
		netWorkingObject.start();
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
		netWorkingObject.kill();
		netWorkingObject = null;
		netWorkingObject.interrupt();
		netWorkingObject = null;
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
		netWorkingObject.sendHashTagData("#LoggedIn");
	}
	public NetWorkingObject getNetWorkingObject() {
		return netWorkingObject;
	}

	public void setNetWorkingObject(NetWorkingObject netWorkingObject) {
		this.netWorkingObject = netWorkingObject;
	}

	public void getUpDateChatReadyUsers() {
		ArrayList<String> allChatReadyUsersArrayList = conectionManger.getAllChatReadyUsers();
		netWorkingObject.sendHashTagData("#UserListAdd");
		netWorkingObject.sendHashTagData(""+allChatReadyUsersArrayList.size());
		for(String userName:allChatReadyUsersArrayList){
			netWorkingObject.sendHashTagData(userName);
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
