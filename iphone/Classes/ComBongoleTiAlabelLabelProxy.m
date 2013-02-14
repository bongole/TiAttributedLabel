/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "ComBongoleTiAlabelLabelProxy.h"
#import "TiUtils.h"

@implementation ComBongoleTiAlabelLabelProxy

-(CGFloat)contentHeightForWidth:(CGFloat)suggestedWidth
{
    CGSize maxSize = CGSizeMake(suggestedWidth<=0 ? 480 : suggestedWidth, 10000);
    NSMutableAttributedString *value = [self valueForKey:@"attributedText__"];
    
    if( value == nil ){
        return [super contentHeightForWidth:suggestedWidth];
    }
    else{
        CGSize size = [value sizeConstrainedToSize:maxSize];
        return [self verifyHeight:size.height]; //Todo: We need to verifyHeight elsewhere as well.
    }
}
@end
