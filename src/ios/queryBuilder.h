/*
 * The MIT License (MIT)
 * Copyright (c) 2015 Joel De La Torriente - jjdltc - https://github.com/jjdltc
 * See a full copy of license in the root folder of the project
 */

@interface queryBuilder

- (NSString*)getQuery :(NSString*)queryFilePath :(NSMutableArray*)replaceParams;

@end
