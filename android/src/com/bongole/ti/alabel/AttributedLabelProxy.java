package com.bongole.ti.alabel;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiContext;

import android.app.Activity;

@Kroll.proxy(creatableInModule=AndroidModule.class, propertyAccessors = {
	TiC.PROPERTY_AUTO_LINK,
	TiC.PROPERTY_COLOR,
	TiC.PROPERTY_ELLIPSIZE,
	TiC.PROPERTY_FONT,
	TiC.PROPERTY_HIGHLIGHTED_COLOR,
	TiC.PROPERTY_HTML,
	TiC.PROPERTY_TEXT,
	TiC.PROPERTY_TEXT_ALIGN,
	TiC.PROPERTY_TEXTID,
	TiC.PROPERTY_WORD_WRAP,
	TiC.PROPERTY_VERTICAL_ALIGN,
	"attributedText"
})
public class AttributedLabelProxy extends TiViewProxy
{
	public AttributedLabelProxy()
	{
		defaultValues.put(TiC.PROPERTY_TEXT, "");
	}

	public AttributedLabelProxy(TiContext tiContext)
	{
		this();
	}

	@Override
	protected KrollDict getLangConversionTable()
	{
		KrollDict table = new KrollDict();
		table.put(TiC.PROPERTY_TEXT, TiC.PROPERTY_TEXTID);
		return table;
	}

	@Override
	public TiUIView createView(Activity activity)
	{
        return new AttributedLabel(this);                                                                                                                
	}
}