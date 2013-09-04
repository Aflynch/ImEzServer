package com.LynchSoftwareEngineering.ImEzServer;

public class SingontoinGUIMager {
	private static TestFrameOutPut testFrameOutPut;
	
	protected SingontoinGUIMager() {
	}
	
	public static TestFrameOutPut getInstanceOfTestFrame(){
		if (testFrameOutPut == null){
			testFrameOutPut = new TestFrameOutPut();// Run on it's own thread?
			Thread t = new Thread(testFrameOutPut);
			t.start();
		}
		return testFrameOutPut;
	}

}
