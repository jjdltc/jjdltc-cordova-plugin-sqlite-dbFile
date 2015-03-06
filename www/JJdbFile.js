/*
 * The MIT License (MIT)
 * Copyright (c) 2015 Joel De La Torriente - jjdltc - http://www.jjdltc.com/
 * See a full copy of license in the root folder of the project
 */

var argscheck       = require('cordova/argscheck'),
    exec            = require('cordova/exec'),
    cordova         = require('cordova');

var validateOptObj  = function(){

};

/**
 * @TODO Desc
 * 
 * @constructor
 */
function JJdbFile(path) {
    argscheck.checkArgs('s', 'JJdbFile.constructor', arguments);
    this.dbPath     = path || false;
}

// /**
//  * Attemp to connect with the db file in the device
//  */
// JJdbFile.prototype.connect = function(options, successCallback, errorCallback) {
//     argscheck.checkArgs('oFF', 'JJdbFile.connect', arguments);
//     exec(successCallback, errorCallback, "JJdbFile", "connect", [options]);
// };

// /**
//  * @TODO Desc
//  */
// JJdbFile.prototype.disconnect = function(options, successCallback, errorCallback) {
//     argscheck.checkArgs('oFF', 'JJdbFile.disconnect', arguments);
//     exec(successCallback, errorCallback, "JJdbFile", "disconnect", [options]);
// };

/**
 * @TODO Desc
 */
JJdbFile.prototype.create = function(options, successCallback, errorCallback) {
    argscheck.checkArgs('oFF', 'JJdbFile.create', arguments);
    exec(successCallback, errorCallback, "JJdbFile", "create", [options, this.dbPath]);
}

/**
 * @TODO Desc
 */
JJdbFile.prototype.read = function(options, successCallback, errorCallback) {
    argscheck.checkArgs('oFF', 'JJdbFile.read', arguments);
    exec(successCallback, errorCallback, "JJdbFile", "read", [options, this.dbPath]);
}

/**
 * @TODO Desc
 */
JJdbFile.prototype.update = function(options, successCallback, errorCallback) {
    argscheck.checkArgs('oFF', 'JJdbFile.update', arguments);
    exec(successCallback, errorCallback, "JJdbFile", "update", [options, this.dbPath]);
}

/**
 * @TODO Desc
 */
JJdbFile.prototype.delete = function(options, successCallback, errorCallback) {
    argscheck.checkArgs('oFF', 'JJdbFile.delete', arguments);
    exec(successCallback, errorCallback, "JJdbFile", "delete", [options, this.dbPath]);
}

/**
 * @TODO Desc
 */
JJdbFile.prototype.execute = function(options, successCallback, errorCallback) {
    argscheck.checkArgs('oFF', 'JJdbFile.execute', arguments);
    exec(successCallback, errorCallback, "JJdbFile", "execute", [options, this.dbPath]);
}

module.exports = JJdbFile;