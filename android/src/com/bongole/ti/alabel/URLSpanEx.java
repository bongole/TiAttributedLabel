package com.bongole.ti.alabel;

import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class URLSpanEx extends URLSpan implements OnClickListener {

	private OnClickListener onClickListener = null;
	
	public URLSpanEx(String src) {
		super(src);
	}
	
	public void setOnClickListener(OnClickListener clickListener) {
		this.onClickListener = clickListener;
	}

	@Override
	public void onClick(View widget) {
		if( this.onClickListener != null ){
			this.onClickListener.onClick(widget);
		}
	}
}
