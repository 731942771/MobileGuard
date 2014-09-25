package com.cuiweiyou.mobileguard;

import com.cuiweiyou.mobileguard.ui.SettingItemRelativeLayout;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * ~~~第5次提交，“设置中心”界面~~~/**<br/>
 */
public class SettingActivity extends Activity {
	
	/** 自定义组合控件，“设置中心”的条目 **/
	private SettingItemRelativeLayout sirl_setting_item;
	/** 配置存储器 **/
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		// (1)创建私有配置文件（“文件名”， 私有模式）
		sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
		
		/**
		！4）~~~~~~~~~~~~~~~~~~~~~自定义控件 4 ~~~~~~~~~~~~~~~~~~~~~~~~~
		**/
		/** 1.找到自定义类创建的组合控件 **/
		sirl_setting_item = (SettingItemRelativeLayout) findViewById(R.id.sirl_setting_item);
		
		
		// 查看上一次配置，更新控件
		boolean autoupdate = sp.getBoolean("autoupdate", false);
		if(autoupdate){
			sirl_setting_item.setChecked(true);
		} else {
			sirl_setting_item.setChecked(false);
		}
		
		
		/** 2.点击此控件，操作子控件 **/
		sirl_setting_item.setOnClickListener(new OnClickListener() {
			/** 3.实现点击处理 **/
			@Override
			public void onClick(View v) {
				// (2)得到编辑器
				Editor edit = sp.edit();
				/** 4.切换配置 **/
				if(sirl_setting_item.isChecked()){		// 如果原为true，点击后为false
					sirl_setting_item.setChecked(false);
					// (3)保存配置（“key”，值）
					edit.putBoolean("autoupdate", false);
				}else{									// 如果原为false，点击后为true
					sirl_setting_item.setChecked(true);
					// (3)保存配置
					edit.putBoolean("autoupdate", true);
				}
				// (4)提交数据！！！
				edit.commit();
			}
		});

	}
	
}
