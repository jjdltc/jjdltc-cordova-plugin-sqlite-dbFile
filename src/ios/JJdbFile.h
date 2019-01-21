/*
 * The MIT License (MIT)
 * Copyright (c) 2015 Joel De La Torriente - jjdltc - https://github.com/jjdltc
 * See a full copy of license in the root folder of the project
 */

#import <Cordova/CDVPlugin.h>

@interface JJdbFile : CDVPlugin

- (void)read:(CDVInvokedUrlCommand*)command;

- (void)execute:(CDVInvokedUrlCommand*)command;

@end
