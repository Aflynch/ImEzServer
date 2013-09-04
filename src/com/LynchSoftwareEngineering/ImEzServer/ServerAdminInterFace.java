package com.LynchSoftwareEngineering.ImEzServer;

import java.util.Scanner;

public class ServerAdminInterFace extends Thread {

	public ServerAdminInterFace() {
		System.out.println("ServerAdminInterFace Ready: ");
		Scanner systemIn = new Scanner(System.in);
		while (true){
			String adminInstruction = systemIn.nextLine();
			if (adminInstruction.equals("exit")){
				System.exit(0);
			}
		}
	}
}
