/*
 * The MIT License (MIT)
 * Copyright (c) 2015 Joel De La Torriente - jjdltc - https://github.com/jjdltc
 * See a full copy of license in the root folder of the project
 */

#import "queryBuilder.h"
//@TODO => Review the complete file, it may fail because of no consideration of edge cases

@implementation queryBuilder

- (NSString*)getQuery :(NSString*)queryFilePath :(NSMutableArray*)replaceParams{
    queryFilePath = [queryFilePath stringByReplacingOccurrencesOfString:@"file://" withString:@""];
    queryFilePath = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:queryFilePath];

    NSString* query = nil;
    BOOL doesAssetExists = [self doesAssetExists:queryFilePath];

    if(!doesAssetExists){
        return query;
    }
    
    query = [self getRawQuery:queryFilePath];
    
    if(query == nil || [query length]==0){
        return query;
    }
    
    query = [self buildFormattedQuery:query :replaceParams];
    
    return query;
}

- (BOOL)doesAssetExists:(NSString*)path{
    BOOL doesPathExist = [[NSFileManager defaultManager] fileExistsAtPath:path];
    return doesPathExist;
}

- (NSString*)getRawQuery:(NSString*)queryFilePath{
    NSString* result = [NSString stringWithContentsOfFile:queryFilePath
                                                  encoding:NSUTF8StringEncoding
                                                     error:NULL];
    return result;
}

- (NSString*)buildFormattedQuery :(NSString*)rawQuery :(NSMutableArray*)replaceParams{
    NSInteger idx = 1;
    for(id item in replaceParams){
        NSString* toSeek = [@"%" stringByAppendingString:[NSString stringWithFormat:@"%ld$s",(long)idx]];
        NSString* replaceWith = ([item isKindOfClass:[NSString class]])
            ?item
            :[item stringValue];

        rawQuery = [rawQuery stringByReplacingOccurrencesOfString:toSeek withString:replaceWith];
        idx++;
    }
    
    return rawQuery;
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
