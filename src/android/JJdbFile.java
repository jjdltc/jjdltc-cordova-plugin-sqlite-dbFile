/*
 * The MIT License (MIT)
 * Copyright (c) 2015 Joel De La Torriente - jjdltc - http://www.jjdltc.com/
 * See a full copy of license in the root folder of the project
 */
package org.jjdltc.cordova.plugin.sqlite;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JJdbFile extends CordovaPlugin {

    private enum ACTIONS {
          connect
        , disconnect
        , create
        , read
        , update
        , delete
        , execute
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
        String actionType       = "";
        // JSONObject validOptions = validOptions(args);
        
        // if(validOptions==null){
        //     this.processResponse(callbackContext, false, "Some parameters were missed - Missed file path -");
        // }
        
        // switch (ACTIONS.valueOf(action)) {
        //     case zip:
        //         actionType              = "compress";
        //         compressZip makeZip     = new compressZip(validOptions);
        //         result                  = makeZip.zip();
        //     break;
        //     case unzip:
        //         actionType              = "decompress";
        //         decompressZip unZip     = new decompressZip(validOptions);
        //         result                  = unZip.unZip();
        //     break;
        //     default:
        //         this.processResponse(callbackContext, false, "Some parameters were missed - Action not found -");
        //         result = false;
        //     break;
        // }

        String msg  = (result)?actionType+" Operation success":actionType+" Operation fail";
        this.processResponse(callbackContext, result, msg);
        return result;
    }

    /**
     * 
     * @param ctx               The plugin CallbackContext
     * @param success           boolean that define if the JS plugin should fire the success or error function
     * @param msg               The String msg to by send
     * @throws JSONException
     */
    private void processResponse(CallbackContext ctx, boolean success, String msg) throws JSONException{
        JSONObject response = new JSONObject();
        response.put("success", success);
        response.put("message", msg);
        if(success){
            ctx.success(response);
        }
        else{
            ctx.error(response);
        }
    }
    
}
