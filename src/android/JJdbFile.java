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
    private JSONObject options      = null;
    private String dbPath           = null;
    private CallbackContext ctx     = null;
    private SQLiteDatabase dbConn   = null;
    
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
        String actionType       = "";
        this.options            = args.optJSONObject(0);
        this.dbPath             = args.optString(1);
        this.ctx                = callbackContext;
        JSONArray resultData    = null;
        
        result                  = this.verifyParams();
        
        switch (ACTIONS.valueOf(action)) {
            case create:
                this.processResponse(callbackContext, false, "create Not Yet Implemented", null, null);
                this.attemptToConnect(false);
            break;
            case read:
                this.attemptToConnect(true);
                resultData = this.read();
                this.processResponse(callbackContext, true, "Consulta realizada", resultData, null);
            break;
            case update:
                this.processResponse(callbackContext, false, "update Not Yet Implemented", null, null);
                this.attemptToConnect(false);
            break;
            case delete:
                this.processResponse(callbackContext, false, "delete Not Yet Implemented", null, null);
                this.attemptToConnect(false);
            break;
            case execute:
                this.processResponse(callbackContext, false, "execute Not Yet Implemented", null, null);
                this.attemptToConnect(false);
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
        
        if(this.dbConn.isOpen()){
            this.dbConn.close();
        }

        String msg  = (result)?actionType+" Operation success":actionType+" Operation fail";
        this.processResponse(callbackContext, result, msg, null, null);
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
    
    private boolean verifyParams() throws JSONException{
        boolean result      = true;
        if(this.options==null || this.dbPath==null){
            result          = false;
            String msg      = (this.options==null)?"option object":"database path";
            this.processResponse(this.ctx, false, "Some parameters were missed - Missed "+msg+" -", null, null);
        }

        File dbFileCheck    = new File(this.dbPath);
        if(!dbFileCheck.exists()){
            result          = false;
            this.processResponse(this.ctx, false, "The selected db filed is missed", null, null);
        }
        
        return result;
    }
    
    private void attemptToConnect(boolean isReadOnly) throws JSONException{
        int openFlag            = (isReadOnly)?SQLiteDatabase.OPEN_READONLY:SQLiteDatabase.OPEN_READWRITE;
        SQLiteDatabase dbConn   = SQLiteDatabase.openDatabase(this.dbPath, null, openFlag | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

        if(!dbConn.isOpen()){
            this.processResponse(this.ctx, false, "Could not open the DB File", null, null);
        }
        
        this.dbConn             = dbConn;
    }
    
    private JSONArray read() throws JSONException{
        queryBuilder qb         = new queryBuilder(this.cordova.getActivity().getApplicationContext());
        String query            = qb.getQuery(this.options.optString("sqlFilePath"), this.options.optJSONArray("queryParams"));
        Cursor result           = this.dbConn.rawQuery(query, null);
        JSONArray data          = cursorToArray(result);
        
        return data;
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
