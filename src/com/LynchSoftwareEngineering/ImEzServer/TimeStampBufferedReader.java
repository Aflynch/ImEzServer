package com.LynchSoftwareEngineering.ImEzServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**TimeStampBufferedReader.java
*  This class extends {@link BufferedReader} and adds the ability to find out when the last 
*  time the readLine method was called.
*@author Andrew F. Lynch 
*/
public class TimeStampBufferedReader extends BufferedReader {

	long timeOfLastReadLine;

	public TimeStampBufferedReader(Reader in) {
		super(in);
	}

	@Override
	public String readLine() throws IOException {
		String buffer = super.readLine();
		System.out.println("\n----------"+buffer);
		timeOfLastReadLine = System.currentTimeMillis();
		return buffer;
	}
	
	public long getTimeOfLastReadLine() {
		return timeOfLastReadLine;
	}
}
