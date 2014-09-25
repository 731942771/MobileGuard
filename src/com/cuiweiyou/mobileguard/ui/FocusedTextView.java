package com.cuiweiyou.mobileguard.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/***
 * ~~~第5次提交
 * @author Administrator
 * 自定义的滚动条控件
 */
public class FocusedTextView extends TextView {

	// 此处右键，source，Generate Constructors from Superclass...
	// 重新3个构造方法
		
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/** 
	 * 关键所在
	 * 无论控件事实上是否真的获得了焦点，都返回true
	 * 原本是当获取焦点才返回true，才能根据这个“true”实现滚动
	 * 直接返回true，欺骗系统
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// return super.isFocused();
		return true;
	}

}
