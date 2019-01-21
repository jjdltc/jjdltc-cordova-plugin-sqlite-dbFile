<!-- Sqlite File Plugin
===

A simple plugin to access to a Sqlite File in the device (Not to a sqlite db instatieted in the device)

Platforms supported

* android

Easy Ese  
---  
  
The object `JJdbFile` is expose in the `window`:

### Methonds

create
read
update
delete
execute

* `read(options, async [, successCallback, errorCallback])` - Allow to read over the db (Using a sql text file or sending the query itselft)
    * `options` - Compression options in a JS object format (Key:"value")
        * __target__: Path/To/Place/Result
        * __name__: Name of the resulted zip (without the .zip)
    * `async` - Flag to decide if the action will lock or not the main thread
    * `successCallback` - Function to call in plugin success
    * `errorCallback` - Function to call in plugin error
* `unzip(file [, options, successCallback, errorCallback])` - Allow to unzip a zip file
    * `file` - Path/To/File.zip (Expect a cordova style path file://)
    * `options` - Extra options in a JS object format (Key:"value")
        * __target__: Path/To/Place/Result
    * `successCallback` - Function to call in plugin success
    * `errorCallback` - Function to call in plugin error  

### Use Example  

To Zip a folder
```
    var PathToFileInString  = cordova.file.externalRootDirectory+"HereIsMyFolder",
        PathToResultZip     = cordova.file.externalRootDirectory;
    JJzip.zip(PathToFileInString, {target:PathToResultZip,name:"SuperZip"},function(data){
        /* Wow everiting goes good, but just in case verify data.success*/
    },function(error){
        /* Wow something goes wrong, check the error.message */
    })
```  

Or To UnZip  

```
    var PathToFileInString  = cordova.file.externalRootDirectory+"HereIsMyFile.zip",
        PathToResultZip     = cordova.file.externalRootDirectory;
    JJzip.unzip(PathToFileInString, {target:PathToResultZip},function(data){
        /* Wow everything goes good, but just in case verify data.success */
    },function(error){
        /* Wow something goes wrong, check the error.message */
    })
```

There is a big TODO list, but in resume  
  
* Write a better documentation
* Add iOS Support (Be Patient)

need to place a the queries in assets (preferred in a folder) -->