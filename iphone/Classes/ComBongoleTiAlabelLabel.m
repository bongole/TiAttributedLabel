/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "ComBongoleTiAlabelLabel.h"
#import "TiUtils.h"
#import <objc/runtime.h>

static NSDictionary *labelOptionDispatcher =nil;

@implementation ComBongoleTiAlabelLabel

-(UILabel*)label
{
    UILabel *label = nil;
    object_getInstanceVariable(self, "label", (void *)&label);
    
    if (label==nil)
    {
        OHAttributedLabel *l = [[OHAttributedLabel alloc] initWithFrame:CGRectZero];
        l.delegate = self;
        
        label = l;
        label.backgroundColor = [UIColor clearColor];
        label.numberOfLines = 0;
        [self addSubview:label];
        self.clipsToBounds = YES;
        
        
        object_setInstanceVariable(self, "label", label);
    }
    
    return label;
}

-(BOOL)attributedLabel:(OHAttributedLabel*)attributedLabel shouldFollowLink:(NSTextCheckingResult*)linkInfo
{
    NSString *url = [linkInfo.extendedURL absoluteString];
    NSDictionary *e = @{@"src": url };
    
    [self.proxy fireEvent:@"link" withObject:e];
    
    return NO;
}

-(CGSize)sizeForFont:(CGFloat)suggestedWidth
{
    NSMutableAttributedString *value = [[self proxy] valueForKey:@"attributedText__"];
    
    if( value == nil ){
        return [super sizeForFont:suggestedWidth];
    }
    else{
        CGSize maxSize = CGSizeMake(suggestedWidth<=0 ? 480 : suggestedWidth, 10000);
        CGSize shadowOffset = [[self label] shadowOffset];
        
        object_setInstanceVariable(self, "requiresLayout", YES);
        
        if ((suggestedWidth > 0) && [[value string] hasSuffix:@" "]) {
            // (CGSize)sizeWithFont:(UIFont *)font constrainedToSize:(CGSize)size lineBreakMode:(UILineBreakMode)lineBreakMode method truncates
            // the string having trailing spaces when given size parameter width is equal to the expected return width, so we adjust it here.
            maxSize.width += 0.00001;
        }
        
        //CGSize size = [value sizeWithFont:font constrainedToSize:maxSize lineBreakMode:UILineBreakModeTailTruncation];
        CGSize size = [value sizeConstrainedToSize:maxSize];
        
        if (shadowOffset.width > 0)
        {
            // if we have a shadow and auto, we need to adjust to prevent
            // font from clipping
            size.width += shadowOffset.width + 10;
        }
        
        return size;
    }
}

-(void)setAttributedText_:(id)args
{
    ENSURE_SINGLE_ARG(args, NSDictionary);
    
    if( labelOptionDispatcher == nil ){
        labelOptionDispatcher = [@{
            @"font": ^(NSMutableAttributedString *mas, id arg, NSRange r){
                UIFont *font = [[TiUtils fontValue:arg] font];
                [mas setFont:font range:r];
            
                NSString *color = [arg objectForKey:@"color"];
                if (color) {
                    UIColor *c = [[TiUtils colorValue:color] color];
                    [mas setTextColor:c range:r];
                }
            },
            @"a": ^(NSMutableAttributedString *mas, id arg, NSRange r){
                NSString *url = [arg objectForKey:@"href"];
                if (url) {
                    [mas setLink:[NSURL URLWithString:url] range:r];
                }
            }
         } retain];
    }
    
    OHAttributedLabel *l = (OHAttributedLabel *)[self label];
    
    NSString *txt = [args objectForKey:@"text"];
    
    NSMutableAttributedString *mas = [NSMutableAttributedString attributedStringWithString:txt];
    
    NSArray *options = [args objectForKey:@"attributes"];
    if ( options != nil ) {
        for (NSArray *opt in options) {
            NSNumber *pos = [opt objectAtIndex:0];
            NSNumber *len = [opt objectAtIndex:1];
            NSDictionary *inner_option = [opt objectAtIndex:2];
            
            for(NSString *key in [inner_option allKeys]){
                CaseBlock c = [labelOptionDispatcher objectForKey:key];
                if( c ){
                    c(mas, [inner_option objectForKey:key], NSMakeRange([pos unsignedIntegerValue], [len unsignedIntegerValue]));
                }
            }
        }
    }
    
    l.attributedText = mas;
    
    [[self proxy] setValue:mas forKey:@"attributedText__"];
    
    [(TiViewProxy *)[self proxy] contentsWillChange];
}


@end
