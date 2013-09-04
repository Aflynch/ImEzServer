package com.LynchSoftwareEngineering.ImEzServer;

import java.util.ArrayList;
import java.util.HashMap;

public class ConectionManger extends Thread  {
	private static HashMap<Double,SocketContaner> socketContanerHashMap;
	private static HashMap<String,Double> chatReadyUsersUserNameKeyHasMap;//
	
	ConectionManger() {
		socketContanerHashMap = new HashMap<Double, SocketContaner>();	
		chatReadyUsersUserNameKeyHasMap = new HashMap<String,Double>();
	}

	public synchronized void add(SocketContaner socketContaner){
		double randomKey = Math.random();
		socketContanerHashMap.put(randomKey,socketContaner);
		socketContaner.setRandomKey(randomKey);
	}
	
	public synchronized void remove(Double randomKey){
		socketContanerHashMap.remove(randomKey);
	}
	
	public synchronized void remover(String userName){
		chatReadyUsersUserNameKeyHasMap.remove(userName);
	}
	
	public synchronized SocketContaner getChatReadyUsers(String userName){
		return  socketContanerHashMap.get(chatReadyUsersUserNameKeyHasMap.get(userName));
	}

	public synchronized void addToChatReadyUsers(double randomKey, String userName) {
		chatReadyUsersUserNameKeyHasMap.put(userName,randomKey);
		upDataAllChatReadyUsersToAdditionsToChatReadyHashSet(userName);
	}
	
	public synchronized void removeFromChatReadyUsers(double randomKey, String userName) {
		chatReadyUsersUserNameKeyHasMap.remove(userName);
		upDataAllChatReadyUsersToSubtractionsFromChatReadyHashSet(userName);
		socketContanerHashMap.remove(randomKey);
	}
	
	
	public synchronized void upDataAllChatReadyUsersToSubtractionsFromChatReadyHashSet(String userName) {
		ArrayList<Double> chatReadyUsersUserNameValuesArrayList = new ArrayList<Double>(chatReadyUsersUserNameKeyHasMap.values());
		for(Double randomKey:chatReadyUsersUserNameValuesArrayList){
			socketContanerHashMap.get(randomKey).getNetWorkingObject().sendHashTagData("#UserListRemove");//client side
			socketContanerHashMap.get(randomKey).getNetWorkingObject().sendHashTagData(""+1);
			socketContanerHashMap.get(randomKey).getNetWorkingObject().sendHashTagData(userName);
		}		
	}

	public synchronized ArrayList<String> getArrayListOfChatReadyUserNames(){
		return new ArrayList<String>(chatReadyUsersUserNameKeyHasMap.keySet());
	}

	private synchronized void upDataAllChatReadyUsersToAdditionsToChatReadyHashSet(String userName) {
		ArrayList<Double> chatReadyUsersUserNameValuesArrayList = new ArrayList<Double>(chatReadyUsersUserNameKeyHasMap.values());
		for(Double randomKey:chatReadyUsersUserNameValuesArrayList){
			socketContanerHashMap.get(randomKey).getNetWorkingObject().sendHashTagData("#UserListAdd");
			socketContanerHashMap.get(randomKey).getNetWorkingObject().sendHashTagData(""+1);
			socketContanerHashMap.get(randomKey).getNetWorkingObject().sendHashTagData(userName);
		}
	}

	public ArrayList<String> getAllChatReadyUsers() {
		return new ArrayList<String>(chatReadyUsersUserNameKeyHasMap.keySet());
	}
	
}
