/*
 * The MIT License (MIT)
 * Copyright (c) 2015 Joel De La Torriente - jjdltc - https://github.com/jjdltc
 * See a full copy of license in the root folder of the project
 */

#import "JJdbFile.h"
#import "queryBuilder.h"
#import <Cordova/CDV.h>
#import <FMDB/FMDatabase.h>

@implementation JJdbFile

- (void)read:(CDVInvokedUrlCommand*)command {
    NSData* actionOptions   = [command argumentAtIndex:0];
    NSNumber* sync          = [command argumentAtIndex:1];
    NSString* dbPath        = [[command argumentAtIndex:2] stringByReplacingOccurrencesOfString:@"file://" withString:@""];
    NSNumber* useSqlFile    = [actionOptions valueForKey:@"useSqlFile"];
    NSNumber* multiple      = [actionOptions valueForKey:@"multiple"];
    NSString* sqlFilePath   = [actionOptions valueForKey:@"sqlFilePath"];
    NSArray* queryParams    = [actionOptions valueForKey:@"queryParams"];

    NSDictionary* actionDictionary = @{
                                       @"useSqlFile"     : (useSqlFile==nil)?@NO:useSqlFile,
                                       @"multiple"       : (multiple==nil)?@NO:multiple,
                                       @"sqlFilePath"    : sqlFilePath,
                                       @"queryParams"    : queryParams,
                                       @"dbPath"         : dbPath,
                                       @"ctx"            : command
                                       };
    
    if([sync isEqual:@YES]){
        SEL doRead = @selector(doRead:);
        [self performSelectorInBackground:doRead withObject:actionDictionary];
    }
    else{
        [self doRead:actionDictionary];
    }
}

- (void)doRead :(NSDictionary*)actionDictionary{
//    BOOL useSqlFile             = [actionDictionary valueForKey:@"useSqlFile"];
//    BOOL multiple               = [actionDictionary valueForKey:@"multiple"];
    NSString* queryFilePath         = [actionDictionary valueForKey:@"sqlFilePath"];
    NSString* dbPath                = [actionDictionary valueForKey:@"dbPath"];
    NSMutableArray* queryParams     = [actionDictionary valueForKey:@"queryParams"];
    NSString* query                 = [queryBuilder getQuery:queryFilePath :queryParams];
    CDVInvokedUrlCommand* command   = [actionDictionary valueForKey:@"ctx"];
    
    FMDatabase *database = [FMDatabase databaseWithPath:dbPath];
    [database open];
    NSMutableArray *result = [NSMutableArray array];
    
    FMResultSet *resultsWithNameLocation = [database executeQuery:query];
    while([resultsWithNameLocation next]) {
        NSDictionary *actualRow = [resultsWithNameLocation resultDictionary];
        [result addObject:actualRow];
    }
    
    [database close];
    
    NSDictionary *responseObj = @{
                                  @"success" : @1,
                                  @"message" : @"-",
                                  @"data"    : result
                                  };

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:responseObj];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)execute:(CDVInvokedUrlCommand*)command {
    NSData* actionOptions   = [command argumentAtIndex:0];
    NSNumber* sync          = [command argumentAtIndex:1];
    NSString* dbPath        = [[command argumentAtIndex:2] stringByReplacingOccurrencesOfString:@"file://" withString:@""];
    NSNumber* useSqlFile    = [actionOptions valueForKey:@"useSqlFile"];
    NSNumber* multiple      = [actionOptions valueForKey:@"multiple"];
    NSString* sqlFilePath   = [actionOptions valueForKey:@"sqlFilePath"];
    NSArray* queryParams    = [actionOptions valueForKey:@"queryParams"];
    
    NSDictionary* actionDictionary = @{
                                       @"useSqlFile"     : (useSqlFile==nil)?@NO:useSqlFile,
                                       @"multiple"       : (multiple==nil)?@NO:multiple,
                                       @"sqlFilePath"    : sqlFilePath,
                                       @"queryParams"    : queryParams,
                                       @"dbPath"         : dbPath,
                                       @"ctx"            : command
                                       };
    
    if([sync isEqual:@YES]){
        SEL doExecute = @selector(doExecute:);
        [self performSelectorInBackground:doExecute withObject:actionDictionary];
    }
    else{
        [self doExecute:actionDictionary];
    }
}

- (void*)doExecute:(NSDictionary*)actionDictionary{
    //    BOOL useSqlFile             = [actionDictionary valueForKey:@"useSqlFile"];
    NSNumber* multiple              = [actionDictionary valueForKey:@"multiple"];
    NSString* queryFilePath         = [actionDictionary valueForKey:@"sqlFilePath"];
    NSString* dbPath                = [actionDictionary valueForKey:@"dbPath"];
    NSMutableArray* queryParams     = [actionDictionary valueForKey:@"queryParams"];
    NSString* query                 = [queryBuilder getQuery:queryFilePath :queryParams];
    NSArray* queryArray             = @[query];
    CDVInvokedUrlCommand* command   = [actionDictionary valueForKey:@"ctx"];
    
    if([multiple isEqual:@YES]){
        queryArray = [query componentsSeparatedByString:@";"];
    }
    
    FMDatabase *database = [FMDatabase databaseWithPath:dbPath];
    [database open];
    NSMutableArray *result = [NSMutableArray array];
    
    BOOL completeAll = @YES;
    for(NSString* item in queryArray){
        BOOL executionResult = [database executeUpdate:item];
        if(!executionResult){
            completeAll = @NO;
        }
    }
    
    [database close];
    
    NSDictionary *responseObj = @{
                                  @"success" : [NSNumber numberWithBool:completeAll],
                                  @"message" : @"-",
                                  @"data"    : result
                                  };
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:responseObj];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


- (void)jsEvent:(NSString*)event:(NSString*)data{
    NSString *eventStrig = [NSString stringWithFormat:@"cordova.fireDocumentEvent('%@'", event];
    // NSString *eventStrig = [NSString stringWithFormat:@"console.log('%@'", event];
    
    if(data != nil){
        eventStrig = [NSString stringWithFormat:@"%@,%@", eventStrig, data];
    }
    
    eventStrig = [eventStrig stringByAppendingString:@");"];
    
    [self.commandDelegate evalJs:eventStrig];
}

- (NSString*) dictionaryToJSONString:(NSDictionary*)toCast{
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:toCast options:NSJSONWritingPrettyPrinted error:&error];
    if(!jsonData){
        return nil;
    }
    else{
        return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    }
}

@end
