package org.jjdltc.cordova.plugin.sqlite;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;

public class ScheduleTask implements Runnable {

	protected JSONObject options	= null;
	protected String dbPath 		= null;
	protected CallbackContext ctx 	= null;
	protected JSONArray resultData   = null;
	
	public ScheduleTask(JSONObject options, String dbPath, CallbackContext ctx) {
		this.options = options;
		this.dbPath = dbPath;
		this.ctx = ctx;
	}

	@Override
	public void run(){

	}
}
