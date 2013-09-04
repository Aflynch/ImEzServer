package com.LynchSoftwareEngineering.ImEzServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ImEzDataBase.java 
 * This class is a singleton that acts as the database for this project. Because of time constraints I 
 * elected use a .txt  file. This is not my TODO list for upgrades in this project. 
 * 
 * @author Andrew F. Lynch
 * 
 */

public class ImEzDataBase {
	private static File file;
	private static ImEzDataBase imEzDataBase;

	protected ImEzDataBase() {
		file = new File("database.txt");
		try {
			Scanner scanner = new Scanner(file);
			scanner.close();
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

	public synchronized static ImEzDataBase getInstance() {
		if (imEzDataBase != null) {
			return imEzDataBase;
		} else {
			return new ImEzDataBase();
		}
	}

	public synchronized boolean newUser(String userName, String passWord) {
		if (isAnOldUser(userName, passWord) == true) {
			return false;
		} else {
			ArrayList<String> userAndPasswordArrayList = getUserAndPasswordArrayList(userName, passWord);
			addRewiteDataBase(userAndPasswordArrayList);
		}
		return true;
	}

	private void addRewiteDataBase(ArrayList<String> userAndPasswordArrayList) {
		PrintWriter p;
		try {
			p = new PrintWriter(file);
			for (String str : userAndPasswordArrayList) {
				p.println(str);
				p.flush();
				System.out.println(str);
			}
			p.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private ArrayList<String> getUserAndPasswordArrayList(String userName,String passWord) {
		ArrayList<String> arrayList = new ArrayList<String>();
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNext()) { // get all old yours and passwords
				arrayList.add(scanner.nextLine());
			}
			arrayList.add(userName + "," + passWord); // add new user and passwords
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public synchronized boolean isAnOldUser(String userName, String passWord) {
		// Same userName different passwords are allowed.
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				if (scanner.nextLine().split(",")[0].equals(userName)) {
					scanner.close();
					return true;
				} 
			}
			scanner.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return false;

	}

	public synchronized boolean oldUserRequestsLongIn(String userName,String passWord) {
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String[] userNameAndPassWord = scanner.nextLine().split(",");
				if (userNameAndPassWord[0].equals(userName) && userNameAndPassWord[1].equals(passWord)) {
					scanner.close();
					return true;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return false;
	}

}
