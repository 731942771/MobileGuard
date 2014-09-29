package com.cuiweiyou.mobileguard.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cuiweiyou.mobileguard.R;

/**
 * ~~~第5次提交~~~<br/>
 * ！2）------------------------自定义控件 2 ---------------------------------------<br/>
 * 根据自定义的“设置中心”界面的条目布局xml文件，创建这个条目布局类<br/>
 * 实现从xml到View的转换<br/>
 * 同时增加一些修改子控件的功能<br/>
 * 后续可以通过调研此类创建出每个条目<br/>
 * 
 * @author Administrator
 */
public class SettingItemRelativeLayout extends RelativeLayout {

	/** 更新提示标题 **/
	private TextView tv_setting_item_autoupdate;
	/** 更新提示信息 **/
	private CheckBox cb_setting_item_autoupdate;
	/** 更新控制复选框 **/
	private TextView tv_setting_item_autoupdate_info;
	
	/** 自定义控件的自定义属性-自动更新标题 **/
	private String autoupdate_title;
	/** 自定义控件的自定义属性-开启自动更新时的提示 **/
	private String autoupdate_desc_on;
	/** 自定义控件的自定义属性-关闭自动更新时的提示 **/
	private String autoupdate_desc_off;

	/**
	 * 增强功能-1<br/>
	 * 实现xml转View
	 */
	private void initItem(Context context) {
		/* 
		 * (上下文，xml源，将生成的控件顶替this)，第3个参数！！！
		 * 后续我们调研此类时，也就能使用这个类里的这个自定义的组合View控件，其中有子控件TextView、CheckBox
		 */
		View.inflate(context, R.layout.activity_setting_item, SettingItemRelativeLayout.this);
		
		// 更新提示条目标题
		tv_setting_item_autoupdate = (TextView) findViewById(R.id.tv_setting_item_autoupdate);
		tv_setting_item_autoupdate.setText(autoupdate_title);
		// 更新提示
		tv_setting_item_autoupdate_info = (TextView) findViewById(R.id.tv_setting_item_autoupdate_info);
		// 更新控制复选框
		cb_setting_item_autoupdate = (CheckBox) findViewById(R.id.cb_setting_item_autoupdate);
	}

	/**
	 * 增强功能-2<br/>
	 * 检测复选框是否勾选
	 */
	public boolean isChecked(){
		// 根据复选框的属性
		return cb_setting_item_autoupdate.isChecked();
	}
	
	/**
	 * 增强功能-2<br/>
	 * 检测复选框是否勾选
	 */
	public void setChecked(boolean checked){
		// 调用复选框的方法
		cb_setting_item_autoupdate.setChecked(checked);
		
		// 同时修改提示信息
		/**
		 *  ~~~第6次提交~~~
		 */
		if(checked){
			//tv_setting_item_autoupdate_info.setText("自动更新功能已经打开");
			tv_setting_item_autoupdate_info.setText(autoupdate_desc_on);
		}else{
			//tv_setting_item_autoupdate_info.setText("自动更新功能已经关闭");
			tv_setting_item_autoupdate_info.setText(autoupdate_desc_off);
		}
		
	}
	
	public SettingItemRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initItem(context);
	}

	/**
	 * 在xml布局里调用此类，调用此2个参数的构造方法
	 * @param context
	 * @param attrs
	 */
	public SettingItemRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	    //自定义控件中的schema
		//xmlns:cuiweiyou="http://schemas.android.com/apk/res/com.cuiweiyou.mobileguard"
		
		//自定义控件对应的attrs.xml中的自定义属性
	    //cuiweiyou:autoupdate_title="自动更新"
	    //cuiweiyou:autoupdate_desc_on="自动更新功能已经打开"
	    //cuiweiyou:autoupdate_desc_off="自动更新功能已经关闭"
	    
		/**
		 * ~~~第6次提交~~~
		 * 5~~》动态控制控件的显示
		 */
	    autoupdate_title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.cuiweiyou.mobileguard", "autoupdate_title");
		autoupdate_desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.cuiweiyou.mobileguard", "autoupdate_desc_on");
		autoupdate_desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.cuiweiyou.mobileguard", "autoupdate_desc_off");
		
		initItem(context);
	}

	/**
	 * new处理的对象，调用此1个参数的构造方法
	 * @param context
	 */
	public SettingItemRelativeLayout(Context context) {
		super(context);
		initItem(context);
	}

}
