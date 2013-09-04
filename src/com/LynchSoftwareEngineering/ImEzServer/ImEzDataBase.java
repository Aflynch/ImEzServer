package com.LynchSoftwareEngineering.ImEzServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/** ImEzDataBase.java
 * 
 * @author Andrew F. Lynch
 *
 */

public class ImEzDataBase extends Thread{
	private static File file;
	private static Scanner scanner;
	private static ImEzDataBase imEzDataBase;
	private static boolean firstRun = false;
	protected ImEzDataBase() {
		start();
		
		file = new  File("database.txt");
		try {
			scanner = new Scanner (file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			newFile();
		}

	}
	
	private static void newFile() {
		try {
			PrintWriter p = new PrintWriter(file);
			p.println("ImAmEzDataBase,Andrew_F_Lynch");
			p.flush();
			p.close();
			System.out.println("new file was made.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized static ImEzDataBase getInstance(){
		if(imEzDataBase != null){
			return imEzDataBase;
		} else {
			return new ImEzDataBase();
		}
	}

	public synchronized boolean newUser(String userName, String passWord) throws IOException {
		if(isAnOldUser(userName, passWord) == true){
			return false;
		} else {
			ArrayList<String> arrayList = new ArrayList<String>();
			Scanner scanner = new Scanner(file);
			while(scanner.hasNext()){
				arrayList.add(scanner.nextLine());
			}
			arrayList.add(userName+","+passWord);
			scanner.close();
			PrintWriter p = new PrintWriter(file);
			for(String str: arrayList){
				p.println(str);
				p.flush();
				System.out.println(str);
			}
			p.close();
		}
		return true;
	}

	public synchronized boolean isAnOldUser(String userName, String passWord) throws FileNotFoundException {
		// Same userName different passwords are allowed. 
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()){//
			if(scanner.nextLine().split(",")[0].equals(userName)) return true;
		}
		return false;
	}
	
	public synchronized boolean oldUserRequestsLongIn(String userName, String passWord){
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			
			while(scanner.hasNextLine()){
				String[] userNameAndPassWord = scanner.nextLine().split(",");
				if(userNameAndPassWord[0].equals(userName)&& userNameAndPassWord[1].equals(passWord)) return true;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
