package org.jjdltc.cordova.plugin.sqlite;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import android.content.Context;

import org.json.JSONArray;

public class queryBuilder {

	private Context appCtx 	= null;
	
	public queryBuilder(Context ctx) {
		this.appCtx 		= ctx;
	}

	public String getQuery(String queryFilePath, JSONArray replaceParams){
		String query 			= null;
		boolean doesAssetExists	= this.doesAssetExists(queryFilePath);
		if(!doesAssetExists){
			return query;
		}
		
		query 					= this.getRawQuery(queryFilePath);
		if(query== null || query.isEmpty()){
			return null;
		}
		
		query 					= this.buildFormattedQuery(query, replaceParams);
		
		return query;
	}
	
	private boolean doesAssetExists(String path){
		String name 	= path.substring(path.lastIndexOf("/")).replaceAll("/+$|^/+", "");
		String folder 	= path.replace(name, "").replaceAll("/+$|^/+","");
        boolean exists	= false;
        
		try {
			exists = Arrays.asList(this.appCtx.getAssets().list(folder)).contains(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return exists;
	}
	
	private String getRawQuery(String queryFilePath){
		String result 			= "";
        
        try {
        	InputStream stream = this.appCtx.getAssets().open(queryFilePath);

	        int size = stream.available();
	        byte[] buffer = new byte[size];
	        stream.read(buffer);
	        stream.close();
	        result = new String(buffer);
        } catch (IOException e) {
        	e.printStackTrace();
        }

		return result;
	}
	
	private String buildFormattedQuery(String rawQuery, JSONArray replaceParams){
		Object[] replaceParamsArr 	= new Object[replaceParams.length()];
		
		for (int i = 0; i < replaceParams.length(); i++) {
			replaceParamsArr[i] 	= replaceParams.opt(i);
		}

		String formattedQuery  			= String.format(rawQuery, replaceParamsArr);
		
		return formattedQuery;
	}
	
}
