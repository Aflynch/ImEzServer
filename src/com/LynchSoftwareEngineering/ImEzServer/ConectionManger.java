package com.LynchSoftwareEngineering.ImEzServer;

/**ConectoinManger.java 
 * This class manages the {@link SocketContaner}s that represents the clients. The purpose of this class
 * is to pass references so that coupling may occur. Coupling is the means by which Strings are transferred  
 * form one {@link SocketContaner} to an other. 
 * 
 * This class has two HashMaps socketContanerRamdomKeyHashMap  and chatReadyUsersUserNameKeyHashMap because
 * when a user connects to the server it has not longed in yet. After the long and validation processes have been completed
 * the users name is known and the {@link SocketContaner} that is a associated with that user is transferred  to the 
 * chatReadyUsersUserNameKeyHashMap. 
 * 
 * When {@link SocketContaner}s are transferred in or out of the chatReadyUsersUserNameKeyHashMap it sends and update 
 * signal to all of the clients that are on line. 
 * 
 * @author Andrew F. Lynch
 */

import java.util.ArrayList;
import java.util.HashMap;

public class ConectionManger extends Thread  {
	private static HashMap<Double,SocketContaner> socketContanerRamdomKeyHashMap;
	private static HashMap<String,Double> chatReadyUsersUserNameKeyHashMap;
	
	ConectionManger() {
		socketContanerRamdomKeyHashMap = new HashMap<Double, SocketContaner>();	
		chatReadyUsersUserNameKeyHashMap = new HashMap<String,Double>();
	}

	public synchronized void add(SocketContaner socketContaner){
		double randomKey = Math.random();
		socketContanerRamdomKeyHashMap.put(randomKey,socketContaner);
		socketContaner.setRandomKey(randomKey);
	}
	
	public synchronized void remove(Double randomKey){
		socketContanerRamdomKeyHashMap.remove(randomKey);
	}
	
	public synchronized void remover(String userName){
		chatReadyUsersUserNameKeyHashMap.remove(userName);
	}
	
	public synchronized SocketContaner getChatReadyUsers(String userName){
		return  socketContanerRamdomKeyHashMap.get(chatReadyUsersUserNameKeyHashMap.get(userName));
	}

	public synchronized void addToChatReadyUsers(double randomKey, String userName) {
		chatReadyUsersUserNameKeyHashMap.put(userName,randomKey);
		upDataAllChatReadyUsersToAdditionsToChatReadyHashSet(userName);
	}
	
	public synchronized void removeFromChatReadyUsers(double randomKey, String userName) {
		chatReadyUsersUserNameKeyHashMap.remove(userName);
		upDataAllChatReadyUsersToSubtractionsFromChatReadyHashSet(userName);
		socketContanerRamdomKeyHashMap.remove(randomKey);
	}
	
	
	public synchronized void upDataAllChatReadyUsersToSubtractionsFromChatReadyHashSet(String userName) {
		ArrayList<Double> chatReadyUsersUserNameValuesArrayList = new ArrayList<Double>(chatReadyUsersUserNameKeyHashMap.values());
		for(Double randomKey:chatReadyUsersUserNameValuesArrayList){
			socketContanerRamdomKeyHashMap.get(randomKey).getNetWorkingObject().sendHashTagData("#UserListRemove");//client side
			socketContanerRamdomKeyHashMap.get(randomKey).getNetWorkingObject().sendHashTagData(""+1);
			socketContanerRamdomKeyHashMap.get(randomKey).getNetWorkingObject().sendHashTagData(userName);
		}		
	}

	public synchronized ArrayList<String> getArrayListOfChatReadyUserNames(){
		return new ArrayList<String>(chatReadyUsersUserNameKeyHashMap.keySet());
	}

	private synchronized void upDataAllChatReadyUsersToAdditionsToChatReadyHashSet(String userName) {
		ArrayList<Double> chatReadyUsersUserNameValuesArrayList = new ArrayList<Double>(chatReadyUsersUserNameKeyHashMap.values());
		for(Double randomKey:chatReadyUsersUserNameValuesArrayList){
			socketContanerRamdomKeyHashMap.get(randomKey).getNetWorkingObject().sendHashTagData("#UserListAdd");
			socketContanerRamdomKeyHashMap.get(randomKey).getNetWorkingObject().sendHashTagData(""+1);
			socketContanerRamdomKeyHashMap.get(randomKey).getNetWorkingObject().sendHashTagData(userName);
		}
	}

	public ArrayList<String> getAllChatReadyUsers() {
		return new ArrayList<String>(chatReadyUsersUserNameKeyHashMap.keySet());
	}
	
}
