Sqlite File plugin
===

A simple plugin to access to a Sqlite File in the device (Not to a sqlite db instantiated in the device nor to create one)

__Contributors are welcome.__

Platforms supported
* android
* iOS (Partial)


Installation
---

`cordova plugin add cordova-sqlite-file-plugin`

Easy Use  
---  

The object `JJdbFile` is expose in the `window`:

### Methonds
* `read(options, async, successCallback, errorCallback)`
    * `options` {Object} - 
        * __useSqlFile__: {Boolean} - Define if use a SQL file or a SQL string
        * __sqlFilePath__: {String} - Path to the SQL file (By now is relative to the asset folder)
        * __queryParams__: {Array} - Params to replaces in the SQL sentence
        * __multiple__: {Boolean} - If true, split the sentence in each `;` and execute in order
    * `async` {Boolean} - Define if should use the main thread or not
    * `successCallback` {Function} - Function to call in plugin success
    * `errorCallback` {Function} - Function to call in plugin fail
* `execute(options, async, successCallback, errorCallback)`
    * `options` {Object} - 
        * __useSqlFile__: {Boolean} - Define if use a SQL file or a SQL string
        * __sqlFilePath__: {String} - Path to the SQL file (By now is relative to the asset folder)
        * __queryParams__: {Array(String|Number)} - Params to replaces in the SQL sentence
        * __multiple__: {Boolean} - If true, split the sentence in each `;` and execute in order
    * `async` {Boolean} - Define if should use the main thread or not
    * `successCallback` {Function} - Function to call in plugin success
        * Params:
            * __response__: {Object} -
                * __success__: {Boolean} -
                * __data__: {Any} -
                * __message__: {String} - 
    * `errorCallback` {Function} - Function to call in plugin fail

### Use Example  

To Read
```
var dbFile      = new JJdbFile("path/to/db/file");
dbFile.read({
    useSqlFile  : true
    sqlFilePath : "Path/To/SQL/File"
    queryParams : []
    multiple    : false
}, true, function(response){
    if(response.success){
        console.log("All Ok")
    }
    console.log("Not Ok, success false")
}, function(){
    console.log("Not Ok, fail")
})
```

There is a big TODO list, but in resume  
  
* Write a better documentation
* Support the queries file locations in others locations and not just assets
* Allow to use SQL sentence (Not just file)
* It could be valid to connect with an instance (Thinking)