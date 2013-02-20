package com.bongole.ti.alabel;

import java.util.HashMap;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.util.TiUIHelper;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import ti.modules.titanium.ui.widget.TiUILabel;

public class AttributedLabel extends TiUILabel {

	public AttributedLabel(TiViewProxy proxy) {
		super(proxy);
	}

	private void setAttributedText(TextView tv, HashMap<String, Object> attro){
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		
		String txt = (String)attro.get("text");
		SpannableString stxt = new SpannableString(txt);
		
		Object[] attributes = (Object[]) attro.get("attributes");
		for( Object a : attributes ){
			Object[] aa = (Object[])a;
			Integer start = (Integer) aa[0];
			Integer end = (Integer) aa[1];
			HashMap<String, Object> attr_inner = (HashMap<String, Object>) aa[2];
			
			if( attr_inner.containsKey("font") ){
				HashMap<String, Object> font_attr = (HashMap<String, Object>) attr_inner.get("font");
				
				String fontSize = null;
		        String fontWeight = null;
		        String fontFamily = null;
		        String fontColor = null;
		        
		        if (font_attr.containsKey("fontSize")) {
		            fontSize = TiConvert.toString(font_attr, "fontSize");
		        }
		        if (font_attr.containsKey("fontWeight")) {
		            fontWeight = TiConvert.toString(font_attr, "fontWeight");
		        }
		        if (font_attr.containsKey("fontFamily")) {
		            fontFamily = TiConvert.toString(font_attr, "fontFamily");
		        }
		        if (font_attr.containsKey("color")) {
		            fontColor = TiConvert.toString(font_attr, "color");
		        }
		        
		        if( fontFamily != null ){
			        stxt.setSpan(new CustomTypefaceSpan(fontFamily, TiUIHelper.toTypeface(tv.getContext(),fontFamily)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        }
		        
		        if( fontWeight != null ){
			        stxt.setSpan(new StyleSpan(TiUIHelper.toTypefaceStyle(fontWeight)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        }
		        
		        if( fontSize != null ){
		        	stxt.setSpan(new AbsoluteSizeSpan((int)TiUIHelper.getRawSize(fontSize, tv.getContext())), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        }
		        
		        if( fontColor != null ){
		        	stxt.setSpan(new ForegroundColorSpan(TiConvert.toColor(fontColor)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        }
			}
			
			if( attr_inner.containsKey("a") ){
				HashMap<String, Object> a_attr = (HashMap<String, Object>) attr_inner.get("a");
				String href = (String)a_attr.get("href");
				if( href != null ){
					final URLSpanEx urlspan = new URLSpanEx(href);
					urlspan.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							KrollDict event = new KrollDict(); 
							event.put("src", urlspan.getURL());
							
							proxy.fireEvent("link", event);
						}
					});
					
		        	stxt.setSpan(urlspan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		
		tv.setText(revertSpans(stxt));
	}
	
	final Spannable revertSpans(Spanned stext) {
	    Object[] spans = stext.getSpans(0, stext.length(), Object.class);
	    Spannable ret = Spannable.Factory.getInstance().newSpannable(stext.toString());
	    if (spans != null && spans.length > 0) {
	        for(int i = spans.length - 1; i >= 0; --i) {
	            ret.setSpan(spans[i], stext.getSpanStart(spans[i]), stext.getSpanEnd(spans[i]), stext.getSpanFlags(spans[i]));
	        }
	    }

	    return ret;
	}
	
	@Override
	public void processProperties(KrollDict d) {
		super.processProperties(d);
		
		TextView tv = (TextView)this.getNativeView();
		
		if( d.containsKey("attributedText") ){
			HashMap<String, Object> attro = d.getKrollDict("attributedText");
			setAttributedText(tv, attro);
			
			tv.invalidate();
		}
	}
	
	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue,
			KrollProxy proxy) {
		super.propertyChanged(key, oldValue, newValue, proxy);
		
		if( key.equals("attributedText")){
			TextView tv = (TextView)this.getNativeView();
			
			setAttributedText(tv, (HashMap<String, Object>) newValue);
			
			tv.invalidate();	
		}
	}
}
