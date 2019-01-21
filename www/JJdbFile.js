/*
 * The MIT License (MIT)
 * Copyright (c) 2015 Joel De La Torriente - jjdltc - http://www.jjdltc.com/
 * See a full copy of license in the root folder of the project
 */

var argscheck       = require('cordova/argscheck'),
    exec            = require('cordova/exec');

/**
 * @TODO Desc
 * 
 * @constructor
 */
function JJdbFile(path) {
    argscheck.checkArgs('s', 'JJdbFile.constructor', arguments);
    this.dbPath     = path || false;
}

/**
 * @TODO Desc
 */
JJdbFile.prototype.create = function(options, async, successCallback, errorCallback) {
    argscheck.checkArgs('o*FF', 'JJdbFile.create', arguments);
    exec(successCallback, errorCallback, "JJdbFile", "create", [options, async, this.dbPath]);
}

/**
 * @TODO Desc
 */
JJdbFile.prototype.read = function(options, async, successCallback, errorCallback) {
    argscheck.checkArgs('o*FF', 'JJdbFile.read', arguments);
    exec(successCallback, errorCallback, "JJdbFile", "read", [options, async, this.dbPath]);
}

/**
 * @TODO Desc
 */
JJdbFile.prototype.update = function(options, async, successCallback, errorCallback) {
    argscheck.checkArgs('o*FF', 'JJdbFile.update', arguments);
    exec(successCallback, errorCallback, "JJdbFile", "update", [options, async, this.dbPath]);
}

/**
 * @TODO Desc
 */
JJdbFile.prototype.delete = function(options, async, successCallback, errorCallback) {
    argscheck.checkArgs('o*FF', 'JJdbFile.delete', arguments);
    exec(successCallback, errorCallback, "JJdbFile", "delete", [options, async, this.dbPath]);
}

/**
 * @TODO Desc
 */
JJdbFile.prototype.execute = function(options, async, successCallback, errorCallback) {
    argscheck.checkArgs('o*FF', 'JJdbFile.execute', arguments);
    exec(successCallback, errorCallback, "JJdbFile", "execute", [options, async, this.dbPath]);
}

module.exports = JJdbFile;