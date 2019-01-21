/*
 * The MIT License (MIT)
 * Copyright (c) 2015 Joel De La Torriente - jjdltc - http://www.jjdltc.com/
 * See a full copy of license in the root folder of the project
 */
package org.jjdltc.cordova.plugin.sqlite;

import java.io.File;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JJdbFile extends CordovaPlugin {

    private enum ACTIONS {
          create
        , read
        , update
        , delete
        , execute
//      , connect
//      , disconnect        
    };
    
    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        boolean result          = true;
        JSONObject options      = args.optJSONObject(0);
        boolean async           = args.optBoolean(1);
        String dbPath           = args.optString(2);
        CallbackContext ctx     = callbackContext;
        
        result                  = this.verifyParams(options, dbPath, ctx);
        
        switch (ACTIONS.valueOf(action)) {
            case create:
                this.processResponse(callbackContext, false, "create Not Yet Implemented", null, null);
            break;
            case read:
                ScheduleTask readTask = new ScheduleTask(options, dbPath, callbackContext){
                    @Override
                    public void run(){
                        try {
                            SQLiteDatabase dbConn = attemptToConnect(true, this.dbPath, this.ctx);
                            this.resultData = read(dbConn, this.options);
                            dbConn.close();
                            processResponse(this.ctx, true, "Consulta realizada", resultData, null);                            
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                if(async){
                    cordova.getThreadPool().execute(readTask);
                }
                else{
                    cordova.getActivity().runOnUiThread(readTask);
                }
            break;
            case update:
                this.processResponse(callbackContext, false, "update Not Yet Implemented", null, null);
            break;
            case delete:
                this.processResponse(callbackContext, false, "delete Not Yet Implemented", null, null);
            break;
            case execute:
                ScheduleTask executeTask = new ScheduleTask(options, dbPath, callbackContext) {
                    public void run(){
                        try {
                            SQLiteDatabase dbConn = attemptToConnect(false, this.dbPath, this.ctx);
                            execute(dbConn, options);
                            dbConn.close();
                            processResponse(this.ctx, true, "SQL Executed", null, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                if(async){
                    cordova.getThreadPool().execute(executeTask);
                }
                else{
                    cordova.getActivity().runOnUiThread(executeTask);
                }                
            break;
//          case connect:
//               
//          break;
//          case disconnect:
//               
//          break;             
            default:
                this.processResponse(callbackContext, false, "Some parameters were missed - Action not found -", null, null);
                result = false;
            break;
        }

//        String msg  = (result)?actionType+" Operation success":actionType+" Operation fail";
//        this.processResponse(callbackContext, result, msg, null, null);
        return result;
    }

    /**
     * 
     * @param ctx               The plugin CallbackContext
     * @param success           boolean that define if the JS plugin should fire the success or error function
     * @param msg               The String msg to by send
     * @throws JSONException
     */
    private void processResponse(CallbackContext ctx, boolean success, String msg, JSONArray data, JSONObject extra) throws JSONException{
        JSONObject response = new JSONObject();
        response.put("success", success);
        response.put("message", msg);
        if(data != null){
            response.put("data", data);
        }
        if(extra != null){
            response.put("extra", extra);
        }
        
        if(success){
            ctx.success(response);
        }
        else{
            ctx.error(response);
        }
    }
    
    private boolean verifyParams(JSONObject options, String dbPath, CallbackContext ctx) throws JSONException{
        boolean result      = true;
        if(options==null || dbPath==null){
            result          = false;
            String msg      = (options==null)?"option object":"database path";
            this.processResponse(ctx, false, "Some parameters were missed - Missed "+msg+" -", null, null);
        }

        File dbFileCheck    = new File(dbPath);
        if(!dbFileCheck.exists()){
            result          = false;
            this.processResponse(ctx, false, "The selected db filed is missed", null, null);
        }
        
        return result;
    }
    
    private SQLiteDatabase attemptToConnect(boolean isReadOnly, String dbPath, CallbackContext ctx) throws JSONException{
        int openFlag            = (isReadOnly)?SQLiteDatabase.OPEN_READONLY:SQLiteDatabase.OPEN_READWRITE;
        SQLiteDatabase dbConn   = SQLiteDatabase.openDatabase(dbPath, null, openFlag | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

        if(!dbConn.isOpen()){
            this.processResponse(ctx, false, "Could not open the DB File", null, null);
        }

        return dbConn;
    }
    
    private JSONArray read(SQLiteDatabase dbConn, JSONObject options) throws JSONException{
        queryBuilder qb         = new queryBuilder(this.cordova.getActivity().getApplicationContext());
        String query            = qb.getQuery(options.optString("sqlFilePath"), options.optJSONArray("queryParams"));
        Cursor result           = dbConn.rawQuery(query, null);
        JSONArray data          = cursorToArray(result);
        
        return data;
    }
    
    private void execute(SQLiteDatabase dbConn, JSONObject options){ 
        queryBuilder qb         = new queryBuilder(this.cordova.getActivity().getApplicationContext());
        String query            = qb.getQuery(options.optString("sqlFilePath"), options.optJSONArray("queryParams"));
        String[] queryArr       = null;
        if(options.optBoolean("multiple")){
            queryArr            = query.split(";");
        }
        else{
            queryArr            = new String[]{query};
        }
        for (String item : queryArr) {
            dbConn.execSQL(item);
        }
    }
    
    private JSONArray cursorToArray(Cursor cursor) throws JSONException{

        JSONArray data          = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String[] cols       = cursor.getColumnNames();
            JSONObject row      = new JSONObject();
            for (int i = 0; i < cols.length; i++) {
                row.put(cols[i], cursor.getString(i));
            }
            data.put(row);
            cursor.moveToNext();
        }

        return data;
    }
}
