package com.thorben.application.service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.logging.Logger;

public class DierkesLooger {
	
	private static final Logger LOOGER = Logger.getLogger(DierkesLooger.class.getName());
	private String directoryName;
	
	public DierkesLooger() {
		this.directoryName = System.getProperty("user.dir");
	}
	
	public DierkesLooger(String file) {
		this.directoryName = file;
	}
	
	public void writeInfoLog(String message, String className) {
		String infoMessage = className + ": " + message;
		LOOGER.info(infoMessage);
		this.writeLogToTxT(null,infoMessage);
	}
	
	public void writeExceptionLog(Exception e, String message, String className) {
		String infoMessage = className + ": " + message;
		LOOGER.info(infoMessage);
		this.writeLogToTxT(e, infoMessage);
	}
	
	public void writeLogToTxT(Exception e, String message) {
		try(BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.directoryName + "/log.txt", true)))){
			wr.append(new Date(System.currentTimeMillis()).toString() + ": " + message + "\n");
			if(e != null) {
				wr.append(e.getMessage() + "\n");
				wr.append(e.toString() + "\n");
			}
			
		} catch(Exception e1) {
			e.printStackTrace();
		}
	}

}
