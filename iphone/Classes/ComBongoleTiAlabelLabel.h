/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
#import "TiUIView.h"
#import "TiUILabel.h"
#import "OHAttributedLabel.h"

typedef void (^CaseBlock)(NSMutableAttributedString *mas, id arg, NSRange r);

@interface ComBongoleTiAlabelLabel : TiUILabel<OHAttributedLabelDelegate> {
}

-(UILabel*)label;

@end
